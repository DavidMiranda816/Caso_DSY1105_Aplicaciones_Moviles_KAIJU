# Kaiju Inventory

Aplicación móvil Android (MVP) para la empresa Kaiju, orientada a la gestión, trazabilidad y control del inventario de sus productos.

## Integrantes
- David Miranda Calderón | correo: dav.mirandac@duocuc.cl
- Daniel Zhain Simms Delgado | correo: da.simms@duocuc.cl
- Rocío Muñoz Rivera | correo: roci.munoz@duocuc.cl / rociobetmr04@gmail.com(correo de colaboración).

## Tecnologías y arquitectura
- Kotlin + Jetpack Compose (Material 3)
- Patrón MVVM: `ui/` (pantallas y componentes), `viewmodel/` (estado y validaciones), `repository/` (acceso a datos), `model/` (clases de datos), `database/` (Room), `navegation/` (rutas y NavHost)
- Persistencia local con Room (SQLite)
- Navigation Compose, StateFlow, CameraX + ML Kit

## Funcionalidades implementadas
| RF | Funcionalidad |
|---|---|
| RF-01 | Inicio de sesión mediante el backend compartido con Veterinaria San Marcos |
| RF-02 | Catálogo de productos con nombre, categoría, precio y stock |
| RF-03 | Búsqueda por nombre, SKU o marca y filtro por categoría |
| RF-04 | Detalle del producto con historial de movimientos |
| RF-05 | Crear y editar productos con formulario validado (solo administrador) |
| RF-06 | Registro de entradas de mercadería |
| RF-07 | Registro de salidas por venta o por consumo |
| RF-08 | Ajustes de inventario con motivo obligatorio |
| RF-09 | Marca de stock bajo en el catálogo y pantalla de alertas |
| RF-10 | Persistencia local: los datos se conservan al cerrar la app |

### Perfiles y permisos
| Acción | Administrador | Encargado | Vendedor |
|---|---|---|---|
| Crear, editar y eliminar productos | ✅ | ❌ | ❌ |
| Entrada | ✅ | ✅ | ❌ |
| Salida por venta | ❌ | ✅ | ✅ |
| Salida por consumo | ❌ | ✅ | ❌ |
| Ajuste | ✅ | ✅ | ❌ |
| Ver alertas | ✅ | ✅ | ❌ |

### Administración de usuarios

Desde **Usuarios**, un administrador puede listar cuentas activas de Kaiju, crear una cuenta con perfil, editar su nombre y correo, y desactivarla. Los perfiles que devuelve la API son `ADMIN`, `VENDEDOR` y `ENCARGADO`. Los productos y movimientos se guardan en Room.

## Endpoints utilizados

