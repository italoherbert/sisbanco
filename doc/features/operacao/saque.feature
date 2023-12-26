Feature: Saque em conta corrente

    O saque em conta corrente acontece com o débito em conta corrente que simula uma 
    retirada de dinheiro da conta

Scenario: Saque realizado com sucesso
    Given que o usuário titular da conta está logado
    When o titular solicita o saque
        And o valor a ser sacado é menor ou igual ao saldo mais o crédito
        And o valor a ser sacado não excede o limite de operação
        And o valor a ser sacado não excede o limite diário
    When o saque é realizado com sucesso
        And dados referentes a operação de débito são armazenados como log da operação        

Scenario: Valor a ser sacado maior que o saldo mais o crédito
    Given que o usuário titular da conta está logado        
    When o titular solicita o saque
        And o valor a ser sacado é maior que o saldo mais o crédito
    Then o saque não acontece
        And uma mensagem é mostrada informando que o saldo e crédito são insuficientes

Scenario: Valor excede o limite de operação
    Given que o usuário titular da conta está logado
    When o titular solicita o saque
        And o valor a ser sacado excede o limite de operação
        And nenhuma operação de movimentação pendente sobre a conta do titular
    Then o saque é registrado como operação pendente para futura execução
        And uma mensagem é mostrada informando que o saque está pendente

Scenario: Valor excede o limite de operação e já há registrada uma operação pendente
    Given que o usuário titular da conta está logado
    When o titular solicita o saque
        And o valor a ser sacado excede o limite de operação
        And existe uma operação de movimentação pendente sobre a conta do titular
    Then uma mensagem é mostrada informando que o saque não pode ser realizado e o motivo

Scenario: Valor a ser sacado excede o limite diário
    Given que o usuário titular da conta está logado
    When o titular solicita o saque
        And o valor a ser sacado excede o limite diário
    Then uma mensagem é mostrada informando que o saque excedeu o limite diário