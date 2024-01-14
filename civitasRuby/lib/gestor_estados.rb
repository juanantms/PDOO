require_relative 'diario'
require_relative 'operaciones_juego'

module Civitas
  class Gestor_estados

    def estado_inicial
      return (Civitas::Estados_juego::INICIO_TURNO)
    end

    def operaciones_permitidas(jugador,estado)
      op = nil

      case estado

      when Civitas::Estados_juego::INICIO_TURNO
        if (jugador.encarcelado)
          op = Civitas::OperacionesJuego::SALIR_CARCEL
        else
          op = Civitas::Operacionesjuego::AVANZAR
        end

      when Civitas::Estados_juego::DESPUES_CARCEL
        op = Civitas::OperacionesJuego::PASAR_TURNO

      when Civitas::Estados_juego::DESPUES_AVANZAR
        if (jugador.encarcelado)
          op = Civitas::OperacionesJuego::PASAR_TURNO
        else
          if (jugador.puede_comprar)
            op = Civitas::OperacionesJuego::COMPRAR
          else
            if (jugador.tiene_algo_que_gestionar)
              op = Civitas::OperacionesJuego::GESTIONAR
            else
              op = Civitas::OperacionesJuego::PASAR_TURNO
            end
          end
        end

      when Civitas::Estados_juego::DESPUES_COMPRAR
        if (jugador.tiene_algo_que_gestionar)
          op = Civitas::OperacionesJuego::GESTIONAR
        else
          op = Civitas::OperacionesJuego::PASAR_TURNO
        end

      when Civitas::Estados_juego::DESPUES_GESTIONAR
        op = OperacionesJuego::PASAR_TURNO
      end

      return op
    end



    def siguiente_estado(jugador, estado, operacion)
      siguiente = nil

      case estado

      when Civitas::Estados_juego::INICIO_TURNO
        if (operacion==Civitas::OperacionesJuego::SALIR_CARCEL)
          siguiente = Estados_juego::DESPUES_CARCEL
        else
          if (operacion==Civitas::OperacionesJuego::AVANZAR)
            siguiente = Civitas::Estados_juego::DESPUES_AVANZAR
          end
        end


      when Civitas::Estados_juego::DESPUES_CARCEL
        if (operacion==Civitas::OperacionesJuego::PASAR_TURNO)
          siguiente = Civitas::Estados_juego::INICIO_TURNO
        end

      when Civitas::Estados_juego::DESPUES_AVANZAR
        case operacion
          when Civitas::Operacionesjuego::PASAR_TURNO
            siguiente = Civitas::Estados_juego::INICIO_TURNO
          when
            Civitas::OperacionesJuego::COMPRAR
              siguiente = Civitas::Estados_juego::DESPUES_COMPRAR
          when Civitas::Operacionesjuego::GESTIONAR
              siguiente = Civitas::Estados_juego::DESPUES_GESTIONAR
        end


      when Civitas::Estados_juego::DESPUES_COMPRAR
        #if (jugador.tiene_algo_que_gestionar)
        if (operacion==Civitas::Operacionesjuego::GESTIONAR)
          siguiente = Civitas::Estados_juego::DESPUES_GESTIONAR
        #  end
        else
          if (operacion==Civitas::Operacionesjuego::PASAR_TURNO)
            siguiente = Civitas::Estados_juego::INICIO_TURNO
          end
        end

      when Civitas::Estados_juego::DESPUES_GESTIONAR
        if (operacion==Civitas::Operacionesjuego::PASAR_TURNO)
          siguiente = Civitas::Estados_juego::INICIO_TURNO
        end
      end

      Diario.instance.ocurre_evento("De: "+estado.to_s+ " con "+operacion.to_s+ 
          " sale: "+siguiente.to_s)

      return siguiente
    end

  end
end
