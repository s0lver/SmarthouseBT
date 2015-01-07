package tamps.cinvestav.se.listenerEventos.ws;

import TiposRecurso.TiposRecurso;
import iadministradores.IAdministradorHorarios;
import tamps.cinvestav.se.acciones.pojos.ConfiguracionHorario;
import tamps.cinvestav.se.acciones.pojos.OpcionesTimer;
import tamps.cinvestav.se.log.Logger;

import javax.jws.WebService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@WebService(endpointInterface = "tamps.cinvestav.se.listenerEventos.ws.Configuraciones")
public class ConfiguracionesImpl implements Configuraciones{

    private static ArrayList<ConfiguracionHorario> configuracionesPendientes = new ArrayList<ConfiguracionHorario>();

    public static ArrayList<ConfiguracionHorario> getConfiguracionesPendientes() {
        ArrayList<ConfiguracionHorario> copia = new ArrayList<ConfiguracionHorario>();
        synchronized (configuracionesPendientes) {
            for(ConfiguracionHorario ch : configuracionesPendientes) {
                copia.add(new ConfiguracionHorario(
                                ch.getIdLego(),
                                TiposRecurso.getTipoFromIdentificador(ch.getRecurso().getIdentificador()),
                                OpcionesTimer.getTipoFromIdentificador(ch.getSentido().getIdentificador()),
                                new Date(ch.getHora().getTime())
                        )
                );
            }

            configuracionesPendientes.clear();
        }
        return copia;
    }

    public static void setConfiguracionesPendientes(ArrayList<ConfiguracionHorario> configuracionesPendientes) {
        ConfiguracionesImpl.configuracionesPendientes = configuracionesPendientes;
    }

    @Override
    public String agregarConfiguracion(int idLego, int idRecurso, int opcionesTimer, String hora) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date horaDelDia = sdf.parse(hora);
            ConfiguracionHorario configuracionHorario;
            configuracionHorario = new ConfiguracionHorario(idLego,
                    TiposRecurso.getTipoFromIdentificador(idRecurso),
                    OpcionesTimer.getTipoFromIdentificador(opcionesTimer),
                    horaDelDia
                    );

            configuracionesPendientes.add(configuracionHorario);
            Logger.log(String.format("Se ha recibido una nueva configuracion de horario %s", configuracionHorario.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "ok";
    }
}





