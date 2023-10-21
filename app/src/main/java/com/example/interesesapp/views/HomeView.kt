package com.example.interesesapp.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.interesesapp.components.Alert
import com.example.interesesapp.components.MainButton
import com.example.interesesapp.components.MainTextField
import com.example.interesesapp.components.ShowInfoCard
import com.example.interesesapp.components.SpaceH
import java.math.BigDecimal
import java.math.RoundingMode
import androidx.compose.ui.graphics.Color
import com.example.interesesapp.viewmodels.PrestamoViewModel

@Composable
fun ContentHomeView(paddingValues: PaddingValues, viewModel: PrestamoViewModel){
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(10.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var montoPrestamo by remember { mutableStateOf("")}
        var cantCuotas by remember { mutableStateOf("")}
        var tasa by remember { mutableStateOf("")}
        var montoInteres by remember { mutableStateOf(0.0)}
        var montoCuota by remember { mutableStateOf(0.0)}
        var showAlert by remember { mutableStateOf(false)}

        val state=viewModel.state

        ShowInfoCard(
            titleInteres = "Total: ",
            montoInteres = state.montoIntereses,
            titleMonto = "Cuota: ",
            monto = state.montoCuota
        )
        MainTextField(value = state.montoPrestamo,
            onValueChange = { viewModel.onValue(value = it, campo = "montoPrestamo")}, label = "Prestamo" )
        SpaceH()
        MainTextField(value = state.cantCuotas,
            onValueChange = {viewModel.onValue(value = it, campo = "cuotas")}, label = "Cuotas")
        SpaceH(10.dp)
        MainTextField(value = state.tasa,
            onValueChange = {viewModel.onValue(value = it, campo = "tasa")}, label = "Tasa")
        SpaceH(10.dp)

        MainButton(text = "Calcular"){
            viewModel.calcular()
        }
        SpaceH()
        MainButton(text = "Limpiar", color = Color.Red) {
            viewModel.Limpiar()
        }
        if(viewModel.state.showAlert){
            Alert(title = "Alerta",
                message = "Ingresa los datos",
                confirmText = "Aceptar",
                onConfirmClick = { viewModel.confirmDialog() }){}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: PrestamoViewModel){
    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Calculadora Prestamos", color = Color.White)},
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor =  MaterialTheme.colorScheme.inversePrimary
            )
        )
    }){
        ContentHomeView(it, viewModel)
    }
}

fun calcularCuota(montoPrestamo: Double, cantCuotas: Int, tasaInteresAnual: Double): Double {
    val tasaInteresAnual = tasaInteresAnual /12 / 100

    val cuota = montoPrestamo * tasaInteresAnual * Math.pow(1 + tasaInteresAnual, cantCuotas.toDouble())/
            (Math.pow(1 + tasaInteresAnual, cantCuotas.toDouble()) - 1)

    val cuotaRedondeada = BigDecimal(cuota).setScale( 2, RoundingMode.HALF_UP).toDouble()

    return cuotaRedondeada
    return cuota
}

fun calcularTotal(montoPrestamo:Double, cantCuotas:Int, tasaInteresAnual: Double): Double {
    val res = cantCuotas * calcularCuota(montoPrestamo, cantCuotas, tasaInteresAnual)
    return BigDecimal(res).setScale(2, RoundingMode.HALF_UP).toDouble()
}