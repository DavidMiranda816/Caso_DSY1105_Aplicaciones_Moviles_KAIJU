package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kaiju.inventory.model.*
import app.kaiju.inventory.repository.InventarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class KaijuViewModel(private val repository: InventarioRepository) : ViewModel() {

    val productos: StateFlow<List<Producto>> =
        repository.productos.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val categorias: StateFlow<List<String>> =
        productos.map { lista -> lista.map { it.categoria }.filter { it.isNotBlank() }.distinct().sorted() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _estadoFormulario = MutableStateFlow(FormularioProductoEstado())
    val estadoFormulario: StateFlow<FormularioProductoEstado> = _estadoFormulario.asStateFlow()

    init { viewModelScope.launch { repository.sembrarSiVacio() } }

    fun actualizarSku(v: String) = _estadoFormulario.update { it.copy(sku = v) }
    fun actualizarNombre(v: String) = _estadoFormulario.update { it.copy(nombreProducto = v) }
    fun actualizarDescripcion(v: String) = _estadoFormulario.update { it.copy(descripcion = v) }
    fun actualizarMarca(v: String) = _estadoFormulario.update { it.copy(marca = v) }
    fun actualizarPrecio(v: String) = _estadoFormulario.update { it.copy(precio = v) }
    fun actualizarCategoria(v: String) = _estadoFormulario.update { it.copy(categoria = v) }
    fun actualizarStock(v: String) = _estadoFormulario.update { it.copy(stock = v) }
    fun actualizarStockMinimo(v: String) = _estadoFormulario.update { it.copy(stockMinimo = v) }

    fun validarFormulario(): Boolean {
        val e = _estadoFormulario.value
        val sku = e.sku.trim()
        val precio = e.precio.toDoubleOrNull()
        val stock = e.stock.toIntOrNull()
        val stockMinimo = e.stockMinimo.toIntOrNull()

        val errorSku = when {
            sku.isBlank() -> "El SKU es obligatorio"
            !e.esEdicion && productos.value.any { it.sku.equals(sku, ignoreCase = true) } -> "Ya existe un producto con ese SKU"
            else -> null
        }
        val errorNombre = when {
            e.nombreProducto.isBlank() -> "El nombre es obligatorio"
            e.nombreProducto.trim().length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
        val errorCategoria = if (e.categoria.isBlank()) "Elige una categoría" else null
        val errorPrecio = when {
            e.precio.isBlank() -> "El precio es obligatorio"
            precio == null || precio <= 0 -> "El precio debe ser un número mayor que 0"
            else -> null
        }
        val errorStock = when {
            e.stock.isBlank() -> "El stock es obligatorio"
            stock == null || stock < 0 -> "Ingresa un número entero (0 o más)"
            else -> null
        }
        val errorStockMinimo = when {
            e.stockMinimo.isBlank() -> "El stock mínimo es obligatorio"
            stockMinimo == null || stockMinimo < 0 -> "Ingresa un número entero (0 o más)"
            else -> null
        }
        val valido = listOf(errorSku, errorNombre, errorCategoria, errorPrecio, errorStock, errorStockMinimo).all { it == null }
        _estadoFormulario.update {
            it.copy(
                errores = ErroresProducto(errorSku, errorNombre, errorCategoria, errorPrecio, errorStock, errorStockMinimo),
                esValido = valido
            )
        }
        return valido
    }

    fun limpiarFormulario() { _estadoFormulario.value = FormularioProductoEstado() }

    fun cargarParaEditar(sku: String) {
        viewModelScope.launch {
            val p = repository.buscar(sku) ?: return@launch
            _estadoFormulario.value = FormularioProductoEstado(
                sku = p.sku, nombreProducto = p.nombreProducto, descripcion = p.descripcion, marca = p.marca,
                precio = if (p.precio % 1.0 == 0.0) p.precio.toLong().toString() else p.precio.toString(),
                categoria = p.categoria, stock = p.stock.toString(),
                stockMinimo = p.stockMinimo.toString(), esEdicion = true
            )
        }
    }

    fun guardarProducto(onSuccess: () -> Unit) {
        if (!validarFormulario()) return
        val e = _estadoFormulario.value
        val producto = Producto(
            sku = e.sku.trim(), nombreProducto = e.nombreProducto.trim(),
            descripcion = e.descripcion.trim(), marca = e.marca.trim(),
            precio = e.precio.toDouble(), categoria = e.categoria.trim(),
            stock = e.stock.toInt(), estadoProducto = true,
            stockMinimo = e.stockMinimo.toInt()
        )
        viewModelScope.launch {
            val error = if (e.esEdicion) { repository.actualizar(producto); null } else repository.crear(producto)
            if (error == null) {
                limpiarFormulario()
                onSuccess()
            } else {
                _estadoFormulario.update { it.copy(errores = it.errores.copy(errorSku = error), esValido = false) }
            }
        }
    }

    fun eliminarProducto(sku: String) { viewModelScope.launch { repository.eliminar(sku) } }

    fun movimientosDeProducto(sku: String): Flow<List<Movimiento>> = repository.movimientosDe(sku)

    fun estaEnAlerta(producto: Producto): Boolean =
        producto.stock <= producto.stockMinimo
}