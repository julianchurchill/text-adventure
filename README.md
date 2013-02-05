Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Backlog
=======

- [FEATURE] Add ability to pick up objects from the current room
  - [TEST] Activity should create Locations with the model as the UserInventory
  - [TEST] TakeSpecificItem trigger removes item from current location in model.
  - [TEST] Location item contents state is persisted upon leaving and reentering room
  - [TEST] Objects available to pick up should be an automatically added as part of the room description - e.g. 'You are in a square room. There is a pickaxe, a banana and some pocket lint here.'
- [REFACTOR]
  - [TEST] View/Presenter moveThroughExit interface should use 'Exit' actions instead of Exit objects directly and should use a common 'UserActionHandler::handleAction' interface
- [FEATURE] Add ability to drop objects from the inventory into the current room
- [FEATURE] The user must be able to scroll the main text view when it gets full up


- Add monsters, npcs

- Add other verbs - attack, eat, drink, dance, take, drop, give, talk

- Save progress
  - Add a menu on the Android menu key so the user can select 'save'.
  - Autosave

- Optional images to go with text?

- Read the map from a text file for easy configurability.
- Read object data from a text file for easy configurability.
- Read npc/monster data from a text file for easy configurability.

