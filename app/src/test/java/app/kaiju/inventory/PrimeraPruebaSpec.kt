package app.kaiju.inventory

import app.kaiju.inventory.model.TipoMovimiento
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class PrimeraPruebaSpec : FunSpec({

    test("TipoMovimiento.esSalida debe identificar correctamente las salidas por venta y consumo") {
        TipoMovimiento.esSalida(TipoMovimiento.SALIDA_VENTA).shouldBeTrue()
        TipoMovimiento.esSalida(TipoMovimiento.SALIDA_CONSUMO).shouldBeTrue()
        TipoMovimiento.esSalida(TipoMovimiento.ENTRADA).shouldBeFalse()
        TipoMovimiento.esSalida(TipoMovimiento.AJUSTE).shouldBeFalse()
        TipoMovimiento.etiqueta(TipoMovimiento.ENTRADA) shouldBe "Entrada"
    }
})
