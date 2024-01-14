package civitas;


import java.util.ArrayList;


public class CasillaSorpresa extends Casilla {
    
    private Sorpresa sorpresa;
    private MazoSorpresa mazo;
    
    /**
     * Constructor por parametros.
     * @param mazo : Mazo de sorpresas.
     * @param nombre : Nombre de la casilla.
     */
    CasillaSorpresa(MazoSorpresa mazo, String nombre) {
        super(nombre);
        this.mazo = mazo;
    }
    
    /**
     * Comprueba si el jugador es correcto e informa a diario 
     * de la sorpresa que recibe.
     * @param iactual : Jugador actual.
     * @param todos : Lista de jugadores
     */
    @Override
    public void recibeJugador(int iactual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(iactual, todos)) {
            sorpresa = mazo.siguiente();
            informe(iactual, todos);
            sorpresa.aplicarAJugador(iactual, todos);
        }
    }
    
    /**
     * Metodo toString() que da informacion de la casilla.
     * @return salida.
     */
    @Override 
    public String toString() {
        String salida = "Sorpresa " + sorpresa + "\nMazo: " + mazo;
        return salida;
    } 
    
}
