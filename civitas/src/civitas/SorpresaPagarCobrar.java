package civitas;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class SorpresaPagarCobrar extends Sorpresa {
    
    private int valor;

    public SorpresaPagarCobrar(int valor, String texto) {
        super(texto);
        this.valor = valor;
    }
    
    @Override
    public void aplicarAJugador(int actual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(actual, todos)) {
            informe(actual, todos);
            todos.get(actual).modificarSaldo(valor);
        }
    }
    
    @Override
    public String toString() {
        String salida = "Valor: " + valor + super.toString();
        return salida;
    }
    
}
