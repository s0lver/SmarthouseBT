package tamps.cinvestav.se.acciones;

import tamps.cinvestav.se.ngineBT.ConexionLego;
import tamps.cinvestav.se.log.Logger;
import tamps.cinvestav.se.mensajes.FabricaMensajes;

import java.util.ArrayList;
import java.util.Date;

public class InformadorHoras {
    private ArrayList<ConexionLego> conexiones;

    public InformadorHoras(ArrayList<ConexionLego> conexiones) {
        this.conexiones = conexiones;
    }

    public void informarHora() {
        Logger.log(Logger.DOUBLE_DASHES);
        Logger.log("Iniciando el envío de la hora actual a los legos");
        Logger.log(Logger.DASHES);
        for (ConexionLego conexion : conexiones) {
            if (conexion.isConexionAbierta()) {
                String cadenaHora = FabricaMensajes.crearMensajeReloj(new Date());
                Logger.log(String.format("Enviando hora: [%s] a lego %d", cadenaHora, conexion.getLego().getId()));
                conexion.enviarComando(cadenaHora.getBytes());

                String respuestaDesdeLego = conexion.recibirRespuesta();
                Logger.log(String.format("Respuesta desde lego: %s", respuestaDesdeLego));

            } else {
                Logger.log(
                        String.format("La conexión con el lego %d, %s no está abierta",
                                conexion.getLego().getId(),
                                conexion.getLego().getTiposlegoByIdTipoLego().getDescripcion())
                );
            }
        }
        Logger.log(Logger.DASHES);
        Logger.log("Fin del envío de la hora actual a los legos");
        Logger.log(Logger.DOUBLE_DASHES);
        System.out.println();
    }
}
