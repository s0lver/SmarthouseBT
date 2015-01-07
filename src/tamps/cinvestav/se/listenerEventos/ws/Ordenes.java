package tamps.cinvestav.se.listenerEventos.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface Ordenes {
    @WebMethod
    String agregarOrden(int idLego, int idRecurso, int accion);
}
