Feature: Execução da operação de transferência

    Esta funcionalidade trata da execução de uma transferência pendente entre uma conta 
    de origem e uma conta de destino

Scenario: Operação executada com sucesso
    Given que o funcionário está logado
    When o funcionario solicita a execução da transferência
        And o valor de transferência é menor ou igual a o 'saldo + crédito' da conta de origem
    Then o valor da transferência é debitado da conta de origem
        And o valor da transferência é creditado na conta de destino
        And dados referentes a operação de transferência são armazenados como log da operação

Scenario: Operação executada com sucesso
    Given que o funcionário está logado
    When o funcionario solicita a execução da transferência
        And o valor a ser transferido é menor ou igual a o 'saldo + crédito' da conta de origem
    Then o valor a ser transferido é debitado da conta de origem
        And o valor a ser transferido é creditado na conta de destino

Scenario: Valor a ser transferido excede o 'saldo + credito' na conta de origem
    Given que so funcionário está logado
    When o funcionario solicita a execução do débito
        And o valor a ser transferido excede o 'saldo + credito' da conta de origem
    Then a operação continua pendente
        And é mostrada uma mensagem informando sobre o saldo insuficiente