
module Civitas
  
  require_relative 'civitas_juego'
  require_relative 'operacion_inmobiliaria'
  require_relative 'operaciones_juego'
  require_relative 'respuestas'
  require_relative 'gestiones_inmobiliarias'
  require_relative 'salidas_carcel'

  
  class Controlador
    
    def initialize(juego, vista)
      @juego = juego
      @vista = vista
    end
    
    def juega
      @vista.set_civitas_juego(@juego)
      while !@juego.final_del_juego
        @vista.actualizar_vista
        @vista.pausa
        operacion = @juego.siguiente_paso
        @vista.mostrar_siguiente_operacion(operacion)
        if operacion != Civitas::OperacionesJuego::PASAR_TURNO
          @vista.mostrar_eventos
        end
        if !@juego.final_del_juego
          case operacion
          when Civitas::OperacionesJuego::COMPRAR
            res = @vista.comprar
            if res == Civitas::Respuestas::SI
              @juego.comprar
            end
            @juego.siguiente_paso_completado(operacion)
          when Civitas::OperacionesJuego::GESTIONAR
            @vista.gestionar
            @vista.iGestion
            @vista.iPropiedad
            lista = [Civitas::GestionesInmobiliarias::VENDER, 
              Civitas::GestionesInmobiliarias::HIPOTECAR, 
              Civitas::GestionesInmobiliarias::CANCELAR_HIPOTECA, 
              Civitas::GestionesInmobiliarias::CONSTRUIR_CASA, 
              Civitas::GestionesInmobiliarias::CONSTRUIR_HOTEL, 
              Civitas::GestionesInmobiliarias::TERMINAR]
            oi = OperacionInmobiliaria.new(lista[@vista.iGestion], 
              @vista.iPropiedad)
            case lista[@vista.iGestion]
            when Civitas::GestionesInmobiliarias::VENDER
              if !@juego.get_jugador_actual.get_nombre_propiedades != nil
                @juego.vender(oi.num_propiedad)
              end
            when Civitas::GestionesInmobiliarias::HIPOTECAR
              if !@juego.get_jugador_actual.get_nombre_propiedades != nil
                @juego.hipotecar(oi.num_propiedad)
              end
            when Civitas::GestionesInmobiliarias::CANCELAR_HIPOTECA
              if !@juego.get_jugador_actual.get_nombre_propiedades != nil
                @juego.cancelar_hipoteca(oi.num_propiedad)
              end
            when Civitas::GestionesInmobiliarias::CONSTRUIR_CASA
                @juego.construir_casa(oi.num_propiedad)
            when Civitas::GestionesInmobiliarias::CONSTRUIR_HOTEL
                @juego.construir_hotel(oi.num_propiedad)
            else
              @juego.siguiente_paso_completado(operacion)
            end
            
          when Civitas::OperacionesJuego::SALIR_CARCEL
            salida = @vista.salir_carcel
            case salida
            when Civitas::SalidasCarcel::PAGANDO
              @juego.salir_carcel_pagando
            when Civitas::SalidasCarcel::TIRANDO
              @juego.salir_carcel_tirando
            end
            @juego.siguiente_paso_completado(operacion)
          end
        else
          lista = Array.new
          lista = @juego.ranking
          lista.reverse
          for i in 0...lista.size
            puts lista.at(i).nombre + "\n"
          end
        end
      end
    end
    
  end
end
