package com.example.appbibliofilia.data.local

import android.content.Context
import org.json.JSONObject
import java.io.IOException
import com.example.appbibliofilia.data.model.Session

class SessionRepository(private val context: Context, private val filename: String = "data.json") {

    fun saveData(session: Session) {
        try {
            val json = JSONObject()
            json.put("isLoggedIn", session.isLoggedIn)
            json.put("name", session.name)
            json.put("email", session.email)

            context.openFileOutput(filename, Context.MODE_PRIVATE).use { fos ->
                fos.write(json.toString().toByteArray())
            }
        } catch (_: IOException) {
            // opcional: loggear el error
        }
    }

    fun loadData(): Session {
        return try {
            val file = context.openFileInput(filename)
            val content = file.bufferedReader().use { it.readText() }
            if (content.isBlank()) return Session()
            val json = JSONObject(content)
            val isLoggedIn = json.optBoolean("isLoggedIn", false)
            val nameStr = json.optString("name", "")
            val name = nameStr.takeIf { it.isNotBlank() }
            val emailStr = json.optString("email", "")
            val email = emailStr.takeIf { it.isNotBlank() }
            Session(isLoggedIn = isLoggedIn, name = name, email = email)
        } catch (_: Exception) {
            Session()
        }
    }
}

