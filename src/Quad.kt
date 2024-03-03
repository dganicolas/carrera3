import kotlin.random.Random

//El quad tiene una autonomía que será la mitad de la que tiene una Motocicleta con la misma cilindrada.
// Tiene cómo característica diferencial el tipo que será un valor entre los siguientes:
// "Cuadriciclos ligeros", "Cuadriciclos no ligeros" y "Vehículos especiales".
class Quad(
    nombre: String,
    capacidadCombustible: Float,
    combustibleActual: Float,
    kilometrosActuales: Float,
    cilindrada: Int
):Motocicleta(nombre,"","",capacidadCombustible,combustibleActual,kilometrosActuales,cilindrada) {
    override fun toString(): String {
        return "Quad"
    }

}

