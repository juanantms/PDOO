package civitas;

import static java.lang.Float.compare;
import java.util.ArrayList;
import GUI.Dado;

/**
 *
 * 
 */
public class Jugador implements Comparable<Jugador>{
   
    static protected int CasasMax = 4;
    static protected int CasasPorHotel = 4;
    protected boolean encarcelado;    
    static protected int HotelesMax = 4;   
    private String nombre;   
    private int numCasillaActual;   
    static protected float PasoPorSalida = 1000f;    
    static protected float PrecioLibertad = 200f;    
    private boolean puedeComprar;    
    protected float saldo;    
    static float SaldoInicial = 7500f;
    protected SorpresaSalirCarcel salvoconducto;    
    protected ArrayList<TituloPropiedad> propiedades;
    protected  boolean especulador;
    
    
    /**
     * Cancela la hipoteca de una propiedad.
     * @param ip
     * @return 
     */
    boolean cancelarHipoteca(int ip) {
        boolean result = false;
        if(encarcelado) {
            return result;
        }
        if(existeLaPropiedad(ip)) {
            TituloPropiedad propiedad = propiedades.get(ip);
            float cantidad = propiedad.getImporteCancelarHipoteca();
            boolean puedoGastar = puedoGastar(cantidad);
            if(puedoGastar) {
                result = propiedad.cancelarHipoteca(this);
                if(result) {
                    Diario.getInstance().ocurreEvento("El jugador " + nombre +
                            " cancela la hipoteca de la propiedad " + ip);
                }
            }
        }
        return result;
    }
    
    /**
     * Calcula el numero de casas mas hoteles.
     * @return cantidad.
     */
    int cantidadCasasHoteles() {
        int cantidad = 0;
        for(int i = 0; i < propiedades.size(); i++) {
            cantidad += propiedades.get(i).cantidadCasasHoteles();
        }
        return cantidad;
    }
    
    /**
     * Compara dos jugadores por su saldo.
     * @param otro
     * @return 
     */
    @Override
    public int compareTo(Jugador otro) {
        return compare(saldo, otro.getSaldo());
    }
    
    /**
     * Compra una propiedad.
     * @param titulo : de la propiedad.
     * @return TRUE si se ha comprado, FALSE si no.
     */
    boolean comprar(TituloPropiedad titulo) {
        boolean result = false;
        if(encarcelado) {
            return result; 
        }
        if(puedeComprar) {
            float precio = titulo.getPrecioCompra();
            if(puedoGastar(precio)) {
                result = titulo.comprar(this);
                if(result) {
                    propiedades.add(titulo);
                    Diario.getInstance().ocurreEvento("El jugador " + nombre + 
                            " ha comprado la propiedad " + titulo.toString());                    
                }
                puedeComprar = false;
            }
        }
        return result;
    }
    
    /**
     * Construye una casa en la propiedad actual.
     * @param ip : identificador de la propiedad.
     * @return TRUE si se construye la casa, FALSE si no.
     */
    boolean construirCasa(int ip) {                                
        boolean result = false;
        boolean puedoEdificarCasa;
        if(encarcelado){
            return result;
        }else{
            if(this.existeLaPropiedad(ip)) {
                TituloPropiedad propiedad = propiedades.get(ip);
                puedoEdificarCasa = this.puedoEdificarCasa(propiedad);
                float precio = propiedad.getPrecioEdificar();
                if(puedoGastar(precio) && propiedad.getNumCasas() < getCasasMax()) {
                    puedoEdificarCasa = true;
                }
                if(puedoEdificarCasa) {
                    result = propiedad.construirCasa(this);
                    if(result) {
                        Diario.getInstance().ocurreEvento("El jugador " + nombre
                                + " construye una casa en la propiedad " + 
                                propiedades.get(ip).getNombre());
                    }
                } 
            }
        }
        return result;
    }
    
    /**
     * Construye un hotel en la propiedad actual.
     * @param ip : identificador de la propiedad.
     * @return TRUE si se construye el hotel, FALSE si no.
     */
    boolean construirHotel(int ip) {
        boolean result = false;
        if(encarcelado) {
            return result;
        }
        if(this.existeLaPropiedad(ip)) {
            TituloPropiedad propiedad = propiedades.get(ip);
            boolean puedoEdificarHotel = this.puedoEdificarHotel(propiedad);
            float precio = propiedad.getPrecioEdificar();
            if(puedoGastar(precio) && (propiedad.getNumHoteles() < 
                    this.getHotelesMax()) && (propiedad.getNumCasas() >= 
                    this.getCasasPorHotel())) {
                puedoEdificarHotel = true;
            }
            if(puedoEdificarHotel) {
                result = propiedad.construirHotel(this);
                propiedad.derruirCasas(CasasPorHotel, this);
                Diario.getInstance().ocurreEvento("El jugador " + nombre + 
                        " construye un hotel en la propiedad " + 
                        propiedades.get(ip).getNombre());
            }
        }
        return result;
    }
    
