
module Civitas

  class OperacionInmobiliaria
    attr_reader :gestion, :num_propiedad
    
    def initialize(gest, ip)
      @gestion = gest
      @num_propiedad = ip
    end 

  end
end
