Feature: Transferência entre contas correntes

    A função de transferência permite que um valor seja debitado da conta de origem 
    e creditado na conta de destino

Scenario: Transferencia realizada com sucesso
    Given que o titular da conta está logado
    When o titular solicitar a transferência
        And o valor a ser transferido for menor ou igual ao saldo mais o crédito
        And o valor a ser transferido não excede o limite de operação
        And o valor a ser transferido não excede o limite diário
    Then o valor é debitado da conta do titular
        And o valor é creditado na conta de destino
        And dados referentes a operação de transferência são armazenados como log da operação        

Scenario: O valor a ser transferido é maior que o saldo mais o crédito
    Given que o titular da conta está logado
    When o titular solicitar a transferência
        And o valor a ser transferido for maior que o saldo mais o crédito
    Then uma mensagem é mostrada informando que não há saldo e crédito suficientes

Scenario: O valor a ser transferido excede o limite de operação
    Given que o titular da conta está logado
    When o titular solicitar a transferência
        And o valor a ser transferido excede o limite de operação
        And nenhuma operação de movimentação pendente registrada para conta do titular
    Then a transferẽncia é registrada como operação pendente para futura execução
        And uma mensagem é mostrada informando que a transferência está pendente    

Scenario: Valor excede o limite de operação e já há registrada uma operação pendente
    Given que o usuário titular da conta está logado
    When o titular solicita a transferência
        And o valor a ser transferido é excede o limite de operação
        And existe uma operação de movimentação pendente sobre a conta do titular
    Then uma mensagem é mostrada informando que a transferẽncia não pode ser realizada e o motivo

Scenario: Valor a ser transferido excede o limite diário
    Given que o usuário titular da conta está logado
    When o titular solicita a transferência
        And o valor a ser transferido excede o limite diário
    Then uma mensagem é mostrada informando que a transferência excedeu o limite diário