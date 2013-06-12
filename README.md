Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Don't forget to drive the development by producing value - the value is game content and this should drive the development of functionality to support it.

To run the emulator - run 'android list avd' to get all the configured virtual devices, then 'emulator @<device-name>' to run the chosen device in the emulator.

Backlog
=======

- [BUG] From email from Alex Bailey "The text is sometime displayed in the wrong order. I can't remember which object, but the scene description told me there was an object present before it was mentioned how it got there. I can send a screenshot for this if I'm not being clear."
  * See email as he also included a screen shot.
- [FEATURE] Tell the user when they get a ruby - make it much more obvious. Atm just mentioned in text and counter in corner goes up.
  * Make counter and ruby graphic larger
  * Add sound effect or screen flash or dialog
- [FEATURE] Tell the user when they have reached the end of the available content! Perhaps a popup saying look out for new content in the next update?
- [TECHNICAL FEATURE] Incremental saving - save the game every 10 actions or something, in case of a crash this avoids the user losing too much progress

- [STORY] Shopkeeper directs you to town hall notice board where reports of strange goings on are being posted. This triggers the entrance to the town hall to become visible.
  - [FEATURE] Talk action responses need to be defined for the shopkeeper.
    - DONE Write acceptance tests
    - DONE Location has a TalkToAction for each item that is canTalkTo() == true
    - TalkToAction
      - DONE A TalkToAction has an item. It's label is 'Talk to ' + item.midSentenceCasedName()
      - DONE A TalkToAction has a follow up action of SayAction, constructed with Item.getTalkable(), for each initial phrase id returned by talkable.initialPhraseIds()
    - SayAction
      - A SayAction has the label 'Say "' + talkable.phraseById( id ) + '"'
      - A SayAction when enacted has user text of talkable.responseToPhraseById( id )
      - A SayAction when enacted has a follow up SayAction for each id returned by talkable.followOnPhrasesForPhraseById( id )
      - A SayAction when enacted calls takeable.executeActionsForPhraseById( id )
    - ActionFactory can create TalkToActions
    - ActionFactory can create SayActions
    - RecordableActionFactory can create TalkToActions
    - RecordableActionFactory can create SayActions
    - Item
      - Item has a getTalkable() method (could return itself or a seperate implementation of the interface)
      - Item.canTalkTo()
    - Talkable
      - phraseById()
      - responseToPhraseById()
      - followOnPhrasesForPhraseById()
      - executeActionsForPhraseById()
    - Deserialise talk related specs from model content into Item.setTalkable()
    - Serialise TalkToAction and SayAction for ActionHistory
    - Correct acceptance tests based on new design

    - [TEST] If an item has talk responses then include a 'Talk' action in Location::actions()
    - [TEST] Talk action allows choice of n phrases
    - [TEST] Choosing a phrase triggers a response as specified in model content
    - [TEST] Choosing a phrase can optionally trigger an alternative set of phrases to become available as follow up actions
    - [TEST] Choosing a phrase can optionally trigger actions (use the same actions as 'item use action:' directive). E.g. make an exit visible
    - Run acceptance tests
- [STORY] At the town hall notice board there are multiple notices (act as reminders of what the player has left to do, small board, medium board, azure board, each with a clue to a different story line). The notices appear as the player completes other prerequisite tasks. There is also a hall of deeds in the town hall where there are notices that state what the player has done, e.g. rescued the shop keeper - it's an achievements record.
- [STORY] On entering the grave yard there is a mysterious presence, when you examine it it moves to a different location. If you keep following/examining it it leads you to a particular grave where an encoded inscription is writte on the headstone. Use the thing from the shopkeeper to read the inscription which directs yuo inside the church to the back of the altar.
- [STORY] On examining the '8th' candlestick on the altar a trapdoor opens. Going below you are blocked by a ravenous wolf guarding a door. You must use an old leg bone from the graveyard to placate it. An exit is then accesible behind the wolf.
- [STORY] Passing the wolf there is a complex of rooms, there are voices and locked doors. One room deep within has a brigand blocking your way, you must bribe him to continue. This introduces a custom failed use message for all items used on the brigand e.g. "He looks at you and sneers in disgust.". Also allow for multiple items to be used with different consequences - e.g. if you use an empty purse you get the succes message 'The brigand throws the empty purse at you hitting you square on the nose. This smarts a little.'. To pass the brigand an empty purse (given to you by the shopkeeper earlier) must be filled with fake coins, perhaps find some, make some or use stones?
- [STORY] Once you pass the brigand you see a few more rooms one of which contains a locked safe. You must find teh 4 digit code to unlock it. The digits can be found in the surrounding rooms, on a piece of burnt paper, scrawled on a desk/floor, somewhere else and finally you must speak to an NPC. This introduces a new action 'Talk' which needs to have a conversation tree associated with it, choose the right dialogue and the NPC spills the beans (final digit)! Go to the safe and use it (probably a new action too 'Use' rather than UseWith). A keyboard pops up and you must enter the correct digits to open the safe and see what is inside.

- [FEATURE] Tablet improvements - make work in landscape, use bigger font depending on screen size
- [FEATURE] Let user change the font size on an options screen
- [FEATURE] Record the number of moves taken so far, present as a form of score on the about dialog or a stats page?
  * Use the new Google games API to record this as a high score?

