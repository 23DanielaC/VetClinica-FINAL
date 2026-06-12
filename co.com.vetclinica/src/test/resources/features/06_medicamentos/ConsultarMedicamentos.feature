# language: es
Característica: Consulta del inventario de medicamentos
  Como administrador de la clínica veterinaria
  Quiero consultar el inventario de medicamentos
  Para conocer el catálogo disponible (solo lectura)

  @medicamentos @consultar
  Escenario: Consultar el inventario de medicamentos
    Dado que el usuario inició sesión como administrador
    Cuando consulta el inventario de medicamentos
    Entonces debería visualizar el catálogo de medicamentos
