Feature: Alterar dados da conta

    Alteração limitada de dados da conta de um titular

Scenario: Conta alterada com sucesso
    Given que o usuario titular logado em sua conta
    When o usuário solicita alteração de dados que ele pode alterar
    Then os dados são alterados com sucesso

Scenario: Existe outro titular com o novo nome de titular informado
    Given que o usuário titular está logado em sua conta
    When o usuário solicita a alteração de dados que ele pode alterar
        And o novo nome do titular é igual a algum outro nome de titular já registrado no sistema
    Then o sistema mostra uma mensagem informando que o novo nome de titular não está disponível