# encoding:utf-8
module Civitas
  
  class Casilla
    
    attr_reader :nombre,:titulo_propiedad

    def initialize(nombre)
      @nombre = nombre
    end
   
    def jugador_correcto(iactual, todos)
      return iactual <= todos.size
    end
    
    def recibe_jugador(iactual, todos)
        informe(iactual,todos)
    end  
    
    def to_string
      salida = "Casilla : #{@nombre} \n"
      return salida
    end
    
    def informe(iactual, todos)
      if jugador_correcto(iactual, todos)
        Diario.instance.ocurre_evento("El jugador #{todos.at(iactual).nombre} 
ha caido en la casilla " + to_string)
      end
    end
    
  private :informe  
  end
end