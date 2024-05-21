package com.example.lab2_1

import android.os.Bundle
import android.view.Display.Mode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab2_1.ui.theme.Lab2_1Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel = ItemViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lazyListState = rememberLazyListState()
            Lab2_1Theme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(Modifier.fillMaxSize()) { //создаем колонку
                        MakeInputPart(viewModel, lazyListState)//вызываем ф-ию для создания полей ввода данных
                        MakeList(viewModel, lazyListState) //вызываем ф-ию для самого списка с данными
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeInputPart(model: ItemViewModel, lazyListState: LazyListState) {
    var langName by remember { //объект для работы с текстом, для названия языка
        mutableStateOf("") //его начальное значение
    }//в функцию mutableStateOf() в качестве параметра передается отслеживаемое значение

    var langYear by remember { //объект для работы с текстом, для года создания языка
        mutableStateOf(0) //его начальное значение
    }

    var langPercent by remember {
        mutableStateOf(0)
    }

    val scope = rememberCoroutineScope() //объект для прокручивания списка при вставке нового эл-та
    Row( //ряд для расположения эл-ов
        verticalAlignment = Alignment.CenterVertically, //центруем по вертикали
        horizontalArrangement = Arrangement.spacedBy(10.dp), //и добавляем отступы между эл-ми
    ) {
        TextField( //текстовое поле для ввода имени языка
            value = langName, //связываем текст из поля с созданным ранее объектом
            onValueChange = { newText -> //обработчик ввода значений в поле
                langName = newText //все изменения сохраняем в наш объект
            },
            textStyle = TextStyle( //объект для изменения стиля текста
                fontSize = 20.sp //увеличиваем шрифт
            ),
            label = { Text("Название") }, //это надпись в текстовом поле
            modifier = Modifier.weight(2f)//это вес колонки.Нужен для распределения долей в ряду.
//Контейнер Row позволяет назначить вложенным компонентам ширину в соответствии с их весом.
//Поэтому полям с данными назначаем вес 2, кнопке вес 1, получается сумма
// всех весов будет 5, и для полей с весом 2 будет выделяться по 2/5 от всей ширины ряда, для
//кнопки с весом 1 будет выделяться 1/5 от всей ширины ряда
        )
        TextField(
            value = langPercent.toString(),
            onValueChange = {
                langPercent = it.toInt()
            },
            textStyle = TextStyle(
                fontSize = 20.sp
            ),
            label = { Text("Популярность") },
            modifier = Modifier.weight(2f)
        )

        TextField( //текстовое поле для ввода года создания языка
            value = langYear.toString(), //связываем текст из поля с созданным ранее объектом
            onValueChange = { newText -> //обработчик ввода значений в поле
//т.к. newText (измененный текст) – это строка, а langYear – целое, то нужно преобразовывать
                langYear = if (newText != "") newText.toInt() else 0 //в нужный формат
            }, //с учетом возможной пустой строки
            textStyle = TextStyle( //объект для изменения стиля текста
                fontSize = 20.sp //увеличиваем шрифт
            ),
//и меняем тип допустимых символов для ввода – только цифры
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Год создания") },
            modifier = Modifier.weight(2f) //назначаем вес поля
        )
        Button( //кнопка для добавления нового языка
            onClick = { //при нажатии кнопки делаем отладочный вывод
                println("added $langName $langYear")
//и добавляем в начало списка новый язык с нужными параметрами

                model.addLangToHead(ProgrLang(langName, langPercent.toFloat() ,langYear))
                scope.launch {//прокручиваем список, чтобы был виден добавленный элемент

                    lazyListState.scrollToItem(0)
                }
                langName = "" //и очищаем поля
                langYear = 0
            },
            modifier = Modifier.weight(1f) //назначаем вес кнопки
        ) {
            Text("Add") //надпись для кнопки
        }
    }
}

@Composable
fun MakeList(viewModel: ItemViewModel, lazyListState: LazyListState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        state = lazyListState //состояние списка соотносим с переданным объектом
    ) {
        items(
            items = viewModel.langListFlow.value,
            key = { lang -> lang.name },
            itemContent = { item ->
                ListRow(item)
            }
        )
    }
}

@Composable
fun ListRow(item: ProgrLang){ //ф-ия для создания ряда с данными для LazyColumn
    Row( //создаем ряд с данными
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .border(BorderStroke(2.dp, Color.Blue)) //синяя граница для каждого эл-та списка
    ) {
        Text( // поле с текстом для названия языка
            text = item.name, //берем имя языка
            fontSize = 24.sp, //устанавливаем размер шрифта
            fontWeight = FontWeight.SemiBold, //делаем текст жирным
//и добавляем отступ слева
            modifier = Modifier.padding(start = 20.dp),
            color = Color.Black
        )
        Text(
            text = item.percent.toString() + "%",
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp),
            color = Color.Black
        )
        Text( // поле с текстом для года создания языка
            text = item.year.toString(), //берем год и преобразуем в строку
            fontSize = 20.sp, //устанавливаем размер шрифта
            modifier = Modifier.padding(10.dp), //добавляем отступ
            color = Color.Black,
            fontStyle = FontStyle.Italic //и делаем шрифт курсивом
        )
    }
}