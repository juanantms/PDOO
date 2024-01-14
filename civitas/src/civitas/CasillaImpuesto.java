package civitas;


import java.util.ArrayList;


public class CasillaImpuesto extends Casilla {
 
    private float importe;
    
    /**
     * Constructor por parametros.
     * @param cantidad  Importe de la casilla.
     * @param nombre Nombre de la casilla.
     */
    CasillaImpuesto(float cantidad, String nombre) {
        super(nombre);
        importe = cantidad;
    }
    
    /**
     * Comprueba si el jugador es correcto e informa a diario de la 
     * casilla en la que cae el jugador, y le hace pagar un impuesto.
     * @param iactual : Jugador.
     * @param todos : Lista de jugadores.
     */
    @Override
    public void recibeJugador(int iactual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(iactual, todos)) {
            informe(iactual, todos);
            todos.get(iactual).pagaImpuesto(importe);
        }
    }
    
    /**
     * Metodo toString() que da informacion de la casilla.
     * @return salida.
     */
    @Override 
    public String toString() {
        String salida = "Casilla " + this.getNombre() + "\nImporte: " + importe;
        return salida;
    } 
    
}