# Help can be found here https://github.com/cucumber/cucumber-jvm

Feature: Enter the town
  In order to satisfy my curiosity
  As a player with a history in the town
  I want to enter the town and find out why it looks so dishevelled

  @things_that_should_not_happen, @reload_TTA2_model
  Scenario: Paraffin soaked torch cannot be created without a rag_wrapped_stick and a jar_of_paraffin
    Given the player has a stick
      When they use the stick with the jar_of_paraffin
      Then the player does not have a paraffin_soaked_torch

    Given the player has a rag_wrapped_stick
      When they use the rag_wrapped_stick with the jar_of_clear_liquid
      Then the player has a rag_wrapped_stick
      Then the player does not have a paraffin_soaked_torch

  @happy_path, @reload_TTA2_model
  Scenario: Create a fiery_torch
    Given the player has a dirty_rag and a stick
      When they use the dirty_rag with the stick
      Then the player has a rag_wrapped_stick
      And the player does not have a dirty_rag
      And the player does not have a stick

    Given the player has a rag_wrapped_stick
      When they use the rag_wrapped_stick with the jar_of_paraffin
      Then the player has a torch
      And the player does not have a rag_wrapped_stick

    Given the player has a torch
      When they use the torch with the glowing_embers
      Then the player has a fiery_torch
      And the player does not have a torch

  @happy_path, @reload_TTA2_model
  Scenario: Use the fiery_torch
    Given the player has a fiery_torch
      When they use the fiery_torch with the fell_beast
      Then the storeroom_door opens
