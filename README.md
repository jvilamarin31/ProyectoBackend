# ProyectoBackend
## Base de datos (MongoDB) - Ejecutar en local

Si deseas ejecutar el proyecto en local, puedes crear la base de datos y la colección en MongoDB con el siguiente script.

> Nota: La colección se llama **`Usuario`** (con mayúscula) porque el modelo en Spring usa `@Document("Usuario")`.

### 1) Crear base de datos y colección `Usuario`

Ejecuta este script en MongoDB (mongosh o MongoDB Compass):

```js
use ProyectoBD;

db.createCollection("Usuario", {
    capped: false,
    validator: {
        $jsonSchema: {
            bsonType: "object",
            title: "Usuario",
            properties: {
                _id: { bsonType: "objectId" },
                first_name: { bsonType: "string", minLength: 1 },
                last_name: { bsonType: "string", minLength: 1 },
                date_birth: { bsonType: "date" },
                address: { bsonType: "string", minLength: 1 },
                token: { bsonType: ["string", "null"] },
                password: { bsonType: "string", minLength: 1, maxLength: 120 },
                mobile_phone: { bsonType: "string", minLength: 1, maxLength: 15 },
                email: { bsonType: "string", minLength: 1 }
            },
            additionalProperties: false,
            required: [
                "first_name",
                "last_name",
                "date_birth",
                "address",
                "password",
                "mobile_phone",
                "email"
            ]
        }
    },
    validationLevel: "strict",
    validationAction: "error"
});
```
### Insertar usuario administrador inicial
Inserta el siguiente usuario en la colección `Usuario`:
```js
db.Usuario.insertOne({
first_name: "Jhon",
last_name: "Eder",
date_birth: ISODate("1981-02-22T00:00:00.000Z"),
mobile_phone: "3005004521",
email: "admin@gmail.com",
password: "$2a$10$EjufBAafJ5abbI4Tnh49pejOmcoZgBV6UGOM5bU.QysgXP/RxoIAm",
address: "cl 86 #61-10"
});
```
### 3) Credenciales para Login (usuario inicial)
Puedes autenticarte con:
- **mobile_phone**: 3005004521
- **password**: admin123

**¿Por qué la contraseña en la BD está hasheada?**
La contraseña se almacena de forma segura utilizando un algoritmo de hashing (bcrypt) para protegerla contra accesos no autorizados. Esto significa que incluso si alguien accede a la base de datos, no podrá ver la contraseña original, solo su versión hasheada.

### 4) Formato de JSON (snake_case)
Aunque internamente en Java se usen atributos en camelCase, la API recibe y retorna JSON en snake_case.

---
## Ejecutar el backend en local (sin Docker)

### Requisitos
- Java 21
- Maven 3+
- (Opcional) MongoDB local si no vas a usar la BD en la nube

### Configuración por variables de entorno
El backend por defecto se conecta a una base de datos en la nube (MongoDB Atlas) porque `application.properties` tiene un valor por defecto.

Si quieres usar una base de datos local, debes **sobrescribir** estas variables de entorno:

- `SPRING_MONGODB_URI` (URI de MongoDB)
- `JWT_SECRET` (clave para firmar tokens JWT)

> Por defecto, si no defines `SPRING_MONGODB_URI`, el proyecto usará el valor de Atlas definido en `application.properties`.

#### Ejemplo usando BD local (Linux/Mac)
```bash
export SPRING_MONGODB_URI="mongodb://localhost:27017/"
export JWT_SECRET="tu_clave_super_secreta"
```
#### Ejemplo usando BD local (Windows PowerShell)
```powershell
$env:SPRING_MONGODB_URI="mongodb://localhost:27017/"
$env:JWT_SECRET="tu_clave_super_secreta"
```
### Comandos para correr el proyecto
1. Clona el repositorio y navega a la carpeta del proyecto.
2. Ejecuta el siguiente comando para compilar y correr el backend:
```bash
mvn spring-boot:run
```
3. Empaquetar y ejecutar el .jar:
```bash
mvn clean package -DskipTests
java -jar target/*.jar
```
El backend quedará disponible en:
- http://localhost:8080/ para la API

---
# Ejecutar con Docker / Docker Compose

### Opción A) Usar la base de datos en la nube (por defecto)
Tu `docker-compose.yml` ya trae valores por defecto para las variables, así que puedes levantarlo así:

```bash
docker compose up --build
```
Esto construye la imagen con el Dockerfile y levanta el contenedor del backend en:
- http://localhost:8080/
### Opción B) Usar una base de datos local (MongoDB local fuera de Docker)
Si tienes MongoDB instalado en tu máquina, puedes sobrescribir la variable de entorno así:
#### Linux/Mac
```bash
SPRING_MONGODB_URI="mongodb://localhost:27017/" docker compose up --build
```
#### Windows PowerShell
```powershell
$env:SPRING_MONGODB_URI="mongodb://localhost:27017/"
docker compose up --build
```
---
## Ejecutar tests y verificar coverage (umbral 70%)

