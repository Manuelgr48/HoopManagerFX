# HoopManagerFX

**HoopManagerFX** es una aplicación de escritorio desarrollada en JavaFX para la gestión de un club de baloncesto.  
Permite administrar equipos, jugadores, partidos, usuarios y estadísticas de rendimiento, diferenciando permisos según el rol del usuario.

Proyecto de Fin de Ciclo de Desarrollo de Aplicaciones Multiplataforma.

## Autor

Manuel García Rey

## Descripción

La aplicación está pensada para facilitar la gestión interna de un club de baloncesto como el Liceo La Paz.  
Desde una única interfaz se pueden consultar equipos, revisar plantillas, registrar partidos, analizar estadísticas de jugadores y generar informes en PDF.

El sistema dispone de distintos perfiles de usuario:

- **Administrador:** acceso completo a la gestión de usuarios, equipos, jugadores, partidos y estadísticas.
- **Entrenador:** puede consultar la información general del club y gestionar los jugadores y estadísticas del equipo que tiene asignado.
- **Jugador / usuario normal:** puede consultar información, pero no modificar datos.

## Funcionalidades principales

- Inicio de sesión con usuarios registrados.
- Registro de nuevos usuarios.
- Dashboard principal con accesos a las secciones principales.
- Gestión de usuarios por parte del administrador.
- Gestión de equipos.
- Consulta de jugadores del club.
- Gestión de jugadores por parte del administrador.
- Gestión de jugadores del propio equipo por parte del entrenador asignado.
- Gestión de partidos.
- Visualización de información detallada de cada equipo.
- Visualización de estadísticas de cada jugador.
- Visualización del rendimiento de los jugadores en cada partido.
- Generación de informes PDF de equipos.
- Buscadores en las tablas principales.
- Control de permisos según rol.
- Validaciones básicas de formularios.
- Base de datos MySQL con datos de prueba.

## Tecnologías utilizadas

- Java 23
- JavaFX 17
- FXML
- MySQL
- JDBC
- Maven
- JasperReports
- CSS integrado en FXML
- Patrón DAO
- Arquitectura por capas

## Estructura del proyecto

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

Requisitos previos
Para ejecutar el proyecto es necesario tener instalado:

JDK 23
Maven
MySQL Server
IntelliJ IDEA, Eclipse o NetBeans
El proyecto está configurado con Maven, por lo que las dependencias se descargan automáticamente.

Configuración de la base de datos
El proyecto incluye un script llamado:

hoopmanager_db.sql
Este script:

Borra la base de datos si ya existe.
Crea la base de datos hoopmanager.
Crea todas las tablas necesarias.
Inserta usuarios de prueba.
Inserta equipos.
Inserta jugadores.
Inserta partidos.
Inserta estadísticas.
Para cargar la base de datos:

Abrir MySQL Workbench.
Abrir el archivo hoopmanager_db.sql.
Ejecutar el script completo.
Comprobar que se ha creado la base de datos hoopmanager.
Configuración de conexión
La conexión a la base de datos se configura en:

src/main/resources/config.properties
Contenido por defecto:

db.url=jdbc:mysql://localhost:3306/hoopmanager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.user=root
db.password=
Si el usuario o contraseña de MySQL son diferentes, deben modificarse en este archivo.

Ejemplo:

db.user=root
db.password=1234
Ejecución del proyecto
Desde IntelliJ IDEA:

Abrir el proyecto.
Esperar a que Maven descargue las dependencias.
Ejecutar la clase:
MainApp.java
También se puede ejecutar desde terminal con Maven:

mvn clean javafx:run
Usuarios de prueba
El script SQL incluye usuarios ya creados.

Administrador
Correo: admin@hoopmanager.com
Contraseña: 1234
Entrenador Liceo La Paz A
Correo: carlos.fernandez@hoopmanager.com
Contraseña: 1234
Entrenador Liceo La Paz B
Correo: marta.sanchez@hoopmanager.com
Contraseña: 1234
Entrenador Liceo La Paz Cadete
Correo: javier.lorenzo@hoopmanager.com
Contraseña: 1234
Entrenador Liceo La Paz Infantil
Correo: laura.pazos@hoopmanager.com
Contraseña: 1234
Usuario normal
Correo: usuario@hoopmanager.com
Contraseña: 1234
Roles y permisos
Administrador
El administrador puede:

