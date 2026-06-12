# language: es
Característica: Agendamiento de turnos
  Como administrador de la clínica veterinaria
  Quiero agendar turnos para las mascotas
  Para organizar la atención del equipo veterinario

  @turnos @agendar
  Escenario: Agendar un nuevo turno
    Dado que el usuario inició sesión como administrador
    Y navega al módulo "Turnos"
    Cuando agenda un turno con los siguientes datos
      | cliente       | mascota | veterinario      | motivo           |
      | Ana Rodríguez | Canela  | Dr. Andrés López | Control anual QA |
    Entonces el turno de la mascota "Canela" debería aparecer en la agenda
