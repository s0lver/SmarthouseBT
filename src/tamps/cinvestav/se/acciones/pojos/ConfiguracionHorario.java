package tamps.cinvestav.se.acciones.pojos;

import TiposRecurso.TiposRecurso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfiguracionHorario {
    private int idLego;
    private TiposRecurso recurso;
    private OpcionesTimer sentido;
    private Date hora;

    public ConfiguracionHorario(int idLego, TiposRecurso recurso, OpcionesTimer sentido, Date hora) {
        this.setIdLego(idLego);
        this.setRecurso(recurso);
        this.setSentido(sentido);
        this.setHora(hora);
    }


    public int getIdLego() {
        return idLego;
    }

    public void setIdLego(int idLego) {
        this.idLego = idLego;
    }

    public TiposRecurso getRecurso() {
        return recurso;
    }

    public void setRecurso(TiposRecurso recurso) {
        this.recurso = recurso;
    }

    public OpcionesTimer getSentido() {
        return sentido;
    }

    public void setSentido(OpcionesTimer sentido) {
        this.sentido = sentido;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return String.format(
                "Recurso %s a la hora %s tendrá valor %s",
                recurso.toString(),
                sdf.format(hora),
                sentido.toString()
        );
    }
}
