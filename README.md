# 🏪 Franchise API

API RESTful reactiva para la gestión de franquicias, sucursales y productos, desarrollada como prueba técnica para Accenture.

---

## 🛠️ Tecnologías utilizadas

- **Java 21**
- **Spring Boot 4.x**
- **Spring WebFlux** — Programación reactiva
- **Spring Data Reactive MongoDB** — Persistencia reactiva
- **MongoDB Atlas** — Base de datos en la nube
- **Docker & Docker Compose** — Contenerización
- **Gradle** — Gestor de dependencias
- **Lombok** — Reducción de código boilerplate
- **JUnit 5 + Mockito + StepVerifier** — Pruebas unitarias

---

## 📐 Arquitectura

El proyecto está basado en **Clean Architecture**, separando las responsabilidades en capas independientes:
```
src/main/java/com/accenture/test/
│
├── domain/                  # Entidades y contratos del negocio
│   ├── model/               # Modelos puros (Franchise, Branch, Product)
│   └── repository/          # Interfaces del repositorio
│
├── application/             # Casos de uso y DTOs
│   ├── usecase/             # Lógica de negocio por caso de uso
│   └── dto/                 # Request y Response objects
│
├── infrastructure/          # Implementaciones externas
│   └── persistence/
│       ├── entity/          # Documentos MongoDB
│       ├── mapper/          # Conversión entity ↔ model
│       ├── adapter/         # Implementación del repositorio
│       └── repository/      # Repositorio reactivo de Spring
│
└── api/                     # Capa de entrada HTTP
    ├── controller/          # Endpoints REST
    └── exception/           # Manejo global de errores
```

---

## ✅ Endpoints disponibles

### Franquicias
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/franchises` | Crear nueva franquicia |
| `PATCH` | `/api/franchises/{franchiseId}/name` | Actualizar nombre de franquicia |
| `GET` | `/api/franchises/{franchiseId}/top-stock` | Producto con más stock por sucursal |

### Sucursales
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/franchises/{franchiseId}/branches` | Agregar sucursal a franquicia |
| `PATCH` | `/api/franchises/{franchiseId}/branches/{branchId}/name` | Actualizar nombre de sucursal |

### Productos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/franchises/{franchiseId}/branches/{branchId}/products` | Agregar producto a sucursal |
| `DELETE` | `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}` | Eliminar producto |
| `PATCH` | `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock` | Modificar stock |
| `PATCH` | `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/name` | Actualizar nombre de producto |

---

## 📦 Formato de respuesta

Todas las respuestas siguen el mismo formato estandarizado:
```json
{
  "status": 200,
  "errorCode": null,
  "message": "OK",
  "data": { }
}
```

### Códigos de error

| Código | Descripción |
|--------|-------------|
| `ERR-001` | Franquicia no encontrada |
| `ERR-002` | Nombre de franquicia duplicado |
| `ERR-101` | Sucursal no encontrada |
| `ERR-102` | Nombre de sucursal duplicado |
| `ERR-201` | Producto no encontrado |
| `ERR-203` | Stock negativo no permitido |
| `ERR-301` | Error de conexión con la base de datos |
| `ERR-401` | Error de validación |
| `ERR-501` | Error interno del servidor |

---

## 🚀 Despliegue local

### Prerrequisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado y corriendo
- [Git](https://git-scm.com/) instalado

### Pasos

**1. Clona el repositorio**
```bash
git clone https://github.com/tuUsuario/franchise-api.git
cd franchise-api
```

**2. Crea el archivo `.env`** en la raíz del proyecto con tu URI de MongoDB Atlas:
```env
SPRING_DATA_MONGODB_URI=mongodb+srv://<usuario>:<password>@<cluster>.mongodb.net/franchisedb?retryWrites=true&w=majority&appName=<appName>
```

**3. Construye y levanta la aplicación**
```bash
docker-compose up --build
```

**4. Verifica que la aplicación esté corriendo**
```bash
curl http://localhost:8081/actuator/health
```

Deberías ver:
```json
{
  "status": "UP"
}
```

**5. ¡Listo!** La API está disponible en `http://localhost:8081`

---

## 🧪 Ejecutar pruebas unitarias
```bash
./gradlew test
```

Para ver el reporte de cobertura:
```bash
./gradlew test jacocoTestReport
```

El reporte se genera en:
```
build/reports/tests/test/index.html
```

---

## 📬 Ejemplos de uso

### Crear una franquicia
```bash
curl -X POST http://localhost:8081/api/franchises \
  -H "Content-Type: application/json" \
  -d '{"name": "Mi Franquicia"}'
```

### Agregar una sucursal
```bash
curl -X POST http://localhost:8081/api/franchises/{franchiseId}/branches \
  -H "Content-Type: application/json" \
  -d '{"name": "Sucursal Norte"}'
```

### Agregar un producto
```bash
curl -X POST http://localhost:8081/api/franchises/{franchiseId}/branches/{branchId}/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Producto A", "stock": 100}'
```

### Consultar producto con más stock por sucursal
```bash
curl http://localhost:8081/api/franchises/{franchiseId}/top-stock
```

---

## 🐳 Comandos Docker útiles
```bash
# Levantar en segundo plano
docker-compose up --build -d

# Ver logs en tiempo real
docker-compose logs -f franchise-api

# Detener la aplicación
docker-compose down

# Reconstruir desde cero
docker-compose down && docker-compose up --build
```

---

## 🗂️ Variables de entorno

| Variable | Descripción | Requerida |
|----------|-------------|-----------|
| `SPRING_DATA_MONGODB_URI` | URI de conexión a MongoDB Atlas | ✅ Sí |

---

## 👨‍💻 Autor

Desarrollado por **Never Jose Florez Mendoza**  
Prueba técnica — Accenture Backend Developer