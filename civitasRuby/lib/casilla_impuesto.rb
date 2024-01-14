# encoding:utf-8
module Civitas
  
  class CasillaImpuesto < Casilla
    
    def initialize(nombre, valor)
      super(nombre)
      @importe = valor
    end
    
    def recibe_jugador(iactual, todos)
      if jugador_correcto(iactual, todos)
        super(iactual, todos)
        todos.at(iactual).paga_impuesto(@importe)
      end
    end
    
    def to_string
      salida = "Casilla : #{@nombre} \n" + "Importe: #{@importe} \n"
      return salida
    end
  
  public :to_string    
  end
end