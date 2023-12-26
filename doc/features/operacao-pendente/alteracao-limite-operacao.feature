Feature: Alteração do valor do limite de operação

    Esta funcionalidade trata da alteração do valor do limite de operação associado a uma 
    conta de um titular cliente do banco. O limite de operação é o valor limite máximo 
    de uma única operação

Scenario: Limite de operação alterado com sucesso
    Given que o funcionario está logado
    When o funcionário solicita a execução da alteração do limite de operação
        And o novo valor do limite de operação é maior ou igual a zero
    Then o valor do limite de operação é alterado com sucesso
        And dados referentes a alteração são armazenados como log da operação

Scenario: Valor negativo
    Given que o funcionario está logado
    When o funcionário solicita a execução da alteração do limite de operação
        And o novo valor do limite de operação é negativo
    Then é mostrada uma mensagem informando que o novo valor informado é negativo