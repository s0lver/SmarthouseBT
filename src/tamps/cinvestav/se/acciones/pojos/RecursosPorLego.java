package tamps.cinvestav.se.acciones.pojos;

import modelos.LegosEntity;
import tamps.cinvestav.se.acciones.pojos.EstatusRecurso;

import java.util.LinkedList;

public class RecursosPorLego {
    private LegosEntity lego;
    private LinkedList<EstatusRecurso> estatusRecurso;

    public RecursosPorLego(LegosEntity lego, LinkedList<EstatusRecurso> estatusRecurso) {
        this.setLego(lego);
        this.setEstatusRecurso(estatusRecurso);
    }

    public LegosEntity getLego() {
        return lego;
    }

    public void setLego(LegosEntity lego) {
        this.lego = lego;
    }

    public LinkedList<EstatusRecurso> getEstatusRecurso() {
        return estatusRecurso;
    }

    public void setEstatusRecurso(LinkedList<EstatusRecurso> estatusRecurso) {
        this.estatusRecurso = estatusRecurso;
    }
}
