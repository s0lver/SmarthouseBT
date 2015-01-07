package tamps.cinvestav.se.acciones.pojos;

public enum Acciones {
    ACCION_RELOJ(6),
    ACCION_TIMER(7),
    ACCION_ORDEN_DIRECTA(9);

    private int identificador;

    Acciones(int identificador) {
        this.identificador = identificador;
    }

    public int getIdentificador() {
        return this.identificador;
    }
}
