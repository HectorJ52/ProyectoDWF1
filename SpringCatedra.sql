CREATE DATABASE SPRING_CATEDRA;
GO

USE SPRING_CATEDRA;
GO

-- =====================================================
-- 1. ROLES
-- =====================================================

CREATE TABLE dbo.roles (
    id_rol BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);
GO

-- =====================================================
-- 2. USUARIOS
-- =====================================================

CREATE TABLE dbo.usuarios (
    id_usuario BIGINT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    activo BIT NOT NULL DEFAULT 1,
    id_rol BIGINT NOT NULL,

    CONSTRAINT FK_usuarios_roles
        FOREIGN KEY (id_rol)
        REFERENCES dbo.roles(id_rol)
);
GO

-- =====================================================
-- 3. CATEGORÍAS
-- =====================================================

CREATE TABLE dbo.categorias (
    id_categoria BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NULL,
    activo BIT NOT NULL DEFAULT 1
);
GO

-- =====================================================
-- 4. MODALIDADES
-- =====================================================

CREATE TABLE dbo.modalidades (
    id_modalidad BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NULL
);
GO

-- =====================================================
-- 5. CLIENTES
-- =====================================================

CREATE TABLE dbo.clientes (
    id_cliente BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20) NULL,
    direccion VARCHAR(255) NULL,
    activo BIT NOT NULL DEFAULT 1
);
GO

-- =====================================================
-- 6. DOCENTES
-- =====================================================

CREATE TABLE dbo.docentes (
    id_docente BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(150) NOT NULL UNIQUE
);
GO

-- =====================================================
-- 7. CURSOS
-- =====================================================

CREATE TABLE dbo.cursos (
    id_curso BIGINT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(MAX) NULL,
    id_categoria BIGINT NOT NULL,
    id_modalidad BIGINT NOT NULL,
    id_docente BIGINT NULL,
    cupo_maximo INT NOT NULL,
    costo DECIMAL(10,2) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    horario VARCHAR(100) NULL,
    activo BIT NOT NULL DEFAULT 1,

    CONSTRAINT FK_cursos_categorias
        FOREIGN KEY (id_categoria)
        REFERENCES dbo.categorias(id_categoria),

    CONSTRAINT FK_cursos_modalidades
        FOREIGN KEY (id_modalidad)
        REFERENCES dbo.modalidades(id_modalidad),

    CONSTRAINT FK_cursos_docentes
        FOREIGN KEY (id_docente)
        REFERENCES dbo.docentes(id_docente),

    CONSTRAINT CK_cursos_cupo
        CHECK (cupo_maximo > 0),

    CONSTRAINT CK_cursos_costo
        CHECK (costo >= 0),

    CONSTRAINT CK_cursos_fechas
        CHECK (fecha_fin >= fecha_inicio)
);
GO

-- =====================================================
-- 8. DIPLOMADOS
-- =====================================================

CREATE TABLE dbo.diplomados (
    id_diplomado BIGINT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(MAX) NULL,
    id_categoria BIGINT NOT NULL,
    id_modalidad BIGINT NOT NULL,
    id_docente BIGINT NULL,
    cupo_maximo INT NOT NULL,
    costo DECIMAL(10,2) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    horario VARCHAR(100) NULL,
    activo BIT NOT NULL DEFAULT 1,

    CONSTRAINT FK_diplomados_categorias
        FOREIGN KEY (id_categoria)
        REFERENCES dbo.categorias(id_categoria),

    CONSTRAINT FK_diplomados_modalidades
        FOREIGN KEY (id_modalidad)
        REFERENCES dbo.modalidades(id_modalidad),

    CONSTRAINT FK_diplomados_docentes
        FOREIGN KEY (id_docente)
        REFERENCES dbo.docentes(id_docente),

    CONSTRAINT CK_diplomados_cupo
        CHECK (cupo_maximo > 0),

    CONSTRAINT CK_diplomados_costo
        CHECK (costo >= 0),

    CONSTRAINT CK_diplomados_fechas
        CHECK (fecha_fin >= fecha_inicio)
);
GO

-- =====================================================
-- 9. INSCRIPCIONES
-- Cada inscripción debe tener un curso O un diplomado.
-- =====================================================

CREATE TABLE dbo.inscripciones (
    id_inscripcion BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    id_curso BIGINT NULL,
    id_diplomado BIGINT NULL,
    fecha_inscripcion DATETIME2 NOT NULL,
    estado VARCHAR(30) NOT NULL,
    activo BIT NOT NULL DEFAULT 1,

    CONSTRAINT FK_inscripciones_clientes
        FOREIGN KEY (id_cliente)
        REFERENCES dbo.clientes(id_cliente),

    CONSTRAINT FK_inscripciones_cursos
        FOREIGN KEY (id_curso)
        REFERENCES dbo.cursos(id_curso),

    CONSTRAINT FK_inscripciones_diplomados
        FOREIGN KEY (id_diplomado)
        REFERENCES dbo.diplomados(id_diplomado),

    CONSTRAINT CK_inscripciones_un_solo_programa
        CHECK (
            (id_curso IS NOT NULL AND id_diplomado IS NULL)
            OR
            (id_curso IS NULL AND id_diplomado IS NOT NULL)
        ),

    CONSTRAINT CK_inscripciones_estado
        CHECK (
            estado IN (
                'PENDIENTE',
                'CONFIRMADA',
                'CANCELADA',
                'FINALIZADA'
            )
        )
);
GO

