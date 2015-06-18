<!----------------------------------------------------------------------------->
# 0.5.0 (2015-05-18)
## New
- refactored Exporter
- updated Gradle from snapshot version to official 2.4 release
- changed Play/Pause buttons
- added StopWatch
- naming: obehave instead of Obehave
- coding per button clicks! \o/

## Bugfixes
- 114: Words in wordlist modifiers are only allowed once
- 111: action groups now appear immediately


<!----------------------------------------------------------------------------->
# 0.4.0 (2015-04-08)
## New
- FontAwesome for buttons
- more comfort while coding: jumping back to subject after finished a coding
- Fancy windows installer \o/ (iscc.exe has to be in %PATH% - http://www.jrsoftware.org/isdl.php)
- changed default paths for studies, logs and exports to ~/Obehave/

## Bugfixes
- 102: Closing codings shouldn't be a problem anymore.


<!----------------------------------------------------------------------------->
# 0.3.0 (2015-04-08)
## New
- Drawing of subject modifier codings

## Known Issues
- 102: Ending active state codings (especially multiple ones, with different modifiers) won't work correctly
- 103: Timeline in CodingControl has issues with loading other observations with a different count of participating subjects

## Bugfixes
- 83: closing program wasn't possible with window decoration
- 84: suppress H2 errors when problems loading database occur
- 85: cannot use Cancel button in "Choose a title" dialog
- 86: Crash when loading a study twice
- 87: Creating a new study at a given, already loaded one deleted the loaded study
- 88: (irrelevant) Exception in ProjectTree when creating a new study
- 90: Hide video playback buttons when there is no video to play
- 93: NPE when clicking somewhere in the TreeView, when there is no TreeItem
- 95: Archived log files were written into the wrong directory
- 96: EventBus exceptions weren't logged properly


<!----------------------------------------------------------------------------->
# 0.2.0 (2015-04-06)
## New
- Loading of Codings from Observations is now possible
- Using a fancy Arranger.

## Known Issues
- 91: Linux can't play mp4/H264 videos
- 92: PopOvers won't stay in the back


<!----------------------------------------------------------------------------->
# 0.1.0 (2015-03-22)
## New
- Creating entities such as Subject, Action, Modifier and Observations is now possible
- Preliminary version of Timeline is created
- Video playback using JavaFX is possible