package app.kaiju.inventory.navegation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.SwapHoriz
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
import app.kaiju.inventory.model.TipoMovimiento
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

    val items = listOf(
        ItemNav(Rutas.Inventario.ruta, Rutas.Inventario.ruta, Icons.AutoMirrored.Filled.List, "Inventario"),
        ItemNav(Rutas.Movimiento.ruta, Rutas.Movimiento.ruta, Icons.Filled.SwapHoriz, "Movimiento"),
        ItemNav(Rutas.Escaner.ruta, Rutas.Escaner.ruta, Icons.Filled.QrCodeScanner, "Escanear"),
        ItemNav(Rutas.Alertas.ruta, Rutas.Alertas.ruta, Icons.Filled.Warning, "Alertas")
    )
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
                PantallaListaProducto(
                    viewModel = viewModel,
                    onIrAAgregar = { navController.navigate(Rutas.Agregar.conSku()) },
                    onIrAEditar = { sku -> navController.navigate(Rutas.Agregar.conSku(sku)) },
                    onIrADetalle = { sku -> navController.navigate(Rutas.DetalleProducto.conSku(sku)) }
                )
            }

            composable(
                route = Rutas.DetalleProducto.ruta,
                arguments = listOf(navArgument(ARG_SKU) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { entry ->
                val sku = entry.arguments?.getString(ARG_SKU) ?: ""
                PantallaDetalleProducto(
                    sku = sku,
                    viewModel = viewModel,
                    onVolver = { navController.popBackStack() },
                    onIrAEditar = { s -> navController.navigate(Rutas.Agregar.conSku(s)) },
                    onIrAMovimiento = { s ->
                        movimientoViewModel.seleccionarProducto(s)
                        navController.navigate(Rutas.Movimiento.ruta)
                    }
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
                PantallaAgregarProducto(
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
                        movimientoViewModel.codigoLeido(codigo)
                        navController.navigate(Rutas.Movimiento.ruta) {
                            popUpTo(Rutas.Escaner.ruta) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onIngresarManual = {
                        movimientoViewModel.activarIngresoManualDeCodigo()
                        navController.navigate(Rutas.Movimiento.ruta) {
                            popUpTo(Rutas.Escaner.ruta) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Rutas.Movimiento.ruta) {
                val usuarioActivo by sesionViewModel.usuarioActivo.collectAsState()
                PantallaMovimiento(
                    viewModel = movimientoViewModel,
                    usuarioActivo = usuarioActivo?.correo ?: "invitado",
                    tiposPermitidos = TipoMovimiento.todos,
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
