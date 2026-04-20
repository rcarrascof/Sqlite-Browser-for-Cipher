package com.rcdev.sqlitebrowser.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

object FileManager {

    fun getPathFromUri(context: Context, uri: Uri): String? {
        // En Android moderno lo mejor es copiar el archivo a cache si no se puede obtener ruta directa
        // para asegurar que SQLCipher pueda abrirlo por ruta de archivo.
        return try {
            val returnCursor = context.contentResolver.query(uri, null, null, null, null)
            val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor?.moveToFirst()
            val fileName = returnCursor?.getString(nameIndex ?: 0)
            returnCursor?.close()

            val file = File(context.cacheDir, fileName ?: "temp_db")
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}
