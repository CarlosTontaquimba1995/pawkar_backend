-- TABLAS DE USUARIOS Y ROLES
----------------------------------------------------------------------------------------------------

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN DEFAULT true
);


-- Tabla de Roles
CREATE TABLE IF NOT EXISTS roles (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(50) NOT NULL UNIQUE,
     detail VARCHAR(100) NOT NULL unique,
     estado BOOLEAN default true,
     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
 );

-- Tabla de Unión Usuario-Roles
CREATE TABLE IF NOT EXISTS user_roles (
     user_id BIGINT NOT NULL,
     role_id BIGINT NOT NULL,
     PRIMARY KEY (user_id, role_id),
     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
     FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

----------------------------------------------------------------------------------------------------
-- TABLAS DE CATEGORÍAS Y ORGANIZACIÓN DE COMPETENCIAS
----------------------------------------------------------------------------------------------------

-- 1. Categorias Padre
CREATE TABLE public.categorias (
    categoria_id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL unique,
    nemonico VARCHAR(100) NOT NULL unique,
    estado boolean default true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
);

-- 2. Subcategorias (Deportes, Sub-eventos, Tipos de comida)
CREATE TABLE public.subcategorias (
    subcategoria_id BIGSERIAL PRIMARY KEY,
    categoria_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(100),
    descripcion TEXT,
    fecha_hora TIMESTAMP WITHOUT TIME ZONE,
    proximo BOOLEAN default true, -- CAMPO AÑADIDO: Para indicar si el equipo está activo
    estado BOOLEAN default true, -- CAMPO AÑADIDO: Para indicar si el equipo está activo
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoria_id) REFERENCES categorias (categoria_id) ON DELETE CASCADE
);

-- Tabla de Unión Subcategoría-Roles (Define qué roles de jugador aplican a cada subcategoría)
CREATE TABLE public.subcategoria_roles (
    subcategoria_id INT NOT NULL,
    rol_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (subcategoria_id, rol_id),
    FOREIGN KEY (subcategoria_id) REFERENCES subcategorias (subcategoria_id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles (id) ON DELETE RESTRICT
);

-- Tabla de Series (Grupos dentro de una subcategoría, ej: Serie A, Serie B de Fútbol)
CREATE TABLE public.series (
    serie_id BIGSERIAL PRIMARY KEY,
    subcategoria_id INT NOT NULL,
    nombre_serie VARCHAR(100) NOT NULL,
    estado BOOLEAN default true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (subcategoria_id, nombre_serie),
    FOREIGN KEY (subcategoria_id) REFERENCES subcategorias (subcategoria_id) ON DELETE CASCADE
);


-- Creación de la nueva tabla de estadios
CREATE TABLE public.estadios (
    estadio_id BIGSERIAL PRIMARY KEY, -- Clave primaria para identificar el estadio
    nombre VARCHAR(150) NOT NULL UNIQUE, -- Nombre del estadio
    detalle TEXT, -- Campo para agregar más información/detalle (opcionalmente VARCHAR)
    estado BOOLEAN default true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
);

-- 3. Equipos (Asociados a un deporte/subcategoría)
CREATE TABLE public.equipos (
    equipo_id BIGSERIAL PRIMARY KEY,
    subcategoria_id INT NOT NULL,
    serie_id INT NOT NULL, -- CAMPO AÑADIDO: Para clasificar el equipo en una serie/grupo
    nombre VARCHAR(100) NOT NULL,
    fundacion DATE,
    estado BOOLEAN default true, -- CAMPO AÑADIDO: Para indicar si el equipo está activo
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subcategoria_id) REFERENCES subcategorias (subcategoria_id) ON DELETE CASCADE,
    FOREIGN KEY (serie_id) REFERENCES series (serie_id) ON DELETE RESTRICT
);

----------------------------------------------------------------------------------------------------
-- TABLAS DE PERSONAS, ROLES Y PLANTILLA
----------------------------------------------------------------------------------------------------

-- 4. Jugadores (Registro maestro de personas)
CREATE TABLE public.jugadores (
    jugador_id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE,
    estado boolean default true, -- CAMPO AÑADIDO: Para indicar si el jugador está activo
    documento_identidad VARCHAR(20) unique,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
);

