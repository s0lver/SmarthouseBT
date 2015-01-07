package tamps.cinvestav.se.acciones;

import TiposLego.TiposLego;
import TiposRecurso.TiposRecurso;
import administradores.AdministradorRecursos;
import iadministradores.IAdministradorRecursos;
import modelos.RecursosEntity;
import org.hibernate.Session;
import tamps.cinvestav.se.acciones.pojos.EstatusRecurso;
import tamps.cinvestav.se.acciones.pojos.RecursosPorTipoLego;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class RepositorioRecursosPorTipoLego {
    private IAdministradorRecursos administradorRecursos;
    private ArrayList<RecursosPorTipoLego> recursosPorTipoLegos;
    private Session sesion;

    public RepositorioRecursosPorTipoLego(Session sesion) {
        this.sesion = sesion;
        this.administradorRecursos = new AdministradorRecursos(sesion);
        this.recursosPorTipoLegos = new ArrayList<RecursosPorTipoLego>();
        fabricarRepositorioRecursosPorTLego();
    }

    public RecursosPorTipoLego buscarRecursosPorTipoLego(int idTipoLego){
        for(RecursosPorTipoLego rptl : recursosPorTipoLegos){
            if (rptl.getTipoLego().getIdentificador() == idTipoLego){
                return rptl;
            }
        }

        throw new RuntimeException(String.format("No puedo encontrar el tipo lego con id %d", idTipoLego));
    }

    public void fabricarRepositorioRecursosPorTLego() {
        Collection<RecursosEntity> bdRecursos = administradorRecursos.buscarPorTipoLego(TiposLego.ADMON_VENTANAS_PERSIANAS.getIdentificador());
        LinkedList<EstatusRecurso> listaEstatusRecurso = fabricarListaRecursosPorTL(bdRecursos);
        recursosPorTipoLegos.add(new RecursosPorTipoLego(TiposLego.ADMON_VENTANAS_PERSIANAS, listaEstatusRecurso));

        bdRecursos = administradorRecursos.buscarPorTipoLego(TiposLego.ADMON_LUCES_AC.getIdentificador());
        listaEstatusRecurso = fabricarListaRecursosPorTL(bdRecursos);
        recursosPorTipoLegos.add(new RecursosPorTipoLego(TiposLego.ADMON_LUCES_AC, listaEstatusRecurso));

        bdRecursos = administradorRecursos.buscarPorTipoLego(TiposLego.ADMON_PUERTA_ACCESO.getIdentificador());
        listaEstatusRecurso = fabricarListaRecursosPorTL(bdRecursos);
        recursosPorTipoLegos.add(new RecursosPorTipoLego(TiposLego.ADMON_PUERTA_ACCESO, listaEstatusRecurso));
    }

    private LinkedList<EstatusRecurso> fabricarListaRecursosPorTL(Collection<RecursosEntity> bdRecursos) {
        LinkedList<EstatusRecurso> respuesta = new LinkedList<EstatusRecurso>();
        for(RecursosEntity recurso:bdRecursos) {
            respuesta.add(
                    new EstatusRecurso(TiposRecurso.getTipoFromIdentificador(recurso.getId()), 2)
            );
        }

        return respuesta;
    }
}
