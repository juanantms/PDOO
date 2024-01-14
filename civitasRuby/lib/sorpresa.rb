# encoding:utf-8

require_relative 'jugador'
require_relative 'sorpresa'

module Civitas
  
  
  
  class Sorpresa
    
    def initialize(texto)
      @texto = texto
    end
    
    def aplicar_a_jugador(actual, todos)
      informe(actual, todos)
    end
    
    def jugador_correcto(actual, todos)
      return actual <= todos.size 
    end

    def informe(actual, todos)
      Diario.instance.ocurre_evento("El jugador #{todos.at(actual).nombre} 
obtiene la sorpresa #{@texto} \n")
    end
     
    def to_string
      return @texto
    end
    
  private_class_method :new
  private :informe
  end
 end
 