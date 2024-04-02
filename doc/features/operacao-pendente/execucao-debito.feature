Feature: Execução de operação de débito/saque

    A execução de operação de débito se refere a execução da operação de movimentação 
    (transação) tipo débito registrada como pendente quando o valor de débito excedeu 
    o limite de operação

Scenario: Operação executada com sucesso
    Given que o funcionário está logado
    When o funcionario solicita a execução do débito
        And o valor de débito é menor ou igual a o 'saldo + crédito' da conta
    Then o valor de débito é debitado da conta
        And dados referentes a operação são armazenados como log da operação

Scenario: Valor de débito excede o 'saldo + credito' na conta de origem
    Given que o funcionário está logado
    When o funcionario solicita a execução do débito
        And o valor de débito excede o 'saldo + credito' da conta
    Then a operação continua pendente
        And é mostrada uma mensagem informando sobre o saldo insuficiente