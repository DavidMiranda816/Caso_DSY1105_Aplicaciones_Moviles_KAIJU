package app.kaiju.inventory.navegation

sealed class Rutas(val ruta: String){
    object Inventario : Rutas("inventario")
    object Agregar : Rutas("agregar_producto")
}