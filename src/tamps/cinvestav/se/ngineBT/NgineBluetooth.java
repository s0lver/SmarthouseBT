package tamps.cinvestav.se.ngineBT;

import administradores.AdministradorLegos;
import iadministradores.IAdministradorLegos;
import modelos.LegosEntity;
import org.hibernate.Session;
import tamps.cinvestav.se.acciones.*;
import tamps.cinvestav.se.acciones.pojos.Cambio;
import tamps.cinvestav.se.acciones.pojos.RecursosPorLego;
import tamps.cinvestav.se.listenerEventos.ws.ConfiguracionesImpl;
import tamps.cinvestav.se.listenerEventos.ws.OrdenesImpl;
import tamps.cinvestav.se.log.Logger;
import util.SessionUtil;

import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class NgineBluetooth {
    private Collection<LegosEntity> listaLegos;
    private ArrayList<ConexionLego> listaConexiones;
    private Session sesionSQL;
    private IAdministradorLegos administradorLegos;
    private ArrayList<RecursosPorLego> matrizEstadosGlobales;
    private ArrayList<Cambio> listaEventos;
    private Dictador dictadorOrdenes;
    private InformadorHoras informadorHoras;
    private InformadorConfiguracionHorarios informadorConfiguraciones;
    private RecolectorEstatus recolectorEstatus;

    private ArrayList<RecursosPorLego> matrizNuevosEstados;
    private DetectorCambios detectorCambios;
    private InformadorCambios informadorCambios;
    private int contadorRounds;

    public NgineBluetooth() {
        Logger.log("Iniciando...");
        Logger.log("Obteniendo sesión SQL...");
        sesionSQL = SessionUtil.getSession();
        Logger.log("Sesión SQL obtenida...");

        administradorLegos = new AdministradorLegos(sesionSQL);
        matrizEstadosGlobales = new ArrayList<RecursosPorLego>();
        contadorRounds = 1;
    }

    public void accionar() {
        prepararConexiones();

        dictadorOrdenes = new Dictador(listaConexiones);
        informadorHoras = new InformadorHoras(listaConexiones);
        informadorConfiguraciones = new InformadorConfiguracionHorarios(listaConexiones);

        while (true) {
            Logger.log(Logger.NUMBER_SYMBOLS);
            Logger.log(String.format("                   #Round %d#                   ", contadorRounds));
            Logger.log(Logger.NUMBER_SYMBOLS);

            dictadorOrdenes.informarOrdenes();
            informadorHoras.informarHora();
            informadorConfiguraciones.informarConfiguraciones();

            recolectorEstatus = new RecolectorEstatus(sesionSQL, listaConexiones);
            matrizNuevosEstados = recolectorEstatus.recolectarEstatus();

            detectorCambios = new DetectorCambios(matrizNuevosEstados, matrizEstadosGlobales, sesionSQL);
            listaEventos = detectorCambios.detectarCambios();

            informadorCambios = new InformadorCambios(listaConexiones, listaEventos);
            informadorCambios.informarCambios();
            matrizEstadosGlobales = matrizNuevosEstados;

            System.out.println();
            contadorRounds++;
        }
    }

    private boolean verificarAlgunaConexionAbierta() {
        for(ConexionLego conexion :listaConexiones ){
            if (conexion.isConexionAbierta())
                return true;
        }
        return false;
    }

    private void prepararConexiones(){
        try {
            obtenerListaConexiones();
            abrirConexiones();

            if (!verificarAlgunaConexionAbierta()){
                System.out.println("No existe ninguna conexión abierta, abortando...");
                System.exit(1);
            }

            System.out.println("Ejecute el programa en los bricks");
            System.out.println("Presione <ENTER> para continuar");
            System.in.read();
            System.out.println("Iniciando!");

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private void obtenerListaConexiones() {
        Logger.log("================================================");
        Logger.log("Obteniendo lista de legos...");
        Logger.log("------------------------------------------------");
        listaLegos= administradorLegos.lista();
        Logger.log("Lista obtenida...");

        if (listaLegos== null || listaLegos.size() == 0) {
            Logger.log("Lista de legos vacía, finalizando ejecución");
            System.exit(1);
        }

        listaConexiones = new ArrayList<ConexionLego>();

        int cantidadLegos = listaLegos.size();

        listaLegos.forEach(new Consumer<LegosEntity>() {
            @Override
            public void accept(LegosEntity lego) {
                ConexionLego conexionLego = new ConexionLego(lego);
                listaConexiones.add(conexionLego);
            }
        });

        Logger.log(String.format("%d legos identificados", listaConexiones.size()));
        Logger.log("================================================");
        Logger.log("\n");
    }

    private void abrirConexiones(){
        Logger.log("================================================");
        Logger.log("Abriendo conexiones Bt...");
        Logger.log("------------------------------------------------");

        int i = 0;
        for (ConexionLego conexion : listaConexiones) {
            Logger.log("-");
            String cadenaLog = String.format(
                    "Abriendo conexión %d Bt con lego [id %d, phy %s, %s]",
                    (i + 1),
                    conexion.getLego().getId(),
                    conexion.getLego().getMac(),
                    conexion.getLego().getTiposlegoByIdTipoLego().getDescripcion()
            );
            Logger.log(cadenaLog);
            boolean exito = conexion.abrirConexion();
            if (exito)
                Logger.log(String.format("Conexión %d establecida!", (i + 1)));
            else
                Logger.log(String.format("No se pudo establecer la conexión %d", (i + 1)));
            i++;
        }
        Logger.log("La lista de conexiones a los legos ha sido procesada");
        Logger.log("================================================");
    }

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9998/ws/configuraciones", new ConfiguracionesImpl());
        Endpoint.publish("http://localhost:9997/ws/ordenes", new OrdenesImpl());

        System.out.println("Listeners web configurados");
        NgineBluetooth app = new NgineBluetooth();
        app.accionar();
    }
}
