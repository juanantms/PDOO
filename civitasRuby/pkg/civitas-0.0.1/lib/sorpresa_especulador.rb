# encoding:utf-8
module Civitas

  class SorpresaEspeculador < Sorpresa
  
    def initialize(texto, fianza)
      super(texto)
      @fianza = fianza
    end

    def aplicar_a_jugador(actual, todos)
      if jugador_correcto(actual, todos)
        super(actual, todos)
        jugador = JugadorEspeculador.new(todos.at(actual), @fianza)
        todos[actual] = jugador
      end
    end
    
    def to_string
      return super.to_string + "\nFianza: #{@fianza}" 
    end
    
  public_class_method :new  
  end
end