    /**
     * Comprueba si el jugador debe ser encarcelado.
     * @return TRUE si el jugador debe ser encarcelado.
     */
    protected boolean debeSerEncarcelado() {
        if(isEncarcelado()) {
            return false;
        }else{
            if(!tieneSalvoconducto()) {
                return true;
            }else{
                perderSalvoconducto();
                Diario.getInstance().ocurreEvento("El jugador " + nombre + 
                        " se libra de la carcel");
                return false;
            }    
        }
    }
    
    /**
     * Comprueba si el jugador esta en bancarrota.
     * @return TRUE si el jugador esta en bancarrota.
     */
    boolean enBancarrota() {
        return saldo <= 0;
    }
    
    /**
     * Encarcela al jugador si debe ser encarcelado y se informa.
     * @param numCasillaCarcel casilla de la carcel.
     * @return TRUE si se encarcela, FALSE si no.
     */
    boolean encarcelar(int numCasillaCarcel) {
        if(debeSerEncarcelado()) {
            moverACasilla(numCasillaCarcel);
            encarcelado = true;
            Diario.getInstance().ocurreEvento("El jugador " + nombre 
                    + " ha sido encarcelado");
        }
        return encarcelado;
    }

    /**
     * Comprueba si existe la propiedad.
     * @param ip indice de la propiedad.
     * @return TRUE si existe, FALSE si no.
     */
    protected boolean existeLaPropiedad(int ip) {
        return ip <= propiedades.size();
    }
    
    /**
     * Consulta el maximo de casas.
     * @return CasasMax.
     */
    protected int getCasasMax() {
        return CasasMax;
    }
    
    /**
     * Consulta el numero de casas por hotel.
     * @return CasasPorHotel.
     */
    int getCasasPorHotel() {
        return CasasPorHotel;
    }
    
    public boolean getEspeculador() {
        return especulador;
    }
    
    /**
     * Consulta el numero maximo de hoteles.
     * @return HotelesMax.
     */
    protected int getHotelesMax() {
        return HotelesMax;
    }
    
    /**
     * Consulta el nombre de la propiedad.
     * @return nombre de la propiedad.
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Consulta el nombre de las propiedades
     * @return 
     */
    public ArrayList<String> getNombrePropiedades(){
       ArrayList<String> nombres = new ArrayList<>();
       for(int i = 0; i < propiedades.size(); i++) {
           nombres.add(propiedades.get(i).getNombre());
       }
       return nombres;
    }
    
    
    /**
     * Consulta la casilla actual.
     * @return casilla actual.
     */
    int getNumCasillaActual() {
        return numCasillaActual;
    }
    
    /**
     * Consulta el precio a pagar para salir libre.
     * @return PrecioLibertad.
     */
    private float getPrecioLibertad() {
        return PrecioLibertad;
    }
    
    /**
     * Consulta el precio del paso por salida.
     * @return PasoPorSalida.
     */
    private float getPremioPasoSalida() {
        return PasoPorSalida;
    }
    
    /**
     * Consulta si tiene salvoconducto.
     * @return salvoconducto.
     */
    Sorpresa getSalvoconducto(){
        return salvoconducto;
    }
    
    /**
     * Consulta las propiedades del jugador.
     * @return propiedades.
     */
    public ArrayList<TituloPropiedad> getPropiedades() {
        return propiedades;
    }
    
    /**
     * Consulta si se puede comprar la propiedad.
     * @return TRUE si puede, FALSE si no.
     */
    boolean getPuedeComprar() {
        return puedeComprar;
    }
    
    /**
     * Consulta el saldo del jugador.
     * @return saldo.
     */
    public float getSaldo() {
        return saldo;
    }
    
