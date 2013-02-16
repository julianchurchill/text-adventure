Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Don't forget to drive the development by producing value - the value is game content and this should drive the development of functionality to support it.

Backlog
=======

- [FEATURE] Unlock the clock tower door with the skeleton key to reveal an 'up' exit
  - [TEST] 'Inventory item' action leads to 'Examine' follow up action
  - [TEST] Change the 'Show inventory' action to make an 'Inventory item' follow up action for each item in the inventory with the item and a label of 'item name'.
  - [TEST] New action - 'UseWith' - add to 'Inventory item' action as additional follow up action. Follow up action 'UseWithSpecificItem' is a choice of items in the location and inventory.
  - [TEST] Enacting 'UseWithSpecificItem' calls useWith() on the item object, returning a default user message 'Nothing happens.'
  - [TEST] Model text can specify that an item can be used with another named item. This should include a message for the user upon using the item with the named item.
  - [TEST] useWith() on the item object when used with a named item it can be used with returns the defined user message.
  - [TEST] useWith() on the item object when used with a named item it can be used with changes some state as defined in the model text. In this feature case it adds an exit to the clock tower location.

- [FEATURE] Actions buttons view should be fixed size and scrollable - 1/4 to 1/3 of the screen height

- [FEATURE] Direction navigator - instead of top, bottom, right, left clickable labels use a compass with small labels.

- [FEATURE] Action navigator - instead of long click context menu sequence use a sidebar which either expands or gets replaced (with a title to preserve context e.g. 'Show inventory...', 'Take an item...').
  - [TEST] action view title defaults to 'Actions...'
  - [TEST] activity sets action view title when user selects an action
  - [TEST] action view includes a cancel button to reset the actions and the title to the top level

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

- [FEATURE] New default action 'Examine some item' - can be used to look at items in the current location without picking it up
- [FEATURE] Replaced long press context menu for actions with actions buttons
- [FEATURE] Read a model setup from a plain text file
- [FEATURE] The user must be able to scroll the main text view when it gets full up
- [FEATURE] Add ability to pick up objects from the current location
- [FEATURE] Add ability to examine items in the inventory
- [FEATURE] Add ability to show inventory
- [FEATURE] Add ability navigate between locations using the four edges of the screen

