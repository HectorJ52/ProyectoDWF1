# Spring Cátedra -- Fase 2: Primer Avance

## 1. Descripción del sistema

**Spring Cátedra** es el backend de un sistema para la gestión de los
servicios de un centro de formación, desarrollado como parte del
proyecto académico. Expone una API REST construida con **Java 21** y
**Spring Boot**, con persistencia mediante **JPA/Hibernate**,
validaciones con Jakarta Validation, manejo centralizado de
excepciones, autenticación con **JWT** y documentación de los
endpoints mediante **Swagger/OpenAPI**.

El sistema contempla los módulos de clientes, cursos, diplomados,
docentes, inscripciones, espacios, equipamiento, catering,
cotizaciones, reservas, solicitudes, pagos y usuarios.

La arquitectura separa responsabilidades en controladores, servicios,
implementaciones, repositorios, modelos y objetos de transferencia de
datos (DTOs Request/Response), siguiendo el flujo:

```text
Controller → Service → Implementation → Repository → Base de datos
```

------------------------------------------------------------------------

## 2. Requisitos para ejecutar el proyecto

- **Java JDK 21**
- **Maven** (el proyecto incluye Maven Wrapper: `mvnw` y `mvnw.cmd`,
  no es obligatorio tener Maven instalado aparte)
- IDE compatible con proyectos Maven (IntelliJ IDEA, Eclipse, VS Code)
- Git, para clonar y trabajar con el repositorio

### Dependencias principales

Definidas en `pom.xml`:

- Spring Boot Starter Data JPA
- Spring Boot Starter Web MVC
- Spring Boot Starter Validation
- Spring Boot Starter Security
- SpringDoc OpenAPI (Swagger)
- H2 Database
- MapStruct
- Lombok
- JJWT (JSON Web Token)

------------------------------------------------------------------------

## 3. Configuración de la base de datos

La persistencia del sistema se administra de forma desacoplada mediante **JPA / Hibernate**. La configuración base se encuentra en:

```text
src/main/resources/application.properties
```

### 3.1 Base de datos en desarrollo (H2 en memoria)

Para el desarrollo y las pruebas en laboratorio, el proyecto utiliza **H2 Database** embebida. Al no definir propiedades de conexión externas, Spring Boot crea la base de datos automáticamente en la **memoria RAM** al iniciar la aplicación.

Características importantes:

- **No existe un archivo físico** (`.db` o similar) dentro del proyecto. H2 se incluye como dependencia Maven y opera en memoria.
- **Comportamiento volátil:** los datos viven únicamente mientras la aplicación esté en ejecución. Al detener el servidor, la memoria se libera y las tablas desaparecen.
- **Esquema generado por JPA:** Hibernate crea las tablas a partir de las entidades del paquete `sv.edu.udb.model`.
- **Siembra automática:** en cada arranque, los componentes `RoleInitializer` y `AdminInitializer` registran los roles del sistema (`ADMIN`, `RECEPCIONISTA`, `CLIENTE`, `CONTABILIDAD`) y el usuario administrador inicial.

### 3.2 Script de base de datos / Plano de producción (Microsoft SQL Server)

Aunque en desarrollo se usa H2 en memoria, el entregable exige un **script o respaldo de la base de datos**. Por esa razón se incluye el plano estructural (DDL) en:

```text
database/SpringCatedra.sql
```

Este script está escrito con sintaxis de **Microsoft SQL Server** (`IDENTITY(1,1)`, `DATETIME2`, etc.) y representa el diseño de la base de datos para un entorno de producción.

**Justificación técnica:**

Gracias a la abstracción de **Spring Data JPA / Hibernate**, la arquitectura está desacoplada del motor de base de datos. Migrar a producción no requiere modificar ninguna clase Java: basta con:

