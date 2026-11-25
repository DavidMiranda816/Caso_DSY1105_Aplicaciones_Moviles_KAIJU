package app.kaiju.inventory.repository

import app.kaiju.inventory.data.model.LoginRequest
import app.kaiju.inventory.data.model.LoginResponse
import app.kaiju.inventory.data.model.UsuarioRequest
import app.kaiju.inventory.data.model.UsuarioResponse
import app.kaiju.inventory.data.remote.RetrofitClient

class AuthRepository {
    private val authService = RetrofitClient.authService

    suspend fun login(correo: String, password: String): Result<LoginResponse> {
        return try {
            val response = authService.login(LoginRequest(correo, password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registrar(nombre: String, correo: String, password: String): Result<UsuarioResponse> {
        return try {
            val response = authService.registrar(UsuarioRequest(nombre, correo, password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
