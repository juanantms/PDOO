/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitas;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class SorpresaPorCasaHotel extends Sorpresa {
    
    private int valor;

    public SorpresaPorCasaHotel(int valor, String texto) {
        super(texto);
        this.valor = valor;
    }
    
    @Override
    public void aplicarAJugador(int actual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(actual, todos)) {
            informe(actual, todos);
            todos.get(actual).modificarSaldo(valor * 
                    todos.get(actual).cantidadCasasHoteles());
        }
    }
    
    @Override
    public String toString() {
        String salida = "Valor: " + valor + super.toString();
        return salida;
    }

}
