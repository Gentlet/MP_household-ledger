package com.example.appproject

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale

object JsonUtil {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    inline fun readJsonFromAssets(context: Context, fileName: String): MutableList<TransactionItem> {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return ArrayList()
        }

        Log.d("LWH READ", jsonString)

        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, JsonDateAdapter(dateFormat))
            .create()

        val listType = object : TypeToken<List<TransactionItem>>() {}.type
        return gson.fromJson(jsonString, listType)
    }

    inline fun readJsonFromFile(context: Context, fileName: String): MutableList<TransactionItem> {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) {
            return readJsonFromAssets(context, fileName)
        }

        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, JsonDateAdapter(dateFormat))
            .create()

        val listType = object : TypeToken<List<TransactionItem>>() {}.type
        return FileReader(file).use { reader -> gson.fromJson(reader, listType) }
    }

    inline fun writeJsonToFile(context: Context, data: List<TransactionItem>, fileName: String) {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Date::class.java, JsonDateAdapter(dateFormat))
            .create()

        val jsonString = gson.toJson(data)
        Log.d("LWH WRITE", jsonString)
        val file = File(context.filesDir, fileName)
        try {
            FileWriter(file).use { writer -> writer.write(jsonString) }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    inline fun filterTransactionsByDate(transactions: List<TransactionItem>, date: Date, d:Int = -1): MutableList<TransactionItem> {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val targetYear = calendar.get(Calendar.YEAR)
        val targetMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based

        return transactions.filter { transaction ->
            calendar.time = transaction.date
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            year == targetYear && month == targetMonth && (day == d || d == -1) && transaction.amount > 0
        }.toMutableList()
    }

    inline fun filterTransactions(transactions: List<TransactionItem>, card: String = "", type: String = ""): MutableList<TransactionItem> {
        return transactions.filter { transaction ->
            (card == "" || transaction.card == card) && (transaction.type == type || type == "") && transaction.amount > 0
        }.toMutableList()
    }
}
