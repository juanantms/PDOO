package civitas;


import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class SorpresaSalirCarcel extends Sorpresa{

    private MazoSorpresa mazo;

    SorpresaSalirCarcel(MazoSorpresa mazo, String texto) {
        super(texto);
        this.mazo = mazo;
    }
    
    @Override
    public void aplicarAJugador(int actual, ArrayList<Jugador> todos) {
        if(jugadorCorrecto(actual, todos)) {
            informe(actual, todos);
            boolean tiene = false;
            for(int i = 0; i < todos.size(); i++) {
                if(todos.get(i).tieneSalvoconducto()) {
                    tiene = true;
                }
            }
            if(!tiene) {
                todos.get(actual).obtenerSalvoconducto(this);
                salirDelMazo();
            }
        }
    }
    
    /**
     * Sale del mazo si la sorpresa es salir de la carcel y la inhabilita.
     */
    void salirDelMazo() {
       mazo.inhabilitarCartaEspecial(this);
    }
    
    @Override
    public String toString() {
        String salida = "Mazo " + mazo + super.toString();
        return salida;
    }
    
    /**
     * Cuando la sorpresa SALIRCARCEL esta usada la habilita.
     */
    void usada() {
        mazo.habilitarCartaEspecial(this);
    }
    
}