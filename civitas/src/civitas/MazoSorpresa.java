package civitas;


import java.util.ArrayList;
import java.util.Collections;


/**
 * CLASE MAZOSORPRESA
 */
public class MazoSorpresa {
    
    /**
    * Tipos de sorpresas que hay.
    */
    private ArrayList<Sorpresa> sorpresas;   
    /**
    * Indica si las cartas estan barajadas.
    */
    private boolean barajada;        
    /**
    * Numero de cartas usadas.
    */
    private int usadas;            
    /**
    * Cuando está activo este atributo, el mazo no se baraja,
    * permitiendo ir obteniendo las sorpresas 
    * siempre en el mismo orden en el que se añaden.
    */
    private boolean debug;        
    /**
     * Cartas especiales dle mazo.
     */
    private ArrayList<Sorpresa> cartasEspeciales;   
    /**
     * Ultima sorpresa en salir.
     */
    private Sorpresa ultimaSorpresa;
    
    
    
    /**
    * Inicializa sorpresas y cartasEspeciales a un contenerdor vacio,
    * barajada a false y usadas a 0.
    */
    private void init() {
        sorpresas = new ArrayList();
        cartasEspeciales = new ArrayList();
        barajada = false;
        usadas = 0;
    }

    /**
     * Constructor con 1 parametro que inicializa debug.
     * Llama al metodo init() y si debug es TRUE se informa al diario.
     * @param d 
     */
    MazoSorpresa(boolean d) {
        debug = d;
        init();
  
        if(debug){
            Diario.getInstance().ocurreEvento("Debug is TRUE");
        }
    }

    /**
     * Constructor sin parametros. 
     * Llama al metodo init() e inicializa debug en FALSE.
     */
    MazoSorpresa() {
        init();
        debug = false;
    }
    
    /**
     * Añade la sorpresa al mazo si no se ha barajado.
     * @param s 
     */
    void alMazo(Sorpresa s) {
        if(!barajada) {
            sorpresas.add(s);
        }
    }

    /**
     * Saca la siguiente sorpresa del mazo.
     * Baraja si debug vale FALSE y se saca la sorpresa del mazo.
     * @return ultimaSorpresa
     */
    Sorpresa siguiente() {
        if(!barajada || usadas == sorpresas.size() && !debug) {
            Collections.shuffle(sorpresas);
            usadas = 0;
            barajada = true;
        }
        usadas++;     
        Sorpresa sorpresa = sorpresas.get(0);
        sorpresas.remove(0);
        sorpresas.add(sorpresa);
        ultimaSorpresa = sorpresa;
        return ultimaSorpresa;
    }
    
    /**
     * Inhabilita una carta del mazo y la saca.
     * @param sorpresa 
     */
    void inhabilitarCartaEspecial(Sorpresa sorpresa) {
        if(sorpresas.contains(sorpresa)) {
            cartasEspeciales.add(sorpresa);            
            sorpresas.remove(sorpresas.indexOf(sorpresa));
            Diario.getInstance().ocurreEvento("Carta INHABILITADA");
        }
    }  
    
    /**
     * Habilita una carta y la añade al mazo.
     * @param sorpresa 
     */
    void habilitarCartaEspecial(Sorpresa sorpresa) {
        if(cartasEspeciales.contains(sorpresa)) {
            sorpresas.add(sorpresa);            
            cartasEspeciales.remove(cartasEspeciales.indexOf(sorpresa));
            Diario.getInstance().ocurreEvento("Carta HABILITADA");
        }
    }

    /**
     * Consulta la ultima sorpresa.
     * @return ultimaSorpresa.
     */
    Sorpresa getUltimaSopresa() {
        return ultimaSorpresa;
    }
 
}