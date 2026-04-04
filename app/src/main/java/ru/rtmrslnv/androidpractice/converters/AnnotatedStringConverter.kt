package ru.rtmrslnv.androidpractice.converters

import androidx.compose.ui.text.AnnotatedString
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import javax.inject.Inject

@ProvidedTypeConverter
class AnnotatedStringConverter @Inject constructor() {
    @TypeConverter
    fun fromString(value: String?): AnnotatedString {
        return AnnotatedString(value ?: "")
    }

    @TypeConverter
    fun toString(value: AnnotatedString?): String {
        return value?.text ?: ""
    }
}