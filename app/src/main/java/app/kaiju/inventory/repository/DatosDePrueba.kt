package app.kaiju.inventory.repository

import app.kaiju.inventory.model.Producto
import app.kaiju.inventory.model.Rol
import app.kaiju.inventory.model.Usuario


object DatosDePrueba {

    val productos: List<Producto> = listOf(
        Producto(
            sku = "PMC-002",
            nombreProducto = "Polera Gato Samurái",
            descripcion = "Polera estampada, talla L, color blanco",
            marca = "Marca propia",
            precio = 13000.0,
            categoria = "Polera manga corta",
            stock = 3,
            estadoProducto = true,
            stockMinimo = 6
        ),
        Producto(
            sku = "PML-002",
            nombreProducto = "Polera ML Tigre Lunar",
            descripcion = "Polera manga larga estampada, talla M, color negro",
            marca = "Marca propia",
            precio = 15000.0,
            categoria = "Polera manga larga",
            stock = 2,
            estadoProducto = true,
            stockMinimo = 4
        ),
        Producto(
            sku = "PPO-001",
            nombreProducto = "Polerón Polo Oni Sonriente",
            descripcion = "Polerón estampado, talla M, color negro",
            marca = "Marca propia",
            precio = 20000.0,
            categoria = "Polerón polo",
            stock = 5,
            estadoProducto = true,
            stockMinimo = 5
        ),
        Producto(
            sku = "PCA-002",
            nombreProducto = "Polerón Canguro Zorro de Fuego",
            descripcion = "Polerón canguro estampado, talla M, color negro",
            marca = "Marca propia",
            precio = 25000.0,
            categoria = "Polerón canguro",
            stock = 0,
            estadoProducto = true,
            stockMinimo = 3
        ),
        Producto(
            sku = "PL-001",
            nombreProducto = "Polera básica negra",
            descripcion = "Polera algodón, talla M, color negro",
            marca = "Marca propia",
            precio = 9990.0,
            categoria = "Polera",
            stock = 25,
            estadoProducto = true,
            stockMinimo = 5
        ),
        Producto(
            sku = "PL-003",
            nombreProducto = "Polera oversize blanca",
            descripcion = "Polera oversize, talla XL, color blanco",
            marca = "Marca propia",
            precio = 13990.0,
            categoria = "Polera",
            stock = 12,
            estadoProducto = true,
            stockMinimo = 4
        ),
        Producto(
            sku = "GO-001",
            nombreProducto = "Gorro de lana",
            descripcion = "Gorro tejido, talla única",
            marca = "Marca propia",
            precio = 7990.0,
            categoria = "Gorro",
            stock = 15,
            estadoProducto = true,
            stockMinimo = 5
        ),
        Producto(
            sku = "GO-002",
            nombreProducto = "Gorra snapback",
            descripcion = "Gorra plana, talla única",
            marca = "Marca propia",
            precio = 9990.0,
            categoria = "Gorra",
            stock = 10,
            estadoProducto = true,
            stockMinimo = 4
        ),
        Producto(
            sku = "CA-001",
            nombreProducto = "Calcetines pack x3",
            descripcion = "Pack de 3 pares, talla única",
            marca = "Marca propia",
            precio = 4990.0,
            categoria = "Calcetín",
            stock = 20,
            estadoProducto = true,
            stockMinimo = 6
        ),
        Producto(
            sku = "JO-001",
            nombreProducto = "Jogger gris",
            descripcion = "Pantalón deportivo, talla M, color gris",
            marca = "Marca propia",
            precio = 19990.0,
            categoria = "Pantalón",
            stock = 6,
            estadoProducto = true,
            stockMinimo = 3
        )
    )

    val usuarios: List<Usuario> = listOf(
        Usuario(correo = "ana@kaiju.cl", contrasena = "1234", nombre = "Ana", rol = Rol.ADMINISTRADOR),
        Usuario(correo = "beto@kaiju.cl", contrasena = "1234", nombre = "Beto", rol = Rol.VENDEDOR),
        Usuario(correo = "carla@kaiju.cl", contrasena = "1234", nombre = "Carla", rol = Rol.ENCARGADO_INVENTARIO)
    )
}