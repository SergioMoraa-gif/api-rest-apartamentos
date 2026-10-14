# API REST - Alquiler de pisos y habitaciones

API REST para administrar el alquiler de propiedades (casas, departamentos) y de sus unidades rentables (habitaciones o pisos completos). Permite registrar clientes y propietarios, publicar propiedades, manejar la disponibilidad, hacer reservas, registrar pagos y dejar reseñas.

Además de la API, el proyecto trae una **vista web sencilla** (Thymeleaf) para administrar propiedades, unidades, clientes, reservas y pagos desde el navegador.

Proyecto de la materia **Arquitectura Orientada a Servicios**.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4 (Web MVC, Data JPA, Validation, Actuator)
- Hibernate / JPA
- MySQL 8
- Thymeleaf + Bootstrap 5 (vista web)
- Maven (incluye Maven Wrapper `mvnw`)
- Postman (pruebas)

## Estructura del proyecto

```
src/main/java/com/api/apartamentos
├── controllers    -> endpoints REST, vista web y manejo de errores
├── models         -> entidades JPA (tablas de la BD)
├── repositories   -> interfaces JpaRepository
└── services       -> lógica de negocio y validaciones
src/main/resources
├── templates      -> vistas Thymeleaf
├── static/css     -> estilos de la vista
└── application.properties
```

## Variables de entorno necesarias

Las credenciales no están en el código. Se leen de un archivo `.env` en la raíz del proyecto (este archivo **no** se sube al repositorio). Se usa `.env.example` como plantilla:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `SERVER_PORT` | Puerto de la API | `8000` |
| `DB_URL` | URL de conexión a MySQL | `jdbc:mysql://localhost:3306/apartamentos?createDatabaseIfNotExist=true` |
| `DB_USERNAME` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contraseña de MySQL | `tu_password` |

## Instalación y ejecución

1. Tener instalado **Java 21** y **MySQL** (encendido).
2. Clonar el repositorio:
   ```bash
   git clone https://github.com/SergioMoraa-gif/api-rest-apartamentos.git
   cd api-rest-apartamentos
   ```
3. Copiar `.env.example` como `.env` y poner la contraseña de MySQL.
4. Ejecutar:
   ```bash
   # Windows
   .\mvnw.cmd spring-boot:run
   # Linux / Mac
   ./mvnw spring-boot:run
   ```
5. La base de datos `apartamentos` y sus tablas se crean solas.
6. La API queda en `http://localhost:8000/api/...` y la vista web en `http://localhost:8000`.

## Listado de endpoints

### Clientes `/api/clientes`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/clientes` | Listar clientes |
| GET | `/api/clientes/{id}` | Obtener un cliente |
| POST | `/api/clientes` | Crear cliente |
| PUT | `/api/clientes/{id}` | Actualizar cliente |
| DELETE | `/api/clientes/{id}` | Eliminar cliente |

### Propiedades `/api/propiedades`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/propiedades` | Listar propiedades |
| GET | `/api/propiedades/{id}` | Obtener una propiedad |
| GET | `/api/propiedades/tipo/{tipo}` | Propiedades por tipo |
| GET | `/api/propiedades/propietario/{id}` | Propiedades de un propietario |
| POST | `/api/propiedades` | Crear propiedad |
| PUT | `/api/propiedades/{id}` | Actualizar propiedad |
| DELETE | `/api/propiedades/{id}` | Eliminar propiedad |

### Unidades rentables `/api/unidades`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/unidades` | Listar unidades |
| GET | `/api/unidades/{id}` | Obtener una unidad |
| GET | `/api/unidades/propiedad/{id}` | Unidades de una propiedad |
| POST | `/api/unidades` | Crear unidad |
| PUT | `/api/unidades/{id}` | Actualizar unidad |
| DELETE | `/api/unidades/{id}` | Eliminar unidad |

### Reservas `/api/reservas`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/reservas` | Listar reservas |
| GET | `/api/reservas/{id}` | Obtener una reserva |
| POST | `/api/reservas` | Crear reserva (calcula el importe) |
| PUT | `/api/reservas/{id}` | Actualizar reserva |
| DELETE | `/api/reservas/{id}` | Eliminar reserva |

