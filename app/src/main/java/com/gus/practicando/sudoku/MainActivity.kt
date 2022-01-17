package com.gus.practicando.sudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children

class MainActivity : AppCompatActivity() {

    private lateinit var cajas : ArrayList<EditText>

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_activity_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_clean) {
            limpiarPantalla()
            return true
        }
        else
            return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // llamo cajas a los EditText para recoger los daots del usuario
        cajas = recogerDatosPantalla()

        // Recogemos el botón resolver y empieza la resolución del sudoku
        val botonResolver: Button = findViewById(R.id.btResolver)
        botonResolver.setOnClickListener {


            // llamo casillas al array de objetos Casilla para poder tratar los datos
            val casillas = crearCasillas()
            if (!isError(casillas))
                resolver(casillas)
            else {
                Toast.makeText(this, "El sudoku contiene datos incompatibles",
                    Toast.LENGTH_SHORT).show()
            }

        }

    }

    // Recogemos los datos introducidos en la pantalla por el usuario y las guardamos en orden para
    // utilizarlos a la vez que las casillas (tendremos dos arrays iguales)
    private fun recogerDatosPantalla() : ArrayList<EditText>{
        val cajasPantalla = arrayListOf<EditText>()
        cajasPantalla.addAll(
            listOf(
                findViewById(R.id.caja00), findViewById(R.id.caja01),findViewById(R.id.caja02),
                findViewById(R.id.caja03), findViewById(R.id.caja04),findViewById(R.id.caja05),
                findViewById(R.id.caja06), findViewById(R.id.caja07),findViewById(R.id.caja08),
                findViewById(R.id.caja10), findViewById(R.id.caja11),findViewById(R.id.caja12),
                findViewById(R.id.caja13), findViewById(R.id.caja14),findViewById(R.id.caja15),
                findViewById(R.id.caja16), findViewById(R.id.caja17),findViewById(R.id.caja18),
                findViewById(R.id.caja20), findViewById(R.id.caja21),findViewById(R.id.caja22),
                findViewById(R.id.caja23), findViewById(R.id.caja24),findViewById(R.id.caja25),
                findViewById(R.id.caja26), findViewById(R.id.caja27),findViewById(R.id.caja28),
                findViewById(R.id.caja30), findViewById(R.id.caja31),findViewById(R.id.caja32),
                findViewById(R.id.caja33), findViewById(R.id.caja34),findViewById(R.id.caja35),
                findViewById(R.id.caja36), findViewById(R.id.caja37),findViewById(R.id.caja38),
                findViewById(R.id.caja40), findViewById(R.id.caja41),findViewById(R.id.caja42),
                findViewById(R.id.caja43), findViewById(R.id.caja44),findViewById(R.id.caja45),
                findViewById(R.id.caja46), findViewById(R.id.caja47),findViewById(R.id.caja48),
                findViewById(R.id.caja50), findViewById(R.id.caja51),findViewById(R.id.caja52),
                findViewById(R.id.caja53), findViewById(R.id.caja54),findViewById(R.id.caja55),
                findViewById(R.id.caja56), findViewById(R.id.caja57),findViewById(R.id.caja58),
                findViewById(R.id.caja60), findViewById(R.id.caja61),findViewById(R.id.caja62),
                findViewById(R.id.caja63), findViewById(R.id.caja64),findViewById(R.id.caja65),
                findViewById(R.id.caja66), findViewById(R.id.caja67),findViewById(R.id.caja68),
                findViewById(R.id.caja70), findViewById(R.id.caja71),findViewById(R.id.caja72),
                findViewById(R.id.caja73), findViewById(R.id.caja74),findViewById(R.id.caja75),
                findViewById(R.id.caja76), findViewById(R.id.caja77),findViewById(R.id.caja78),
                findViewById(R.id.caja80), findViewById(R.id.caja81),findViewById(R.id.caja82),
                findViewById(R.id.caja83), findViewById(R.id.caja84),findViewById(R.id.caja85),
                findViewById(R.id.caja86), findViewById(R.id.caja87),findViewById(R.id.caja88)))

        for (caja in cajasPantalla) {
            caja.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if (caja.text.toString().isNotEmpty()) {
                        val numero = caja.text.toString().toInt()
                        if (numero == 0 || numero > 9)
                            caja.value = ""
                    }
                }
            }
        }

        return cajasPantalla
    }


    // Vamos a crear el array con todas las casillas del sudoku
    private fun crearCasillas() : ArrayList<Casilla>{
        var contador = 0
        val casillas = arrayListOf<Casilla>()
        // Recorremos las 9 columnas
        for (i in 0 .. 8) {
            // Recorremos las 9 filas
            for (j in 0 ..8 ){
                // Instanciamos cada uno de las casillas (con el init del objeto Casilla
                // determinamos también el cuadrante al que pertenece
                val casilla = Casilla(i, j)
                // En caso de que la caja (datos introducido por el usuario) tenga un valor ya dado
                // lo guardamos en la casilla e indicamos que está resuelto
                val caja: EditText = cajas[contador]
                val valorString: String = caja.text.toString()
                if(valorString.isNotEmpty()){
                    val valorInt = valorString.toInt()
                    casilla.swInical = true
                    casilla.resultado = valorInt
                    casilla.resolver(valorInt)
                }
                casillas.add(casilla)
                contador++
            }
        }
        return casillas
    }

    // Una vez que tenemos los datos ya cargados empezamos la resolución del sudoku
    private fun resolver(casillas: ArrayList<Casilla>){
            var casillasActualizadas = casillas
            // Mientras haya casillas con cambios, volvemos a recorrer el array de casillas completo
            while (isCasillasAComprobar(casillas)){
                casillasActualizadas = eliminarPosibilidades(casillas)
            }

            // Comprobamos si está resuelto o no
            if (!isSudokuResuelto(casillasActualizadas)){
                // COMPROBAMOS SI YA SE HA COMETIDO ALGÚN ERROR
                if (isError(casillasActualizadas))
                    Toast.makeText(this, "QUE SE PARE TODO QUE ALGO VA MAL DESDE DENTRO DEL WHILE", Toast.LENGTH_SHORT).show()
                else {
                    casillasActualizadas = quitarPares(casillasActualizadas)
                    //if (quitarPorCuadrante(casillasActualizadas))
                    //    resolver(casillasActualizadas)
                    segundaComprobacion(casillasActualizadas)
                    pintarFinal(casillas)
                    imprimirOpciones(casillasActualizadas)


                }
            } else{
                // CUANDO SABEMOS QUE ESTÁ COMPLETADO, COMPROBAMOS SI HABÍA ERRORES O NO
                if (isError(casillasActualizadas))
                    Toast.makeText(this, "HA HABIDO ERRORES", Toast.LENGTH_SHORT).show()
                else
                    pintarFinal(casillas)


            }

    }

    private fun quitarPares(casillas: ArrayList<Casilla>): ArrayList<Casilla> {

        for (casilla in casillas) {
            if (casilla.resultado == null) {


                val fila = getMismaFila(casillas, casilla.fila)
                for (cFila in fila)
                    if (cFila.resultado == null)
                        if (casilla.opciones == cFila.opciones && !isMismaCasilla(casilla, cFila) && casilla.opciones.size == 2)
                            for (cLimpiar in fila)
                                if (!isMismaCasilla(cLimpiar, casilla) && !isMismaCasilla(cLimpiar, cFila) && cLimpiar.resultado == null)
                                    for (opcion in casilla.opciones)
                                        cLimpiar.actualizar(opcion)

                val columna = getMismaColumna(casillas, casilla.columna)
                for (cColumna in columna)
                    if (cColumna.resultado == null)
                        if (casilla.opciones == cColumna.opciones && !isMismaCasilla(casilla, cColumna) && casilla.opciones.size == 2)
                            for (cLimpiar in columna)
                                if (!isMismaCasilla(cLimpiar, casilla) && !isMismaCasilla(cLimpiar, cColumna) && cLimpiar.resultado == null)
                                    for (opcion in casilla.opciones)
                                        cLimpiar.actualizar(opcion)

                val cuadricula = getMismaCuadricula(casillas, casilla.cuadricula)
                for (cCuadricula in cuadricula)
                    if (cCuadricula.resultado == null)
                        if (casilla.opciones == cCuadricula.opciones && !isMismaCasilla(casilla, cCuadricula) && casilla.opciones.size == 2)
                            for (cLimpiar in cuadricula)
                                if (!isMismaCasilla(cLimpiar, casilla) && !isMismaCasilla(cLimpiar, cCuadricula) && cLimpiar.resultado == null)
                                    for (opcion in casilla.opciones)
                                        cLimpiar.actualizar(opcion)
            }
        }

        return casillas
    }

    private fun quitarPorCuadrante(casillas: ArrayList<Casilla>): Boolean{
        var swCambios = false
        for (casilla in casillas) {
            if (casilla.resultado == null) {
                val opcionesCasilla = casilla.opciones
                val cuadricula = getMismaCuadriculaDiferentesVacios(casilla, casillas)
                for (opcion in opcionesCasilla) {
                    val aux = arrayListOf<Casilla>()
                    for (cCuadricula in cuadricula)
                        if (cCuadricula.opciones.contains(opcion))
                            aux.add(cCuadricula)

                    if (aux.size > 0) {
                        if (isTodosMismaFila(casilla, aux)) {
                            val mismaFila = getMismaFila(casillas, casilla.fila)
                            for (cBorrar in mismaFila)
                                if (cBorrar.resultado == null && casilla.cuadricula != cBorrar.cuadricula)
                                    if (cBorrar.opciones.contains(opcion)) {
                                        cBorrar.actualizar(opcion)
                                        if (cBorrar.resultado != null)
                                            swCambios = true
                                    }
                        }
                        if (isTodosMismaColumna(casilla, aux)) {
                            val mismaColumna = getMismaColumna(casillas, casilla.columna)
                            for (cBorrar in mismaColumna)
                                if (cBorrar.resultado == null && casilla.cuadricula != cBorrar.cuadricula)
                                    if (cBorrar.opciones.contains(opcion)) {
                                        cBorrar.actualizar(opcion)
                                        if (cBorrar.resultado != null)
                                            swCambios = true
                                    }

                        }
                    }
                }
            }
        }

        return swCambios
    }

    private fun isTodosMismaFila (casillaRevision: Casilla, casillasComparteOpciones: ArrayList<Casilla>) : Boolean {
        val fila = casillaRevision.fila
        for (casillaO in casillasComparteOpciones)
            if (casillaO.fila != fila)
                return false
        return true
    }

    private fun isTodosMismaColumna(casillaRevision: Casilla, casillasComparteOpciones: ArrayList<Casilla>) : Boolean {
        val columna = casillaRevision.columna
        for (casillaO in casillasComparteOpciones)
            if (casillaO.columna != columna)
                return false
        return true
    }

    private fun segundaComprobacion(casillas: ArrayList<Casilla>) {
        for (casilla in casillas)
            if (casilla.resultado == null)
                for (i in 1..9)
                    comprobacionPro(casilla, i, casillas)
    }

    // la idea es recorrer todas las casillas de la misma fila y comprobar los casos donde solo haya
    // una opción válida, en ese caso sabremos que es la indicada. Para ello, sobre cada casilla,
    // miramos si en la columna ya
    private fun comprobacionPro(casilla: Casilla, numero: Int, casillas: ArrayList<Casilla>) {
        val casillasAux = arrayListOf<Casilla>()
        val casillasCuadricula = getMismaCuadricula(casillas, casilla.cuadricula)

        // solo comprobamos cuando el número no esté en la cuadricula
        if (!contieneNumero(casillasCuadricula, numero)) {
            for (opcion in casillasCuadricula) {
                // del cuadrante solo queremos mirar las casillas que estén vacías
                if (opcion.resultado == null) {
                    // miramos si en esa misma fila ya está metido
                    val laFilaComprobacion = getMismaFila(casillas, opcion.fila)
                    if (!contieneNumero(laFilaComprobacion, numero)) {
                        // comprobamos si aparece en la columna
                        val laColumnaComprobacion = getMismaColumna(casillas, opcion.columna)
                        if (!contieneNumero(laColumnaComprobacion, numero)) {
                            casillasAux.add(opcion)
                            if (casillasAux.size > 1) {
                                break
                            }
                        }
                    }
                }
            }
        }

        if (casillasAux.size == 1) {
            casillasAux[0].resolver(numero)
            casillasAux[0].resultado = numero
            resolver(casillas)
        }


    }

    private fun contieneNumero (casillas: ArrayList<Casilla>, numero: Int) : Boolean {
        for (casilla in casillas)
            if (casilla.resultado != null)
                if (casilla.resultado == numero)
                    return true
        return false
    }

    private fun getMismaFila(casillas: ArrayList<Casilla>, fila: Int): ArrayList<Casilla> {
        val aux = arrayListOf<Casilla>()
        for (opcion in casillas)
            if (opcion.fila == fila)
                aux.add(opcion)
        return aux
    }

    private fun getMismaColumna(casillas: ArrayList<Casilla>, columna: Int): ArrayList<Casilla> {
        val aux = arrayListOf<Casilla>()
        for (opcion in casillas)
            if (opcion.columna == columna)
                aux.add(opcion)
        return aux
    }

    private fun getMismaCuadricula(casillas: ArrayList<Casilla>, cuadricula: Int): ArrayList<Casilla> {
        val aux = arrayListOf<Casilla>()
        for (opcion in casillas)
            if (opcion.cuadricula == cuadricula)
                aux.add(opcion)
        return aux
    }

    private fun getMismaCuadriculaDiferentesVacios(casilla: Casilla, casillas: ArrayList<Casilla>): ArrayList<Casilla>{
        val aux = arrayListOf<Casilla>()
        val cuadricula = casilla.cuadricula
        for (opcion in casillas)
            if (opcion.cuadricula == cuadricula && opcion.resultado == null && !isMismaCasilla(casilla, opcion))
                aux.add(opcion)

        return aux
    }

    // Recorremos todas las casillas, en cuanto encuentra que hay que comprobar, devolvemos true
    private fun isCasillasAComprobar(casillas: ArrayList<Casilla>): Boolean {
        for (casilla in casillas)
            if (casilla.swComprobar)
                return true
        return false
    }

    // Vamos a recorrer todas las casillas para buscar aquellas que hayan tenido actualizaciones
    private fun eliminarPosibilidades(casillas: ArrayList<Casilla>): ArrayList<Casilla>{
        for (casilla in casillas) {
            var revisar: ArrayList<Casilla>? = null
            if (casilla.swComprobar ) {
                // En caso de que la casilla haya sido actualizada, obtenemos todas las casillas que
                // están relacionadas (fila, columna y cuadrante) y acutalizamos el estado indicando
                // que ya no habrá que volver a comprobarla
                revisar = getCasillasRelacionadas(casillas,casilla)
                casilla.swComprobar = false
            }
            // En caso de que haya casillas relacionadas para revisar, indicamos sobre todas ellas
            // que tienen que eliminar el número que ya sabemos que es definitivo
            if (revisar!=null)
                for (casillaAActualizar in revisar)
                    casillaAActualizar.actualizar(casilla.resultado!!)
        }
        return casillas
    }

    // Vamos a recorrer todas las casillas, y usando los datos de la casilla actualizada (fila,
    // columna y cuadrícula) devolvemos las que están relacionadas
    private fun getCasillasRelacionadas (casillas: ArrayList<Casilla>, casillaActualizada: Casilla): ArrayList<Casilla> {
        val implicados = ArrayList<Casilla>()
        casillas.forEach {
            // Las que ya están resueltas no nos interesan, sólo las pendientes de resolver
            if(it.resultado==null)
                if (isCasillaRelacionada(it, casillaActualizada))
                    implicados.add(it)
        }
        return implicados
    }

    // Comparamos dos casillas, si comparten fila, columna o cuadrante, indicamos que sí es relacionada
    private fun isCasillaRelacionada(casilla1 : Casilla, casilla2: Casilla) : Boolean{
        if (casilla1.fila == casilla2.fila || casilla1.columna == casilla2.columna ||
                casilla1.cuadricula == casilla2.cuadricula)
            return true
        return false
    }

    // Recorremos todas las casillas, en cuanto haya una casilla no resuelta devolvemos un false
    private fun isSudokuResuelto(casillas: ArrayList<Casilla>) : Boolean {
        for (casilla in casillas )
            if (casilla.resultado == null)
                return false
        return true
    }

    // Usamos esta función para comprobar si tenemos en una misma fila, columna o cuadrante dos
    // resultados iguales
    private fun isError(casillas: ArrayList<Casilla>): Boolean{
        // Recorremos todas las casillas
        for (casillaReferencia in casillas)
            // Comprobamos solo las casillas con resultado
            if (casillaReferencia.resultado!= null)
                // Volvemos a recorrer el array para poder comparar los resultados
                for (casillaContra in casillas)
                    // Si en la segunda vuelta tampoco tiene resultado no necesitamos comprobar
                    if (casillaContra.resultado!=null)
                        // Ahora comprobamos que las dos casillas están relacionadas, dejando fuera
                        // obviamente los casos en los que las dos casillas sean la misma
                        if (isCasillaRelacionada(casillaReferencia, casillaContra) && !isMismaCasilla(casillaReferencia, casillaContra))

                            // ESTE CASO COMPRAMOS EL RESULTADO DE AMBAS CASILLAS, SI SON EL MISMO
                            // HA HABIDO ALGUN ERROR
                            if (casillaReferencia.resultado == casillaContra.resultado)
                                return true
        return false
    }

    // TENEMOS DOS CASILLAS PARA COMPARAR, SI COMPARTEN FILA Y COLUMNA SON LA MISMA
    private fun isMismaCasilla(casilla1: Casilla, casilla2: Casilla) : Boolean {
        if (casilla1.fila == casilla2.fila && casilla1.columna == casilla2.columna)
            return true
        return false
    }

    // metodo para forzar a un resultado
    private fun actualizarUno(casillas: ArrayList<Casilla>): ArrayList<Casilla>{
        for (casilla in casillas){
            if (casilla.opciones.size == 2){
                println("Estamos poniendo en la casilla [${casilla.fila}, ${casilla.columna}] el valor ${casilla.opciones.first()}, las opciones eran ${casilla.opciones.toString()}")
                val prueba = casilla.opciones.first()
                casilla.actualizar(prueba)
                break
            }
        }
        return casillas
    }


    // RECORREMOS TODAS LAS CASILLAS CON EL RESTULTADO, Y POR EL ORDEN RECOGEMOS SU
    // CAJA EQUIVALENTE Y LO PONEMOS EN EL TEXT
    private fun pintarFinal(casillas: ArrayList<Casilla>){
        for ((contador, casilla) in casillas.withIndex()){
            if (casilla.resultado!=null){
                val valor = casilla.resultado
                val caja: EditText = cajas[contador]
                if (!casilla.swInical)
                    caja.setTextColor(getColor(R.color.naranjaMarron))
                caja.value= valor!!.toString()
            }
        }
    }

    private fun imprimirOpciones(casillas: ArrayList<Casilla>) {
        for (casilla in casillas) {
            if (casilla.resultado == null)
                Log.i("Opciones finales", "Casilla f${casilla.fila}-C${casilla.columna}: opciones ${casilla.opciones.toString()}")
        }
    }



    // Dejamos todas las casillas en blanco
    private fun limpiarPantalla() {
        val tabla : TableLayout = findViewById(R.id.tlTablero)
        for (tableRow in tabla.children) {
            tableRow as TableRow
            for (editText in tableRow.children){
                if (editText is EditText) {
                    editText.value = ""
                    editText.setTextColor(getColor(R.color.naranja))
                }

            }
        }
    }

}



//OJO ESTO
var EditText.value
    get() = this.text.toString()
    set(value) {
        this.setText(value)
    }