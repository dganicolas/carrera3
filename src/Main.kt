
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Extiende la clase [Float] para permitir el redondeo del número a un número específico de posiciones decimales.
 *
 * @param posiciones El número de posiciones decimales a las que se redondeará el valor.
 * @return Un [Float] redondeado al número de posiciones decimales especificadas.
 */
fun Float.redondear(posiciones: Int): Float {
    val factor = 10.0.pow(posiciones.toDouble()).toFloat()
    return (this * factor).roundToInt() / factor
}

var NOMBRES = emptyList<String>().toMutableList() // es la constante de tipo lista de string que uso para determinar si ese nombre ya fue registrado


/**
 * Punto de entrada del programa. Crea una lista de vehículos y una carrera, e inicia la carrera mostrando
 * los resultados al finalizar.
*/
fun main() {
    /**
     * Esta funcion sirve para limpiar la consola
     * */
    fun limpiarPantalla() {
        for (i in (1..20)){
            println()
        }
    }


    /**
     * esta funcion comprueba que el numero introducido es un 1
     * @param numero es el numero a comprobar
     * @return retorna un booleano, que sirve para lanzar una excepcion en asignarParticipantes()
     * */
    fun comprobarNumero(numero:Int):Boolean{
        if (numero == 1){return true}
        else{return false}
    }


    /**
     * pregunta, cuantos participantes seran en el carrera,
     * en caso de introducir mal los datos, dara una expecepcion
     * @return numero de participantes
     * */
    fun asignarParticipantes():Int{
        limpiarPantalla()
        print("cuantos participantes son en total:")
        try {
            val numero = readln().toInt()
            if (comprobarNumero(numero)) {
                throw IllegalArgumentException("ERROR el numero introducido debe ser mayor que 1, DALE ENTER PARA CONTINUAR")
            }
            return numero
        }catch (e: NumberFormatException) {
            println("ERROR el dato introducido no valido, DALE ENTER PARA CONTINUAR")
            readln()
        } catch (e: IllegalArgumentException) {
            println(e.message)
            readln()
        }
        return 0
    }


    /**
     * pregunta elnumero de participantes, pero en este caso si el numero es erroneo, hara un bucle infinito, hasta que el dato sea correcto
     * @return numero de participantes
     * */
    fun preguntarParticipantes(): Int{
        var numero = asignarParticipantes()
        while (numero < 1){
            numero = asignarParticipantes()
            limpiarPantalla()
        }
        return numero
    }


    /**
     * compruebo que el nombre no este vacio o relleno con espacios
     * @param nombre es el nombre que comprueba
     * @param mensaje es el coche que debo asignar el nombre
     * @return retorna el nombre pero cumpliendo la regla de no estar vacio
     * */
    fun ComprobarNombre(nombre:String, mensaje:String):String{
        var nombre = nombre
        while (nombre.isBlank() || nombre in NOMBRES){
            println(mensaje)
            nombre=readln().lowercase().capitalize()
            if (nombre.isBlank()){
                println("ERROR, el nombre no debe estar vacio")
            }
            if(nombre in NOMBRES){
                println("ERROR el nombre ya se esta utilizando")
            }
        }
        NOMBRES.add(nombre)
        return nombre
    }


    /**
     * le asigna una marca al azar al vehiculo
     * @return la marca elegida
     * */
    fun asignarMarca():String{
        val marca = arrayOf("Benelli","KTM","Bimota","Ducati","Harley-Davidson","Hyosung","Aprilia","Abarth", "Alfa Romeo", "Aston Martin", "Audi", "Bentley", "BMW", "Cadillac", "Chevrolet", "Citroën", "Dacia", "Ferrari", "Fiat", "Ford", "Honda", "Infiniti", "Isuzu", "Jaguar", "Jeep", "Kia", "Lamborghini")
        val numero = (0..26).random()
        return marca[numero]
    }


    /**
     * le asigna un modelo al azar al vehiculo
     * @return el modelo elegida
     * */
    fun asignarModelo():String{
        val  modelo = arrayOf("YZF R15","GSX-R 750", "Ninja","CBR","Tiger Rally","Super Hooligan","TNT i","Panigale","Fat Boy","S RR","Sportsman","Am Outlander","Grizzly","KingQuad","Brute Force","Cat Alterra","FourTrax Rancher","MXU","Scrambler XP","F-150","Silverado","Ram 1500","Tacoma","Sierra","Titan","D-MAX","L200","Amarok","X-Class","Corolla","Civic","Golf","Mustang","3 Series","A4","C-Class","Elantra","Optima","Impreza")
        val numero = (0..38).random()
        return modelo[numero]
    }


    /**
     * determina si el vehiculo es electrico o no
     * @return si es electrico o no
     * */
    fun seraElectrico():Boolean{
        val numero= (1..2).random()
        return if(numero ==1) true else false
    }


    /**
     * necesito esta funcion para si el vehiculo es eletrico escriba si o escriba no en caso de que no sea
     * @param esElectrico esta variable se fijo en seraElectrico()
     * @return el modelo elegida
     * */
    fun Strelectrico(esElectrico: Boolean):String{
        return if(esElectrico) "Si" else "No"
    }


    /**
     * le asigna las CC a las motocicletas
     * @return las CC elegida
     * */
    fun tipoCilindradas():Int{
        val numero= (1..8).random()
        return when(numero){
            1-> 125
            2->250
            3->400
            4->500
            6->750
            7->900
            8->1000
            else -> {125}
        }
    }


    /**
     * esta funcion servira para ver cual tipo de vehiculo asignare al vehiculo en cueston
     * @param nombre, es el nombre del vehiculo
     * */
    fun asignarVehiculo(nombre:String):Vehiculo{
        val numero = (1..4).random()
        when (numero){
            1 -> { // Automóvil de 30 a 60L
                val gasolina = (30..60).random().toFloat()
                val nivelGasolina = (gasolina*((20..100).random()/100)).redondear(2)
                val marca= asignarMarca()
                val modelo = asignarModelo()
                val esElectrico= seraElectrico()
                val StrElectrico = Strelectrico(esElectrico)
                println("Te ha tocado un Automóvil(nombre=$nombre, marca=$marca, modelo=$modelo, capacidad=$gasolina, combustible=$nivelGasolina, eléctrico=$StrElectrico)")
                return Automovil(nombre,marca,modelo,gasolina,nivelGasolina,0.0f,esElectrico)
            }


            2->{// Motocicleta de 15 a 30L
                //cilindradas 125, 250, 400, 500, 750, 900 y 1000
                val gasolina = (15..30).random().toFloat()
                val nivelGasolina = (gasolina*((20..100).random()/100)).redondear(2)
                val marca= asignarMarca()
                val modelo = asignarModelo()
                val cilindradas= tipoCilindradas()
                println("Te ha tocado un Motocicleta(nombre=$nombre, marca=$marca, modelo=$modelo, capacidad=$gasolina, combustible=$nivelGasolina, CC=$cilindradas)")
                return Motocicleta(nombre,marca,modelo,gasolina,nivelGasolina,0.0f,cilindradas)
            }


            3->{// Quad de 20 a 40L
                //cilindradas 125, 250, 400, 500, 750, 900 y 1000
                val gasolina = (20..40).random().toFloat()
                val nivelGasolina = (gasolina*((20..100).random()/100)).redondear(2)
                val marca= asignarMarca()
                val modelo = asignarModelo()
                val cilindradas= tipoCilindradas()
                println("Te ha tocado un Quad(nombre=$nombre, marca=$marca, modelo=$modelo, capacidad=$gasolina, combustible=$nivelGasolina, CC=$cilindradas)")
                return Quad(nombre,gasolina,nivelGasolina,0.0f,cilindradas)
            }


            4->{// Camión de 90 a 150L
                val gasolina = (90..150).random().toFloat()
                val nivelGasolina = (gasolina*((20..100).random()/100)).redondear(2)
                val marca= asignarMarca()
                val modelo = asignarModelo()
                val MMA= (1000..10000).random().toFloat()
                println("Te ha tocado un Camion(nombre=$nombre, marca=$marca, modelo=$modelo, capacidad=$gasolina, combustible=$nivelGasolina, peso: $MMA")
                return Camion(nombre,gasolina,nivelGasolina,0.0f,MMA)
            }


            else-> {return Camion("BUG",123.0f,100.0f,0.0f,1000f)}
        }
    }


    /**
     * esta funcion es la que crea los participantes de la carrera, creando la lista del mismo
     * @param numero son el numero de participantes
     * @return la lista de participantes
     * */
    fun crearParticipantes(numero: Int):List<Vehiculo>{
        var nombre = ""
        val listaParticipantes = emptyList<Vehiculo>().toMutableList()
        for (i in (1..numero)){
            var nombre = ComprobarNombre(nombre,"Nombre del vehículo $i -> " )
            listaParticipantes.add(asignarVehiculo(nombre))
        }
        return listaParticipantes
    }

    val numeros_participantes = preguntarParticipantes()
    val vehiculos = crearParticipantes(numeros_participantes)

    val carrera = Carrera("Gran Carrera de Filigranas", 1000f, vehiculos)

    println("\n*** ${carrera.nombreCarrera} ***\n")
    carrera.iniciarCarrera()

    val resultados = carrera.obtenerResultados()

    println("* Clasificación:\n")
    resultados.forEach { println("${it.posicion} -> ${it.vehiculo.nombre} (${it.vehiculo.kilometrosActuales} kms)") }

    println("\n" + resultados.joinToString("\n") { it.toString() })

    println("\n* Historial Detallado:\n")
    resultados.forEach { println("${it.posicion} -> ${it.vehiculo.nombre}\n${it.historialAcciones.joinToString("\n")}\n") }
}