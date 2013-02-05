Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Backlog
=======

- [FEATURE] Add ability to pick up objects from the current room
  - [TEST] Presenter tells view that 'Pick up' action is available in rooms with objects in
  - [TEST] Pick up action has choices, i.e. the object to pick up. Upon triggering the action the presenter must tell the view what choices are available
  - [TEST] View tells presenter which object is chosen for pick up action
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

