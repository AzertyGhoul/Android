package com.example.lab_1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_1.ui.theme.Lab_1Theme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab_1Theme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Ui()
                }
            }
        }
    }
}

@Composable
fun Ui() {
    var firstNumber by remember {
        mutableStateOf("")
    }

    var secondNumber by remember {
        mutableStateOf("")
    }

    var thirdNumber by remember {
        mutableStateOf("")
    }

    var answer by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    Column (
        modifier = Modifier.fillMaxSize().padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextField(
                value = firstNumber,
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                singleLine = true,
                onValueChange = {
                    firstNumber = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = secondNumber,
                modifier = Modifier.weight(1f),
                singleLine = true,
                onValueChange = {
                    secondNumber = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = thirdNumber,
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                singleLine = true,
                onValueChange = {
                    thirdNumber = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

        }

        Button (
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                if (firstNumber != "" && secondNumber != "" && thirdNumber != "") {
                    answer = (firstNumber.toFloat().pow(1/3f) * secondNumber.toFloat().pow(1/3f) * thirdNumber.toFloat().pow(1/3f)).toString()
                } else {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Calculate")
        }

        Text(
            text = "Tut budet cifra ${answer}"
        )
    }
}