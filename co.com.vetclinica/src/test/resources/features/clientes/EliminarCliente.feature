# language: es
Característica: Restricción de eliminación de clientes
  Como administrador de la clínica veterinaria
  Quiero recibir un aviso al intentar eliminar un cliente
  Para entender que la API no permite el borrado de registros

  @clientes @eliminar
  Escenario: Intentar eliminar un cliente muestra el aviso de restricción
    Dado que el usuario inició sesión como administrador
    Y navega al módulo "Clientes"
    Y existe un cliente registrado con nombre "Cliente Para Eliminar QA"
    Cuando intenta eliminar el cliente "Cliente Para Eliminar QA"
    Entonces el sistema muestra el mensaje "La API no permite eliminar clientes."
