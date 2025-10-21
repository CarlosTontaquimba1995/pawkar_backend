# Documentación de la API

## Tabla de Contenidos
- [Categorías](#categorías)
  - [Obtener todas las categorías](#obtener-todas-las-categorías)
  - [Obtener categoría por ID](#obtener-categoría-por-id)
  - [Crear categoría](#crear-categoría)
  - [Crear múltiples categorías](#crear-múltiples-categorías)
  - [Actualizar categoría](#actualizar-categoría)
  - [Eliminar categoría](#eliminar-categoría)
- [Equipos](#equipos)
  - [Obtener equipos](#obtener-todos-los-equipos)
  - [Verificar existencia de equipos](#verificar-existencia-de-equipos)
  - [Obtener equipo por ID](#obtener-equipo-por-id)
  - [Crear equipo](#crear-equipo)
  - [Actualizar equipo](#actualizar-equipo)
  - [Eliminar equipo](#eliminar-equipo)
- [Sanciones](#sanciones)
- [Tabla de Posiciones](#tabla-de-posiciones)
- [Autenticación](#autenticación)

## Categorías

### Obtener todas las categorías

**URL**: `/categorias`  
**Método**: `GET`  
**Descripción**: Obtiene una lista de categorías  
**Autenticación Requerida**: No  
**Roles**: `ROLE_USER`, `ROLE_ADMIN`

**Respuestas**

**200 OK** - Operación exitosa

```json
{
  "success": true,
  "message": "Categorías obtenidas exitosamente",
  "data": [
    {
      "categoriaId": 1,
      "nombre": "Categoría A"
    }
  ],
  "timestamp": "2025-10-20T10:30:00.000+00:00"
}
```

### Obtener categoría por ID

**URL**: `/categorias/{categoriaId}`  
**Método**: `GET`  
**Descripción**: Obtiene una categoría por ID  
**Autenticación Requerida**: No  
**Roles**: `ROLE_USER`, `ROLE_ADMIN`

**Parámetros de Ruta**:
- `categoriaId` (requerido): ID de la categoría

**Respuestas**

**200 OK** - Operación exitosa

```json
{
  "success": true,
  "message": "Categoría obtenida exitosamente",
  "data": {
    "categoriaId": 1,
    "nombre": "Categoría A"
  },
  "timestamp": "2025-10-20T10:30:00.000+00:00"
}
```

### Crear categoría

**URL**: `/categorias`  
**Método**: `POST`  
**Descripción**: Crea una nueva categoría  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
- `nombre` (requerido): Nombre de la categoría

**Respuestas**

**201 Created** - Operación exitosa

```json
{
  "success": true,
  "message": "Categoría creada exitosamente",
  "data": {
    "categoriaId": 1,
    "nombre": "Categoría A"
  },
  "timestamp": "2025-10-20T10:30:00.000+00:00"
}
```

### Crear múltiples categorías

**URL**: `/categorias/multiple`  
**Método**: `POST`  
**Descripción**: Crea múltiples categorías  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Cuerpo de la Solicitud**:
- `categorias` (requerido): Lista de categorías a crear

**Respuestas**

**201 Created** - Operación exitosa

```json
{
  "success": true,
  "message": "Categorías creadas exitosamente",
  "data": [
    {
      "categoriaId": 1,
      "nombre": "Categoría A"
    },
    {
      "categoriaId": 2,
      "nombre": "Categoría B"
    }
  ],
  "timestamp": "2025-10-20T10:30:00.000+00:00"
}
```

### Actualizar categoría

**URL**: `/categorias/{categoriaId}`  
**Método**: `PUT`  
**Descripción**: Actualiza una categoría  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `categoriaId` (requerido): ID de la categoría

**Cuerpo de la Solicitud**:
- `nombre` (requerido): Nuevo nombre de la categoría

**Respuestas**

**200 OK** - Operación exitosa

```json
{
  "success": true,
  "message": "Categoría actualizada exitosamente",
  "data": {
    "categoriaId": 1,
    "nombre": "Categoría A actualizada"
  },
  "timestamp": "2025-10-20T10:30:00.000+00:00"
}
```

### Eliminar categoría

**URL**: `/categorias/{categoriaId}`  
**Método**: `DELETE`  
**Descripción**: Elimina una categoría  
**Autenticación Requerida**: Sí  
**Roles**: `ROLE_ADMIN`

**Parámetros de Ruta**:
- `categoriaId` (requerido): ID de la categoría

**Respuestas**

**200 OK** - Operación exitosa

```json
{
  "success": true,
  "message": "Categoría eliminada exitosamente",
  "data": null,
  "timestamp": "2025-10-20T10:30:00.000+00:00"
}
```

## Equipos

### Obtener todos los equipos

**URL**: `/equipos`  
**Método**: `GET`  
**Descripción**: Obtiene una lista paginada de equipos con opciones de búsqueda, filtrado y ordenación.  
**Autenticación Requerida**: No  
**Roles**: `ROLE_USER`, `ROLE_ADMIN`

**Notas**:
- Por defecto solo se muestran los equipos con `estado = true`
- El parámetro `estado` permite filtrar por estado del equipo
- Se requiere rol `ROLE_ADMIN` para ver equipos inactivos (`estado = false`)
- Los equipos no se eliminan físicamente, se marcan como inactivos

**Parámetros de Consulta**:
- `page` (opcional): Número de página (comenzando desde 0). Por defecto: 0
- `size` (opcional): Cantidad de elementos por página. Por defecto: 10
- `sort` (opcional): Campo de ordenación (ej: `nombre,desc`)
- `search` (opcional): Texto para buscar equipos por nombre
- `estado` (opcional, boolean): Filtra por estado del equipo (`true` para activos, `false` para inactivos)
- `subcategoriaId` (opcional): Filtrar por ID de subcategoría
- `serieId` (opcional): Filtrar por ID de serie

**Ejemplos de solicitud**:

1. Obtener primera página de equipos (10 por defecto):
```http
GET /equipos HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
```

2. Buscar equipos por nombre:
```http
GET /equipos?search=Equipo%20A&estado=true&page=0&size=10&sort=nombre,asc HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
```

3. Ordenar equipos por nombre descendente:
```http
GET /equipos?sort=nombre,desc&estado=true HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
```

4. Búsqueda con paginación:
```http
GET /equipos?search=Equipo&page=0&size=5&estado=true HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
```

**Respuestas**

**200 OK** - Operación exitosa

1. Para búsquedas exitosas:
```json
{
  "success": true,
  "message": "Equipos obtenidos exitosamente",
  "data": {
    "content": [
      {
        "equipoId": 1,
        "nombre": "Equipo A",
        "subcategoriaId": 1,
        "subcategoriaNombre": "Fútbol",
        "serieId": 1,
        "serieNombre": "Serie A",
        "fundacion": "2000-01-01",
        "jugadoresCount": 15,
        "estado": true,
        "fechaCreacion": "2025-01-15T10:30:00Z",
        "fechaActualizacion": "2025-10-01T14:25:00Z"
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
  "timestamp": "2025-10-20T10:30:00.000+00:00"
}
```

2. Para listado general:
```json
{
  "success": true,
  "message": "Equipos obtenidos exitosamente",
  "data": {
    "content": [
      {
        "equipoId": 1,
        "nombre": "Equipo A",
        "subcategoriaId": 1,
        "subcategoriaNombre": "Fútbol",
        "serieId": 1,
        "serieNombre": "Serie A",
        "fundacion": "2000-01-01",
        "jugadoresCount": 15,
        "estado": true,
        "fechaCreacion": "2025-01-15T10:30:00Z",
        "fechaActualizacion": "2025-10-01T14:25:00Z"
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
  "timestamp": "2025-10-20T10:30:00.000+00:00"
}
```

### Verificar existencia de equipos

**URL**: `/equipos/existen`  
**Método**: `GET`  
**Descripción**: Verifica si existen equipos registrados en el sistema según los criterios de búsqueda  
**Autenticación Requerida**: No  
**Roles**: `ROLE_USER`, `ROLE_ADMIN`

**Parámetros de Consulta**:
- `estado` (opcional, boolean): Filtra por estado del equipo (`true` para activos, `false` para inactivos)
- `subcategoriaId` (opcional): Filtrar por ID de subcategoría
- `serieId` (opcional): Filtrar por ID de serie

**Respuestas**

**200 OK** - Operación exitosa

```json
{
  "success": true,
  "message": "Se encontraron equipos registrados con los criterios especificados",
  "data": true,
  "timestamp": "2025-10-20T10:30:00.000+00:00"
}
```

**200 OK** - No se encontraron equipos

```json
{
  "success": true,
  "message": "No se encontraron equipos registrados con los criterios especificados",
  "data": false,
  "timestamp": "2025-10-20T10:30:00.000+00:00"
}
```

**Ejemplos de Uso**

1. Verificar si existen equipos activos:
```http
GET /equipos/existen?estado=true HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
```

2. Verificar si existen equipos en una subcategoría específica:
```http
GET /equipos/existen?subcategoriaId=1&estado=true HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
```

3. Verificar si existen equipos inactivos en una serie específica:
```http
GET /equipos/existen?serieId=2&estado=false HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
Authorization: Bearer <token>
```

## Sanciones

### Obtener todas las sanciones

**URL**: `/sanciones`  
**Método**: `GET`  
**Descripción**: Obtiene todas las sanciones registradas  
**Autenticación Requerida**: No

**Respuesta (200 OK)**:
```json
[
  {
    "id": 1,
    "tipoSancion": "TARJETA_AMARILLA",
    "descripcion": "Falta técnica",
    "fechaSancion": "2025-10-19T10:00:00",
    "jugadorId": 1,
    "encuentroId": 1
  }
]
```

### Obtener sanción por ID

**URL**: `/sanciones/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene una sanción específica por su ID  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `id` (requerido): ID de la sanción

**Respuesta (200 OK)**:
```json
{
  "id": 1,
  "tipoSancion": "TARJETA_AMARILLA",
  "descripcion": "Falta técnica",
  "fechaSancion": "2025-10-19T10:00:00",
  "jugadorId": 1,
  "encuentroId": 1
}
```

### Crear una nueva sanción

**URL**: `/sanciones`  
**Método**: `POST`  
**Descripción**: Crea una nueva sanción  
**Autenticación Requerida**: Sí

**Cuerpo de la Solicitud**:
```json
{
  "tipoSancion": "TARJETA_AMARILLA",
  "descripcion": "Falta técnica",
  "fechaSancion": "2025-10-19T10:00:00",
  "jugadorId": 1,
  "encuentroId": 1
}
```

**Respuesta (200 OK)**:
```json
{
  "id": 1,
  "tipoSancion": "TARJETA_AMARILLA",
  "descripcion": "Falta técnica",
  "fechaSancion": "2025-10-19T10:00:00",
  "jugadorId": 1,
  "encuentroId": 1
}
```

### Actualizar una sanción

**URL**: `/sanciones/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza una sanción existente  
**Autenticación Requerida**: Sí

**Parámetros de Ruta**:
- `id` (requerido): ID de la sanción a actualizar

**Cuerpo de la Solicitud**:
```json
{
  "tipoSancion": "TARJETA_ROJA",
  "descripcion": "Falta grave",
  "fechaSancion": "2025-10-19T10:00:00",
  "jugadorId": 1,
  "encuentroId": 1
}
```

**Respuesta (200 OK)**:
```json
{
  "id": 1,
  "tipoSancion": "TARJETA_ROJA",
  "descripcion": "Falta grave",
  "fechaSancion": "2025-10-19T10:00:00",
  "jugadorId": 1,
  "encuentroId": 1
}
```

### Eliminar una sanción

**URL**: `/sanciones/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina una sanción por su ID  
**Autenticación Requerida**: Sí

**Parámetros de Ruta**:
- `id` (requerido): ID de la sanción a eliminar

**Respuesta (204 No Content)**:
```
// Sin contenido en la respuesta
```

### Obtener sanciones por jugador

**URL**: `/sanciones/jugador/{jugadorId}`  
**Método**: `GET`  
**Descripción**: Obtiene todas las sanciones de un jugador específico  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `jugadorId` (requerido): ID del jugador

**Respuesta (200 OK)**:
```json
[
  {
    "id": 1,
    "tipoSancion": "TARJETA_AMARILLA",
    "descripcion": "Falta técnica",
    "fechaSancion": "2025-10-19T10:00:00",
    "jugadorId": 1,
    "encuentroId": 1
  }
]
```

### Obtener sanciones por encuentro

**URL**: `/sanciones/encuentro/{encuentroId}`  
**Método**: `GET`  
**Descripción**: Obtiene todas las sanciones de un encuentro específico  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `encuentroId` (requerido): ID del encuentro

**Respuesta (200 OK)**:
```json
[
  {
    "id": 1,
    "tipoSancion": "TARJETA_AMARILLA",
    "descripcion": "Falta técnica",
    "fechaSancion": "2025-10-19T10:00:00",
    "jugadorId": 1,
    "encuentroId": 1
  }
]
```

### Obtener sanciones por tipo

**URL**: `/sanciones/tipo/{tipoSancion}`  
**Método**: `GET`  
**Descripción**: Obtiene todas las sanciones de un tipo específico  
**Autenticación Requerida**: No

**Parámetros de Ruta**:
- `tipoSancion` (requerido): Tipo de sanción (ej: TARJETA_AMARILLA, TARJETA_ROJA, SUSPENSION, etc.)

**Respuesta (200 OK)**:
```json
[
  {
    "id": 1,
    "tipoSancion": "TARJETA_AMARILLA",
    "descripcion": "Falta técnica",
    "fechaSancion": "2025-10-19T10:00:00",
    "jugadorId": 1,
    "encuentroId": 1
  }
]
```

## Tabla de Posiciones

### Obtener tabla de posiciones por subcategoría

**URL**: `/api/tabla-posicion/subcategoria/{subcategoriaId}`  
**Método**: `GET`  
**Descripción**: Obtiene la tabla de posiciones para una subcategoría específica  
**Autenticación requerida**: No  
**Versión**: 1.0.0

**Parámetros de la URL**:
- `subcategoriaId` (requerido): ID de la subcategoría

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Tabla de posiciones obtenida exitosamente",
  "data": [
    {
      "subcategoriaId": 1,
      "equipoId": 1,
      "equipoNombre": "Equipo A",
      "partidosJugados": 5,
      "victorias": 3,
      "derrotas": 1,
      "empates": 1,
      "golesAFavor": 8,
      "golesEnContra": 4,
      "diferenciaGoles": 4,
      "puntos": 10,
      "posicion": 1
    },
    {
      "subcategoriaId": 1,
      "equipoId": 2,
      "equipoNombre": "Equipo B",
      "partidosJugados": 5,
      "victorias": 2,
      "derrotas": 2,
      "empates": 1,
      "golesAFavor": 5,
      "golesEnContra": 6,
      "diferenciaGoles": -1,
      "puntos": 7,
      "posicion": 2
    }
  ]
}
```

### Buscar en la tabla de posiciones

**URL**: `/api/tabla-posicion/search`  
**Método**: `GET`  
**Descripción**: Busca en la tabla de posiciones según diferentes criterios  
**Autenticación requerida**: No  
**Versión**: 1.0.0

#### Parámetros de consulta

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| `subcategoriaId` | Integer | No | Filtra por ID de subcategoría |
| `categoriaId` | Integer | No | Filtra por ID de categoría |
| `equipoId` | Integer | No | Filtra por ID de equipo |
| `serieId` | Integer | No | Filtra por ID de serie |
| `nombreEquipo` | String | No | Búsqueda por nombre o parte del nombre del equipo (no sensible a mayúsculas) |
| `page` | Integer | No | Número de página (0-based), por defecto: 0 |
| `size` | Integer | No | Tamaño de la página, por defecto: 10 |
| `sort` | String | No | Campo(s) de ordenación en formato `campo,direccion` (ej: `puntos,desc`, `equipo.nombre,asc`). Por defecto: `puntos,desc` |

#### Ejemplos de uso

1. **Búsqueda por subcategoría**:
   ```
   GET /api/tabla-posicion/search?subcategoriaId=5
   ```

2. **Búsqueda por categoría**:
   ```
   GET /api/tabla-posicion/search?categoriaId=1
   ```

3. **Búsqueda por equipo**:
   ```
   GET /api/tabla-posicion/search?equipoId=10
   ```

4. **Búsqueda por serie**:
   ```
   GET /api/tabla-posicion/search?serieId=3
   ```

5. **Búsqueda por nombre de equipo**:
   ```
   GET /api/tabla-posicion/search?nombreEquipo=pepito
   ```

6. **Búsqueda avanzada con múltiples filtros**:
   ```
   GET /api/tabla-posicion/search?categoriaId=1&serieId=3&page=0&size=20&sort=puntos,desc
   ```

7. **Ordenar por nombre de equipo ascendente**:
   ```
   GET /api/tabla-posicion/search?sort=equipo.nombre,asc
   ```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Búsqueda de tabla de posiciones exitosa",
  "data": {
    "content": [
      {
        "posicion": 1,
        "equipo": {
          "equipoId": 1,
          "nombreEquipo": "Equipo A",
          "logo": "url_del_logo"
        },
        "puntos": 15,
        "partidosJugados": 5,
        "partidosGanados": 5,
        "partidosEmpatados": 0,
        "partidosPerdidos": 0,
        "golesAFavor": 12,
        "golesEnContra": 2,
        "diferenciaGoles": 10
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
      "unpaged": false,
      "paged": true
    },
    "totalElements": 25,
    "totalPages": 3,
    "last": false,
    "size": 10,
    "number": 0,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "numberOfElements": 10,
    "first": true,
    "empty": false
  }
}
```

#### Campos de respuesta

- `content`: Array de registros de la tabla de posiciones
    - `posicion`: Posición en la tabla
    - `equipo`: Información del equipo
        - `equipoId`: ID del equipo
        - `nombreEquipo`: Nombre del equipo
        - `logo`: URL del logo del equipo
    - `puntos`: Puntos totales
    - `partidosJugados`: Partidos jugados
    - `partidosGanados`: Partidos ganados
    - `partidosEmpatados`: Partidos empatados
    - `partidosPerdidos`: Partidos perdidos
    - `golesAFavor`: Goles a favor
    - `golesEnContra`: Goles en contra
    - `diferenciaGoles`: Diferencia de goles

#### Información de paginación

- `pageable`: Información de paginación
- `totalElements`: Número total de elementos
- `totalPages`: Número total de páginas
- `size`: Número de elementos por página
- `number`: Número de página actual
- `sort`: Información de ordenación actual
- `first`: Indica si es la primera página
- `last`: Indica si es la última página
- `empty`: Indica si el contenido está vacío

#### Notas

1. Las búsquedas de texto no distinguen entre mayúsculas y minúsculas
2. Los filtros múltiples se combinan con lógica AND
3. El ordenamiento predeterminado es por puntos (`puntos`) en orden descendente
4. Se puede ordenar por cualquier campo usando el formato `campo,direccion` donde dirección es `asc` o `desc`
5. La respuesta incluye metadatos de paginación para facilitar la navegación
```

### Actualizar tabla de posiciones desde un partido

**URL**: `/api/tabla-posicion/actualizar-desde-partido`  
**Método**: `POST`  
**Descripción**: Actualiza la tabla de posiciones basada en el resultado de un partido  
**Autenticación requerida**: Sí  
**Content-Type**: `application/json`

**Request Body**:
```json
{
  "subcategoriaId": 1,
  "equipoLocalId": 1,
  "equipoVisitanteId": 2,
  "golesLocal": 2,
  "golesVisitante": 1,
  "estadoPartido": "FINALIZADO"
}
```

**Campos del Request Body**:
- `subcategoriaId` (requerido): ID de la subcategoría del partido
- `equipoLocalId` (requerido): ID del equipo local
- `equipoVisitanteId` (requerido): ID del equipo visitante
- `golesLocal` (opcional, default: 0): Goles del equipo local
- `golesVisitante` (opcional, default: 0): Goles del equipo visitante
- `estadoPartido` (requerido): Estado actual del partido (ej. "FINALIZADO", "SUSPENDIDO", etc.)

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Tabla de posiciones actualizada exitosamente",
  "data": null
}
```

**Códigos de error**:
- `400 Bad Request`: Si faltan campos requeridos o los datos son inválidos
- `500 Internal Server Error`: Si ocurre un error al procesar la solicitud

### Crear o actualizar posición en la tabla manualmente

**URL**: `/api/tabla-posicion`  
**Método**: `POST`  
**Descripción**: Crea o actualiza manualmente una posición en la tabla de posiciones  
**Autenticación requerida**: Sí  
**Content-Type**: `application/json`

**Request Body**:
```json
{
  "subcategoriaId": 1,
  "equipoId": 1,
  "partidosJugados": 5,
  "victorias": 3,
  "derrotas": 1,
  "empates": 1,
  "puntos": 10
}
```

**Campos del Request Body**:
- `subcategoriaId` (requerido): ID de la subcategoría
- `equipoId` (requerido): ID del equipo
- `partidosJugados` (opcional, default: 0): Número de partidos jugados
- `victorias` (opcional, default: 0): Número de victorias
- `derrotas` (opcional, default: 0): Número de derrotas
- `empates` (opcional, default: 0): Número de empates
- `puntos` (opcional, default: 0): Puntos totales

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Posición actualizada exitosamente",
  "data": {
    "subcategoria": {
      "subcategoriaId": 1,
      "nombre": "Fútbol"
    },
    "equipo": {
      "equipoId": 1,
      "nombre": "Equipo A"
    },
    "partidosJugados": 5,
    "victorias": 3,
    "derrotas": 1,
    "empates": 1,
    "golesAFavor": 8,
    "golesEnContra": 4,
    "diferenciaGoles": 4,
    "puntos": 10
  }
}
```

### Actualizar posición en la tabla

**URL**: `/api/tabla-posicion`  
**Método**: `PUT`  
**Descripción**: Actualiza una posición existente en la tabla de posiciones  
**Autenticación requerida**: Sí  
**Content-Type**: `application/json`

**Request Body**:
```json
{
  "subcategoriaId": 1,
  "equipoId": 1,
  "partidosJugados": 6,
  "victorias": 4,
  "derrotas": 1,
  "empates": 1,
  "puntos": 13
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Posición actualizada exitosamente",
  "data": {
    "subcategoria": {
      "subcategoriaId": 1,
      "nombre": "Fútbol"
    },
    "equipo": {
      "equipoId": 1,
      "nombre": "Equipo A"
    },
    "partidosJugados": 6,
    "victorias": 4,
    "derrotas": 1,
    "empates": 1,
    "puntos": 13
  }
}
```

### Eliminar posición de la tabla

**URL**: `/api/tabla-posicion/subcategoria/{subcategoriaId}/equipo/{equipoId}`  
**Método**: `DELETE`  
**Descripción**: Elimina una posición específica de la tabla de posiciones  
**Autenticación requerida**: Sí

**Parámetros de la URL**:
- `subcategoriaId` (requerido): ID de la subcategoría
- `equipoId` (requerido): ID del equipo

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Posición eliminada exitosamente",
  "data": null
}
```

**Códigos de error**:
- `400 Bad Request`: Si faltan campos requeridos o los datos son inválidos
- `404 Not Found`: Si no se encuentra la posición especificada
- `500 Internal Server Error`: Si ocurre un error al procesar la solicitud

## Generación de Encuentros
## Generación de Encuentros

### Generar encuentros

**URL**: `/api/encuentros/generar`  
**Método**: `POST`  
**Descripción**: Genera encuentros según el tipo de generación especificado  
**Autenticación requerida**: Sí  
**Content-Type**: `application/json`

#### 1. Generación Automática (TODOS_CONTRA_TODOS)

**Descripción**: Genera automáticamente partidos entre todos los equipos de las series seleccionadas con las siguientes características:
- Período de partidos: Del 14 de febrero al 3 de marzo de 2025
- Horarios disponibles por día: 8:00 AM, 10:00 AM, 12:00 PM, 2:00 PM, 4:00 PM
- Distribución de estadios:
    - Peguche: 60% de los partidos
    - Agato: 30% de los partidos
    - La Bolsa: 10% de los partidos
- No se permiten partidos simultáneos en el mismo estadio
- Se permiten partidos simultáneos en diferentes estadios

**Request Body**:
```json
{
  "tipoGeneracion": "TODOS_CONTRA_TODOS",
  "fechaInicio": "2025-02-14",
  "fechaFin": "2025-03-03",
  "subcategoriaId": 1
}
```

#### 2. Generación Manual (SELECCION_MANUAL)

**Descripción**: Crea encuentros específicos según la lista proporcionada.

**Request Body**:
```json
{
  "tipoGeneracion": "SELECCION_MANUAL",
  "fechaInicio": "2025-11-01",
  "fechaFin": "2025-12-31",
  "subcategoriaId": 1,
  "encuentrosManuales": [
    {
      "equipoLocalId": 1,
      "equipoVisitanteId": 2,
      "fechaHora": "2025-11-01T10:00:00",
      "estadioLugar": "Estadio Principal"
    },
    {
      "equipoLocalId": 3,
      "equipoVisitanteId": 4,
      "fechaHora": "2025-11-01T12:00:00",
      "estadioLugar": "Estadio Secundario"
    }
  ]
}
```

**Campos del Request Body**:
- `tipoGeneracion` (requerido):
    - `TODOS_CONTRA_TODOS`: Genera partidos entre todos los equipos de las series
    - `SELECCION_MANUAL`: Crea solo los partidos especificados en `encuentrosManuales`
- `fechaInicio` (requerido): Fecha de inicio para los encuentros (formato: YYYY-MM-DD)
- `fechaFin` (requerido): Fecha de fin para los encuentros (formato: YYYY-MM-DD)
- `subcategoriaId` (opcional): ID de la subcategoría para filtrar las series
- `encuentrosManuales` (requerido si tipoGeneracion es SELECCION_MANUAL): Lista de encuentros a crear manualmente, donde cada encuentro incluye:
    - `equipoLocalId` (requerido): ID del equipo local
    - `equipoVisitanteId` (requerido): ID del equipo visitante
    - `fechaHora` (requerido): Fecha y hora del partido (formato: YYYY-MM-DDThh:mm:ss)
    - `estadioLugar` (requerido): Nombre del estadio o lugar del partido

**Response (200 OK)**:
```json
[
  {
    "id": 101,
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol",
    "titulo": "Equipo A vs Equipo B",
    "fechaHora": "2025-11-01T10:00:00",
    "estadioLugar": "Estadio Principal",
    "estado": "Pendiente"
  },
  {
    "id": 102,
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol",
    "titulo": "Equipo C vs Equipo D",
    "fechaHora": "2025-11-01T12:00:00",
    "estadioLugar": "Estadio Principal",
    "estado": "Pendiente"
  }
]
```

**Códigos de error**:
- 400 Bad Request: Si faltan campos requeridos o los datos son inválidos
- 404 Not Found: Si no se encuentra la subcategoría especificada
- 500 Internal Server Error: Si ocurre un error al generar los encuentros

## Encuentros

### Crear un nuevo encuentro

**URL**: `/encuentros`  
**Método**: `POST`  
**Descripción**: Crea un nuevo encuentro  
**Autenticación requerida**: Sí  
**Content-Type**: `application/json`

**Request Body**:
```json
{
  "subcategoriaId": 1,
  "titulo": "Partido de fútbol entre Equipo A y Equipo B",
  "fechaHora": "2025-12-25T15:00:00",
  "estadioLugar": "Estadio Principal",
  "estado": "Pendiente"
}
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "subcategoriaId": 1,
  "subcategoriaNombre": "Fútbol",
  "titulo": "Partido de fútbol entre Equipo A y Equipo B",
  "fechaHora": "2025-12-25T15:00:00",
  "estadioLugar": "Estadio Principal",
  "estado": "Pendiente"
}
```

### Obtener todos los encuentros

**URL**: `/encuentros`  
**Método**: `GET`  
**Descripción**: Obtiene todos los encuentros registrados  
**Autenticación requerida**: No

**Response (200 OK)**:
```json
[
  {
    "id": 1,
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol",
    "titulo": "Partido de fútbol entre Equipo A y Equipo B",
    "fechaHora": "2025-12-25T15:00:00",
    "estadioLugar": "Estadio Principal",
    "estado": "Pendiente"
  },
  {
    "id": 2,
    "subcategoriaId": 2,
    "subcategoriaNombre": "Baloncesto",
    "titulo": "Partido de baloncesto",
    "fechaHora": "2025-12-26T18:00:00",
    "estadioLugar": "Coliseo Municipal",
    "estado": "Pendiente"
  }
]
```

### Obtener un encuentro por ID

**URL**: `/encuentros/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene un encuentro específico por su ID  
**Autenticación requerida**: No  
**Parámetros de la URL**:
- `id` (requerido): ID del encuentro

**Response (200 OK)**:
```json
{
  "id": 1,
  "subcategoriaId": 1,
  "subcategoriaNombre": "Fútbol",
  "titulo": "Partido de fútbol entre Equipo A y Equipo B",
  "fechaHora": "2025-12-25T15:00:00",
  "estadioLugar": "Estadio Principal",
  "estado": "Pendiente"
}
```

### Buscar encuentros

**URL**: `/encuentros/search`  
**Método**: `GET`  
**Descripción**: Busca encuentros según diferentes criterios  
**Autenticación requerida**: No  
**Versión**: 1.0.0

#### Parámetros de consulta

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| `subcategoriaId` | Integer | No | Filtra por ID de subcategoría |
| `fechaInicio` | DateTime | No | Fecha de inicio en formato `YYYY-MM-DD` o `YYYY-MM-DDTHH:mm:ss` |
| `fechaFin` | DateTime | No | Fecha de fin en formato `YYYY-MM-DD` o `YYYY-MM-DDTHH:mm:ss` |
| `estadioLugar` | String | No | Búsqueda por nombre o parte del nombre del estadio (case-insensitive) |
| `estado` | String | No | Estado del encuentro: `Pendiente`, `En juego`, `Finalizado`, etc. |
| `equipoId` | Integer | No | Filtra encuentros donde participe el equipo especificado |
| `page` | Integer | No | Número de página (0-based), por defecto: 0 |
| `size` | Integer | No | Tamaño de la página, por defecto: 10 |
| `sort` | String | No | Campo(s) de ordenación en formato `campo,direccion` (ej: `fechaHora,asc`) |

#### Ejemplos de uso

1. **Búsqueda básica por subcategoría**:
   ```
   GET /encuentros/search?subcategoriaId=5
   ```

2. **Búsqueda por rango de fechas**:
   ```
   GET /encuentros/search?fechaInicio=2025-01-01&fechaFin=2025-12-31
   ```

3. **Búsqueda por estadio**:
   ```
   GET /encuentros/search?estadioLugar=Peguche
   ```

4. **Búsqueda avanzada con múltiples filtros**:
   ```
   GET /encuentros/search?subcategoriaId=5&estado=Pendiente&estadioLugar=Peguche&page=0&size=20&sort=fechaHora,asc
   ```

#### Notas
- La búsqueda por `estadioLugar` es insensible a mayúsculas/minúsculas y busca coincidencias parciales
- Se pueden combinar múltiples filtros según sea necesario
- La paginación es opcional pero recomendada para grandes conjuntos de datos

**Response (200 OK)**:
```json
{
  "content": [
    {
      "id": 1,
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol",
      "fechaHora": "2025-12-25T15:00:00",
      "estadioLugar": "Estadio Principal",
      "estado": "Pendiente"
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
}
```

### Obtener encuentros por subcategoría

**URL**: `/encuentros/subcategoria/{subcategoriaId}`  
**Método**: `GET`  
**Descripción**: Obtiene todos los encuentros de una subcategoría específica  
**Autenticación requerida**: No  
**Parámetros de la URL**:
- `subcategoriaId` (requerido): ID de la subcategoría

**Response (200 OK)**:
```json
[
  {
    "id": 1,
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol",
    "titulo": "Partido de fútbol entre Equipo A y Equipo B",
    "fechaHora": "2025-12-25T15:00:00",
    "estadioLugar": "Estadio Principal",
    "estado": "Pendiente"
  },
  {
    "id": 3,
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol",
    "titulo": "Otro partido de fútbol",
    "fechaHora": "2025-12-27T16:00:00",
    "estadioLugar": "Estadio Secundario",
    "estado": "Pendiente"
  }
]
```

### Actualizar un encuentro

**URL**: `/encuentros/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza un encuentro existente  
**Autenticación requerida**: Sí  
**Content-Type**: `application/json`

**Request Body**:
```json
{
  "subcategoriaId": 1,
  "titulo": "Partido de fútbol actualizado",
  "fechaHora": "2025-12-25T16:00:00",
  "estadioLugar": "Estadio Principal",
  "estado": "En juego"
}
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "subcategoriaId": 1,
  "subcategoriaNombre": "Fútbol",
  "titulo": "Partido de fútbol actualizado",
  "fechaHora": "2025-12-25T16:00:00",
  "estadioLugar": "Estadio Principal",
  "estado": "En juego"
}
```

### Eliminar un encuentro

**URL**: `/encuentros/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina un encuentro existente  
**Autenticación requerida**: Sí

**Response (204 No Content)**: No content

**Errores**:
- `400 Bad Request`: Si la validación de los datos falla
- `401 Unauthorized`: Si no se proporciona un token de autenticación válido
- `403 Forbidden`: Si el usuario no tiene permisos para realizar la acción
- `404 Not Found`: Si el encuentro no existe
- `500 Internal Server Error`: Error interno del servidor

## Plantillas

### Añadir múltiples jugadores a un equipo (Bulk)

**URL**: `/api/plantillas/bulk`  
**Método**: `POST`  
**Descripción**: Añade múltiples jugadores a un equipo en una sola petición  
**Autenticación requerida**: Sí  
**Content-Type**: `application/json`

**Request Body**:
```json
{
  "jugadores": [
    {
      "equipoId": 1,
      "jugadorId": 1,
      "numeroCamiseta": 1,
      "rolId": 1
    },
    {
      "equipoId": 1,
      "jugadorId": 2,
      "numeroCamiseta": 10,
      "rolId": 2
    },
    {
      "equipoId": 1,
      "jugadorId": 3,
      "numeroCamiseta": 7,
      "rolId": 2
    }
  ]
}
```

**Response (201 Created)**:
```json
{
  "success": true,
  "message": "Plantillas creadas exitosamente",
  "data": [
    {
      "equipoId": 1,
      "equipoNombre": "Equipo Ejemplo",
      "jugadorId": 1,
      "jugadorNombreCompleto": "Jugador Uno",
      "numeroCamiseta": 1,
      "rolId": 1,
      "rolNombre": "PORTERO"
    },
    {
      "equipoId": 1,
      "equipoNombre": "Equipo Ejemplo",
      "jugadorId": 2,
      "jugadorNombreCompleto": "Jugador Dos",
      "numeroCamiseta": 10,
      "rolId": 2,
      "rolNombre": "JUGADOR"
    },
    {
      "equipoId": 1,
      "equipoNombre": "Equipo Ejemplo",
      "jugadorId": 3,
      "jugadorNombreCompleto": "Jugador Tres",
      "numeroCamiseta": 7,
      "rolId": 2,
      "rolNombre": "JUGADOR"
    }
  ]
}
```

**Errores**:
- `400 Bad Request`: Si la validación de los datos falla o si algún jugador ya está en el equipo
- `401 Unauthorized`: Si no se proporciona un token de autenticación válido
- `403 Forbidden`: Si el usuario no tiene permisos para realizar esta acción
- `404 Not Found`: Si algún equipo, jugador o rol no existe

### Obtener todas las plantillas

**URL**: `/api/plantillas`  
**Método**: `GET`  
**Descripción**: Obtiene todas las plantillas registradas  
**Autenticación requerida**: No

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Plantillas obtenidas exitosamente",
  "data": [
    {
      "equipoId": 1,
      "equipoNombre": "Equipo Ejemplo",
      "jugadorId": 1,
      "jugadorNombreCompleto": "Nombre Apellido",
      "numeroCamiseta": 10,
      "rolId": 1,
      "rolNombre": "JUGADOR"
    }
  ]
}
```

### Obtener una plantilla específica

**URL**: `/api/plantillas/{equipoId}/{jugadorId}`  
**Método**: `GET`  
**Descripción**: Obtiene una plantilla específica por ID de equipo y jugador  
**Autenticación requerida**: No

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Plantilla obtenida exitosamente",
  "data": {
    "equipoId": 1,
    "equipoNombre": "Equipo Ejemplo",
    "jugadorId": 1,
    "jugadorNombreCompleto": "Nombre Apellido",
    "numeroCamiseta": 10,
    "rolId": 1,
    "rolNombre": "JUGADOR"
  }
}
```

### Obtener jugadores de un equipo

**URL**: `/api/plantillas/equipo/{equipoId}`  
**Método**: `GET`  
**Descripción**: Obtiene todos los jugadores de un equipo específico  
**Autenticación requerida**: No

**Parámetros de ruta**:
- `equipoId`: ID del equipo del que se desean obtener los jugadores

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Plantilla del equipo obtenida exitosamente",
  "data": [
    {
      "equipoId": 1,
      "equipoNombre": "Equipo Ejemplo",
      "jugadorId": 1,
      "jugadorNombreCompleto": "Jugador Uno",
      "numeroCamiseta": 1,
      "rolId": 1,
      "rolNombre": "PORTERO"
    },
    {
      "equipoId": 1,
      "equipoNombre": "Equipo Ejemplo",
      "jugadorId": 2,
      "jugadorNombreCompleto": "Jugador Dos",
      "numeroCamiseta": 10,
      "rolId": 2,
      "rolNombre": "JUGADOR"
    },
    {
      "equipoId": 1,
      "equipoNombre": "Equipo Ejemplo",
      "jugadorId": 3,
      "jugadorNombreCompleto": "Jugador Tres",
      "numeroCamiseta": 7,
      "rolId": 2,
      "rolNombre": "JUGADOR"
    }
  ]
}
```

**Errores**:
- `404 Not Found`: Si el equipo no existe o no tiene jugadores registrados

### Crear una nueva plantilla

**URL**: `/api/plantillas`  
**Método**: `POST`  
**Descripción**: Crea una nueva plantilla  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Request Body**:
```json
{
  "equipoId": 1,
  "jugadorId": 1,
  "numeroCamiseta": 10,
  "rolId": 1
}
```

**Response (201 Created)**:
```json
{
  "success": true,
  "message": "Plantilla creada exitosamente",
  "data": {
    "equipoId": 1,
    "equipoNombre": "Equipo Ejemplo",
    "jugadorId": 1,
    "jugadorNombreCompleto": "Nombre Apellido",
    "numeroCamiseta": 10,
    "rolId": 1,
    "rolNombre": "JUGADOR"
  }
}
```

### Eliminar una plantilla

**URL**: `/api/plantillas/{equipoId}/{jugadorId}`  
**Método**: `DELETE`  
**Descripción**: Elimina una plantilla específica por ID de equipo y jugador  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Response (204 No Content)**:
```
// No content
```

---


## Equipos

### Obtener equipos por serie

**URL**: `/equipos/serie/{serieId}`  
**Método**: `GET`  
**Descripción**: Obtiene todos los equipos de una serie específica  
**Autenticación requerida**: No

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Equipos obtenidos exitosamente",
  "data": [
    {
      "equipoId": 1,
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol",
      "serieId": 1,
      "serieNombre": "Serie A",
      "nombre": "Equipo Ejemplo",
      "fundacion": "1900-01-01"
    }
  ]
}
```

### Obtener equipos por subcategoría

**URL**: `/equipos/subcategoria/{subcategoriaId}`  
**Método**: `GET`  
**Descripción**: Obtiene una lista de equipos filtrados por subcategoría y opcionalmente por serie.  
**Autenticación Requerida**: No

**Parámetros de ruta**:
- `subcategoriaId` (requerido): ID de la subcategoría

**Parámetros de consulta**:
- `serieId` (opcional): ID de la serie para filtrar adicionalmente por serie

**Ejemplos de solicitud**:

1. Obtener todos los equipos de una subcategoría:
```
GET /equipos/subcategoria/1
```

2. Obtener equipos de una subcategoría filtrados por serie:
```
GET /equipos/subcategoria/1?serieId=2
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Equipos obtenidos exitosamente (filtrados por subcategoría: 1 y serie: 2)",
  "data": [
    {
      "equipoId": 1,
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol",
      "serieId": 2,
      "serieNombre": "Serie B",
      "nombre": "Equipo A",
      "fundacion": "2000-01-01",
      "jugadoresCount": 2
    },
    {
      "equipoId": 2,
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol",
      "serieId": 2,
      "serieNombre": "Serie B",
      "nombre": "Equipo B",
      "fundacion": "2001-05-15",
      "jugadoresCount": 3
    }
  ]
}
```

**Respuesta cuando no se encuentran equipos**:
```json
{
  "success": true,
  "message": "No se encontraron equipos para los criterios especificados",
  "data": []
}
```

---

### Crear un nuevo equipo

**URL**: `/api/equipos`  
**Método**: `POST`  
**Descripción**: Crea un nuevo equipo  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Request Body**:
```json
{
  "subcategoriaId": 1,
  "serieId": 1,
  "nombre": "Nuevo Equipo",
  "fundacion": "2000-01-01"
}
```

**Response (201 Created)**:
```json
{
  "success": true,
  "message": "Equipo creado exitosamente",
  "data": {
    "equipoId": 1,
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol",
    "serieId": 1,
    "serieNombre": "Serie A",
    "nombre": "Nuevo Equipo",
    "fundacion": "2000-01-01"
  }
}
```

---

### Crear múltiples equipos en lote

**URL**: `/api/equipos/bulk`  
**Método**: `POST`  
**Descripción**: Crea múltiples equipos en una sola operación  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Request Body**:
```json
{
  "equipos": [
    {
      "subcategoriaId": 1,
      "serieId": 1,
      "nombre": "Equipo A",
      "fundacion": "2000-01-01"
    },
    {
      "subcategoriaId": 1,
      "serieId": 1,
      "nombre": "Equipo B",
      "fundacion": "2001-01-01"
    }
  ]
}
```

**Response (201 Created)**:
```json
{
  "success": true,
  "message": "Equipos creados exitosamente",
  "data": [
    {
      "equipoId": 1,
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol",
      "serieId": 1,
      "serieNombre": "Serie A",
      "nombre": "Equipo A",
      "fundacion": "2000-01-01"
    },
    {
      "equipoId": 2,
      "subcategoriaId": 1,
      "subcategoriaNombre": "Fútbol",
      "serieId": 1,
      "serieNombre": "Serie A",
      "nombre": "Equipo B",
      "fundacion": "2001-01-01"
    }
  ]
}
```

**Errores**:
- `400 Bad Request`: Si la lista de equipos está vacía o contiene datos inválidos
- `400 Bad Request`: Si hay equipos duplicados en la misma serie dentro de la misma solicitud
- `400 Bad Request`: Si ya existe un equipo con el mismo nombre en la misma serie
- `404 Not Found`: Si la subcategoría o serie especificada no existe

---

## Jugadores

### Obtener jugadores con paginación y búsqueda

**URL**: `/jugadores`  
**Método**: `GET`  
**Descripción**: Obtiene una lista paginada de jugadores con opciones de búsqueda y ordenación.  
**Autenticación Requerida**: No  

**Parámetros de Consulta**:
- `page` (opcional): Número de página (comenzando desde 0). Por defecto: 0
- `size` (opcional): Cantidad de elementos por página. Por defecto: 10
- `sort` (opcional): Campo de ordenación (ej: `apellido,asc` o `nombre,desc`). Campos disponibles: `id`, `nombre`, `apellido`, `fechaNacimiento`, `documentoIdentidad`
- `search` (opcional): Texto para buscar jugadores por nombre o apellido

**Ejemplo de solicitud**:
```http
GET /jugadores?page=0&size=10&sort=apellido,asc&search=González HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
```

**Respuesta Exitosa (200 OK)**:
```json
{
  "success": true,
  "message": "Se encontraron 5 jugador(es) con nombre o apellido que contiene: González",
  "data": {
    "content": [
      {
        "id": 1,
        "nombre": "Carlos",
        "apellido": "González",
        "fechaNacimiento": "1995-05-15",
        "documentoIdentidad": "12345678",
        "equipoId": 1,
        "equipoNombre": "Equipo A"
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
    "first": true,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "numberOfElements": 1,
    "size": 10,
    "number": 0,
    "empty": false
  }
}
```

### Obtener jugador por ID

**URL**: `/jugadores/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene los detalles de un jugador específico.  
**Autenticación Requerida**: No  

**Parámetros de Ruta**:
- `id`: ID del jugador a consultar

**Ejemplo de solicitud**:
```http
GET /jugadores/1 HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
```

### Crear jugador

**URL**: `/jugadores`  
**Método**: `POST`  
**Descripción**: Crea un nuevo jugador.  
**Autenticación Requerida**: Sí (Rol: ADMIN)  

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Nuevo",
  "apellido": "Jugador",
  "fechaNacimiento": "2000-01-01",
  "documentoIdentidad": "87654321",
  "equipoId": 1
}
```

### Actualizar jugador

**URL**: `/jugadores/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza los datos de un jugador existente.  
**Autenticación Requerida**: Sí (Rol: ADMIN)  

**Parámetros de Ruta**:
- `id`: ID del jugador a actualizar

**Cuerpo de la Solicitud**:
```json
{
  "nombre": "Nombre Actualizado",
  "apellido": "Apellido Actualizado",
  "fechaNacimiento": "2000-01-01",
  "documentoIdentidad": "87654321",
  "equipoId": 2
}
```

### Eliminar jugador

**URL**: `/jugadores/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina un jugador.  
**Autenticación Requerida**: Sí (Rol: ADMIN)  

**Parámetros de Ruta**:
- `id`: ID del jugador a eliminar

### Obtener conteo de jugadores

**URL**: `/jugadores/count`  
**Método**: `GET`  
**Descripción**: Obtiene el conteo total de jugadores.  
**Autenticación Requerida**: No  

**Ejemplo de respuesta exitosa (200 OK)**:
```json
{
  "success": true,
  "message": "Conteo de jugadores obtenido exitosamente",
  "data": {
    "totalJugadores": 124,
    "jugadoresActivos": 120,
    "jugadoresInactivos": 4
  }
}
```

**URL**: `/jugadores`  
**Método**: `GET`  
**Descripción**: Obtiene una lista paginada de jugadores con opciones de búsqueda y ordenación.  
**Autenticación Requerida**: Depende de la configuración de seguridad  
**Roles**: Depende de la configuración de seguridad

**Parámetros de Consulta**:
- `page` (opcional): Número de página (comenzando desde 0). Por defecto: 0
- `size` (opcional): Cantidad de elementos por página. Por defecto: 10
- `sort` (opcional): Campo de ordenación (ej: `apellido,asc` o `nombre,desc`). Por defecto: `apellido,asc`
- `search` (opcional): Texto para buscar jugadores por nombre o apellido (búsqueda insensible a mayúsculas/minúsculas)

**Campos ordenables**: `id`, `nombre`, `apellido`, `fechaNacimiento`, `documentoIdentidad`

**Ejemplos de solicitud**:

1. Obtener primera página de jugadores (10 por defecto):
```http
GET /jugadores HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
```

2. Buscar jugadores por nombre o apellido:
```http
GET /jugadores?search=Carlos HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
```

3. Paginación personalizada y ordenamiento:
```http
GET /jugadores?page=1&size=20&sort=nombre,asc HTTP/1.1
Host: api.ejemplo.com
Accept: application/json
```

**Respuesta exitosa (200 OK)**:
```json
{
  "success": true,
  "message": "Jugadores obtenidos exitosamente",
  "data": {
    "content": [
      {
        "id": 1,
        "nombre": "Carlos",
        "apellido": "Pérez",
        "fechaNacimiento": "1990-01-15",
        "documentoIdentidad": "12345678"
      },
      {
        "id": 2,
        "nombre": "Ana",
        "apellido": "Gómez",
        "fechaNacimiento": "1992-05-20",
        "documentoIdentidad": "87654321"
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
    "last": false,
    "totalPages": 5,
    "totalElements": 42,
    "first": true,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "numberOfElements": 10,
    "size": 10,
    "number": 0,
    "empty": false
  }
}
```

**URL**: `/jugadores`  
**Método**: `GET`  
**Descripción**: Obtiene una lista paginada de jugadores con opciones de búsqueda y ordenación.  
**Autenticación Requerida**: Depende de la configuración de seguridad  
**Roles**: Depende de la configuración de seguridad

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Jugadores obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Juan",
      "apellido": "Pérez",
      "fechaNacimiento": "1990-01-01",
      "documentoIdentidad": "12345678"
    }
  ]
}
```

---

### Obtener un jugador por ID

**URL**: `/api/jugadores/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene un jugador por su ID  
**Autenticación requerida**: No

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Jugador obtenido exitosamente",
  "data": {
    "id": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "fechaNacimiento": "1990-01-01",
    "documentoIdentidad": "12345678"
  }
}
```

---

### Crear un nuevo jugador

**URL**: `/api/jugadores`  
**Método**: `POST`  
**Descripción**: Crea un nuevo jugador  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Request Body**:
```json
{
  "nombre": "Carlos",
  "apellido": "González",
  "fechaNacimiento": "1995-05-15",
  "documentoIdentidad": "87654321"
}
```

**Response (201 Created)**:
```json
{
  "success": true,
  "message": "Jugador creado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Carlos",
    "apellido": "González",
    "fechaNacimiento": "1995-05-15",
    "documentoIdentidad": "87654321"
  }
}
```

---

### Crear múltiples jugadores en lote

**URL**: `/api/jugadores/bulk`  
**Método**: `POST`  
**Descripción**: Crea múltiples jugadores en una sola operación  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Request Body**:
```json
{
  "jugadores": [
    {
      "nombre": "Jugador 1",
      "apellido": "Apellido 1",
      "fechaNacimiento": "1990-01-01",
      "documentoIdentidad": "11111111"
    },
    {
      "nombre": "Jugador 2",
      "apellido": "Apellido 2",
      "fechaNacimiento": "1991-02-02",
      "documentoIdentidad": "22222222"
    }
  ]
}
```

**Response (201 Created)**:
```json
{
  "success": true,
  "message": "Jugadores creados exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Jugador 1",
      "apellido": "Apellido 1",
      "fechaNacimiento": "1990-01-01",
      "documentoIdentidad": "11111111"
    },
    {
      "id": 2,
      "nombre": "Jugador 2",
      "apellido": "Apellido 2",
      "fechaNacimiento": "1991-02-02",
      "documentoIdentidad": "22222222"
    }
  ]
}
```

**Errores**:
- `400 Bad Request`: Si la lista de jugadores está vacía o contiene datos inválidos
- `400 Bad Request`: Si hay jugadores con el mismo documento de identidad en la misma solicitud
- `400 Bad Request`: Si ya existe un jugador con el mismo documento de identidad

---

### Actualizar un jugador

**URL**: `/api/jugadores/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza un jugador existente  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Request Body**:
```json
{
  "nombre": "Carlos",
  "apellido": "González López",
  "fechaNacimiento": "1995-05-15",
  "documentoIdentidad": "87654321"
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Jugador actualizado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Carlos",
    "apellido": "González López",
    "fechaNacimiento": "1995-05-15",
    "documentoIdentidad": "87654321"
  }
}
```

---

### Eliminar un jugador

**URL**: `/api/jugadores/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina un jugador existente  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Jugador eliminado exitosamente"
}
```

**Errores**:
- `404 Not Found`: Si el jugador con el ID especificado no existe
- `400 Bad Request`: Si el jugador no puede ser eliminado por restricciones de integridad referencial

---

### Actualizar un equipo existente

**URL**: `/api/equipos/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza los datos de un equipo existente.  
**Validaciones**:
- El nombre del equipo debe ser único dentro de la misma subcategoría (no se permiten nombres duplicados, sin distinguir mayúsculas/minúsculas)
- La subcategoría y la serie deben existir

**Autenticación requerida**: Sí (Rol: ADMIN)

**Request Body**:
```json
{
  "subcategoriaId": 1,
  "serieId": 1,
  "nombre": "Equipo Actualizado",
  "fundacion": "2001-01-01"
}
```

**Códigos de error**:
- `400 Bad Request`: Si ya existe un equipo con el mismo nombre en la misma categoría
- `404 Not Found`: Si el equipo, subcategoría o serie no existen

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Equipo actualizado exitosamente",
  "data": {
    "equipoId": 1,
    "subcategoriaId": 1,
    "subcategoriaNombre": "Fútbol",
    "serieId": 1,
    "serieNombre": "Serie A",
    "nombre": "Equipo Actualizado",
    "fundacion": "2001-01-01"
  }
}
```

**Ejemplo de error (400 Bad Request)**:
```json
{
  "timestamp": "2025-10-20T13:07:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Ya existe un equipo con el nombre 'Equipo Actualizado' en la categoría: Fútbol",
  "path": "/api/equipos/1"
}
```

---

### Eliminar un equipo

**URL**: `/api/equipos/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina un equipo existente  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Equipo eliminado exitosamente",
  "data": null
}
```

---

## Series

### Crear una nueva serie

**URL**: `/api/series`  
**Método**: `POST`  
**Descripción**: Crea una nueva serie  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Request Body**:
```json
{
  "subcategoriaId": 1,
  "nombreSerie": "Nombre de la serie"
}
```

**Response (201 Created)**:
```json
{
  "success": true,
  "message": "Serie creada exitosamente",
  "data": {
    "serieId": 1,
    "subcategoriaId": 1,
    "subcategoriaNombre": "Nombre de la subcategoría",
    "nombreSerie": "Nombre de la serie"
  }
}
```

---

### Crear múltiples series en lote

**URL**: `/api/series/bulk`  
**Método**: `POST`  
**Descripción**: Crea múltiples series en una sola operación  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Request Body**:
```json
{
  "series": [
    {
      "subcategoriaId": 1,
      "nombreSerie": "Serie A"
    },
    {
      "subcategoriaId": 1,
      "nombreSerie": "Serie B"
    }
  ]
}
```

**Response (201 Created)**:
```json
{
  "success": true,
  "message": "Series creadas exitosamente",
  "data": [
    {
      "serieId": 1,
      "subcategoriaId": 1,
      "subcategoriaNombre": "Nombre de la subcategoría",
      "nombreSerie": "Serie A"
    },
    {
      "serieId": 2,
      "subcategoriaId": 1,
      "subcategoriaNombre": "Nombre de la subcategoría",
      "nombreSerie": "Serie B"
    }
  ]
}
```

---

### Obtener series por subcategoría

**URL**: `/api/series/subcategoria/{subcategoriaId}`  
**Método**: `GET`  
**Descripción**: Obtiene todas las series de una subcategoría específica  
**Autenticación requerida**: No

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Series obtenidas exitosamente",
  "data": [
    {
      "serieId": 1,
      "subcategoriaId": 1,
      "subcategoriaNombre": "Nombre de la subcategoría",
      "nombreSerie": "Nombre de la serie 1"
    },
    {
      "serieId": 2,
      "subcategoriaId": 1,
      "subcategoriaNombre": "Nombre de la subcategoría",
      "nombreSerie": "Nombre de la serie 2"
    }
  ]
}
```

---

### Obtener una serie por ID

**URL**: `/api/series/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene los detalles de una serie específica  
**Autenticación requerida**: No

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Serie obtenida exitosamente",
  "data": {
    "serieId": 1,
    "subcategoriaId": 1,
    "subcategoriaNombre": "Nombre de la subcategoría",
    "nombreSerie": "Nombre de la serie"
  }
}
```

---

### Actualizar una serie

**URL**: `/api/series/{id}`  
**Método**: `PUT`  
**Descripción**: Actualiza los datos de una serie existente  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Request Body**:
```json
{
  "subcategoriaId": 1,
  "nombreSerie": "Nuevo nombre de la serie"
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Serie actualizada exitosamente",
  "data": {
    "serieId": 1,
    "subcategoriaId": 1,
    "subcategoriaNombre": "Nombre de la subcategoría",
    "nombreSerie": "Nuevo nombre de la serie"
  }
}
```

---

### Eliminar una serie

**URL**: `/api/series/{id}`  
**Método**: `DELETE`  
**Descripción**: Elimina una serie existente  
**Autenticación requerida**: Sí (Rol: ADMIN)

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Serie eliminada exitosamente",
  "data": null
}
```

---


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

### Actualizar categoría

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


## Subcategorías

### Crear múltiples subcategorías (Bulk)

**URL**: `/subcategorias/bulk`  
**Método**: `POST`  
**Descripción**: Crea múltiples subcategorías en una sola petición  
**Autenticación requerida**: Sí (Rol: ADMIN)  
**Content-Type**: `application/json`

**Request Body**:
```json
{
  "subcategorias": [
    {
      "categoriaId": 1,
      "nombre": "Subcategoría 1",
      "descripcion": "Descripción de la subcategoría 1"
    },
    {
      "categoriaId": 1,
      "nombre": "Subcategoría 2",
      "descripcion": "Descripción de la subcategoría 2"
    }
  ]
}
```

**Response (201 Created)**:
```json
{
  "success": true,
  "message": "Subcategorías creadas exitosamente",
  "data": [
    {
      "subcategoriaId": 1,
      "nombre": "Subcategoría 1",
      "descripcion": "Descripción de la subcategoría 1",
      "categoriaId": 1
    },
    {
      "subcategoriaId": 2,
      "nombre": "Subcategoría 2",
      "descripcion": "Descripción de la subcategoría 2",
      "categoriaId": 1
    }
  ]
}
```

**Errores**:
- `400 Bad Request`: Si la validación de los datos falla o si la categoría especificada no existe
- `401 Unauthorized`: Si no se proporciona un token de autenticación válido
- `403 Forbidden`: Si el usuario no tiene permisos de administrador

### Listar todas las subcategorías

**URL**: `/api/subcategorias`  
**Método**: `GET`  
**Descripción**: Obtiene la lista de todas las subcategorías

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Subcategorías obtenidas exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Subcategoría 1",
      "descripcion": "Descripción de la subcategoría 1",
      "categoria": {
        "id": 1,
        "nombre": "Categoría 1"
      }
    }
  ]
}
```

### Obtener subcategoría por ID

**URL**: `/api/subcategorias/{id}`  
**Método**: `GET`  
**Descripción**: Obtiene una subcategoría específica por su ID

**Parámetros de ruta**:
- `id`: ID de la subcategoría

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Subcategoría encontrada",
  "data": {
    "id": 1,
    "nombre": "Subcategoría 1",
    "descripcion": "Descripción de la subcategoría 1",
    "categoria": {
      "id": 1,
      "nombre": "Categoría 1"
    }
  }
}
```

