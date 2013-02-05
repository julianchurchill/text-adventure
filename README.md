Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Backlog
=======

- [FEATURE] Add ability to pick up objects from the current room
  - [TEST] NormaItem is a value object
  - [TEST] TakeAnItem is a value object
  - [TEST] TakeAnItem label should be 'Take an item'
  - [TEST] TakeAnItem has follow up actions of TakeSpecificItem actions populated from items in current location
  - [TEST] TakeSpecificItem has user text 'You take {item name}.'
  - [TEST] View tells presenter which object is chosen for take action
  - [TEST] Presenter adds object to user inventory
  - [TEST] Presenter removes object from room - state is persisted upon leaving and reentering room
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

