package com.example.labs

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.Serializable

data class Auto(val numberPlate: String, val engineСapacity: Float, val brand: String, val picture: String = R.drawable.no_picture.toString()) :Serializable

class ItemViewModel : ViewModel() {
    private var autoList = mutableStateListOf(
        Auto("YZA6789", 2.4f, "Mazda", R.drawable.mazda.toString()),
        Auto("ABC1234", 1.6f, "Toyota", R.drawable.toyota.toString()),
        Auto("XYZ5678", 2.0f, "Honda", R.drawable.honda.toString()),
        Auto("DEF9101", 2.5f, "Ford", R.drawable.ford.toString()),
        Auto("GHI2345", 3.0f, "Chevrolet", R.drawable.chevrolet.toString()),
        Auto("JKL6789", 4.0f, "BMW", R.drawable.bmw.toString()),
        Auto("MNO1234", 1.8f, "Mercedes", R.drawable.mersedes.toString()),
        Auto("PQR5678", 2.2f, "Audi", R.drawable.audi.toString()),
        Auto("STU9101", 2.8f, "Volkswagen", R.drawable.volkswagen.toString()),
        Auto("VWX2345", 3.5f, "Nissan", R.drawable.nissan.toString()),
    )

    private val autoListStateFlow = MutableStateFlow(autoList)
    val  autoListFlow: StateFlow<List<Auto>> get() = autoListStateFlow

    fun clearList(){
        autoList.clear()
    }

    fun addAuto(item: Auto) {
        autoList.add(0, item)
    }

    fun addAutoEnd(item: Auto) {
        autoList.add(autoList.count(), item)
    }

    fun removeAuto(item: Auto) {
        autoList.remove(autoList[autoList.indexOf(item)])
    }

    fun changeImage(index: Int, value: String) {
        autoList[index] = autoList[index].copy(picture = value)
    }
}