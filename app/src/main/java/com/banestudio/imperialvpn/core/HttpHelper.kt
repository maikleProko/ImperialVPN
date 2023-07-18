package com.banestudio.imperialvpn.core
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*

object HttpHelper {
    suspend fun getStringREST(url: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val client = HttpClient()
                val response: HttpResponse = client.get(url)
                client.close()
                response.bodyAsText()
            } catch (e: Exception) {
                null
            }
        }
    }

}