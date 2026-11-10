package app.kaiju.inventory.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import app.kaiju.inventory.repository.PokemonRepository
import app.kaiju.inventory.data.remote.PokeApiClient
import app.kaiju.inventory.viewmodel.PokedexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEasterEgg(onVolver: () -> Unit) {
    val viewModel: PokedexViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return PokedexViewModel(PokemonRepository(PokeApiClient.api)) as T
            }
        }
    )
    val estado by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.buscarPokemon("pikachu")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("¡Easter Egg!") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (estado.cargando) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Buscando en la PokéAPI...")
            } else if (estado.error != null) {
                Text(estado.error!!, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.buscarPokemon("pikachu") }) {
                    Text("Reintentar")
                }
            } else {
                estado.pokemon?.let { p ->
                    Text("¡Has encontrado a ${p.name.uppercase()}!", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))

                    AsyncImage(
                        model = p.sprites.frontDefault,
                        contentDescription = p.name,
                        modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("ID: #${p.id}")
                    Text("Altura: ${p.height / 10.0} m")
                    Text("Peso: ${p.weight / 10.0} kg")
                    Text("Tipo: ${p.types.firstOrNull()?.type?.name ?: "Desconocido"}")
                }
            }
        }
    }
}