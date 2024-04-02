
Feature: Exibição de extrato da conta

    O usuário titular da conta pode solicitar um resumo dos dados de sua conta bancária 
    e, então, é exibido um extrato da conta

Scenario: Extrato gerado com sucesso
    Given que o usuário titular da conta está logado
    When o usuário titular solicitar a geração de extrato da conta
    Then um resumo dos dados da conta é gerado e mostrado