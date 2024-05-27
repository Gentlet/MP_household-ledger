package com.example.appproject

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object JsonUtil {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    inline fun <reified T> readJsonFromAssets(context: Context, fileName: String): List<T> {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }

        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, JsonDateAdapter(dateFormat))
            .create()

        val listType = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(jsonString, listType)
    }
}