-- 5. Plantilla (Jugadores por equipo)
CREATE TABLE public.plantilla (
    equipo_id INT NOT NULL,
    jugador_id INT NOT NULL,
    numero_camiseta INT,
    rol_id BIGINT NOT NULL, -- CAMPO CORREGIDO: Referencia el ID (BIGINT) de la tabla 'roles', no el 'name'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (equipo_id, jugador_id),
    FOREIGN KEY (equipo_id) REFERENCES equipos (equipo_id) ON DELETE CASCADE,
    FOREIGN KEY (jugador_id) REFERENCES jugadores (jugador_id) ON DELETE RESTRICT,
    FOREIGN KEY (rol_id) REFERENCES roles (id) ON DELETE RESTRICT -- REFERENCIA CORREGIDA
);

----------------------------------------------------------------------------------------------------
-- TABLAS DE ENCUENTROS Y RESULTADOS
----------------------------------------------------------------------------------------------------

-- 6. Encuentros (Partidos, Horarios, Degustaciones, Presentaciones)

CREATE TABLE public.encuentros (
    encuentro_id BIGSERIAL PRIMARY KEY,
    subcategoria_id INT NOT NULL,
    estadio_id BIGINT NOT NULL, 
    
    -- NUEVOS CAMPOS DE RELACIÓN CON EQUIPOS
    equipo_local_id BIGINT NOT NULL,       -- ID del equipo que juega como local
    equipo_visitante_id BIGINT NOT NULL,   -- ID del equipo que juega como visitante
    
    titulo VARCHAR(150) NOT NULL,
    fecha_hora TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    estado VARCHAR(50) DEFAULT 'PROGRAMADO',
    activo boolean default true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- RELACIÓN 1: SUB-CATEGORÍAS
    FOREIGN KEY (subcategoria_id) REFERENCES public.subcategorias (subcategoria_id) ON DELETE RESTRICT,
    
    -- RELACIÓN 2: ESTADIOS
    FOREIGN KEY (estadio_id) REFERENCES public.estadios (estadio_id) ON DELETE RESTRICT,
    
    -- RELACIÓN 3: EQUIPO LOCAL
    CONSTRAINT fk_equipo_local
        FOREIGN KEY (equipo_local_id)
        REFERENCES public.equipos (equipo_id) ON DELETE RESTRICT,
        
    -- RELACIÓN 4: EQUIPO VISITANTE
    CONSTRAINT fk_equipo_visitante
        FOREIGN KEY (equipo_visitante_id)
        REFERENCES public.equipos (equipo_id) ON DELETE RESTRICT
);

-- 7. Participacion_Encuentro (Quién juega contra quién, resultados del partido)
CREATE TABLE public.participacion_encuentro (
    encuentro_id INT NOT NULL,
    equipo_id INT NOT NULL,
    es_local BOOLEAN DEFAULT FALSE,
    goles_puntos INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (encuentro_id, equipo_id),
    FOREIGN KEY (encuentro_id) REFERENCES encuentros (encuentro_id) ON DELETE CASCADE,
    FOREIGN KEY (equipo_id) REFERENCES equipos (equipo_id) ON DELETE RESTRICT
);

-- 8. Tabla_Posiciones (Resultados acumulados por subcategoría/deporte)
CREATE TABLE public.tabla_posiciones (
    subcategoria_id INT NOT NULL,
    equipo_id INT NOT NULL,
    partidos_jugados INT DEFAULT 0,
    victorias INT DEFAULT 0,
    derrotas INT DEFAULT 0,
    empates INT DEFAULT 0,
    puntos INT DEFAULT 0,
    goles_a_favor INT DEFAULT 0,     -- CAMPO AÑADIDO: Goles/puntos anotados
    goles_en_contra INT DEFAULT 0,   -- CAMPO AÑADIDO: Goles/puntos recibidos
    diferencia_goles INT DEFAULT 0,  -- CAMPO AÑADIDO: Diferencia de goles/puntos
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (subcategoria_id, equipo_id),
    FOREIGN KEY (subcategoria_id) REFERENCES subcategorias (subcategoria_id) ON DELETE CASCADE,
    FOREIGN KEY (equipo_id) REFERENCES equipos (equipo_id) ON DELETE CASCADE
);

-- 9. Sanciones (Tarjetas, penalizaciones)
CREATE TABLE public.sanciones (
    sancion_id BIGSERIAL PRIMARY KEY,
    jugador_id INT NOT NULL,
    encuentro_id INT,
    tipo_sancion VARCHAR(50) NOT NULL,
    motivo TEXT,
    detalle_sancion TEXT,
    fecha_registro DATE DEFAULT CURRENT_DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (jugador_id) REFERENCES jugadores (jugador_id) ON DELETE RESTRICT,
    FOREIGN KEY (encuentro_id) REFERENCES encuentros (encuentro_id) ON DELETE SET NULL
);