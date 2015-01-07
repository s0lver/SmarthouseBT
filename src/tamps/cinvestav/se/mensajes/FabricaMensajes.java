package tamps.cinvestav.se.mensajes;

import TiposRecurso.TiposRecurso;
import tamps.cinvestav.se.acciones.pojos.Acciones;
import tamps.cinvestav.se.acciones.pojos.OpcionesTimer;

import java.util.Calendar;
import java.util.Date;

public class FabricaMensajes {

    public static String crearMensajeOrdenDirecta(TiposRecurso recurso, int accion) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf(Acciones.ACCION_ORDEN_DIRECTA.getIdentificador()));
        sb.append(String.valueOf(recurso.getIdentificador()));
        sb.append(String.valueOf(accion));

        return sb.toString();
    }

    public static String crearMensajeReloj(Date hora) {
        Calendar calendario  = Calendar.getInstance();
        calendario.setTime(hora);

        int horas = calendario.get(Calendar.HOUR_OF_DAY);
        int minutos = calendario.get(Calendar.MINUTE);

        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf(Acciones.ACCION_RELOJ.getIdentificador()));

        sb.append(colocarCerosAIzquierda(String.valueOf(horas)));
        sb.append(colocarCerosAIzquierda(String.valueOf(minutos)));

        return sb.toString();
    }

    public static String crearMensajeTimer(TiposRecurso recurso, OpcionesTimer opcionTimer, Date hora) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(hora);

        int horas = calendario.get(Calendar.HOUR_OF_DAY);
        int minutos = calendario.get(Calendar.MINUTE);

        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf(Acciones.ACCION_TIMER.getIdentificador()));

        sb.append(String.valueOf(recurso.getIdentificador()));
        sb.append(String.valueOf(opcionTimer.getIdentificador()));

        sb.append(colocarCerosAIzquierda(String.valueOf(horas)));
        sb.append(colocarCerosAIzquierda(String.valueOf(minutos)));

        return sb.toString();
    }

    public static String crearMensajeRequest(TiposRecurso recurso) {
        StringBuilder sb = new StringBuilder();
        sb.append(recurso.getIdentificador());

        return sb.toString();
    }

    public static String crearMensajeNotificacion(TiposRecurso recurso, int statusRecurso) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(recurso.getIdentificador()));
        sb.append(String.valueOf(statusRecurso));

        return sb.toString();
    }

    public static String colocarCerosAIzquierda(String s) {
        String cadena = String.format("%02d", Integer.parseInt(s));
        return cadena;
    }
}
