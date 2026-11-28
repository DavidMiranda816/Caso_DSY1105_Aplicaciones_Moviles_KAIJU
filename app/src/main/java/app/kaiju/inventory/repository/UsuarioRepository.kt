package app.kaiju.inventory.repository

import app.kaiju.inventory.data.model.UsuarioAltaRequest
import app.kaiju.inventory.data.model.UsuarioResponse
import app.kaiju.inventory.data.model.UsuarioUpdateRequest
import app.kaiju.inventory.data.remote.RetrofitClient
import app.kaiju.inventory.model.Rol
import app.kaiju.inventory.model.rolDesdeBackend
import kotlinx.coroutines.CancellationException

class UsuarioRepository {
    private val usuarioService = RetrofitClient.usuarioService

    suspend fun listar(token: String): Result<List<UsuarioResponse>> = ejecutar {
        usuarioService.listarUsuarios("Bearer $token")
            .filter { it.activo && rolDesdeBackend(it.rol) != null }
    }

    suspend fun crear(
        token: String,
        nombre: String,
        correo: String,
        password: String,
        rol: Rol
    ): Result<UsuarioResponse> = ejecutar {
        usuarioService.crearUsuario(
            "Bearer $token",
            UsuarioAltaRequest(nombre, correo, password, rol.codigoBackend)
        )
    }

    suspend fun editar(token: String, id: Long, nombre: String, correo: String): Result<UsuarioResponse> =
        ejecutar {
            usuarioService.actualizarUsuario("Bearer $token", id, UsuarioUpdateRequest(nombre, correo))
        }

    suspend fun eliminar(token: String, id: Long): Result<Unit> = ejecutar {
        usuarioService.desactivarUsuario("Bearer $token", id)
    }

    private inline fun <T> ejecutar(operacion: () -> T): Result<T> {
        return try {
            Result.success(operacion())
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
