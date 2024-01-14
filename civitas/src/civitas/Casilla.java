package civitas;


import java.util.ArrayList;

/**
 * CLASE CASILLA
 */
public class Casilla {
    
    private String nombre;
    
    /**
     * Constructor por parametros.
     * @param nombre : Nombre de la casilla.
     */
    Casilla(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Consultor del nombre de la casilla.
     * @return nombre.
     */
    public String getNombre() {
        return nombre;
    }  
    
    /**
     * Informa del jugador que cae en la casilla a diario.
     * @param iactual : Jugador que cae.
     * @param todos : Lista de jugadores.
     */
    void informe(int iactual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(iactual, todos)) {
            Diario.getInstance().ocurreEvento("El jugador " + 
                todos.get(iactual).getNombre() + " ha caido en la casilla " 
                + toString());
        }
    }
    
    /**
     * Comprueba si el jugador es correcto.
     * @param iactual : Jugador que se va a comprobar.
     * @param todos : Lista de jugadores.
     * @return 
     */
    public boolean jugadorCorrecto(int iactual, ArrayList<Jugador> todos) {
        return iactual <= todos.size();
    }
    
    /**
     * Comprueba en que calle cae el jugador.
     * @param iactual : jugador actual.
     * @param todos : Lista de jugadores.
     */
    void recibeJugador(int iactual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(iactual, todos)) {
            informe(iactual, todos);
        }
    }
    
    /**
     * Metodo toString() que da informacion de la casilla.
     * @return salida.
     */
    @Override 
    public String toString() {
        String salida = "Casilla: " + nombre;
        return salida;
    }   
}