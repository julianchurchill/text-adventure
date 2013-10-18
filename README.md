Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Don't forget to drive the development by producing value - the value is game content and this should drive the development of functionality to support it.

Building
========

The easiest way to build is to choose which app you want to build then run './config/watchAndBuildApp[12].sh'. This will configure and build the app and wait for changes to the source, triggering a rebuild when a change occurs.

For more control of the build process first run './config/prepare.sh [12]' to copy the correct resource files, source files and AndroidManifest.xml for the app you want to build. Then run 'ant debug' or 'ant release' to build everything, run all the unit tests and acceptance tests and create a deployable APK file. Once built the APK can be found at bin/main/TextAdventure-[debug|release].apk.

Emulator
========

To run the emulator - run 'android list avd' to get all the configured virtual devices, then 'emulator @<device-name>' to run the chosen device in the emulator.

Save files are stored on the emulated device under /data/data/com.chewielouie.textadventure/files. Run 'adb shell' to get access to the file system.

Backlog
=======

Technical Tasks

- Reduce the height of the buttons to better fit more in without squishing the text view down too much
- Add cucumber test run as part of build, see https://github.com/cucumber/cucumber-jvm and config/common/test/CucumberStepDefs.java and src/test/resources/cucumber/TTA2/TTA2_entering_the_town.feature
- Rename git tags to prefix with TTA1_
- Reword 'item use action' to 'item on use action' so it reads like the 'item on examine action' usage.
- Update model for app 1 to use take item action where appropriate - make sure this will be backwards compatible with current behaviour - save files will have 'pick up' actions for items that will now be automatically picked up. So the original extra 'pick up' must be harmless and just fail silently since the user already has the item in their inventory.
- Analytics
  - https://developers.google.com/analytics/devguides/collection/android/v3/
  - Things to capture
    - Active time (between onResume() and onPause()) from start to finish of game/average completion time
    - Total active time spent playing
    - Average active time period
    - Load time
    - Save time
    - Count how many times new game is started
    - Count how many times game is completed (to find percent of started games that are completed)
    - Count how many actions are made to complete game
    - Count how many times options dialog is opened
    - Count how many times map is opened
- Save/restore checkpoints - on branch 'checkpoints'
  - Save dumps action history to a file action_history_checkpoint_[n]
    - Toast to show checkpoint has been saved (and which number...)
  - Restore loads latest numbered action_history_checkpoint_[n] file and then deletes it
    - Not sure deleting is a good idea... what if you the progress and want to return to the same checkpoint, you can't because it's been deleted!
    - Toast to show checkpoint has been restored (and which number...)
  - Add menu items, restore is disabled if no checkpoint files available
- Is there a better way to help give the user better directional context? e.g. a compass, each exit on a newline

Marketing

- Add keywords and description to Google Play description to include - "Story, game, puzzle"
- Forums and newsgroups: intfiction.org, rec.games.int-fiction, rec.arts.int-fiction
  - TTA1 release posted on intfiction.org
- Game indexes and review sites: ifdb.tads.org, textadventures.co.uk, www.androidtapp.com
  - TTA1 androidtapp review request submitted
  - TTA1 added to ifdb - http://ifdb.tads.org/viewgame?id=6bsa13ogybwiucr0
- Add a Google+ page for the app, add to About box and play store/amazon store listings
  - Added Google+ page for Tiny Text Adventure https://plus.google.com/u/0/115456188877848985242

Bugs

- [BUG] Clickable area for exit seems to extend down the text view underneath the link - is the span limited correctly?
  - FIX Other possible fix is to pad the textview with a single line of blank text after the rest of the text is in place. Same idea as we already do with adding a single " " to stop the clickable span extending to the end of the line.
    - This fix works ok on 2.3.3 (emulator) but not on 4.3 (my nexus 4).
- [BUG] Use 'an' instead of 'a' in front of nouns that start with a vowel.
  - New item action - change countable noun prefix e.g. changing locked door to 'unlocked door' must also change the countable noun prefix

Story - TTA2

