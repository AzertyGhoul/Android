package com.example.labs

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class autoDBhelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_NAME = "Autos"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "Autos_Table"
        val ID_COL = "id"
        val NUMBER_PLATE_COL = "Number_Plate"
        val ENGINE_CAPACITY_COL = "Engine_Capacity"
        val BRAND_COL = "Brand"
        val PICTURE_COL = "Picture"
    }

    override fun onCreate(DB: SQLiteDatabase) {
        val query = (
                "CREATE TABLE " + TABLE_NAME + "(" +
                        ID_COL + " INTEGER PRIMARY KEY autoincrement, " +
                        NUMBER_PLATE_COL + " TEXT," +
                        ENGINE_CAPACITY_COL + " FLOAT," +
                        BRAND_COL + " TEXT," +
                        PICTURE_COL + " TEXT)")
        DB.execSQL(query)
    }

    override fun onUpgrade(DB: SQLiteDatabase, oldVerison: Int, newVersion: Int) {
        DB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(DB)
    }

    fun getCursor(): Cursor? {
        val DB = this.readableDatabase
        return DB.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }

    fun isEmpty() : Boolean {
        val  cursor = getCursor()
        return !cursor!!.moveToFirst()
    }
    fun printDB() {
        val cursor = getCursor()
        if (!isEmpty()) {
            cursor!!.moveToFirst()
            val numberColIndex = cursor.getColumnIndex(NUMBER_PLATE_COL)
            val engineCapacityIndex = cursor.getColumnIndex(ENGINE_CAPACITY_COL)
            val brandIndex = cursor.getColumnIndex(BRAND_COL)
            val pictureIndex = cursor.getColumnIndex(PICTURE_COL)

            do {
                print("${cursor.getString(numberColIndex)}")
                print("${cursor.getString(engineCapacityIndex)}")
                print("${cursor.getString(brandIndex)}")
                print("${cursor.getString(pictureIndex)}")
            } while (cursor.moveToNext())
        } else {
            println("DB is empty")
        }
    }

    fun addArrayToDB(auto: ArrayList<Auto>) {
        auto.forEach{
            addAuto(it)
        }
    }

    fun addAuto(item : Auto) {
        val values = ContentValues()
        values.put(NUMBER_PLATE_COL, item.numberPlate)
        values.put(ENGINE_CAPACITY_COL, item.engineСapacity)
        values.put(BRAND_COL, item.brand)
        values.put(PICTURE_COL, item.picture)
        val DB = this.writableDatabase
        DB.insert(TABLE_NAME,null, values)
        DB.close()
    }

    fun changeImg(numberPlate: String, img: String) {
        val DB = this.writableDatabase
        val values = ContentValues()
        values.put(PICTURE_COL, img)
        DB.update(TABLE_NAME, values, NUMBER_PLATE_COL + " = '$numberPlate'", null)
        DB.close()
    }

    fun  getAutoArray() : ArrayList<Auto> {
        var autoArray = ArrayList<Auto>()
        val cursor = getCursor()
        if (!isEmpty()) {
            cursor!!.moveToFirst()
            val numberColIndex = cursor.getColumnIndex(NUMBER_PLATE_COL)
            val engineCapacityIndex = cursor.getColumnIndex(ENGINE_CAPACITY_COL)
            val brandIndex = cursor.getColumnIndex(BRAND_COL)
            val pictureIndex = cursor.getColumnIndex(PICTURE_COL)
            do {
                val numberPlate = cursor.getString(numberColIndex)
                val engineCapacity = cursor.getString(engineCapacityIndex).toFloat()
                val brand = cursor.getString(brandIndex)
                val picture = cursor.getString(pictureIndex)
                autoArray.add(Auto(numberPlate, engineCapacity, brand, picture))
            } while (cursor.moveToNext())
        } else {
            println("DB is empty")
        }
        return autoArray
    }
}
