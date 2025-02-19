# Proyecto de Gestión de Tareas con Autenticación JWT

## Descripción del Proyecto

Este proyecto es una API RESTful desarrollada en Java utilizando Spring Boot. Proporciona funcionalidades para gestionar tareas (`tasks`) y usuarios (`users`), incluyendo autenticación y autorización mediante tokens JWT (JSON Web Token). Además, implementa un sistema robusto de manejo de excepciones global para garantizar respuestas consistentes en caso de errores.

### Características Principales
- **Autenticación y Autorización**: Los usuarios pueden iniciar sesión, registrarse y cerrar sesión. Se utiliza JWT para la autenticación.
- **Gestión de Tareas**: Los usuarios pueden crear, actualizar, eliminar y consultar tareas. También pueden cambiar el estado de las tareas.
- **Catálogos**: El sistema incluye catálogos predefinidos para estados (`status`) y roles (`roles`).
- **Manejo de Excepciones Global**: Se capturan y manejan diferentes tipos de excepciones (por ejemplo, validaciones fallidas, recursos no encontrados, errores de autenticación) para proporcionar respuestas claras y estructuradas.
- **Validación de Datos**: Se utilizan anotaciones de validación (`@Valid`) para asegurar que los datos de entrada cumplan con los requisitos esperados.
- **Documentación con Swagger**: La API está documentada utilizando Swagger/OpenAPI para facilitar la exploración y pruebas.

---

## Requisitos Mínimos

Para ejecutar este proyecto, necesitarás lo siguiente:

1. **Java Development Kit (JDK)**: Versión 17 o superior.
2. **Maven**: Para compilar y gestionar las dependencias del proyecto.
3. **MySQL**: Base de datos MySQL configurada y accesible.
4. **IDE Opcional**: Puedes usar IntelliJ IDEA, Eclipse, o cualquier otro IDE compatible con proyectos Maven.

---

## Instalación y Ejecución

### Paso 1: Clonar el Repositorio

Clona este repositorio en tu máquina local usando el siguiente comando:

```bash
git clone <URL_DEL_REPOSITORIO>
cd <NOMBRE_DEL_PROYECTO>
```

### Paso 2: Configurar Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto con las siguientes variables de entorno o renombra .env.template a .env:

```env
DB_URL=jdbc:mysql://localhost:3306/db_springboot_task_app
DB_USER=root
DB_PASS=
JWT_SECRET=kjsdjhsdhjdfhjghsdhgjsdghsdghhgsdvhvgdshgjdshjdshjjhdshjdshjdshjdsw
API_PATH=/api/v1
JWT_EXPIRATION=3600000
JWT_REFRESH_TOKEN_EXPIRATION=10080
MAX_REQUESTS_PER_MINUTE=100
```

Abre el archivo .env y reemplaza los valores de las variables con tus propias configuraciones. Por ejemplo:

DB_URL: La URL de conexión a tu base de datos MySQL.

DB_USER y DB_PASS: Tus credenciales de MySQL.

JWT_SECRET: Una clave secreta segura para firmar los tokens JWT.

JWT_EXPIRATION y JWT_REFRESH_TOKEN_EXPIRATION: Los tiempos de expiración para los tokens JWT y refresh tokens.

API_PATH: El contexto de la API (por defecto /api/v1).

MAX_REQUESTS_PER_MINUTE: El límite de solicitudes por minuto.

> **Nota**: Puedes usar cualquier base de datos MySQL que prefieras. Si no deseas usar db_springboot_task_app, asegúrate de actualizar el valor de DB_URL con el nombre de tu base de datos. 
<!-- > **Nota**: Asegúrate de reemplazar `DB_USER` y `DB_PASS` con tus credenciales de MySQL. -->

### Paso 3: Configurar `application.properties`

El archivo `application.properties` ya está configurado para leer las variables de entorno desde el archivo `.env`. Sin embargo, si deseas personalizarlo, puedes modificarlo directamente en `src/main/resources/application.properties`.

Ejemplo de configuración:

```properties
spring.application.name=task-app
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
server.servlet.context-path=${API_PATH}
spring.hateoas.use-hal-as-default-json-media-type=true
server.error.include-stacktrace=never
api.security.token.secret=${JWT_SECRET}
api.security.token.expiration=${JWT_EXPIRATION}
api.security.token.refresh-token-expiration=${JWT_REFRESH_TOKEN_EXPIRATION}
api.security.max-requests-per-minute=${MAX_REQUESTS_PER_MINUTE}
logging.file.path=logs
logging.file.name=logs/spring.log
# Habilitar Swagger UI
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.use-root-path=true
springdoc.swagger-ui.disable-swagger-default-url=true
```

### Paso 4: Crear la Base de Datos

Asegúrate de que la base de datos `db_springboot_task_app` exista en tu servidor MySQL. Puedes crearla ejecutando el siguiente comando en MySQL:

```sql
CREATE DATABASE db_springboot_task_app;
```

### Paso 5: Insertar Datos Iniciales

Después de crear la base de datos, inserta los datos iniciales en las tablas `roles` y `status`. Ejecuta los siguientes comandos SQL:

```sql
INSERT INTO `roles` (id, name) VALUES (1, "ADMIN");
INSERT INTO `roles` (id, name) VALUES (2, "USER");

INSERT INTO `status` (id, name) VALUES (1, "PENDING");
INSERT INTO `status` (id, name) VALUES (2, "IN_PROGRESS");
INSERT INTO `status` (id, name) VALUES (3, "COMPLETED");
```

> **Nota**: Estos datos son necesarios para que el sistema funcione correctamente. Asegúrate de ejecutar estos comandos antes de iniciar la aplicación.

### Paso 6: Compilar el Proyecto

Ejecuta el siguiente comando para compilar el proyecto y descargar las dependencias:

```bash
mvn clean install
```

### Paso 7: Ejecutar la Aplicación

Una vez compilado, inicia la aplicación con el siguiente comando:

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080${API_PATH}` (por ejemplo, `http://localhost:8080/api/v1`).

---

## Documentación con Swagger

La API está documentada utilizando Swagger/OpenAPI. Una vez que la aplicación esté en ejecución, puedes acceder a la interfaz de Swagger UI en la siguiente URL:

```
http://localhost:8080/api/v1/swagger-ui/index.html
```

---

## Manejo de Excepciones

El proyecto incluye un manejador global de excepciones (`GlobalExceptionHandler`) que captura y maneja errores comunes, como:

- **Errores de Validación**: Cuando los datos de entrada no cumplen con las reglas establecidas.
- **Recursos No Encontrados**: Cuando se intenta acceder a un recurso inexistente.
- **Errores de Autenticación/Autorización**: Cuando el token JWT no es válido o ha expirado.
- **Errores Internos del Servidor**: Para cualquier error inesperado.

Las respuestas de error siguen un formato estándar:

```json
{
    "status": "FAIL",
    "message": "Descripción del error",
    "results": {
        // Detalles adicionales del error, si aplica
    }
}
```

---

## Pruebas

Puedes probar los endpoints utilizando herramientas como:
- **Postman**: Importa una colección de Postman con los endpoints definidos.
- **Swagger UI**: Accede a `http://localhost:8080/api/v1/swagger-ui/index.html` para explorar y probar los endpoints.

---

## Licencia

Este proyecto está bajo la licencia [MIT License](https://opensource.org/license/MIT).
