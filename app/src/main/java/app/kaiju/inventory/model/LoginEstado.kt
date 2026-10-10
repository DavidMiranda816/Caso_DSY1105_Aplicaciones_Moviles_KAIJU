package app.kaiju.inventory.model

data class LoginEstado (
    val correo: String = "",
    val contrasena: String = "",
    val inicioCompletado: Boolean = false
)

data class ErrorLogin(
    val mensajeError: String? = null
)