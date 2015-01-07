package tamps.cinvestav.se.acciones;

import TiposRecurso.TiposRecurso;
import administradores.AdministradorEventos;
import administradores.AdministradorLegosRecursos;
import iadministradores.IAdministradorEventos;
import modelos.EventosEntity;
import modelos.LegosEntity;
import modelos.LegosrecursosEntity;
import org.hibernate.Session;
import tamps.cinvestav.se.acciones.pojos.Cambio;
import tamps.cinvestav.se.acciones.pojos.EstatusRecurso;
import tamps.cinvestav.se.acciones.pojos.RecursosPorLego;
import tamps.cinvestav.se.log.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class DetectorCambios {
    private ArrayList<RecursosPorLego> matrizNuevosEstados;
    private ArrayList<RecursosPorLego> matrizEstadosActuales;
    private IAdministradorEventos administradorEventos;
    private Session sesion;

    public DetectorCambios(ArrayList<RecursosPorLego> matrizNuevosEstados,
                           ArrayList<RecursosPorLego> matrizEstadosActuales, Session sesion) {
        this.matrizNuevosEstados = matrizNuevosEstados;
        this.matrizEstadosActuales = matrizEstadosActuales;
        this.sesion = sesion;
        this.administradorEventos = new AdministradorEventos(sesion);
    }

    public ArrayList<Cambio> detectarCambios() {
        ArrayList<Cambio> listaCambios = new ArrayList<Cambio>();
        Logger.log(Logger.DOUBLE_DASHES);
        Logger.log("Iniciando la detección de cambios en los estatus de los legos");
        Logger.log(Logger.DASHES);
        if (matrizEstadosActuales.isEmpty()) {
            for (RecursosPorLego rpl : matrizNuevosEstados) {
                for (EstatusRecurso estatus : rpl.getEstatusRecurso()) {
                    Cambio cambio = crearNotificacionCambio(rpl.getLego(), estatus.getRecurso(), estatus.getEstatus());
                    listaCambios.add(cambio);
                }
            }
        }else{
            for(RecursosPorLego recursosAntiguoEstatus : matrizEstadosActuales) {
                LegosEntity lego = recursosAntiguoEstatus.getLego();
                RecursosPorLego recursosNuevoEstatus = obtenerNuevosEstatusDeRecursos(lego, matrizNuevosEstados);
                for (EstatusRecurso antiguoEstatusRecurso : recursosAntiguoEstatus.getEstatusRecurso()) {
                    TiposRecurso recurso = antiguoEstatusRecurso.getRecurso();
                    EstatusRecurso nuevoEstatusRecurso = obtenerNuevoEstatusDeRecurso(recurso, recursosNuevoEstatus);
                    if (antiguoEstatusRecurso.getEstatus() != nuevoEstatusRecurso.getEstatus()) {
                        Cambio cambio = crearNotificacionCambio(lego, antiguoEstatusRecurso.getRecurso(), nuevoEstatusRecurso.getEstatus());
                        listaCambios.add(cambio);

                        Logger.log(cambio.toString());
                    }
                }
            }
        }
        Logger.log(Logger.DASHES);
        System.out.println("Se han detectado " + listaCambios.size() + " nuevos eventos");
        Logger.log("Fin de la detección de cambios en los estatus de los legos");
        Logger.log(Logger.DOUBLE_DASHES);
        System.out.println();
        guardarCambios(listaCambios);
        return listaCambios;
    }

    private void guardarCambios(ArrayList<Cambio> listaEventos) {
        AdministradorLegosRecursos adminLR = new AdministradorLegosRecursos(sesion);

        Timestamp ts = new Timestamp(new Date().getTime());
        for (Cambio eventoActual : listaEventos) {
            LegosrecursosEntity lr = adminLR.buscarPorLegoYRecurso(eventoActual.getLegosEntity().getId(),
                    eventoActual.getRecurso().getIdentificador()
            );
            EventosEntity cambio = new EventosEntity();
            cambio.setSentido((byte) eventoActual.getNuevoValor());
            cambio.setTimestamp(ts);
            cambio.setIdLegoRecurso(lr.getId());

            administradorEventos.agregar(cambio);
        }
    }

    private EstatusRecurso obtenerNuevoEstatusDeRecurso(TiposRecurso recurso, RecursosPorLego recursosNuevoEstatus) {
        LinkedList<EstatusRecurso> listaEstatusRecursos = recursosNuevoEstatus.getEstatusRecurso();
        for (EstatusRecurso estadoRecurso : listaEstatusRecursos) {
            if (estadoRecurso.getRecurso().getIdentificador() == recurso.getIdentificador()) {
                return estadoRecurso;
            }
        }
        throw new RuntimeException(
                String.format("No se puede encontrar el nuevo estatus del recurso con id %d en la lista de nuevos estatus",
                        recurso.getIdentificador())
        );
    }

    private RecursosPorLego obtenerNuevosEstatusDeRecursos(LegosEntity lego, ArrayList<RecursosPorLego> matrizNuevosEstatus){
        for(RecursosPorLego legoEstadosRecurso:matrizNuevosEstatus){
            if (legoEstadosRecurso.getLego().getId() == lego.getId()) {
                return legoEstadosRecurso;
            }
        }
        throw new RuntimeException(
                String.format("No se puede encontrar los nuevos estaus del lego con id %d en la matriz de estados especificada",
                        lego.getId())
        );
    }

    private Cambio crearNotificacionCambio(LegosEntity lego, TiposRecurso recurso, int nuevoEstatus) {
        Cambio cambio = new Cambio(lego, recurso, nuevoEstatus);
        return cambio;
    }
}
