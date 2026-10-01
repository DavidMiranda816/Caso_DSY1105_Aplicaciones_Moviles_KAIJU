package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import app.kaiju.inventory.model.ErroresProducto
import app.kaiju.inventory.model.FormularioProductoEstado
import app.kaiju.inventory.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class KaijuViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _estadoFormulario = MutableStateFlow(FormularioProductoEstado())
    val estadoFormulario: StateFlow<FormularioProductoEstado> = _estadoFormulario.asStateFlow()

    fun actualizarSku(sku: String) {
        _estadoFormulario.update { it.copy(sku = sku) }
        validarFormulario()
    }

    fun actualizarNombre(nombre: String) {
        _estadoFormulario.update { it.copy(nombreProducto = nombre) }
        validarFormulario()
    }

    fun actualizarDescripcion(descripcion: String) {
        _estadoFormulario.update { it.copy(descripcion = descripcion) }
    }

    fun actualizarMarca(marca: String) {
        _estadoFormulario.update { it.copy(marca = marca) }
    }

    fun actualizarPrecio(precio: String) {
        _estadoFormulario.update { it.copy(precio = precio) }
        validarFormulario()
    }

    fun actualizarCategoria(categoria: String) {
        _estadoFormulario.update { it.copy(categoria = categoria) }
    }

    fun actualizarStock(stock: String) {
        _estadoFormulario.update { it.copy(stock = stock) }
        validarFormulario()
    }

    private fun validarFormulario() {
        val estado = _estadoFormulario.value
        val errorSku = if (estado.sku.isBlank()) "El SKU es obligatorio" else null
        val errorNombre = if (estado.nombreProducto.isBlank()) "El nombre es obligatorio" else null
        val errorPrecio = when {
            estado.precio.isBlank() -> "El precio es obligatorio"
            estado.precio.toDoubleOrNull() == null -> "Ingresa un precio válido"
            else -> null
        }
        val errorStock = when {
            estado.stock.isBlank() -> "El stock es obligatorio"
            estado.stock.toIntOrNull() == null -> "Ingresa un número entero"
            else -> null
        }

        val errores = ErroresProducto(
            errorSku = errorSku,
            errorNombre = errorNombre,
            errorPrecio = errorPrecio,
            errorStock = errorStock
        )

        val esValido = errorSku == null && errorNombre == null && errorPrecio == null && errorStock == null

        _estadoFormulario.update { it.copy(errores = errores, esValido = esValido) }
    }

    fun guardarProducto(onSuccess: () -> Unit) {
        validarFormulario()
        val estado = _estadoFormulario.value

        if (estado.esValido) {
            val nuevoProducto = Producto(
                sku = estado.sku,
                nombreProducto = estado.nombreProducto,
                descripcion = estado.descripcion,
                marca = estado.marca,
                precio = estado.precio.toDoubleOrNull() ?: 0.0,
                categoria = estado.categoria,
                stock = estado.stock.toIntOrNull() ?: 0,
                estadoProducto = estado.estadoProducto
            )

            _productos.update { it + nuevoProducto }
            _estadoFormulario.value = FormularioProductoEstado()
            onSuccess()
        }
    }
}