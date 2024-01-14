package civitas;


public class JugadorEspeculador extends Jugador {
    
    private static int FactorEspeculador = 2;
    private int fianza;
    
    JugadorEspeculador(Jugador jugador, int fianza) {
        super(jugador);
        especulador = true;
        this.fianza = fianza;
        this.actualizaPropietarioPorConversion();
    }
    
    @Override
    protected boolean debeSerEncarcelado() {
        boolean res = false;
        if(super.debeSerEncarcelado()) {
            if(!puedePagarFianza()) {
                res = true;
            }
        }
        return res;
    }
    
    private boolean puedePagarFianza() {
        boolean puedePagar = false;
        if(saldo >= fianza) {
            modificarSaldo(-(this.fianza));
            puedePagar = true;
        }
        return puedePagar;
    }
    
    private boolean puedoEdificarCasa(TituloPropiedad propiedad) {
        if(encarcelado) {
            return false;
        }
        if(propiedad.getNumCasas() < getCasasMax()) {
            return saldo <= propiedad.getPrecioEdificar();
        }else{
            return false;
        }
    }
    
    private boolean puedoEdificarHotel(TituloPropiedad propiedad) {
        if(encarcelado) {
            return false;
        }
        if(propiedad.getNumHoteles() < getHotelesMax() && 
                propiedad.getNumCasas() >= 4) {
            return saldo <= propiedad.getPrecioEdificar();
        }else{
            return false;
        }
    }
    
    @Override
    boolean pagaImpuesto(float cantidad) {
        if(encarcelado) {
            return false;
        }else{
            return paga(cantidad/FactorEspeculador);
        }
    }
    
    private void actualizaPropietarioPorConversion() {
        for(int i = 0; i < super.propiedades.size(); i++) {
            propiedades.get(i).setPropietario(this);
        }
    }
    
    @Override
    protected int getCasasMax() {
        return CasasMax*FactorEspeculador;
    }
    
    @Override
    protected int getHotelesMax() {
        return HotelesMax*FactorEspeculador;
    }
    
    @Override
    public String toString() {
        String salida = "Jugador Especulador:\n" + "Fianza: " + fianza + 
                super.toString();
        return salida;
    }
    
    @Override
    boolean construirCasa(int ip) {
        boolean result = false;
        if(encarcelado) {
            return result;
        }
        if(this.existeLaPropiedad(ip)) {
            TituloPropiedad propiedad = propiedades.get(ip);
            boolean puedoEdificarCasa = this.puedoEdificarCasa(propiedad);
            float precio = propiedad.getPrecioEdificar();
            if(puedoGastar(precio) && (propiedad.getNumHoteles() < 
                    this.getHotelesMax()) && (propiedad.getNumCasas() >= 
                    this.getCasasPorHotel())) {
                puedoEdificarCasa = true;
            }
            if(puedoEdificarCasa) {
                result = propiedad.construirCasa(this);
                if(result) {
                    Diario.getInstance().ocurreEvento("El jugador "+ 
                            this.getNombre() + 
                            " construye una casa en la propiedad " + 
                            propiedades.get(ip).getNombre());    
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
    @Override
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
                Diario.getInstance().ocurreEvento("El jugador "+this.getNombre() 
                        + " construye un hotel en la propiedad " + 
                        propiedades.get(ip).getNombre());
            }
        }
        return result;
    }
    
}
