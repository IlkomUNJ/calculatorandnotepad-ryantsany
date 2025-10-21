package com.example.composebasic.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

data class CalculatorUiState (
    var inputs: String = "0",
    var shouldShowScientific: Boolean = false,
    var shouldShowInverse: Boolean = false,
    var resetDisplay: Boolean = false
)

class CalculatorViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState = _uiState.asStateFlow()

    fun addNumber(input: String){
        if(_uiState.value.resetDisplay){
            _uiState.update {
                it.copy(
                    resetDisplay = false,
                    inputs = input
                )
            }
        } else if(_uiState.value.inputs == "0"){
            _uiState.update {
                it.copy(
                    inputs = input
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    inputs = _uiState.value.inputs + input
                )
            }
        }
    }

    fun addOperator(operator: String) {
        if(_uiState.value.inputs.isNotEmpty() && !(_uiState.value.inputs.last() == '+' || _uiState.value.inputs.last() == '-' || _uiState.value.inputs.last() == '÷' || _uiState.value.inputs.last() == 'x')){
            _uiState.update {
                it.copy(
                    inputs = _uiState.value.inputs + operator
                )
            }
        }
    }

    fun basicMath(expr: String): Double {
        val terms = mutableListOf<String>()
        val operators = mutableListOf<String>()
        var currentTerm = ""

        for (i in expr.indices) {
            val char = expr[i]
            if (char in listOf('+', '-') && i > 0) {
                terms.add(currentTerm)
                operators.add(char.toString())
                currentTerm = ""
            } else {
                currentTerm += char
            }
        }
        terms.add(currentTerm)

        val evaluatedTerms = terms.map { term ->
            var result = 0.0
            var currentNum = ""
            var operation = ""

            for (char in term) {
                if (char in listOf('*', '/')) {
                    if (currentNum.isNotEmpty()) {
                        if (operation.isEmpty()) {
                            result = currentNum.toDouble()
                        } else {
                            when (operation) {
                                "*" -> result = result * currentNum.toDouble()
                                "/" -> result = result / currentNum.toDouble()
                            }
                        }
                    }
                    operation = char.toString()
                    currentNum = ""
                } else {
                    currentNum += char
                }
            }

            if (currentNum.isNotEmpty()) {
                if (operation.isEmpty()) {
                    result = currentNum.toDouble()
                } else {
                    when (operation) {
                        "*" -> result = result * currentNum.toDouble()
                        "/" -> result = result / currentNum.toDouble()
                    }
                }
            }
            result
        }

        var finalResult = evaluatedTerms[0]
        for (i in operators.indices) {
            when (operators[i]) {
                "+" -> finalResult += evaluatedTerms[i + 1]
                "-" -> finalResult -= evaluatedTerms[i + 1]
            }
        }

        return finalResult
    }

    fun calculateFactorial(n: Int): Double{
        if (n > 1){
            return n * calculateFactorial(n - 1)
        }
        return 1.0
    }

    fun evaluate(expr: String): Double {
        val expression = expr.replace("x", "*").replace("÷", "/").replace("−", "-")

        when {
            expression.startsWith("√") -> {
                val num = expression.drop(1).toDouble()
                return sqrt(num)
            }
            expression.endsWith("!") -> {
                val num = expression.dropLast(1).toInt()
                return calculateFactorial(num)
            }
            expression.contains("^") -> {
                val parts = expression.split("^")
                if (parts.size == 2) {
                    val base = evaluate(parts[0])
                    val exp = evaluate(parts[1])
                    return base.pow(exp)
                }
            }
            expression.startsWith("1/") -> {
                val num = expression.drop(2).toDouble()
                return 1 / num
            }
            expression.startsWith("eksp(") -> {
                val num = expression.removePrefix("eksp(").toDouble()
                return exp(num)
            }
            expression.startsWith("log(") -> {
                val num = expression.removePrefix("log(").toDouble()
                return log10(num)
            }
            expression.startsWith("ln(") -> {
                val num = expression.removePrefix("ln(").toDouble()
                return ln(num)
            }
            expression.startsWith("sin(") -> {
                val num = expression.removePrefix("sin(").toDouble()
                return sin(Math.toRadians(num))
            }
            expression.startsWith("cos(") -> {
                val num = expression.removePrefix("cos(").toDouble()
                return cos(Math.toRadians(num))
            }
            expression.startsWith("tan(") -> {
                val num = expression.removePrefix("tan(").toDouble()
                return tan(Math.toRadians(num))
            }
            expression.startsWith("asin(") -> {
                val num = expression.removePrefix("asin(").toDouble()
                return Math.toDegrees(asin(num))
            }
            expression.startsWith("acos(") -> {
                val num = expression.removePrefix("acos(").toDouble()
                return Math.toDegrees(acos(num))
            }
            expression.startsWith("atan(") -> {
                val num = expression.removePrefix("atan(").toDouble()
                return Math.toDegrees(atan(num))
            }
        }

        return basicMath(expression)
    }

    fun calculate() {
        try {
            val result = evaluate(_uiState.value.inputs)

            if (result.isNaN() || result.isInfinite()) {
                _uiState.update {
                    it.copy(
                        inputs = "Error",
                        resetDisplay = true
                    )
                }
            } else if (result % 1.0 == 0.0) {
                _uiState.update {
                    it.copy(
                        inputs = result.toLong().toString(),
                        resetDisplay = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        inputs = result.toString(),
                        resetDisplay = true
                    )
                }
            }

        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    inputs = "Error",
                    resetDisplay = true
                )
            }
        }
    }

    fun calculatePercent() {
        val current = _uiState.value.inputs.toDoubleOrNull() ?: return
        val result = current / 100
        if (result % 1.0 == 0.0) {
            _uiState.update {
                it.copy(
                    inputs = result.toLong().toString()
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    inputs = result.toString()
                )
            }

        }
    }

    fun clear(){
        _uiState.update {
            it.copy(
                inputs = "0"
            )
        }
    }

    fun backspace(){
        if(_uiState.value.resetDisplay){
            _uiState.update {
                it.copy(
                    inputs = "0"
                )
            }
        } else if(_uiState.value.inputs.length > 1){
            _uiState.update {
                it.copy(
                    inputs = _uiState.value.inputs.dropLast(1)
                )
            }
        } else if(_uiState.value.inputs.length == 1 && _uiState.value.inputs != "0"){
            _uiState.update {
                it.copy(
                    inputs = "0"
                )
            }
        }
    }

    fun toggleScientific(){
        _uiState.update {
            it.copy(
                shouldShowScientific = !_uiState.value.shouldShowScientific
            )
        }
    }

    fun toggleInverse(){
        _uiState.update {
            it.copy(
                shouldShowInverse = !_uiState.value.shouldShowInverse
            )
        }
    }

}