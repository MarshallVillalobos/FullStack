# PerfumeSuus - API REST con Spring Boot + JPA + MySQL

API REST para la gestión de un catálogo de perfumes y usuarios, con integración a la API externa **Fragella** para consultar información adicional de fragancias. El proyecto sigue una arquitectura por capas:

- `controller` — capa web / endpoints REST
- `service` — lógica de negocio
- `repository` — acceso a datos con **JPA / Hibernate**
- `model` — entidades JPA / estructura de datos
- `dto` — objetos de transferencia de datos (evitan exponer entidades directamente)
- `mapper` — conversión entre entidades y DTOs
- `exception` — manejo centralizado de errores con `@RestControllerAdvice`

---

## 1) Requisitos

- Java 21
- Maven (opcional si usas `mvnw`)
- **MySQL** corriendo en `localhost:3306`
- IDE recomendado: VS Code / IntelliJ / Eclipse
- Postman (opcional, se incluye colección de pruebas)

> El esquema de base de datos es gestionado automáticamente por **Flyway** al iniciar la aplicación mediante el script `V1__init.sql`. No es necesario crearlo manualmente.

---

## 2) Configuración de base de datos

El archivo `src/main/resources/application.properties` debe contener la conexión a MySQL y las credenciales de la API externa:

```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/perfumessus?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Flyway
spring.flyway.enabled=true

# API Externa Fragella
fragella.base-url=https://api.fragella.com
fragella.api-key=TU_API_KEY_AQUI
```

- `createDatabaseIfNotExist=true` → crea la base de datos si no existe.
- `ddl-auto=validate` → Hibernate valida el esquema contra las entidades (Flyway se encarga de crearlo).
- Flyway ejecuta `V1__init.sql` automáticamente al primer arranque, creando las tablas `categorias`, `usuarios`, `perfumes`, `pedidos` y `detalles_pedidos`.

---

## 3) ¿Cómo ejecutar el proyecto?

### Opción A: usando Maven Wrapper (recomendado)

Desde la carpeta raíz del proyecto (`perfumessus`):

#### En Windows (PowerShell / CMD)

```bash
.\mvnw.cmd spring-boot:run
```

#### En Linux / macOS

```bash
./mvnw spring-boot:run
```

### Opción B: compilar y ejecutar el JAR

```bash
./mvnw clean package
java -jar target/perfumessus-0.0.1-SNAPSHOT.jar
```

> En Windows, reemplaza `./mvnw` por `.\mvnw.cmd`.

---

## 4) URL base de la API

Por defecto Spring Boot levanta en el puerto `8080`:

```
http://localhost:8080
```

Los recursos disponibles son:

```
/api/v1/perfumes
/api/v1/usuarios
```

---

## 5) Endpoints — Perfumes

### 5.1 Listar todos los perfumes
- **Método:** `GET`
- **URL:** `/api/v1/perfumes`
- **Respuesta exitosa:** `200 OK` con lista de perfumes, o `204 No Content` si el catálogo está vacío.

### 5.2 Obtener perfume por ID
- **Método:** `GET`
- **URL:** `/api/v1/perfumes/{id}`
- **Respuesta exitosa:** `200 OK` con el perfume encontrado, o `404 Not Found` si no existe.

### 5.3 Crear perfume
- **Método:** `POST`
- **URL:** `/api/v1/perfumes`
- **Body JSON ejemplo:**

```json
{
    "nombre": "Sauvage",
    "marca": "Dior",
    "tipo": "Eau de Parfum",
    "ml": 60,
    "precio": 115990.0,
    "stock": 25
}
```

- **Respuesta exitosa:** `201 Created` con el perfume creado.

### 5.4 Actualizar perfume
- **Método:** `PUT`
- **URL:** `/api/v1/perfumes/{id}`
- **Body JSON ejemplo:**

```json
{
    "nombre": "Sauvage Elixir",
    "marca": "Dior",
    "tipo": "Extrait de Parfum",
    "ml": 60,
    "precio": 155000.0,
    "stock": 10
}
```

- **Respuesta exitosa:** `200 OK` con el perfume actualizado, o `404 Not Found` si el ID no existe.

### 5.5 Eliminar perfume
- **Método:** `DELETE`
- **URL:** `/api/v1/perfumes/{id}`
- **Respuesta exitosa:** `204 No Content`, o `404 Not Found` si el ID no existe.

