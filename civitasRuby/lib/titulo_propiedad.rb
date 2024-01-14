# encoding: utf-8
module Civitas
  
  class TituloPropiedad
    
    attr_reader :nombre,:num_casas,:num_hoteles,:hipotecado,:propietario,
      :precio_compra,:precio_edificar
    
    def initialize(nombre, alquiler_base, factor_revalorizacion, hipoteca_base, 
        precio_compra, precio_edificar)
      @alquiler_base = alquiler_base
      @@factor_intereses_hipoteca = 1.1
      @factor_revalorizacion = factor_revalorizacion
      @hipoteca_base = hipoteca_base
      @nombre = nombre
      @precio_compra = precio_compra
      @precio_edificar = precio_edificar
      @hipotecado = false
      @num_casas = 0
      @num_hoteles = 0
      @propietario = nil
      @ip = 0
    end
    
    def self.titulo_propiedad(nombre, ab, fr, hb, pc, pe)
      new(nombre, ab, fr, hb, pc, pe)
    end
    
    def actualiza_propietario_por_conversion(jugador)
      @propietario = jugador
    end
    
    def cancelar_hipoteca(jugador)
      if es_este_el_propietario(jugador) && @hipotecado
        @propietario.paga(get_importe_cancelar_hipoteca)  
        @hipotecado = false
        return true
      else
        return false
      end
    end
    
    def cantidad_casas_hoteles
      return @num_casas + @num_hoteles
    end
    
    def comprar(jugador)
      if !tiene_propietario && jugador.paga(@precio_compra)
        actualiza_propietario_por_conversion(jugador)
        return true
      else
        return false
      end
    end
    
    def construir_casa(jugador)
      if es_este_el_propietario(jugador) && jugador.paga(@precio_edificar) &&
          @num_casas < jugador.get_casas_max
        @num_casas += 1
        return true
      else
        return false
      end
    end
    
    def construir_hotel(jugador)
      if es_este_el_propietario(jugador) && jugador.paga(@precio_edificar*5) &&
          @num_casas<jugador.get_casas_max && @num_hoteles<jugador.get_hoteles_max
        @num_hoteles += 1
        @num_casas -= 4
        return true
      else
        return false
      end
    end
    
    def derruir_casas(n, jugador)
      if es_este_el_propietario(jugador) && @num_casas >= n
        @num_casas -= n
        return true
      else
        return false
      end
    end
    
    def derruir_hoteles(n, jugador)
      if es_este_el_propietario(jugador) && @num_hoteles >= n
        @num_hoteles -= n
        return true
      else
        return false
      end
    end
    
    def get_hipoteca
      return @hipoteca
    end
    
    def get_importe_cancelar_hipoteca
      return get_importe_hipoteca * @@factor_intereses_hipoteca
    end
    
    def get_nombre
      return @nombre
    end
    
    def get_num_casas
      return @num_casas
    end
    
    def get_num_hoteles
      return @num_hoteles
    end
    
    def get_precio_compra
      return @precio_compra
    end
    
    def get_precio_edificar
      return @precio_edificar
    end
    
    def get_propietario
      return @propietario
    end
    
    def hipotecar(jugador)
      if !@hipotecado && es_este_el_propietario(jugador)
        @propietario.recibe(get_importe_hipoteca)
        @hipotecado = true
        return true
      else
        return false
      end
    end
    
    def tiene_propietario
      return @propietario != nil
    end
       
    def to_string
      if tiene_propietario
        salida = " Titulo de la Propiedad:
        Nombre: #{@nombre}
        Propietario: #{@propietario.nombre}
        Precio compra: #{@precio_compra}
        Alquiler base: #{@alquiler_base}
        Hipoteca base: #{@hipotecabase}
        Factor de intereses de la hipoteca: #{@@factor_intereses_hipoteca}
        Precio para edificar: #{@precio_edificar}
        Número casas: #{@num_casas}
        Número hoteles: #{@num_hoteles}
        Factor de Revaloracizacion: #{@factor_revalorizacion}
        "  
      else
        salida = "No tiene Propietario."
      end
      return salida
    end
    
    def tramitar_alquiler(jugador)
      if tiene_propietario && !es_este_el_propietario(jugador)
        jugador.paga_alquiler(get_precio_alquiler)
        @propietario.recibe(get_precio_alquiler)
      end
    end
    
    def vender(jugador)
      if !@hipotecado && es_este_el_propietario(jugador)
        @propietario.recibe(get_precio_venta)
        derruir_casas(@num_casas, jugador)
        derruir_hoteles(@num_hoteles, jugador)
        actualiza_propietario_por_conversion(nil)
        return true
      else
        return false
      end
    end
   
    private
    
    def es_este_el_propietario(jugador)
      return @propietario.equal?(jugador)
    end
    
    def get_importe_hipoteca
      return @hipoteca_base * (1 + (@num_casas * 0.5) + (@num_hoteles * 2.5))
    end
    
    def get_precio_venta
      return @factor_revalorizacion * (@precio_edificar * 
        (@num_casas + 5 * @num_hoteles) + @precio_compra)
    end
       
    def get_precio_alquiler
      if propietario_encarcelado || @hipotecado
        precioalquiler = 0
      else
        precioalquiler = @alquiler_base * (1 + (@num_casas * 0.5) 
          + (@num_hoteles * 2.5))
      end
      return precioalquiler
    end
    
    def propietario_encarcelado
      return !(!@propietario.is_encarcelado || !tiene_propietario)
    end
    
  end

end

