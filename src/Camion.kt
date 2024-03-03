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
): Automovil(nombre, "", "", capacidadCombustible, combustibleActual, kilometrosActuales,false) {
    companion object{
        val KM_LITROS_CAMION = 6.25f //esta es la constante que los camiones inicialmente pueden recorrer, a mayor Mma menor recorrido
        val PESO_MIL = 0.2f //esta constante es la que por cada 1000KG resta 0.2km que puede hacer el camion
        val KG_MMA = 1000 // esta constante la uso para dividir entre mil

    }
    override fun calcularAutonomia(): Float = (combustibleActual * (KM_LITROS_CAMION - (Mma * PESO_MIL/KG_MMA))).redondear(2)
    override fun toString(): String {
        return "Camion"
    }
}