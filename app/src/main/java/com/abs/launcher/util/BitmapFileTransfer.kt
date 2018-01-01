package com.abs.launcher.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.abs.launcher.util.cache.FileTransfer
import java.io.FileOutputStream

/**
 * Created by zy on 17-12-18.
 */
class BitmapFileTransfer: FileTransfer<Bitmap> {

    override fun saveToFile(file: String, obj: Bitmap) {
        val fos = FileOutputStream(file)
        obj.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    }

    override fun decode(file: String): Bitmap? {
        return BitmapFactory.decodeFile(file)
    }

}