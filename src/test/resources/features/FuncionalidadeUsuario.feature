# language: pt

Funcionalidade: API Usuario

   Cenário: Cadastrar um novo usuario
     Quando submeter um novo usuario
     Então o usuario é registrado com sucesso
     E retornado o usuario registrado