Crear, modificar y eliminar usuarios.
Asignar roles.
Asignar equipos a entrenadores.
Crear, modificar y eliminar equipos.
Crear, modificar y eliminar jugadores.
Crear, modificar y eliminar partidos.
Crear, modificar y eliminar estadísticas.
Consultar toda la información del club.
Generar informes PDF.
Entrenador
El entrenador puede:

Consultar los equipos del club.
Consultar todos los jugadores.
Acceder al detalle de su equipo.
Gestionar jugadores de su propio equipo.
Gestionar estadísticas de jugadores de su propio equipo.
Consultar partidos y rendimiento.
No puede gestionar usuarios ni modificar equipos que no sean el suyo.

Usuario normal
El usuario normal puede:

Consultar información general.
Ver equipos.
Ver jugadores.
Ver partidos.
Consultar estadísticas.
No puede modificar, añadir ni eliminar datos.

Principales vistas de la aplicación
Login
Permite iniciar sesión con correo y contraseña.

Registro
Permite crear una cuenta nueva.
Los usuarios registrados desde esta vista se crean con rol de usuario normal.

Inicio
Vista principal con tarjetas de acceso a las secciones principales:

Equipos
Jugadores
Partidos
Usuarios
La tarjeta de usuarios solo está disponible para administradores.

Equipos
Muestra todos los equipos del club.
Permite acceder a la información detallada de cada equipo.

Detalle de equipo
Muestra:

Información básica del equipo.
Jugadores del equipo.
Gráfico de rendimiento.
Generación de informe PDF.
Gestión de jugadores si el usuario tiene permisos.
Jugadores
Muestra los jugadores del club.
Permite buscar por nombre, apellidos o equipo.
Desde esta vista se puede acceder al rendimiento individual de cada jugador.

Partidos
Muestra los partidos registrados.
Permite consultar rival, ubicación, resultado y equipo local.
Desde cada partido se puede acceder a la información de rendimiento de los jugadores del equipo local.

Usuarios
Vista exclusiva del administrador.
Permite crear, modificar y eliminar usuarios, asignar roles y vincular equipos.

Informes PDF
La aplicación permite generar informes PDF desde el detalle de un equipo.

El informe incluye información del equipo y sus jugadores.
Para generarlo:

Entrar en Equipos.
Seleccionar un equipo.
Pulsar en Acceder a información.
Pulsar en Generar informe.
Seleccionar dónde guardar el PDF.
Datos de prueba
La base de datos incluye datos de prueba para:

4 equipos.
24 jugadores.
15 partidos.
Estadísticas individuales de jugadores.
Usuarios con distintos roles.
Esto permite probar la aplicación desde el primer arranque sin introducir datos manualmente.

Pruebas recomendadas
Antes de entregar o ejecutar la demo, se recomienda comprobar:

Iniciar sesión como administrador.
Iniciar sesión como entrenador.
Iniciar sesión como usuario normal.
Comprobar que cada rol ve únicamente las opciones que le corresponden.
Crear un usuario nuevo.
Modificar un usuario.
Intentar quitar el último administrador.
Buscar equipos.
Acceder al detalle de un equipo.
Generar un PDF.
Buscar jugadores.
Ver estadísticas de un jugador.
Crear, modificar y eliminar un jugador como administrador.
Probar que no se permite repetir dorsal dentro del mismo equipo.
Crear, modificar y eliminar partidos.
Ver información de un partido.
Añadir y modificar estadísticas.
Cerrar sesión y volver al login.
Posibles mejoras futuras
Algunas mejoras que podrían añadirse en futuras versiones son:

Calendario visual de partidos.
Exportación de estadísticas a Excel.
Comparación entre jugadores.
Estadísticas avanzadas por temporada.
Gestión de entrenamientos.
Control de lesiones.
Notificaciones por correo electrónico.
Historial de cambios por usuario.
Mejoras visuales con una hoja CSS global.
Despliegue con instalador para Windows.
Licencia
Proyecto desarrollado con finalidad académica para el Proyecto de Fin de Ciclo de DAM.