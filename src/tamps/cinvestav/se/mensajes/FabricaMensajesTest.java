package tamps.cinvestav.se.mensajes;

import TiposRecurso.TiposRecurso;
import org.junit.Test;
import tamps.cinvestav.se.acciones.pojos.Acciones;
import tamps.cinvestav.se.acciones.pojos.OpcionesTimer;

import java.util.Calendar;

public class FabricaMensajesTest {

    private int accionSet = Acciones.ACCION_ORDEN_DIRECTA.getIdentificador();
    private int accionReloj = Acciones.ACCION_RELOJ.getIdentificador();
    private int accionTimer = Acciones.ACCION_TIMER.getIdentificador();
    @Test
    public void testCrearMensajeSet() throws Exception {
        String cadena = FabricaMensajes.crearMensajeOrdenDirecta(TiposRecurso.AIRE_ACONDICIONADO, 0);
        assert cadena.equals(accionSet + "" + 4 + "" + 0);
    }

    @Test
    public void testCrearMensajeReloj() throws Exception {
        Calendar calendario = Calendar.getInstance();
        calendario.set(2014, 12, 14, 23, 17);

        String cadena = FabricaMensajes.crearMensajeReloj(calendario.getTime());
        System.out.println(cadena);
        assert cadena.equals(accionReloj + "2317");
    }

    @Test
    public void testCrearMensajeTimer() throws Exception {
        Calendar calendario = Calendar.getInstance();
        calendario.set(2014, 12, 14, 23, 59);

        String cadena;
        cadena = FabricaMensajes.crearMensajeTimer(TiposRecurso.LUCES, OpcionesTimer.APAGAR, calendario.getTime());

        System.out.println(cadena);
        assert cadena.equals(accionTimer + "3" + "0"+"2359");
    }

    @Test
    public void testCrearMensajeRequest() throws Exception {
        String cadena = FabricaMensajes.crearMensajeRequest(TiposRecurso.ALARMA);

        assert cadena.equals("8");

    }

    @Test
    public void testCrearMensajeNotificacion() throws Exception {
        String cadena = FabricaMensajes.crearMensajeNotificacion(TiposRecurso.ALARMA, 0);

        assert cadena.equals("80");

    }
}