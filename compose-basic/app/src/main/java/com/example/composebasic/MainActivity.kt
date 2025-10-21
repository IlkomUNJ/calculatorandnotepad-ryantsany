package com.example.composebasic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composebasic.ui.screen.Calculator
import com.example.composebasic.ui.screen.Notepad
import com.example.composebasic.ui.screen.TextEditor
import com.example.composebasic.ui.theme.ComposeBasicTheme
import com.example.composebasic.ui.viewmodel.TextEditorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeBasicTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

object NavRoutes {
    const val MAIN = "main_screen"
    const val CALCULATOR = "calculator_screen"
    const val NOTEPAD = "notepad_screen"
    const val TEXT_EDITOR = "text_editor_screen"
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {

    val navigationController = rememberNavController()
    val notepadViewModel: TextEditorViewModel = viewModel()

    Surface(modifier) {
        NavHost(
            navController = navigationController,
            startDestination = NavRoutes.MAIN
        ) {
            composable(NavRoutes.MAIN) {
                MainScreen(navController = navigationController)
            }

            composable(NavRoutes.CALCULATOR) {
                Calculator(navController = navigationController)
            }

            composable(NavRoutes.NOTEPAD) {
                Notepad(navController = navigationController, viewModel = notepadViewModel)
            }

            composable(NavRoutes.TEXT_EDITOR) {
                TextEditor(navController = navigationController, viewModel = notepadViewModel)
            }
        }
    }
}

@Preview
@Composable
fun MyAppPreview() {
    ComposeBasicTheme {
        MyApp(Modifier.fillMaxSize())
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavController) {
    Column (
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button (
            onClick = {navController.navigate(NavRoutes.CALCULATOR)}
        ) {
            Text("Calculator")
        }
        Button (
            onClick = {navController.navigate(NavRoutes.NOTEPAD)}
        ) {
            Text("Notepad")
        }
    }
}

