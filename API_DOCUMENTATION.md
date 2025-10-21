# Documentación de la API

## Tabla de Contenidos
- [Categorías](#categorías)
  - [Obtener todas las categorías](#obtener-todas-las-categorías)
  - [Obtener categoría por ID](#obtener-categoría-por-id)
  - [Crear categoría](#crear-categoría)
  - [Crear múltiples categorías](#crear-múltiples-categorías)
  - [Actualizar categoría](#actualizar-categoría)
  - [Eliminar categoría](#eliminar-categoría)

## Categorías

### Obtener todas las categorías

**URL**: `/categorias`  
**Método**: `GET`  
**Descripción**: Obtiene una lista de todas las categorías disponibles  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_USER`, `ROLE_MODERATOR`, `ROLE_ADMIN`

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Categorías obtenidas exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Categoría 1"
    },
    {
      "id": 2,
      "nombre": "Categoría 2"
    }
  ],
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Obtener categoría por ID

**URL**: `/categorias/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene los detalles de una categoría específica por su ID  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_USER`, `ROLE_MODERATOR`, `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID de la categoría a consultar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Categoría obtenida exitosamente",
  "data": {
    "id": 1,
    "nombre": "Categoría 1"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Crear categoría

**URL**: `/categorias`  
**Método**: `POST`  
**Descripción**: Crea una nueva categoría  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_MODERATOR`, `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Nueva Categoría"
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Categoría creada exitosamente",
  "data": {
    "id": 3,
    "nombre": "Nueva Categoría"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Crear múltiples categorías

**URL**: `/categorias/bulk`  
**Método**: `POST`  
**Descripción**: Crea varias categorías en una sola petición  
**Autenticación Requerida**: Pública o `ROLE_ADMIN`  
**Roles**: `PUBLIC_ACCESS` o `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
```json
{
  "categorias": [
    {
      "nombre": "Categoría 1"
    },
    {
      "nombre": "Categoría 2"
    }
  ]
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Categorías creadas exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Categoría 1"
    },
    {
      "id": 2,
      "nombre": "Categoría 2"
    }
  ],
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Actualizar categoría

**URL**: `/categorias/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza una categoría existente  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_MODERATOR`, `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID de la categoría a actualizar

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Categoría Actualizada"
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Categoría actualizada exitosamente",
  "data": {
    "id": 1,
    "nombre": "Categoría Actualizada"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Eliminar categoría

**URL**: `/categorias/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina una categoría existente  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID de la categoría a eliminar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Categoría eliminada exitosamente",
  "data": null,
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```