-- =====================================================
-- 10. ESPACIOS
-- =====================================================

CREATE TABLE dbo.espacios (
    id_espacio BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(MAX) NULL,
    capacidad INT NOT NULL,
    tipo_espacio VARCHAR(50) NOT NULL,
    activo BIT NOT NULL DEFAULT 1,

    CONSTRAINT CK_espacios_capacidad
        CHECK (capacidad > 0),

    CONSTRAINT CK_espacios_tipo
        CHECK (
            tipo_espacio IN (
                'AUDITORIO',
                'SALA_REUNIONES',
                'LABORATORIO',
                'AULA',
                'SALA_MULTIMEDIA'
            )
        )
);
GO

-- =====================================================
-- 11. EQUIPAMIENTOS
-- =====================================================

CREATE TABLE dbo.equipamientos (
    id_equipamiento BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NULL
);
GO

-- =====================================================
-- 12. RELACIÓN ENTRE ESPACIOS Y EQUIPAMIENTOS
-- =====================================================

CREATE TABLE dbo.espacio_equipamiento (
    id_espacio BIGINT NOT NULL,
    id_equipamiento BIGINT NOT NULL,

    CONSTRAINT PK_espacio_equipamiento
        PRIMARY KEY (id_espacio, id_equipamiento),

    CONSTRAINT FK_espacio_equipamiento_espacios
        FOREIGN KEY (id_espacio)
        REFERENCES dbo.espacios(id_espacio),

    CONSTRAINT FK_espacio_equipamiento_equipamientos
        FOREIGN KEY (id_equipamiento)
        REFERENCES dbo.equipamientos(id_equipamiento)
);
GO

-- =====================================================
-- 13. SERVICIOS DE CATERING
-- =====================================================

CREATE TABLE dbo.servicios_catering (
    id_servicio BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(MAX) NULL,
    precio_persona DECIMAL(10,2) NOT NULL,
    activo BIT NOT NULL DEFAULT 1,

    CONSTRAINT CK_servicios_catering_precio
        CHECK (precio_persona >= 0)
);
GO

-- =====================================================
-- 14. COTIZACIONES
-- =====================================================

CREATE TABLE dbo.cotizaciones (
    id_cotizacion BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    fecha_cotizacion DATETIME2 NOT NULL,
    estado VARCHAR(30) NOT NULL,
    monto_total DECIMAL(12,2) NOT NULL,

    CONSTRAINT FK_cotizaciones_clientes
        FOREIGN KEY (id_cliente)
        REFERENCES dbo.clientes(id_cliente),

    CONSTRAINT CK_cotizaciones_total
        CHECK (monto_total >= 0),

    CONSTRAINT CK_cotizaciones_estado
        CHECK (
            estado IN (
                'PENDIENTE',
                'EN_PROCESO',
                'APROBADA',
                'RECHAZADA'
            )
        )
);
GO

-- =====================================================
-- 15. DETALLES DE COTIZACIÓN
-- =====================================================

CREATE TABLE dbo.detalle_cotizacion (
    id_detalle BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_cotizacion BIGINT NOT NULL,
    tipo_item VARCHAR(30) NOT NULL,
    id_referencia BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,

    CONSTRAINT FK_detalle_cotizacion_cotizaciones
        FOREIGN KEY (id_cotizacion)
        REFERENCES dbo.cotizaciones(id_cotizacion),

    CONSTRAINT CK_detalle_cotizacion_tipo
        CHECK (
            tipo_item IN (
                'CURSO',
                'DIPLOMADO',
                'ESPACIO',
                'CATERING'
            )
        ),

    CONSTRAINT CK_detalle_cotizacion_referencia
        CHECK (id_referencia > 0),

    CONSTRAINT CK_detalle_cotizacion_cantidad
        CHECK (cantidad > 0),

    CONSTRAINT CK_detalle_cotizacion_precio
        CHECK (precio_unitario >= 0),

    CONSTRAINT CK_detalle_cotizacion_subtotal
        CHECK (subtotal >= 0)
);
GO

-- =====================================================
-- 16. RESERVAS DE ESPACIOS
-- =====================================================

