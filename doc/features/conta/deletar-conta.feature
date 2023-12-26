Feature: Deletar conta de usuário

    Esta função de remoção de conta se refere a remoção da conta e usuário de um titular

Scenario: Conta deletada com sucesso
    Given que o funcionário ou usuário titular está logado
        And que a conta foi encontrada
    When o funcionário ou titular da conta solicitar a remoção
    Then o usuário é removido no keycloak
        And a conta é removida

Scenario: Falha na remoção do usuário no keycloak
    Given que o funcionário ou usuário titular está logado
        And que a conta foi encontrada
    When o funcionário ou titular solicitar a remoção
    Then acontece falha na remoção do usuário no keycloak
        And uma mensagem é mostrada para o usuário informando que não foi possível remover o usuário