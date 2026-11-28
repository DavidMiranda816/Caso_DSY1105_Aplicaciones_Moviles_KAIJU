package app.kaiju.inventory

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUiEmulationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testInteraccionVisualEnDispositivoFisico() {

        composeRule.onNodeWithText("Correo electrónico").performTextInput("admin@kaiju.app")
        composeRule.onNodeWithText("Contraseña").performTextInput("admin1234")

        composeRule.onNodeWithText("Iniciar Sesión").performClick()
    }
}
