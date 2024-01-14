# encoding:utf-8
module Civitas

  class JugadorEspeculador < Jugador
  
    @@factor_especulador = 2
    
    def initialize(jugador, fianza)
      jugador_copia(jugador)
      @fianza = fianza
      @especulador = true
    end

    def debe_ser_encarcelado
      res = false
      if super
        if !puede_pagar_fianza
          res = true
        end
      end
      return res
    end
    
    def puede_pagar_fianza
      puedo_pagar = false
      if @saldo >= @fianza
        modificar_saldo(-@fianza)
        puedo_pagar = true
      end
      return puedo_pagar
    end
    
    def puedo_edificar_casa(propiedad)
      if @encarcelado
        return false
      end
      if propiedad.num_casas < (@@casas_max*@@factor_especulador)
        return @saldo >= propiedad.precio_edificar
      else
        return false
      end
    end
    
    def puedo_edificar_hotel(propiedad)
      if @encarcelado
        return false
      end
      if propiedad.num_hoteles < (@@hoteles_max*@@factor_especulador) && 
          propiedad.num_casas >= 4
        return @saldo >= propiedad.precio_edificar
      else
        return false
      end
    end
    
    def paga_impuesto(cantidad)
      if @encarcelado
        return false
      else
        return paga(cantidad/@@factor_especulador)
      end
    end
    
    def get_casas_max
      return @@casas_max * @@factor_especulador
    end
    
    def get_hoteles_max
      return @@hoteles_max * @@factor_especulador
    end
    
    def to_string
      salida = "Jugador Especulador:\n" + "FIANZA: #{@fianza}\n" + 
        super.to_string
      return salida
    end
    
    def construir_casa(ip)
      result = false
      if @encarcelado
        return result
      else
        existe = existe_la_propiedad(ip)
        if existe
          propiedad = @propiedades.at(ip)
          puedo_edificar_casa = self.puedo_edificar_casa(propiedad)
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
      existe = existe_la_propiedad(ip)
      if existe
        propiedad = @propiedades.at(ip)
        puedo_edificar_hotel = self.puedo_edificar_hotel(propiedad)
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
    
    
  end
end