1. Ejecutar el script `database/SpringCatedra.sql` en un servidor Microsoft SQL Server.
2. Actualizar las propiedades de conexión en `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://LOCALHOST_O_IP:1433;databaseName=CatedraDB;encrypt=true;trustServerCertificate=true;
spring.datasource.username=TU_USUARIO_SQL
spring.datasource.password=TU_CONTRASEÑA_SQL
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

De esta forma se cumple el requisito de entregar un script de base de datos, manteniendo al mismo tiempo un entorno de desarrollo ligero y ágil con H2.

------------------------------------------------------------------------

## 4. Instrucciones de ejecución

### 4.1 Clonar el repositorio

```bash
git clone [URL_DEL_REPOSITORIO]
cd spring-catedra
```

### 4.2 Configurar la base de datos (si aplica)

1. Crear la base de datos, si se usará una distinta a H2 en memoria.
2. Ejecutar el script `database/SpringCatedra.sql`, si aplica.
3. Configurar credenciales y URL de conexión en `application.properties`.
4. Verificar que el servidor de base de datos esté activo.

Si no se configura ninguna base de datos externa, el proyecto arranca
igual usando H2 en memoria, sin pasos adicionales.

### 4.3 Ejecutar el proyecto

En Windows:

```bash
mvnw.cmd spring-boot:run
```

En Linux/macOS:

```bash
./mvnw spring-boot:run
```

También puede ejecutarse directamente la clase principal:

```text
sv.edu.udb.SpringCatedraApplication
```

### 4.4 Acceder a Swagger

Una vez iniciada la aplicación:

```text
http://localhost:8080/swagger-ui/index.html
```

Documentación OpenAPI en formato JSON:

```text
http://localhost:8080/v3/api-docs
```

------------------------------------------------------------------------

## 5. Estructura de paquetes

El código fuente principal se encuentra en:

```text
src/main/java/sv/edu/udb/
```

```text
sv.edu.udb
├── config
│   ├── AdminInitializer
│   ├── OpenApiConfig
│   ├── RoleInitializer
│   └── SecurityConfig
│
├── controller
│   ├── AuthController
│   ├── CategoriaController
│   ├── ClienteController
│   ├── CotizacionController
│   ├── CursoController
│   ├── DetalleCotizacionController
│   ├── DiplomadoController
│   ├── DocenteController
│   ├── EquipamientoController
│   ├── EspacioController
│   ├── InscripcionController
│   ├── ModalidadController
│   ├── PagoController
│   ├── ReservaEspacioController
│   ├── ServicioCateringController
│   ├── SolicitudCateringController
│   └── UsuarioController
│   ├── request      (DTOs de entrada)
│   └── response     (DTOs de salida)
│
├── exception
│   ├── EspacioOcupadoException
│   ├── GlobalExceptionHandler
│   └── SaldoInsuficienteException
│
├── implementation   (implementaciones de los servicios)
├── model             (entidades JPA)
├── repository        (interfaces Spring Data JPA)
│
├── security
│   ├── CustomUserDetailsService
│   ├── JwtAuthenticationFilter
│   └── JwtService
│
└── service
    └── mapper        (mappers MapStruct)
```

Los modelos representan las entidades persistentes; los objetos
`Request` y `Response` separan los datos de entrada y salida de los
controladores, y los `mapper` convierten entre entidades y DTOs.

------------------------------------------------------------------------

## 6. Integrantes y responsabilidades

| Integrante                                | Responsabilidad               |
|-------------------------------------------|-------------------------------|
| Joaquín Enmanuel Ayala Orellana. AO250420 | Gestión académica, script SQL |
| Héctor Gabriel Juárez Melgar.    JM252298 | Clientes e inscripciones      |
| Brandon Eliel Bernal Rodriguez. BR253266  | Cotizaciones                  |
| Jonathan Manuel Valencia Ugarte. VU253065 | Espacios y catering           |
| Brandon Steven Reyes Reyes. RR241992      | Pagos y seguridad             |
