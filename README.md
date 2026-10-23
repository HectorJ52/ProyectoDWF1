
# Spring Cátedra — Fase 3

## 1. Descripción del sistema

**Spring Cátedra** (UCA-CFC Connect) es el backend de un sistema empresarial para la gestión de los servicios del Centro de Formación Continua. Expone una API REST construida con **Java 21** y **Spring Boot**, con persistencia mediante **JPA/Hibernate**, validaciones con Jakarta Validation, manejo centralizado de excepciones, autenticación y autorización con **Spring Security + JWT**, y documentación de los endpoints mediante **Swagger/OpenAPI**.

El sistema contempla los módulos de:

- Gestión académica (cursos, diplomados, categorías, modalidades, docentes)
- Clientes e inscripciones
- Cotizaciones
- Alquiler de espacios y equipamiento
- Servicios y solicitudes de catering
- Agenda / reservas (con control de conflictos de horario)
- Pagos
- Usuarios, roles y seguridad

La arquitectura separa responsabilidades siguiendo el flujo:

```text
Controller → Service → Implementation → Repository → Base de datos
```

Se utilizan DTOs (`Request` / `Response`) y MapStruct para el mapeo entre capas.

---

## 2. Requisitos para ejecutar el proyecto

- **Java JDK 21**
- **Maven** (el proyecto incluye Maven Wrapper: `mvnw` / `mvnw.cmd`)
- IDE compatible con Maven (IntelliJ IDEA, Eclipse, NetBeans, VS Code)
- **Microsoft SQL Server**
- Git

### Dependencias principales

Definidas en `pom.xml`:

- Spring Boot Starter Data JPA
- Spring Boot Starter Web
- Spring Boot Starter Validation
- Spring Boot Starter Security
- SpringDoc OpenAPI (Swagger)
- Microsoft SQL Server JDBC Driver
- MapStruct
- Lombok
- JJWT (JSON Web Token)
- JUnit 5 + Mockito (pruebas unitarias)

---

## 3. Configuración de la base de datos

La configuración se encuentra en:

```text
src/main/resources/application.properties
```

### 3.1 Base de datos principal (Microsoft SQL Server)

El proyecto está configurado para trabajar con **Microsoft SQL Server**.

1. Crear la base de datos (si aún no existe).
2. Ejecutar el script de estructura ubicado en:

```text
SpringCatedra.sql
```

3. Configurar las credenciales en `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=SPRING_CATEDRA;encrypt=true;trustServerCertificate=true;
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> **Nota:** Ajusta el nombre de la base de datos, el puerto y las credenciales según tu entorno local.

### 3.2 Roles y usuario inicial

Al iniciar la aplicación se ejecutan los componentes:

- `RoleInitializer` → crea los roles: `ADMIN`, `RECEPCIONISTA`, `CLIENTE`, `CONTABILIDAD`
- `AdminInitializer` → crea el usuario administrador inicial

---

## 4. Instrucciones de ejecución

### 4.1 Clonar el repositorio

```bash
git clone https://github.com/HectorJ52/ProyectoDWF1.git
cd ProyectoDWF1
```

### 4.2 Configurar la base de datos

1. Asegúrate de que SQL Server esté en ejecución.
2. Ejecuta el script `SpringCatedra.sql` (si es necesario).
3. Verifica/ajusta las credenciales en `application.properties`.

### 4.3 Ejecutar el proyecto

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

También puedes ejecutar la clase principal:

```text
sv.edu.udb.SpringCatedraApplication
```

### 4.4 Acceder a Swagger

Una vez iniciada la aplicación:

- Swagger UI:  
  `http://localhost:8080/swagger-ui/index.html`

- OpenAPI JSON:  
  `http://localhost:8080/v3/api-docs`

---

## 5. Seguridad

El sistema utiliza:

- **Spring Security**
- **JWT** (JSON Web Token) para autenticación sin estado
- Control de acceso por roles

### Roles del sistema

| Rol            | Descripción principal                          |
|----------------|------------------------------------------------|
| ADMIN          | Configuración completa del sistema             |
| RECEPCIONISTA  | Operaciones diarias (inscripciones, reservas…) |
| CLIENTE        | Consultas y solicitudes propias                |
| CONTABILIDAD   | Gestión y validación de pagos                  |

### Autenticación

- Endpoint de login: `POST /api/auth/login`
- Se debe enviar usuario/correo y contraseña.
- La respuesta incluye el token JWT que debe enviarse en el header:

```http
Authorization: Bearer <token>
```

---

## 6. Pruebas unitarias

Se implementaron pruebas unitarias con **JUnit 5** y **Mockito**, enfocadas en la lógica de negocio más crítica:

- Módulo de **Reservas de Espacio**
- Validación de disponibilidad / solapamiento de horarios
- Casos de éxito y de fallo (lanzamiento de `EspacioOcupadoException`)

Para ejecutar las pruebas:

```bash
mvn test
```

O de forma específica:

```bash
mvn test -Dtest=ReservaEspacioServiceTest
```

---

## 7. Estructura de paquetes

```text
src/main/java/sv/edu/udb/
├── config
│   ├── AdminInitializer
│   ├── OpenApiConfig
│   ├── RoleInitializer
│   └── SecurityConfig
│
├── controller
│   ├── AuthController
│   ├── ... (demás controladores)
│   ├── request/          ← DTOs de entrada
│   └── response/         ← DTOs de salida
│
├── exception
│   ├── EspacioOcupadoException
│   ├── GlobalExceptionHandler
│   └── SaldoInsuficienteException
│
├── implementation        ← Implementaciones de los servicios
├── model                 ← Entidades JPA
├── repository            ← Interfaces Spring Data JPA
│
├── security
│   ├── CustomUserDetailsService
│   ├── JwtAuthenticationFilter
│   └── JwtService
│
└── service
    └── mapper/           ← Mappers MapStruct
```

---

## 8. Integrantes y responsabilidades

| Integrante                                      | Responsabilidad principal (Fase 3)          |
|------------------------------------------------|---------------------------------------------|
| Joaquín Enmanuel Ayala Orellana — AO250420     | Base de datos (SQL Server) + configuración  |
| Héctor Gabriel Juárez Melgar — JM252298        | Lógica de agenda y conflictos de horario    |
| Brandon Eliel Bernal Rodríguez — BR253266      | Pruebas unitarias (JUnit + Mockito)         |
| Jonathan Manuel Valencia Ugarte — VU253065     | Seguridad (JWT + control de roles)          |
| Brandon Steven Reyes Reyes — RR241992          | Documentación y entregable final            |

---
