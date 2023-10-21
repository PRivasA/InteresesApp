package com.example.interesesapp.models

data class PrestamoState(
    val montoPrestamo: String="",
    val cantCuotas: String="",
    val tasa: String="",
    val montoIntereses:Double=0.0,
    val montoCuota: Double=0.0,
    var showAlert: Boolean=false
)
