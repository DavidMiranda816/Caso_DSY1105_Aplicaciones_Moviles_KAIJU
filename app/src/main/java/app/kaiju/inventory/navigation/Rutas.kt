package app.kaiju.inventory.navigation

sealed class Rutas(val ruta: String){
    object Login : Rutas("login")
    object Inventario : Rutas("inventario")
    object Agregar : Rutas("agregar_producto")
    object Alertas : Rutas("alertas")
}