Este proyecto usa **JaCoCo** para medir cobertura y tiene un **umbral mínimo del 70%** configurado.
Si el coverage es menor a 70%, el build fallará en la fase `verify`.

### Ejecutar tests
```bash
mvn test
```
### Ejecutar tests + generar reporte + validar umbral
```bash
mvn clean verify
```
### Ver el reporte de cobertura
Después de mvn `verify`, abre el reporte HTML:
- `target/site/jacoco/index.html`

Ahí podrás ver el porcentaje por paquete/clase/método.

---
## CI/CD (GitHub Actions) - Resumen

Este repositorio incluye un pipeline que se ejecuta en la rama `main` cuando haces `push`.

### CI (Continuous Integration)
- Compila el proyecto.
- Ejecuta los tests.
- Valida el coverage mínimo del **70%**.
- (Opcional) Construye el Docker para asegurar que la imagen también se puede generar correctamente.

Si algo falla, el pipeline marca el build como fallido.

### CD (Continuous Deployment)
Si el CI pasa, el pipeline dispara un despliegue en Render (deploy manual controlado desde GitHub Actions).
Esto evita depender del “auto-deploy” de Render por cada push.
---

## Probar la API con Postman

Este proyecto incluye una colección de Postman para probar los endpoints tanto en **local** como en **deploy**.

### Importar la colección

Tienes dos opciones para importar la colección de Postman:

1. **Importar desde archivo JSON**: 
   - Ve a la carpeta `postman/` en este repositorio
   - Descarga el archivo JSON de la colección
   - En Postman, haz clic en **Import** y selecciona el archivo descargado

2. **Unirte al workspace compartido**:
   - Usa este enlace de invitación: [Unirse al workspace de Postman](https://app.getpostman.com/join-team?invite_code=56439a4765204efb0a1eeb456b250b95347fc170d370ad35c38521eae8ebf281&target_code=35a127d0b524396584731d6ecc02f254)
   - Acepta la invitación y tendrás acceso directo a la colección actualizada


### Estructura de la colección

La colección está organizada en dos carpetas:

- **LocalHost**: contiene las mismas peticiones, pero apuntando a la URL local (localhost).
- **Deploy**: contiene las mismas peticiones, pero apuntando a la URL del backend desplegado (Render).

Ambas carpetas tienen **exactamente el mismo funcionamiento**; la única diferencia es la URL.

### Endpoints públicos (sin token)

Fuera de la carpeta `Security` encontrarás los endpoints que **NO requieren token**:

- **GetUsers**: lista todos los usuarios.
- **Login**: permite autenticarse y obtener el token.

#### Login (genera token)
En la petición `Login` envía un JSON con:

- `mobile_phone`
- `password`

Ejemplo:
```json
{
  "mobile_phone": "3001234567",
  "password": "MiPassword123"
}
```
Al ejecutar Login, la respuesta retorna un token. Cópialo, porque lo vas a usar en las peticiones protegidas.

### Endpoints protegidos (requieren token)

Dentro de la carpeta `Security` encontrarás los endpoints que **SÍ requieren token**:
- **GetUser**
- **CreateUser**
- **CreateUser**
- **DeleteUser**

#### Cómo usar el token en Postman
1. Selecciona la carpeta `Security` o la petición específica que quieres probar.
2. Ve a la pestaña **Authorization**.
3. En el **Auth Type**, selecciona **Bearer Token**.
4. En el campo de token, pega el token que obtuviste del endpoint `Login`.

**Importante:** si usas un token expirado, la API puede responder 403

### Cuerpos (JSON) por operación

#### CreateUser
Para crear un usuario, envía un JSON con los siguientes campos:
- `first_name`
- `last_name`
- `date_birth`
- `mobile_phone`
- `email`
- `password`
- `address`

Ejemplo:
```json
{
  "first_name": "Juan",
  "last_name": "Vila",
  "date_birth": "2003-01-15",
  "mobile_phone": "3001234567",
  "email": "juan@email.com",
  "password": "MiPassword123",
  "address": "Cali, Colombia"
}
```
#### UpdateUser
En UpdateUser puedes modificar cualquiera de estos campos:
- `first_name`
- `last_name`
- `date_birth`
- `mobile_phone`
- `email`
- `password`
- `address`

Además debes indicar el id del usuario a modificar.

```json
{
  "first_name": "Juan Esteban",
  "address": "Nueva dirección"
}
```
#### GetUser
Solo debes indicar el id del usuario que quieres consultar.

#### GetUsers
No requiere cuerpo, solo ejecuta la petición para obtener la lista de usuarios.

#### DeleteUser
Solo debes indicar el id del usuario que quieres eliminar. No requiere cuerpo, solo ejecuta la petición.