Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Backlog
=======

- [TEST] Expand the long click area to the ScrollView or better still the whole app
- [BUG] If you are in the middle of a chain of actions (e.g. taking an item) and the back key is pressed to cancel, then the actions are not reset to the topmost level.
- [REFACTOR]
  - [TEST] View/Presenter moveThroughExit interface should use 'Exit' actions instead of Exit objects directly and should use a common 'UserActionHandler::handleAction' interface
- [FEATURE] Add ability to drop objects from the inventory into the current room
- [FEATURE] Allow the user to examine objects in the current location without taking them first


- Add monsters, npcs

- Add other verbs - attack, eat, drink, dance, take, drop, give, talk

- Save progress
  - Add a menu on the Android menu key so the user can select 'save'.
  - Autosave

- Optional images to go with text?

- Read the map from a text file for easy configurability.
- Read object data from a text file for easy configurability.
- Read npc/monster data from a text file for easy configurability.

Done
====

- [FEATURE] The user must be able to scroll the main text view when it gets full up
- [FEATURE] Add ability to pick up objects from the current location
- [FEATURE] Add ability to examine items in the inventory
- [FEATURE] Add ability to show inventory
- [FEATURE] Add ability navigate between locations using the four edges of the screen

