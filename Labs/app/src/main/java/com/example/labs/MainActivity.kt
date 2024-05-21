package com.example.labs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Display.Mode
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labs.ui.theme.LabsTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val viewModel = ItemViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lazyListState = rememberLazyListState()
            LabsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.secondaryContainer) {
                    Column(Modifier.fillMaxSize()) {
                        MakeAppBar(viewModel, lazyListState)
                        Main(viewModel, lazyListState)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeAppBar(viewModel: ItemViewModel, lazyListState: LazyListState) {
    var displayMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newAuto = result.data?.getSerializableExtra("newAuto") as Auto
            if (viewModel.autoListFlow.value.find { it.numberPlate == newAuto.numberPlate } == null) {
                viewModel.addAuto(newAuto)
                scope.launch {
                    lazyListState.scrollToItem(0)
                }
            } else {
                Toast.makeText(context, "Exists", Toast.LENGTH_LONG).show()
            }
        }
    }

    if (openDialog.value) {MakeAlertDialog(context, "About", openDialog)}

    TopAppBar(
        title = {Text(text = "Машинки")},
        modifier = Modifier.padding(10.dp).border(BorderStroke(2.dp, Color(149, 79, 65)),RoundedCornerShape(5.dp)),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(103, 59, 50)
        ),
        actions = {
            IconButton(
                onClick = {displayMenu = !displayMenu}
            ) {
                Icon(Icons.Default.ShoppingCart, null)
            }
            DropdownMenu(
                expanded = displayMenu,
                onDismissRequest = {displayMenu = !displayMenu},
                modifier = Modifier.background(Color(149, 79, 65))
            ) {
                DropdownMenuItem(
                    text = { Text(text = "About") },
                    onClick = {
                        openDialog.value = true
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Add auto") },
                    onClick = {
                        val newIntent = Intent(context, InputActivity::class.java)
                        startForResult.launch(newIntent)
                        displayMenu = !displayMenu
                    }
                )
            }
        }
    )
}

@Composable
fun Main(viewModel: ItemViewModel, lazyListState: LazyListState) {
    val context = LocalContext.current
    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize(),
        state = lazyListState
    ) {
        items(
            items = viewModel.autoListFlow.value,
            key = { auto -> auto.numberPlate } ,
            itemContent = { item ->
                ListRow(item)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeAlertDialog(context: Context, dialogTitle: String, openDialog: MutableState<Boolean>) {
    val autoInfo = remember { mutableStateOf("") }

    try {
        autoInfo.value = context.getString(context.resources.getIdentifier(dialogTitle, "string", context.packageName))
    }  catch (e: Resources.NotFoundException,) {
        autoInfo.value = "Not found"
    }
    AlertDialog(
        onDismissRequest = {openDialog.value = false},
        title = { Text(text = dialogTitle) },
        text = { Text(text = autoInfo.value) },
        containerColor = Color(103, 59, 50),
        confirmButton = {
            Button(
                onClick = {openDialog.value = false},
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(149, 79, 65),
                    contentColor = Color.White
                )
            ){
                Text(text = "Close")}
        }
    )
}

@Composable
fun MakeCringe(context: Context, cringeDialog: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = {cringeDialog.value = false},
        title = { Text(text = "JUST TAP") },
        text = { Text(text = "" +
                "JUST TAP JUST TAP JUST TAP JUST TAP " +
                "JUST TAP JUST TAP JUST TAP JUST TAP " +
                "JUST TAP JUST TAP JUST TAP JUST TAP") },
        confirmButton = {
            Button(
                onClick = {
                    cringeDialog.value = false
                }
            ) {
                Text(text = "JUST TAP")
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListRow(item: Auto) {
    val context = LocalContext.current
    val listCurrValue = remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val cringeDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        MakeAlertDialog(context, listCurrValue.value, openDialog)
    }

    if (cringeDialog.value) {
        MakeCringe(context, cringeDialog)
    }

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp).border(2.dp, Color(149, 79, 65), RoundedCornerShape(5.dp)).padding(10.dp)
            .combinedClickable (
                onClick = {
                    listCurrValue.value = item.brand
                    openDialog.value = true
                },
                onLongClick = {
                    cringeDialog.value = true
                }
            )
    ) {
        Column {
            Text(
                text = "Plate number : " + item.numberPlate,
                fontSize = 20.sp)
            Text(
                text = "Engine capacity : " + item.engineСapacity.toString() + "L",
                fontSize = 20.sp)
            Text(
                text = "Brand : " + item.brand,
                fontSize = 20.sp)
        }

        Image(
            painter = painterResource(id = item.picture),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(140.dp).clip(RoundedCornerShape(5.dp)),
            colorFilter = ColorFilter.tint(Color(149, 79, 65), blendMode = BlendMode.Hue)
        )
    }
}
