module Civitas
  
  class MazoSorpresa
    
    def initialize(debug)
      @barajada = false
      @usadas = 0
      @sorpresas = Array.new
      @cartas_especiales = Array.new
      @debuf = debug
      @ultima_sorpresa = nil
      if @debug
        Diario.instance.ocurre_evento("Debug is TRUE")
      end
    end
    
    def al_mazo(sorpresa)
      if !@barajada
        @sorpresas << sorpresa
      end
    end
    
    def get_ultima_sorpresa
      return @ultima_sorpresa
    end
    
    def habilitar_carta_especial(sorpresa)
      for i in 0..@cartas_especiales.size
        if @cartas_especiales.at(i) == sorpresa
          aux = @cartas_especiales.at(i)
          @cartas_especiales.delete.at(i)
          @sorpresas << aux
          Diario.instance.ocurre_evento("Carta HABILITADA")
        end
      end
    end

    def inhabilitar_carta_especial(sorpresa)
      for i in 0..@sorpresas.size
        if @sorpresas.at(i) == sorpresa
          aux = @sorpresas.at(i)
          @sorpresas.delete.at(i)
          @cartas_especiales << aux
          Diario.instance.ocurre_evento("Carta INHABILITADA")
        end
      end
    end
    
    def siguiente
      if !@barajada || @usadas == @sorpresas.size && !@debug
        @sorpresas.shuffle
        @usadas = 0
        @barajada = true
      end
      @usadas += 1
      @ultima_sorpresa = @sorpresas.at(0)
      @sopresas.delete_at(0)
      @sorpresas << @ultima_sorpresa
      return @ultima_sorpresa
    end
    
  end
end