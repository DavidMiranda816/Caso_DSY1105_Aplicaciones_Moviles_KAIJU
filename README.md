# Kaiju Inventory

Aplicación móvil Android (MVP) para la empresa Kaiju, orientada a la gestión, trazabilidad y control del inventario de sus productos.

## Integrantes
- David Miranda Calderón
- Daniel Zhain Simms Delgado
- Rocío Muñoz Rivera

## Tecnologías y arquitectura
- Kotlin + Jetpack Compose (Material 3)
- Patrón MVVM: `ui/` (pantallas y componentes), `viewmodel/` (estado y validaciones), `repository/` (acceso a datos), `model/` (clases de datos), `database/` (Room), `navegation/` (rutas y NavHost)
- Persistencia local con Room (SQLite)
- Navigation Compose, StateFlow, CameraX + ML Kit

## Funcionalidades implementadas
| RF | Funcionalidad |
|---|---|
| RF-01 | Inicio de sesión con usuarios de prueba; cada perfil ve solo las opciones que le corresponden |
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

### Usuarios de prueba (contraseña: 1234)
- ana@kaiju.cl — Administrador
- beto@kaiju.cl — Vendedor
- carla@kaiju.cl — Encargado de inventario

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

### Identidad visual
Paleta propia en `ui/theme` (morado como color principal, amarillo y naranjo para acentos y rojo para alertas), con buen contraste para leer en bodega.

## Pasos para ejecutar
1. Clonar el repositorio:
   `git clone https://github.com/DavidMiranda816/Caso_DSY1105_Aplicaciones_Moviles_KAIJU.git`
2. Abrir la carpeta en Android Studio y esperar la sincronización de Gradle.
3. Crear o elegir un emulador (Device Manager) con Android 7.0 (API 24) o superior.
4. Presionar Run ▶.
5. Iniciar sesión con un usuario de prueba o con los botones de acceso rápido.

## Diseño y planificación
- Diseño (sketch, wireframe y mockup): [Carpeta de Drive del equipo](https://drive.google.com/drive/folders/1XL4gLUkanAdTLWINhARqaPNu_OugBA-n?usp=sharing)
- Planificación: [Tablero de Trello](https://trello.com/b/HXUx1zm2/tablero-trello)
