package app.kaiju.inventory.model

enum class Rol(val etiqueta: String) {
    ADMINISTRADOR("Administrador"),
    VENDEDOR("Vendedor"),
    ENCARGADO_INVENTARIO("Encargado de inventario")
}

data class Usuario(
    val correo: String,
    val contrasena: String,
    val nombre: String,
    val rol: Rol
)