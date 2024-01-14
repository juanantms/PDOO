package civitas;


import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class SorpresaPorJugador extends Sorpresa {
    
    private int valor;

    SorpresaPorJugador(int valor, String texto) {
        super(texto);
        this.valor = valor;
    }
    
    @Override
    public void aplicarAJugador(int actual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(actual, todos)) {
            informe(actual, todos);
            SorpresaPagarCobrar resta = new SorpresaPagarCobrar(valor*(-1), 
                    "Pagan todos los jugadores menos el no afectado.");
            for(int i = 0; i < todos.size(); i++) {
                if( i != actual) {
                    resta.aplicarAJugador(i, todos);
                }
            }
            SorpresaPagarCobrar suma = new SorpresaPagarCobrar(valor * 
                    (todos.size()-1), "Recibe dinero de todos los jugadores.");
            suma.aplicarAJugador(actual, todos);
        }
    }
    
    @Override
    public String toString() {
        String salida = "Valor: " + valor + super.toString();
        return salida;
    }
    
}