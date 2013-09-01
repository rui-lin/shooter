Shooter
=======
Multiplayer 2D tank game, built as a java applet.
Play against the computer or another human player, in teams or individually.

Gameplay
--------
Play as a team of 2 against the computer's team duo in this simple but addicting
top view tank game. Earn powerups, unlock new weapons, and defeat the other team!

Default weapons: Basic Cannon, Blob Gun, Machine Gun, Rocket Launcher
Unlockables: Sniper, Mine, Icicle Launcher, Waller

Powerups: Chicken Soup (Protects against Freeze), Antidote, Health Potion,
Tank speed up, Bullet speed up, Toxic waste trap, Recoil decrease, etc.

Default controls
----------------
Player 1
* Movement: WASD
* Shoot: <Space>
* Change Weapons: Q or E

Player 2
* Movement: <Arrow Keys>
* Shoot: <Ctrl>
* Change Weapons: / or <Shift>

Controls can changed in game (saved in `config.ini`)

Game Tweaks
---------------------------------------
## Play against more computers, or human players.
Add another `addPlayer()` call in `Shooter.java: showGameScreen()`

## Change AI difficulty, number of teams, lives, etc.
Modify the parameters in `addPlayer()` calls in `Shooter.java: showGameScreen()`

Note: I was going to add a panel to expose this to the player but did not get around to it yet.

## Add new weapons
Weapons/bullets are of a generic nature and can be created dynamically using the
`Weapon` class, and specifying attributes such as size, color, damage, ammo, etc.
Note that effects cannot be generalized, but there are currently 3 supported ones,
poison, freeze, and absorb hp, that can be attributed to certain weapons.
For examples, look in `Player.java: DEFAULT_WEAPONS`

Misc
----
Tested on java 1.6