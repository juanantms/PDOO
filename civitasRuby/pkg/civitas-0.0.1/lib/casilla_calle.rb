# encoding:utf-8
module Civitas
  
  class CasillaCalle < Casilla
    attr_reader :titulo_propiedad

    def initialize(nombre, titulo)
      super(nombre)
      @titulo_propiedad = titulo
    end
    
    def recibe_jugador(iactual, todos)
      if jugador_correcto(iactual, todos)
        super(iactual, todos)
        jugador = todos.at(iactual)
        if !@titulo_propiedad.tiene_propietario
          jugador.puede_comprar_casilla
        else
          @titulo_propiedad.tramitar_alquiler(jugador)
        end
      end
    end
    
    def to_string
      salida = "Casilla : #{@nombre} \n" + @titulo_propiedad.to_string
      return salida
    end
    
  public :to_string  
  end
end