package app.kaiju.inventory.data.remote

import app.kaiju.inventory.data.model.UsuarioAltaRequest
import app.kaiju.inventory.data.model.UsuarioResponse
import app.kaiju.inventory.data.model.UsuarioUpdateRequest
import retrofit2.http.*

interface UsuarioApiService {
    @POST("api/usuarios")
    suspend fun crearUsuario(
        @Header("Authorization") token: String,
        @Body request: UsuarioAltaRequest
    ): UsuarioResponse

    @GET("api/usuarios")
    suspend fun listarUsuarios(@Header("Authorization") token: String): List<UsuarioResponse>

    @PUT("api/usuarios/{id}")
    suspend fun actualizarUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: UsuarioUpdateRequest
    ): UsuarioResponse

    @PATCH("api/usuarios/{id}/desactivar")
    suspend fun desactivarUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    )
}
