# encoding:utf-8
require 'singleton'

module Civitas
  
  class Dado
    
    include Singleton
    attr_reader :ultimo_resultado

    
    def initialize
      @debug = false
      @ultimo_resultado = 1
      @@salida_carcel = 5
    end
    
    def tirar
      if !@debug
        @ultimo_resultado = 1 + rand(6).to_i
      else
        return @ultimo_resultado
      end
      return @ultimo_resultado
    end
    
    def get_ultimo_resultado
      return @ultimo_resultado
    end
    
    def salgo_de_la_carcel
      tirada = tirar
      return tirada == 5
    end
    
    def quien_empieza(n)
      empieza = rand(n).to_i 
      return empieza
    end
    
    def set_debug(d)
      if d
        Diario.instance.ocurre_evento("Debug is TRUE")
        @debug = d
      else
        @debug = d
        Diario.instance.ocurre_evento("Debug is FALSE")
      end
    end
    
  end
end