package com.mobiai.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object SaveUtils {
    private val TAG = SaveUtils::class.java.name

    private fun getTempDir(context: Context): File {
        val temp = File(context.filesDir, "manifier")
        if (!temp.exists() && temp.mkdirs()) {
        }
        return temp
    }

    fun getFileName(context: Context, fileName: String = ""): File {
        return File(getTempDir(context), fileName + System.currentTimeMillis() + ".jpg")
    }

    fun convertBitmapToFile(data : ByteArray, out: File) {
        var outputStream : OutputStream? = null
        try {
            outputStream = FileOutputStream(out)
            outputStream.write(data)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "convertBitmapToFile: " + e.message!!)
        }finally {
            outputStream?.close()
        }
    }


}