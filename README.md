Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Don't forget to drive the development by producing value - the value is game content and this should drive the development of functionality to support it.

Backlog
=======

- [FEATURE] Read a model setup from a plain text file
  - [TEST] ItemFactory.create() should supercede ItemFactory.create( String x4 )
  - [TEST] Activity should create a PlainTextModelPopulator and give it the model to populate as well as some plain text describing the model contents. Change the demo model content to use this mechanism.
- [FEATURE] Write some decent content! Expand it and expand the framework to supply functionality to support the content.

- [FEATURE] Action navigator - instead of long click context menu sequence use a sidebar which either expands or gets replaced (with a title to preserve context e.g. 'take', 'drop').
- [FEATURE] Direction navigator - instead of top, bottom, right, left clickable labels use a compass with small labels.

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

Done
====

- [FEATURE] The user must be able to scroll the main text view when it gets full up
- [FEATURE] Add ability to pick up objects from the current location
- [FEATURE] Add ability to examine items in the inventory
- [FEATURE] Add ability to show inventory
- [FEATURE] Add ability navigate between locations using the four edges of the screen