### Crear subcategoría

**URL**: `/api/subcategorias`  
**Método**: `POST`  
**Roles Requeridos**: `ROLE_MODERATOR`, `ROLE_ADMIN`

**Request Body**:
```json
{
  "categoriaId": 1,
  "nombre": "Nueva Subcategoría",
  "descripcion": "Descripción de la nueva subcategoría"
}
```

**Response (201 Created)**:
```json
{
  "success": true,
  "message": "Subcategoría creada exitosamente",
  "data": {
    "id": 2,
    "nombre": "Nueva Subcategoría",
    "descripcion": "Descripción de la nueva subcategoría",
    "categoria": {
      "id": 1,
      "nombre": "Categoría 1"
    }
  }
}
```

### Actualizar subcategoría

**URL**: `/api/subcategorias/{id}`  
**Método**: `PUT`  
**Roles Requeridos**: `ROLE_MODERATOR`, `ROLE_ADMIN`

**Parámetros de ruta**:
- `id`: ID de la subcategoría a actualizar

**Request Body**:
```json
{
  "categoriaId": 1,
  "nombre": "Subcategoría Actualizada",
  "descripcion": "Descripción actualizada"
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Subcategoría actualizada exitosamente",
  "data": {
    "id": 1,
    "nombre": "Subcategoría Actualizada",
    "descripcion": "Descripción actualizada",
    "categoria": {
      "id": 1,
      "nombre": "Categoría 1"
    }
  }
}
```

