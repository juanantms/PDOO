package civitas;

import java.util.ArrayList;

/**
 * CLASE TABLERO
 */
public class Tablero {

    private int numCasillaCarcel;
    private int porSalida;
    private boolean tieneJuez;
    private ArrayList<Casilla> casillas = null;
    
    
    /**
     * Constructor de la clase Tablero
     * @param indCarcel: Recibe el parametro indCarcel que nos indica donde 
     *                   esta la carcel. 
     */
    public Tablero(int indCarcel) {
        /*
        * La carcel no puede estra en la casilla 0, la casilla de Salida.
        */
        if(indCarcel >= 1){
            numCasillaCarcel = indCarcel;
        }else{
            numCasillaCarcel = 1;
        }
        
        casillas = new ArrayList();
        casillas.add(new Casilla("Salida"));
        porSalida = 0;
        tieneJuez = false;
    }
    

    /**
     * Funcion que comprueba si las condiciones del tablero son correctas.
     * @return true si son correctas.
     */
    private boolean correcto() {
        return (casillas.size() > numCasillaCarcel) && tieneJuez;
    }
    
    /**
     * Comprueba si numCasilla es un indice valido.
     * @param numCasilla
     * @return true si correcto() tambien devuelve true y numCasilla es valido.
     */
    private boolean correcto(int numCasilla) {
        return correcto() && numCasilla < casillas.size();
    }
    
    /**
    * Consultor de numCasillaCarcel.
    * @return la posicion de la carcel en el tablero.
    */
    int getCarcel() {
        return numCasillaCarcel;
    }
    
    /**
     * Consulta el numero de casillas en el tablero.
     * @return el numero de casillas.
     */
    int getNumCasillas() {
        return casillas.size();
    }
    
    /**
    * Consultor de porSalida.
    * @return el valor de porSalida.
    */
    public int getPorSalida() {
        if(porSalida > 0){
            int aux = porSalida;
            porSalida--;
            return aux;
        }else{
            return porSalida;
        }
    }

    /**
     * Añade la casilla carcel o la casilla que se pasa por el parametro.
     * @param casilla tipo de casilla.
     */
    void aniadeCasilla(Casilla casilla) {
        if(casillas.size() == numCasillaCarcel) {
            casillas.add(new Casilla("Impel Down"));  // Carcel
            casillas.add(casilla);
        }else{
            casillas.add(casilla);
        }
    }
    
    /**
     * Añade una casilla en la posicion pos.
     * @param casilla : Casilla
     * @param pos : Posicion de la casilla
     */
    /*void aniadeCasilla(Casilla casilla, int pos) {
        if(casillas.size() == numCasillaCarcel) {
            casillas.add(new Casilla(numCasillaCarcel, "Carcel"));
        }
        casillas.add(pos, casilla);
    }*/

    /**
     * Añade una casilla juez si no la hay.
     */
    void aniadeJuez() {
        if(!tieneJuez) {
            casillas.add(new CasillaJuez(numCasillaCarcel, "Juez"));
            tieneJuez = true;
        }
    }
    
    /**
     * Devuelve la casilla del indice numCasilla.
     * @param numCasilla indice de la casilla.
     * @return la casilla si el indice es valido si no es valido devuelve null.
     */
    Casilla getCasilla(int numCasilla) {
        if(correcto(numCasilla)) {
            return casillas.get(numCasilla);
        }else{
            return null;
        }
    }

    /**
     * Calcula la posicion al final del turno.
     * @param actual: posicion inicial.
     * @param tirada: resultado de tirar los dados.
     * @return posicion final.
     */
    int nuevaPosicion(int actual, int tirada) {
       /* int posfinal = actual + tirada;
        
        if(!correcto(actual)) {
            return -1;
        }else{
            if(posfinal > this.casillas.size()) {
                posfinal = posfinal%casillas.size();
                porSalida++;
                return posfinal;                
            }else{
                return posfinal;
            }
        }   */
        
        if(correcto()){
        int a = actual + tirada;
        int b = (actual + tirada)%casillas.size();
            if(a == b)
                return a;
            else {
                porSalida++;
                return b;
            }
        }else{
            return -1;
        }
    }

    /**
     * Calcula la tirada conseguida mediante los dados.
     * @param origen posicion inicial.
     * @param destino posicion final.
     * @return la tirada de los dados.
     */
    int calcularTirada(int origen, int destino) {
        int tirada = destino - origen;  
        
        if(!correcto(origen) || !correcto(destino)) {
            return -1;
        }
        if(tirada < 0) {
            porSalida++;
            tirada = tirada + casillas.size();
        }
        return tirada;
    }           
}