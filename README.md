Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Don't forget to drive the development by producing value - the value is game content and this should drive the development of functionality to support it.

Backlog
=======

- [REFACTOR] Consider making the order of statements in the model text irrelevant. Currently the deserialisation depends on a strict ordering - this might be a pain and lead to silly errors in the model text that are not obvious to find.
- [REFACTOR] Moves items into their own namespace
- [REFACTOR] Exits should deserialise themselves - not Location
- [REFACTOR] Deserialisation should be pulled out into seperate classes - ModelDeserialiser, ModelLocationDeserialiser, ItemDeserialiser - all coordinate with a PlainTextDeserialiser used by the PlainTextModelPopulator

- [TEST] action view includes a cancel button to reset the actions and the title to the top level

- [FEATURE] Actions buttons view should be fixed size and scrollable - 1/4 to 1/3 of the screen height

- [FEATURE] Direction navigator - instead of top, bottom, right, left clickable labels use a compass with small labels.

- [FEATURE] Action navigator - instead of long click context menu sequence use a sidebar which either expands or gets replaced (with a title to preserve context e.g. 'Show inventory...', 'Take an item...').
  - [TEST] action view title defaults to 'Actions...'
  - [TEST] activity sets action view title when user selects an action

- [REFACTOR]
  - [TEST] View/Presenter moveThroughExit interface should use 'Exit' actions instead of Exit objects directly and should use a common 'UserActionHandler::handleAction' interface

- Add monsters, npcs

- Add other verbs - attack, eat, drink, dance, take, drop, give, talk

- Save progress
  - Add a menu on the Android menu key so the user can select 'save'.
  - Autosave

- Optional images to go with text?

Done
====

- [FEATURE] Items can be used with other items. This iteraction must be specified in the model content text and is limited to the following actions - change item name, change item description, make an exit visible
- [FEATURE] New default action 'Examine some item' - can be used to look at items in the current location without picking it up
- [FEATURE] Replaced long press context menu for actions with actions buttons
- [FEATURE] Read a model setup from a plain text file
- [FEATURE] The user must be able to scroll the main text view when it gets full up
- [FEATURE] Add ability to pick up objects from the current location
- [FEATURE] Add ability to examine items in the inventory
- [FEATURE] Add ability to show inventory
- [FEATURE] Add ability navigate between locations using the four edges of the screen

