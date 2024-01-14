package civitas;


import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class SorpresaIrCarcel extends Sorpresa {
    
    private Tablero tablero;

    public SorpresaIrCarcel(Tablero tablero, String texto) {
        super(texto);
        this.tablero = tablero;
    }
    
    @Override
    public void aplicarAJugador(int actual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(actual, todos)) {
            informe(actual, todos);
            todos.get(actual).encarcelar(tablero.getCarcel());
        }
    }
    
    @Override
    public String toString() {
        String salida = "Tablero: " + tablero + super.toString();
        return salida;
    }
    
}
