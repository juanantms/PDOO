package civitas;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class SorpresaIrCasilla extends Sorpresa {
    
    private int valor;
    private Tablero tablero;
    
    SorpresaIrCasilla(Tablero tablero, int valor, String texto) {
        super(texto);
        this.tablero = tablero;
        this.valor = valor;
    }
    
    @Override
    public void aplicarAJugador(int actual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(actual, todos)) {
            informe(actual, todos);
            int casillaActual = todos.get(actual).getNumCasillaActual();
            int tirada = tablero.calcularTirada(casillaActual, valor);
            int nuevacasilla = tablero.nuevaPosicion(casillaActual, tirada);
            todos.get(actual).moverACasilla(nuevacasilla);
            tablero.getCasilla(nuevacasilla).recibeJugador(actual, todos);
        }
    }
    
    @Override
    public String toString() {
        String salida = "Valor: " + valor + "Tablero: " + tablero + 
                super.toString();
        return salida;
    }
    
}
