package com.gus.practicando.sudoku

class Casilla (val fila: Int, val columna: Int, var cuadricula : Int= 0, var resultado: Int? = null,
               var swComprobar : Boolean = false, var swInical : Boolean = false) {

    // Indicamos que en cada casilla, inicialmente van a estar las nueve opciones posibles
    var opciones : MutableList<Int> = arrayListOf(1,2,3,4,5,6,7,8,9)

    // Con la columna y fila, ya conocidas, determinamos a que cuadrante pertenece
    init{
        cuadricula = when (columna) {
            in 0 .. 2 -> {
                when (fila){
                    in 0 .. 2 -> {
                        0
                    }
                    in 3..5 -> {
                        3
                    }
                    in 6..8 -> {
                        6
                    }
                    else ->
                        10
                }
            }
            in 3..5 -> {
                when (fila){
                    in 0 .. 2 -> {
                        1
                    }
                    in 3..5 -> {
                        4
                    }
                    in 6..8 -> {
                        7
                    }
                    else ->
                        10
                }
            }
            in 6..8 -> {
                when (fila){
                    in 0 .. 2 -> {
                        2
                    }
                    in 3..5 -> {
                        5
                    }
                    in 6..8 -> {
                        8
                    }
                    else ->
                        10
                }
            }
            else ->
                10
        }
    }

    // Cuando sabemos cual es número final de la casilla, vacíamos las opciones para luego dejar sólo
    // el resultado final e indicamos que necesita comprobación en la siguiente vuelta que demos
    // sobre esta casilla
    fun resolver(nuevoValor: Int) {
        opciones.clear()
        opciones.add(nuevoValor)
        swComprobar = true
    }

    // Llamamos a esta función cuando ya sabemos el número que tenemos que eliminar de las posibles
    // opciones que quedan en la casilla
    fun actualizar(numEliminar:Int){
        // Recorremos las opciones que quedan para comprobar si aún estaba la que queremos eliminar,
        // en ese caso la eliminamos
        val opcionesActualizadas = mutableListOf<Int>()
        for (opcion in opciones) {
            if (opcion != numEliminar)
                opcionesActualizadas.add(opcion)
        }

        opciones = opcionesActualizadas

        // Por último llamaos a la comprobación de si se ha resuelto o no la casilla
        comprobarSiEstaResuelto()

    }

    // Después de actualizar la casilla comprobamos si se ha resuelto. Lo hacemos con el número de
    // opciones que le quedan en la casilla, si solo queda 1 estará resuelta por lo que dejamos
    // en resultado y también indicamos que en las próximas vueltas de casillas deberán comprobar
    // esta casilla ya que puede haber generado cambio en sus filas, columnas y cuadrante relacionado
    private fun comprobarSiEstaResuelto() {
        if (opciones.size == 1){
            resultado = opciones[0]
            swComprobar = true
        }
    }

/*
    fun getNumeroColumnaRelacionadas (): IntArray {
        val aux = intArrayOf()
        when  (columna) {
            0 -> {
                aux[0] = 1
                aux[1] = 2
            }
            1 -> {
                aux[0] = 0
                aux[1] = 2
            }
            2 -> {
                aux[0] = 0
                aux[1] = 1
            }

            3 -> {
                aux[0] = 4
                aux[1] = 5
            }
            4 -> {
                aux[0] = 3
                aux[1] = 5
            }
            5 -> {
                aux[0] = 3
                aux[1] = 4
            }

            6 -> {
                aux[0] = 7
                aux[1] = 8
            }
            7 -> {
                aux[0] = 6
                aux[1] = 8
            }
            8 -> {
                aux[0] = 6
                aux[1] = 7
            }
        }

        return aux
    }

    fun getNumeroFilaRelacionadas (): IntArray {
        val aux = intArrayOf()
        when  (fila) {
            0 -> {
                aux[0] = 1
                aux[1] = 2
            }
            1 -> {
                aux[0] = 0
                aux[1] = 2
            }
            2 -> {
                aux[0] = 0
                aux[1] = 1
            }

            3 -> {
                aux[0] = 4
                aux[1] = 5
            }
            4 -> {
                aux[0] = 3
                aux[1] = 5
            }
            5 -> {
                aux[0] = 3
                aux[1] = 4
            }

            6 -> {
                aux[0] = 7
                aux[1] = 8
            }
            7 -> {
                aux[0] = 6
                aux[1] = 8
            }
            8 -> {
                aux[0] = 6
                aux[1] = 7
            }
        }

        return aux
    }
*/

}