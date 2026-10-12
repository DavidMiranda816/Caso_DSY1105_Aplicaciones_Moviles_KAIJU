package app.kaiju.inventory.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning                 
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.kaiju.inventory.ui.screen.PantallaAlertas                  
import app.kaiju.inventory.ui.screen.PantallaCatalogo
import app.kaiju.inventory.ui.screen.PantallaFormularioProducto
import app.kaiju.inventory.ui.screen.PantallaLogin
import app.kaiju.inventory.viewmodel.KaijuViewModel
import app.kaiju.inventory.viewmodel.LoginViewModel

@Composable
fun AppNavHost(
    viewModel: KaijuViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    val usuarioActivo by loginViewModel.usuarioActivo.collectAsState()
    val puedeCrear = loginViewModel.puedeCrearProductos(usuarioActivo)
    val puedeVerAlertas = loginViewModel.puedeVerAlertas(usuarioActivo)    

    val mostrarBottomBar = rutaActual == Rutas.Inventario.ruta ||
            rutaActual == Rutas.Agregar.ruta ||
            rutaActual == Rutas.Alertas.ruta                               

    Scaffold(
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Inventario.ruta,
                        onClick = {
                            if (rutaActual != Rutas.Inventario.ruta) {
                                navController.navigate(Rutas.Inventario.ruta) {
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Lista de Productos") },
                        label = { Text("Inventario") }
                    )
                    if (puedeCrear) {
                        NavigationBarItem(
                            selected = rutaActual == Rutas.Agregar.ruta,
                            onClick = {
                                if (rutaActual != Rutas.Agregar.ruta) {
                                    navController.navigate(Rutas.Agregar.ruta) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            icon = { Icon(Icons.Filled.Add, contentDescription = "Agregar Producto") },
                            label = { Text("Agregar") }
                        )
                    }
                    if (puedeVerAlertas) {                                        
                        NavigationBarItem(
                            selected = rutaActual == Rutas.Alertas.ruta,
                            onClick = {
                                if (rutaActual != Rutas.Alertas.ruta) {
                                    navController.navigate(Rutas.Alertas.ruta) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            icon = { Icon(Icons.Filled.Warning, contentDescription = "Alertas de stock") },
                            label = { Text("Alertas") }
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
                PantallaLogin(
                    modeloVista = loginViewModel,
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
                    puedeAgregar = puedeCrear,
                    onIrAAgregar = { navController.navigate(Rutas.Agregar.ruta) }
                )
            }

            composable(Rutas.Agregar.ruta) {
                PantallaFormularioProducto(
                    viewModel = viewModel,
                    onProductoAgregado = {
                        navController.navigate(Rutas.Inventario.ruta) {
                            popUpTo(Rutas.Inventario.ruta) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.Alertas.ruta) {                                       
                PantallaAlertas(viewModel = viewModel)
            }
        }
    }
}