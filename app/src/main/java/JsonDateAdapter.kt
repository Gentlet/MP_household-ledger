package com.example.appproject

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.text.DateFormat
import java.text.ParseException
import java.util.Date

class JsonDateAdapter(private val dateFormat: DateFormat) : TypeAdapter<Date>() {
    @Synchronized
    override fun write(out: JsonWriter, value: Date?) {
        out.value(value?.let { dateFormat.format(it) })
    }

    @Synchronized
    override fun read(input: JsonReader): Date? {
        return try {
            dateFormat.parse(input.nextString())
        } catch (e: ParseException) {
            null
        }
    }
}
