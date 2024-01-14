#encoding:utf-8
require_relative 'operaciones_juego'
require 'io/console'
require_relative 'civitas_juego'
require_relative 'operacion_inmobiliaria'
require_relative 'respuestas'
require_relative 'gestiones_inmobiliarias'
require_relative 'salidas_carcel'


module Civitas

  class Vista_textual

    attr_reader :iGestion, :iPropiedad

    
    def mostrar_estado(estado)
      puts estado
    end

    
    def pausa
      print "Pulsa una tecla"
      STDIN.getch
      print "\n"
    end

    def lee_entero(max,msg1,msg2)
      ok = false
      begin
        print msg1
        cadena = gets.chomp
        begin
          if (cadena =~ /\A\d+\Z/)
            numero = cadena.to_i
            ok = true
          else
            raise IOError
          end
        rescue IOError
          puts msg2
        end
        if (ok)
          if (numero >= max)
            ok = false
          end
        end
      end while (!ok)

      return numero
    end



    def menu(titulo,lista)
      tab = "  "
      puts titulo
      index = 0
      lista.each { |l|
        puts tab+index.to_s+"-"+l
        index += 1
      }

      opcion = lee_entero(lista.length,
                          "\n"+tab+"Elige una opción: ",
                          tab+"Valor erróneo")
      return opcion
    end

    def salir_carcel
      lista_salidas = [Civitas::SalidasCarcel::PAGANDO.to_s, 
        Civitas::SalidasCarcel::TIRANDO.to_s]
      opcion = menu("Elige la forma para intentar salir de la carcel", 
        lista_salidas)
      return Civitas::SalidasCarcel::Lista_salidas[opcion]
    end
    
    def comprar
      lista_respuestas = [Civitas::Respuestas::SI.to_s, 
        Civitas::Respuestas::NO.to_s]
      opcion = menu("Desea comprar comprar la propiedad", lista_respuestas)
      return Civitas::Respuestas::Lista_respuestas[opcion]
    end

    def gestionar
      lista_gestiones = [Civitas::GestionesInmobiliarias::VENDER.to_s, 
        Civitas::GestionesInmobiliarias::HIPOTECAR.to_s, 
        Civitas::GestionesInmobiliarias::CANCELAR_HIPOTECA.to_s, 
        Civitas::GestionesInmobiliarias::CONSTRUIR_CASA.to_s, 
        Civitas::GestionesInmobiliarias::CONSTRUIR_HOTEL.to_s, 
        Civitas::GestionesInmobiliarias::TERMINAR.to_s]
      opcion = menu("Seleccione la operacion a realizar", lista_gestiones)
      @iGestion = opcion
      n = @juego_model.get_jugador_actual.get_nombre_propiedades
      if opcion != 5 && n != nil
        indice = menu("Seleccione la propiedad sobre la que desea realizar la 
gestion", n)
      end
      @iPropiedad = indice
    end

    def get_gestion
      return @iGestion
    end

    def get_propiedad
      return  @iPropiedad
    end

    def mostrar_siguiente_operacion(operacion)
      puts operacion
    end

    def mostrar_eventos
      while Diario.instance.eventos_pendientes
        puts Diario.instance.leer_evento+"\n"
      end
    end

    def set_civitas_juego(civitas)
         @juego_model = civitas
         self.actualizar_vista
    end

    def actualizar_vista
      puts @juegos_model.info_jugador_texto + "\n" + 
        @juegos_model.get_casilla_actual.to_string
    end


  end

end
