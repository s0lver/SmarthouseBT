package tamps.cinvestav.se.acciones;

import modelos.LegosEntity;
import org.hibernate.Session;
import tamps.cinvestav.se.acciones.pojos.EstatusRecurso;
import tamps.cinvestav.se.acciones.pojos.RecursosPorLego;
import tamps.cinvestav.se.acciones.pojos.RecursosPorTipoLego;
import tamps.cinvestav.se.log.Logger;
import tamps.cinvestav.se.mensajes.FabricaMensajes;
import tamps.cinvestav.se.ngineBT.ConexionLego;

import java.util.ArrayList;
import java.util.LinkedList;

public class RecolectorEstatus {
    private ArrayList<ConexionLego> conexiones;
    private Session sesion;
    private RepositorioRecursosPorTipoLego repositorioRecPorTL;
    private ArrayList<RecursosPorLego> matrizEstatus;

    public RecolectorEstatus(Session sesion, ArrayList<ConexionLego> conexiones) {
        this.conexiones = conexiones;
        this.sesion = sesion;
        repositorioRecPorTL = new RepositorioRecursosPorTipoLego(sesion);
        repositorioRecPorTL.fabricarRepositorioRecursosPorTLego();
    }

    public ArrayList<RecursosPorLego> recolectarEstatus() {
        matrizEstatus = new ArrayList<RecursosPorLego>();
        Logger.log(Logger.DOUBLE_DASHES);
        Logger.log("Iniciando la recolección de los estatus de los legos");
        Logger.log(Logger.DASHES);

        for (ConexionLego conexion : conexiones) {
            Logger.log(String.format("Solicitando estatus de recursos a lego %d en %s:", conexion.getLego().getId(), conexion.getLego().getMac()));

            if (conexion.isConexionAbierta()) {
                // Preguntar solamente por los recursos de este tipo lego
                LegosEntity lego = conexion.getLego();
                RecursosPorTipoLego recursosDelLego = repositorioRecPorTL.buscarRecursosPorTipoLego(lego.getIdTipoLego());
                LinkedList<EstatusRecurso> recursos = recursosDelLego.getEstatusRecurso();

                for (EstatusRecurso recurso : recursos) {
                    String cadenaSolicitudEstatus = FabricaMensajes.crearMensajeRequest(recurso.getRecurso());
                    Logger.log(String.format("Solicitando estado recurso %s a lego %d [%s]", recurso.getRecurso().toString(), conexion.getLego().getId(), cadenaSolicitudEstatus));
                    conexion.enviarComando(cadenaSolicitudEstatus.getBytes());

                    String respuestaDesdeLego = conexion.recibirRespuesta();
                    Logger.log(String.format("Respuesta desde lego: %s", respuestaDesdeLego));
                    int estatusRecurso = Integer.valueOf(respuestaDesdeLego);

                    recurso.setEstatus(estatusRecurso);
                }
                anexarAMatriz(lego, recursos);
            } else {
                Logger.log(
                        String.format("La conexión con el lego %d, %s no está abierta",
                                conexion.getLego().getId(),
                                conexion.getLego().getTiposlegoByIdTipoLego().getDescripcion())
                );
            }
        }
        Logger.log("Fin de la recolección de los estatus de los legos");
        Logger.log(Logger.DOUBLE_DASHES);
        System.out.println();
        return matrizEstatus;
    }

    private void anexarAMatriz(LegosEntity lego, LinkedList<EstatusRecurso> recursos) {
        matrizEstatus.add(new RecursosPorLego(lego, recursos));
    }
}