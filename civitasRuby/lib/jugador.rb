# encoding:utf-8
module Civitas
  
  
  class Jugador
    
    include Comparable

    attr_reader :nombre,:num_casilla_actual,:casas_max,:casas_por_hotel,
      :propiedades,:puede_comprar,:saldo,:encarcelado,:salvoconducto,
      :especulador

    def initialize(nombre)
      @@casas_max = 4
      @@casas_por_hotel = 4
      @encarcelado = false
      @@hoteles_max = 4
      @nombre = nombre
      @num_casilla_actual = 0
      @@paso_por_salida = 1000
      @@precio_libertad = 200
      @puede_comprar = true
      @saldo = 7500
      @@saldo_inicial = 7500
      @salvoconducto = nil
      @propiedades = Array.new
      @especulador = false
    end
    
    def jugador_copia(otro)
      @nombre = otro.nombre
      @encarcelado = otro.encarcelado
      @num_casilla_actual = otro.numCasillaActual
      @puede_comprar = otro.puedeComprar
      @saldo = otro.saldo
      @salvoconducto = otro.salvoconducto
      @propiedades = otro.propiedades
      for i in @propiedades
        i.actualiza_propietario_por_conversion(self)  
      end
      @especulador = otro.especulador
    end
    
    def self.jugador(otro)
      @nombre = otro.nombre
      @encarcelado = otro.encarcelado
      @num_casilla_actual = otro.numCasillaActual
      @puede_comprar = otro.puedeComprar
      @saldo = otro.saldo
      @salvoconducto = otro.salvoconducto
      @propiedades = otro.propiedades
    end
    
    def cancelar_hipoteca(ip)
        result = false
      if @encarcelado
        return result
      end
      if existe_la_propiedad(ip)
        propiedad = @propiedades.at(ip)
        cantidad = propiedad.get_importe_cancelar_hipoteca
        puedo_gastar = puedo_gastar(cantidad)
        if puedo_gastar
          result = propiedad.cancelar_hipoteca(self)
          if result
            Diario.instance.ocurre_evento("El jugador #{@nombre}" + 
                "cancela la hipoteca de la propiedad #{ip.to_s}")
          end
        end
      end
      return result
    end
    
    def cantidad_casas_hoteles
      cantidad = 0
      for i in (0..@propiedades.size - 1)
        cantidad += @propiedades.at(i).num_casas
        cantidad += @propiedades.at(i).num_hoteles
      end
      return cantidad
    end
    
    def compare_to(otro)
      return @saldo <=> otro.get_saldo
    end
    
    def comprar(titulo)
      result = false
      if @encarcelado
        return result
      end
      if @puede_comprar
        precio = titulo.precio_compra
        if puedo_gastar(precio)
          result = titulo.comprar(self)
          if result
            @propiedades << titulo
            Diario.instance.ocurre_evento("El jugador #{@nombre}" + 
                "comprala propiedad #{titulo.to_string}")
          end
          @puede_comprar = false
        end
      end
      return result
    end
    
    def construir_casa(ip)
      result = false
      if @encarcelado
        return result
      else
        existe = existe_la_propiedad(ip)
        if existe
          propiedad = @propiedades.at(ip)
          puedo_edificar_casa = puedo_edificar_casa(propiedad)
          if puedo_edificar_casa
            result = propiedad.construir_casa(self)
            if result
              Diario.instance.ocurre_evento("El jugador #{@nombre}" +
                  "construye una casa en la propiedad #{ip.to_s}")
            end
          end 
        end
      end
      return result
    end
    
    def construir_hotel(ip)
      result = false
      if @encarcelado
        return result
      end
      if existe_la_propiedad(ip)
        propiedad = @propiedades.at(ip)
        puedo_edificar_hotel = puedo_edificar_hotel(propiedad)
        precio = propiedad.precio_edificar
        if puedo_gastar(precio) && propiedad.num_hoteles < @@hoteles_max && 
            propiedad.num_casas >= @@casas_por_hotel
          puedo_edificar_hotel = true
        end
        if puedo_edificar_hotel
          result = propiedad.construir_hotel(self)
          propiedad.derruir_casa(@@casas_por_hotel, self)
          Diario.instance.ocurre_evento("El jugador #{@nombre}" + 
              "construye un hotel en la propiedad #{ip.to_s}")
        end
      end
      return result
    end
    
    def debe_ser_encarcelado
      if @encarcelado
        return false
      else
        if !tiene_salvoconducto
          return true
        else
          perder_salvoconducto
          Diario.instance.ocurre_evento("El jugador #{@nombre} se 
libra de la carcel")
          return false
        end
      end
    end
    
    def en_bancarrota
      return @saldo <= 0
    end
    
    def encarcelar(num_casilla_carcel)
      if debe_ser_encarcelado
        mover_a_casilla(num_casilla_carcel)
        @encarcelado = true
        Diario.instance.ocurre_evento("Se ha encarcelado al jugador #{@nombre}")
        return @encarcelado
      end
    end
    
    def get_casas_por_hotel
      return @@casas_por_hotel
    end
    
    def get_especulador
      return @especulador
    end
    
    def get_nombre
      return @nombre
    end
    
    def get_num_casilla_actual
      return @num_casilla_actual
    end
    
    def get_propiedades
      nombres =  Array.new
      for i in 0..@propiedades.size
        nombres << @propiedades.at(i).nombre
      end
      return nombres
    end
    
    def get_puede_comprar
      return @puede_comprar
    end
    
    def get_saldo
      return @saldo
    end
    
    def get_salvoconducto
      return @salvoconducto
    end
    
    def hipotecar(ip)
      result = false
      if @encarcelado
        return result
      end
      if existe_la_propiedad(ip)
        propiedad = @propiedades.at(ip)
        result= propiedad.hipotecar(self)
      end
      if result
        Diario.instance.ocurre_evento("El jugador #{@nombre} hipoteca 
la propiedad #{ip.to_s}")
      end
      return result
    end
    
    def is_encarcelado
      return @encarcelado
    end
    
    def modificar_saldo(cantidad)
      @saldo += cantidad
      Diario.instance.ocurre_evento("Se ha modificado el saldo." +
          "\nNuevo saldo: #{@saldo}")
    end
    
    def mover_a_casilla(num_casilla)
      if @encarcelado
        return false
      else
        @num_casilla_actual = num_casilla
        @puede_comprar = false
        Diario.instance.ocurre_evento("El juagdor #{@nombre} se ha movido a 
la casilla #{@num_casilla_actual.to_s}")
        return true
      end
    end
    
    def obtener_salvoconducto(sorpresa)
      if @encarcelado
        return false
      else
        @salvoconducto = sorpresa
        return true
      end
    end
    
    def paga(cantidad)
      paga = modificar_saldo(cantidad * -1)
      return paga
    end
    
    def paga_alquiler(cantidad)
      if @encarcelado
        return false
      else
        paga = paga(cantidad)
        return paga
      end
      
    end
    
    def paga_impuesto(cantidad)
      if @encarcelado
        return false
      else
        paga = paga(cantidad)
        return paga
      end
    end
    
    def pasa_por_salida
      modificar_saldo(@@paso_por_salida)
      Diario.instance.ocurre_evento("El jugador #{@nombre} ha recibido 
#{@@paso_por_salida.to_s} por pasar po la Salida.")
    end
    
    def puede_comprar_casilla
      @puede_comprar = !encarcelado
      return @puedo_comprar
    end
    
    def recibe(cantidad)
      if @encarcelado
        return false
      else
        recibe = modificar_saldo(cantidad)
        return recibe
      end
    end
    
    def salir_carcel_pagando
      if @encarcelado && puede_salir_carcel_pagando
        paga(@@precio_libertad)
        @encarcelado = false
        Diario.instance.ocurre_evento("El jugador #{@nombre} ha pagado 
#{@@precio_libertad} y sale de la carcel.")
        return true
      else 
        return false
      end
    end
    
    def salir_carcel_tirando
      if Dado.instance.salgo_de_la_carcel
        @encarcelado = false
        Diario.instance.ocurre_evento("El jugador #{@nombre} sale de la carcel 
gracias a los dados.")
        return true
      else 
        return false
      end
    end
    
    def tiene_algo_que_gestionar
      return @propiedades != nil

    end
    
    def tiene_salvoconducto
      return @salvoconducto != nil
    end
    
    def to_string
      for i in 0...@propiedades.size
        todas = todas + @propiedades.at(i).nombre + " "
      end
      salida = "Nombre: " + @nombre + "\nSaldo: " + @saldo.to_s + 
        "\nCasilla actual: " + @num_casilla_actual.to_s + 
        "\n¿Esta encarcelado? " + @encarcelado.to_s +
        "\nNumero de propiedades: " + @propiedades.size.to_s + 
        "\nPropiedades:\n" + todas
      return salida
    end
    
    def vender(ip)
      if @encarcelado
        return false
      else
        if existe_la_propiedad(ip)
          vendida = @propiedades.at(ip).vender(self)
          if vendida
            Diario.instance.ocurre_evento("La propiedad 
#{@propiedades.at(ip).nombre} ha sido vendida por #{@nombre}")
            @propiedades.delete_at(ip)
            return true
          else
            return false
          end
        else 
          return false
        end
      end
    end
    
    def get_nombre_propiedades
      nombres = Array.new
      for i in 0...@propiedades.size
        nombres << @propiedades[i].nombre
      end
      return nombres
    end
    
    
    private
    
    def existe_la_propiedad(ip)
      return ip <= @propiedades.size
    end
    
    def get_casas_max
      return @@casas_max
    end
    
    def get_hoteles_max
      return @@hoteles_max
    end
    
    def get_precio_libertad
      return @@precio_libertad
    end
    
    def get_premio_paso_salida
      return @@paso_por_salida
    end
    
    def perder_salvoconducto
      @salvoconducto.usada
      @salvoconducto = nil
    end
    
    def puede_salir_carcel_pagando
      return @saldo>=@@PrecioLibertad
    end
    
    def puedo_edificar_casa(propiedad)
      if @encarcelado
        return false
      else
        if propiedad.num_casas <= @@casas_max
          return @saldo >= propiedad.precio_edificar
        else
          return false
        end
      end
    end
    
    def puedo_edificar_hotel(propiedad)
      if @encarcelado
        return false
      else
        if (propiedad.num_hoteles <= @@hoteles_max) && (propiedad.num_casas ==4)
          return @saldo >= propiedad.precio_edificar
        else
          return false
        end
      end
    end
    
    def puedo_gastar(precio)
      if @encarcelado
        return false
      else
        return @saldo >= precio
      end
    end
    
    
  end
end