Checkers Server
=============

Komande
----------------
#####Login
	> LOGIN: <username>
#####Leave game
	> LEAVE GAME: <game_id>
#####Send a game request
	> GAME REQUEST: <username>
#####Accept a game request
	> GAME ACCEPT: <inviter_username>
#####Decline a game request
	> GAME DECLINE: <inviter_username>
#####Send a message to game room
	> GAMEMSG: <message>
#####Send a message to lobby
	> LOBBYMSG: <message>
#####Move a figure in a game room
	> MOVE: <game_id> <from_x> <from_y> <to_x> <to_y> <eaten>
#####Reply to a ping
	> PONG
#####Print all connected users
	> USERS
#####Print all available users
	> FREE USERS
#####Print all active game
	> GAMES
#####Print all commands
	> HELP

Povratne vrednosti
-----------
#####E_OK
	Sve je prošlo ok.
#####E_ARGUMENT_COUNT 
	Zadata komanda ima pogrešan broj argumenata.
#####E_COMMAND
	Zadata komanda nije prepoznata. 
#####E_INVALID_MOVE
	Zbog pravila igre, potez je bio nelegalan.
#####E_USERNAME_TAKEN
	Odabrano korisničko ime je već zauzeto.
#####E_GAME_FULL
	Igra već ima dva igrača i nema mesta za dodatne igrače.
#####E_NO_PLAYER
	Igrač nije ulogovan. (pre bilo koje komande treba uraditi LOGIN: <ime>).
#####E_NO_GAME
	Igra sa datim id-em ne postoji.
#####E_MESSAGE: username message
	Nova poruka od igrača username.
#####E_INFO: message
	Informativna poruka.
#####E_LOBBY_MESSAGE: username message
	Nova poruka od igrača username u lobby-u.
#####E_LOBBY_INFO: message
	Informativna poruka u lobby-u.
#####E_GAME_STARTED
	Dva igrača su konektovana, igra je počela.
#####E_GAME_OVER
	Igra je gotova.
#####E_GAME_ACCEPTED: username
	Username je prihvatio poziv za igru.
#####E_GAME_DECLINED: username
	Username je odbio poziv za igru.
#####E_GAME_REQUEST: inviter
	Inviter je poslao poziv za novu igru.
#####E_INVALID_ARGUMENTS
	Argumenti komande su u lošem formatu.
#####E_LAG: lag
	Vreme odziva u sekundama.
#####E_COMMANDS: commands
	Lista svih dostupnih komandi.
#####E_USERS: users
	Lista svih konektovanih korisnika.
#####E_STATE: state
	Serijalizovan objekat sa trenutnim stanjem igre u Base64 formatu.
#####PING
	PING poruka. Zahteva odgovor sa PONG komandom.
