package app.kaiju.inventory.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.withTransaction
import app.kaiju.inventory.database.KaijuDatabase
import app.kaiju.inventory.repository.InventarioRepository

class KaijuViewModelFactory(contexto: Context) : ViewModelProvider.Factory {

    private val db = KaijuDatabase.obtenerInstancia(contexto)
    private val repository = InventarioRepository(
        productoDao = db.productoDao(),
        movimientoDao = db.movimientoDao(),
        transaccion = { bloque -> db.withTransaction { bloque() } }
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val vm: ViewModel = when {
            modelClass.isAssignableFrom(KaijuViewModel::class.java) -> KaijuViewModel(repository)
            modelClass.isAssignableFrom(MovimientoViewModel::class.java) -> MovimientoViewModel(repository)
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
        return vm as T
    }
}