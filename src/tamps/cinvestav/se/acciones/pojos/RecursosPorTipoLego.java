package tamps.cinvestav.se.acciones.pojos;

import TiposLego.TiposLego;
import tamps.cinvestav.se.acciones.pojos.EstatusRecurso;

import java.util.LinkedList;

public class RecursosPorTipoLego {
    private TiposLego tipoLego;
    private LinkedList<EstatusRecurso> estatusRecurso;

    public RecursosPorTipoLego(TiposLego tipoLego, LinkedList<EstatusRecurso> estatusRecurso) {
        this.setTipoLego(tipoLego);
        this.setEstatusRecurso(estatusRecurso);
    }

    public TiposLego getTipoLego() {
        return tipoLego;
    }

    public void setTipoLego(TiposLego tipoLego) {
        this.tipoLego = tipoLego;
    }

    public LinkedList<EstatusRecurso> getEstatusRecurso() {
        return estatusRecurso;
    }

    public void setEstatusRecurso(LinkedList<EstatusRecurso> estatusRecurso) {
        this.estatusRecurso = estatusRecurso;
    }
}