[Backend compartido de Veterinaria San Marcos](https://github.com/DavidMiranda816/Caso-DSY1104-Fullstack-Veterinaria-San-Marco-Backend)

| Método y ruta | Uso en Kaiju |
|---|---|
| `POST /api/auth/login` | Obtiene el token y el perfil de la cuenta. |
| `GET /api/usuarios` | Lista usuarios con el token de administrador. |
| `POST /api/usuarios` | Crea un usuario con nombre, correo, contraseña y rol. |
| `PUT /api/usuarios/{id}` | Edita nombre y correo. |
| `PATCH /api/usuarios/{id}/desactivar` | Quita una cuenta del listado activo e impide que vuelva a iniciar sesión. |
| `GET https://pokeapi.co/api/v2/pokemon/pikachu` | Muestra la ficha de Pikachu en la pantalla oculta. |

## Decisiones del equipo

### Recursos nativos
| Necesidad del caso | Recurso | Permiso y tipo | RF | Si el usuario dice que no |
|---|---|---|---|---|
| El encargado o vendedor necesita identificar un producto rápido en bodega | Cámara con CameraX + ML Kit (escáner QR) | CAMERA, tiempo de ejecución | RF-06, RF-07 | Puede ingresar el SKU a mano o ir a Ajustes |
| El encargado necesita enterarse cuando un producto queda bajo su stock mínimo | Notificaciones (NotificationCompat) | POST_NOTIFICATIONS, tiempo de ejecución (Android 13+) | RF-09 | La app sigue funcionando; las alertas se ven en la pantalla Alertas |

Además, al confirmar un movimiento el teléfono vibra (LocalHapticFeedback, sin permiso).

### Animaciones
- Entrada de la tarjeta de login (deslizamiento, aparición y escala) y mensaje de error animado: dan retroalimentación al iniciar sesión.
- La marca de stock bajo aparece con AnimatedVisibility y el color del stock cambia de forma gradual: avisa visualmente qué productos requieren reposición.
- El stock resultante en Movimiento cambia de color según el resultado (normal, bajo el mínimo o negativo): anticipa el efecto del movimiento antes de confirmarlo.

### Módulo propio: escáner QR para registrar movimientos
Desde la pestaña Escanear, el usuario lee el código QR de un producto y la app abre el formulario de movimiento con ese producto ya seleccionado. En una bodega con muchos productos parecidos, evita buscarlos a mano en una lista y reduce errores al registrar entradas y salidas. Si no hay permiso de cámara, se puede ingresar el SKU manualmente.

### Easter Egg (Funcionalidad oculta)
En la pantalla de inicio de sesión (`PantallaLogin`), se puede presionar **7 veces consecutivas** el ícono del logo de la app o el título *"Kaiju Inventory"*. Esto activa una navegación a la pantalla especial `PantallaEasterEgg`, la cual se conecta a la **PokéAPI** vía Retrofit para consultar y mostrar la información y el sprite oficial de Pikachu.

### Identidad visual
Paleta propia en `ui/theme` (morado como color principal, amarillo y naranjo para acentos y rojo para alertas), con buen contraste para leer en bodega.

## Pasos para ejecutar

1. Levanta el backend compartido con MySQL y la migración V2. Incorpora la rama [`feat/usuarios-kaiju`](https://github.com/DavidMiranda816/Caso-DSY1104-Fullstack-Veterinaria-San-Marco-Backend/tree/feat/usuarios-kaiju) sobre la rama del backend que ya usas para conservar sus migraciones. Para el primer ingreso usa una cuenta `ADMIN` existente. Esa cuenta puede crear los perfiles de vendedor y encargado desde Kaiju.
2. Abre este proyecto en Android Studio. Sincroniza Gradle, instala el SDK Android 37 y selecciona un emulador o teléfono con Android 7.0 (API 24) o superior.
3. Para una demo con conexión local, usa un emulador de Android 16 (API 36). La app usa `http://10.0.2.2:8080/` por defecto. Si el backend vive en EC2, puedes reenviar su puerto 8080 a tu equipo por SSH y continuar usando esa URL en el emulador. Para una instancia accesible por HTTPS, define `KAIJU_BACKEND_URL=https://tu-dominio/` en `~/.gradle/gradle.properties` y recompila. Usa la raíz de la API, sin agregar `/api`.
4. En un teléfono por USB sin servidor HTTPS, ejecuta `adb reverse tcp:8080 tcp:8080` y configura `KAIJU_BACKEND_URL=http://127.0.0.1:8080/` antes de compilar. El backend debe ser accesible en el puerto 8080 del equipo, directamente o por el túnel SSH.
5. Ejecuta la app e inicia sesión con la cuenta de administrador. En **Usuarios**, crea un vendedor, edita su nombre, cierra sesión y prueba la nueva cuenta. Vuelve al administrador, desactiva una segunda cuenta de prueba y comprueba que el login de esa cuenta se rechaza.
6. Ejecuta `./gradlew testDebugUnitTest` y, con el emulador iniciado, `./gradlew connectedDebugAndroidTest`.

## Diseño y planificación
- Diseño (sketch, wireframe y mockup): [Carpeta de Drive del equipo](https://drive.google.com/drive/folders/1XL4gLUkanAdTLWINhARqaPNu_OugBA-n?usp=sharing)
- Planificación: [Tablero de Trello](https://trello.com/b/HXUx1zm2/tablero-trello)

## APK Firmado y Llave JKS (Keystore)
- [Carpeta de Google Drive (Apk + Firma)](https://drive.google.com/drive/folders/1KN811Uie2XbZhlpw1NTTlwqb1BznPO7): Contiene el archivo `app-release.apk` compilado y firmado, junto con la llave de firmas `.jks` (`mi_llave`).