### Pagos `/api/pagos`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/pagos` | Listar pagos |
| GET | `/api/pagos/{id}` | Obtener un pago |
| GET | `/api/pagos/reserva/{id}` | Pagos de una reserva |
| POST | `/api/pagos` | Registrar pago |
| PUT | `/api/pagos/{id}` | Actualizar pago |
| POST | `/api/pagos/{id}/confirmar` | Confirmar pago |
| POST | `/api/pagos/{id}/reembolsar` | Reembolsar pago |
| DELETE | `/api/pagos/{id}` | Eliminar pago (solo si no está pagado) |

### Otros recursos
Con los mismos métodos CRUD (GET, GET por id, POST, PUT, DELETE):

| Recurso | Ruta | Consulta extra |
|---|---|---|
| Disponibilidades | `/api/disponibilidades` | `GET /api/disponibilidades/unidad/{id}` |
| Imágenes de propiedad | `/api/propiedad-imagenes` | `GET /api/propiedad-imagenes/propiedad/{id}` |
| Reseñas | `/api/resenas` | `GET /api/resenas/reserva/{id}` |
| Notificaciones | `/api/notificaciones` | `GET /api/notificaciones/destinatario/{id}` |

## Ejemplos de peticiones y respuestas

Orden recomendado para probar: cliente → propiedad → unidad → reserva → pago.

### Crear cliente
`POST /api/clientes`
```json
{
  "tipo": "PROPIETARIO",
  "nombres": "Sergio",
  "apellidos": "Morales",
  "email": "sergio@correo.com",
  "telefono": "2291234567",
  "direccion": "Veracruz, Ver.",
  "password": "123456"
}
```
Respuesta `201 Created` (la contraseña se guarda encriptada y no se muestra):
```json
{
  "id": 1,
  "tipo": "PROPIETARIO",
  "nombres": "Sergio",
  "apellidos": "Morales",
  "email": "sergio@correo.com",
  "telefono": "2291234567",
  "estatus": "ACTIVO",
  "fechaRegistro": "2026-10-14T10:00:00"
}
```

### Crear propiedad
`POST /api/propiedades`
```json
{
  "propietario": { "id": 1 },
  "tipo": "Departamento",
  "titulo": "Departamento frente al mar",
  "descripcion": "Departamento amueblado con vista al mar",
  "direccion": "Blvd. Manuel Ávila Camacho 100",
  "ciudad": "Veracruz",
  "estadoFederativo": "Veracruz",
  "codigoPostal": "91700",
  "pais": "México",
  "latitud": 19.18,
  "longitud": -96.13,
  "precioNoche": 1200.00,
  "capacidad": 4,
  "numHabitaciones": 2,
  "numBanos": 1,
  "metrosCuadrados": 80,
  "comodidades": "WiFi, aire acondicionado",
  "reglas": "No fumar"
}
```

### Crear unidad rentable
`POST /api/unidades`
```json
{
  "propiedad": { "idPropiedad": 1 },
  "nombre": "Habitación 1",
  "tipo": "Habitación",
  "capacidad": 2,
  "precioNoche": 600.00
}
```

### Crear reserva
`POST /api/reservas`
```json
{
  "unidad": { "idUnidad": 1 },
  "cliente": { "id": 1 },
  "fechaEntrada": "2026-11-01T15:00:00",
  "fechaSalida": "2026-11-04T12:00:00",
  "comentario": "Llegamos en la tarde"
}
```
El `importeTotal` se calcula solo (precio por noche × número de noches).

### Registrar pago
`POST /api/pagos`
```json
{
  "reserva": { "idReservacion": 1 },
  "importe": 1800.00,
  "fechaVencimiento": "2026-10-30",
  "metodo": "TRANSFERENCIA"
}
```
Después se confirma con `POST /api/pagos/1/confirmar`.

### Respuestas de error
Los errores 400 y 409 regresan este formato:
```json
{
  "timestamp": "2026-10-14T10:05:00",
  "status": 400,
  "error": "La unidad ya está reservada en ese rango"
}
```

| Código | Cuándo |
|---|---|
| `400 Bad Request` | Datos inválidos, JSON mal escrito o regla de negocio no cumplida |
| `404 Not Found` | El recurso con ese id no existe (respuesta sin cuerpo) |
| `409 Conflict` | Email repetido o se intenta borrar un registro que tiene datos relacionados |

## Autor

Sergio Antonio Morales Mavil - Ingeniería en Sistemas Computacionales
