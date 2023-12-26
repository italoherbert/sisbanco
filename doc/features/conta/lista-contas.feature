Feature: Filtragem de contas registradas no sistema

    Esta funcionalidade trata da filtragem de contas no sistema que pode, tanto listar todas 
    as contas, ou apenas as contas conforme o filtro

Scenario: Filtragem ocorrida com sucesso
    Given que o funcionario está logado no sistema
    When o funcionario solicitar a filtragem de contas
    Then a lista de contas conforme o filtro é gerada e mostrada