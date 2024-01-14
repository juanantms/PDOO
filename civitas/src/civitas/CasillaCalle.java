package civitas;


import java.util.ArrayList;


public class CasillaCalle extends Casilla {
    
    private TituloPropiedad titulo;
    
    /**
     * Constructor por parametros.
     * @param titulo : Titulo de la propiedad de la casilla.
     */
    CasillaCalle(TituloPropiedad titulo) {
        super(titulo.getNombre());
        this.titulo = titulo;
    }
    
     /**
     * Consultor del titulo de la propiedad.
     * @return titulo.
     */
    TituloPropiedad getTituloPropiedad() {
        return titulo;
    }
    
    /**
     * Comprueba si el jugador es correcto e informa a diario de la 
     * casilla en la que cae el jugador, y compra la calle.
     * @param iactual : jugador actual.
     * @param todos : Lista de jugadores.
     */
    @Override
    public void recibeJugador(int iactual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(iactual, todos)) {
            informe(iactual, todos);
            Jugador jugador = todos.get(iactual);
            if(!titulo.tienePropietario()) {
                jugador.puedeComprarCasilla();
            }else{
                titulo.tramitarAlquiler(jugador);
            }
        }
    }
    
    /**
     * Metodo toString() que da informacion de la casilla.
     * @return salida.
     */
    @Override 
    public String toString() {
        String salida = "Titulo de la propiedad: " + titulo.toString();
        return salida;
    } 
    
}
