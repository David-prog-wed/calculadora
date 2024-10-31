package com.example.calculadora

import android.icu.text.DecimalFormat
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {
    val SUMA = "+"
    val RESTA = "-"
    val MULTIPLICACION = "*"
    val DIVISION = "÷"
    val PORCENTAJE = "%"

    var operacionActual = ""

    var primerNumero: Double = Double.NaN
    var segundoNumero: Double = Double.NaN

    lateinit var tvTemp: TextView
    lateinit var tvResult: TextView

    lateinit var formatoDecimal: DecimalFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        formatoDecimal = DecimalFormat("#.##########")
        tvTemp = findViewById(R.id.tvTemp)
        tvResult = findViewById(R.id.tvResult)
    }

    fun cambiarOperador(b: View) {
        if (tvTemp.text.isNotEmpty() || primerNumero.toString()!="NaN") {
            calcular()
            operacionActual = when (val simbolo = (b as Button).text.toString().trim()) {
                "÷" -> "/"
                "X" -> "*"
                else -> simbolo
            }
            tvResult.text = formatoDecimal.format(primerNumero) + operacionActual
            tvTemp.text = ""
        }
    }

fun calcular() {
    try {
        if (!primerNumero.isNaN()) {
            if (tvTemp.text.isEmpty()) {
                tvTemp.text = tvResult.text
            }
            segundoNumero = tvTemp.text.toString().toDouble()

            when (operacionActual) {
                "+" -> primerNumero += segundoNumero
                "-" -> primerNumero -= segundoNumero
                "*" -> primerNumero *= segundoNumero
                "/" -> {
                    if (segundoNumero == 0.0) {
                        tvResult.text = "Error: División por cero"
                        primerNumero = Double.NaN
                        return
                    } else {
                        primerNumero /= segundoNumero
                    }
                }
                "%" -> primerNumero %= segundoNumero
            }

            tvTemp.text = "" // Limpiar tvTemp después de calcular
        } else {
            primerNumero = tvTemp.text.toString().toDouble()
        }
    } catch (e: NumberFormatException) {
        tvResult.text = "Error: Formato de número inválido"
        primerNumero = Double.NaN
    }
}

fun seleccionarNumero(b: View) {
    val boton = b as Button
    val nuevoDigito = boton.text.toString()

    if (nuevoDigito == "." && tvTemp.text.toString().contains(".")) {
        // Evitar múltiples puntos decimales
        return
    }
    val nuevoTexto = tvTemp.text.toString() + nuevoDigito
    if (nuevoTexto.length > 10) { // Limitar a 10 dígitos
        return
    }
    tvTemp.text = nuevoTexto
}
    fun igual(b:View){
        calcular()
        tvResult.text = formatoDecimal.format(primerNumero)
        //primerNumero = Double.NaN
        operacionActual = ""
        tvTemp.text = "" // Opcional: Limpiar tvTemp después de igual()
    }

    fun borrar(b: View) {
        val boton: Button = b as Button
        if (boton.text.toString().trim() == "C") {
            if(tvTemp.text.toString().isNotEmpty()){
                var datosActuales:CharSequence = tvTemp.text as CharSequence
                tvTemp.text = datosActuales.subSequence(0,datosActuales.length-1)
            }else{
                primerNumero = Double.NaN
                segundoNumero = Double.NaN
                tvTemp.text = ""
                tvResult.text = ""
            }
        }else if(boton.text.toString().trim() == "CA") {
            primerNumero = Double.NaN
            segundoNumero = Double.NaN
            tvTemp.text = ""
            tvResult.text = ""
        }
    }
}