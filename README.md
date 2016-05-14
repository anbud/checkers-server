Checkers Server
=============

Komande
----------------
#### Login
	> LOGIN: <username>
#### Create game
	> CREATE GAME:
#### Join game
	> JOIN GAME: <game_id>
#### Leave game
	> LEAVE GAME: <game_id>

Povratne vrednosti
-----------
####E_OK
	Sve je prošlo ok.
####E_ARGUMENT_COUNT 
	Zadata komanda ima pogrešan broj argumenata.
####E_COMMAND
	Zadata komanda nije prepoznata. 
####E_INVALID_MOVE
	Zbog pravila igre, potez je bio nelegalan.
