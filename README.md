Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Don't forget to drive the development by producing value - the value is game content and this should drive the development of functionality to support it.

Backlog
=======

- [FEATURE] Action navigator - instead of long click context menu sequence use a sidebar which either expands or gets replaced (with a title to preserve context e.g. 'Show inventory...', 'Take an item...').
  - [TEST] Remove long press context menu stuff
  - [TEST] Should the presenter be reaffirming the root actions _even_ when it sets follow up actions? This makes it hard for the view to determine the current set of actions... It should be the presenters responsibility to provide the right actions based on the state, e.g. changing location or enacting an action. Is giveUserImmediateActionChoice() even necessary, isn't setActions() enough?
  - [TEST] action view title defaults to 'Actions...'
  - [TEST] activity sets action view title when user selects an action
  - [TEST] action view includes a cancel button to reset the actions and the title to the top level

- [FEATURE] Unlock the clock tower door with the skeleton key to reveal an 'up' exit
  - [TEST] New item type - unmovable item - e.g. the clock tower door
  - [TEST] Split 'Show inventory' into items, when item is selected then given a choice of actions, initially 'Examine'
  - [TEST] New action - 'UseWith' available when an item is selected from the inventory. Follow up action 'UseWithSpecificItem' is a choice of items in the location and inventory.
  - [TEST] Enacting 'UseWithSpecificItem' calls useWith() on the item object, returning a default user message 'Nothing happens.'
  - [TEST] Model text can specify that an item can be used with another named item. This should include a message for the user upon using the item with the named item.
  - [TEST] useWith() on the item object when used with a named item it can be used with returns the defined user message.
  - [TEST] useWith() on the item object when used with a named item it can be used with changes some state as defined in the model text. In this feature case it adds an exit to the clock tower location.

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

