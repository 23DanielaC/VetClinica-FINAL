# language: es
Característica: Consulta del directorio de veterinarios
  Como administrador de la clínica veterinaria
  Quiero consultar el directorio de veterinarios
  Para conocer el equipo médico disponible (catálogo de solo lectura)

  @veterinarios @consultar
  Escenario: Consultar el directorio de veterinarios
    Dado que el usuario inició sesión como administrador
    Cuando consulta el directorio de veterinarios
    Entonces debería visualizar el directorio del equipo médico veterinario
