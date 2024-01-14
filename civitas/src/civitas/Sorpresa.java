package civitas;


import java.util.ArrayList;

public abstract class Sorpresa {
    
    private String texto;
    
    
    public abstract void aplicarAJugador(int actual,  ArrayList<Jugador> todos);
    
    /**
     * Informa de la sorpresa que obtiene el jugador.
     * @param actual indice del jugador actual.
     * @param todos jugadores.
     */
    protected void informe(int actual,  ArrayList<Jugador> todos) {
        Diario.getInstance().ocurreEvento("El jugador " + 
                todos.get(actual).getNombre() + 
                " obtiene la sorpresa " + texto);
    }
   
    /**
     * Comprueba si el jugador actual es correcto.
     * @param actual indice del jugador actual.
     * @param todos jugadores.
     * @return 
     */
    public boolean jugadorCorrecto(int actual,  ArrayList<Jugador> todos) {
        return actual < todos.size() && actual >= 0;
    }
    
    Sorpresa(String texto) {
        this.texto = texto;
    }
    
    /**
     * Proporciona informacion de la sorpresa.
     * @return 
     */
    @Override
    public String toString() {
        String salida = "Sorpresa: " + texto;
        return salida;
    }
    
}
