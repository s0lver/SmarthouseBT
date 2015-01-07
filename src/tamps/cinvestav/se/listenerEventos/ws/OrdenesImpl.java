package tamps.cinvestav.se.listenerEventos.ws;

import TiposRecurso.TiposRecurso;
import tamps.cinvestav.se.acciones.pojos.Orden;
import tamps.cinvestav.se.log.Logger;

import javax.jws.WebService;
import java.text.ParseException;
import java.util.ArrayList;

@WebService(endpointInterface = "tamps.cinvestav.se.listenerEventos.ws.Ordenes")
public class OrdenesImpl implements Ordenes{
    private static ArrayList<Orden> ordenesPendientes = new ArrayList<Orden>();

    public static ArrayList<Orden> getOrdenesPendientes() {
        ArrayList<Orden> copia = new ArrayList<Orden>();
        synchronized (ordenesPendientes) {
            for(Orden orden : ordenesPendientes) {
                copia.add(new Orden(orden.getIdLego(),
                                TiposRecurso.getTipoFromIdentificador(orden.getRecurso().getIdentificador()),
                                orden.getAccion())
                );
            }

            ordenesPendientes.clear();
        }
        return copia;
    }

    public static void setOrdenesPendientes(ArrayList<Orden> configuracionesPendientes) {
        OrdenesImpl.ordenesPendientes = configuracionesPendientes;
    }

    @Override
    public String agregarOrden(int idLego, int idRecurso, int accion) {
        Orden orden = new Orden(idLego,
                TiposRecurso.getTipoFromIdentificador(idRecurso),
                accion
        );

        ordenesPendientes.add(orden);
        Logger.log(String.format("Se ha recibido una nueva orden %s", orden.toString()));
        return "ok";
    }
}
