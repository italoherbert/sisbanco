Feature: Alteração do valor do limite diário

    Esta funcionalidade trata da alteração do valor do limite diário associado a uma 
    conta de um titular cliente do banco

Scenario: Limite diário alterado com sucesso
    Given que o funcionario está logado
    When o funcionário solicita a execução da alteração do limite diário
        And o novo valor do limite diário é maior ou igual a zero
    Then o valor do limite diário é alterado com sucesso
        And dados referentes a alteração são armazenados como log da operação

Scenario: Valor negativo
    Given que o funcionario está logado
    When o funcionário solicita a execução da alteração do limite diário
        And o novo valor do limite diário é negativo
    Then é mostrada uma mensagem informando que o novo valor informado é negativo