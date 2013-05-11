Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Don't forget to drive the development by producing value - the value is game content and this should drive the development of functionality to support it.

To run the emulator - run 'android list avd' to get all the configured virtual devices, then 'emulator @<device-name>' to run the chosen device in the emulator.

Backlog
=======

* [FEATURE] Save file new format
  * DONE Turn off JSON save format file writing - this allows us to make the Presenter be a Model observer and have a cyclic dependency (only cyclic in real objects, the interface keep it all seperate). This cycle causes JSON-IO to crash on saving.
  * DONE Remove Recordable[Item|Model] and Recordable[Item|Model]Decorator, ItemDecorator, ModelDecorator, NullModelDecorator, changes in Activity and all related tests
  * DONE Remove PlainTextModelSerialiser created for rejected save proposal
  * DONE View/Presenter moveThroughExit interface should use 'Exit' actions instead of Exit objects directly and should use a common 'UserActionHandler::handleAction' interface
    * DONE [TEST] Activity should call userActionHandler.enact() with an ExitAction retrieved from the ActionFactory
    * DONE [TEST] UserActionFactory needs to create Exit actions
    * DONE [TEST] ExitAction must call model.moveThroughExit( exit )
    * DONE [TEST] BasicModel must publish when it changes location
    * DONE [TEST] Presenter must subscribe for model events
    * DONE [TEST] Presenter on location change in model clears actionText and calls render()
    * DONE [TEST] Remove Presenter.moveThroughExit interface
  * DONE In activity RecordableActionFactory wraps the UserActionFactory in createNewGameModel(), setupPresenter() and addExitActionHandler() and passes an ActionHistory to it
  * DONE RecordableActionFactory delegates all public methods to UserActionFactory
  * DONE RecordableActionFactory sets itself on the wrapped factory as the ActionFactory to pass to child Actions so they can create Actions.
  * DONE UserActionFactory passes the set ActionFactory for other Actions to use to create new Actions to the Actions it creates.
  * DONE RecordableActionFactory wraps returned Action objects from UserActionFactory in RecordableAction.
  * DONE RecordableActionFactory passes ActionHistory to RecordableAction objects on construction.
  * DONE RecordableActionFactory also set relevant Items and Exits on RecordableAction
  * DONE RecordableAction delegates all public methods to wrapped Action
  * DONE RecordableAction on trigger records action and associated Items and Exits in ActionHistory
  * DONE Add ActionParameters to RecordableAction and RecordableActionFactory to hide details.
  * DONE Move ActionHistory and BasicHistory to action namespace.
  * DONE Implement BasicActionHistory
  * DONE Implement ActionHistorySerialiser
  * All Exits need IDs in the model - make it mandatory for deserialisation
  * All Locations need IDs in the model - make it mandatory for deserialisation
  * All Items need IDs in the model - make it mandatory for deserialisation
  * On pause the ActionHistorySerialiser is used to serialise the ActionHistory and the result is written to a file
  * Implement ActionHistoryDeserialiser
  * On resume the base model is loaded and the ActionHistoryDeserialiser is used to load the ActionHistory. An ActionReplayer is used to re-run the ActionHistory on the Model.
  * Acceptance tests for pause/resume action save and replay?
  * Merge recordable_actions branch to main line
  * For backwards compatibility with JSON save file from version 1.0 a ModelMerger is needed - load the JSON model, load the base model and merge the JSON one into the base model.
    * Implement as n hard coded rules to translate from 1.0 to 2.0 models:
      * If the bagsofjunk have been examined run Examine on them
      * If the moundofearth has been used run UseWith on the spade and moundofearth and _also_ add the spade to the inventory
      * If clockface has been used then run UseWith on the clockface and the clockmechanism
    * Delete old JSON based save file
  * Acceptance tests for backwards compatibility with JSON for v1.0 being loaded and transformed for v2.0?
  * Defensive loading... if an old JSON file doesn't load capture the exception and recover gracefully, start a clean new game

- [BUG] User still has map after giving it to the shopkeeper!
- [BUG] Double blank line after item actions - clarify need for '\n\n' in Presenter.enact()
- [FEATURE] Exits should have consistent colours for direction hints - e.g. all North should be green
- [FEATURE] Tell the user when they have reached the end of the available content! Perhaps a popup saying look out for new content in the next update?
- [FEATURE] Tablet improvements - make work in landscape, use bigger font depending on screen size
- [FEATURE] Let user change the font size on an options screen
- [FEATURE] Record the number of moves taken so far, present as a form of score on the about dialog or a stats page?

- [STORY] Shopkeeper directs you to investigate strange goings on in the graveyard, gives you some item to use later
- [STORY] On entering teh grave yard there is a mysterious presence, when you examine it it moves to a different location. If you keep following/examining it it leads you to a particular grave where an encoded inscription is writte on the headstone. Use the thing from the shopkeeper to read the inscription which directs yuo inside the church to the back of the altar.
- [STORY] On examining the '8th' candlestick on the altar a trapdoor opens. Going below you are blocked by a ravenous wolf guarding a door. You must use an old leg bone from the graveyard to placate it. An exit is then accesible behind the wolf.
- [STORY] Passing the wolf there is a complex of rooms, there are voices and locked doors. One room deep within has a brigand blocking your way, you must bribe him to continue. This introduces a custom failed use message for all items used on the brigand e.g. "He looks at you and sneers in disgust.". Also allow for multiple items to be used with different consequences - e.g. if you use an empty purse you get the succes message 'The brigand throws the empty purse at you hitting you square on the nose. This smarts a little.'. To pass the brigand an empty purse (given to you by the shopkeeper earlier) must be filled with fake coins, perhaps find some, make some or use stones?
- [STORY] Once you pass the brigand you see a few more rooms one of which contains a locked safe. You must find teh 4 digit code to unlock it. The digits can be found in the surrounding rooms, on a piece of burnt paper, scrawled on a desk/floor, somewhere else and finally you must speak to an NPC. This introduces a new action 'Talk' which needs to have a conversation tree associated with it, choose the right dialogue and the NPC spills the beans (final digit)! Go to the safe and use it (probably a new action too 'Use' rather than UseWith). A keyboard pops up and you must enter the correct digits to open the safe and see what is inside.

- [REFACTOR] LocationTests and TakeSpecificItemTests are using real NormalItems - change to use mocks
- [FEATURE] Actions buttons view should be fixed size and scrollable - 1/4 to 1/3 of the screen height
- [FEATURE] Ruby counter should show how many out of how many achieved - e.g. 10/100 - this needs to be a value specified in the model content text file. It is currently hard coded in BasicModel.java.

- [FEATURE-EDITOR] Update webapp editor to [de]serialise visibility and allow setting of visibility
- [FEATURE-EDITOR] Update webapp editor to allow adding/deletion of on examine actions to items

- [REFACTOR] Item interface is getting fat. Can 'use' and 'examine' be split off, perhaps Examinable, Usable? Is there any benefit?

- [FEATURE] In model content txt items can only be used in one direction, e.g. a spade with a mound of earth and the target item has to include the definition of what happens. If the original item (e.g. spade) includes this information it is ignored. Should this be changed to be bi-directional? It would make writing content easier but needs considering as it might not always be appropriate.


- Add monsters, npcs

- Add other verbs - attack, eat, drink, dance, take, drop, give, talk

- Optional images to go with text?

Done
====

2.0
---

* [TECHNICAL FEATURE] An ActionFactory is needed for user actions like Examine, TakeAnItem, ShowInventory. This needs to be passed to the Presenter and the other Actions that create actions.

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
