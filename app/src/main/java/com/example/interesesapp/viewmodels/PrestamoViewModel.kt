package com.example.interesesapp.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.interesesapp.models.PrestamoState
import com.example.interesesapp.views.calcularCuota
import java.math.BigDecimal
import java.math.RoundingMode

class PrestamoViewModel: ViewModel() {
    var state by mutableStateOf(PrestamoState())
        private set

    fun  confirmDialog(){
        state=state.copy(showAlert = false)
    }
    fun Limpiar(){
        state=state.copy(
            montoPrestamo="",
            cantCuotas="",
            tasa="",
            montoIntereses=0.0,
            montoCuota=0.0
        )
    }
    fun onValue(value: String, campo: String){
        Log.i("pao",campo)
        Log.i("pao",value)
        when(campo){
            "montoPrestamo"-> state=state.copy(montoPrestamo = value)
            "cuotas"-> state=state.copy(cantCuotas = value)
            "tasa"-> state=state.copy(tasa=value)
        }
    }
    private fun calcularTotal(
        montoPrestamo: Double, cantCuotas:Int, tasaInteresesAnual:Double
    ):Double{
        val res= cantCuotas * calcularCuota(montoPrestamo, cantCuotas, tasaInteresesAnual)
        return BigDecimal(res).setScale(2, RoundingMode.HALF_UP).toDouble()
    }
    private fun calcularCuota(
        montoPrestamo: Double, cantCuotas: Int, tasaInteresesAnual: Double
    ):Double{
        val tasaInteresesMensual=tasaInteresesAnual/12/100
        val cuota=montoPrestamo * tasaInteresesMensual * Math.pow(
            1 + tasaInteresesMensual,
            cantCuotas.toDouble()
        ) / (Math.pow(1 + tasaInteresesMensual, cantCuotas.toDouble()) -1)
        val cuotaRedondeada=BigDecimal(cuota).setScale(2, RoundingMode.HALF_UP).toDouble()
        return cuotaRedondeada
    }
    fun calcular(){
        val montoPrestamo=state.montoPrestamo
        val cantCuotas=state.cantCuotas
        val tasa=state.tasa
        if (montoPrestamo!="" && tasa!=""){
            state=state.copy(
                montoCuota = calcularCuota(montoPrestamo.toDouble(), cantCuotas.toInt(), tasa.toDouble()),
                montoIntereses = calcularTotal(montoPrestamo.toDouble(), cantCuotas.toInt(), tasa.toDouble())
            )
        }else{
            state=state.copy(showAlert = true)
        }
    }
}