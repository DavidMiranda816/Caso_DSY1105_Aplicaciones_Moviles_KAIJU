package app.kaiju.inventory.data.model

data class LoginRequest(
    val correo: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val tipo: String?,
    val usuario: UsuarioResponse?
)

data class UsuarioRequest(
    val nombre: String,
    val correo: String,
    val password: String
)

data class UsuarioResponse(
    val id: Long,
    val nombre: String,
    val correo: String,
    val rol: String,
    val activo: Boolean,
    val fechaCreacion: String?
)

data class UsuarioUpdateRequest(
    val nombre: String,
    val correo: String
)
