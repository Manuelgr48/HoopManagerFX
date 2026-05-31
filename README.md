
```markdown
# HoopManagerFX

**HoopManagerFX** es una aplicación de escritorio desarrollada en JavaFX para la gestión integral de un club de baloncesto. Permite administrar equipos, jugadores, partidos, usuarios y estadísticas de rendimiento, diferenciando permisos según el rol del usuario.

> Proyecto de Fin de Ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM).

---

## Autor
**Manuel García Rey**

---

## Descripción

La aplicación está pensada para facilitar la gestión interna de un club de baloncesto, como el Liceo La Paz. Desde una única interfaz centralizada se pueden consultar equipos, revisar plantillas, registrar partidos, analizar estadísticas de jugadores y generar informes en PDF.

---

## Funcionalidades Principales

* **Autenticación:** Inicio de sesión y registro de nuevos usuarios.
* **Dashboard:** Panel principal con accesos rápidos a las secciones clave.
* **Gestión de Usuarios:** Control total por parte del administrador.
* **Gestión Deportiva:** Administración de equipos, jugadores y partidos.
* **Análisis y Estadísticas:** Visualización del rendimiento de los jugadores en cada partido.
* **Reportes:** Generación de informes en formato PDF para los equipos.
* **Navegabilidad:** Buscadores integrados en las tablas principales.
* **Seguridad:** Control de permisos basado en roles y validaciones de formularios.

---

## Tecnologías Utilizadas

* **Lenguaje:** Java 23
* **Interfaz Gráfica:** JavaFX 17, FXML, CSS integrado
* **Base de Datos:** MySQL, JDBC, Patrón DAO
* **Herramientas:** Maven, JasperReports
* **Arquitectura:** Diseño basado en capas

---

## Estructura del Proyecto

```text
HoopManagerFX
├── src
│   └── main
│       ├── java
│       │   └── com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx
│       │       ├── controller
│       │       ├── dao
│       │       ├── model
│       │       ├── service
│       │       └── util
│       └── resources
│           ├── com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx
│           ├── images
│           ├── reportes
│           └── config.properties
├── hoopmanager_db.sql
├── pom.xml
└── README.md

```

---

## Instalación y Configuración

### 1. Requisitos Previos

Para compilar y ejecutar el proyecto, asegúrate de tener instalado:

* JDK 23
* Maven
* MySQL Server
* IDE recomendado: IntelliJ IDEA, Eclipse o NetBeans

### 2. Configuración de la Base de Datos

El proyecto incluye el script `hoopmanager_db.sql`, el cual se encarga de crear la base de datos `hoopmanager`, generar las tablas e insertar los datos de prueba (usuarios, equipos, jugadores, partidos y estadísticas).

**Pasos para cargar la base de datos:**

1. Abre MySQL Workbench (o tu cliente MySQL preferido).
2. Carga y ejecuta el archivo `hoopmanager_db.sql` en su totalidad.
3. Verifica que la base de datos `hoopmanager` se ha creado correctamente.

### 3. Configuración de Conexión

La conexión a la base de datos se gestiona desde el archivo `src/main/resources/config.properties`. Si tus credenciales de MySQL son distintas a las que vienen por defecto, actualízalas aquí:

```properties
db.url=jdbc:mysql://localhost:3306/hoopmanager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.user=root
db.password=tu_contraseña_aqui

```

### 4. Ejecución del Proyecto

El proyecto está configurado con Maven, por lo que las dependencias se descargarán automáticamente.

* **Desde el IDE (ej. IntelliJ IDEA):** Ejecuta la clase `MainApp.java`.
* **Desde la terminal:**
```bash
mvn clean javafx:run

```



---

## Usuarios de Prueba

Para facilitar la evaluación de la aplicación, la base de datos incluye los siguientes usuarios preconfigurados:

| Rol | Correo Electrónico | Contraseña | Entidad Asignada |
| --- | --- | --- | --- |
| **Administrador** | `admin@hoopmanager.com` | `1234` | Sistema completo |
| **Entrenador** | `carlos.fernandez@hoopmanager.com` | `1234` | Liceo La Paz A |
| **Entrenador** | `marta.sanchez@hoopmanager.com` | `1234` | Liceo La Paz B |
| **Entrenador** | `javier.lorenzo@hoopmanager.com` | `1234` | Liceo La Paz Cadete |
| **Entrenador** | `laura.pazos@hoopmanager.com` | `1234` | Liceo La Paz Infantil |
| **Usuario Normal** | `usuario@hoopmanager.com` | `1234` | Ninguna (Solo lectura) |

---

## Roles y Permisos

| Permisos | Administrador | Entrenador | Usuario Normal |
| --- | --- | --- | --- |
| Consultar información (Equipos, Partidos, Estadísticas) | Sí | Sí | Sí |
| Gestionar jugadores y estadísticas de su **propio equipo** | Sí | Sí | No |
| Crear, modificar y eliminar cualquier Equipo/Jugador/Partido | Sí | No | No |
| Gestionar usuarios y asignar roles/equipos | Sí | No | No |
| Generar informes PDF | Sí | No | No |

---

## Guía de Pruebas Recomendadas

Antes de desplegar o presentar la aplicación, se sugiere realizar el siguiente flujo de pruebas:

1. **Control de Accesos:** Iniciar sesión con los tres roles distintos y verificar que las vistas y permisos se ajustan a lo definido.
2. **Gestión de Usuarios (Admin):** Crear, editar y eliminar usuarios. Verificar la restricción de no poder eliminar al último administrador.
3. **Gestión Deportiva (Admin/Entrenador):** Añadir, modificar y eliminar jugadores. Comprobar la validación de dorsales duplicados en un mismo equipo.
4. **Partidos y Estadísticas:** Registrar un partido, añadir estadísticas individuales y verificar que se reflejan correctamente.
5. **Reportes:** Acceder al detalle de un equipo y generar el informe PDF correctamente en la ruta deseada.

---

## Posibles Mejoras Futuras

* Calendario visual interactivo de partidos.
* Exportación de estadísticas a formato Excel (.xlsx).
* Herramienta de comparación directa entre jugadores.
* Registro y control de lesiones.
* Gestión de asistencias a entrenamientos.
* Notificaciones automatizadas por correo electrónico.
* Historial de auditoría (cambios por usuario).
* Despliegue de la aplicación mediante un instalador nativo para Windows.

---

Enlace para el vídeo demostración de la app: https://drive.google.com/drive/folders/1nRqGTTRUgtw5hkU9KEJ41ZQO8OkaMDTS?usp=sharing

---

## Licencia

Proyecto desarrollado estrictamente con finalidad académica.

```

```
