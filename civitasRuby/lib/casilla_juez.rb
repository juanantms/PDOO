# encoding:utf-8
module Civitas

  class CasillaJuez < Casilla
  
    def initialize(nombre, carcel)
      super(nombre)
      @carcel = carcel
    end
  
    def recibe_jugador(iactual, todos)
      if jugador_correcto(iactual, todos)
        super(iactual, todos)
        todos.at(iactual).encarcelar(@carcel)
      end
    end
    
    def to_string
      salida = "Casilla : #{@nombre} \n"
      return salida
    end
   
  public :to_string  
  end
end