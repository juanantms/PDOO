# encoding:utf-8
module Civitas

  class Tablero
    
    require_relative 'casilla'
    attr_reader :num_casilla_carcel

    
    def initialize(indcarcel)
      if indcarcel >= 1
        @num_casilla_carcel = indcarcel
      else
        @num_casilla_carcel = 1
      end
      @casillas = Array.new
      @casillas << Casilla.new("Salida")
      @por_salida = 0
      @tiene_juez = false
    end 
    
    def aniade_casilla(casilla)
      if @casillas.size == @num_casilla_carcel
        @casillas << Casilla.new("Carcel")
        @casillas << casilla
      else
        @casillas << casilla
      end
    end
    
    def aniade_juez
      if !@tiene_juez
        @casillas << Casilla.new("Juez")
        @tiene_juez = true
      end
    end
    
    def calcular_tirada(origen, destino)
      tirada = destino - origen;
      if !correcto(origen)
        return -1
      end
      if !correcto(destino)
        return -1
      end
      if tirada < 0
        @por_salida++ 
        tirada = tirada + @casillas.size
      end
      return tirada
    end
    
    def correcto
      correcto = false
      if (@casillas.size > @num_casilla_carcel) && @tiene_juez
        correcto = true
      end
      return correcto
    end
    
    def correcto_1(num_casilla)
      correcto1 = false
      if (num_casilla < @casillas.size) && correcto
        correcto1 = true
      end
      return correcto1
    end
    
    def get_carcel
      return @num_casilla_carcel
    end 
    
    def get_casilla(num_casilla)
      if correcto_1(num_casilla)
        return @casilla.at(num_casilla)
      else
        return nil
      end
    end
    
    def get_num_casillas
      return @casillas.size
    end
    
    def get_por_salida
      aux = @por_salida
      if @por_salida > 0
        @por_salida -=1
        return aux
      else
        return @por_salida
      end 
    end
    
    def nueva_posicion(actual, tirada)
      posfinal = actual + tirada
      if !correcto1(actual)
        return -1
      else
        if posfinal > @casillas.size
          posfinal = posfinal % @casillas.size
          @por_salida += 1
          return posfinal
        else
          return posfinal
        end
      end
    end
    
  end
end