package ru.rtmrslnv.androidpractice.converters

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import javax.inject.Inject

@ProvidedTypeConverter
class ImageBitmapConverter @Inject constructor() {
    @TypeConverter
    fun fromByteArray(bytes: ByteArray?): ImageBitmap? {
        if (bytes == null) {
            return null
        }
        val bmp = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return null
        return bmp.asImageBitmap()
    }

    @TypeConverter
    fun toByteArray(image: ImageBitmap?): ByteArray? {
        if (image == null) {
            return null
        }
        val bmp = image.asAndroidBitmap()
        val stream = java.io.ByteArrayOutputStream()
        bmp.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}
