package app.kaiju.inventory.repository

import app.kaiju.inventory.data.model.UsuarioRequest
import app.kaiju.inventory.data.model.UsuarioResponse
import app.kaiju.inventory.data.model.UsuarioUpdateRequest
import app.kaiju.inventory.data.remote.RetrofitClient

class UsuarioRepository {
    private val authService = RetrofitClient.authService
    private val usuarioService = RetrofitClient.usuarioService

    suspend fun listar(token: String): Result<List<UsuarioResponse>> {
        return try {
            Result.success(usuarioService.listarUsuarios("Bearer $token"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crear(nombre: String, correo: String, password: String): Result<UsuarioResponse> {
        return try {
            Result.success(authService.registrar(UsuarioRequest(nombre, correo, password)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editar(token: String, id: Long, nombre: String, correo: String): Result<UsuarioResponse> {
        return try {
            Result.success(usuarioService.actualizarUsuario("Bearer $token", id, UsuarioUpdateRequest(nombre, correo)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminar(token: String, id: Long): Result<Unit> {
        return try {
            val response = usuarioService.desactivarUsuario("Bearer $token", id)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
