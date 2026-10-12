package app.kaiju.inventory.repository

import app.kaiju.inventory.model.Producto

//datos ficticios.
object DatosDePrueba {

    //confirmar categorías en la entrevista.
    val categorias = listOf(
        "Polera manga corta",
        "Polera manga larga",
        "Polerón polo",
        "Polerón canguro",
        "Accesorios"
    )

    val productos = listOf(
        Producto(
            sku = "PMC-001", nombreProducto = "Polera Dragón Neón",
            descripcion = "Polera estampada, talla M, color negro",
            marca = "Marca propia", precio = 13000.0,
            categoria = "Polera manga corta",
            stock = 24, estadoProducto = true, stockMinimo = 6
        ),
        Producto(
            sku = "PMC-002", nombreProducto = "Polera Gato Samurái",
            descripcion = "Polera estampada, talla L, color blanco",
            marca = "Marca propia", precio = 13000.0,
            categoria = "Polera manga corta",
            stock = 3, estadoProducto = true, stockMinimo = 6 
        ),
        Producto(
            sku = "PMC-003", nombreProducto = "Polera Oversize Ola Roja",
            descripcion = "Polera oversize estampada, talla XL, color negro",
            marca = "Marca propia", precio = 16000.0,
            categoria = "Polera manga corta",
            stock = 12, estadoProducto = true, stockMinimo = 4
        ),
        Producto(
            sku = "PML-001", nombreProducto = "Polera ML Robot Retro",
            descripcion = "Polera manga larga estampada, talla S, color negro",
            marca = "Marca propia", precio = 15000.0,
            categoria = "Polera manga larga",
            stock = 9, estadoProducto = true, stockMinimo = 4
        ),
        Producto(
            sku = "PML-002", nombreProducto = "Polera ML Tigre Lunar",
            descripcion = "Polera manga larga estampada, talla M, color negro",
            marca = "Marca propia", precio = 15000.0,
            categoria = "Polera manga larga",
            stock = 2, estadoProducto = true, stockMinimo = 4 
        ),
        Producto(
            sku = "PPO-001", nombreProducto = "Polerón Polo Oni Sonriente",
            descripcion = "Polerón polo estampado, talla M, color negro",
            marca = "Marca propia", precio = 20000.0,
            categoria = "Polerón polo",
            stock = 5, estadoProducto = true, stockMinimo = 5 
        ),
        Producto(
            sku = "PCA-001", nombreProducto = "Polerón Canguro Luna Pixel",
            descripcion = "Polerón canguro estampado, talla L, color negro",
            marca = "Marca propia", precio = 25000.0,
            categoria = "Polerón canguro",
            stock = 14, estadoProducto = true, stockMinimo = 5
        ),
        Producto(
            sku = "PCA-002", nombreProducto = "Polerón Canguro Zorro de Fuego",
            descripcion = "Polerón canguro estampado, talla M, color negro",
            marca = "Marca propia", precio = 25000.0,
            categoria = "Polerón canguro",
            stock = 0, estadoProducto = true, stockMinimo = 3 
        ),
        Producto(
            sku = "ACC-001", nombreProducto = "Tote bag de prueba",
            descripcion = "Bolso de tela estampado",
            marca = "Marca propia", precio = 8000.0,
            categoria = "Accesorios",
            stock = 30, estadoProducto = true, stockMinimo = 8
        ),
        Producto(
            sku = "ACC-002", nombreProducto = "Parche bordado de prueba",
            descripcion = "Parche para ropa",
            marca = "Marca propia", precio = 3000.0,
            categoria = "Accesorios",
            stock = 45, estadoProducto = true, stockMinimo = 10
        )
    )

    //Agregar usuarios de prueba con usuarios con rol
}