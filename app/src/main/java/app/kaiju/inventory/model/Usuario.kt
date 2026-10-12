package app.kaiju.inventory.model

enum class Rol {
    ADMINISTRADOR,
    VENDEDOR,
    ENCARGADO_INVENTARIO
}

data class Usuario(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val rol: Rol
)