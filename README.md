Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Backlog
=======

- [FEATURE] Add ability to pick up objects from the current room
  - [TEST] TakeAnItem creates TakeSpecificItems with the UserInventory
  - [TEST] Location creates TakeAnItem with the UserInventory
  - [TEST] BasicModel implements UserInventory interface to add the item to the inventory
  - [TEST] Locations are created with a UserInventory
  - [TEST] TakeSpecificItem trigger removes item from current location in model.
  - [TEST] Location item contents state is persisted upon leaving and reentering room
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

