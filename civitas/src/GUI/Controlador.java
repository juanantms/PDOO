package GUI;

import civitas.*;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import GUI.Respuestas;

public class Controlador {
    private CivitasJuego juego;
    private CivitasView vista;
    
    public Controlador(CivitasJuego juego, CivitasView vista) {
        this.juego = juego;
        this.vista = vista;
    }
    
    public void juega() {
        vista.setCivitasJuego(juego);
        
        while(!juego.finalDelJuego()) {
            OperacionesJuego operacion = juego.siguientePaso();
            vista.actualizarVista();
            vista.mostrarSiguienteOperacion(operacion);
            if(operacion != civitas.OperacionesJuego.PASAR_TURNO) {
                vista.mostrarEventos();
            }
            if(!juego.finalDelJuego()) {
                switch(operacion) {
                    case COMPRAR:
                        Respuestas res = vista.comprar();
                        if(res == Respuestas.SI) {
                            juego.comprar();
                        }
                        juego.siguientePasoCompletado(operacion);
                        break;
                        
                    case GESTIONAR:
                        vista.gestionar();
                        vista.getGestion();
                        vista.getPropiedad();
                        OperacionInmobiliaria oi = new OperacionInmobiliaria(GestionesInmobiliarias.values()[vista.getGestion()], vista.getPropiedad());
                        
                        switch(GestionesInmobiliarias.values()[vista.getGestion()]) {
                            case VENDER:
                                if(juego.getJugadorActual().getNombrePropiedades() != null) {
                                    juego.vender(oi.getNumPropiedad());
                                    break;
                                }
                            case HIPOTECAR:
                                if(juego.getJugadorActual().getNombrePropiedades() != null) {
                                    juego.hipotecar(oi.getNumPropiedad());
                                    break;
                                }
                            case CANCELAR_HIPOTECA:
                                if(juego.getJugadorActual().getNombrePropiedades() != null) {
                                   juego.cancelarHipoteca(oi.getNumPropiedad());
                                   break;
                                }
                            case CONSTRUIR_CASA:
                                juego.construirCasa(oi.getNumPropiedad());
                                break;
                            case CONSTRUIR_HOTEL:
                                juego.construirHotel(oi.getNumPropiedad());
                                break;
                            default:
                                juego.siguientePasoCompletado(operacion);
                               break;
                        }
                        break;
                    case SALIR_CARCEL:
                        SalidasCarcel salida = vista.salirCarcel();
                        switch(salida) {
                            case PAGANDO:
                                juego.salirCarcelPagando();
                                break;
                            case TIRANDO:
                                juego.salirCarcelTirando();
                                break;
                        }
                }
            }else{
                ArrayList<Jugador> ranking = juego.ranking();
                for(int i = 0; i < ranking.size(); i++) {
                    System.out.println(ranking.get(i).getNombre() + "\n");
                }
                vista.actualizarVista();
            }
        }
    }
    
}

