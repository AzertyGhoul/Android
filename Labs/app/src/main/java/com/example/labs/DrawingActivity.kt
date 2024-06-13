package com.example.labs

import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.labs.ui.theme.LabsTheme

class DrawingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val buttonName = arrayOf(
                stringResource(R.string.rect),
                stringResource(R.string.oval),
                stringResource(R.string.image),
                stringResource(R.string.save)
            )
            val myView: MyGraphView? = MyGraphView(applicationContext)
            val viewRemember = remember {
                mutableStateOf(myView)
            }
            LabsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        MakeTopButtons(buttonName, viewRemember.value)
                        CustomView(viewRemember.value)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MakeTopButtons(buttonName: Array<String>, myView: MyGraphView?) {
        var primiveChoice by remember { mutableStateOf(false) }
        var colorChoice by remember { mutableStateOf(false) }
        var strokeWidthChoise by remember { mutableStateOf(false) }
        var stokeJoinChoise by remember { mutableStateOf(false) }
        var strokeCapChoise by remember { mutableStateOf(false) }
        var textFieldActive by remember { mutableStateOf(false) }
        var buttonActive by remember { mutableStateOf(false) }
        var nameSave by remember { mutableStateOf("") }
        TopAppBar(
            title = { Text(text = "Рисовалки")},
            modifier = Modifier.padding(10.dp).border(BorderStroke(2.dp, Color(149, 79, 65)), RoundedCornerShape(5.dp)),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(103, 59, 50)
            ),
            actions = {
                IconButton(
                    onClick = {primiveChoice = !primiveChoice}
                ) {
                    Icon(Icons.Default.Settings, null)
                }

                IconButton(
                    onClick = {colorChoice = !colorChoice}
                ) {
                    Icon(Icons.Default.Settings, null)
                }

                IconButton(
                    onClick = {strokeWidthChoise = !strokeWidthChoise}
                ) {
                    Icon(Icons.Default.Settings, null)
                }

                IconButton(
                    onClick = {stokeJoinChoise = !stokeJoinChoise}
                ) {
                    Icon(Icons.Default.Settings, null)
                }

                IconButton(
                    onClick = {strokeCapChoise = !strokeCapChoise}
                ) {
                    Icon(Icons.Default.Settings, null)
                }

                DropdownMenu(
                    expanded = primiveChoice,
                    onDismissRequest = {primiveChoice = false}
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Circle") },
                        onClick = {
                            myView!!.funcArray[buttonName.lastIndexOf(buttonName[1])]()
                            primiveChoice = !primiveChoice
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(text = "Image") },
                        onClick = {
                            myView!!.funcArray[buttonName.lastIndexOf(buttonName[2])]()
                            primiveChoice = !primiveChoice
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(text = "Rect") },
                        onClick = {
                            myView!!.funcArray[buttonName.lastIndexOf(buttonName[0])]()
                            primiveChoice = !primiveChoice
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(text = "Second name") },
                        onClick = {
                            myView!!.drawSecondName()
                            primiveChoice = !primiveChoice
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(text = "Save") },
                        onClick = {
                            buttonActive = !buttonActive
                            primiveChoice = !primiveChoice
                            textFieldActive = !textFieldActive
                        }
                    )
                }
                DropdownMenu(
                    expanded = colorChoice,
                    onDismissRequest = {colorChoice = false}
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "White") },
                        onClick = {
                            myView!!.setColor(android.graphics.Color.WHITE)
                            colorChoice = !colorChoice
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Black") },
                        onClick = {
                            myView!!.setColor(android.graphics.Color.BLACK)
                            colorChoice = !colorChoice
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Gray") },
                        onClick = {
                            myView!!.setColor(android.graphics.Color.GRAY)
                            colorChoice = !colorChoice
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Ltgray") },
                        onClick = {
                            myView!!.setColor(android.graphics.Color.LTGRAY)
                            colorChoice = !colorChoice
                        }
                    )
                }
                DropdownMenu(
                    expanded = strokeWidthChoise,
                    onDismissRequest = {strokeWidthChoise = false}
                ) {
                    DropdownMenuItem(
                        text = {Text(text = "10F")},
                        onClick = {
                            myView!!.setStrokeWidth(10F)
                            strokeWidthChoise = !strokeWidthChoise
                        }
                    )
                    DropdownMenuItem(
                        text = {Text(text = "15F")},
                        onClick = {
                            myView!!.setStrokeWidth(15F)
                            strokeWidthChoise = !strokeWidthChoise
                        }
                    )
                    DropdownMenuItem(
                        text = {Text(text = "20F")},
                        onClick = {
                            myView!!.setStrokeWidth(20F)
                            strokeWidthChoise = !strokeWidthChoise
                        }
                    )
                    DropdownMenuItem(
                        text = {Text(text = "25F")},
                        onClick = {
                            myView!!.setStrokeWidth(25F)
                            strokeWidthChoise = !strokeWidthChoise
                        }
                    )
                }

                DropdownMenu(
                    expanded = stokeJoinChoise,
                    onDismissRequest = {stokeJoinChoise = !stokeJoinChoise}
                ) {
                    DropdownMenuItem(
                        text = {Text(text = "Round")},
                        onClick = {
                            myView!!.setStrokeJoin(Paint.Join.ROUND)
                            stokeJoinChoise = !stokeJoinChoise
                        }
                    )
                    DropdownMenuItem(
                        text = {Text(text = "Bevel")},
                        onClick = {
                            myView!!.setStrokeJoin(Paint.Join.BEVEL)
                            stokeJoinChoise = !stokeJoinChoise
                        }
                    )
                    DropdownMenuItem(
                        text = {Text(text = "Miter")},
                        onClick = {
                            myView!!.setStrokeJoin(Paint.Join.MITER)
                            stokeJoinChoise = !stokeJoinChoise
                        }
                    )
                }

                DropdownMenu(
                    expanded = strokeCapChoise,
                    onDismissRequest = {strokeCapChoise = false}
                ) {
                    DropdownMenuItem(
                        text = {Text(text = "Round")},
                        onClick = {
                            myView!!.setStrokeCap(Paint.Cap.ROUND)
                            strokeCapChoise = !strokeCapChoise
                        }
                    )
                    DropdownMenuItem(
                        text = {Text(text = "Butt")},
                        onClick = {
                            myView!!.setStrokeCap(Paint.Cap.BUTT)
                            strokeCapChoise = !strokeCapChoise
                        }
                    )
                    DropdownMenuItem(
                        text = {Text(text = "Square")},
                        onClick = {
                            myView!!.setStrokeCap(Paint.Cap.SQUARE)
                            strokeCapChoise = !strokeCapChoise
                        }
                    )
                }
            }
        )
        Row (
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ){
            OutlinedTextField(
                value = nameSave,
                label = {
                    Text(text = "Enter name to save")
                },
                onValueChange = {nameSave = it},
                enabled = textFieldActive
            )
            Button(
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.padding(10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(149, 79, 65),
                    contentColor = Color.White,
                ),
                enabled = buttonActive,
                onClick = {
                    myView!!.saveImage(nameSave)
                    nameSave = ""
                    textFieldActive = !textFieldActive
                    buttonActive = !buttonActive
                },

            ) {
                Text(
                    text = "Save",
                )
            }
        }
    }

    @Composable
    fun CustomView(myView: MyGraphView?) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                myView!!
            }
        )

    }
}