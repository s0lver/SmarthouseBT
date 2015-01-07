package tamps.cinvestav.se.acciones.pojos;

import TiposRecurso.TiposRecurso;
import modelos.LegosEntity;

public class Cambio {
    private LegosEntity legosEntity;
    private TiposRecurso recurso;
    private int nuevoValor;

    public Cambio(LegosEntity legosEntity, TiposRecurso recurso, int nuevoValor) {
        this.legosEntity = legosEntity;
        this.recurso = recurso;
        this.nuevoValor = nuevoValor;
    }

    public LegosEntity getLegosEntity() {
        return legosEntity;
    }

    public void setLegosEntity(LegosEntity legosEntity) {
        this.legosEntity = legosEntity;
    }

    public TiposRecurso getRecurso() {
        return recurso;
    }

    public void setRecurso(TiposRecurso recurso) {
        this.recurso = recurso;
    }

    public int getNuevoValor() {
        return nuevoValor;
    }

    public void setNuevoValor(int nuevoValor) {
        this.nuevoValor = nuevoValor;
    }

    public String toString() {
        return String.format(
                "Cambio en lego %d, phy %s, el recurso %s ahora tiene el valor %d",
                legosEntity.getId(),
                legosEntity.getMac(),
                recurso.toString(),
                nuevoValor);
    }
}
