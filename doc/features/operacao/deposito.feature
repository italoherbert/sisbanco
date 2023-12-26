Feature: Deposito em conta corrente

    O depósito em conta corrente acontece quando o titular deposita um valor na sua 
    conta corrente

Scenario: Deposito realizado com sucesso
    Given que o usuário titular da conta está logado
    When o titular solicitar o depósito em conta corrente
    Then o depósito é realizado
        And dados referentes a operação de crédito são armazenados como log da operação        