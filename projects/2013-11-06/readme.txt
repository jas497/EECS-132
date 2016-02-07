A clone of the game of JewelQuest.  Click on two gems to swap them.  If
three or more in a row are of the same type, they are removed and the
tiles marked.  No auto-removal - user must target the rocks him- or 
her- self.  A cell with a black background/border is uncleared, while a 
cell with a white of same is cleared.  The game is won when all of the 
black cells have been converted to white.

Which gem types are present is based on resistor color codes.
Gems chosen are as follows:
	0 - schorl
	1 - smoky quartz
	2 - pyrope
	3 - carnelian
	4 - cat's eye
	5 - tsavorite
	6 - worthless piece of blue glass
	7 - amethyst
Each icon represents a normal cut for that gem, as approximated by MS Paint.

notes on the code:

data is an array of arrays of bytes.  It represents the board.  When
referencing it, hex digits are used (0x??), even though decimal tends
to be shorter to type (e.g., 0x10 instead of 16).

structure of data[][]: low hex digit is type, high is flags
flags: null, null, to-be-cleared, marked. High-to-low.
To-be-cleared is for "deleting" the rocks.  Marked is the indicator
of whether a cell at these coordinates has already been cleared.
Whole board must return true for (data[y][x] & 0x10 == 0x10).  
