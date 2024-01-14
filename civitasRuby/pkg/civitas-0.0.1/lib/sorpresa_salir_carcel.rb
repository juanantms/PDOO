# encoding:utf-8
module Civitas

  class SorpresaSalirCarcel < Sorpresa
    
    def initialize(texto, mazo)
      super(texto)
      @mazo = mazo
    end
    
    def aplicar_a_jugador(actual, todos)
      if jugador_correcto(actual, todos)
        super(actual, todos)
        tiene = false
        for i in (0..todos.size)
          if todos.at(i).tiene_salvoconducto
            tiene = true
          end
        end
        if !tiene
          todos.at(actual).obtener_salvoconducto(self)
          salir_del_mazo
        end
      end
    end
    
    def usada 
      @mazo.habilitar_carta_especial(self)
  end
  
  def salir_del_mazo
      @mazo.inhabilitar_carta_especial(self)
  end

   def to_string
      return super.to_string 
    end  
  
  public_class_method :new  
  end
end