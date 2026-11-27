package app.kaiju.inventory.repository

import app.kaiju.inventory.data.model.LoginRequest
import app.kaiju.inventory.data.model.LoginResponse
import app.kaiju.inventory.data.remote.RetrofitClient
import kotlinx.coroutines.CancellationException

class AuthRepository {
    private val authService = RetrofitClient.authService

    suspend fun login(correo: String, password: String): Result<LoginResponse> {
        return try {
            val response = authService.login(LoginRequest(correo, password))
            Result.success(response)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