    /**
     * Hipoteca la propiedad del jugador.
     * @param ip : identificador de la propiedad.
     * @return TRUE si hipoteca, FALSE si no.
     */
    boolean hipotecar(int ip) {
        boolean resultado = false;
        if(encarcelado) {
            return resultado;
        }
        if(existeLaPropiedad(ip)) {
            TituloPropiedad propiedad = propiedades.get(ip);
            resultado = propiedad.hipotecar(this);
        }
        if(resultado) {
            Diario.getInstance().ocurreEvento("El jugador " + nombre +
                    " hipoteca la propiedad " + ip);
        }
        return resultado;
    }
    
    /**
     * Comprueba si el jugador esta encarcelado.
     * @return TRUE si esta encarcelado, FALSE si no.
     */
    public boolean isEncarcelado() {
        return encarcelado;
    }
    
    /**
     * Constructor que recibe un nombre.
     * @param nombre 
     */
    Jugador(String nombre) {
        this.nombre = nombre;
        numCasillaActual = 0;
        saldo = 7500;
        puedeComprar = true;
        propiedades = new ArrayList<>();
        encarcelado = false;
        salvoconducto = null;
        especulador = false;
    }
    
    /**
     * Constructor que copia un jugador en otro.
     * @param otro 
     */
    protected Jugador(Jugador otro) {
        this.nombre = otro.nombre;
        this.encarcelado = otro.encarcelado;
        this.numCasillaActual = otro.numCasillaActual;
        this.puedeComprar = otro.puedeComprar;
        this.saldo = otro.saldo;
        this.salvoconducto = otro.salvoconducto;
        this.propiedades = otro.propiedades;
        this.especulador = otro.especulador;
    }
    
    /**
     * Modifica el saldo del jugador sumandole una cantidad.
     * @param cantidad
     * @return TRUE si se ha modificado, FALSE si no.
     */
    boolean modificarSaldo(float cantidad) {
        this.saldo += cantidad;
        Diario.getInstance().ocurreEvento("Se ha modificado el saldo de "+nombre 
                + " y es de " + saldo);
        return true;
    }
    
    /**
     * Se mueve el jugador a la casilla pasada como parametro 
     * si no esta encarcelado.
     * @param numCasilla
     * @return TRUE si se ha movido, FALSE si no.
     */
    boolean moverACasilla(int numCasilla) {
        if(isEncarcelado()) {
            return false;
        }else{
            numCasillaActual = numCasilla;
            puedeComprar = false;
            Diario.getInstance().ocurreEvento("El jugador " + nombre + 
                    " se mueve a la casilla " + 
                    Float.toString(numCasillaActual));
            return true;
        }
    }
    
    /**
     * El jugador obtiene un salvoconducto.
     * @param sorpresa
     * @return TRUE si obtiene salvoconducto, FALSE si no.
     */
    boolean obtenerSalvoconducto(SorpresaSalirCarcel sorpresa) {
        if(encarcelado) {
            return false;
        }else{
            salvoconducto = sorpresa;
            return true;
        }
    }
    
    /**
     * El jugador paga una cantidad y se le modifica el saldo.
     * @param cantidad
     * @return TRUE si se ha modificado el saldo, FALSE si no.
     */
    boolean paga(float cantidad) {
        return modificarSaldo(cantidad * (-1));
    }
    
    /**
     * El jugador paga el alquiler si no esta encarcelado.
     * @param cantidad
     * @return TRUE si lo paga, FALSE si no.
     */
    boolean pagaAlquiler(float cantidad) {
        if(isEncarcelado()) {
            return false;
        }else{
            return paga(cantidad);
        }
    }

    /**
     * El jugador paga el impuesto si no esta encarcelado.
     * @param cantidad
     * @return TRUE si lo paga, FALSE si no.
     */
    boolean pagaImpuesto(float cantidad) {
        if(isEncarcelado()) {
            return false;
        }else{
            return paga(cantidad);
        }
    }
    
    /**
     * Comprueba si el jugador pasa por la salida y se le cobra.
     * @return siempre TRUE.
     */
    boolean pasaPorSalida() {
        modificarSaldo(PasoPorSalida);
        Diario.getInstance().ocurreEvento("El jugador " + nombre + " recibe " + 
                Float.toString(PasoPorSalida) + " por pasar por la salida");
        return true;
    }
    
    /**
     * El jugador pierde salvoconducto.
     */
    private void perderSalvoconducto() {
        salvoconducto.usada();
        salvoconducto = null;
    }
    
    /**
     * Consulta si el jugador puede comprar una casilla.
     * @return TRUE si puede comprar, FALSE si no.
     */
    boolean puedeComprarCasilla() {
        puedeComprar = !encarcelado;
        return puedeComprar;
    }
    
