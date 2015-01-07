package tamps.cinvestav.se.acciones.pojos;

import TiposRecurso.TiposRecurso;

public class EstatusRecurso {
    TiposRecurso recurso;
    int estatus;

    public EstatusRecurso(TiposRecurso recurso, int estatus) {
        this.recurso = recurso;
        this.estatus = estatus;
    }

    public TiposRecurso getRecurso() {
        return recurso;
    }

    public void setRecurso(TiposRecurso recurso) {
        this.recurso = recurso;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
