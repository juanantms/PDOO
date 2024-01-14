/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import civitas.CivitasJuego;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class TespP5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CivitasView view = new CivitasView();
        Dado.createInstance(view);
        Dado.getInstance().setDebug(false);
        CapturaNombres captura = new CapturaNombres(view, true);
        ArrayList<String> nombres = new ArrayList<>();
        nombres = captura.getNombres();
        CivitasJuego juego = new CivitasJuego(nombres);
        Controlador controlador = new Controlador(juego, view);
        
        view.setCivitasJuego(juego);
        //view.actualizarVista();
        
        controlador.juega();
    }
    
}
