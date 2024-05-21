package com.example.lab2_1

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProgrLang(val name: String, val percent: Float , val year: Int)

class ItemViewModel : ViewModel() {
    private var langList = mutableStateListOf( //создаем список из языков программирования
        ProgrLang("Basic",20f,1964),
        ProgrLang("Pascal", 30f,1975),
        ProgrLang("C", 10f,1972),
        ProgrLang("C++", 100f,1983),
        ProgrLang("C#", 60f,2000),
        ProgrLang("Java", 90f,1995),
        ProgrLang("Python", 75f,1991),
        ProgrLang("JavaScript", 34f,1995),
        ProgrLang("Kotlin", 21f,2011)
    )
    //добавляем объект, который будет отвечать за изменения в созданном списке
    private val _langListFlow = MutableStateFlow(langList)
    //и геттер для него, который его возвращает
    val langListFlow: StateFlow<List<ProgrLang>> get() = _langListFlow
    fun clearList(){ //метод для очистки списка, понадобится в лаб.раб.No5
        langList.clear()
    }
    fun addLangToHead(lang: ProgrLang) { //метод для добавления нового языка в начало списка
        langList.add(0, lang)
    }
    fun addLangToEnd(lang: ProgrLang) { //метод для добавления нового языка в конец списка
        langList.add( lang)
    }
    fun removeItem(item: ProgrLang) { //метод для удаления элемента из списка
        langList.remove(langList[langList.indexOf(item)])
    }
}