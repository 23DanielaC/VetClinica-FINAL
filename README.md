# 🐾 VetNova — Sistema de Gestión Veterinaria

VetNova es una aplicación web de gestión integral para clínicas veterinarias. Permite administrar clientes, mascotas, veterinarios, turnos, consultas, medicamentos, vacunas, facturas y pagos desde una interfaz moderna y responsiva.

---

## 🚀 Tecnologías utilizadas

| Tecnología | Versión | Rol |
|---|---|---|
| [Vue 3](https://vuejs.org/) | ^3.4 | Framework principal (Composition API) |
| [Vite](https://vitejs.dev/) | ^5.1 | Bundler y servidor de desarrollo |
| [Vue Router](https://router.vuejs.org/) | ^4.3 | Navegación SPA (hash history) |
| [Pinia](https://pinia.vuejs.org/) | ^2.1 | Manejo de estado global |
| [Tailwind CSS](https://tailwindcss.com/) | ^3.4 | Estilos utilitarios |
| [Axios](https://axios-http.com/) | ^1.6 | Cliente HTTP |
| [@heroicons/vue](https://heroicons.com/) | ^2.1 | Íconos SVG |

---

## 📁 Estructura del proyecto

```
vetnova/
├── src/
│   ├── assets/          # Estilos globales (main.css)
│   ├── components/
│   │   ├── layout/      # AppLayout.vue (estructura principal)
│   │   └── ui/          # StatCard, CrudTable, CrudModal
│   ├── router/          # Definición de rutas (index.js)
│   ├── stores/          # Estado global con Pinia
│   │   ├── auth.js      # Autenticación
│   │   └── data.js      # Datos de la aplicación
│   └── views/           # Vistas por módulo
│       ├── auth/        # Login
│       ├── clientes/
│       ├── mascotas/
│       ├── veterinarios/
│       ├── turnos/
│       ├── consultas/
│       ├── medicamentos/
│       ├── vacunas/
│       ├── facturas/
│       └── pagos/
├── index.html
├── vite.config.js
├── tailwind.config.js
└── package.json
```

---

## ⚙️ Instalación y ejecución local

### Requisitos previos

- [Node.js](https://nodejs.org/) v18 o superior
- npm v9 o superior (viene incluido con Node.js)

### Pasos

```bash
# 1. Clona el repositorio o descomprime el proyecto
cd vetnova

# 2. Instala las dependencias
npm install

# 3. Inicia el servidor de desarrollo
npm run dev
```

Una vez ejecutado, abre tu navegador en:

```
http://localhost:5173
```

---

## 🔐 Credenciales de acceso (demo)

| Campo | Valor |
|---|---|
| Email | `admin@vetnova.com` |
| Contraseña | `admin123` |

> ⚠️ El login actual es una simulación. Para producción, reemplaza la función `login()` en `src/stores/auth.js` con una llamada real a tu API backend.

---

## 📦 Scripts disponibles

```bash
npm run dev       # Servidor de desarrollo (localhost:5173)
npm run build     # Genera la versión de producción en /dist
npm run preview   # Previsualiza el build de producción
```

---

## 🧭 Módulos del sistema

- **Dashboard** — Vista general con estadísticas
- **Clientes** — Registro y gestión de dueños de mascotas
- **Mascotas** — Historial y datos de cada animal
- **Veterinarios** — Gestión del personal médico
- **Turnos** — Agenda y programación de citas
- **Consultas** — Registro de atenciones médicas
- **Medicamentos** — Inventario de fármacos
- **Vacunas** — Control de esquemas de vacunación
- **Facturas** — Emisión y seguimiento de facturas
- **Pagos** — Registro de pagos recibidos

---

## 👥 Autores

Desarrollado como proyecto de gestión veterinaria con Vue 3 + Vite.

---

## 📄 Licencia

Este proyecto es de uso académico/privado. Todos los derechos reservados.