### Eliminar subcategoría

**URL**: `/api/subcategorias/{id}`  
**Método**: `DELETE`  
**Roles Requeridos**: `ROLE_MODERATOR`, `ROLE_ADMIN`

**Parámetros de ruta**:
- `id`: ID de la subcategoría a eliminar

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Subcategoría eliminada exitosamente"
}
```

## Roles por Subcategoría

### Obtener roles por ID de subcategoría

**URL**: `/api/subcategoria-roles/subcategoria/{subcategoriaId}`  
**Método**: `GET`  
**Descripción**: Obtiene los roles asociados a una subcategoría específica por su ID

**Parámetros de ruta**:
- `subcategoriaId`: ID de la subcategoría

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Roles obtenidos correctamente",
  "data": [
    {
      "id": 1,
      "name": "ROLE_USER",
      "detail": "Usuario estándar"
    },
    {
      "id": 2,
      "name": "ADMIN",
      "detail": "Administrador"
    }
  ]
}
```

### Obtener roles por nombre de subcategoría

**URL**: `/api/subcategoria-roles/subcategoria/nombre/{nombreSubcategoria}`  
**Método**: `GET`  
**Descripción**: Obtiene los roles asociados a una subcategoría específica por su nombre

**Parámetros de ruta**:
- `nombreSubcategoria`: Nombre de la subcategoría (ej: "Fútbol", "Baloncesto")

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Roles obtenidos correctamente para la subcategoría: Fútbol",
  "data": [
    {
      "id": 3,
      "name": "JUGADOR",
      "detail": "Jugador de fútbol"
    },
    {
      "id": 4,
      "name": "ENTRENADOR",
      "detail": "Entrenador de fútbol"
    }
  ]
}
```

### Asignar rol a subcategoría

**URL**: `/api/subcategoria-roles/subcategoria/{subcategoriaId}/rol/{rolId}`  
**Método**: `POST`  
**Roles Requeridos**: `ROLE_ADMIN`

**Parámetros de ruta**:
- `subcategoriaId`: ID de la subcategoría
- `rolId`: ID del rol a asignar

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Rol asignado correctamente a la subcategoría"
}
```

### Eliminar rol de subcategoría

**URL**: `/api/subcategoria-roles/subcategoria/{subcategoriaId}/rol/{rolId}`  
**Método**: `DELETE`  
**Roles Requeridos**: `ROLE_ADMIN`

**Parámetros de ruta**:
- `subcategoriaId`: ID de la subcategoría
- `rolId`: ID del rol a eliminar

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Rol eliminado correctamente de la subcategoría"
}
```

### Asignar múltiples roles a subcategoría

**URL**: `/api/subcategoria-roles/bulk`  
**Método**: `POST`  
**Roles Requeridos**: `ROLE_ADMIN`

**Request Body**:
```json
{
  "subcategoriaId": 1,
  "roles": [1, 2, 3]
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Roles asignados correctamente a la subcategoría",
  "data": {
    "asignados": 3,
    "fallidos": 0
  }
}
```


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