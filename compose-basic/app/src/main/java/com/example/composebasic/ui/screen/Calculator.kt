package com.example.composebasic.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.composebasic.ui.theme.BlueEqualButton
import com.example.composebasic.ui.theme.DarkBlueButton
import com.example.composebasic.ui.theme.DarkGreyButton
import com.example.composebasic.ui.theme.Navy
import com.example.composebasic.ui.theme.onDarkBlueButton
import com.example.composebasic.ui.theme.onDarkGreyButton
import com.example.composebasic.ui.viewmodel.CalculatorViewModel
import kotlin.math.*

@Composable
fun Calculator(navController: NavController, viewModel: CalculatorViewModel = viewModel())
{
    val modifier : Modifier = Modifier
    val uiState by viewModel.uiState.collectAsState()

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(color = DarkGreyButton),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box (
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            contentAlignment = Alignment.BottomEnd
        ){
            Text(
                text = uiState.inputs,
                fontSize = 50.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = onDarkBlueButton
            )
        }

        Column (
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = Navy,
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                )
                .padding(bottom = 15.dp)

            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            if(uiState.shouldShowScientific){
                Row (
                    modifier = modifier
                        .padding(top = 10.dp)
                ) {
                    CalcButton(modifier = modifier.weight(1f), num = "⌄", click = { viewModel.toggleScientific() }, fontSize = 30.sp, contentColor = onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "x!", click = { viewModel.addNumber("!") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "xʸ", click = { viewModel.addNumber("^") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "√x", click = { viewModel.addNumber("√") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)

                }

                Row (
                    modifier = modifier
                        .padding(top = 10.dp)
                ) {
                    CalcButton(modifier = modifier.weight(1f), num = "INV", click = { viewModel.toggleInverse() }, fontSize = 20.sp, contentColor =  onDarkGreyButton)
                    if(!uiState.shouldShowInverse) {
                        CalcButton(modifier = modifier.weight(1f), num = "sin", click = { viewModel.addNumber("sin(") }, fontSize = 25.sp, contentColor = onDarkGreyButton)
                        CalcButton(modifier = modifier.weight(1f), num = "cos", click = { viewModel.addNumber("cos(") }, fontSize = 22.sp, contentColor = onDarkGreyButton)
                        CalcButton(modifier = modifier.weight(1f), num = "tan", click = { viewModel.addNumber("tan(") }, fontSize = 25.sp, contentColor = onDarkGreyButton)
                    } else {
                        CalcButton(modifier = modifier.weight(1f), num = "sin⁻¹", click = { viewModel.addNumber("asin(") }, fontSize = 15.sp, contentColor = onDarkGreyButton)
                        CalcButton(modifier = modifier.weight(1f), num = "cos⁻¹", click = { viewModel.addNumber("acos(") }, fontSize = 15.sp, contentColor = onDarkGreyButton)
                        CalcButton(modifier = modifier.weight(1f), num = "tan⁻¹", click = { viewModel.addNumber("atan(") }, fontSize = 15.sp, contentColor = onDarkGreyButton)
                    }
                }

                Row (
                    modifier = modifier
                        .padding(top = 10.dp)
                ) {
                    CalcButton(modifier = modifier.weight(1f), num = "1/x", click = { viewModel.addNumber("1/") }, fontSize = 25.sp, contentColor = onDarkGreyButton)
                    if(!uiState.shouldShowInverse){
                        CalcButton(modifier = modifier.weight(1f), num = "ln", click = { viewModel.addNumber("ln(")}, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    } else {
                        CalcButton(modifier = modifier.weight(1f), num = "e^", click = { viewModel.addNumber("eksp(") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    }
                    if(!uiState.shouldShowInverse){
                        CalcButton(modifier = modifier.weight(1f), num = "log", click = { viewModel.addNumber("log(") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    } else {
                        CalcButton(modifier = modifier.weight(1f), num = "10^", click = { viewModel.addNumber("10^") }, fontSize = 22.sp, contentColor =  onDarkGreyButton)
                    }

                    CalcButton(modifier = modifier.weight(1f), num = "e", click = { viewModel.addNumber("$E") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                }
            }
                Row (
                    modifier = modifier
                        .padding(top = 10.dp)
                ) {
                    CalcButton(modifier = modifier.weight(1f), num = "AC", click = { viewModel.clear() }, contentColor = onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "⌫", click = { viewModel.backspace() }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "SC", click = { viewModel.toggleScientific() }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "÷", click = { viewModel.addOperator("÷") }, fontSize = 25.sp, containerColor = DarkBlueButton, contentColor = onDarkBlueButton)

                }

                Row {
                    CalcButton(modifier = modifier.weight(1f), num = "7", click = { viewModel.addNumber("7") })
                    CalcButton(modifier = modifier.weight(1f), num = "8", click = { viewModel.addNumber("8") })
                    CalcButton(modifier = modifier.weight(1f), num = "9", click = { viewModel.addNumber("9") })
                    CalcButton(modifier = modifier.weight(1f), num = "x", click = { viewModel.addOperator("x") }, fontSize = 25.sp, containerColor = DarkBlueButton, contentColor = onDarkBlueButton)
                }



                Row {
                    CalcButton(modifier = modifier.weight(1f), num = "4", click = { viewModel.addNumber("4") })
                    CalcButton(modifier = modifier.weight(1f), num = "5", click = { viewModel.addNumber("5") })
                    CalcButton(modifier = modifier.weight(1f), num = "6", click = { viewModel.addNumber("6")  })
                    CalcButton(modifier = modifier.weight(1f), num = "−", click = { viewModel.addOperator("-") }, fontSize = 25.sp, containerColor = DarkBlueButton, contentColor = onDarkBlueButton)
                }



                Row {
                    CalcButton(modifier = modifier.weight(1f), num = "1", click = { viewModel.addNumber("1")  })
                    CalcButton(modifier = modifier.weight(1f), num = "2", click = { viewModel.addNumber("2")  })
                    CalcButton(modifier = modifier.weight(1f), num = "3", click = { viewModel.addNumber("3") })
                    CalcButton(modifier = modifier.weight(1f), num = "+", click = { viewModel.addOperator("+") }, fontSize = 25.sp, containerColor = DarkBlueButton, contentColor = onDarkBlueButton
                    )
                }



                Row {
                    CalcButton(modifier = modifier.weight(1f), num = "%", click = { viewModel.calculatePercent() }, fontSize = 25.sp)
                    CalcButton(modifier = modifier.weight(1f), num = "0", click = { viewModel.addNumber("0") })
                    CalcButton(modifier = modifier.weight(1f), num = ".", click = { viewModel.addNumber(".") }, fontSize = 25.sp)
                    CalcButton(modifier = modifier.weight(1f), num = "=", click = { viewModel.calculate() }, fontSize = 25.sp, containerColor = BlueEqualButton)
                }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview()
{
    Calculator(navController = rememberNavController())
}

@Composable
fun CalcButton (
    modifier : Modifier = Modifier,
    num : String = "",
    click : ()-> Unit,
    containerColor : Color = DarkGreyButton,
    contentColor : Color = onDarkBlueButton,
    fontSize : TextUnit = 20.sp
)
{
    Button(
        modifier = modifier
            .padding(5.dp)
            .aspectRatio(1.5f),
        onClick = click,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor

        ),
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            text = num,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
