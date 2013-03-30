Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Don't forget to drive the development by producing value - the value is game content and this should drive the development of functionality to support it.

Backlog
=======

- [STORY] Find clock hour hand in untakeable bags of junk somewhere.
  - [TEST] New trigger ItemAction on examine, ChangeItemVisibility( clock hour hand )
    DONE - Examine.trigger() needs to call Item.examine() and add Item.examineText() to the normal output for examine.
    - [Normal]Item needs to implement examine() to call examine actions
    - [Normal]Item needs to implement addOnExamineAction()
    - Deserialiser needs to parse 'item on examine action:action name:parameters'
    - [Normal]Item needs to implement examineText()
    - Deserialiser needs to parse 'item examine message:You look closer and see a ... It drops at your feet.'
    - [Normal]Item needs to return a blank examineText() after examine() if examine action is not repeatable
    - Deserialiser needs to parse 'item examine action is not repeatable:'
- [STORY] Take clock face to top of clock tower and use with clock mechanism and minute hand and hour hand. Get some points or money or something and maybe a quest/hint as to what to do next - open up a new area.
  - [TEST] ItemAction DestroyItem (clock face, minute hand, hour hand)
  - [TEST] Succesful use text "You fit the final piece of the clock. The air stirs around you as you slide it gently into place. blah blah you get some rubies"
  - [TEST] New ItemAction something like AwardRubies

- [FEATURE-EDITOR] Update webapp editor to [de]serialise visibility and allow setting of visibility
- [FEATURE-EDITOR] Update webapp editor to allow adding/deletion of on examine actions to items

- [REFACTOR] Item interface is getting fat. Can 'use' and 'examine' be split off, perhaps Examinable, Usable? Is there any benefit?

- [FEATURE] Show a ruby counter at the top of the screen. Also have a nice little ruby graphic.
- [REFACTOR] LocationTests and TakeSpecificItemTests are using real NormalItems - change to use mocks
- [TEST] action view includes a cancel button to reset the actions to the top level
- [FEATURE] Actions buttons view should be fixed size and scrollable - 1/4 to 1/3 of the screen height
- [FEATURE] Ruby counter should show how many out of how many achieved - e.g. 10/100

- [FEATURE] Direction navigator - instead of top, bottom, right, left clickable labels use a compass with small labels.

- [FEATURE] In model content txt items can only be used in one direction, e.g. a spade with a mound of earth and the target item has to include the definition of what happens. If the original item (e.g. spade) includes this information it is ignored. Should this be changed to be bi-directional? It would make writing content easier but needs considering as it might not always be appropriate.

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

- [FEATURE] New ItemAction ChangeItemVisibility
- [FEATURE] New ItemAction DestroyItem
- [FEATURE] Items can be used with other items. This iteraction must be specified in the model content text and is limited to the following actions - change item name, change item description, make an exit visible
- [FEATURE] New default action 'Examine some item' - can be used to look at items in the current location without picking it up
- [FEATURE] Replaced long press context menu for actions with actions buttons
- [FEATURE] Read a model setup from a plain text file
- [FEATURE] The user must be able to scroll the main text view when it gets full up
- [FEATURE] Add ability to pick up objects from the current location
- [FEATURE] Add ability to examine items in the inventory
- [FEATURE] Add ability to show inventory
- [FEATURE] Add ability navigate between locations using the four edges of the screen

- [STORY] Use spade on mound of dirt outside the town to uncover a clock face.

- [BUG] Using key on the door doesn't change door name from locked to unlocked...
  - [FIX] Mid-sentence cased name was being set in the demo content and overriding the new name
