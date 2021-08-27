// стиль написания сценария на языке керкен - Behaviour Driven Testing (запускаем при помощи инструмента cucumber)
Feature: Groups

 Scenario Outline: Group creation // чтобы сделать сценарий параметризованным, добавляем "Outline", секцию "Examples" и в блоке "When" шде указывались конкретные значения (xxx) поменять на <name>
   Given a set of groups
   When I create a new group with name <name>, header <header> and footer <footer>
   Then the new set of groups is equal to the old set with the added group

   Examples:
   | name      |header        |footer       |
   |test name  |test header   |test footer  |