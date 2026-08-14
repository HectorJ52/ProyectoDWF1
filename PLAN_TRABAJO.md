# Plan de Trabajo y Cronograma

## UCA-CFC Connect - Fase 1

---

## Distribución de Responsabilidades por Integrante

| Integrante | Nombre Completo | Código | Responsabilidad en Fase 1 |
|------------|-----------------|--------|---------------------------|
| Integrante 1 | Brandon Steven Reyes Reyes | RR241992 | Problemática, justificación y objetivos |
| Integrante 2 | Joaquín Enmanuel Ayala Orellana | AO250420 | Modelo de dominio (diagrama de clases) |
| Integrante 3 | Jonathan Manuel Valencia Ugarte | VU253065 | Modelo de base de datos (diagrama relacional) |
| Integrante 4 | Brandon Eliel Bernal Rodriguez | BR253266 | Mockups y flujos de procesos |
| Integrante 5 | Hector Gabriel Juarez Melgar | JM252298 | Endpoints REST, README, GitHub, plan de trabajo y cronograma |

---

## Módulos Asignados por Integrante (para Fase 2 y 3)

| Integrante | Nombre | Módulo Asignado |
|------------|--------|-----------------|
| Integrante 1 | Brandon Steven Reyes Reyes | Seguridad + Autenticación |
| Integrante 2 | Joaquín Enmanuel Ayala Orellana | Gestión Académica + Agenda |
| Integrante 3 | Jonathan Manuel Valencia Ugarte | Clientes + Inscripciones |
| Integrante 4 | Brandon Eliel Bernal Rodriguez | Cotizaciones + Pagos |
| Integrante 5 | Hector Gabriel Juarez Melgar | Espacios + Catering |

---

## Roles del Equipo

| Rol | Responsabilidad | Integrante |
|-----|-----------------|------------|
| Líder Técnico | Coordinación, revisión de código, integración | Integrante 1 (Brandon Steven Reyes Reyes) |
| QA / Pruebas | Validación de calidad | Integrante 2 (Joaquín Enmanuel Ayala Orellana) |
| Documentación | Documentación técnica | Integrante 3 (Jonathan Manuel Valencia Ugarte) |
| DevOps | Configuración de entorno, GitHub | Integrante 4 (Brandon Eliel Bernal Rodriguez) |
| UX/UI | Mockups y diseño | Integrante 5 (Hector Gabriel Juarez Melgar) |

---

## Cronograma de Actividades - Fase 1

| Semana | Fechas | Actividad | Responsable |
|--------|--------|-----------|-------------|
| 1 | 15 - 21 Julio | Análisis de requerimientos y definición de modelo de dominio | Todos |
| 2 | 22 - 28 Julio | Diseño de base de datos y diagrama relacional | Todos |
| 3 | 29 Jul - 4 Ago | Definición de endpoints REST y mockups | Todos |
| 4 | 5 - 11 Ago | Creación de repositorio GitHub y estructura inicial | Todos |
| 5 | 12 - 13 Ago | Revisión final y preparación de entregables | Todos |
| - | **14 Agosto** | **Entrega Fase 1** | Todos |

---

## Entregables por Integrante - Fase 1

| Integrante | Nombre | Entregable |
|------------|--------|------------|
| Integrante 1 | Brandon Steven Reyes Reyes | Documento: Problemática, justificación y objetivos |
| Integrante 2 | Joaquín Enmanuel Ayala Orellana | Diagrama de clases (Modelo de dominio) |
| Integrante 3 | Jonathan Manuel Valencia Ugarte | Diagrama relacional (Modelo de base de datos) |
| Integrante 4 | Brandon Eliel Bernal Rodriguez | Mockups de interfaces y flujos de procesos |
| Integrante 5 | Hector Gabriel Juarez Melgar | Endpoints REST, README, GitHub, Plan de trabajo y cronograma |

---

## Herramientas a Utilizar

| Herramienta | Propósito |
|-------------|-----------|
| GitHub | Control de versiones y colaboración |
| Spring Boot | Desarrollo del backend |
| MySQL / PostgreSQL | Base de datos |
| Swagger | Documentación de API |
| Maven | Gestión de dependencias |
| JUnit / Mockito | Pruebas unitarias |
| Docker | Contenerización (opcional) |

---

## Ramas de GitHub
```
main
└── develop
    ├── feature/auth
    ├── feature/gestion-academica
    ├── feature/clientes
    ├── feature/inscripciones
    ├── feature/cotizaciones
    ├── feature/espacios
    ├── feature/catering
    ├── feature/agenda
    ├── feature/pagos
    └── feature/usuarios
```

---

## Flujo de Trabajo en GitHub

1. Cada integrante trabaja en su rama `feature/`
2. Realiza commits frecuentes con mensajes claros
3. Crea Pull Requests a la rama `develop`
4. Revisión de código por al menos un compañero
5. Merge a `develop` después de aprobación
6. Merge a `main` solo para entregables oficiales