    /**
     * Comprueba si puede salir de la carcel pagando.
     * @return TRUE si puede salir pagando, FALSE si no.
     */
    private boolean puedoSalirCarcelPagando() {
        return saldo >= PrecioLibertad;
    }
    
    /**
     * Comprueba si se puede edificar una casa.
     * @param propiedad
     * @return TRUE si se puede, FALSE si no.
     */
    private boolean puedoEdificarCasa(TituloPropiedad propiedad) {
        if(encarcelado) {
            return false;
        }
        if(propiedad.getNumCasas() <= CasasMax) {
            return saldo >= propiedad.getPrecioEdificar();
        }else{
            return false;
        }
    }
    
    /**
     * Comprueba si se puede edificar un hotel.
     * @param propiedad
     * @return TRUE si se puede, FALSE si no.
     */
    private boolean puedoEdificarHotel(TituloPropiedad propiedad) {
        if(encarcelado) {
            return false;
        }
        if((propiedad.getNumHoteles() <= HotelesMax) && 
                (propiedad.getNumCasas() == CasasMax)) {
            return saldo >= propiedad.getPrecioEdificar();
        }else{
            return false;
        }
    }
    
    /**
     * Consulta si el jugador puede gastarse ese dinero.
     * @param precio
     * @return TRUE si puede gastarlo, FALSE si no.
     */
    protected boolean puedoGastar(float precio) {
        if(isEncarcelado()) {
            return false;
        }else{
            return saldo >= precio;
        }
    }
    
    /**
     * EL jugador recibe una cantidad de dinero. si no esta encarcelado.
     * @param cantidad
     * @return TRUE si recibe, FALSE si no.
     */
    boolean recibe(float cantidad) {
        if(isEncarcelado()) {
            return false;
        }else{
            return modificarSaldo(cantidad);
        }
    }
    
    /**
     * Comprueba si puede salir de la carcel pagando si esta encarcelado.
     * Informa si el saldo del jugador es mayor al precio de salir de la carcel.
     * @return TRUE si puede salir, FALSE si no.
     */
    boolean salirCarcelPagando() {
        if(encarcelado&& puedoSalirCarcelPagando()) {
            paga(PrecioLibertad);
            encarcelado = false;
            Diario.getInstance().ocurreEvento("El jugador " + nombre + 
                    " ha pagado " + Float.toString(PrecioLibertad) + 
                    " y puede salir de la carcel");
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Comprueba si se puede salir de la carcel tirando.
     * @return TRUE si puede salir, FALSE si no.
     */
    boolean salirCarcelTirando() {
        if(Dado.getInstance().salgoDeLaCarcel()) {
            encarcelado = false;
            Diario.getInstance().ocurreEvento("El jugador " + nombre + 
                    " sale de la carcel gracias a los dados");
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Comprueba si tiene que gestionar alguna casa u hotel.
     * @return TRUE si tiene casas u hoteles, FALSE si no tiene.
     */
    boolean tieneAlgoQueGestionar() {
        return propiedades != null;
    }
    
    /**
     * Comprueba si tiene un salvoconducto.
     * @return TRUE si tiene, FALSE si no.
     */
    boolean tieneSalvoconducto() {
        return salvoconducto!=null;
    }
    /**
     * Proporciona una informacion del jugador con una cadena de caracteres.
     * @return salida
     */
    @Override 
    public String toString() {
        String todas = "", aux;
        
        for(int i = 0; i < propiedades.size(); i++) {
            aux = propiedades.get(i).toString();
            todas = todas + " ";
            todas = todas + aux;
        }
        String salida = "Jugador: " + getNombre() +
                "\nCasilla Actual: " + numCasillaActual +
                "\nSaldo: " + saldo + 
                "\nNumero de propiedades: " + propiedades.size() + 
                "\nPropiedades: \n" + todas;
      
        return salida;
    }
    
    /**
     * Se vende la propiedad (ip).
     * @param ip indice de la propiedad.
     * @return TRUE si se vende, FALSE si no.
     */
    boolean vender(int ip) {
        if(isEncarcelado()) {
            return false;
        }else{
            if(existeLaPropiedad(ip)) {
                if(propiedades.get(ip).vender(this)) {
                    Diario.getInstance().ocurreEvento("Se ha vendido la "
                            + "propiedad" + propiedades.get(ip).getNombre() +
                            " del jugador " + this.nombre);
                    propiedades.remove(ip);
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
    }
}