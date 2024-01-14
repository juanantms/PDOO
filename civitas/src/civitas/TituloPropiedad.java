package civitas;


public class TituloPropiedad {
    
    private float alquilerBase;
    private float precioCompra;
    private float precioEdificar;
    private float hipotecaBase;        
    private float factorRevalorizacion;
    private static float factorInteresesHipoteca = 1.1f;
    private boolean hipotecado;
    private String nombre;
    private int numCasas;
    private int numHoteles;
    private Jugador propietario;
    
    /**
     * Constructor
     * @param nombre
     * @param preciobasealquiler
     * @param factorrev
     * @param preciobasehipoteca
     * @param preciocompra
     * @param precioporedificar 
     */
    public TituloPropiedad(String nombre, float preciobasealquiler, 
            float factorrev,float preciobasehipoteca, float preciocompra, 
            float precioporedificar) { 
        
        hipotecado = false;
        numCasas = 0;
        numHoteles = 0;
        this.nombre = nombre;
        alquilerBase = preciobasealquiler;
        factorRevalorizacion = factorrev;
        hipotecaBase = preciobasehipoteca;
        precioCompra = preciocompra;
        precioEdificar = precioporedificar;
        propietario = null;
    }
    
    
    
    /**
     * Actualiza el propietario a uno nuevo .
     * @param jugador nuevo propietario.
     */
    void actualizaPropietarioPorConversion(Jugador jugador) {
        propietario = jugador;
    }

    /**
     * Cancela la hipoteca de la propiedad del jugador.
     * @param jugador propietario
     * @return TRUE si se cancela, FALSE si no.
     */
    boolean cancelarHipoteca(Jugador jugador) {
        if(hipotecado && esEsteElPropietario(jugador)) {
            propietario.paga(getImporteCancelarHipoteca());
            hipotecado = false;
            return true;
        }else{
            return false;
        }
    }

    /**
     * Consulta el numero de casas y hoteles.
     * @return numero de casas y hoteles.
     */
    int cantidadCasasHoteles() {
        return numCasas + numHoteles;
    }
    
    /**
     * Compra la propiedad.
     * @param jugador comprador.
     * @return TRUE si se compra, FALSE si no.
     */
    boolean comprar(Jugador jugador) {
        if(tienePropietario()) {
            return false;
        }else{
            propietario = jugador;
            propietario.paga(precioCompra);
            return true;
        }
    }
    
    /**
     * Construye una casa si es propietario de la propiedad y 
     * si tiene menos del maximo de casas permitidas en la propiedad.
     * @param jugador propietario
     * @return TRUE si se construye la casa, FALSE si no.
     */
    boolean construirCasa(Jugador jugador) {
        boolean result = false;
        if(esEsteElPropietario(jugador) && (numCasas < 4)) {
            result = true;
            jugador.paga(precioEdificar);
            numCasas++;
        }
        return result;
    }
    
    /**
     * Construye un hotel si es propietario de la propiedad y 
     * si tiene menos del numero de casas por hotel
     * @param jugador propietario
     * @return TRUE si se construye la casa, FALSE si no.
     */
    boolean construirHotel(Jugador jugador) {
        boolean result = false;
        if(esEsteElPropietario(jugador) && (numHoteles < 
                jugador.getCasasPorHotel())) {
            result = true;
            jugador.paga(precioEdificar*5);
            numHoteles++;
        }
        return result;
    }
    
