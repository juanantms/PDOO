package civitas;


import java.util.ArrayList;


public class CasillaJuez extends Casilla {
    
    private static int carcel;
    
    /**
     * Constructor por parametros.
     * @param numCasillaCarcel : Indice de la carcel.
     * @param nombre : Nombre de la casilla.
     */
    CasillaJuez(int numCasillaCarcel, String nombre) {
        super(nombre);
        carcel = numCasillaCarcel;
    }

    /**
     * Comprueba si el jugador es correcto e informa a diario que el 
     * jugador ha caido en la carcel y se encarcela al jugador.
     * @param iactual : Jugador.
     * @param todos : Lista de jugadores.
     */
    @Override
    public void recibeJugador(int iactual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(iactual, todos)) {
            informe(iactual, todos);
            todos.get(iactual).encarcelar(carcel);
        }
    }

    /**
     * Metodo toString() que da informacion de la casilla.
     * @return salida.
     */
    @Override 
    public String toString() {
        String salida = "Nombre " + this.getNombre();
        return salida;
    } 
    
}