package com.example.labs

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labs.ui.theme.LabsTheme

class InputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.secondaryContainer) {
                    addAuto()
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun addAuto() {
        var autoName by remember { mutableStateOf("") }
        var autoEngineCapacity by remember { mutableStateOf("") }
        var autoBrand by remember { mutableStateOf("") }

        val scope = rememberCoroutineScope()
    Column (
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            OutlinedTextField(
                value = autoName,
                onValueChange = {
                    autoName = it
                },
                label = {
                    Text(
                        text = "Plate",
                        fontSize = 20.sp
                    )
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                ),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(5.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors()

            )
            OutlinedTextField(
                value = autoEngineCapacity,
                onValueChange = {
                    autoEngineCapacity = it
                },
                label = {
                    Text(
                        text = "Capacity",
                        fontSize = 20.sp
                    )
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                ),
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(5.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            OutlinedTextField(
                value = autoBrand,
                onValueChange = {
                    autoBrand = it
                },
                label = {
                    Text(
                        text = "Brand",
                        fontSize = 20.sp
                    )
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                ),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(5.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )

        }
        Button(
            onClick = {
            if (autoEngineCapacity.isEmpty()) {
                autoEngineCapacity = "0"
            }
                val newAuto = Auto(autoName, autoEngineCapacity.toFloat(), autoBrand)
                val intent = Intent()
                intent.putExtra("newAuto", newAuto)
                autoName = ""
                autoEngineCapacity = ""
                autoBrand = ""
                setResult(RESULT_OK, intent)
                finish()
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.padding(10.dp, 0.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(149, 79, 65),
                contentColor = Color.White,
            )
        ) {
            Text("Add")
        }
    }
    }
}
