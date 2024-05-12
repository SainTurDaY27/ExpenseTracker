package org.classapp.expensetrackerapp.utilities

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

fun ReadJSONFromAssets(context: Context, path: String): String {
    val identifier = "[ReadJSON]"
    try {
        val file = context.assets.open(path)
        Log.i(
            identifier,
            "Successfully opened file $path"
        )
        val bufferedReader = BufferedReader(InputStreamReader(file))
        val stringBuilder = StringBuilder()
        bufferedReader.useLines { lines ->
            lines.forEach {
                stringBuilder.append(it)
            }
        }
        Log.i(
            identifier,
            "Successfully read file $path"
        )

        val jsonString = stringBuilder.toString()
        Log.i(
            identifier,
            "Successfully converted file $path to string"
        )
        return jsonString
    } catch (e: Exception) {
        Log.e(
            identifier,
            "Failed to read file $path"
        )
        e.printStackTrace()
        return ""
    }
}