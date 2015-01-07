package tamps.cinvestav.se.ngineBT;

import jssc.SerialPort;
import jssc.SerialPortException;
import modelos.LegosEntity;

import java.util.Arrays;

public class ConexionLego{
    private SerialPort serialPort;

    private boolean conexionAbierta;

    private LegosEntity lego;

    public ConexionLego(LegosEntity lego) {
        this.lego = lego;
        this.serialPort = new SerialPort(lego.getMac());
        this.setConexionAbierta(false);
    }

    public boolean abrirConexion() {
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            this.setConexionAbierta(true);
            return true;
        } catch (SerialPortException e) {
            System.out.println("Ocurrió un error al abrir la conexión");
            return false;
        }
    }

    public void cerrarConexion() {
        try {
            serialPort.closePort();
            this.setConexionAbierta(false);
        } catch (SerialPortException e) {
            System.out.println("Ocurrió un error al cerrar la conexión");
        }
    }

    private byte[] prepararComando(byte[] payload){
        byte[] comando = new byte[80];

        byte longitudBytes;
        byte MAILBOX = 0;

        longitudBytes = (byte) payload.length;
        comando[0] = 0x00;
        comando[1] = 0x09;
        comando[2] = MAILBOX;
        comando[3] = (byte)(longitudBytes + 1);

        int posicionInicioMensaje = 4;
        longitudBytes = (byte)(longitudBytes + posicionInicioMensaje);
        int i = posicionInicioMensaje;

        for (; ; ) {
            comando[i] = payload[i - posicionInicioMensaje];
            i++;
            if (i >= longitudBytes) break;
        }

        return comando;
    }

    public void enviarComando(byte[] payload) {
        try {
            byte[] comando = prepararComando(payload);
            byte[] bytesHaciaLego = new byte[]{(byte) comando.length, (byte) 0};

            serialPort.writeBytes(bytesHaciaLego);
            serialPort.writeBytes(comando);

            Thread.sleep(250);
            serialPort.readBytes();
            //Thread.sleep(500);
        } catch (SerialPortException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String recibirRespuesta() {
        String respuestaLego = null;
        byte[] otro = new byte[]{0x05, 0x00, 0x00, 0x13, 0x0A, 0x00, 0x01};
        try {
            serialPort.writeBytes(otro);
            Thread.sleep(250);
            byte[] bytesEntrada = serialPort.readBytes();

            if (bytesEntrada != null) {
                if (bytesEntrada[4] != 0) {
                    throw new RuntimeException("El estatus byte no es 0, hubo un error en la transmisión");
                }
                byte[] bytesSinTamanyo = Arrays.copyOfRange(bytesEntrada, 2, bytesEntrada.length);
                int indicadorTamanyo = 4;
                int tamanyo = bytesSinTamanyo[indicadorTamanyo];
                byte[] payloadMensaje = Arrays.copyOfRange(bytesSinTamanyo, indicadorTamanyo + 1, indicadorTamanyo + tamanyo);

                respuestaLego = convertirAString(payloadMensaje);
            } else {
                throw new RuntimeException("No se recibió respuesta del lego");
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return respuestaLego;
    }

    private String convertirAString(byte[] arregloBytes){
        // En los primeros bytes debería estar el tamaño de la respuesta
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arregloBytes.length; i++) {
            sb.append((char) arregloBytes[i]);
        }
        return sb.toString();
    }

    private void imprimirContenidoArray(byte[] bytesEntrada) {
        for (int i = 0; i < bytesEntrada.length; i++) {
            System.out.print((char)bytesEntrada[i]);
        }
    }

    public LegosEntity getLego() {
        return lego;
    }

    public boolean isConexionAbierta() {
        return conexionAbierta;
    }

    public void setConexionAbierta(boolean conexionAbierta) {
        this.conexionAbierta = conexionAbierta;
    }
}
