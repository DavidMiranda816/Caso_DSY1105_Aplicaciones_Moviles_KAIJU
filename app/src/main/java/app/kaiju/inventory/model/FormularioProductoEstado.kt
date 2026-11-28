package app.kaiju.inventory.model

data class FormularioProductoEstado(
    val sku: String = "",
    val nombreProducto: String = "",
    val descripcion: String = "",
    val marca: String = "",
    val precio: String = "",
    val categoria: String = "",
    val stock: String = "",
    val stockMinimo: String = "5",
    val estadoProducto: Boolean = true,
    val esEdicion: Boolean = false,
    val errores: ErroresProducto = ErroresProducto(),
    val esValido: Boolean = false
)

data class ErroresProducto(
    val errorSku: String? = null,
    val errorNombre: String? = null,
    val errorCategoria: String? = null,
    val errorPrecio: String? = null,
    val errorStock: String? = null,
    val errorStockMinimo: String? = null
)