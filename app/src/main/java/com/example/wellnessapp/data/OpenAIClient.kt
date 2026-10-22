package com.example.wellnessapp.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object OpenAIClient {

    private var apiKey: String = ""

    fun loadKey(context: Context) {
        try {
            val input = context.assets.open("openai.key")
            apiKey = input.bufferedReader().readText().trim()
            println("API KEY LOADED: $apiKey")
        } catch (e: Exception) {
            println("FAILED TO LOAD KEY: ${e.localizedMessage}")
        }
    }

    suspend fun ask(prompt: String): String = withContext(Dispatchers.IO) {
        try {
            if (apiKey.isBlank())
                return@withContext "Error: API key is empty"

            val url = URL("https://api.openai.com/v1/chat/completions")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                setRequestProperty("Authorization", "Bearer $apiKey")
                setRequestProperty("Content-Type", "application/json")
            }

            val requestJson = """
                {
                    "model": "gpt-4o-mini",
                    "messages": [
                        {"role": "user", "content": "$prompt"}
                    ]
                }
            """.trimIndent()

            connection.outputStream.use { it.write(requestJson.toByteArray()) }

            val stream = if (connection.responseCode in 200..299)
                connection.inputStream
            else
                connection.errorStream

            val response = stream.bufferedReader().readText()
            println("RAW RESPONSE: $response")

            val json = JSONObject(response)

            if (json.has("error")) {
                val msg = json.getJSONObject("error").getString("message")
                return@withContext "Error: $msg"
            }

            return@withContext json
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")

        } catch (e: Exception) {
            return@withContext "Error: ${e.localizedMessage}"
        }
    }
}
