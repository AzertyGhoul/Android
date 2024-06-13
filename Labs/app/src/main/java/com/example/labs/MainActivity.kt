package com.example.labs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import com.example.labs.ui.theme.LabsTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val viewModel = ItemViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val DBHelper = autoDBhelper(this)
        if (savedInstanceState != null && savedInstanceState.containsKey("Auto")) {
            val tempArray = savedInstanceState.getSerializable("Auto") as ArrayList<Auto>
            viewModel.clearList()
            tempArray.forEach {
                viewModel.addAutoEnd(it)
            }
            Toast.makeText(this, "From saved", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "From create", Toast.LENGTH_LONG).show()
            if (DBHelper!!.isEmpty()) {
                println("DB is empty")
                var tempAutoArr = ArrayList<Auto>()
                viewModel.autoListFlow.value.forEach{
                    tempAutoArr.add(it)
                }
                DBHelper!!.addArrayToDB(tempAutoArr)
                DBHelper!!.printDB()
            } else {
                println("DB has records")
                DBHelper!!.printDB()
                val tempAutoArr = DBHelper!!.getAutoArray()
                viewModel.clearList()
                tempAutoArr.forEach({
                    println(it.picture)
                    viewModel.addAuto(it)
                })
            }
        }
        setContent {
            val lazyListState = rememberLazyListState()
            LabsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.secondaryContainer) {
                    Column(Modifier.fillMaxSize()) {
                        MakeAppBar(viewModel, lazyListState, DBHelper!!)
//                        Main(viewModel, lazyListState, DBHelper!!)
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
        var tempArray = ArrayList<Auto>()
        viewModel.autoListFlow.value.forEach{
            tempArray.add(it)
        }
        outState.putSerializable("Auto", tempArray)
        super.onSaveInstanceState(outState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeAppBar(viewModel: ItemViewModel, lazyListState: LazyListState, DBHelper: autoDBhelper) {

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
                DBHelper.addAuto(newAuto)
                scope.launch {
                    lazyListState.scrollToItem(0)
                }
            } else {
                Toast.makeText(context, "Exists", Toast.LENGTH_LONG).show()
            }
        }
    }

    if (openDialog.value) {MakeAlertDialog(context, "About", openDialog)}

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

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
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        if (drawerState.isClosed) drawerState.open()
                        else drawerState.close()
                    }
                }
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = ""
                )
            }
        }
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(10.dp))
                NavigationDrawerItem(
                    icon = {Icon(Icons.Default.Star, contentDescription = null)},
                    label = {Text("Drawing")},
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        val newInt =Intent(context, DrawingActivity::class.java)
                        context.startActivity(newInt)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        },
        content = {
            Column (
                modifier = Modifier.fillMaxSize().padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Main(viewModel, lazyListState, DBHelper)
            }
        }
    )
}

@Composable
fun Main(viewModel: ItemViewModel, lazyListState: LazyListState, DBHelper: autoDBhelper) {
    val context = LocalContext.current
    val autoListState = viewModel.autoListFlow.collectAsState()
    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize(),
        state = lazyListState
    ) {
        items(
            items = viewModel.autoListFlow.value,
            key = { auto -> auto.numberPlate } ,
            itemContent = { item ->
                ListRow(item, autoListState, viewModel, DBHelper)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListRow(item: Auto, autoListState: State<List<Auto>> ,viewModel: ItemViewModel, DBHelper: autoDBhelper) {
    val context = LocalContext.current
    val listCurrValue = remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val cringeDialog = remember { mutableStateOf(false) }
    var mDisplayMenu by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.data?.data != null) {
            println("image url = ${result.data?.data}")
            val imgUrl = result.data?.data
            val index = autoListState.value.indexOf(item)
            DBHelper!!.changeImg(item.numberPlate, imgUrl.toString())
            viewModel.changeImage(index, imgUrl.toString())
        }
    }

    if (openDialog.value) {
        MakeAlertDialog(context, listCurrValue.value, openDialog)
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
                    mDisplayMenu = true
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

        DropdownMenu(
            expanded = mDisplayMenu,
            onDismissRequest = {mDisplayMenu = false}
        ) {
            DropdownMenuItem(
                text =  { Text(text = "Change image", fontSize = 20.sp) },
                onClick = {
                    mDisplayMenu = !mDisplayMenu
                    val permission: String =  Manifest.permission.READ_EXTERNAL_STORAGE
                    val grant = ContextCompat.checkSelfPermission(context, permission)
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        val permission_list = arrayOfNulls<String>(1)
                        permission_list[0] = permission
                        ActivityCompat.requestPermissions(context as Activity, permission_list, 1)
                    }

                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                        addCategory(Intent.CATEGORY_OPENABLE) }
                    launcher.launch(intent)
                }
            )
        }

        Image(
            painter = if (pictureIsInt(item.picture)) painterResource(item.picture.toInt())
            else rememberImagePainter(item.picture),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(140.dp).clip(RoundedCornerShape(5.dp)),
            colorFilter = ColorFilter.tint(Color(149, 79, 65), blendMode = BlendMode.Hue)
        )
    }
}
fun pictureIsInt(picture: String) : Boolean {
    var data = try {
        picture.toInt()
    } catch (e: NumberFormatException) {
        null
    }
    return  data != null
}
