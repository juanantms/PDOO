package civitas;

import java.util.ArrayList;
import GUI.Dado;


public class CivitasJuego {
    
    private int indiceJugadorActual;
    private EstadosJuego estado;
    private GestorEstados gestor;
    private ArrayList<Jugador> jugadores;
    private MazoSorpresa mazo;
    private Tablero tablero;


    private void avanzaJugador() {
        Jugador jugadorActual = getJugadorActual();
        int posicionActual = jugadorActual.getNumCasillaActual();
        int tirada = Dado.getInstance().tirar();
        int posicionNueva = tablero.nuevaPosicion(posicionActual, tirada);
        Casilla casilla = tablero.getCasilla(posicionNueva);
        this.contabilizarPasosPorSalida(jugadorActual);
        jugadorActual.moverACasilla(posicionNueva);
        casilla.recibeJugador(indiceJugadorActual, jugadores);
        this.contabilizarPasosPorSalida(jugadorActual);
    }
    
    public boolean cancelarHipoteca(int ip) {
        return jugadores.get(indiceJugadorActual).cancelarHipoteca(ip);
    }

    public CivitasJuego(ArrayList<String> nombres) {
        jugadores = new ArrayList();
        for(int i = 0; i < nombres.size(); i++) {
            jugadores.add(new Jugador(nombres.get(i)));
        }
        gestor = new GestorEstados();
        estado = gestor.estadoInicial();
        indiceJugadorActual = Dado.getInstance().quienEmpieza(jugadores.size());
        mazo = new MazoSorpresa();
        inicializaTablero(mazo);
        inicializaMazoSorpresa(tablero);
    }
    
    public boolean comprar() {
        boolean res;
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        int numCasillaActual = jugadorActual.getNumCasillaActual();
        Casilla casilla = tablero.getCasilla(numCasillaActual);
        TituloPropiedad titulo = ((CasillaCalle)casilla).getTituloPropiedad();
        res = jugadorActual.comprar(titulo);
        return res;
    }

    public boolean construirCasa(int ip) {
        return jugadores.get(indiceJugadorActual).construirCasa(ip);
    }

    public boolean construirHotel(int ip) {
        return jugadores.get(indiceJugadorActual).construirHotel(ip);
    }

    private void contabilizarPasosPorSalida(Jugador jugadorActual) {
        if(tablero.getPorSalida() > 0) {
            jugadorActual.pasaPorSalida();
        }
    }

    public boolean finalDelJuego() {
        boolean fin = false;
        for(int i = 0; i < jugadores.size(); i++) {
            if(jugadores.get(i).enBancarrota()) {
                fin = true;
            }
        }
        return fin;
    }

    public Casilla getCasillaActual() {
        return tablero.getCasilla(jugadores.get(indiceJugadorActual)
                .getNumCasillaActual());
    }

    public Jugador getJugadorActual() {
        return jugadores.get(indiceJugadorActual);
    }

    public boolean hipotecar(int ip) {
        return jugadores.get(indiceJugadorActual).hipotecar(ip);
    }

    public String infoJugadorTexto() {
        return jugadores.get(indiceJugadorActual).toString() 
                + "\nInformacion de la casilla actual:\n"  
                + getCasillaActual().toString();
    }

    private void inicializaMazoSorpresa(Tablero tablero) {
        
        mazo.alMazo(new SorpresaEspeculador(100, "Te conviertes en jugador especulador, ahora eres admin aprovecha las ventajas."));
        mazo.alMazo(new SorpresaPagarCobrar(500, "Ganas 500 berries por el flow que tienes."));
        mazo.alMazo(new SorpresaPagarCobrar(-500, "Por pringado te roban el Iphone y gastas 500 berries por uno nuevo."));
        mazo.alMazo(new SorpresaPagarCobrar(200, "Recibes 200 berries por cada casa y hotel."));
        mazo.alMazo(new SorpresaPagarCobrar(-100, "Pagas 100 berries por cada casa y hotel."));
        mazo.alMazo(new SorpresaIrCarcel(tablero, "Vas a la carcel por pesado."));
        mazo.alMazo(new SorpresaIrCasilla(tablero, 0, "Avanza hasta la Salida."));
        mazo.alMazo(new SorpresaIrCasilla(tablero, 9, "Ve a la casilla de descanso."));
        mazo.alMazo(new SorpresaIrCasilla(tablero, 0, "Avanza hasta la Salida"));        
        mazo.alMazo(new SorpresaSalirCarcel(mazo, "Obtienes la llave de la celda y escapas de la carcel."));
        mazo.alMazo(new SorpresaIrCarcel(tablero, "Vas a la carcel por OTAKU"));
        mazo.alMazo(new SorpresaPorJugador(150, "Los demas jugadores te pagan 150 berries porque estas mamadisimo y tienen miedo."));
        mazo.alMazo(new SorpresaPorJugador(-150, "Pagas  150 berries a los demas jugadores porque hueles peste."));        
                
    }