- The barricade attack
  - Giving the bone to Cabal triggers night fall and the creatures attack, you must help defend the barricade through the night and at least one fell beast ambushes you when you enter another location. You defeat it using a weapon given to you by a townsperson.
  - Then you must talk to the people manning the barricade to glean that they expect a fire attack to be imminent and are calling out for the 'fire shield'. You must repair it (there is a wheel missing) and push it from the town hall (which it had been protecting) to the barricade, upon which the people shelter with you behind it and are protected from being engulfed by flames. You see a glimpse through the fire of a metal man a small distance from the other side of the barricade seemingly directing the beasts where to attack and controlling a dragon.
  - Finally when you leave the location to check the rest of the town defences (suggested by talking again if you don't do it anyway) you get attacked by one more fell beast, defeat it and the attack ends, day dawns.
- In the morning the towns people ask you to get help from a nearby village over the river - Ubiquity.
- A river and bridge between the two settlements
  - Use a boat to get across initially, perhaps repair the bridge from the other side to return to the town
- Gibbling Boon is a good name for a creature.
- Fastidious mim - a bird
- Indivisible turpet. "You should have known, the fastidious mim always comes with it's companion creature - the indivisible turpet. The turpet is a big, fat worm, also known as Crane's turpet after the famous botanist, who met his maker shortly after discovering and naming the turpet and then while testing it's indivisibility, he sucumbed to blood loss from an accidentally amputated toe. Later botanists determined that this unexpectedly rapid exsanguination could be attributed to the same chemical which allowed the turpet to reconstitute itself after being chopped up. Upon the first chopping it released a sufficient amount into the air to trigger temporary haemophilia and the combination of the chemical and Crane's unwavering experimental determination sealed his fate."
- Underwater adventure of somesort - perhaps in the river. Meet water faeries
- Babbages cabbage - a cabbage power steam computer, perhaps interact by placing runes
- Magical robots - perhaps trapped water imps - agents for either side, initially created by the witch to serve her, wills too strong for her to control, some changed sides. Self naming, Least Chance of Failure, Random Acts of Kindness, Vengeful Beyond Death, Penchant for Foolish Bravery
- You reach Ubiquity only to discover it has been subdued by Bella to serve her - the future of Perpetuity...
- An ancient temple to old gods with traps and puzzles, entrance puzzle run, a la Indiana Jones:
  - First room, broken bridge, examine stalactites to reveal bits you could hook a rope on, then use whip with them to swing over broken bridge
  - Second room, stone monkey on pedestal with gems for eyes, out of reach and facing you. Skull chandelier above tied in place next to you. Use knife with chandelier rope to drop it, chandelier still out of reach, use whip to swing it into monkey which knocks it so that light from hole in the roof is directed through it's eyes into a gem in the wall and a hidden door opens.
  - Third room, south indian monkey trap, locked door, key at the bottom of a pit an arms length deep, wider at the bottom than the top. The key is encased in clear wax. You can put your arm down the pit and grab the key ball but cannot get it out of the pit. You must drop a torch down there to melt the wax and then get the key out. Objects: key in wax, pit, torch. Use torch with pit, take key.
  - Fourth room
  - Fifth room
- A portal to a dark plane (deep inside the temple) with demon creatures you must defeat some how
- Bellas HQ - a castle in a forest on the other side of the river.
  - Final battle should be in several locations, you must beat the witch in one room in a certain way, then she flees and you follow. Repeat 3 times, different rooms and different ways to beat her.
  - Perhaps you can fail to beat the witch in a room if you do the wrong thing. Consequence is a reset of the combat sequence to the first room.
- Sub quests
  - Create and play a kazoo at a childrens party at a nobles house (achievement Player of the Kazoo)
  - Collect bugs - using a bug collector to make them visible and catch them. There are 12 to find and there are clues at the locations to indicate that there is a bug there. Something like "There is a flicker of movement in the corner." (Bug Collector)
- Add some negative use responses
  - DONE Using Imperator with chainsaw before it's fixed - "It rattles and tattles and chokes to a halt. The belt ran without anything to do the slicing, these chainsaw needs teeth!"
  - DONE Using stick with rag - "What are you going to do, beat the rag into submission?"
  - DONE Using rag with paraffin - "You'll get covered in paraffin if you do that which won't be very attractive to the ladies. If you were concerned with that sort of thing."
  - DONE Using rag with glowing embers - "The rag won't light, perhaps it needs an accelerant."
  - DONE Pocket lint and glowing embers - "There's not really enough of it to start a fire."
  - DONE Stick and glowing embers - "You can try to beat the embers out but the damage has already been done."
  - DONE Stick and the fell beast - "Really? Do you want to fight the beast or play with it?"
- Add embedded images at suitable places with <img src=""/> tags
  - DONE Spiky-toothed goober (large vicious fish)
  - DONE Portcullis
  - DONE Barrelling goon (big fur ball, shooting about like pinball)
  - Fell beast (hairless dog with fiery eyes)
  - Mushrooms
  - Barrelling goon (big fur ball, shooting about like pinball)

Features

- [FEATURE] Accessibility - Text to speech
  - DONE Add a check box for TTS to the options dialog, save as a preference, default is off
  - Why does a 'Starting game' dialog appear and the save file get reloaded when turning TTS _on_ ?
  - Only play new text, e.g. examine an object, play the description but do not play the location description again
    - Should the item list and exit list be replayed?
- [FEATURE] Accessibility - Voice recognition
- [FEATURE] Map - extras
    - Make better masks and a better map! Charlies on the case.
- [UI] Parchment background extras
  - Scroll bar images need to compliment the parchment background...
- [UI] New icon for TTA2
- [UI] Images for inventory items to appear on buttons next to label
- [UI] Images for actions to appear on buttons next to label
- [UI] Restrict action button list to half screen height
- [FEATURE] Story log - a record of all the story text you've seen so far - i.e. the 'text on first entry to location'. Possibly also the conversation text? How much of this to store, room descriptions, item activations?
- [FEATURE] Google games API Achievements
  - Chatterbox (talk to everyone about everything)
  - Converser (talked to everyone)
  - Champion (complete the game)
  - Perfect (complete game in minimal moves) - questionable worth and hard to calculate
  - Explorer (has gone to every location)
  - Pathfinder (has gone through every exit)
  - Player of the Kazoo (completed a sub quest to create and play a kazoo at a childrens party)
- [FEATURE] Sound effects
  - Door unlocking, clock tower ticking/bells, ghostly sounds in the graveyard, mice scratching at the church, market hustle and bustle.
  - Make on/off-able in options
- [FEATURE] What's new dialog for first run of new version.
- [FEATURE] Google games API Leaderboard - Record the number of moves taken so far, present as a form of score on the about dialog or a stats page?

- [TECHNICAL FEATURE] Incremental saving - save the game every 10 actions or something, in case of a crash this avoids the user losing too much progress
- [FEATURE] Tablet improvements - make work in landscape, use bigger font depending on screen size

- [FEATURE] In model content txt items can only be used in one direction, e.g. a spade with a mound of earth and the target item has to include the definition of what happens. If the original item (e.g. spade) includes this information it is ignored. Should this be changed to be bi-directional? It would make writing content easier but needs considering as it might not always be appropriate.

- [IDEA] Add other verbs - attack, eat, drink, dance, drop, give
- [IDEA] Optional images to go with text?

Done
====

Note: Multiple version numbers are used where changes are common to multiple apps

Marketing

- Google Play Store
  - Listed
- Amazon App Store
  - Listed
- SlideME app store http://slideme.org/developers
  - Listed
- Facebook, Twitter, Google+
  - Release 1.0 and 2.0 advertised on all of the above

TTA1 v2.2.x/TTA2 v1.0.x
---

- [FEATURE] One shot contextual text can be displayed on entering a room and is then proceeded by the normal room description.
- [FEATURE] Map - shows where you've been
- [FEATURE] Added location area to title bar
- [FEATURE] Smarter resume - all the text from the actions that have taken place in the current location is shown upon loading. Allows players to regain context much more easily - e.g. if they were in the middle of a conversation or half way through a puzzle.
- [FEATURE] Enabled 'move to sd card'
- [UI] Parchment background (from http://www.myfreetextures.com/worn-parchment-paper-2)
  - Added higher res background parchment image
- [UI] Full width action buttons
- [UI] HTML can be embedded in text descriptions and action messages. Not all HTML tags are supported. Supported HTML tags by this method on android can be found here: http://commonsware.com/blog/Android/2010/05/26/html-tags-supported-by-textview.html
  - HTML tag support in any description or text resultant from an action
  - Images are automatically centered, but must use wrap the img tag in <div align="center"></div> otherwise image will not be surrounded with line breaks and therefore will have nothing to be centred within. The align="center" property is redundant as it doesn't work but may be useful for future extensions so please include it.
- [FEATURE] Text to speech
  - Added a check box for TTS to the options dialog, saves as a preference, default is off
- [UI] Action buttons have a complimentary style to the text background
- [UI] Removed title bar to give more space for content

- [BUG] Double new lines appear before and after talk phrases are printed
  - [FIX] SayAction was adding unnecessary new lines.
- [BUG] Order of exits can be confusing - reordered to this {North, West, East, South}
- [BUG] Auto scroll to the top of the unread text is too absolute - it doesn't take into account line spacing - it could do with adding a few pixels to the top (e.g. the margin/padding/line spacing divided by 2)
  - [FIX] Add a few pixels to the top of the auto scroll
- [BUG] Invisible items appeared in the 'use with' list - e.g. cider bottle after giving it to fisherman

- [TECHNICAL TASK] Seperation of content from framework so we can have one Android app + business logic and swap in different content for different builds
  - Mask images should be auto found by looking for resources starting with area-id. E.g. the mask filename for the town-area should be town-area_mask.png
  - Build system should set up Android resources and manifest for the correct app (TTA1 or TTA2). Copy files from somewhere else in the git repo. Don't save the manifest or res content to git anymore. Update README with instructions on configure before build.
- [TECHNICAL TASK] Ensure two different apps (need two unique Java package names in manifest and source for activity) can co-exist
  - Add chewielouie.textadventure_common namespace for all common code between app1 and 2 activities.
  - Leave a skeleton activity in chewielouie.textadventure and chewielouie.textadventure2 which inherit from TextAdventureCommonActivity which has abstract methods for getting the 'R' class ids
  - Add appropriate build files for config/1 and config/2 so that only textadventureN and textadventure_common are included in the build. Worst case copy the full namespace + Activity from the config/1/2 areas and keep them in source control there instead of under src/.
  - Move Acceptance tests and activity tests so they use the Common activity not a specific one
    - Activity acceptance tests need to have data built in - not taken from loaded model_content.txt. The model content needs to be supplied to the activity when the test creates it. Without this the acceptance tests will never pass for textadventure2.
- [TECHNICAL TASK] New item action - change exit visibility
- [TECHNICAL TASK] To avoid screen jumping BasicModel caches location description until location changes.
- [TECHNICAL TASK] Removed 'make exit visible' and swap use of it with 'change exit visibility'
- [TECHNICAL TASK] is/are choice for list of available items. E.g. when entering the burnt out shop the list is "There is some smouldering embers here." - more correctly it should "are some". Applied to 'Bags of junk' in TTA1 and 'Smouldering embers' in TTA2
  - Items can now be marked as plural 'item is plural:' - causes 'There are' to be used instead of 'There is'.
- [TECHNICAL TASK] New item action - take item. This stops the model content having to leave items on the floor that the character would usually have picked up without an action.
- [TECHNICAL TASK] New event trigger 'exit on use action', e.g. 'exit on use action:destroy item:fiery_torch' for example in TTA2 where you escape into the store cupboard but drop the torch.
- [TECHNICAL TASK] Max score needs to be a value specified in the model content text file. It is currently hard coded in BasicModel.java.
- [TECHNICAL TASK] Proper nouns - e.g. Oubliette. Added 'item is proper noun:' tag for item in model_content so that on examination the 'the' is dropped - e.g. 'You examine Oubliette.'.
- [TECHNICAL TASK] Multiple _can be used with_ clauses, e.g. ImpSaw2000 (broken) and imperator _and_ spiky-toothed goober teeth
- [TECHINCAL TASK] Added 'You use [the] x with [the] y.' before outputting the result.
- [TECHINCAL TASK] Turned on proguard for release APK generation - http://developer.android.com/tools/help/proguard.html
- [TECHNICAL TASK] Made the item and exit list italic to distinguish them as state rather than story

TTA2 only
- [STORY] Enter town, find a barricade on main street, go to merchants lane, general store door is broken but has a hole in, when you examine it you get your arm stuck. There are no exits and some items around you.
  - To escape you must examine one of the items you can reach, which breaks into two and then examine another part - after which a voice says "Psst... what are you doing down there?". Then you can talk to the 'disembodied voice', who eventually frees you in exchange for your multitool, leaving you outside the shop with the door open. The owner of the disembodied voice is nowhere to be found. Increment score.
- [STORY] Entering the general store you encounter a dangerous animal, looks like a rabid dog but has no fur and deep red eyes. It eyes you hungrily. Past the animal is the back of the shop through which you can see a window that should lead you to the other side of the barricade. You stay in the daylight framing the doorway as the fell beast seems to be wary of the light and will not come closer to you while you stand there.
  - You must assemble a torch from items in nearby locations, you'll need a stick, a rag, dip it in a jar of clear liquid in the general store (is actually paraffin, if you examine you smell it). Light the torch from glowing embers in the shop next door which is just a burnt out shell.
  - Use with the 'fell beast' allowing you to see the exit at the back of the shop. After entering the cupboard the torch is destroyed, score is incremented and you see there is a one-way exit through the window, leading to the cobbled street north of the barricade.
- [STORY] Repair the barricade
  - When you first the barricade you meet some towns people, they distrust and dislike you but explain that the witch lives and has wrought her revenge upon the town, sending fell beasts upon them when night time falls. You are surrounded by the mob, from amongst them stands a girl wielding a shoe, she hits you, her dad cmoes over 'Oubliette, Oubliette! What do you think you are doing, stop that at once! You must never beat a stranger with a shoe, you might as well tickle them, always use a good sturdy leather boot" and he passes her sturdy leather boot. She considers it, weighs it and then beats you. Everything goes black.
  - You wake up in the late afternoon. You are tied up in the street, propped, sitting, against a wall. Oubliette is watching you, she apologises and introduces her sister Flechette, you notice she is holding a boot behind her back. You must talk to Oubliette to get free, she tells you all about the witch and how it's your fault, if you say something wrong Flechette hits you with the boot. Eventually you must agree to help.
  - You must help them repair the barricade to the north of the town by the guards tower/gate before night falls. Speaking to Cabal Jambon causes him to give you a broken impsaw 2000 and an imperator.
  - You must find teeth from a vicious Spiky-toothed Goobers (fish) to repair the impsaw. You must assemble a fishing rod, wake a fisherman to get his lure and send him back to sleep with a bottle of cider. Then you can use the fishing rod on the river to retrieve a Goober (the first fish is a useless Blue Mullet).
  - Then you must use the imperator on the impsaw to enrage the imp and get it to run around causing the chain of teeth to spin round. You then use it to chop branches from a tree in the wood and bring the branches back to repair the barricade.
  - Once the barricade is repaired you must take evidence of an impending attack to Cabal Jambon. The evidence he wants is a charred bone with runes inscribed given to you by Oubliette after picking mushrooms by the castle ruins.
- [STORY] Oubliette asks that you escort her for a mushroom gathering task to an abandoned castle in ruins protected by a barrelling goon. Once completed she gives you a mushroom - it can heal if eaten a little or cause a very deep sleep if too much is eaten. You also get a charred bone that looks like it has been used in a magic ritual.
  - This can be completed in parallel to the barricade repair.
  - The first puzzle is the portcullis, you must get an iron bar from the undergrowth next to it, unjam the mechanism, climb over the hidden path of rocks to get to the other side of the portcullis and use the winch handle to wind the winch to raise the portcullis.
  - The second puzzle is beating the barrelling goon - you must place a hollow tree trunk so that when the goon barrels it shoots down a slide and up a ramp over the castle ruins wall leaving the area safe for gathering mushrooms.

- [MINOR STORY CORRECTION] Little girl is introduced as "Flechette" but her description remains a small girl".
- [MINOR STORY CORRECTION] Once you've accepted Oubliette's mushroom challenge she disappears but if you try talking to the little girl you are told that the older girl pulls her back.
- [MINOR STORY CORRECTION] Cabal Jambon no longer repeats himself when you talk to him and doesn't repeatedly pass the chainsaw and imperator to you
- [MINOR STORY CORRECTION] Cabal Jambon is named properly after being introduced by talking to him or by Oubliette
- [MINOR STORY CORRECTION] Iron bar left in the mechanism after unjamming the portcullis
- [MINOR STORY CORRECTION] Two Oubliettes no longer appear outside the castle gate portcullis after opening it if you don't talk to her first

2.1.1
-----

- [BUG] Lost save game when restarting phone - reported on intfiction forum here http://www.intfiction.org/forum/viewtopic.php?f=19&t=8891
  - Second report of lost saves, this time partial ('It jumped back once and made me redo a small section. Last night, though, it skipped back to almost the beginning.'). From Play reviews (Lisa Rodenberry) 16/9/13.
  - Likely cause is overlapping load AsyncTask and onPause()/save event. If the loading task has not completed when onPause() is called it will save an empty or a partially populated action history.
    - This could be reproduced with a very slow emulator (start then stop app). Or add a wait at the end of loadSerialisedActionHistory() after file is loaded (but before it is parsed).
    - [FIX] check if load has finished in onPause() and if not then cancel load task and skip saving (since nothing will have changed). Add 'loading' flag to onResume() before executing load task which is cleared in LoadTask.onPostExecute().

2.1
---

- [BUG] Can't start a new game, old game gets saved and on load is used instead of new game
  - [FIX] reset action history on new game

2.0
---

- [FEATURE] Scrollbars in action buttons list are permanent to make it clear that there might be more buttons
- [FEATURE] Scroll to the top of _new_ description content instead of scrolling to the bottom of the current content all the time.
- [FEATURE] Forced available items text in a locations to be just before the exit text instead of getting lost in the description/story text.
- [FEATURE] The user will be notified when they have reached the end of the available content with text at the end of the description.
- [FEATURE] Removed mentions of rubies in the text. Changed the score representation in the GUI to a simple percentage.
- [FEATURE] The font size can be changed via the options dialog.
- [FEATURE] The 'Examine an item' button text changed to 'Examine' so it works nicely with items and people.
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
- [BUG] Freeze and long load/resume time
  - [FIX] BasicModel.findExitByID() is cached in a map to improve fetch time
  - [FIX] When loading the save file, view disabler in Activity::replayActions() tells the presenter to stop sending view updates. This should save a significant amount of string construction for no reason. - 17% of load game time
  - [FIX] BasicModel.findItemByID() is cached in a map to improve fetch time - 33% of load game time
  - [FIX] Loading is in a seperate thread with a spinner on the GUI, should avoid Android saying 'app not responding'
- [BUG] Save time is very very long too...
  - [FIX] Most of time was spent serialising and building a string. Using a StringBuilder in ActionHistorySerialiser reduced the time taken by a factor of ten.

- [TECHNICAL FEATURE] Items can have talk/say action chains. If an item has talk responses then include a 'Talk' action in Location::actions(). Talk action allows choice of n phrases. Choosing a phrase triggers a response as specified in model content. Choosing a phrase can optionally trigger an alternative set of phrases to become available as follow up actions. Choosing a phrase can optionally trigger actions (use the same actions as 'item use action:' directive) e.g. make an exit visible.
  - DONE Location has a TalkToAction for each item that is canTalkTo() == true
  - TalkToAction
    - DONE A TalkToAction has an item. It's label is 'Talk to ' + item.midSentenceCasedName()
    - DONE A TalkToAction has a follow up action of SayAction, constructed with Item.getTalkPhraseSource(), for each initial phrase id returned by talkable.initialPhraseIds()
  - SayAction
    - DONE A SayAction has the label 'Say "' + talkable.shortPhraseById( id ) + '"'
    - DONE A SayAction when enacted has user text of talkable.phraseById( id ) + talkable.responseToPhraseById( id )
    - DONE A SayAction when enacted has user text of just talkable.phraseById( id ) when no responses are available
    - DONE A SayAction when enacted has a follow up SayAction for each id returned by talkable.followOnPhrasesForPhraseById( id )
    - DONE A SayAction when enacted calls talkable.executeActionsForPhraseById( id )
  - DONE UserActionFactory can create TalkToActions
  - DONE UserActionFactory can create SayActions
  - DONE RecordableActionFactory can create TalkToActions
  - DONE RecordableActionFactory can create SayActions
  - DONE Deserialise talk related specs from model content into TalkPhraseSink
  - Item
    - DONE Item.canTalkTo()
    - DONE addInitialTalkPhrase()
    - DONE phraseById()
    - DONE responseToPhraseById()
    - DONE followOnPhrasesForPhraseById()
    - DONE executeActionsForPhraseById()
    - DONE shortPhraseById()
  - DONE Deserialise short talk phrases into TalkPhraseSink
  - DONE Deserialise follow up phrases specified by id only e.g. "item talk follow up phrase to:lookingwell:lookingforwork:". Use TalkPhraseSink.addFollowUpPhrase( parentId, childId ).
  - DONE Deserialise TalkToAction and SayAction for ActionHistory
  - DONE Serialise TalkToAction and SayAction for ActionHistory and ActionParameters
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

- [STORY] Once you have all the ingredients you return to Lady Bella and use them all with her. Once the last is used "She says 'Wonderful, you have done very well, now leave me to my work I must concentrate.' The look on Lady Bella's face suddenly turns very dark as she assembles the ingredients and potion making paraphenalia and you wonder if it is simply concentration or something else. You turn to Lady Bella to ask if there was any reward for bringing her these items but before you can speak she scowls at you and shouts 'Get out!'. You back up towards the door as she puts the ingredients into a large mixing bowl and your senses are suddenly assailed by a hideous smell. 'That doesn't smell like a love potion to me! ... that smells more like a death potion!'. Bella laughs mockingly 'Stupid boy, of course it's not a love potion, what a waste of an arc stone that would be, this is to get rid of my worthless husband.' You recoil, horrified at the realisation you have been used by this woman to help her achieve her evil goal. 'But why?' you ask. 'I can't stand him, he's an idiotic oaf. He gives me nothing and expects everything. I'm going to take all that is his and make him vanish from living memory.'"
  - You can defeat her by using 'Witches Bane' a herb that you find somewhere.
  - Increment the score.
- [STORY] The Mendicants scapula can be found in the Ossery of the friary. Increment the score.
  - To gain entry you must give alms to a friar at the gate who has taken a vow of silence. You can talk to him but he only replies '...'. There is a sign next to him explaining the alms for entry bit. Get the alms as 'food and wine' from the store room in the brigands hideout under the church.
  - There is a north courtyard with a tower leading to a library. There is an Einstein-a-like friar librarian stuck on a puzzle "Rupriks Hexadecachoron - 'The problem is it has too many surfaces, I just can't explain the angles to a layman like you.'" in the library who you must help to enable you to get entrance to the tunnels underneath the friary. Once helped a loose flagstone becomes visible in the pantry off the kitchen.
  - You can also gain access to the north building from the north courtyard which includes a kitchen and a route through the kitchen down underneath the friary (found by uncovering a loose flagstone which is only visible after helping the librarian). In the tunnels you can find the "piled bones of long dead friars". You can see and pick up a clavicle, a tibia and a fibula, as well as a scapula.
- [STORY] You must find the silk blood in the forest. The forest is a maze of twisty passages - all alike. Increment the score.
- [STORY] The Arc stone can be found by using the magic pick axe with the shimmering seam in the mine, 3 times. First time the message is 'The seam appears to react to the pick axe as soon as you bring it near. You strike once and a deep rumble resounds about you however none of the rock is chipped away.', make the seam invisible and make the next seam visible - use message is 'The seam is definitely reactive, you raise the pick axe and bring it firmly down on the rock. A small fleck of rock chips away to reveal a larger stone embedded in the seam however it will take some more work to remove it.', make the seam invisible and make the final seam visible - use message is 'You resolve to strike hard this time and raise the axe in both hands bringing it confidently down upon the seam. The rock cracks and the embedded stone is freed from the surrounding rock.'. Increment the score.
- [STORY] When you speak to Oren's wife (Lady Bella Constantine) she thanks you for retrieving the necklace. The original necklace description needs to include 'it is adorned with several symbols in amongst the Sapphires - you do not recognise them but they look a little out of place on such a necklace.'. She asks if you can help her with a harmless love spell for a friend - she needs three ingredients - a mendicants scapula (from the ossery of a friary), some silk blood (a herb found in the forest) and an arc stone (found by mining a seam in McCreedys mine). She casts a spell on your pick-axe to make it a magic pick axe and mentions only green eyed men can find an arc stone - that's good because you have green eyes. (the general store shop keepers description needs to include a small mention that he has green eyes). The shimmering seam in the mine becomes visible.
- [STORY] On the east side of town is the Nobels quarter. Oren Constantine's house is here, he sits on the porch waiting for news about his wife's necklace. Until you have the necklace you can say this:
  - "Hello Mr Constantine, I was wondering if you had any jobs an up and coming adventurer might be interested in." => "Well unless you've got my wife's necklace I have nothing for you, now get off my land before I release the hounds. Hrrmph." says Constantine as he pointedly turns his back on you.
  - Once you have necklace you can say this:
    - "Hello Mr Constantine - is this your wifes necklace?" => "Why yes good sir! Thank you, you have cured a great headache for me, at last I can have some peace!"
    - "I'm so glad, I found it in a hidden crypt, in a safe being guarded by some brigands of dubious quality." => "Oh is that so?" replies Constantine. "Well never mind how you found it, what's important is that we have it back." He seems to make a point of saying the last sentence overly loud. Glancing furtively about you wonder if anyone might be listening. You pass the necklace to Oren.
    - "Ok, well here it is." you say expectantly. => "And here is your reward." Oren passes you a bright ruby, which you add to the growing collection in your pocket. "And whilst you're here Im sure my wife would like to thank you personally." He motions towards the front door and you feel you cannot refuse his invitation.
    - After saying the above the necklace is destroyed and the entrance to the house is made visible. You gain a ruby and the notice boards for done and todo are made in/visible as appropriate at the town hall.
- [STORY] Once you pass the brigand you see a few more rooms one of which contains a locked voice-activated safe. You must find the 3 word code to unlock it. The words can be found in the surrounding rooms, on a piece of burnt paper, scrawled on the side of the safe and finally you must speak to an NPC.
  - 'Fnargl' is found on a bit of paper in a store room. 'Bendlebox' is on a piece of paper in the mine where the map was found. 'Floomembrow' is found on a bit of paper in the hovel.
  - To open the safe you must say the 3 words in the correct order. They should all be nonsense words (total 39 unique words needed) like bongle, doofap, tremedor, obviate, alucidit, banaroo, diddle, bendlebox, roobibus, diffdap, fnargl, anthrakh, pottum, junisper, bentium, blongle, boodle, gafaro, leeple, conjuper. If you get the sequence wrong the safe says "What? I'm not letting you in, that was clearly nonsense.", "What are you, some sort of idiot? Stop babbling at me and leave me in peace.", "Who do you think you are? I'm smarter than you and I know you made that up!". On success "Phew, I've been waiting for someone to open this safe for ages, I'm gasping for some fresh air, I've been here for days." says a small imp perched on a tiny chair inside the safe.
  - Opening the safe reveals a necklace which you can pick up. You must use the dust of the ancients to remove the magical cloaking, then the score is increased.
  - This also makes 'grumpy_oren_constantine' invisible and 'amiable_oren_constantine' visible.
- [STORY] Passing the wolf there is a complex of rooms, there are voices and locked doors. One room deep within has a brigand blocking your way. You can talk to the Brigand, yuo complement him on his hair and clothing, he opens up and admits he is just an actor, flattered by your compliments. He lets you pass.
- [STORY] On examining the '8th' candlestick above the altar you realise it is different from the rest. You can use a rope with it to trigger trapdoor. Going below you are blocked by a ravenous wolf guarding a door, his collar and nameplate say 'Cuddles'. You must use an old leg bone from the graveyard to placate it. An exit is then accessible behind the wolf.
- [STORY] At the town hall notice board there are multiple notices (act as reminders of what the player has left to do, small board, medium board, azure board, each with a clue to a different story line). The notices appear as the player completes other prerequisite tasks. There is also a hall of deeds in the town hall where there are notices that state what the player has done, e.g. rescued the shop keeper - it's an achievements record.
- [STORY] On entering the grave yard there is a raging Ansible, when you examine it it moves to a different location. The description states "You feel compelled to follow it.". If you keep following/examining it it leads you to a particular grave where an encoded inscription is written on the headstone. Use the thing from the shopkeeper to read the inscription which directs you inside the church to the back of the altar and makes the candlesticks visible.
- [STORY] Shopkeeper directs you to town hall notice board where reports of strange goings on are being posted. This triggers the entrance to the town hall to become visible.

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

Rejected
========

- Probably a bad idea as with the Talkable/TalkPhraseSource/Sink experience it has made using the Item a bit awkward.
- [REFACTOR] Item interface is getting fat. Can 'use' and 'examine' be split off, perhaps Examinable, Usable? Is there any benefit?

- No longer supporting the webapp editor.
- [FEATURE-EDITOR] Update webapp editor to [de]serialise visibility and allow setting of visibility
- [FEATURE-EDITOR] Update webapp editor to allow adding/deletion of on examine actions to items
