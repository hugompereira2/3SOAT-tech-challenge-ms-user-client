# language: pt

Funcionalidade: API Cliente

   Cenário: Cadastrar um novo cliente
     Quando submeter um novo cliente
     Então o cliente é registrado com sucesso
     E retornado o cliente registrado

   Cenário: Listar cliente existente
    Dado que um cliente já foi cadastrado
    Quando requisitar a busca de um cliente
    Então o cliente é retornado com sucesso