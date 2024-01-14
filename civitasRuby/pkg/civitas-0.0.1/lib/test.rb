require_relative 'tablero'
require_relative 'mazo_sorpresa'
require_relative 'casilla'
require_relative 'diario'
require_relative 'dado'
require_relative 'tipo_sorpresa'
require_relative 'tipo_casilla'
require_relative 'operaciones_juego'
require_relative 'estados_juego'
require_relative 'sorpresa'
require_relative 'civitas_juego'
require_relative 'gestor_estados'
require_relative 'jugador'
require_relative 'vista_textual'
require_relative 'controlador'
require_relative 'operacion_inmobiliaria'
require_relative 'gestiones_inmobiliarias'
require_relative 'respuestas'
require_relative 'salidas_carcel'


module Civitas
  class Test
    
    def self.main
      
      vistatexto = Vista_textual.new
      nombres = Array.new
      nombres << "Juan"
      nombres << "Pablo"
      nombres << "Nadia"
      nombres << "Alvaro"
      Dado.instance.set_debug(true)
      juego = CivitasJuego.new(nombres)
      controlador = Controlador.new(juego, vistatexto)
      
      controlador.juega
      
    end
    
    Test.main
  end
end
