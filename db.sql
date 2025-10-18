-- Create tables for the application
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN DEFAULT true
);

-- Add more tables as needed for your application
-- Example:
CREATE TABLE IF NOT EXISTS roles (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(50) NOT NULL unique,
     detail VARCHAR(100) NOT NULL UNIQUE
 );

INSERT INTO roles (name, detail) VALUES 
('ROLE_USER','Rol usuario'), ('ROLE_ADMIN','Rol administrador');


CREATE TABLE IF NOT EXISTS user_roles (
     user_id BIGINT NOT NULL,
     role_id BIGINT NOT NULL,
     PRIMARY KEY (user_id, role_id),
     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
     FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);



-- 1. Categorias Padre
CREATE TABLE categorias (
    categoria_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- 2. Subcategorias (Deportes, Sub-eventos, Tipos de comida)
CREATE TABLE subcategorias (
    subcategoria_id SERIAL PRIMARY KEY,
    categoria_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    FOREIGN KEY (categoria_id) REFERENCES categorias (categoria_id) ON DELETE CASCADE
);

-- DDL para la nueva tabla de mapeo de roles por subcategoría
CREATE TABLE subcategoria_roles (
    subcategoria_id INT NOT NULL,
    rol_id BIGINT NOT NULL,
    
    PRIMARY KEY (subcategoria_id, rol_id),
    
    FOREIGN KEY (subcategoria_id) 
        REFERENCES subcategorias (subcategoria_id) 
        ON DELETE CASCADE,
        
    FOREIGN KEY (rol_id) 
        REFERENCES roles (id) 
        ON DELETE RESTRICT
);


-- 3. Equipos (Asociados a un deporte/subcategoría)
CREATE TABLE equipos (
    equipo_id SERIAL PRIMARY KEY,
    subcategoria_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    fundacion DATE,
    FOREIGN KEY (subcategoria_id) REFERENCES subcategorias (subcategoria_id) ON DELETE CASCADE
);

-- 4. Jugadores (Registro maestro de personas)
CREATE TABLE jugadores (
    jugador_id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE,
    documento_identidad VARCHAR(20) UNIQUE
);

-- 5. Plantilla (Jugadores por equipo con rol ID)
CREATE TABLE plantilla (
    equipo_id INT NOT NULL,
    jugador_id INT NOT NULL,
    numero_camiseta INT,
    rol_id BIGINT NOT NULL, -- Ahora usamos el ID del rol
    
    PRIMARY KEY (equipo_id, jugador_id),
    
    FOREIGN KEY (equipo_id) 
        REFERENCES equipos (equipo_id) 
        ON DELETE CASCADE,
        
    FOREIGN KEY (jugador_id) 
        REFERENCES jugadores (jugador_id) 
        ON DELETE RESTRICT,
        
    FOREIGN KEY (rol_id) 
        REFERENCES roles (id) 
        ON DELETE RESTRICT -- FK al ID del rol
);

-- 6. Encuentros (Partidos, Horarios, Degustaciones, Presentaciones)
CREATE TABLE encuentros (
    encuentro_id SERIAL PRIMARY KEY,
    subcategoria_id INT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    estadio_lugar VARCHAR(150) NOT NULL,
    estado VARCHAR(50) DEFAULT 'Pendiente', -- Ej: Pendiente, Finalizado, Cancelado
    FOREIGN KEY (subcategoria_id) REFERENCES subcategorias (subcategoria_id) ON DELETE RESTRICT
);

-- 7. Participacion_Encuentro (Quién juega contra quién, solo para deportes)
CREATE TABLE participacion_encuentro (
    encuentro_id INT NOT NULL,
    equipo_id INT NOT NULL,
    es_local BOOLEAN DEFAULT FALSE,
    goles_puntos INT DEFAULT 0,
    PRIMARY KEY (encuentro_id, equipo_id),
    FOREIGN KEY (encuentro_id) REFERENCES Encuentros (encuentro_id) ON DELETE CASCADE,
    FOREIGN KEY (equipo_id) REFERENCES Equipos (equipo_id) ON DELETE RESTRICT
);

-- 8. Tabla_Posiciones (Resultados acumulados por subcategoría/deporte)
CREATE TABLE tabla_posiciones (
    subcategoria_id INT NOT NULL,
    equipo_id INT NOT NULL,
    partidos_jugados INT DEFAULT 0,
    victorias INT DEFAULT 0,
    derrotas INT DEFAULT 0,
    empates INT DEFAULT 0,
    puntos INT DEFAULT 0,
    PRIMARY KEY (subcategoria_id, equipo_id),
    FOREIGN KEY (subcategoria_id) REFERENCES subcategorias (subcategoria_id) ON DELETE CASCADE,
    FOREIGN KEY (equipo_id) REFERENCES equipos (equipo_id) ON DELETE CASCADE
);

-- 9. Sanciones (Tarjetas, penalizaciones)
CREATE TABLE sanciones (
    sancion_id SERIAL PRIMARY KEY,
    jugador_id INT NOT NULL,
    encuentro_id INT, -- Opcional, si la sanción es general (ej: multa), puede ser NULL
    tipo_sancion VARCHAR(50) NOT NULL, -- Ej: Tarjeta Amarilla, Roja, Multa
    motivo TEXT,
    detalle_sancion TEXT, -- El detalle de la sancion , suspendido por 3 partidos
    fecha_registro DATE DEFAULT CURRENT_DATE,
    FOREIGN KEY (jugador_id) REFERENCES jugadores (jugador_id) ON DELETE RESTRICT,
    FOREIGN KEY (encuentro_id) REFERENCES encuentros (encuentro_id) ON DELETE SET NULL
);