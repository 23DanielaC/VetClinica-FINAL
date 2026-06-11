# VetNova – Sistema de Gestión Veterinaria

## 🚀 Instalación y ejecución

### Requisitos
- Node.js 18+ (https://nodejs.org)
- npm (incluido con Node.js)

### Pasos

```bash
# 1. Descomprime el ZIP en una carpeta
# 2. Abre una terminal en esa carpeta y ejecuta:

npm install

# 3. Inicia el servidor de desarrollo:
npm run dev

# 4. Abre en el navegador:
# http://localhost:5173
```

### Credenciales de acceso (demo)
- **Email:** admin@vetnova.com
- **Contraseña:** admin123

---

## 📦 Generar versión para producción / XAMPP

```bash
npm run build
```

Esto genera una carpeta `dist/`. Copia el contenido de `dist/` dentro de `C:/xampp/htdocs/vetnova/` y accede desde:

```
http://localhost/vetnova/
```

> **Nota:** La app usa `createWebHashHistory` (rutas con #), por eso funciona sin configuración
> especial en Apache/XAMPP.

---

## 🗂 Estructura del proyecto

```
src/
├── assets/          → CSS global (Tailwind)
├── components/
│   ├── layout/      → AppLayout (Sidebar + Navbar)
│   └── ui/          → CrudTable, CrudModal, StatCard
├── stores/          → auth.js, data.js (Pinia)
├── router/          → index.js
└── views/           → Login + Dashboard + 9 CRUDs
```

## 🔧 Tecnologías
- Vue 3 + Vite
- Vue Router 4
- Pinia (estado global)
- Tailwind CSS
- Heroicons Vue
- Axios (listo para conectar tu API)