### 5.6 Consultar API externa Fragella
- **Método:** `GET`
- **URL:** `/api/v1/perfumes/externo?nombre={nombre}`
- **Descripción:** Consulta la API externa Fragella y retorna información detallada del perfume buscado (marca, rating, precio, notas olfativas, acordes principales).
- **Ejemplo:** `/api/v1/perfumes/externo?nombre=Sauvage`
- **Respuesta exitosa:** `200 OK` con lista de resultados, o `204 No Content` si no hay coincidencias.

---

## 6) Endpoints — Usuarios

### 6.1 Listar todos los usuarios
- **Método:** `GET`
- **URL:** `/api/v1/usuarios`
- **Respuesta exitosa:** `200 OK` con lista de usuarios (sin exponer la clave), o `204 No Content` si no hay registros.

### 6.2 Obtener usuario por ID
- **Método:** `GET`
- **URL:** `/api/v1/usuarios/{id}`
- **Respuesta exitosa:** `200 OK` con el usuario encontrado, o `404 Not Found` si no existe.

### 6.3 Crear usuario
- **Método:** `POST`
- **URL:** `/api/v1/usuarios`
- **Body JSON ejemplo:**

```json
{
    "nombre": "Marshall Villalobos",
    "email": "marshall.v@example.com",
    "clave": "admin1234",
    "rol": "ADMIN"
}
```

- **Respuesta exitosa:** `201 Created` con el usuario creado. Si no se especifica `rol`, se asigna `"USER"` por defecto.

### 6.4 Actualizar usuario
- **Método:** `PUT`
- **URL:** `/api/v1/usuarios/{id}`
- **Body JSON ejemplo:**

```json
{
    "nombre": "Marshall Villalobos (Actualizado)",
    "email": "marshall.v@example.com",
    "rol": "SUPER_USER",
    "clave": "nuevaClave99"
}
```

- **Respuesta exitosa:** `200 OK` con el usuario actualizado, o `404 Not Found` si el ID no existe.

### 6.5 Eliminar usuario
- **Método:** `DELETE`
- **URL:** `/api/v1/usuarios/{id}`
- **Respuesta exitosa:** `204 No Content`, o `404 Not Found` si el ID no existe.

---

## 7) Manejo de errores

El proyecto incluye un `GlobalExceptionHandler` que intercepta y retorna respuestas claras ante los errores más comunes:

| Situación | Código HTTP |
|---|---|
| Campos inválidos en el body (validaciones) | `400 Bad Request` |
| Email duplicado u otro conflicto de base de datos | `409 Conflict` |
| API externa no encontró el perfume | `404 Not Found` |
| API Key de Fragella inválida o expirada | `401 Unauthorized` |
| Error de comunicación con Fragella | `502 Bad Gateway` |
| Error inesperado del servidor | `500 Internal Server Error` |

---

## 8) Estructura del proyecto

```
src/main/java/com/duoc/perfumessus/
├── config/         → WebClientConfig (configuración de WebClient para Fragella)
├── controller/     → PerfumeController, UsuarioController
├── dto/            → PerfumeDTO, UsuarioDTO, FragellaDTO
├── exception/      → GlobalExceptionHandler
├── mapper/         → PerfumeMapper, UsuarioMapper
├── model/          → Perfume, Usuario, Categoria, Pedido, DetallePedido
├── repository/     → PerfumeRepository, UsuarioRepository
└── service/        → PerfumeService, UsuarioService, FragellaService

src/main/resources/
├── application.properties
└── db/migration/
    └── V1__init.sql
```

---

## 9) Colección Postman

Se incluye el archivo `Testing_de_Endpoints.postman_collection.json` con todos los casos de prueba organizados:

- **Perfumes / Funcionales** — CRUD completo + consulta a Fragella
- **Perfumes / Validación de errores** — campos inválidos, IDs inexistentes, inyección de letras en ID
- **Usuarios / Funcionales** — CRUD completo
- **Usuarios / Validación de errores** — campos vacíos, email inválido, email duplicado, ID fantasma

Para importarla: abrir Postman → `Import` → seleccionar el archivo `.json`.

---

## 10) Autores

- **Marshall Villalobos**
