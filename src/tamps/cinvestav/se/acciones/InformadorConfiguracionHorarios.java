package tamps.cinvestav.se.acciones;

import TiposRecurso.TiposRecurso;
import tamps.cinvestav.se.acciones.pojos.ConfiguracionHorario;
import tamps.cinvestav.se.acciones.pojos.OpcionesTimer;
import tamps.cinvestav.se.listenerEventos.ws.ConfiguracionesImpl;
import tamps.cinvestav.se.ngineBT.ConexionLego;
import tamps.cinvestav.se.log.Logger;

import java.util.ArrayList;
import java.util.Calendar;

import static tamps.cinvestav.se.mensajes.FabricaMensajes.crearMensajeTimer;
import static tamps.cinvestav.se.util.UtileriasConexionBt.obtenerConexionDeLego;

public class InformadorConfiguracionHorarios {
    private ArrayList<ConexionLego> conexiones;

    public InformadorConfiguracionHorarios(ArrayList<ConexionLego> conexiones) {
        this.conexiones = conexiones;
    }

    private ArrayList<ConfiguracionHorario> obtenerConfiguraciones() {
       /* // obtener las configuraciones de algún lado
        int idLego = 7;
        ArrayList<ConfiguracionHorario> configuracionesHorario = new ArrayList<ConfiguracionHorario>();

        Calendar hora = Calendar.getInstance();
        hora.set(2014, 12, 14, 8, 0);

        ConfiguracionHorario ch = new ConfiguracionHorario(idLego, TiposRecurso.LUCES, OpcionesTimer.APAGAR, hora.getTime());
        configuracionesHorario.add(ch);

        hora.set(2014, 12, 14, 17, 30);
        ch = new ConfiguracionHorario(idLego, TiposRecurso.LUCES, OpcionesTimer.ENCENDER, hora.getTime());
        configuracionesHorario.add(ch);

        return configuracionesHorario;*/
        return ConfiguracionesImpl.getConfiguracionesPendientes();
    }

    public void informarConfiguraciones(){
        Logger.log(Logger.DOUBLE_DASHES);
        Logger.log("Iniciando el envío de configuración de horas a los legos");
        Logger.log(Logger.DASHES);
        ArrayList<ConfiguracionHorario> configuraciones = obtenerConfiguraciones();
        if (!configuraciones.isEmpty()) {
            Logger.log(String.format("Se han encontrado %d configuraciones pendientes", configuraciones.size()));
            int i = 1;
            for (ConfiguracionHorario configuracion : configuraciones) {
                Logger.log(String.format("Procesando configuración %d", i));

                ConexionLego conexion = obtenerConexionDeLego(conexiones, configuracion.getIdLego());
                if (conexion == null) {
                    throw new RuntimeException("No puedo encontrar la conexión del lego " + configuracion.getIdLego());
                }

                if (conexion.isConexionAbierta()) {
                    String cadenaConf = crearMensajeTimer(configuracion.getRecurso(), configuracion.getSentido(), configuracion.getHora());

                    byte[] cadenaEnBytes = cadenaConf.getBytes();
                    conexion.enviarComando(cadenaEnBytes);
                    Logger.log(String.format("Enviando configuración a lego %d en %s: %s [%s]",
                            conexion.getLego().getId(),
                            conexion.getLego().getMac(),
                            configuracion.toString(),
                            cadenaConf
                    ));

                    String respuestaDesdeLego = conexion.recibirRespuesta();
                    Logger.log(String.format("Respuesta desde lego: %s", respuestaDesdeLego));
                }
                else{
                    Logger.log(String.format("La conexión no está abierta con el lego %d", configuracion.getIdLego()));
                }
                i++;
            }
        }
        Logger.log(Logger.DASHES);
        Logger.log("Fin del envío de configuración de horas a los legos");
        Logger.log(Logger.DOUBLE_DASHES);
        System.out.println();
    }
}
