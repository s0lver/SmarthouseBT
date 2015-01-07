package tamps.cinvestav.se.acciones.pojos;

import TiposRecurso.TiposRecurso;

public class Orden {
    private int idLego;
    private TiposRecurso recurso;
    private int accion;

    public Orden(int idLego, TiposRecurso recurso, int accion) {
        this.setIdLego(idLego);
        this.recurso = recurso;
        this.accion = accion;
    }

    public TiposRecurso getRecurso() {
        return recurso;
    }

    public void setRecurso(TiposRecurso recurso) {
        this.recurso = recurso;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public int getIdLego() {
        return idLego;
    }

    public void setIdLego(int idLego) {
        this.idLego = idLego;
    }

    public String toString() {
        return String.format(
                "Orden para lego %d, el recurso %s debe tener tomar el valor %d",
                idLego, recurso.toString(), accion
        );
    }
}
