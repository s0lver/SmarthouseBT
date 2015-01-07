package tamps.cinvestav.se.acciones.pojos;

public enum OpcionesTimer {
    APAGAR(0),
    ENCENDER(1),
    CANCELAR(2);

    private int identificador;

    OpcionesTimer(int identificador) {
        this.identificador = identificador;
    }

    public int getIdentificador() {
        return this.identificador;
    }

    public static OpcionesTimer getTipoFromIdentificador(int id) {
        switch(id) {
            case 0:
                return APAGAR;
            case 1:
                return ENCENDER;
            case 2:
                return CANCELAR;
            default:
                throw new RuntimeException("No puedo convertir ese id en un tipo de recurso");
        }
    }
}
