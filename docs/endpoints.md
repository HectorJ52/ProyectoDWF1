# Endpoints REST - UCA-CFC Connect

## Convenciones Generales

**Base URL:** `http://localhost:8080/api`

**Parámetros comunes para listados:**
- `?page=0&size=10` - Paginación
- `?sort=campo,asc|desc` - Ordenamiento
- `?campo=valor` - Filtros

**Roles del Sistema:**
- `ADMIN` - Configuración completa
- `RECEPCIONISTA` - Operaciones diarias
- `CLIENTE` - Registro y consultas
- `CONTABILIDAD` - Gestión de pagos
-
-   Módulo de Autenticación y Seguridad

| Método | Endpoint | Descripción | Roles Permitidos |
|--------|----------|-------------|------------------|
| POST | `/api/auth/login` | Iniciar sesión | Todos |
| POST | `/api/auth/logout` | Cerrar sesión | Todos |
| POST | `/api/auth/register` | Registro de usuario (CLIENTE) | Público |
| POST | `/api/auth/recover-password` | Recuperación de contraseña | Público |

Request Body - Login:
```json
{
  "username": "admin",
  "password": "Admin123"
}
