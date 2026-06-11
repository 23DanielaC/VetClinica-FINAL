# language: es
Característica: Inicio de sesión en VetClinica
  Como administrador de la clínica veterinaria
  Quiero iniciar sesión en el sistema
  Para acceder a la gestión de clientes, mascotas y turnos

  @login
  Escenario: Iniciar sesión con credenciales válidas
    Dado que el usuario está en la página de inicio de sesión
    Cuando inicia sesión con las siguientes credenciales
      | correo               | clave    |
      | admin@vetclinica.com | admin123 |
    Entonces debería visualizar el panel principal autenticado
