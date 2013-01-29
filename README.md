Text Adventure
==============

An Android old-skool text adventure game. This is another exercise in TDD in an Android environment.

Backlog
=======

- Begin in a more complex starting room with four exits, north, south, east and west.
  - Allow the user to move north, south, east and west by tapping a quadrant of the screen.
  - Change colour of direction labels background to contrast better with main text area.
  - [TEST] Activity uses exit labels on onscreen direction labels.
    - [TEST] Turn exit labels into Exit class and update all tests in Activity.
    - [TEST] Change Location to store exits as Exit objects.
    - [TEST] Change Model to retrieve exits as Exit list.
    - [TEST] Change Presenter and View to pass exits as Exit list.
    - [TEST] Add 'direction hint' to Exit object so Activity can use it to place N,S,E,W appropriately.
    - [TEST] UserActionHandler.moveThroughExit should take an Exit object
  - [TEST] Activity receives touch events, translates to available direction and calls presenter to do the movement
<br/>
- Make a more complicated map

- Add objects that can be examined to rooms
  - Pressing the centre of the screen gives a list of verbs, just 'examine' is available atm.
  - When user selects examine a list of objects in the room is given.
  - When the user chooses the object a description is shown.
  - The user can show the text for the room again by selecting 'examine' and choosing 'room'.

- Add other monsters, npcs

- Add other verbs - attack, eat, drink, dance, take, drop, give, talk

- Present verbs in a more accessible format - a wheel?

- Save progress
  - Add a menu on the Android menu key so the user can select 'save'.
  - Autosave

- Optional images to go with text?

Technical Tasks
===============

- Read the map from a text file for easy configurability.
- Read object data from a text file for easy configurability.
- Read npc/monster data from a text file for easy configurability.
- Improve build system so only necessary files are recompiled.
- Improve build system so only tests with changed source code are rerun.