- [REFACTOR] LocationTests and TakeSpecificItemTests are using real NormalItems - change to use mocks
- [TECHNICAL FEATURE] Ruby counter should show how many out of how many achieved - e.g. 10/100 - this needs to be a value specified in the model content text file. It is currently hard coded in BasicModel.java.

- [FEATURE-EDITOR] Update webapp editor to [de]serialise visibility and allow setting of visibility
- [FEATURE-EDITOR] Update webapp editor to allow adding/deletion of on examine actions to items

- [REFACTOR] Item interface is getting fat. Can 'use' and 'examine' be split off, perhaps Examinable, Usable? Is there any benefit?

- [FEATURE] In model content txt items can only be used in one direction, e.g. a spade with a mound of earth and the target item has to include the definition of what happens. If the original item (e.g. spade) includes this information it is ignored. Should this be changed to be bi-directional? It would make writing content easier but needs considering as it might not always be appropriate.

- [IDEA] Add monsters, npcs
- [IDEA] Add other verbs - attack, eat, drink, dance, take, drop, give, talk
- [IDEA] Optional images to go with text?

Done
====

2.0
---

- [FEATURE] Exits have consistent colours for direction hints - e.g. all North are green.
  - The exits are also ordered consistently.

- [BUG] Clicking exit links can sometimes select the wrong one if you select near the end of a line - it selects the exit on the new line.
  - [FIX] A spannable click region will extend to the edge of the text view or the next non-spanning text or spanned region. Fixed by adding a single white space at the end of the list of exits to stop the spannable click region.
- [BUG] Double blank line after item actions - clarify need for '\n\n' in Presenter.enact()
  - [FIX] Lots of text presentation newline issues fixed across presenter, activity and change location description item action.
- [BUG] Entire inventory is not visible if everything is picked up in the game.
  - [FIX] If actions list is larger than available space it now becomes scrollable.
- [BUG] User still has map after giving it to the shopkeeper!
  - [FIX] Map is now destroyed once given to the shopkeeper.

* [TECHNICAL FEATURE] Save file new format
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
  * DONE On pause the ActionHistorySerialiser is used to serialise the ActionHistory and the result is written to a file
  * DONE Implement ActionHistoryDeserialiser
    * DONE Return a list of actions
    * DONE Creates appropriate action object types from factory based on name
    * DONE Retrieves Exit mentioned by id from a model
    * DONE Retrieves Items mentioned by id from a model
  * DONE RecordableActionFactory needs to add ModelLocations by id to actions parameters.
  * DONE ActionHistorySerialiser needs to record ModelLocations by id for actions that use them.
  * DONE Model needs a findLocationByID() method.
  * DONE ActionHistoryDeserialser needs to find ModelLocations by id from the model for those actions that need them.
  * DONE On resume the ActionHistory is cleared by the activity
  * DONE On resume the activity is used to re-run the list of actions from the deserialiser from the action history save file
  * DONE [BUG] Clock minute hand and skeleton key appear twice in the inventory after load - why?
    * Fix was to read in the save file properly - was using a 1024 buffer so getting a lot of left over rubbish.
  * DONE On resume and re-run of saved actions activity needs to pause responding to view updates, pauseViewUpdates(), continueViewUpdates()
  * DONE Acceptance test for resume action save and replay
  * DONE Acceptance test for pause action save
  * DONE Merge recordable_actions branch to main line
  * DONE Acceptance tests for backwards compatibility with JSON for v1.0 being loaded and transformed for v2.0
  * For backwards compatibility with JSON save file from version 1.0 a ModelMerger is needed - load the JSON model, load the base model and merge the JSON one into the base model.
    * DONE Implement BasicModelV1_0ToActionListConverter
      * Implement as n hard coded rules to translate from 1.0 to 2.0 models:
        * If the bagsofjunk have been examined run Examine on them
        * If the moundofearth has been used run UseWith on the spade and moundofearth and _also_ add the spade to the inventory
        * If clockface has been used then run UseWith on the clockface and the clockmechanism
    * DONE Implement JSONToActionListConverter::actions()
    * DONE Set the current location in the new model to match the old JSON saved location
    * Issues after converting a JSON model to an action list:
      * DONE Make sure the main text is re-presented to the user cleanly - no remanants of all the actions ocurring during load, just a fresh location description
      * DONE Locked door is not unlocked
        * FIXED: Some items in the old JSON save format may not have IDs, patch up the old model after loading with the missing IDs. Missing ids are pocket lint, banana peel, bags of junk, locked door, dust of the ancients.
      * DONE generateClockHourHandLifetimeActions() must include clock face lifetime actions
      * DONE generateClockMinuteHandLifetimeActions() must include clock hour hand lifetime actions
      * DONE When checking for item changes visibility was not taken into account, therefore all conditions were always positive. Fixed to take visibility into account.
    * DONE Check acceptance tests work
    * DONE Add more acceptance tests - a medium length and a full story one
    * DONE Delete old JSON based save file in onPause if it still exists
    * DONE Upon loading converted action history - the current location is the original start location because there are no 'exit' actions to make the player move.
      * Leave the user at the start location after converting the JSON (instead of setting to the last location)?
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
