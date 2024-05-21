package com.example.labs

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.Serializable

data class Auto(val numberPlate: String, val engineСapacity: Float, val brand: String, val picture: Int = R.drawable.no_picture):Serializable

class ItemViewModel : ViewModel() {
    private var autoList = mutableStateListOf(
        Auto("YZA6789", 2.4f, "Mazda", R.drawable.mazda),
        Auto("ABC1234", 1.6f, "Toyota", R.drawable.toyota),
        Auto("XYZ5678", 2.0f, "Honda", R.drawable.honda),
        Auto("DEF9101", 2.5f, "Ford", R.drawable.ford),
        Auto("GHI2345", 3.0f, "Chevrolet", R.drawable.chevrolet),
        Auto("JKL6789", 4.0f, "BMW", R.drawable.bmw),
        Auto("MNO1234", 1.8f, "Mercedes", R.drawable.mersedes),
        Auto("PQR5678", 2.2f, "Audi", R.drawable.audi),
        Auto("STU9101", 2.8f, "Volkswagen", R.drawable.volkswagen),
        Auto("VWX2345", 3.5f, "Nissan", R.drawable.nissan),
    )

    private val autoListStateFlow = MutableStateFlow(autoList)
    val  autoListFlow: StateFlow<List<Auto>> get() = autoListStateFlow

    fun clearList(){
        autoList.clear()
    }

    fun addAuto(item: Auto) {
        autoList.add(0, item)
    }

    fun removeAuto(item: Auto) {
        autoList.remove(autoList[autoList.indexOf(item)])
    }
}