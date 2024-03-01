//Camion es una clase derivada de Automovil
// El camión tiene un consumo de 16L/100Km.
//Además tendrá un peso expresado en Kg (min 1000 máx 10000) que afectará a su autonomía...
// ya que cada 1000 Kg de peso reduce los KM por litro en 0.2.
class Camion(
    nombre:String,
    capacidadCombustible:Float,
    combustibleActual:Float,
    kilometrosActuales:Float,
    var Mma:Float
): Vehiculo(nombre, "", "", capacidadCombustible, combustibleActual, kilometrosActuales) {
    override fun calcularAutonomia(): Float = (combustibleActual * (KM_POR_LITRO - (1 - (Mma/1000)))).redondear(2)
}