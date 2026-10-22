package app.kaiju.inventory.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.kaiju.inventory.ui.screen.*
import app.kaiju.inventory.viewmodel.KaijuViewModel
import app.kaiju.inventory.viewmodel.KaijuViewModelFactory
import app.kaiju.inventory.viewmodel.LoginViewModel
import app.kaiju.inventory.viewmodel.MovimientoViewModel
import app.kaiju.inventory.viewmodel.SesionViewModel

private data class ItemNav(
    val ruta: String,
    val destino: String,
    val icono: ImageVector,
    val etiqueta: String
)

@Composable
fun AppNavHost() {
    val contexto = LocalContext.current
    val factory = remember { KaijuViewModelFactory(contexto) }
    val viewModel: KaijuViewModel = viewModel(factory = factory)
    val movimientoViewModel: MovimientoViewModel = viewModel(factory = factory)
    val sesionViewModel: SesionViewModel = viewModel()

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    val usuarioActivo by sesionViewModel.usuarioActivo.collectAsState()
    val puedeGestionar = sesionViewModel.puedeGestionarProductos(usuarioActivo)
    val puedeVerAlertas = sesionViewModel.puedeVerAlertas(usuarioActivo)
    val tiposPermitidos = sesionViewModel.tiposMovimientoPermitidos(usuarioActivo)

    val cerrarSesion: () -> Unit = {
        sesionViewModel.cerrarSesion()
        movimientoViewModel.reiniciar()
        viewModel.limpiarFormulario()
        navController.navigate(Rutas.Login.ruta) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    val items = buildList {
        add(ItemNav(Rutas.Inventario.ruta, Rutas.Inventario.ruta, Icons.AutoMirrored.Filled.List, "Inventario"))
        if (puedeGestionar) {
            add(ItemNav(Rutas.Agregar.ruta, Rutas.Agregar.conSku(), Icons.Filled.Add, "Agregar"))
        }
        if (tiposPermitidos.isNotEmpty()) {
            add(ItemNav(Rutas.Escaner.ruta, Rutas.Escaner.ruta, Icons.Filled.QrCodeScanner, "Escanear"))
        }
        if (puedeVerAlertas) {
            add(ItemNav(Rutas.Alertas.ruta, Rutas.Alertas.ruta, Icons.Filled.Warning, "Alertas"))
        }
    }
    val mostrarBottomBar = items.any { it.ruta == rutaActual }

    Scaffold(
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = rutaActual == item.ruta,
                            onClick = {
                                if (rutaActual != item.ruta) {
                                    navController.navigate(item.destino) { launchSingleTop = true }
                                }
                            },
                            icon = { Icon(item.icono, contentDescription = item.etiqueta) },
                            label = { Text(item.etiqueta) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.Login.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.Login.ruta) {
                val loginViewModel: LoginViewModel = viewModel()
                PantallaLogin(
                    modeloVista = loginViewModel,
                    validarCredenciales = { correo, clave -> sesionViewModel.iniciarSesion(correo, clave) },
                    alIngresarExitosamente = {
                        navController.navigate(Rutas.Inventario.ruta) {
                            popUpTo(Rutas.Login.ruta) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.Inventario.ruta) {
                PantallaCatalogo(
                    viewModel = viewModel,
                    usuario = usuarioActivo,
                    puedeGestionar = puedeGestionar,
                    puedeRegistrarMovimientos = tiposPermitidos.isNotEmpty(),
                    onIrAAgregar = { navController.navigate(Rutas.Agregar.conSku()) },
                    onIrAEditar = { sku -> navController.navigate(Rutas.Agregar.conSku(sku)) },
                    onIrAMovimiento = { navController.navigate(Rutas.Movimiento.ruta) },
                    onCerrarSesion = cerrarSesion
                )
            }

            composable(
                route = Rutas.Agregar.ruta,
                arguments = listOf(navArgument(ARG_SKU) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { entry ->
                PantallaFormularioProducto(
                    viewModel = viewModel,
                    skuEditar = entry.arguments?.getString(ARG_SKU),
                    onTerminado = {
                        navController.navigate(Rutas.Inventario.ruta) {
                            popUpTo(Rutas.Inventario.ruta) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.Alertas.ruta) {
                PantallaAlertas(viewModel = viewModel)
            }

            composable(Rutas.Escaner.ruta) {
                PantallaEscaner(
                    onCodigoLeido = { codigo ->
                        movimientoViewModel.codigoLeido(codigo)       // el ViewModel busca el producto
                        navController.navigate(Rutas.Movimiento.ruta) {
                            popUpTo(Rutas.Escaner.ruta) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onIngresarManual = {                               // alternativa si niega la cámara
                        movimientoViewModel.activarIngresoManualDeCodigo()
                        navController.navigate(Rutas.Movimiento.ruta) {
                            popUpTo(Rutas.Escaner.ruta) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Rutas.Movimiento.ruta) {
                PantallaMovimiento(
                    viewModel = movimientoViewModel,
                    usuarioActivo = usuarioActivo?.correo ?: "invitado",
                    tiposPermitidos = tiposPermitidos,
                    onEscanear = { navController.navigate(Rutas.Escaner.ruta) },
                    onVolver = {
                        movimientoViewModel.reiniciar()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}