Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Don't forget to drive the development by producing value - the value is game content and this should drive the development of functionality to support it.

Backlog
=======

* [FEATURE] Save file new format
  * An ActionFactory is needed for user actions like Examine, TakeAnItem, ShowInventory. This needs to be passed to the Presenter and the other Actions that create actions.
    * DONE [TEST] Presenter should use an ActionFactory
    * DONE [TEST] UserActionFactory should implement createShowInventoryAction()
    * DONE [TEST] Activity should give the presenter a UserActionFactory
    * DONE [TEST] ShowInventory should use an ActionFactory
    * DONE [TEST] InventoryItem should use an ActionFactory
    * DONE [TEST] UserActionFactory should implement createExamineAction()
    * DONE [TEST] UserActionFactory should implement createUseWithAction()
    * DONE [TEST] ExamineAnItem should use an ActionFactory
    * DONE [TEST] Location needs to use ActionFactory to create ExamineAnItem action
    * DONE [TEST] UserActionFactory should implement createExamineAnItemAction()
    * DONE [TEST] Remove Location constructor that does not take an ActionFactory
    * DONE [TEST] Remove LocationFactory constructor that does not take an ActionFactory
    * DONE [TEST] Location needs to use ActionFactory to create TakeAnItem action
    * DONE [TEST] UserActionFactory should implement createTakeAnItemAction()
    * DONE [TEST] TakeAnItem should use an ActionFactory
    * DONE [TEST] Remove TakeAnItem constructor that does not use an ActionFactory
    * DONE [TEST] UserActionFactory should implement createTakeSpecificItemAction()
    * DONE [TEST] UseWith should use an ActionFactory
    * DONE [TEST] Remove UseWith constructor that does not use an ActionFactory
    * DONE [TEST] UserActionFactory should implement createUseWithSpecificItemAction()
    * [TEST] Actions that create actions need an ActionFactory reference:
      * DONE ShowInventory creates InventoryItem actions
      * DONE InventoryItem creates Examine and UseWith actions
      * DONE ExamineAnItem creates Examine actions
      * DONE Location creates ExamineAnItem actions
      * DONE Location creates TakeAnItem actions
      * DONE TakeAnItem creates TakeSpecificItem actions
      * DONE UseWith creates UseWithSpecificItem actions
  * DONE An ItemDecorator and ModelDecorator can be passed to the ItemActionFactory which uses it to wrap Item and Model in RecordableItem and RecordableModel objects before passing to the ItemActions it creates.
    * DONE [TEST] ItemDecorator is used by ItemActionFactory
    * DONE [TEST] ModelDecorator is used by ItemActionFactory
  * DONE On create ActionHistory is created and used to store the actions that occur.
  * DONE On create RecordableItemDecorator is created with an ActionHistory
  * DONE On create RecordableItemDecorator is passed to the ItemActionFactory in createNewGameModel()
  * DONE [TEST] Implement RecordableItemDecorator, wraps item in new RecordableItem object
  * A RecordableItem implements Item interface.
    * DONE [TEST] It delegates all calls to the target Item.
    * DONE [TEST] It records 'use' and 'examine' actions on the Item in the ActionHistory.
  * On create RecordableModelDecorator is created with an ActionHistory
  * On create RecordableModelDecorator is passed to the ItemActionFactory in createNewGameModel()
  * [TEST] Implement RecordableModelDecorator, wraps model in new RecordableModel created with ActionHistory object
  * The RecordableModel, Location, Exit and UserInventory work like RecordableItem. RecordableModel also will return Locations wrapped in a RecordableLocation object after retrieving from the real Model. RecordableLocation likewise does the same for Exits.
  * The ItemDecorator and ModelDecorator can be passed to the ActionFactory to wrap Item and Model in RecordableItem and RecordableModel objects before passing to the Actions it creates.
  * On create RecordableItemDecorator is passed to the ActionFactory in both createNewGameModel() and setupPresenter()
  * On create RecordableModelDecorator is passed to the ActionFactory in both createNewGameModel() and setupPresenter()
  * Implement ActionHistory
  * On pause the ActionHistorySerialiser is used to serialise the ActionHistory and the result is written to a file
  * On resume the base model is loaded and the ActionHistoryDeserialiser is used to load the ActionHistory. An ActionReplayer is used to re-run the ActionHistory on the Model.
    * How to handle replay of Item.use()/examine() which enact all the registered ItemActions? (therefore causing name, description changes etc... which the ActionReplayed will already be doing itself...) - could just call 'setUsedFlag()' to set the fact that it has been used without running the actions
  * For backwards compatibility with JSON save file from version 1.0 a ModelMerger is needed - load the JSON model, load the base model and merge the JSON one into the base model.
    * Implement as n hard coded rules to translate from 1.0 to 2.0 models:
      * If the bagsofjunk have been examined run Examine on them
      * If the moundofearth has been used run UseWith on the spade and moundofearth and _also_ add the spade to the inventory
      * If clockface has been used then run UseWith on the clockface and the clockmechanism
  * Remove PlainTextModelSerialiser created for rejected save proposal
  * Acceptance tests for pause/resume action save and replay?
  * Acceptance tests for backwards compatibility with JSON for v1.0 being loaded and transformed for v2.0?

- [BUG] User still has map after giving it to the shopkeeper!
- [FEATURE] Exits should have consistent colours for direction hints - e.g. all North should be green
- [FEATURE] Tell the user when they have reached the end of the available content! Perhaps a popup saying look out for new content in the next update?
- [FEATURE] Tablet improvements - make work in landscape, use bigger font depending on screen size
- [FEATURE] Let user change the font size on an options screen
- [FEATURE] Record the number of moves taken so far, present as a form of score on the about dialog or a stats page?

- [STORY] Shopkeeper directs you to investigate strange goings on in the graveyard, gives you some item to use later
- [STORY] On entering teh grave yard there is a mysterious presence, when you examine it it moves to a different location. If you keep following/examining it it leads you to a particular grave where an encoded inscription is writte on the headstone. Use the thing from the shopkeeper to read the inscription which directs yuo inside the church to the back of the altar.
- [STORY] On examining the '8th' candlestick on the altar a trapdoor opens. Going below you are blocked by a ravenous wolf guarding a door. You must use an old leg bone from the graveyard to placate it. An exit is then accesible behind the wolf.
- [STORY] Passing the wolf there is a complex of rooms, there are voices and locked doors. One room deep within has a brigand blocking your way, you must fight and defeat him to continue.

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
