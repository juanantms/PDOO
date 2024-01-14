# encoding:utf-8
module Civitas

  class SorpresaPorCasaHotel < Sorpresa
    
    def initialize(texto, valor)
      super(texto)
      @valor = valor
    end
    
    def aplicar_a_jugador(actual, todos)
      if jugador_correcto(actual, todos)
        super(actual, todos)
        todos.at(actual).modificar_saldo(@valor * 
          todos.at(actual).cantidad_casas_hoteles)
      end
    end
  
    def to_string
      return super.to_string + "\nValor: #{@valor}"
    end
    
  public_class_method :new  
  end
end