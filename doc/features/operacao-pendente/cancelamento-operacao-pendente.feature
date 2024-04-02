Feature: Cancelamento de operação pendente

    O cancelamento de operação pendente remove a operação pendente sem executá-la

Scenario: Cancelamento executado com sucesso
    Given que o funcionário está logado no sistema
    When o funcionário solicita o cancelamento de uma operação pendente
    Then a operação pendente é removida
        And a operação é cancelada com sucesso
        And dados referentes ao cancelamento são armazenados como log da operação

Scenario: Falha no registro de log
    Given que o funcionário está logado no sistema
    When o funcionário solicita o cancelamento de uma operação pendente
        And acontece uma falha no registro de log da operação cancelada
    Then a operação pendente é removida 
        And o log é mostrado na saída padrão
        And a operação é cancelada com sucesso