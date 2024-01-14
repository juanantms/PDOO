# encoding:utf-8
module Civitas

  class SorpresaIrCasilla < Sorpresa
  
    def initialize(texto, tablero, valor)
      super(texto)
      @tablero = tablero
      @valor = valor
    end
    
    def aplicar_a_jugador(actual, todos)
      if jugador_correcto(actual, todos)
        super(actual, todos)
        casilla_actual = todos.at(actual).num_casilla_actual
        tirada = @tablero.calcular_tirada(casilla_actual, @valor)
        nueva_casilla = @tablero.nueva_posicion(casilla_actual, tirada)
        todos.at(actual).mover_a_casilla(nueva_casilla)
        @tablero.get_casilla(nueva_casilla).recibe_jugador(actual, todos)
      end
    end
    
    def to_string
      return super.to_string + "\nValor: #{@valor} " 
    end
  
  public_class_method :new  
  end
end