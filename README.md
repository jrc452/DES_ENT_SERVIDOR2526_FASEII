# 🛒 Panel de Gestión para Categorías y Productos

Proyecto desarrollado en **Spring Boot**, **Thymeleaf**, **JPA/Hibernate** y **Bootstrap**, que implementa un completo panel de administración para gestionar dos entidades relacionadas entre sí mediante una relación **1:N**:

* **Categoría** (Entidad principal)
* **Producto** (Entidad secundaria)

Incluye CRUD completo, vistas responsive, consultas personalizadas, autenticación, estadísticas y exportación de datos.

---

# 📌 1. Descripción del proyecto

Este proyecto es una aplicación web que permite gestionar categorías y productos dentro de un panel de administración. Está pensado como parte de un sistema de tienda o catálogo digital.

El sistema permite:

* Gestionar categorías (crear, editar, eliminar, ver detalles)
* Gestionar productos asociados a una categoría
* Visualizar detalles completos de una categoría junto con todos sus productos
* Realizar búsquedas avanzadas
* Exportar listados a CSV o PDF
* Consultar estadísticas (conteos, agregaciones)
* Confirmar eliminaciones mediante modal de Bootstrap
* Autenticarse mediante Google OAuth (Spring Security)

La aplicación está construida siguiendo arquitectura MVC y los principios de Spring Boot.

---

# 🧩 2. Diagrama ER actualizado

**Entidades principales:**

* Usuario
* Perfil
* Dirección
* Categoría
* Producto
* Pedido
* DetallePedido
* Pago
* Reseña

*(A continuación coloca la captura o imagen del ER que generé para ti)*

```
[ Aquí insertar imagen del diagrama ER en PNG/JPG ]
```

---

# ⚙️ 3. Instrucciones de instalación y ejecución

## ✔️ Requisitos previos

* Java **17** o superior
* Maven **3.8+**
* MySQL o H2 Database
* Git (opcional)

---

## 📥 3.1 Clonar el repositorio

```bash
git clone https://github.com/jrc452/DES_ENT_SERVIDOR2526_FASEII.git
cd DES_ENT_SERVIDOR2526_FASEII
```

---

## 🗃️ 3.2 Configurar Base de Datos

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tienda?useSSL=false
spring.datasource.username=root
spring.datasource.password=tu_clave

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## ▶️ 3.3 Ejecutar el proyecto

### Con Maven:

```bash
mvn spring-boot:run
```

### O compilado:

```bash
mvn clean install
java -jar target/tienda-0.0.1-SNAPSHOT.jar
```

---

## 🌐 3.4 Acceso a la aplicación

Una vez iniciado:

```
http://localhost:8080
```

Menú superior incluye:

* **Categorías**
* **Productos**
* **Búsqueda Avanzada**
* **Estadísticas**
* **Login con Google**

---

# 🧠 4. Funcionalidades implementadas

## ✔️ CRUD Completo

* Crear, listar, editar, eliminar categorías
* Crear, listar, editar, eliminar productos
* Asociación producto ↔ categoría

---

## ✔️ Vista especial de detalle

La vista `/categorias/detalle/{id}` incluye:

* Datos completos de la categoría
* Listado de productos asociados
* Botón “Añadir producto”
* Acciones rápidas: editar/eliminar producto

---

## ✔️ Consultas personalizadas (Repositories)

### Categoría Repository:

* `findByNombreContainingAndDescripcionContaining`
* `findByDescripcionContainingIgnoreCase`
* `findAllByOrderByNombreAsc`
* `countByNombreContaining`
* `findTopByOrderByIdCategoriaDesc`
* `@Query` con parámetro: categorías con más de X productos

### Producto Repository:

* `deleteByStockLessThan`
* `deleteByCategoriaIdCategoria`
* `findByPrecioBetween`
* `@Query`: productos disponibles por categoría

---

## ✔️ Búsqueda avanzada

Formulario que permite filtrar registros usando métodos personalizados del repositorio.

---

## ✔️ Estadísticas

Página `/estadisticas` mostrando:

* Total de categorías
* Total de productos
* Promedios y agregaciones
* Ranking de categorías con más productos
  *(Puede ampliarse según necesidad)*

---

## ✔️ Exportación de datos

Botones en el listado:

* **Exportar CSV**
* **Exportar PDF**

---

## ✔️ Confirmación de eliminación

Uso de **modal Bootstrap** antes de borrar registros.

---

## ✔️ Logs

Se registran eventos:

* Creación
* Modificación
* Eliminación
* Login (si OAuth activado)

---

## ✔️ Autenticación con Google OAuth

Login mediante Google:

* Spring Security
* Configuración OAuth2
* Redirección automática al panel

---

# 📸 5. Capturas de pantalla de la aplicación

```
/capturas/lista_categorias.png  
/capturas/detalle_categoria.png  
/capturas/form_producto.png  
/capturas/estadisticas.png  
/capturas/login_google.png  
```