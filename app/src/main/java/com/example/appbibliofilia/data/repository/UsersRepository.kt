package com.example.appbibliofilia.data.repository

import android.content.Context
import org.json.JSONArray
import com.example.appbibliofilia.data.model.UserRecord

class UsersRepository(private val context: Context, private val assetFileName: String = "users.json") {

    private fun loadAll(): List<UserRecord> {
        return try {
            val json = context.assets.open(assetFileName).bufferedReader().use { it.readText() }
            val arr = JSONArray(json)
            val list = mutableListOf<UserRecord>()
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val username = o.optString("username", "")
                val password = o.optString("password", "")
                val name = o.optString("name", "")
                val email = o.optString("email", "")
                list.add(
                    UserRecord(
                        username = username,
                        password = password,
                        name = name.takeIf { it.isNotBlank() },
                        email = email.takeIf { it.isNotBlank() }
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun findUser(username: String, password: String): UserRecord? {
        return loadAll().firstOrNull { (it.username == username || it.email == username) && it.password == password }
    }
}
