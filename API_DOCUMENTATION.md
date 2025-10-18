# Documentación de la API

## Autenticación

### Iniciar Sesión

**URL**: `/api/auth/signin`  
**Método**: `POST`  
**Descripción**: Autentica a un usuario y devuelve un token JWT

**Request Body**:
```json
{
  "username": "usuario",
  "password": "contraseña"
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Inicio de sesión exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "id": 1,
    "username": "usuario",
    "email": "usuario@example.com",
    "roles": ["ROLE_USER"]
  }
}
```

### Registrar Usuario

**URL**: `/api/auth/signup`  
**Método**: `POST`  
**Descripción**: Registra un nuevo usuario

**Request Body**:
```json
{
  "username": "nuevo_usuario",
  "email": "nuevo@example.com",
  "password": "contraseña",
  "role": ["user"]
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "id": 2,
    "username": "nuevo_usuario",
    "email": "nuevo@example.com",
    "roles": ["ROLE_USER"]
  }
}
```

## Roles

### Obtener todos los roles

**URL**: `/api/roles`  
**Método**: `GET`  
**Descripción**: Obtiene la lista de todos los roles disponibles

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Lista de roles obtenida exitosamente",
  "data": [
    {
      "id": 1,
      "name": "ROLE_ADMIN",
      "detail": "Administrador con acceso total al sistema"
    },
    {
      "id": 2,
      "name": "ROLE_USER",
      "detail": "Usuario estándar con acceso básico"
    },
    {
      "id": 3,
      "name": "PORTERO",
      "detail": "Jugador en la posición de portero"
    }
  ]
}
```

### Obtener rol por nombre

**URL**: `/api/roles/{name}`  
**Método**: `GET`  
**Descripción**: Obtiene un rol específico por su nombre

**Parámetros de ruta**:
- `name`: Nombre del rol (ej: ROLE_ADMIN, PORTERO)

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Rol encontrado exitosamente",
  "data": {
    "id": 1,
    "name": "ROLE_ADMIN",
    "detail": "Administrador con acceso total al sistema"
  }
}
```

**Response (404 Not Found)**:
```json
{
  "success": false,
  "message": "No se encontró el rol especificado",
  "error": "No encontrado",
  "status": 404
}
```

### Crear o actualizar rol

**URL**: `/api/roles`  
**Método**: `POST`  
**Descripción**: Crea un nuevo rol o actualiza uno existente

**Headers**:
- `Content-Type: application/json`
- `Authorization: Bearer <token>`

**Request Body**:
```json
{
  "name": "NUEVO_ROL",
  "detail": "Descripción del nuevo rol"
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Rol creado exitosamente",
  "data": {
    "id": 4,
    "name": "NUEVO_ROL",
    "detail": "Descripción del nuevo rol"
  }
}
```

### Crear o actualizar múltiples roles

**URL**: `/api/roles/bulk`  
**Método**: `POST`  
**Descripción**: Crea o actualiza múltiples roles a la vez

**Headers**:
- `Content-Type: application/json`
- `Authorization: Bearer <token>`

**Request Body**:
```json
{
  "roles": [
    {
      "name": "ROL_1",
      "detail": "Descripción del rol 1"
    },
    {
      "name": "ROL_2",
      "detail": "Descripción del rol 2"
    }
  ]
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Roles procesados exitosamente",
  "data": [
    {
      "id": 5,
      "name": "ROL_1",
      "detail": "Descripción del rol 1"
    },
    {
      "id": 6,
      "name": "ROL_2",
      "detail": "Descripción del rol 2"
    }
  ]
}
```

## Gestión de Categorías

### Obtener Todas las Categorías

**URL**: `/api/categorias`  
**Método**: `GET`  
**Roles Requeridos**: `ROLE_USER`, `ROLE_MODERATOR`, `ROLE_ADMIN`

**Response (200 OK)**:
```json
[
  {
    "categoriaId": 1,
    "nombre": "Electrónica"
  },
  {
    "categoriaId": 2,
    "nombre": "Ropa"
  }
]
```

### Obtener una Categoría por ID

**URL**: `/api/categorias/{id}`  
**Método**: `GET`  
**Roles Requeridos**: `ROLE_USER`, `ROLE_MODERATOR`, `ROLE_ADMIN`

**Response (200 OK)**:
```json
{
  "categoriaId": 1,
  "nombre": "Electrónica"
}
```

### Crear una Nueva Categoría

**URL**: `/api/categorias`  
**Método**: `POST`  
**Roles Requeridos**: `ROLE_MODERATOR`, `ROLE_ADMIN`

**Request Body**:
```json
{
  "nombre": "Electrodomésticos"
}
```

**Response (200 OK)**:
```json
{
  "categoriaId": 3,
  "nombre": "Electrodomésticos"
}
```

### Crear Múltiples Categorías

**URL**: `/api/categorias/bulk`  
**Método**: `POST`  
**Roles Requeridos**: `ROLE_ADMIN`

**Request Body**:
```json
{
  "categorias": [
    {"nombre": "Hogar"},
    {"nombre": "Deportes"},
    {"nombre": "Juguetes"}
  ]
}
```

**Response (200 OK)**:
```json
[
  {
    "categoriaId": 4,
    "nombre": "Hogar"
  },
  {
    "categoriaId": 5,
    "nombre": "Deportes"
  },
  {
    "categoriaId": 6,
    "nombre": "Juguetes"
  }
]
```

### Actualizar una Categoría

**URL**: `/api/categorias/{id}`  
**Método**: `PUT`  
**Roles Requeridos**: `ROLE_MODERATOR`, `ROLE_ADMIN`

**Request Body**:
```json
{
  "nombre": "Electrónica Actualizada"
}
```

**Response (200 OK)**:
```json
{
  "categoriaId": 1,
  "nombre": "Electrónica Actualizada"
}
```

### Eliminar una Categoría

**URL**: `/api/categorias/{id}`  
**Método**: `DELETE`  
**Roles Requeridos**: `ROLE_ADMIN`

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Categoría eliminada exitosamente",
  "data": null,
  "timestamp": "2025-10-18T14:30:00-05:00"
}
```

## Manejo de Errores

### Respuesta de Error Genérico

**Status Code**: 4xx/5xx  
**Response**:
```json
{
  "success": false,
  "message": "Mensaje descriptivo del error",
  "error": "Tipo de error",
  "status": 400,
  "timestamp": "2025-10-18T14:30:00-05:00"
}
```

### Códigos de Estado Comunes

- `200 OK`: Solicitud exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Error en la solicitud (validación, formato incorrecto, etc.)
- `401 Unauthorized`: No autenticado
- `403 Forbidden`: No autorizado para acceder al recurso
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error interno del servidor

## Autenticación

Todas las rutas (excepto `/api/auth/signin` y `/api/auth/signup`) requieren autenticación mediante JWT.

**Header de Autenticación**:
```
Authorization: Bearer <token_jwt>
```

## Ejemplo de Uso con cURL

### Iniciar Sesión
```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"username":"admin", "password":"admin123"}'
```

### Obtener Todos los Roles (Autenticado)
```bash
curl -X GET http://localhost:8080/api/roles \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
