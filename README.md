Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Don't forget to drive the development by producing value - the value is game content and this should drive the development of functionality to support it.

Backlog
=======

- [FEATURE] Action navigator - instead of long click context menu sequence use a sidebar which either expands or gets replaced (with a title to preserve context e.g. 'Show inventory...', 'Take an item...').
  - [TEST] action view has buttons showing currently available actions
  - [TEST] pressing an action button triggers that action
  - [TEST[ action view is updated with follow up actions
  - [TEST] action view title defaults to 'Actions...'
  - [TEST] activity sets action view title when user selects an action
  - [TEST] action view includes a cancel button to reset the actions and the title to the top level
- [FEATURE] Direction navigator - instead of top, bottom, right, left clickable labels use a compass with small labels.

- [TEST] Expand the long click area to the ScrollView or better still the whole app
  - Redundant if the long click menu is removed
- [BUG] If you are in the middle of a chain of actions (e.g. taking an item) and the back key is pressed to cancel, then the actions are not reset to the topmost level.
  - Redundant if the long click menu is removed

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

Done
====

- [FEATURE] Read a model setup from a plain text file
- [FEATURE] The user must be able to scroll the main text view when it gets full up
- [FEATURE] Add ability to pick up objects from the current location
- [FEATURE] Add ability to examine items in the inventory
- [FEATURE] Add ability to show inventory
- [FEATURE] Add ability navigate between locations using the four edges of the screen

