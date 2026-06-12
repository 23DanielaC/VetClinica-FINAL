# language: es
Característica: Registro de clientes
  Como administrador de la clínica veterinaria
  Quiero registrar nuevos clientes
  Para mantener actualizada la base de datos de propietarios

  @clientes @agregar
  Escenario: Registrar un nuevo cliente con datos válidos
    Dado que el usuario inició sesión como administrador
    Y navega al módulo "Clientes"
    Cuando registra un cliente con los siguientes datos
      | nombre                  | telefono   | email                  | direccion        |
      | Cliente Automatizado QA | 3001234567 | cliente.qa@example.com | Calle 123 #45-67 |
    Entonces el cliente "Cliente Automatizado QA" debería aparecer en el listado
