package tamps.cinvestav.se.acciones;

import TiposRecurso.TiposRecurso;
import tamps.cinvestav.se.acciones.pojos.Orden;
import tamps.cinvestav.se.listenerEventos.ws.Ordenes;
import tamps.cinvestav.se.listenerEventos.ws.OrdenesImpl;
import tamps.cinvestav.se.ngineBT.ConexionLego;
import tamps.cinvestav.se.log.Logger;

import java.util.ArrayList;

import static tamps.cinvestav.se.mensajes.FabricaMensajes.crearMensajeOrdenDirecta;
import static tamps.cinvestav.se.util.UtileriasConexionBt.obtenerConexionDeLego;

public class Dictador {
    private ArrayList<ConexionLego> conexiones;

    public Dictador(ArrayList<ConexionLego> conexiones) {
        this.conexiones = conexiones;
    }

    private ArrayList<Orden> obtenerOrdenes() {
        // obtener las órdenes de algún lado
        return OrdenesImpl.getOrdenesPendientes();
    }

    public void informarOrdenes(){
        Logger.log(Logger.DOUBLE_DASHES);
        Logger.log("Iniciando el envío de órdenes a los legos");
        Logger.log(Logger.DASHES);
        ArrayList<Orden> ordenes = obtenerOrdenes();
        if (!ordenes.isEmpty()) {
            Logger.log(String.format("Se han encontrado %d ordenes pendientes", ordenes.size()));
            int i = 1;
            for (Orden orden : ordenes) {
                Logger.log(String.format("Procesando orden %d", i));

                ConexionLego conexion = obtenerConexionDeLego(conexiones, orden.getIdLego());
                if (conexion == null) {
                    throw new RuntimeException("No puedo encontrar la conexión del lego " + orden.getIdLego());
                }

                if (conexion.isConexionAbierta()) {
                    String cadenaOrden = crearMensajeOrdenDirecta(orden.getRecurso(), orden.getAccion());

                    byte[] cadenaEnBytes = cadenaOrden.getBytes();
                    conexion.enviarComando(cadenaEnBytes);
                    Logger.log(String.format("Enviando orden: %s [%s]", orden.toString(), cadenaOrden));
                    String respuestaDesdeLego = conexion.recibirRespuesta();
                    Logger.log(String.format("Respuesta desde lego: %s", respuestaDesdeLego));
                }
                else{
                    Logger.log(String.format("La conexión no está abierta con el lego %d", orden.getIdLego()));
                }
                i++;
            }
        }
        Logger.log(Logger.DASHES);
        Logger.log("Fin del envío de órdenes a los legos");
        Logger.log(Logger.DOUBLE_DASHES);
        System.out.println();
    }
}
