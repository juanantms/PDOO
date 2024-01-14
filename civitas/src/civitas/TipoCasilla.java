package civitas;

/*
* Este enumerado representa todos los tipos de casillas del juego.
* En las casillas de DESCANSO al jugador no le ocurre nada.
* La salida, la carcel y el aparcamiento se consideran dentro de la CALLE.
*/
enum TipoCasilla {
    CALLE,
    SORPRESA,
    JUEZ,
    IMPUESTO,
    DESCANSO
}