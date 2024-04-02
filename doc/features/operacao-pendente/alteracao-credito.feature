Feature: Alteração do valor de crédito

    Esta funcionalidade trata da alteração do valor de crédito associado a uma conta de 
    um titular cliente do banco

Scenario: Crédito alterado com sucesso
    Given que o funcionario está logado
    When o funcionário solicita a execução da alteração do crédito
        And o novo valor de crédito é maior ou igual a zero
    Then o valor de crédito é alterado com sucesso
        And dados referentes a alteração são armazenados como log da operação

Scenario: Valor negativo
    Given que o funcionario está logado
    When o funcionário solicita a execução da alteração do crédito
        And o novo valor de crédito é negativo
    Then é mostrada uma mensagem informando que o novo valor informado é negativo