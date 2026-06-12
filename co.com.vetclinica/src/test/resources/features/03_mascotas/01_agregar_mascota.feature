# language: es
Característica: Registro de mascotas
  Como administrador de la clínica veterinaria
  Quiero registrar nuevas mascotas
  Para asociarlas a sus propietarios y llevar su seguimiento

  @mascotas @agregar
  Escenario: Registrar una nueva mascota con datos válidos
    Dado que el usuario inició sesión como administrador
    Y navega al módulo "Mascotas"
    Cuando registra una mascota con los siguientes datos
      | nombre         | especie | raza    | propietario   |
      | Firulais QA Bot | Perro   | Criollo | Ana Rodríguez |
    Entonces la mascota "Firulais QA Bot" debería aparecer en el listado