    /**
     * Derruye n Casas del jugador en esa propiedad.
     * @param n: numero de casas.
     * @param jugador
     * @return TRUE si se han derruido, FALSE si no.
     */
    boolean derruirCasas(int n, Jugador jugador) {
        if(esEsteElPropietario(jugador) && numCasas>=n) {
            numCasas -= n;
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Derruye n Hoteles del jugador en esa propiedad.
     * @param n: numero de hoteles.
     * @param jugador
     * @return TRUE si se han derruido, FALSE si no.
     */
    boolean derruirHoteles(int n, Jugador jugador) {
        if(esEsteElPropietario(jugador) && numHoteles>=n) {
            numHoteles -= n;
            return true;
        }else{
            return false;
        }
    }

    /**
     * Comprueba si es el propietario de la propiedad.
     * @param jugador
     * @return 
     */
    private boolean esEsteElPropietario(Jugador jugador) {
        return jugador == propietario;
    }
    
    /**
     * Consulta si esta hipotecado o no.
     * @return TRUE si esta, FALSE si no.
     */
    public boolean getHipotecado() {
        return hipotecado;
    }

    /**
     * Devuelve el importe de la hipoteca.
     * @return importe.
     */
    private float getImporteHipoteca() {
        return hipotecaBase * (1f + (numCasas * 0.5f) + (numHoteles * 2.5f));
    }

    /**
     * Devuelve el importe que se obtiene al hipotecar el
     * título multiplicado por factorInteresesHipoteca(10%)
     * @return importe.
     */
    float getImporteCancelarHipoteca() {
        return factorInteresesHipoteca * this.getImporteHipoteca();
    }

    /**
     * Consulta el nombre de la propiedad.
     * @return nombre.
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Consulta numero de casas.
     * @return numCasas.
     */
    public int getNumCasas() {
        return numCasas;
    }
    
    /**
     * Consulta el numero de hoteles.
     * @return numHoteles.
     */
    public int getNumHoteles() {
        return numHoteles;
    }
    
    /**
     * Devuelve el precio del alquiler calculado según las reglas del
     * juego. Si el título se encuentra hipotecado o si el propietario está
     * encarcelado el precio del alquiler será cero.
     * @return precio de alquiler.
     */
    private float getPrecioAlquiler() {
        float precioalquiler;
        if(propietarioEncarcelado() || hipotecado) {
            precioalquiler = 0;
        }else{
            precioalquiler = alquilerBase * (1 + (numCasas*0.5f) +
                    (numHoteles * 2.5f));   
        }
        return precioalquiler;
    }
    
    /**
     * Consulta  el precio de compra.
     * @return precioCompra.
     */
    float getPrecioCompra() {
        return precioCompra;
    }
    
    /**
     * Consulta el precio para edificar.
     * @return precioEdificar
     */
    float getPrecioEdificar() {
        return precioEdificar;
    }
    
    /**
     * Devuelve la suma del precio de compra con el precio de edificar las
     * casas y hoteles que tenga, multiplicado éste último por el factor 
     * de revalorización.
     * @return precio de venta.
     */
    private float getPrecioVenta() {
        float suma;
        suma = precioCompra + precioEdificar * (5*numHoteles + numCasas)
                * factorRevalorizacion;
        return suma;
    }
    
    /**
     * Obtiene el propietario de la propiedad.
     * @return el propietario.
     */
    Jugador getPropietario(){
        return propietario;
    }
    
    /**
     * Hipoteca la propiedad del jugador.
     * @param jugador propietario.
     * @return TRUE si se hipoteca, FALSE si no.
     */
    boolean hipotecar(Jugador jugador){
        if(!hipotecado && esEsteElPropietario(jugador)) {
            propietario.recibe(getImporteHipoteca());
            hipotecado = true;
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Comprueba si el propietario se encuentra en la carcel.
     * @return TRUE si el propietario esta en la carcel, FALSE si no.
     */
    private boolean propietarioEncarcelado() {
        return !(!propietario.encarcelado || !tienePropietario());
    }
    
    public void setPropietario(JugadorEspeculador jugador) {
        propietario = jugador;
    }
    
    /**
     * Devuelve true si tiene propietario.
     * @return TRUE si tiene propietario, FALSE si no.
     */
    boolean tienePropietario() {
        return propietario!=null;
    }
    
    /**
     * Proporciona una representacion en forma de caracteres del estado completo
     * de la propiedad
     * @return salida
     */
    @Override
    public String toString() {
        String salida;
        if(tienePropietario()) {
            salida = "Titulo de la Propiedad: \n" 
                    + "Nombre: " + nombre 
                    + "\nPropietario: " + propietario.getNombre()
                    + "\nHipotecada: " + hipotecado 
                    + "\nPrecio de Compra: " + precioCompra 
                    + "\nAlquiler base: " + alquilerBase 
                    + "\nHipoteca base: " + hipotecaBase
                    + "\nFactor de intereses de la hipoteca: " 
                    + factorInteresesHipoteca
                    + "\nPrecio para edificar: " + precioEdificar                     
                    + "\nNumero de Casas: " + numCasas 
                    + "\nNumero de Hoteles: " + numHoteles 
                    + "\nFactor de Revalorizacion: " + factorRevalorizacion;
        }else{
            salida = "Titulo de la Propiedad: \n" 
                    + "Nombre: " + nombre 
                    + "\nPrecio de Compra: " + precioCompra 
                    + "\nNo tiene Propietario.";
        }
        return salida;
    }

    /**
     * Tramita el alquiler de un jugador.
     * @param jugador
     */
    void tramitarAlquiler(Jugador jugador) {
        if(tienePropietario() && !esEsteElPropietario(jugador)) {
            float precio = getPrecioAlquiler();
            jugador.pagaAlquiler(precio);
            propietario.recibe(precio);
        }
    }
    
    /**
     * Se vende una propiedad del jugador.
     * @param jugador
     * @return TRUE si se puede vender FALSE si no.
     */
    Boolean vender(Jugador jugador) {
        if(esEsteElPropietario(jugador) && !hipotecado) {
            propietario.recibe(getPrecioVenta());
            derruirCasas(numCasas, jugador);
            derruirHoteles(numHoteles, jugador);
            actualizaPropietarioPorConversion(null);
            return true;
        }else{
            return false;
        }
    }
}