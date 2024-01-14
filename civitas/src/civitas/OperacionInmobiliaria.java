package civitas;


public class OperacionInmobiliaria {
    
    private int numPropiedad;
    private GestionesInmobiliarias gestion;
    
    public OperacionInmobiliaria(GestionesInmobiliarias gest, int ip) {
        gestion = gest;
        numPropiedad = ip;
    }
    
    public GestionesInmobiliarias getGestion(){
        return gestion;
    }
    
    public int getNumPropiedad(){
        return numPropiedad;
    }
}
