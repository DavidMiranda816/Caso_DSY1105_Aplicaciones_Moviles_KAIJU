package app.kaiju.inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kaiju.inventory.model.*
import app.kaiju.inventory.repository.InventarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovimientoViewModel(private val repository: InventarioRepository) : ViewModel() {

    val productos: StateFlow<List<Producto>> =
        repository.productos.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _estado = MutableStateFlow(FormularioMovimientoEstado())
    val estado: StateFlow<FormularioMovimientoEstado> = _estado.asStateFlow()

    fun seleccionarProducto(sku: String) =
        _estado.update { it.copy(codigoProducto = sku, errorProducto = null) }

    fun codigoLeido(codigo: String) {
        val sku = codigo.trim()
        viewModelScope.launch {
            val producto = repository.buscar(sku)?.takeIf { it.estadoProducto }
            _estado.update {
                if (producto != null) it.copy(codigoProducto = producto.sku, errorProducto = null, ingresoManual = false)
                else it.copy(errorProducto = "El código \"$sku\" no corresponde a ningún producto")
            }
        }
    }

    fun activarIngresoManualDeCodigo() = _estado.update { it.copy(ingresoManual = true) }
    fun actualizarTipo(t: String) = _estado.update { it.copy(tipo = t, errorMotivo = null) }

    fun ajustarTipoPermitido(permitidos: List<String>) {
        if (permitidos.isNotEmpty() && _estado.value.tipo !in permitidos) {
            _estado.update { it.copy(tipo = permitidos.first()) }
        }
    }

    fun actualizarCantidad(v: String) = _estado.update { it.copy(cantidad = v.filter(Char::isDigit), errorCantidad = null) }
    fun actualizarMotivo(v: String) = _estado.update { it.copy(motivo = v, errorMotivo = null) }
    fun actualizarObservacion(v: String) = _estado.update { it.copy(observacion = v) }

    fun validarFormulario(): Boolean {
        val e = _estado.value
        val cantidad = e.cantidad.toIntOrNull()
        val errorProducto = if (e.codigoProducto.isBlank()) "Selecciona o escanea un producto" else null
        val errorCantidad = when {
            cantidad == null -> "Ingresa una cantidad"
            cantidad == 0 && e.tipo != TipoMovimiento.AJUSTE -> "La cantidad debe ser mayor a 0"
            else -> null
        }
        val errorMotivo = if (e.tipo == TipoMovimiento.AJUSTE && e.motivo.isBlank()) "El ajuste requiere un motivo" else null
        _estado.update { it.copy(errorProducto = errorProducto, errorCantidad = errorCantidad, errorMotivo = errorMotivo) }
        return errorProducto == null && errorCantidad == null && errorMotivo == null
    }

    fun confirmarMovimiento(usuario: String, permitidos: List<String>) {
        if (_estado.value.tipo !in permitidos) {
            _estado.update { it.copy(errorCantidad = "Tu perfil no puede registrar este tipo de movimiento") }
            return
        }
        if (!validarFormulario()) return
        val e = _estado.value
        viewModelScope.launch {
            val error = repository.registrarMovimiento(
                e.codigoProducto, e.tipo, e.cantidad.toInt(), e.motivo.trim(), e.observacion.trim(), usuario
            )
            _estado.update {
                if (error == null) FormularioMovimientoEstado(tipo = it.tipo, registrado = true)
                else it.copy(errorCantidad = error)   // p. ej. "Stock insuficiente"
            }
        }
    }

    fun mensajeMostrado() = _estado.update { it.copy(registrado = false) }
    fun reiniciar() { _estado.value = FormularioMovimientoEstado() }
}