CREATE TABLE dbo.reservas_espacio (
    id_reserva BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_espacio BIGINT NOT NULL,
    id_cliente BIGINT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    motivo VARCHAR(255) NULL,
    monto_total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    fecha_registro DATETIME2 NOT NULL
        DEFAULT SYSDATETIME(),

    CONSTRAINT FK_reservas_espacio_espacios
        FOREIGN KEY (id_espacio)
        REFERENCES dbo.espacios(id_espacio),

    CONSTRAINT FK_reservas_espacio_clientes
        FOREIGN KEY (id_cliente)
        REFERENCES dbo.clientes(id_cliente),

    CONSTRAINT CK_reservas_espacio_fechas
        CHECK (fecha_fin >= fecha_inicio),

    CONSTRAINT CK_reservas_espacio_horas
        CHECK (hora_fin > hora_inicio),

    CONSTRAINT CK_reservas_espacio_monto
        CHECK (monto_total >= 0),

    CONSTRAINT CK_reservas_espacio_estado
        CHECK (
            estado IN (
                'PENDIENTE',
                'CONFIRMADA',
                'CANCELADA',
                'FINALIZADA'
            )
        )
);
GO

-- =====================================================
-- 17. SOLICITUDES DE CATERING
-- =====================================================

CREATE TABLE dbo.solicitudes_catering (
    id_solicitud BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    id_servicio BIGINT NOT NULL,
    numero_asistentes INT NOT NULL,
    menu VARCHAR(MAX) NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    lugar VARCHAR(150) NULL,
    estado VARCHAR(20) NOT NULL,
    precio_total DECIMAL(12,2) NOT NULL,
    id_reserva_espacio BIGINT NULL,
    id_cotizacion BIGINT NULL,
    fecha_registro DATETIME2 NOT NULL
        DEFAULT SYSDATETIME(),

    CONSTRAINT FK_solicitudes_catering_clientes
        FOREIGN KEY (id_cliente)
        REFERENCES dbo.clientes(id_cliente),

    CONSTRAINT FK_solicitudes_catering_servicios
        FOREIGN KEY (id_servicio)
        REFERENCES dbo.servicios_catering(id_servicio),

    CONSTRAINT FK_solicitudes_catering_reservas
        FOREIGN KEY (id_reserva_espacio)
        REFERENCES dbo.reservas_espacio(id_reserva),

    CONSTRAINT FK_solicitudes_catering_cotizaciones
        FOREIGN KEY (id_cotizacion)
        REFERENCES dbo.cotizaciones(id_cotizacion),

    CONSTRAINT CK_solicitudes_catering_asistentes
        CHECK (numero_asistentes > 0),

    CONSTRAINT CK_solicitudes_catering_precio
        CHECK (precio_total >= 0),

    CONSTRAINT CK_solicitudes_catering_estado
        CHECK (
            estado IN (
                'PENDIENTE',
                'CONFIRMADA',
                'CANCELADA',
                'FINALIZADA'
            )
        )
);
GO

-- =====================================================
-- 18. PAGOS
-- Cada pago corresponde a una inscripción O cotización.
-- =====================================================

CREATE TABLE dbo.pagos (
    id_pago BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_inscripcion BIGINT NULL,
    id_cotizacion BIGINT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha_pago DATETIME2 NOT NULL,
    metodo_pago VARCHAR(30) NOT NULL,
    estado VARCHAR(30) NOT NULL,

    CONSTRAINT FK_pagos_inscripciones
        FOREIGN KEY (id_inscripcion)
        REFERENCES dbo.inscripciones(id_inscripcion),

    CONSTRAINT FK_pagos_cotizaciones
        FOREIGN KEY (id_cotizacion)
        REFERENCES dbo.cotizaciones(id_cotizacion),

    CONSTRAINT CK_pagos_monto
        CHECK (monto >= 0),

    CONSTRAINT CK_pagos_origen
        CHECK (
            (id_inscripcion IS NOT NULL AND id_cotizacion IS NULL)
            OR
            (id_inscripcion IS NULL AND id_cotizacion IS NOT NULL)
        )
);
GO

-- =====================================================
-- 19. DATOS INICIALES
-- =====================================================

INSERT INTO dbo.roles (nombre)
VALUES
    ('ADMIN'),
    ('USER'),
    ('RECEPCIONISTA'),
    ('CLIENTE'),
    ('CONTABILIDAD');
GO

INSERT INTO dbo.categorias (nombre, descripcion, activo)
VALUES
    ('Tecnología', 'Cursos de desarrollo de software', 1),
    ('Negocios', 'Cursos de administración y finanzas', 1);
GO

INSERT INTO dbo.modalidades (nombre, descripcion)
VALUES
    ('PRESENCIAL', 'Clases en el campus'),
    ('VIRTUAL', 'Clases en modalidad virtual');
GO

-- =====================================================
-- 20. VERIFICACIÓN DE TABLAS
-- =====================================================

SELECT
    TABLE_SCHEMA,
    TABLE_NAME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;
GO

-- Aqui estamos actualizando la contraseña del admin (que ya existe en la bd)
-- en SET password es donde pegamos el hush generado
UPDATE dbo.usuarios
SET password = '$2a$10$y1Gc7wUjIIbol.YlKLzqPOdtRwmF7MmkoP9rqhGNDnXeyVNpikJlK'
WHERE username = 'admin';

-- Verificamos que se haya cambio en su contraseña
SELECT
    id_usuario,
    username,
    correo,
    password,
    activo,
    id_rol
FROM dbo.usuarios
WHERE username = 'admin';
GO