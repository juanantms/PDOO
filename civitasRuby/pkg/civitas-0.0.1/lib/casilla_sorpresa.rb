# encoding:utf-8
module Civitas

  class CasillaSorpresa < Casilla
  
    def initialize(nombre, mazo)
      super(nombre)
      @mazo = mazo
    end
    
    def recibe_jugador(iactual, todos)
      if jugador_correcto(iactual, todos)
        sorpresa = @mazo.siguiente
        super(iactual, todos)
        sorpresa.aplicar_a_jugador(iactual, todos)
      end
    end
    
    def to_string
      salida = "Casilla : #{@nombre} \n"
      return salida
    end

  public :to_string
  end
end