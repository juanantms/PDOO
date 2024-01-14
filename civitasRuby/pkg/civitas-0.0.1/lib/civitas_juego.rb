# encoding:utf-8

require_relative 'tablero'
require_relative 'casilla'
require_relative 'casilla_calle'
require_relative 'casilla_juez'
require_relative 'casilla_impuesto'
require_relative 'casilla_sorpresa'
require_relative 'gestor_estados'
require_relative 'titulo_propiedad'
require_relative 'jugador'
require_relative 'operaciones_juego'
require_relative 'sorpresa'
require_relative 'sorpresa_ir_carcel'
require_relative 'sorpresa_ir_casilla'
require_relative 'sorpresa_pagar_cobrar'
require_relative 'sorpresa_por_casa_hotel'
require_relative 'sorpresa_por_jugador'
require_relative 'sorpresa_salir_carcel'
require_relative 'sorpresa_especulador'

module Civitas
  

  class CivitasJuego
  
    def initialize(nombres)
      @jugadores = Array.new
      for i in 0..nombres.size
        @jugadores << Jugador.new(nombres.at(i))
      end
      @indice_jugador_actual = Dado.instance.quien_empieza(@jugadores.size)
      @gestor_estados = Gestor_estados.new
      @estado = @gestor_estados.estado_inicial
      @mazo = MazoSorpresa.new(false)
      inicializa_tablero(@mazo)
      inicializa_mazo_sorpresa(@tablero)
    end
  
    def cancelar_hipoteca(ip)
      return @jugadores.at(@indice_jugador_actual).cancelar_hipoteca(ip)
    end
    
    def comprar
      jugador_actual = @jugadores.at(@indice_jugador_actual)
      num_casilla_actual = jugador_actual.num_casilla_actual
      casilla = @tablero.get_casilla(num_casilla_actual)
      titulo = casilla.titulo_propiedad
      resultado = jugador_actual.comprar(titulo)
      return resultado
    end
    
    def construir_casa(ip)
      @jugadores.at(@indice_jugador_actual).construir_casa(ip)
    end
    
    def construir_hotel(ip)
      @jugadores.at(@indice_jugador_actual).construir_hotel(ip)
    end
    
    def final_del_juego
      resultado = false
      for i in 0...@jugadores.size
        if @jugadores.at(i).en_bancarrota
          resultado = true
        end
      end
      return resultado
    end 
    
    def get_casilla_actual
      return @tablero.get_casilla(@jugadores.at(@indice_jugador_actual).
          num_casilla_actual)
    end
    
    def get_jugador_actual
      return @jugadores.at(@indice_jugador_actual)
    end
    
    def hipotecar(ip)
      @jugadores.at(@indice_jugador_actual).hipotecar(ip)
    end
    
    def info_jugador_texto
      @jugadores.at(@indice_jugador_actual).to_string
    end
    
    def salir_carcel_pagando
      return @jugadores.at(@indice_jugador_actual).salir_carcel_pagando
    end
    
    def salir_carcel_tirando
      return @jugadores.at(@indice_jugador_actual).salir_carcel_tirando
    end
    
    def siguiente_paso
      jugador_actual = @jugadores.at(@indice_jugador_actual)
      operacion = @gestor_estados.operaciones_permitidas(jugador_actual,@estado)
      if operacion == Civitas::OperacionesJuego::PASAR_TURNO
        pasar_turno
        siguiente_paso_completado(operacion)
      else
        if operacion == Civitas::OperacionesJuego::AVANZAR
          avanzar_jugador
          siguiente_paso_completado(operacion)
        end
      end
      return operacion
    end
    
    def siguiente_paso_completado(operacion)
      @estado = @gestor_estados.siguiente_estado(
        @jugadores.at(@indice_jugador_actual), @estado, operacion)
    end
    
    def vender(ip)
      return @jugadores.at(@indice_jugador_actual).vender(ip)
    end
    
    def ranking
      rank = []
      for i in 0..@jugadores.size
        rank << @jugadores.at(i)
      end
      rank.sort
      return rank
    end
    
    
    private
    
    
    def avanzar_jugador
      jugador_actual = @jugadores.at(@indice_jugador_actual)
      posicion_actual = jugador_actual.num_casilla_actual
      tirada = Dado.instance.tirar
      posicion_nueva = @tablero.nueva_posicion(posicion_actual, tirada)
      casilla = @tablero.get_casilla(posicion_nueva)
      contabilizar_pasos_por_salida(jugador_actual)
      jugador_actual.mover_a_casilla(posicion_nueva)
      casilla.recibe_jugador(@indice_jugador_actual, @jugadores)
      contabilizar_pasos_por_salida(jugador_actual)
    end
    
    def contabilizar_pasos_por_salida(jugador)
      while @tablero.get_por_salida > 0
        jugador.pasa_por_salida
      end
    end
    
    def inicializa_mazo_sorpresa(tablero)
      
      @mazo.al_mazo(SorpresaEspeculador.new("Te conviertes en jugador especulador, ahora eres admin aprovecha las ventajas.", 100))
      @mazo.al_mazo(SorpresaPagarCobrar.new("Ganas 500 berries por el flow que tienes.", 500))
      @mazo.al_mazo(SorpresaPagarCobrar.new("Por pringado te roban el Iphone y gastas 500 berries por uno nuevo.", -500))
      @mazo.al_mazo(SorpresaPagarCobrar.new("Recibes 200 berries por cada casa y hotel.", 200))
      @mazo.al_mazo(SorpresaPagarCobrar.new("Pagas 100 berries por cada casa y hotel.", -100))
      @mazo.al_mazo(SorpresaIrCarcel.new("Vas a la carcel por pesado.", tablero ))
      @mazo.al_mazo(SorpresaIrCasilla.new("Avanza hasta la Salida.", tablero, 0))
      @mazo.al_mazo(SorpresaIrCasilla.new("Ve a la casilla de descanso.", tablero, 9))
      @mazo.al_mazo(SorpresaIrCasilla.new("Avanza hasta la Salida", tablero, 0))        
      @mazo.al_mazo(SorpresaSalirCarcel.new("Obtienes la llave de la celda y escapas de la carcel.", @mazo))
      @mazo.al_mazo(SorpresaIrCarcel.new("Vas a la carcel por OTAKU", tablero))
      @mazo.al_mazo(SorpresaPorJugador.new("Los demas jugadores te pagan 150 berries porque estas mamadisimo y tienen miedo.", 150))
      @mazo.al_mazo(SorpresaPorJugador.new("Pagas  150 berries a los demas jugadores porque hueles peste.", -150))
      
    end
    
    def inicializa_tablero(mazo)
      @tablero = Tablero.new(11)
      @mazo = mazo
      
      @tablero.aniade_casilla(CasillaCalle.new("Arlong Park", TituloPropiedad.new("Arlong Park", 100, 1.1, 650, 1000, 200)))
      @tablero.aniade_casilla(CasillaCalle.new("Loguetown", TituloPropiedad.new("Loguetown", 110, 1.1, 660, 1100, 210)))
      @tablero.aniade_casilla(CasillaSorpresa.new("Surpriseeee!!!", mazo))
      @tablero.aniade_casilla(CasillaCalle.new("Alabasta" ,TituloPropiedad.new("Alabasta", 120, 1.20, 670, 1200, 220)))
      @tablero.aniade_casilla(CasillaCalle.new("Skypea", TituloPropiedad.new("Skypea", 130, 1.20, 680, 1300, 230)))
      @tablero.aniade_casilla(CasillaImpuesto.new("Multa por feo", 550))
      @tablero.aniade_casilla(CasillaCalle.new("Water 7", TituloPropiedad.new("Water 7", 140, 1.25, 690, 1400, 240)))
      @tablero.aniade_casilla(CasillaCalle.new("Thriller Bark", TituloPropiedad.new("Thriller Bark", 150, 1.25, 700, 1500, 250)))
      @tablero.aniade_casilla(Casilla.new("Descanso por relleno"))
      @tablero.aniade_casilla(CasillaCalle.new("Enies Lobby", TituloPropiedad.new("Enies Lobby", 160 , 1.3, 710, 1600, 260)))
      @tablero.aniade_juez
      @tablero.aniade_casilla(CasillaCalle.new("Archipielago Sabaody", TituloPropiedad.new("Archipielago Sabaody", 170, 1.3, 720, 1700, 270)))
      @tablero.aniade_casilla(CasillaCalle.new("Marineford", TituloPropiedad.new("Marineford", 200, 1.4, 750, 1900, 300)))
      @tablero.aniade_casilla(CasillaSorpresa.new("Surpriseeee!!!", mazo))
      @tablero.aniade_casilla(CasillaCalle.new("Dressrosa", TituloPropiedad.new("Dressrosa", 210, 1.4, 760, 2000, 310)))
      @tablero.aniade_casilla(CasillaCalle.new("Whole Cake", TituloPropiedad.new("Whole Cake", 220, 1.4, 780, 2100, 320)))
      @tablero.aniade_casilla(CasillaImpuesto.new("Multado por la cara", 650))
      @tablero.aniade_casilla(CasillaSorpresa.new("Surpriseeee!!!", mazo))
      @tablero.aniade_casilla(CasillaCalle.new("Wano", TituloPropiedad.new("Wano", 240, 1.5, 800, 2250, 350)))  
      @tablero.aniade_casilla(CasillaCalle.new("Raftel", TituloPropiedad.new("Raftel", 270, 1.6, 830, 2500, 375)))
      
    end
    
    def pasar_turno
      @indice_jugador_actual += 1
      if @indice_jugador_actual == @jugadores.size
        @indice_jugador_actual = 0
      end
    end
    

  end

end