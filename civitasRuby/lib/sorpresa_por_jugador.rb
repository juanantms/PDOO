# encoding:utf-8
module Civitas

  class SorpresaPorJugador < Sorpresa
  
    def initialize(texto, valor)
      super(texto)
      @valor = valor
    end

    def aplicar_a_jugador(actual, todos)
      if jugador_correcto(actual, todos)
        super(actual, todos)
        sorpresa_resta = SorpresaPagarCobrar.new("Pagan todos los jugadores 
menos el no afectado.", @valor * -1, )
        sorpresa_suma = SorpresaPagarCobrar("Recibe dinero de todos los 
jugadores.", @valor * (todos.size -1))
        for i in (0..todos.size)
          if i != actual
            sorpresa_resta.aplicar_a_jugador_pagar_cobrar(i, todos)
          else
            sorpresa_suma.aplicar_a_jugador_pagar_cobrar(i, todos)
          end
        end
      end
    end
    
    def to_string
      return super.to_string + "\nValor: #{@valor}"
    end
  
  public_class_method :new  
  end
end