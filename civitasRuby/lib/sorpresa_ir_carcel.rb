# encoding:utf-8
module Civitas

  class SorpresaIrCarcel < Sorpresa
  
    def initialize(texto, tablero)
      super(texto)
      @tablero = tablero
    end

    def aplicar_a_jugador(actual, todos)
      if jugador_correcto(actual, todos)
        super(actual, todos)
        todos.at(actual).encarcelar(@tablero.get_carcel)
      end
    end
    
    def to_string
      return @texto
    end
  
  public_class_method :new
  end
end