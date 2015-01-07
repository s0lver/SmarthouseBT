package tamps.cinvestav.se.acciones;

import tamps.cinvestav.se.acciones.pojos.Cambio;
import tamps.cinvestav.se.log.Logger;
import tamps.cinvestav.se.mensajes.FabricaMensajes;
import tamps.cinvestav.se.ngineBT.ConexionLego;
import tamps.cinvestav.se.util.UtileriasConexionBt;

import java.util.ArrayList;

public class InformadorCambios {
    private ArrayList<ConexionLego> listaConexiones;
    private ArrayList<Cambio> eventos;

    public InformadorCambios(ArrayList<ConexionLego> listaConexiones, ArrayList<Cambio> eventos) {
        this.listaConexiones = listaConexiones;
        this.eventos = eventos;
    }

    public void informarCambios(){
        Logger.log(Logger.DOUBLE_DASHES);
        Logger.log("Iniciando el envío de notificaciones de cambio a los legos");
        Logger.log(Logger.DASHES);
        for(Cambio cambio : eventos) {
            for (ConexionLego conexion : listaConexiones){
                if (conexion.isConexionAbierta()) {
                    String cadenaEvento = FabricaMensajes.crearMensajeNotificacion(cambio.getRecurso(), cambio.getNuevoValor());
                    Logger.log(

                            String.format("Enviando notificación de cambio [%s] a lego %d, [Recurso %d %s, Valor: %d]",
                                    cadenaEvento,
                                    conexion.getLego().getId(),
                                    cambio.getRecurso().getIdentificador(),
                                    cambio.getRecurso().toString(),
                                    cambio.getNuevoValor())
                    );

                    conexion.enviarComando(cadenaEvento.getBytes());

                    String respuestaDesdeLego = conexion.recibirRespuesta();
                    Logger.log(String.format("Respuesta desde lego: %s", respuestaDesdeLego));

                }else{
                    Logger.log(
                            String.format("La conexión con el lego %d, %s no está abierta",
                                    conexion.getLego().getId(),
                                    conexion.getLego().getTiposlegoByIdTipoLego().getDescripcion())
                    );
                }
            }
        }
        Logger.log(Logger.DASHES);
        Logger.log("Fin del envío de notificaciones de cambio a los legos");
        Logger.log(Logger.DOUBLE_DASHES);
        System.out.println();
    }
}
