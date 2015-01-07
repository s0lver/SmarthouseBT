package tamps.cinvestav.se.util;

import tamps.cinvestav.se.ngineBT.ConexionLego;

import java.util.ArrayList;

public class UtileriasConexionBt {
    public static ConexionLego obtenerConexionDeLego(ArrayList<ConexionLego> conexiones, int idLego) {
        int cantidadConexiones = conexiones.size();
        for (int i = 0; i < cantidadConexiones; i++) {
            if (conexiones.get(i).getLego().getId() == idLego)
                return conexiones.get(i);
        }
        return null;
    }
}
