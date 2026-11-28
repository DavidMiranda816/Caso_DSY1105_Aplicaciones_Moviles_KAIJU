package app.kaiju.inventory.model

enum class Rol(val etiqueta: String, val codigoBackend: String) {
    ADMINISTRADOR("Administrador", "ADMIN"),
    VENDEDOR("Vendedor", "VENDEDOR"),
    ENCARGADO_INVENTARIO("Encargado de inventario", "ENCARGADO")
}

data class Usuario(
    val id: Long,
    val correo: String,
    val nombre: String,
    val rol: Rol
)

fun rolDesdeBackend(rol: String): Rol? = Rol.entries.find { it.codigoBackend == rol }
