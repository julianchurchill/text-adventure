Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Don't forget to drive the development by producing value - the value is game content and this should drive the development of functionality to support it.

Backlog
=======

* [FEATURE] Save file backwards compatibility alternative
  * Save current score, inventory content, exits visibility, item visibility, examined and used states, changed item descriptions and names, changed location descriptions and names, save file format version number
  * Save destroyed item ids so items can be removed upon load - these can be saved as a list in the model for version 2.0
  * On load read in base world content first then replace corresponding world data with the saved items. Remove destroyed items.
  * For backwards compatibility with version 1.0 if save file detected load 1.0 save file as new world, figure out what items have been destroyed and add to destroyed list in model, trigger an immediate save, then reload base world content and new save file. Delete old 1.0 save file.
  * In future expansion of world be careful to only add new exits, locations, items and actions, not modify existing exits, locations, items or actions.

* [FEATURE] Save file backwards compatibility
  * [TASK] Game world must be serialised and saved to a file - this is the new save game scheme
    * DONE - [TEST] Activity uses PlainTextSerialiser onPause to save game to a file
    * [TEST] PlainTextSerialiser for Model - including private info e.g. current score
    * [TEST] PlainTextModelSerialiser should save a version number for the file/model format
    * [TEST] Collate de/serialise constant strings in a common class
    * [TEST] PlainTextModelPopulator should read an optional version number for the file/model format
    * [TEST] PlainTextExitSerialiser for Exits - including visibility
    * [TEST] PlainTextExitSerialiser should save a version number for the serialisation format
    * [TEST] PlainTextExitDeserialiser should read an optional version number for the serialisation format
    * [TEST] PlainTextLocationSerialiser for Locations
    * [TEST] PlainTextLocationSerialiser should save a version number for the serialisation format
    * [TEST] PlainTextLocationDeserialiser should read an optional version number for the serialisation format
    * [TEST] PlainTextItemSerialiser for Items/ItemActions - including whether item has been used or examined
    * [TEST] PlainTextItemSerialiser should save a version number for the serialisation format
    * [TEST] PlainTextItemDeserialiser should read an optional version number for the serialisation format
    * ??? How do we ensure compatibility between Serialiser and Deserialiser as development continues?
  * [TASK] ModelMerger needs implementing
    * [TEST] ModelMerger adds new locations from input to target
    * [TEST] ModelMerger adds new exits from input to target
    * [TEST] ModelMerger adds new items from input to target
    * [TEST] ModelMerger removes missing locations from target that are not longer in input
    * [TEST] ModelMerger removes missing exits from target that are not longer in input
    * [TEST] ModelMerger removes missing items from target that are not longer in input
    * [TEST] ModelMerger modifies properties of locations that have changed in input compared to target
    * [TEST] ModelMerger modifies properties of exits that have changed in input compared to target
    * [TEST] ModelMerger modifies properties of items that have changed in input compared to target
  * [TASK] Activity save file logic as above needs implementing
    * [TEST] On startup activity loads the base content into a model object.
    * [TEST] If a new save file is available the activity loads it in another model object.
    * [TEST] If not then if an old save file is available the activity loads it in another model object.
    * [TEST] Finally the activity merges the save file model into the base content model and uses the result as the current world.

- [BUG] User still has map after giving it to the shopkeeper!
- [FEATURE] Exits should have consistent colours for direction hints - e.g. all North should be green
- [FEATURE] Tablet improvements - make work in landscape, use bigger font depending on screen size
- [FEATURE] Let user change the font size on an options screen
- [FEATURE] Record the number of moves taken so far, present as a form of score on the about dialog or a stats page?

- [REFACTOR] LocationTests and TakeSpecificItemTests are using real NormalItems - change to use mocks
- [FEATURE] Actions buttons view should be fixed size and scrollable - 1/4 to 1/3 of the screen height
- [FEATURE] Ruby counter should show how many out of how many achieved - e.g. 10/100 - this needs to be a value specified in the model content text file. It is currently hard coded in BasicModel.java.

- [FEATURE-EDITOR] Update webapp editor to [de]serialise visibility and allow setting of visibility
- [FEATURE-EDITOR] Update webapp editor to allow adding/deletion of on examine actions to items

- [REFACTOR] Item interface is getting fat. Can 'use' and 'examine' be split off, perhaps Examinable, Usable? Is there any benefit?

- [FEATURE] In model content txt items can only be used in one direction, e.g. a spade with a mound of earth and the target item has to include the definition of what happens. If the original item (e.g. spade) includes this information it is ignored. Should this be changed to be bi-directional? It would make writing content easier but needs considering as it might not always be appropriate.

- [REFACTOR]
  - [TEST] View/Presenter moveThroughExit interface should use 'Exit' actions instead of Exit objects directly and should use a common 'UserActionHandler::handleAction' interface

- Add monsters, npcs

- Add other verbs - attack, eat, drink, dance, take, drop, give, talk

- Optional images to go with text?

Done
====

2.0
---

1.0
---

- [FEATURE] New game - resets everything. Available in the main menu.
- [FEATURE] Autosave upon leaving the app (with the back key usually) so upon returning the user can continue their game.
- [FEATURE] Respond to the 'Back' button by cancelling the action chain if in the middle of an action chain, quit game otherwise.
- [FEATURE] New ItemAction increment score.
- [FEATURE] New ItemAction change location description.
- [FEATURE] About dialog available from main menu.
- [FEATURE] Direction label buttons are replaced with hyper-text exits in description
- [FEATURE] New event 'on examine' for ItemActions to be trigger upon
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

- [STORY] Find a man manacled to a rock. If you use the multitool and the man, the man reacts and cowers.
   - Find an axe handle and an axe head. Use with each other to make a blunt pick axe.
   - Find an axe sharpener, use it on the pick axe.
   - Use the pick axe to chip away at the rock and free the man.
   - Man is free but looks lost, you must give him a map found by examining a table in a nearby room to help him get back to town.
   - Get another ruby.
- [STORY] Take clock face to top of clock tower and use with clock mechanism and minute hand and hour hand. Get some rubies as points and a hint as to what to do next - opens up a new area near the mine.
- [STORY] Use the skeleton key to unlock the clock tower.
- [STORY] Pick up minute hand from outside the clock tower.
- [STORY] Use spade on mound of dirt outside the town to uncover a clock face.
- [STORY] Find clock hour hand in untakeable bags of junk somewhere.

- [BUG] Using key on the door doesn't change door name from locked to unlocked...
  - [FIX] Mid-sentence cased name was being set in the demo content and overriding the new name
