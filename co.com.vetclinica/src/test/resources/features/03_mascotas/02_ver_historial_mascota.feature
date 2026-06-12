# language: es
Característica: Consulta del historial médico de mascotas
  Como administrador de la clínica veterinaria
  Quiero consultar el historial médico de una mascota
  Para revisar sus consultas y vacunas registradas

  @mascotas @historial
  Escenario: Consultar el historial médico de una mascota
    Dado que el usuario inició sesión como administrador
    Y navega al módulo "Mascotas"
    Y existe una mascota registrada con nombre "Historial QA Bot"
    Cuando consulta el historial médico de la mascota "Historial QA Bot"
    Entonces debería visualizar el historial médico de "Historial QA Bot"
