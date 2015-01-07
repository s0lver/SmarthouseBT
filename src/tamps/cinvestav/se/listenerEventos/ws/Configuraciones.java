package tamps.cinvestav.se.listenerEventos.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface Configuraciones {
    @WebMethod
    String agregarConfiguracion(int idLego, int idRecurso, int opcionesTimer, String hora);
}
