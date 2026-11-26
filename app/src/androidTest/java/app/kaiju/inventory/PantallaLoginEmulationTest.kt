package app.kaiju.inventory

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.kaiju.inventory.ui.screen.PantallaLogin
import app.kaiju.inventory.ui.theme.KaijuTheme
import app.kaiju.inventory.viewmodel.LoginViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PantallaLoginEmulationTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loginConCamposVaciosMuestraMensajeDeError() {
        composeRule.setContent {
            KaijuTheme {
                PantallaLogin(
                    modeloVista = LoginViewModel(),
                    alAutenticar = {},
                    alIngresarExitosamente = {},
                    onIrARegistro = {},
                    onEasterEggEncontrado = {}
                )
            }
        }

        composeRule.onNodeWithText("Iniciar Sesión").performClick()

        composeRule.onNodeWithText("Por favor, completa todos los campos").assertIsDisplayed()
    }
}