    private void inicializaTablero(MazoSorpresa mazo) {
        
        tablero = new Tablero(11);
        this.mazo = mazo;
        
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Arlong Park", 100, 1.1f, 650, 1000, 200)));
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Loguetown", 110, 1.1f, 660, 1100, 210)));
        tablero.aniadeCasilla(new CasillaSorpresa(mazo, "Surpriseeee!!!")); 
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Alabasta", 120, 1.20f, 670, 1200, 220)));
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Skypea", 130, 1.20f, 680, 1300, 230)));
        tablero.aniadeCasilla(new CasillaImpuesto(550, "Multa por feo"));
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Water 7", 140, 1.25f, 690, 1400, 240)));
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Thriller Bark", 150, 1.25f, 700, 1500, 250)));
        tablero.aniadeCasilla(new Casilla("Descanso por relleno"));
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Enies Lobby", 160 , 1.3f, 710, 1600, 260)));
        tablero.aniadeJuez();
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Archipielago Sabaody", 170, 1.3f, 720, 1700, 270)));
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Marineford", 200, 1.4f, 750, 1900, 300)));
        tablero.aniadeCasilla(new CasillaSorpresa(mazo, "Surpriseeee!!!"));   
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Dressrosa", 210, 1.4f, 760, 2000, 310)));
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Whole Cake", 220, 1.4f, 780, 2100, 320)));
        tablero.aniadeCasilla(new CasillaImpuesto(650, "Multado por la cara"));
        tablero.aniadeCasilla(new CasillaSorpresa(mazo, "Surpriseeee!!!")); 
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Wano", 240, 1.5f, 800, 2250, 350)));   
        tablero.aniadeCasilla(new CasillaCalle(new TituloPropiedad("Raftel", 270, 1.6f, 830, 2500, 375)));
       
    }

    private void pasarTurno() {
        indiceJugadorActual++;
        if(indiceJugadorActual == jugadores.size()) {
            indiceJugadorActual = 0;
        }
    }

    public ArrayList<Jugador> ranking() {
        ArrayList<Jugador>ranking = jugadores;
        for(int i = 0; i < ranking.size() - 1; i++) {
            for(int j = i + 1; j < ranking.size(); j++) {    
                if(ranking.get(i).compareTo(ranking.get(j)) < 0) {
                    Jugador aux = new Jugador(ranking.get(i));
                    ranking.set(i, ranking.get(j));
                    ranking.set(j, aux);
                }
            }
        }
        return ranking;
    }

    public boolean salirCarcelPagando() {
        return jugadores.get(indiceJugadorActual).salirCarcelPagando();
    }

    public boolean salirCarcelTirando() {
        return jugadores.get(indiceJugadorActual).salirCarcelTirando();
    }

    public OperacionesJuego siguientePaso() {
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        OperacionesJuego operacion = 
                gestor.operacionesPermitidas(jugadorActual, estado);
        if(operacion == OperacionesJuego.PASAR_TURNO) {
            this.pasarTurno();
            this.siguientePasoCompletado(operacion);
        }
        if(operacion == OperacionesJuego.AVANZAR) {
            this.avanzaJugador();
            this.siguientePasoCompletado(operacion);
        }
        return operacion;
    }

    public void siguientePasoCompletado(OperacionesJuego operacion) {
        estado = gestor.siguienteEstado(jugadores.get(indiceJugadorActual), 
                estado, operacion);
    }

    public boolean vender(int ip) {
        return jugadores.get(indiceJugadorActual).vender(ip);
    }
}
