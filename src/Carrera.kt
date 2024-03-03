import kotlin.math.ceil

/**
 * Representa una carrera que incluye múltiples vehículos como participantes. La carrera tiene un nombre, una distancia total
 * a recorrer, y maneja el estado y el avance de cada vehículo participante.
 *
 * @property nombreCarrera El nombre de la carrera para identificación.
 * @property distanciaTotal La distancia total que los vehículos deben recorrer para completar la carrera.
 * @constructor Inicializa una carrera con una lista de vehículos participantes y valida la distancia mínima requerida.
 */
class Carrera(
    val nombreCarrera: String,
    private val distanciaTotal: Float,
    private var participantes: List<Vehiculo> = listOf()
) {
    private val historialAcciones = mutableMapOf<String, MutableList<String>>()
    private var estadoCarrera = false // Indica si la carrera está en curso o ha finalizado.

    init {
        require(distanciaTotal >= 1000) { "La distancia total de la carrera debe ser al menos 1000 km." }
        participantes.forEach { vehiculo -> inicializaDatosParticipante(vehiculo) }
    }

    companion object {
        private var unaVez = 0
        private var GANADOR= ""
        private const val KM_PARA_FILIGRANA = 20f // Cada 20 km, se realiza una filigrana.
    }

    /**
     * Representa el resultado final de un vehículo en la carrera, incluyendo su posición final, el kilometraje total recorrido,
     * el número de paradas para repostar, y un historial detallado de todas las acciones realizadas durante la carrera.
     *
     * @property vehiculo El [Vehiculo] al que pertenece este resultado.
     * @property posicion La posición final del vehículo en la carrera, donde una posición menor indica un mejor rendimiento.
     * @property kilometraje El total de kilómetros recorridos por el vehículo durante la carrera.
     * @property paradasRepostaje El número de veces que el vehículo tuvo que repostar combustible durante la carrera.
     * @property historialAcciones Una lista de cadenas que describen las acciones realizadas por el vehículo a lo largo de la carrera, proporcionando un registro detallado de su rendimiento y estrategias.
     */
    data class ResultadoCarrera(
        val vehiculo: Vehiculo,
        val posicion: Int,
        val kilometraje: Float,
        val paradasRepostaje: Int,
        val historialAcciones: List<String>
    )

    /**
     * Proporciona una representación en cadena de texto de la instancia de la carrera, incluyendo detalles clave como
     * el nombre de la carrera, la distancia total a recorrer, la lista de participantes, el estado actual de la carrera
     * (en curso o finalizada), el historial de acciones realizadas por los vehículos durante la carrera y las posiciones
     * actuales de los participantes.
     *
     * @return Una cadena de texto que describe los atributos principales de la carrera, incluyendo el nombre,
     * distancia total, participantes, estado actual, historial de acciones y posiciones de los vehículos participantes.
     */
    override fun toString(): String {
        return "NombreCarrera: $nombreCarrera, DistanciaTotal: $distanciaTotal, Participantes: $participantes, EstadoCarrera: $estadoCarrera, HistorialAcciones: $historialAcciones."}

    /**
     * Inicializa los datos de un participante en la carrera, preparando su historial de acciones y estableciendo
     * su posición inicial. Este método se llama automáticamente al agregar un nuevo vehículo a la carrera.
     *
     * @param vehiculo El [Vehiculo] cuyos datos se inicializan.
     */
    private fun inicializaDatosParticipante(vehiculo: Vehiculo) {
        historialAcciones[vehiculo.nombre] = mutableListOf()
    }

    /**
     * Inicia el proceso de la carrera, haciendo que cada vehículo avance de forma aleatoria hasta que un vehículo
     * alcanza o supera la distancia total de la carrera, determinando así el ganador.
     */
    fun iniciarCarrera() {
        println("¡Comienza la carrera!")
        var contador = 0
        estadoCarrera = true // Indica que la carrera está en curso.
        while (estadoCarrera) {

            if (contador % 5 == 0){
                println("*** CLASIFICACIÓN PARCIAL (ronda $contador) ***")
                for (i in participantes){
                    println("*. ${i.nombre} $i(km = ${i.kilometrosActuales}, combustible = ${i.combustibleActual} L")
                }
                Thread.sleep(500)
            }
            contador+= 1
            val vehiculoSeleccionado = seleccionaVehiculoQueAvanzara()
            avanzarVehiculo(vehiculoSeleccionado)

            val vehiculoGanador = determinarGanador()
            if (vehiculoGanador != null) {
                estadoCarrera = false
                println("\n¡Carrera finalizada!")
                println("\n¡¡¡ENHORABUENA ${vehiculoGanador.nombre}!!!\n")
            }

        }
    }

    /**
     * Selecciona aleatoriamente un vehículo participante para avanzar en la carrera. Este método se utiliza dentro
     * del proceso de la carrera para decidir qué vehículo realiza el próximo movimiento.
     *
     * @return El [Vehiculo] seleccionado para avanzar.
     */
    private fun seleccionaVehiculoQueAvanzara() = participantes.random()

    /**
     * Calcula el número de tramos o segmentos en los que se divide la distancia que un vehículo intenta recorrer.
     * Esto se utiliza para simular el avance por etapas de los vehículos en la carrera.
     *
     * @param distancia La distancia total a recorrer en este turno por el vehículo.
     * @return El número de tramos como [Int] en los que se divide la distancia.
     */
    private fun obtenerNumeroDeTramos(distancia: Float) = ceil(distancia.toDouble() / KM_PARA_FILIGRANA).toInt()

    /**
     * Determina la distancia que un vehículo intentará recorrer en su próximo turno, asegurando que no exceda
     * la distancia total de la carrera.
     *
     * @param kilometrosRecorridos Los kilómetros ya recorridos por el vehículo.
     * @return La distancia a recorrer en el siguiente turno.
     */
    private fun obtenerDistanciaARecorrer(kilometrosRecorridos: Float) : Float {
        val distanciaAleatoria = (10..200).random()

        // Comprobar que no nos vamos a pasar de la distancia total a recorrer en la carrera
        // Asegurarnos que va a recorrer los km exactos para llegar a la meta
        return if (distanciaAleatoria + kilometrosRecorridos > this.distanciaTotal) {
            this.distanciaTotal - kilometrosRecorridos
        } else {
            distanciaAleatoria.toFloat()
        }
    }

    /**
     * Avanza un vehículo a lo largo de la carrera, dividiendo su avance en tramos y realizando las acciones
     * correspondientes en cada tramo, como realizar filigranas o repostar combustible.
     *
     * @param vehiculo El [Vehiculo] que avanzará en la carrera.
     */
    private fun avanzarVehiculo(vehiculo: Vehiculo) {
        val distanciaTotalEnAvance = obtenerDistanciaARecorrer(vehiculo.kilometrosActuales)
        val numeroDeTramos = obtenerNumeroDeTramos(distanciaTotalEnAvance) // Rompemos el recorrido en tramos de 20 km.

        registrarAccion(vehiculo.nombre, "Inicia viaje: A recorrer $distanciaTotalEnAvance kms (${vehiculo.kilometrosActuales} kms y ${vehiculo.combustibleActual} L actuales)")

        var distanciaRestanteEnAvance = distanciaTotalEnAvance
        repeat(numeroDeTramos) { //Tramos de KM_PARA_FILIGRANA km
            val distanciaDeTramo = minOf(KM_PARA_FILIGRANA, distanciaRestanteEnAvance)

            avanzarTramo(vehiculo, distanciaDeTramo)
            distanciaRestanteEnAvance -= distanciaDeTramo
            repeat((0..3).random()) { realizarFiligrana(vehiculo) }
        }

        registrarAccion(vehiculo.nombre, "Finaliza viaje: Total Recorrido $distanciaTotalEnAvance kms (${vehiculo.kilometrosActuales} kms y ${vehiculo.combustibleActual} L actuales)")

        actualizarPosiciones()
    }

    /**
     * Avanza un vehículo a través de un tramo específico de la carrera, ajustando su combustible y kilometraje según
     * la distancia recorrida.
     * Realiza las operaciones necesarias para repostar si el combustible no es suficiente para completar el tramo.
     *
     * @param vehiculo El [Vehiculo] que avanza.
     * @param distanciaEnTramo La distancia del tramo a recorrer por el vehículo.
     */
    private fun avanzarTramo(vehiculo: Vehiculo, distanciaEnTramo: Float) {
        var distanciaRestante = vehiculo.realizaViaje(distanciaEnTramo)
        registrarAccion(vehiculo.nombre, "Avance tramo: Recorrido ${distanciaEnTramo - distanciaRestante} kms")

        // Si le queda alguna distancia por recorrer debe repostar
        while (distanciaRestante > 0) {
            val repostado = vehiculo.repostar(0f, vehiculo) // Llenamos el tanque
            vehiculo.paradas += 1
            registrarAccion(vehiculo.nombre, "Repostaje tramo: $repostado L")

            // Necesitamos de nuevo una distancia para después compararla con la distanciaRestante que devuelve realizarViaje()
            val distancia = distanciaRestante
            distanciaRestante = vehiculo.realizaViaje(distancia)
            registrarAccion(vehiculo.nombre, "Avance tramo: Recorrido ${distancia - distanciaRestante} kms")
        }
    }

    /**
     * Determina de manera aleatoria si un vehículo debe realizar una filigrana durante su turno en la carrera.
     * La decisión se basa en una probabilidad del 50%, donde un resultado menor que 0.5 indica que el vehículo
     * debería realizar una filigrana.
     *
     * @return [Boolean] Verdadero si el vehículo debe realizar una filigrana, falso en caso contrario.
     */
    private fun comprobarSiTocaHacerFiligrana() = (Math.random() < 0.5)

    /**
     * Intenta que un vehículo realice una filigrana durante su avance en la carrera. La filigrana
     * (derrape o caballito) se realiza basada en una probabilidad aleatoria y consume combustible adicional.
     *
     * @param vehiculo El [Vehiculo] que intentará realizar la filigrana.
     */
    private fun realizarFiligrana(vehiculo: Vehiculo) {
        // Lógica para realizar filigranas de motociletas y automovil y registrarlas. Se hará o no aleatoriamente.
        if (comprobarSiTocaHacerFiligrana()) {
            val combustibleRestante: Float
            var kmPerdidos = (10..50).random()
            if ((1..5).random() < 3){
                kmPerdidos = 10
            }
            if (vehiculo is Automovil) {
                combustibleRestante = vehiculo.realizaDerrape()
                registrarAccion(vehiculo.nombre, "Derrape: Combustible restante $combustibleRestante L, KM perdidos: -$kmPerdidos.")
                vehiculo.kilometrosActuales -=kmPerdidos
                if (vehiculo.kilometrosActuales < 0f){
                    vehiculo.kilometrosActuales = 0f
                }
            } else if (vehiculo is Motocicleta) {
                combustibleRestante = vehiculo.realizaCaballito()
                registrarAccion(vehiculo.nombre, "Caballito: Combustible restante $combustibleRestante L, KM perdidos: -$kmPerdidos.")
                vehiculo.kilometrosActuales -=kmPerdidos
                if (vehiculo.kilometrosActuales < 0f){
                    vehiculo.kilometrosActuales = 0f
                }
            }
        }
    }

    /**
     * Actualiza la posición de un vehículo en la carrera, sumando la distancia recorrida en el último tramo
     * a su total acumulado.
     *
     * @param nombreVehiculo El nombre del [Vehiculo] cuya posición se actualizará.
     * @param kilometraje La distancia recorrida en el último tramo.
     */
    private fun actualizarPosiciones() {
        //val kilometrosRecorridos = posiciones[nombreVehiculo] ?: 0f
        //posiciones[nombreVehiculo] = kilometrosRecorridos + kilometraje
        participantes = participantes.sortedByDescending { it.kilometrosActuales }
    }

    /**
     * Determina si hay un ganador de la carrera, basado en si algún vehículo ha alcanzado la distancia
     * total de la carrera.
     *
     * @return El [Vehiculo] ganador, si existe; de lo contrario, devuelve null.
     */
    private fun determinarGanador(): Vehiculo? {
        val maxKilometros = participantes.maxByOrNull { it.kilometrosActuales }
        var ganador: Vehiculo? = null
        var primero: Vehiculo?
        var contador=0
        if ((maxKilometros?.kilometrosActuales ?: 0f) >= distanciaTotal){
            if ((maxKilometros?.kilometrosActuales ?: 0f) >= distanciaTotal && unaVez == 0) {
                primero = maxKilometros
                GANADOR = (primero?.nombre).toString()
                unaVez = 2
            }
            for (vehiculo in participantes){
                if(vehiculo.kilometrosActuales == distanciaTotal){
                    contador++
                }
            }
            if (contador == participantes.size){
            ganador = participantes.find { it.nombre == GANADOR }
            }}

        return ganador
    }

    /**
     * Registra una acción específica realizada por un vehículo en su historial de acciones durante la carrera.
     *
     * @param nombreVehiculo El nombre del vehículo que realiza la acción.
     * @param accion La descripción de la acción realizada.
     */
    private fun registrarAccion(nombreVehiculo: String, accion: String) {
        historialAcciones[nombreVehiculo]?.add(accion)
    }

    /**
     * Genera y devuelve una lista de los resultados de la carrera, incluyendo la posición final,
     * el kilometraje total recorrido, el número de paradas para repostar, y el historial de acciones
     * para cada vehículo participante.
     *
     * @return Una lista de objetos [ResultadoCarrera] que representan los resultados finales de la carrera.
     */
    fun obtenerResultados(): List<ResultadoCarrera> {
        val resultados = mutableListOf<ResultadoCarrera>()
        var posicion = 0
        participantes.sortedByDescending { it.kilometrosActuales }.forEach {
            val historial = historialAcciones[it.nombre] ?: emptyList()
                resultados.add(
                    ResultadoCarrera(
                        it,
                        posicion + 1,
                        it.kilometrosActuales,
                        it.paradas,
                        historial
                    )
                )
        }
        return resultados
    }

}