# Documentación de la API

## Tabla de Contenidos

- [Autenticación](#autenticación)
  - [Iniciar sesión](#iniciar-sesión)
  - [Registrar usuario](#registrar-usuario)
  - [Refrescar token](#refrescar-token)

- [Encuentros](#encuentros)
  - [Crear un encuentro](#crear-un-encuentro)
  - [Crear múltiples encuentros](#crear-múltiples-encuentros)
  - [Obtener todos los encuentros](#obtener-todos-los-encuentros)
  - [Obtener encuentro por ID](#obtener-encuentro-por-id)
  - [Obtener encuentros por subcategoría](#obtener-encuentros-por-subcategoría)
  - [Buscar encuentros (POST)](#buscar-encuentros-post)
  - [Buscar encuentros con parámetros de consulta](#buscar-encuentros-con-parámetros-de-consulta)
  - [Obtener encuentros por equipo](#obtener-encuentros-por-equipo)
  - [Actualizar encuentro](#actualizar-encuentro)
  - [Eliminar encuentro](#eliminar-encuentro)

- [Estadios](#estadios)
  - [Obtener todos los estadios](#obtener-todos-los-estadios)
  - [Obtener estadio por ID](#obtener-estadio-por-id)
  - [Crear estadio](#crear-estadio)
  - [Actualizar estadio](#actualizar-estadio)
  - [Eliminar estadio](#eliminar-estadio)
  - [Crear múltiples estadios](#crear-múltiples-estadios)

- [Generación de Encuentros](#generación-de-encuentros)
  - [Generar encuentros](#generar-encuentros)

- [Categorías](#categorías)
  - [Obtener todas las categorías](#obtener-todas-las-categorías)
  - [Obtener categoría por ID](#obtener-categoría-por-id)
  - [Obtener categoría por nemonico](#obtener-categoría-por-nemonico)
  - [Crear categoría](#crear-categoría)
  - [Crear múltiples categorías](#crear-múltiples-categorías)
  - [Actualizar categoría](#actualizar-categoría)
  - [Eliminar categoría](#eliminar-categoría)
- [Subcategorías](#subcategorías)
  - [Obtener todas las subcategorías](#obtener-todas-las-subcategorías)
  - [Obtener subcategoría por ID](#obtener-subcategoría-por-id)
  - [Obtener subcategorías por categoría](#obtener-subcategorías-por-categoría)
  - [Obtener próximos eventos](#obtener-próximos-eventos)
  - [Obtener eventos pasados](#obtener-eventos-pasados)
  - [Crear subcategoría](#crear-subcategoría)
  - [Crear múltiples subcategorías](#crear-múltiples-subcategorías)
  - [Actualizar subcategoría](#actualizar-subcategoría)
  - [Eliminar subcategoría](#eliminar-subcategoría)
- [Gestión de Roles por Subcategoría](#gestión-de-roles-por-subcategoría)
  - [Asignar un Rol a una Subcategoría](#asignar-un-rol-a-una-subcategoría)
  - [Eliminar un Rol de una Subcategoría](#eliminar-un-rol-de-una-subcategoría)
  - [Obtener Roles por ID de Subcategoría](#obtener-roles-por-id-de-subcategoría)
  - [Actualizar Relación Subcategoría-Rol](#actualizar-relación-subcategoría-rol)
  - [Obtener Roles por Nombre de Subcategoría](#obtener-roles-por-nombre-de-subcategoría)
  - [Asignar Múltiples Roles a una Subcategoría](#asignar-múltiples-roles-a-una-subcategoría-bulk)
- [Gestión de Roles](#gestión-de-roles)
  - [Crear o Actualizar un Rol](#crear-o-actualizar-un-rol)
  - [Crear o Actualizar Múltiples Roles](#crear-o-actualizar-múltiples-roles-bulk)
  - [Obtener Todos los Roles](#obtener-todos-los-roles)
  - [Obtener Rol por Nombre](#obtener-rol-por-nombre)
- [Sanciones](#sanciones)
  - [Obtener todas las sanciones](#obtener-todas-las-sanciones)
  - [Obtener sanción por ID](#obtener-sanción-por-id)
  - [Crear sanción](#crear-sanción)
  - [Actualizar sanción](#actualizar-sanción)
  - [Eliminar sanción](#eliminar-sanción)
  - [Obtener sanciones por jugador](#obtener-sanciones-por-jugador)
  - [Obtener sanciones por encuentro](#obtener-sanciones-por-encuentro)
  - [Obtener sanciones por tipo](#obtener-sanciones-por-tipo)
- [Series](#series)
  - [Obtener series por subcategoría](#obtener-series-por-subcategoría)
  - [Obtener serie por ID](#obtener-serie-por-id)
  - [Crear serie](#crear-serie)
  - [Crear múltiples series](#crear-múltiples-series)
  - [Actualizar serie](#actualizar-serie)
  - [Eliminar serie](#eliminar-serie)
- [Equipos](#equipos)
  - [Obtener todos los equipos](#obtener-todos-los-equipos)
  - [Obtener equipos por serie](#obtener-equipos-por-serie)
  - [Obtener equipos por subcategoría](#obtener-equipos-por-subcategoría)
  - [Obtener equipo por ID](#obtener-equipo-por-id)
  - [Crear equipo](#crear-equipo)
  - [Crear múltiples equipos](#crear-múltiples-equipos)
  - [Actualizar equipo](#actualizar-equipo)
  - [Eliminar equipo](#eliminar-equipo)
  - [Verificar existencia de equipos](#verificar-existencia-de-equipos)
  - [Contar equipos](#contar-equipos)
- [Jugadores](#jugadores)
  - [Obtener todos los jugadores](#obtener-todos-los-jugadores)
  - [Contar jugadores activos](#contar-jugadores-activos)
  - [Obtener jugador por ID](#obtener-jugador-por-id)
  - [Crear jugador](#crear-jugador)
  - [Actualizar jugador](#actualizar-jugador)
  - [Eliminar jugador](#eliminar-jugador)
  - [Crear múltiples jugadores](#crear-múltiples-jugadores)
- [Verificación](#verificación)
  - [Verificar existencia de registros](#verificar-existencia-de-registros)

## Autenticación

### Iniciar sesión

**URL**: `/auth/signin`  
**Método**: `POST`  
**Descripción**: Autentica a un usuario y devuelve un token JWT  
**Autenticación Requerida**: No  
**Roles**: No aplica

**Cuerpo de la Solicitud**:
```json
{
  "username": "usuario_ejemplo",
  "password": "contraseña123"
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Usuario autenticado exitosamente",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "id": 1,
    "username": "usuario_ejemplo",
    "email": "usuario@ejemplo.com",
    "roles": ["ROLE_USER"],
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "timestamp": "2025-10-22T10:30:00.000+00:00"
}
```

**Errores**:
- `400 Bad Request`: Credenciales inválidas

### Registrar usuario

**URL**: `/auth/signup`  
**Método**: `POST`  
**Descripción**: Registra un nuevo usuario en el sistema  
**Autenticación Requerida**: No  
**Roles**: No aplica

**Cuerpo de la Solicitud**:
```json
{
  "username": "nuevo_usuario",
  "email": "nuevo@usuario.com",
  "password": "contraseña123",
  "role": ["user"]
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "¡Usuario registrado exitosamente!",
  "data": null,
  "timestamp": "2025-10-22T10:30:00.000+00:00"
}
```

**Errores**:
- `400 Bad Request`: Nombre de usuario o correo electrónico ya en uso

### Refrescar token

**URL**: `/auth/refreshtoken`  
**Método**: `POST`  
**Descripción**: Refresca el token JWT del usuario autenticado  
**Autenticación Requerida**: Sí (se requiere un token JWT válido)  
**Roles**: Cualquier rol autenticado

**Cabeceras Requeridas**:
- `Authorization: Bearer <token>`

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "token": "nuevo.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "id": 1,
    "username": "usuario_ejemplo",
    "email": "usuario@ejemplo.com",
    "roles": ["ROLE_USER"],
    "refreshToken": "nuevo.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "timestamp": "2025-10-22T10:35:00.000+00:00"
}
```

**Errores**:
- `400 Bad Request`: No se pudo refrescar el token
- `401 Unauthorized`: Token no proporcionado o inválido

## Encuentros

### Crear un encuentro

**URL**: `/encuentros`  
**Método**: `POST`  
**Descripción**: Crea un nuevo encuentro  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`, `ROLE_MODERATOR`

**Cuerpo de la Solicitud**:
```json
{
  "fechaHora": "2025-12-31T20:00:00",
  "estadioLugar": "Estadio Principal",
  "subcategoriaId": 1,
  "equipoLocalId": 1,
  "equipoVisitanteId": 2,
  "estado": "PENDIENTE"
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Encuentro creado exitosamente",
  "data": {
    "id": 1,
    "fechaHora": "2025-12-31T20:00:00",
    "estadioLugar": "Estadio Principal",
    "estado": "PENDIENTE",
    "subcategoriaId": 1,
    "equipoLocalId": 1,
    "equipoVisitanteId": 2
  }
}
```

### Crear múltiples encuentros

**URL**: `/encuentros/bulk`  
**Método**: `POST`  
**Descripción**: Crea múltiples encuentros en una sola operación  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`, `ROLE_MODERATOR`

**Cuerpo de la Solicitud**:
```json
[
  {
    "fechaHora": "2025-12-31T20:00:00",
    "estadioLugar": "Estadio Principal",
    "subcategoriaId": 1,
    "equipoLocalId": 1,
    "equipoVisitanteId": 2,
    "estado": "PENDIENTE"
  },
  {
    "fechaHora": "2026-01-07T20:00:00",
    "estadioLugar": "Estadio Secundario",
    "subcategoriaId": 1,
    "equipoLocalId": 3,
    "equipoVisitanteId": 4,
    "estado": "PENDIENTE"
  }
]
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Encuentros creados exitosamente",
  "data": [
    {
      "id": 1,
      "fechaHora": "2025-12-31T20:00:00",
      "estadioLugar": "Estadio Principal",
      "estado": "PENDIENTE",
      "subcategoriaId": 1,
      "equipoLocalId": 1,
      "equipoVisitanteId": 2
    },
    {
      "id": 2,
      "fechaHora": "2026-01-07T20:00:00",
      "estadioLugar": "Estadio Secundario",
      "estado": "PENDIENTE",
      "subcategoriaId": 1,
      "equipoLocalId": 3,
      "equipoVisitanteId": 4
    }
  ]
}
```

### Obtener todos los encuentros

**URL**: `/encuentros`  
**Método**: `GET`  
**Descripción**: Obtiene una lista de todos los encuentros  
**Autenticación Requerida**: No

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Lista de encuentros obtenida exitosamente",
  "data": [
    {
      "id": 1,
      "fechaHora": "2025-12-31T20:00:00",
      "estadioLugar": "Estadio Principal",
      "estado": "PENDIENTE",
      "subcategoriaId": 1,
      "equipoLocalId": 1,
      "equipoVisitanteId": 2
    }
  ]
}
```

### Obtener encuentro por ID

**URL**: `/encuentros/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene un encuentro específico por su ID  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `id` (requerido): ID del encuentro

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Encuentro obtenido exitosamente",
  "data": {
    "id": 1,
    "fechaHora": "2025-12-31T20:00:00",
    "estadioLugar": "Estadio Principal",
    "estado": "PENDIENTE",
    "subcategoriaId": 1,
    "equipoLocalId": 1,
    "equipoVisitanteId": 2
  }
}
```

### Obtener encuentros por subcategoría

**URL**: `/encuentros/subcategoria/{subcategoriaId}`  
**Método**: `GET`  
**Descripción**: Obtiene todos los encuentros de una subcategoría específica  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `subcategoriaId` (requerido): ID de la subcategoría

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Encuentros por subcategoría obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "fechaHora": "2025-12-31T20:00:00",
      "estadioLugar": "Estadio Principal",
      "estado": "PENDIENTE",
      "subcategoriaId": 1,
      "equipoLocalId": 1,
      "equipoVisitanteId": 2
    }
  ]
}
```

### Buscar encuentros (POST)

**URL**: `/encuentros/search`  
**Método**: `POST`  
**Descripción**: Busca encuentros según criterios específicos  
**Autenticación Requerida**: No

**Cuerpo de la Solicitud**:
```json
{
  "titulo": "Partido",
  "fechaInicio": "2025-12-01",
  "fechaFin": "2025-12-31",
  "subcategoriaId": 1,
  "equipoId": 1,
  "estadioLugar": "Principal"
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Búsqueda de encuentros completada exitosamente",
  "data": {
    "content": [
      {
        "id": 1,
        "fechaHora": "2025-12-31T20:00:00",
        "estadioLugar": "Estadio Principal",
        "estado": "PENDIENTE",
        "subcategoriaId": 1,
        "equipoLocalId": 1,
        "equipoVisitanteId": 2
      }
    ],
    "pageable": {
      "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
      },
      "pageNumber": 0,
      "pageSize": 10,
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "first": true,
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "numberOfElements": 1,
    "size": 10,
    "number": 0,
    "empty": false
  }
}
```

### Buscar encuentros con parámetros de consulta

**URL**: `/encuentros/search/params`  
**Método**: `GET`  
**Descripción**: Busca encuentros usando parámetros de consulta  
**Autenticación Requerida**: No

**Parámetros de Consulta**:
- `subcategoriaId` (opcional): ID de la subcategoría
- `fechaInicio` (opcional): Fecha de inicio (formato ISO 8601)
- `fechaFin` (opcional): Fecha de fin (formato ISO 8601)
- `estadioLugar` (opcional): Nombre o parte del nombre del estadio
- `estado` (opcional): Estado del encuentro
- `equipoId` (opcional): ID del equipo (local o visitante)
- `page` (opcional): Número de página (por defecto 0)
- `size` (opcional): Tamaño de página (por defecto 10)

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Búsqueda de encuentros completada exitosamente",
  "data": {
    "content": [
      {
        "id": 2,
        "subcategoriaId": 5,
        "subcategoriaNombre": "Fútbol",
        "titulo": "Equipo B vs Equipo C",
        "fechaHora": "2025-10-31T08:00:00",
        "estadioNombre": "PEGUCHE",
        "estado": "PROGRAMADO",
        "activo": true
      }
    ],
    "pageable": {
      "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
      },
      "pageNumber": 0,
      "pageSize": 10,
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "first": true,
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "numberOfElements": 1,
    "size": 10,
    "number": 0,
    "empty": false
  }
}
```

### Obtener encuentros por equipo

**URL**: `/encuentros/equipo/{equipoId}`  
**Método**: `GET`  
**Descripción**: Obtiene todos los encuentros en los que participa un equipo específico  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `equipoId` (requerido): ID del equipo

**Parámetros de Consulta**:
- `page` (opcional): Número de página (por defecto 0)
- `size` (opcional): Tamaño de página (por defecto 10)

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Encuentros del equipo obtenidos exitosamente",
  "data": {
    "content": [
      {
        "id": 1,
        "fechaHora": "2025-12-31T20:00:00",
        "estadioLugar": "Estadio Principal",
        "estado": "PENDIENTE",
        "subcategoriaId": 1,
        "equipoLocalId": 1,
        "equipoVisitanteId": 2
      }
    ],
    "pageable": {
      "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
      },
      "pageNumber": 0,
      "pageSize": 10,
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "first": true,
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "numberOfElements": 1,
    "size": 10,
    "number": 0,
    "empty": false
  }
}
```

### Actualizar encuentro

**URL**: `/encuentros/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza un encuentro existente  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`, `ROLE_MODERATOR`

**Parámetros de Ruta**:
- `id` (requerido): ID del encuentro a actualizar

**Cuerpo de la Solicitud**:
```json
{
  "fechaHora": "2025-12-31T21:00:00",
  "estadioLugar": "Estadio Principal Actualizado",
  "estado": "EN_JUEGO",
  "subcategoriaId": 1,
  "equipoLocalId": 1,
  "equipoVisitanteId": 2
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Encuentro actualizado exitosamente",
  "data": {
    "id": 1,
    "fechaHora": "2025-12-31T21:00:00",
    "estadioLugar": "Estadio Principal Actualizado",
    "estado": "EN_JUEGO",
    "subcategoriaId": 1,
    "equipoLocalId": 1,
    "equipoVisitanteId": 2
  }
}
```

### Eliminar encuentro

**URL**: `/encuentros/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina un encuentro existente  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`, `ROLE_MODERATOR`

**Parámetros de Ruta**:
- `id` (requerido): ID del encuentro a eliminar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Encuentro eliminado exitosamente",
  "data": null
}
```
## Generación de Encuentros

### Generar encuentros

**URL**: `/generacion-encuentros`  
**Método**: `POST`  
**Descripción**: Genera encuentros automáticamente según los parámetros proporcionados  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
```json
{
  "subcategoriaId": 5,
  "tipoGeneracion": "SELECCION_MANUAL",
  "encuentrosManuales": [
    {
      "equipoLocalId": 2,
      "equipoVisitanteId": 3,
      "fecha": "2025-10-31",
      "hora": "08:00",
      "estadioId": 1
    }
  ]
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Encuentros generados exitosamente",
  "data": [
    {
      "id": 1192,
      "titulo": "Equipo B vs Equipo C",
      "fechaHora": "2025-10-31T08:00:00",
      "estadioLugar": "PEGUCHE",
      "estado": "PROGRAMADO",
      "subcategoriaId": 5,
      "subcategoriaNombre": "Fútbol"
    }
  ],
  "timestamp": "2025-10-31T09:22:45.057755600"
}
```

**Códigos de Error**:
- `400 Bad Request`: Si los parámetros de entrada no son válidos
- `401 Unauthorized`: Si no se proporciona un token JWT válido
- `403 Forbidden`: Si el usuario no tiene permisos de administrador
- `404 Not Found`: Si la subcategoría especificada no existe
- `500 Internal Server Error`: Si ocurre un error al generar los encuentros


### Códigos de Estado HTTP

- `200 OK`: La solicitud se completó exitosamente
- `400 Bad Request`: Error en los datos de la solicitud
- `401 Unauthorized`: No autorizado (token no proporcionado o inválido)
- `403 Forbidden`: No tiene permisos para realizar esta acción
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error interno del servidor

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
**Autenticación Requerida**: No  
**Roles**: No aplica

**Parámetros de Ruta**:
- `id` (requerido): ID de la categoría a consultar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Categoría obtenida exitosamente",
  "data": {
    "id": 1,
    "nombre": "Categoría 1",
    "nemonico": "CATEGORIA_1",
    "estado": true
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Obtener categoría por nemonico

**URL**: `/categorias/nemonico/{nemonico}`  
**Método**: `GET`  
**Descripción**: Obtiene una categoría por su nemonico. Soporta búsqueda flexible de singular/plural (ej: "EVENTO" o "EVENTOS")  
**Autenticación Requerida**: No  
**Roles**: No aplica

**Parámetros de Ruta**:
- `nemonico` (requerido): Nemonico de la categoría (ej: "EVENTO", "EVENTOS", "DEPORTE", "DEPORTES")

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Categoría obtenida exitosamente",
  "data": {
    "id": 2,
    "nombre": "Eventos",
    "nemonico": "EVENTOS",
    "estado": true
  }
}
```

**Errores**:
- `404 Not Found`: No se encontró la categoría con el nemonico especificado

### Obtener próximos eventos

**URL**: `/subcategorias/eventos/proximos`  
**Método**: `GET`  
**Descripción**: Obtiene una lista de los próximos eventos (subcategorías con proximo = true) de la categoría de eventos  
**Autenticación Requerida**: No

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Próximos eventos obtenidos correctamente",
  "data": [
    {
      "id": 1,
      "nombre": "Torneo de Fútbol 2023",
      "descripcion": "Torneo anual de fútbol",
      "fechaHora": "2023-12-15T15:00:00",
      "proximo": true,
      "categoriaId": 1
    },
    {
      "id": 2,
      "nombre": "Torneo de Voleibol 2023",
      "descripcion": "Torneo de voleibol playa",
      "fechaHora": "2023-12-20T16:30:00",
      "proximo": true,
      "categoriaId": 1
    }
  ],
  "timestamp": "2023-11-05T10:00:00.000+00:00"
}
```

**Errores**:
- `404 Not Found`: No se encontró la categoría de eventos
- `500 Internal Server Error`: Error al procesar la solicitud

### Obtener eventos pasados

**URL**: `/subcategorias/eventos/pasados`  
**Método**: `GET`  
**Descripción**: Obtiene una lista de los eventos pasados (subcategorías con proximo = false) de la categoría de eventos  
**Autenticación Requerida**: No

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Eventos pasados obtenidos correctamente",
  "data": [
    {
      "id": 3,
      "nombre": "Torneo de Baloncesto 2022",
      "descripcion": "Torneo de baloncesto intercolegial",
      "fechaHora": "2022-11-10T14:00:00",
      "proximo": false,
      "categoriaId": 1
    },
    {
      "id": 4,
      "nombre": "Torneo de Tenis 2022",
      "descripcion": "Torneo de tenis individual",
      "fechaHora": "2022-10-05T09:30:00",
      "proximo": false,
      "categoriaId": 1
    }
  ],
  "timestamp": "2023-11-05T10:05:00.000+00:00"
}
```

**Errores**:
- `404 Not Found`: No se encontró la categoría de eventos
- `500 Internal Server Error`: Error al procesar la solicitud

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

## Subcategorías

### Obtener todas las subcategorías

**URL**: `/subcategorias`  
**Método**: `GET`  
**Descripción**: Obtiene una lista de todas las subcategorías disponibles  
**Autenticación Requerida**: No

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Lista de subcategorías obtenida exitosamente",
  "data": [
    {
      "subcategoriaId": 1,
      "nombre": "Fútbol",
      "descripcion": "Torneos de fútbol",
      "categoriaId": 1,
      "categoriaNombre": "Deportes",
      "fechaHora": ""
    },
    {
      "subcategoriaId": 2,
      "nombre": "Básquet",
      "descripcion": "Torneos de básquet",
      "categoriaId": 1,
      "categoriaNombre": "Deportes",
      "fechaHora": ""
    },
    {
      "subcategoriaId": 3,
      "nombre": "Runakay",
      "descripcion": "Concierto runakay",
      "categoriaId": 2,
      "categoriaNombre": "Eventos",
      "fechaHora": "2025-12-31T20:00:00"
    }
  ],
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Obtener subcategoría por ID

**URL**: `/subcategorias/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene los detalles de una subcategoría específica por su ID  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `id` (requerido): ID de la subcategoría a consultar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Subcategoría encontrada exitosamente",
  "data": {
    "subcategoriaId": 1,
    "nombre": "Fútbol",
    "descripcion": "Torneos de fútbol",
    "categoriaId": 1,
    "categoriaNombre": "Deportes",
    "fechaHora": "2025-12-31T20:00:00"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Obtener subcategorías por categoría

**URL**: `/subcategorias/categoria/{categoriaId}`  
**Método**: `GET`  
**Descripción**: Obtiene todas las subcategorías asociadas a una categoría específica  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `categoriaId` (requerido): ID de la categoría

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Subcategorías por categoría obtenidas exitosamente",
  "data": [
    {
      "subcategoriaId": 1,
      "nombre": "Fútbol",
      "descripcion": "Torneos de fútbol",
      "categoriaId": 1,
      "categoriaNombre": "Deportes"
    },
    {
      "subcategoriaId": 2,
      "nombre": "Básquet",
      "descripcion": "Torneos de básquet",
      "categoriaId": 1,
      "categoriaNombre": "Deportes"
    }
  ],
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Crear subcategoría

**URL**: `/subcategorias`  
**Método**: `POST`  
**Descripción**: Crea una nueva subcategoría  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Nueva Subcategoría",
  "descripcion": "Descripción de la subcategoría",
  "categoriaId": 1
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Subcategoría creada exitosamente",
  "data": {
    "subcategoriaId": 3,
    "nombre": "Nueva Subcategoría",
    "descripcion": "Descripción de la subcategoría",
    "categoriaId": 1,
    "categoriaNombre": "Deportes"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Crear múltiples subcategorías

**URL**: `/subcategorias/bulk`  
**Método**: `POST`  
**Descripción**: Crea varias subcategorías en una sola petición  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
```json
{
  "subcategorias": [
    {
      "nombre": "Fútbol",
      "descripcion": "Torneos de fútbol",
      "categoriaId": 1
    },
    {
      "nombre": "Básquet",
      "descripcion": "Torneos de básquet",
      "categoriaId": 1
    }
  ]
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Subcategorías creadas exitosamente",
  "data": [
    {
      "subcategoriaId": 1,
      "nombre": "Fútbol",
      "descripcion": "Torneos de fútbol",
      "categoriaId": 1,
      "categoriaNombre": "Deportes"
    },
    {
      "subcategoriaId": 2,
      "nombre": "Básquet",
      "descripcion": "Torneos de básquet",
      "categoriaId": 1,
      "categoriaNombre": "Deportes"
    }
  ],
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Actualizar subcategoría

**URL**: `/subcategorias/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza una subcategoría existente  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID de la subcategoría a actualizar

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Fútbol Actualizado",
  "descripcion": "Torneos de fútbol actualizado",
  "categoriaId": 1
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Subcategoría actualizada exitosamente",
  "data": {
    "subcategoriaId": 1,
    "nombre": "Fútbol Actualizado",
    "descripcion": "Torneos de fútbol actualizado",
    "categoriaId": 1,
    "categoriaNombre": "Deportes"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Eliminar subcategoría

**URL**: `/subcategorias/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina una subcategoría existente  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID de la subcategoría a eliminar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Subcategoría eliminada exitosamente",
  "data": null,
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

## Series

### Crear serie

**URL**: `/series`  
**Método**: `POST`  
**Descripción**: Crea una nueva serie  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Serie A",
  "descripcion": "Descripción de la serie A",
  "subcategoriaId": 1
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Serie creada exitosamente",
  "data": {
    "id": 1,
    "nombre": "Serie A",
    "descripcion": "Descripción de la serie A",
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Crear múltiples series

**URL**: `/series/bulk`  
**Método**: `POST`  
**Descripción**: Crea varias series en una sola petición  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
```json
{
  "series": [
    {
      "nombre": "Serie A",
      "descripcion": "Descripción de la serie A",
      "subcategoriaId": 1
    },
    {
      "nombre": "Serie B",
      "descripcion": "Descripción de la serie B",
      "subcategoriaId": 1
    }
  ]
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Series creadas exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Serie A",
      "descripcion": "Descripción de la serie A",
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol"
    },
    {
      "id": 2,
      "nombre": "Serie B",
      "descripcion": "Descripción de la serie B",
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol"
    }
  ],
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Obtener series por subcategoría

**URL**: `/series/subcategoria/{subcategoriaId}`  
**Método**: `GET`  
**Descripción**: Obtiene todas las series asociadas a una subcategoría específica  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `subcategoriaId` (requerido): ID de la subcategoría

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Series obtenidas exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Serie A",
      "descripcion": "Descripción de la serie A",
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol"
    },
    {
      "id": 2,
      "nombre": "Serie B",
      "descripcion": "Descripción de la serie B",
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol"
    }
  ],
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Obtener serie por ID

**URL**: `/series/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene los detalles de una serie específica por su ID  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `id` (requerido): ID de la serie a consultar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Serie obtenida exitosamente",
  "data": {
    "id": 1,
    "nombre": "Serie A",
    "descripcion": "Descripción de la serie A",
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Actualizar serie

**URL**: `/series/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza una serie existente  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID de la serie a actualizar

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Serie A Actualizada",
  "descripcion": "Descripción actualizada de la serie A",
  "subcategoriaId": 1
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Serie actualizada exitosamente",
  "data": {
    "id": 1,
    "nombre": "Serie A Actualizada",
    "descripcion": "Descripción actualizada de la serie A",
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Eliminar serie

**URL**: `/series/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina una serie existente  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID de la serie a eliminar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Serie eliminada exitosamente",
  "data": null,
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

## Equipos

### Obtener todos los equipos

**URL**: `/equipos`  
**Método**: `GET`  
**Descripción**: Obtiene una lista paginada de todos los equipos con opciones de búsqueda y ordenamiento  
**Autenticación Requerida**: No

**Parámetros de Consulta**:
- `page` (opcional, predeterminado: 0): Número de página (0-based)
- `size` (opcional, predeterminado: 10): Tamaño de la página
- `sort` (opcional): Campo por el cual ordenar (ej: 'nombre', 'fechaCreacion,desc')
- `nombre` (opcional): Filtrar por nombre (búsqueda parcial)
- `search` (opcional): Término de búsqueda general

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Equipos obtenidos exitosamente",
  "data": {
    "content": [
      {
        "id": 1,
        "nombre": "Equipo A",
        "descripcion": "Descripción del equipo A",
        "serieId": 1,
        "serieNombre": "Serie A",
        "subcategoriaId": 1,
        "subcategoriaNombre": "Fútbol"
      }
    ],
    "pageable": {
      "sort": {
        "sorted": true,
        "unsorted": false,
        "empty": false
      },
      "pageNumber": 0,
      "pageSize": 10,
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "size": 10,
    "number": 0,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Obtener equipos por serie

**URL**: `/equipos/serie/{serieId}`  
**Método**: `GET`  
**Descripción**: Obtiene todos los equipos asociados a una serie específica  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `serieId` (requerido): ID de la serie

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Equipos obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Equipo A",
      "descripcion": "Descripción del equipo A",
      "serieId": 1,
      "serieNombre": "Serie A",
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol"
    },
    {
      "id": 2,
      "nombre": "Equipo B",
      "descripcion": "Descripción del equipo B",
      "serieId": 1,
      "serieNombre": "Serie A",
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol"
    }
  ],
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Obtener equipos por subcategoría

**URL**: `/equipos/subcategoria/{subcategoriaId}`  
**Método**: `GET`  
**Descripción**: Obtiene todos los equipos asociados a una subcategoría específica, con filtro opcional por serie  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `subcategoriaId` (requerido): ID de la subcategoría

**Parámetros de Consulta**:
- `serieId` (opcional): ID de la serie para filtrar los equipos

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Equipos obtenidos exitosamente (filtrados por subcategoría: 1 y serie: 1)",
  "data": [
    {
      "id": 1,
      "nombre": "Equipo A",
      "descripcion": "Descripción del equipo A",
      "serieId": 1,
      "serieNombre": "Serie A",
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol"
    },
    {
      "id": 2,
      "nombre": "Equipo B",
      "descripcion": "Descripción del equipo B",
      "serieId": 1,
      "serieNombre": "Serie A",
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol"
    }
  ],
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Obtener equipo por ID

**URL**: `/equipos/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene los detalles de un equipo específico por su ID  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `id` (requerido): ID del equipo a consultar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Equipo obtenido exitosamente",
  "data": {
    "id": 1,
    "nombre": "Equipo A",
    "descripcion": "Descripción del equipo A",
    "serieId": 1,
    "serieNombre": "Serie A",
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Crear equipo

**URL**: `/equipos`  
**Método**: `POST`  
**Descripción**: Crea un nuevo equipo  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Nuevo Equipo",
  "descripcion": "Descripción del nuevo equipo",
  "serieId": 1
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Equipo creado exitosamente",
  "data": {
    "id": 3,
    "nombre": "Nuevo Equipo",
    "descripcion": "Descripción del nuevo equipo",
    "serieId": 1,
    "serieNombre": "Serie A",
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Crear múltiples equipos

**URL**: `/equipos/bulk`  
**Método**: `POST`  
**Descripción**: Crea varios equipos en una sola petición  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
```json
{
  "equipos": [
    {
      "nombre": "Equipo C",
      "descripcion": "Descripción del equipo C",
      "serieId": 1
    },
    {
      "nombre": "Equipo D",
      "descripcion": "Descripción del equipo D",
      "serieId": 1
    }
  ]
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Equipos creados exitosamente",
  "data": [
    {
      "id": 3,
      "nombre": "Equipo C",
      "descripcion": "Descripción del equipo C",
      "serieId": 1,
      "serieNombre": "Serie A",
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol"
    },
    {
      "id": 4,
      "nombre": "Equipo D",
      "descripcion": "Descripción del equipo D",
      "serieId": 1,
      "serieNombre": "Serie A",
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol"
    }
  ],
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Actualizar equipo

**URL**: `/equipos/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza un equipo existente  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID del equipo a actualizar

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Equipo A Actualizado",
  "descripcion": "Descripción actualizada del equipo A",
  "serieId": 1
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Equipo actualizado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Equipo A Actualizado",
    "descripcion": "Descripción actualizada del equipo A",
    "serieId": 1,
    "serieNombre": "Serie A",
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol"
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Eliminar equipo

**URL**: `/equipos/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina un equipo existente  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID del equipo a eliminar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Equipo eliminado exitosamente",
  "data": null,
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```

### Verificar existencia de equipos

**URL**: `/equipos/existen`  
**Método**: `GET`  
**Descripción**: Verifica si existen equipos registrados en el sistema  
**Autenticación Requerida**: No

**Respuesta Exitosa (200 OK)**
```
true
```

### Contar equipos

**URL**: `/equipos/count`  
**Método**: `GET`  
**Descripción**: Obtiene el conteo total de equipos registrados  
**Autenticación Requerida**: No

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Conteo de equipos obtenido exitosamente",
  "data": {
    "totalEquipos": 10,
    "porSubcategoria": [
      {
        "subcategoriaId": 1,
        "subcategoriaNombre": "Fútbol",
        "total": 6
      },
      {
        "subcategoriaId": 2,
        "subcategoriaNombre": "Básquet",
        "total": 4
      }
    ]
  },
  "timestamp": "2025-10-21T11:30:00.000+00:00"
}
```
## Jugadores

### Obtener todos los jugadores

**URL**: `/jugadores`  
**Método**: `GET`  
**Descripción**: Obtiene una lista paginada de jugadores con opciones de búsqueda y ordenamiento  
**Autenticación Requerida**: No  
**Roles**: No aplica

**Parámetros de Consulta**:
- `page` (opcional, default=0): Número de página (0-based)
- `size` (opcional, default=10): Tamaño de la página
- `sort` (opcional): Campo por el cual ordenar, seguido de dirección (ej: `apellido,asc` o `fechaNacimiento,desc`)
- `search` (opcional): Término de búsqueda para filtrar por nombre o apellido

**Campos de ordenamiento permitidos**: `id`, `nombre`, `apellido`, `fechaNacimiento`, `documentoIdentidad`

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Jugadores obtenidos exitosamente",
  "data": {
    "content": [
      {
        "id": 1,
        "nombre": "Juan",
        "apellido": "Pérez",
        "fechaNacimiento": "1990-05-15",
        "documentoIdentidad": "12345678",
        "activo": true
      },
      {
        "id": 2,
        "nombre": "María",
        "apellido": "González",
        "fechaNacimiento": "1992-08-22",
        "documentoIdentidad": "87654321",
        "activo": true
      }
    ],
    "totalPages": 1,
    "totalElements": 2,
    "size": 10,
    "number": 0
  },
  "timestamp": "2025-10-22T17:00:00.000+00:00"
}
```

### Contar jugadores activos

**URL**: `/jugadores/count`  
**Método**: `GET`  
**Descripción**: Obtiene el número total de jugadores activos  
**Autenticación Requerida**: No  
**Roles**: No aplica

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Total de jugadores activos obtenido exitosamente",
  "data": {
    "total": 15
  },
  "timestamp": "2025-10-22T17:05:00.000+00:00"
}
```

### Obtener jugador por ID

**URL**: `/jugadores/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene los detalles de un jugador específico por su ID  
**Autenticación Requerida**: No  
**Roles**: No aplica

**Parámetros de Ruta**:
- `id` (requerido): ID del jugador a consultar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Jugador obtenido exitosamente",
  "data": {
    "id": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "fechaNacimiento": "1990-05-15",
    "documentoIdentidad": "12345678",
    "activo": true
  },
  "timestamp": "2025-10-22T17:10:00.000+00:00"
}
```

### Crear jugador

**URL**: `/jugadores`  
**Método**: `POST`  
**Descripción**: Crea un nuevo jugador  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_MODERATOR`, `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Carlos",
  "apellido": "Gómez",
  "fechaNacimiento": "1993-07-20",
  "documentoIdentidad": "11223344",
  "activo": true
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Jugador creado exitosamente",
  "data": {
    "id": 3,
    "nombre": "Carlos",
    "apellido": "Gómez",
    "fechaNacimiento": "1993-07-20",
    "documentoIdentidad": "11223344",
    "activo": true
  },
  "timestamp": "2025-10-22T17:15:00.000+00:00"
}
```

### Actualizar jugador

**URL**: `/jugadores/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza los datos de un jugador existente  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_MODERATOR`, `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID del jugador a actualizar

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Carlos Eduardo",
  "apellido": "Gómez López",
  "fechaNacimiento": "1993-07-20",
  "documentoIdentidad": "11223344",
  "activo": true
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Jugador actualizado exitosamente",
  "data": {
    "id": 3,
    "nombre": "Carlos Eduardo",
    "apellido": "Gómez López",
    "fechaNacimiento": "1993-07-20",
    "documentoIdentidad": "11223344",
    "activo": true
  },
  "timestamp": "2025-10-22T17:20:00.000+00:00"
}
```

### Eliminar jugador

**URL**: `/jugadores/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina un jugador del sistema  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID del jugador a eliminar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Jugador eliminado exitosamente",
  "data": null,
  "timestamp": "2025-10-22T17:25:00.000+00:00"
}
```

### Crear múltiples jugadores

**URL**: `/jugadores/bulk`  
**Método**: `POST`  
**Descripción**: Crea varios jugadores en una sola operación  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_MODERATOR`, `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
```json
{
  "jugadores": [
    {
      "nombre": "Ana",
      "apellido": "Martínez",
      "fechaNacimiento": "1991-03-10",
      "documentoIdentidad": "22334455",
      "activo": true
    },
    {
      "nombre": "Pedro",
      "apellido": "Sánchez",
      "fechaNacimiento": "1994-11-25",
      "documentoIdentidad": "33445566",
      "activo": true
    }
  ]
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Jugadores creados exitosamente",
  "data": [
    {
      "id": 4,
      "nombre": "Ana",
      "apellido": "Martínez",
      "fechaNacimiento": "1991-03-10",
      "documentoIdentidad": "22334455",
      "activo": true
    },
    {
      "id": 5,
      "nombre": "Pedro",
      "apellido": "Sánchez",
      "fechaNacimiento": "1994-11-25",
      "documentoIdentidad": "33445566",
      "activo": true
    }
  ],
  "timestamp": "2025-10-22T17:30:00.000+00:00"
}
```

**Errores**:
- `400 Bad Request`: Datos de entrada inválidos o faltantes
- `409 Conflict`: El documento de identidad ya está registrado
- `500 Internal Server Error`: Error al procesar la solicitud

## Verificación

### Verificar existencia de registros

**URL**: `/verificacion/existen-registros`  
**Método**: `GET`  
**Descripción**: Verifica si existen registros de subcategorías y series en el sistema  
**Autenticación Requerida**: No  
**Roles**: No aplica

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Verificación de registros exitosa",
  "data": {
    "existenRegistros": true
  },
  "timestamp": "2025-10-23T11:55:00.000+00:00"
}
```

**Propiedades de la respuesta**:
- `existenRegistros`: `true` si existen tanto subcategorías como series, `false` en caso contrario

## Gestión de Roles por Subcategoría

### Asignar un Rol a una Subcategoría

Asigna un rol específico a una subcategoría.

**URL**: `/api/subcategoria-roles/subcategoria/{subcategoriaId}/rol/{rolId}`
**Método**: `POST`
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `subcategoriaId` (requerido): ID de la subcategoría
- `rolId` (requerido): ID del rol a asignar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Rol asignado correctamente a la subcategoría",
  "data": {
    "subcategoriaId": 1,
    "rolId": 1,
    "nombreSubcategoria": "Fútbol",
    "nombreRol": "ROLE_USER"
  }
}
```

### Eliminar un Rol de una Subcategoría

Elimina un rol específico de una subcategoría.

**URL**: `/api/subcategoria-roles/subcategoria/{subcategoriaId}/rol/{rolId}`
**Método**: `DELETE`
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `subcategoriaId` (requerido): ID de la subcategoría
- `rolId` (requerido): ID del rol a eliminar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Rol eliminado correctamente de la subcategoría",
  "data": null
}
```

### Obtener Roles por ID de Subcategoría

Obtiene todos los roles asignados a una subcategoría específica.

**URL**: `/api/subcategoria-roles/subcategoria/{subcategoriaId}`
**Método**: `GET`
**Autenticación Requerida**: Sí  
**Roles**: Cualquier rol autenticado

**Parámetros de Ruta**:
- `subcategoriaId` (requerido): ID de la subcategoría

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Roles obtenidos correctamente",
  "data": [
    {
      "rolId": 3,
      "rolName": "PORTERO",
      "rolDetail": "Portero",
      "subcategoriaId": 5,
      "subcategoriaName": "Fútbol"
    },
    {
      "id": 2,
      "nombre": "ROLE_MODERATOR"
    }
  ]
}
```

### Actualizar Relación Subcategoría-Rol

Actualiza la relación entre una subcategoría y un rol existente.

**URL**: `/api/subcategoria-roles/{id}`  
**Método**: `PUT`  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID de la relación subcategoría-rol a actualizar

**Cuerpo de la Solicitud**:
```json
{
  "subcategoriaId": 1,
  "rolId": 2
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Relación entre subcategoría y rol actualizada exitosamente",
  "data": {
    "id": 1,
    "subcategoriaId": 1,
    "rolId": 2
  }
}
```

**Errores**:
- `400 Bad Request`: Si ya existe una relación idéntica o los datos son inválidos
- `404 Not Found`: Si la relación, subcategoría o rol no existen

### Obtener Roles por Nombre de Subcategoría

Obtiene todos los roles asignados a una subcategoría específica por su nombre.

**URL**: `/api/subcategoria-roles/subcategoria/nombre/{nombreSubcategoria}`
**Método**: `GET`
**Autenticación Requerida**: Sí  
**Roles**: Cualquier rol autenticado

**Parámetros de Ruta**:
- `nombreSubcategoria` (requerido): Nombre de la subcategoría

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Roles obtenidos correctamente para la subcategoría: Fútbol",
  "data": [
    {
      "id": 1,
      "nombre": "ROLE_USER"
    },
    {
      "id": 2,
      "nombre": "ROLE_MODERATOR"
    }
  ]
}
```

### Asignar Múltiples Roles a una Subcategoría (Bulk)

Asigna múltiples roles a una subcategoría en una sola operación.

**URL**: `/api/subcategoria-roles/bulk`
**Método**: `POST`
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud (JSON)**:
```json
{
  "subcategoriaId": 1,
  "rolesIds": [1, 2, 3]
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Roles asignados correctamente a la subcategoría",
  "data": {
    "subcategoriaId": 1,
    "rolesAsignados": [
      {
        "id": 1,
        "nombre": "ROLE_USER"
      },
      {
        "id": 2,
        "nombre": "ROLE_MODERATOR"
      },
      {
        "id": 3,
        "nombre": "ROLE_ADMIN"
      }
    ]
  }
}
```

## Gestión de Roles

### Crear o Actualizar un Rol

Crea un nuevo rol o actualiza uno existente si ya existe.

**URL**: `/api/roles`  
**Método**: `POST`  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud (JSON)**:
```json
{
  "name": "ROLE_NUEVO",
  "description": "Descripción del nuevo rol"
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Rol creado exitosamente",
  "data": {
    "id": 4,
    "name": "ROLE_NUEVO",
    "description": "Descripción del nuevo rol"
  }
}
```

### Crear o Actualizar Múltiples Roles (Bulk)

Crea o actualiza múltiples roles en una sola operación.

**URL**: `/api/roles/bulk`  
**Método**: `POST`  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud (JSON)**:
```json
{
  "roles": [
    {
      "name": "ROLE_NUEVO1",
      "description": "Descripción del rol 1"
    },
    {
      "name": "ROLE_NUEVO2",
      "description": "Descripción del rol 2"
    }
  ]
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Roles procesados exitosamente",
  "data": [
    {
      "id": 5,
      "name": "ROLE_NUEVO1",
      "description": "Descripción del rol 1"
    },
    {
      "id": 6,
      "name": "ROLE_NUEVO2",
      "description": "Descripción del rol 2"
    }
  ]
}
```

### Obtener Todos los Roles

Obtiene una lista de todos los roles disponibles en el sistema.

**URL**: `/api/roles`  
**Método**: `GET`  
**Autenticación Requerida**: Sí  
**Roles**: Cualquier rol autenticado

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Lista de roles obtenida exitosamente",
  "data": [
    {
      "id": 1,
      "name": "ROLE_USER",
      "description": "Rol de usuario estándar"
    },
    {
      "id": 2,
      "name": "ROLE_MODERATOR",
      "description": "Rol de moderador"
    },
    {
      "id": 3,
      "name": "ROLE_ADMIN",
      "description": "Rol de administrador"
    }
  ]
}
```

### Obtener Rol por Nombre

Obtiene los detalles de un rol específico por su nombre.

**URL**: `/api/roles/{name}`  
**Método**: `GET`  
**Autenticación Requerida**: Sí  
**Roles**: Cualquier rol autenticado

**Parámetros de Ruta**:
- `name` (requerido): Nombre del rol (ej: ROLE_USER, ROLE_ADMIN)

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Rol encontrado exitosamente",
  "data": {
    "id": 1,
    "name": "ROLE_USER",
    "description": "Rol de usuario estándar"
  }
}
```

**Respuesta de Error (404 Not Found)**
```json
{
  "success": false,
  "message": "No se encontró el rol especificado",
  "error": "Not Found",
  "path": "/api/roles/ROL_INEXISTENTE"
}
```

**Respuesta de Error (400 Bad Request)**
```json
{
  "success": false,
  "message": "Nombre de rol no válido: ROL_INVALIDO",
  "error": "Bad Request",
  "path": "/api/roles/ROL_INVALIDO"
}
```

### Obtener Rol por ID

Obtiene los detalles de un rol específico por su ID.

**URL**: `/roles/{id}`  
**Método**: `GET`  
**Autenticación Requerida**: Sí  
**Roles**: Cualquier rol autenticado

**Parámetros de Ruta**:
- `id` (requerido): ID numérico del rol

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Rol encontrado exitosamente",
  "data": {
    "id": 1,
    "name": "ROLE_USER",
    "detail": "Rol de usuario estándar",
    "estado": true
  },
  "timestamp": "2025-10-24T11:30:00.000+00:00"
}
```

**Respuesta de Error (404 Not Found)**
```json
{
  "success": false,
  "message": "No se encontró el rol con ID: 999",
  "error": "Not Found",
  "path": "/roles/999"
}
```

### Actualizar Rol

Actualiza los detalles de un rol existente.

**URL**: `/roles/{id}`  
**Método**: `PUT`  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID numérico del rol a actualizar

**Cuerpo de la Solicitud**
```json
{
  "name": "ROLE_UPDATED",
  "detail": "Descripción actualizada del rol"
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Rol actualizado exitosamente",
  "data": {
    "id": 1,
    "name": "ROLE_UPDATED",
    "detail": "Descripción actualizada del rol",
    "estado": true
  },
  "timestamp": "2025-10-24T11:45:00.000+00:00"
}
```

**Respuesta de Error (404 Not Found)**
```json
{
  "success": false,
  "message": "No se encontró el rol con ID: 999",
  "error": "Not Found",
  "path": "/roles/999"
}
```

**Respuesta de Error (400 Bad Request)**
```json
{
  "success": false,
  "message": "El campo 'name' no puede estar vacío",
  "error": "Bad Request",
  "path": "/roles/1"
}
```

### Eliminar Rol

Elimina un rol existente del sistema.

**URL**: `/roles/{id}`  
**Método**: `DELETE`  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID numérico del rol a eliminar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Rol eliminado exitosamente",
  "data": null,
  "timestamp": "2025-10-24T12:00:00.000+00:00"
}
```

**Respuesta de Error (404 Not Found)**
```json
{
  "success": false,
  "message": "No se encontró el rol con ID: 999",
  "error": "Not Found",
  "path": "/roles/999"
}
```

## Sanciones

### Obtener todas las sanciones

Obtiene una lista de todas las sanciones registradas en el sistema.

**URL**: `/sanciones`  
**Método**: `GET`  
**Autenticación Requerida**: No

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Sanciones obtenidas exitosamente",
  "data": [
    {
      "id": 1,
      "jugadorId": 3,
      "jugadorNombre": "Jugador 3 Apellido 3",
      "encuentroId": 102,
      "encuentroTitulo": "Equipo A vs Equipo C",
      "tipoSancion": "TARJETA_ROJA",
      "motivo": "Motivo",
      "detalleSancion": "3 días",
      "fechaRegistro": "2025-10-19"
    }
  ]
}
```

### Obtener sanción por ID

Obtiene los detalles de una sanción específica por su ID.

**URL**: `/sanciones/{id}`  
**Método**: `GET`  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `id` (requerido): ID de la sanción a consultar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Sanción obtenida exitosamente",
  "data": {
    "id": 1,
    "jugadorId": 1,
    "jugadorNombre": "Juan Pérez",
    "encuentroId": 1,
    "encuentroTitulo": "Partido 1",
    "tipoSancion": "Tarjeta Amarilla",
    "motivo": "Falta técnica",
    "detalleSancion": "Jugador amonestado por conducta antideportiva",
    "fechaRegistro": "2023-10-24"
  }
}
```

### Crear sanción

Crea una nueva sanción en el sistema.

**URL**: `/sanciones`  
**Método**: `POST`  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`, `ROLE_MODERATOR`

**Cuerpo de la Solicitud (JSON)**:
```json
{
  "jugadorId": 1,
  "encuentroId": 1,
  "tipoSancion": "Tarjeta Roja",
  "motivo": "Juego brusco grave",
  "detalleSancion": "Entrada violenta sobre el jugador contrario",
  "fechaRegistro": "2023-10-24"
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Sanción creada exitosamente",
  "data": {
    "id": 2,
    "jugadorId": 1,
    "jugadorNombre": "Juan Pérez",
    "encuentroId": 1,
    "encuentroTitulo": "Partido 1",
    "tipoSancion": "Tarjeta Roja",
    "motivo": "Juego brusco grave",
    "detalleSancion": "Entrada violenta sobre el jugador contrario",
    "fechaRegistro": "2023-10-24"
  }
}
```

### Actualizar sanción

Actualiza una sanción existente.

**URL**: `/sanciones/{id}`  
**Método**: `PUT`  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`, `ROLE_MODERATOR`

**Parámetros de Ruta**:
- `id` (requerido): ID de la sanción a actualizar

**Cuerpo de la Solicitud (JSON)**:
```json
{
  "jugadorId": 1,
  "encuentroId": 1,
  "tipoSancion": "Tarjeta Roja",
  "motivo": "Juego brusco grave",
  "detalleSancion": "Entrada violenta sobre el jugador contrario - Sanción confirmada por el árbitro",
  "fechaRegistro": "2023-10-24"
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Sanción actualizada exitosamente",
  "data": {
    "id": 2,
    "jugadorId": 1,
    "jugadorNombre": "Juan Pérez",
    "encuentroId": 1,
    "encuentroTitulo": "Partido 1",
    "tipoSancion": "Tarjeta Roja",
    "motivo": "Juego brusco grave",
    "detalleSancion": "Entrada violenta sobre el jugador contrario - Sanción confirmada por el árbitro",
    "fechaRegistro": "2023-10-24"
  }
}
```

### Eliminar sanción

Elimina una sanción del sistema.

**URL**: `/sanciones/{id}`  
**Método**: `DELETE`  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `id` (requerido): ID de la sanción a eliminar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Sanción eliminada exitosamente"
}
```

### Obtener sanciones por jugador

Obtiene todas las sanciones asociadas a un jugador específico.

**URL**: `/sanciones/jugador/{jugadorId}`  
**Método**: `GET`  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `jugadorId` (requerido): ID del jugador

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Sanciones por jugador obtenidas exitosamente",
  "data": [
    {
      "id": 1,
      "jugadorId": 1,
      "jugadorNombre": "Juan Pérez",
      "encuentroId": 1,
      "encuentroTitulo": "Partido 1",
      "tipoSancion": "Tarjeta Amarilla",
      "motivo": "Falta técnica",
      "detalleSancion": "Jugador amonestado por conducta antideportiva",
      "fechaRegistro": "2023-10-24"
    }
  ]
}
```

### Obtener sanciones por encuentro

Obtiene todas las sanciones asociadas a un encuentro específico.

**URL**: `/sanciones/encuentro/{encuentroId}`  
**Método**: `GET`  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `encuentroId` (requerido): ID del encuentro

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Sanciones por encuentro obtenidas exitosamente",
  "data": [
    {
      "id": 1,
      "jugadorId": 1,
      "jugadorNombre": "Juan Pérez",
      "encuentroId": 1,
      "encuentroTitulo": "Partido 1",
      "tipoSancion": "Tarjeta Amarilla",
      "motivo": "Falta técnica",
      "detalleSancion": "Jugador amonestado por conducta antideportiva",
      "fechaRegistro": "2023-10-24"
    },
    {
      "id": 2,
      "jugadorId": 2,
      "jugadorNombre": "Carlos Gómez",
      "encuentroId": 1,
      "encuentroTitulo": "Partido 1",
      "tipoSancion": "Tarjeta Roja",
      "motivo": "Juego brusco grave",
      "detalleSancion": "Entrada violenta sobre el jugador contrario",
      "fechaRegistro": "2023-10-24"
    }
  ]
}
```

### Obtener sanciones por tipo

Obtiene todas las sanciones de un tipo específico.

**URL**: `/sanciones/tipo/{tipoSancion}`  
**Método**: `GET`  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `tipoSancion` (requerido): Tipo de sanción a buscar (ej: "Tarjeta Amarilla", "Tarjeta Roja")

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Sanciones por tipo obtenidas exitosamente",
  "data": [
    {
      "id": 1,
      "jugadorId": 1,
      "jugadorNombre": "Juan Pérez",
      "encuentroId": 1,
      "encuentroTitulo": "Partido 1",
      "tipoSancion": "Tarjeta Amarilla",
      "motivo": "Falta técnica",
      "detalleSancion": "Jugador amonestado por conducta antideportiva",
      "fechaRegistro": "2023-10-24"
    },
    {
      "id": 3,
      "jugadorId": 3,
      "jugadorNombre": "Ana López",
      "encuentroId": 2,
      "encuentroTitulo": "Partido 2",
      "tipoSancion": "Tarjeta Amarilla",
      "motivo": "Tiempo perdido",
      "detalleSancion": "Demora en la ejecución de un saque de banda",
      "fechaRegistro": "2023-10-25"
    }
  ]
}
```

**Respuesta de Error (404 Not Found)**
```json
{
  "success": false,
  "message": "No se encontraron sanciones del tipo especificado",
  "error": "Not Found",
  "path": "/sanciones/tipo/TipoInexistente"
}
```

## Estadios

### Obtener todos los estadios

**URL**: `/estadios`  
**Método**: `GET`  
**Descripción**: Obtiene una lista de todos los estadios registrados  
**Autenticación Requerida**: Sí  
**Roles Permitidos**: USER, MODERATOR, ADMIN

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Estadios obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Estadio Olímpico",
      "detalle": "Estadio principal de la ciudad",
      "estado": true
    },
    ...
  ]
}
```

### Obtener estadio por ID

**URL**: `/estadios/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene los detalles de un estadio específico por su ID  
**Autenticación Requerida**: Sí  
**Roles Permitidos**: USER, MODERATOR, ADMIN

**Parámetros de Ruta**:
- `id` (requerido): ID del estadio a obtener

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Estadio obtenido exitosamente",
  "data": {
    "id": 1,
    "nombre": "Estadio Olímpico",
    "detalle": "Estadio principal de la ciudad",
    "estado": true
  }
}
```

### Crear estadio

**URL**: `/estadios`  
**Método**: `POST`  
**Descripción**: Crea un nuevo estadio  
**Autenticación Requerida**: Sí  
**Roles Permitidos**: MODERATOR, ADMIN

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Nuevo Estadio",
  "detalle": "Detalles del nuevo estadio",
  "estado": true
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Estadio creado exitosamente",
  "data": {
    "id": 2,
    "nombre": "Nuevo Estadio",
    "detalle": "Detalles del nuevo estadio",
    "estado": true
  }
}
```

### Actualizar estadio

**URL**: `/estadios/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza los datos de un estadio existente  
**Autenticación Requerida**: Sí  
**Roles Permitidos**: MODERATOR, ADMIN

**Parámetros de Ruta**:
- `id` (requerido): ID del estadio a actualizar

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Estadio Actualizado",
  "detalle": "Nuevos detalles del estadio",
  "estado": false
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Estadio actualizado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Estadio Actualizado",
    "detalle": "Nuevos detalles del estadio",
    "estado": false
  }
}
```

### Eliminar estadio

**URL**: `/estadios/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina un estadio (eliminación lógica)  
**Autenticación Requerida**: Sí  
**Roles Permitidos**: ADMIN

**Parámetros de Ruta**:
- `id` (requerido): ID del estadio a eliminar

**Respuesta Exitosa (200 OK)**
```json
{
  "success": true,
  "message": "Estadio eliminado exitosamente",
  "data": null
}
```

### Crear múltiples estadios

**URL**: `/estadios/bulk`  
**Método**: `POST`  
**Descripción**: Crea múltiples estadios en una sola operación  
**Autenticación Requerida**: Sí  
**Roles Permitidos**: MODERATOR, ADMIN

**Cuerpo de la Solicitud**:
```json
{
  "estadios": [
    {
      "nombre": "Estadio 1",
      "detalle": "Detalle estadio 1",
      "estado": true
    },
    {
      "nombre": "Estadio 2",
      "detalle": "Detalle estadio 2",
      "estado": true
    }
  ]
}
```

**Respuesta Exitosa (201 Created)**
```json
{
  "success": true,
  "message": "Estadios creados exitosamente",
  "data": [
    {
      "id": 3,
      "nombre": "Estadio 1",
      "detalle": "Detalle estadio 1",
      "estado": true
    },
    {
      "id": 4,
      "nombre": "Estadio 2",
      "detalle": "Detalle estadio 2",
      "estado": true
    }
  ]
}
```

### Códigos de Error

- **404 Not Found**: Cuando no se encuentra el estadio con el ID proporcionado
- **400 Bad Request**: Cuando los datos de la solicitud son inválidos
- **403 Forbidden**: Cuando el usuario no tiene permisos para realizar la acción
```
