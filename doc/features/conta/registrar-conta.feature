Feature: registrar conta de cliente do banco

    Esta função se refere ao registro de uma conta de cliente do banco, junto da  
    criação de um usuário gerenciado pelo keycloak e vinculado a conta 
    do titular recem criada

Scenario: Conta registrada com sucesso
    Given que o funcionario está autenticado
    When o funcionario solicita o registro da conta        
    Then o usuário da conta é criado com sucesso no keycloak
        And a conta é criada com sucesso
        And o vinculo entre conta e usuário por username é realizado com sucesso

Scenario: Falha na criação do usuário
    Given que o funcionario está autenticado
    When o funcionario solicita o registro da conta
    Then o sistema exibe uma mensagem de que houve falha na criação da conta

Scenario: Username já existe
    Given que o funcionario está autenticado
    When o funcionario solicita registrar conta
        And o username informado já existe no banco de dados de contas
    Then o sistema mostra uma mensagem informando que o username informado não está disponível
