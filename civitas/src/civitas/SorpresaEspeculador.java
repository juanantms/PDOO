package civitas;


import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class SorpresaEspeculador extends Sorpresa {
    
    private int fianza;
    
    SorpresaEspeculador(int fianza, String texto) {
        super(texto);
        this.fianza = fianza;
    }
    
    @Override
    public void aplicarAJugador(int actual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(actual, todos)) {
            informe(actual, todos);
            JugadorEspeculador jugador = new JugadorEspeculador(
                    todos.get(actual), fianza);
            todos.set(actual, jugador);
        }
    }
    
    @Override
    public String toString() {
        String salida = "Fianza: " + fianza + super.toString();
        return salida;
    }
    
}
