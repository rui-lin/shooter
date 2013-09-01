import java.awt.event.*;

import java.util.*;

import java.util.Arrays;

public class PlayerManager extends Thread implements KeyListener
{
    List < Player > players;
    boolean[] keysDown;
    Shooter game;

    public PlayerManager (Shooter game)
    {
	players = Collections.synchronizedList (new ArrayList < Player > ());
	this.game = game;
	keysDown = new boolean [256]; // all possible keys
	Arrays.fill (keysDown, false);
	game.gameScreen.addKeyListener (this);
    }


    public void addPlayer (Player p, boolean human, int team)
    {
	p.human = human;
	p.playerID = players.size () + 1;
	p.teamID = team;
	p.x = (int) (Math.random () * (game.SCREEN_WIDTH - p.width - 50) + 50);
	p.y = (int) (Math.random () * (game.SCREEN_HEIGHT - p.height - 50) + 50);
	players.add (p);
    }


    public void removePlayer (Player p)
    {
	players.remove (p);
    }


    public List < Player > getPlayers ()
    {
	return players;
    }


    public int getMaxPlayers ()
    {
	return players.size ();
    }


    public void damagePlayer (int player, int dmg)
    {
	players.get (player).health -= dmg;
    }


    public void updateHumanPlayer (Player player)
    {
	// Apply movement
	if (keysDown [player.keys [player.UP]])
	{
	    player.y -= Math.max (0, player.speed + player.moveSpeedBonus);
	    player.gunDir = player.UP;
	}
	else if (keysDown [player.keys [player.DOWN]])
	{
	    player.y += Math.max (0, player.speed + player.moveSpeedBonus);
	    player.gunDir = player.DOWN;
	}
	else if (keysDown [player.keys [player.LEFT]])
	{
	    player.x -= Math.max (0, player.speed + player.moveSpeedBonus);
	    player.gunDir = player.LEFT;
	}
	else if (keysDown [player.keys [player.RIGHT]])
	{
	    player.x += Math.max (0, player.speed + player.moveSpeedBonus);
	    player.gunDir = player.RIGHT;
	}

	// Check if player wants to switch weapons
	if (keysDown [player.keys [player.NEXT_WEAPON]])
	{
	    int g = player.weapons.indexOf (player.weapon);
	    while (true)
	    {
		g++;
		if (g >= player.weapons.size ())
		    g = 0;
		if (player.weapons.get (g).available)
		    break;
	    }
	    player.weapon = player.weapons.get (g);
	}
	if (keysDown [player.keys [player.PREVIOUS_WEAPON]])
	{
	    int g = player.weapons.indexOf (player.weapon);
	    while (true)
	    {
		g++;
		if (g >= player.weapons.size ())
		    g = 0;
		if (player.weapons.get (g).available)
		    break;
	    }
	    player.weapon = player.weapons.get (g);
	}

	// Check if player shoots
	if (keysDown [player.keys [player.SHOOT]] &&  // shoot key is pressed
		(player.weapon.ammo > 0 || player.weapon.ammo == -1) &&  // ammo is sufficient
		(player.timeInRecoil - player.recoilSpeedBonus) > player.weapon.recoil) // recoil has passed
	{
	    player.timeInRecoil = 0;
	    if (player.weapon.ammo != -1)
		player.weapon.ammo--;

	    game.projectileManager.addProjectile (new Projectile (player, player.weapon));
	}
    }

    private boolean chance (double c) {
	if ((int) (Math.random()*c) == 0)
	    return true;
	else
	    return false;
    }


    public void updateComputerPlayer (Player player)
    {
	// Move player
	if (player.moving) {
		if (player.gunDir == player.UP)
			player.y -= Math.max (0, player.speed + player.moveSpeedBonus);
		if (player.gunDir == player.DOWN)
			player.y += Math.max (0, player.speed + player.moveSpeedBonus);
		if (player.gunDir == player.LEFT)
			player.x -= Math.max (0, player.speed + player.moveSpeedBonus);
		if (player.gunDir == player.RIGHT)
			player.x += Math.max (0, player.speed + player.moveSpeedBonus);
	}

	// Chance of changing direction
	if (player.enragedtime>0) {
		player.moving=true;
		player.enragedtime--;
	} else {
		if (player.moving) {
			if (chance(300)) player.moving=false;
		} else {
			if (chance(40)) player.moving=true;
		}
	}

	if (player.moving)
	if (chance(50)) // 1/50 chance of dir change
	    player.gunDir = (int) Math.round (Math.random () * 3);

	// Change direction if near edge of screen
	if (player.x <= 50 || player.x >= game.SCREEN_WIDTH - player.width - 50 || player.y <= 50 || player.y >= game.SCREEN_HEIGHT - player.height - 50)
	    if ((int) (Math.random () * 20) == 0)
		player.gunDir = (int) Math.round (Math.random () * 3);

	// Change direction if on edge of screen
	if (player.x == 0)
	    player.gunDir = player.RIGHT;
	if (player.x == game.SCREEN_WIDTH - player.width)
	    player.gunDir = player.LEFT;
	if (player.y == 0)
	    player.gunDir = player.DOWN;
	if (player.y == game.SCREEN_HEIGHT - player.height)
	    player.gunDir = player.UP;

	// Chance of changing weapon
	if ((int) (Math.random () * 500) == 0 || player.weapon.ammo == 0)  // 1/500 chance of wep change, or when no ammo
	{
	    do
	    {
		int g = (int) (Math.random () * (player.weapons.size () - 1));
		player.weapon = player.weapons.get (g);
	    }
	    while (!player.weapon.available || player.weapon.ammo == 0);
	}

	// Shoot if player within range
	if ((int) (Math.random () * 5) == 0)
	    for (int i = 0 ; i < players.size () ; i++)
	    {
			Player p = players.get (i);
			if (p.teamID == player.teamID && p.teamID != 0) // comps on same team don't shoot each other
				continue;
			if ((player.attacking && chance(5)) || !player.attacking) {
				player.attacking=false;
				if (Math.abs (p.x - player.x) < player.width) // can opponent be hit by vertical shot
				{
					if (p.y < player.y)
						player.gunDir = player.UP;
					else
						player.gunDir = player.DOWN;
					player.attacking=true;
				}
				else if (Math.abs (p.y - player.y) < player.height) // can opponent be hit by horizontal shot
				{
					if (p.x < player.x)
						player.gunDir = player.LEFT;
					else
						player.gunDir = player.RIGHT;
					player.attacking=true;
				}
			}
			if (player.attacking) {
				if (chance(5)) player.moving=true;
				if (player.timeInRecoil - player.recoilSpeedBonus > player.weapon.recoil) {  // recoil has passed
					if (player.weapon.ammo != -1)
						player.weapon.ammo--;

					player.timeInRecoil = 0;
					game.projectileManager.addProjectile (new Projectile (player, player.weapon));
				}
				player.enragedtime=100;
			}
	    }

	// Dodge all bullets
	List < Projectile > proj = new ArrayList < Projectile > (game.projectileManager.getProjectiles ());
	for (int i = 0 ; i < proj.size () ; i++)
	{
	    Projectile p = proj.get (i);

	    if (p.x + p.width > player.x && p.x < player.x + player.width) // bullet vertically above or below player
		if ((p.y < player.y && p.yMove > 0) || p.y > player.y && p.yMove < 0) // bullet's direction will cause it to hit player
		{
		    player.moving=true;
		    if ( !chance(player.health/20) ) continue;
		    player.enragedtime=30;
		    if ((p.x - player.x > player.x + player.width - p.x && player.x > player.width) || player.x > game.SCREEN_WIDTH - player.width * 2)
				player.gunDir = player.LEFT;
		    else
				player.gunDir = player.RIGHT;
		    break;
		}
	    //player.gunDir = (Math.round (Math.random ()) == 0) ? player.LEFT:
	    //player.RIGHT;
	    if (p.y + p.height > player.y && p.y < player.y + player.height) // bullet horizontally beside player
		if ((p.x < player.x && p.xMove > 0) || p.x > player.x && p.xMove < 0) // bullet's direction will cause it to hit player
		{
		    player.moving=true;
		    if ( !chance(player.health/20) ) continue;
		    player.enragedtime=30;
		    if ((p.y - player.y > player.y + player.height - p.y && player.y > player.height) || player.y > game.SCREEN_HEIGHT - player.height * 2)
				player.gunDir = player.UP;
		    else
				player.gunDir = player.DOWN;
		    break;
		}
	    //player.gunDir = (Math.round (Math.random ()) == 0) ? player.UP:
	    //player.DOWN;
	}
    }


    public void run ()
    {
	List < Solid > solids = game.solidManager.getSolids ();
	List < Bonus > bonuses = game.bonusManager.getBonuses ();
	while (true)
	{
	    // updates all players
	    for (int i = 0 ; i < players.size () ; i++)
	    {
		Player player = players.get (i);

		// Update recoil
		player.timeInRecoil++;

		// Check if player is gamed-over (no lives left)
		if (player.lives <= 0)
		{
		    System.out.println (player.lives);
		    players.remove (player);
		    System.out.println ("someone died");
		    continue;
		}

		// Check if player died
		if (player.health <= 0)
		{
		    player.reset ();
		    player.x = (int) (Math.random () * (game.SCREEN_WIDTH - player.width - 100) + 50);
		    player.y = (int) (Math.random () * (game.SCREEN_HEIGHT - player.height - 100) + 50);
		    game.bonusManager.addRandomBonus ();
		    game.bonusManager.addRandomBonus ();
		}

		// Apply status effects
		if (player.poisonTime > 0)
		{
		    player.poisonTime--;
		    if (player.poisonTime % 10 == 0)
			player.health--;
		}
		if (player.frozenTime > 0)
		{
		    player.frozenTime--;
		    continue; // go check next player (this player can't do anything more)
		}

		// Check collision
		for (int j = 0 ; j < solids.size () ; j++)
		{
		    if (game.intersects (solids.get (i).x, solids.get (i).width, solids.get (j).y, solids.get (j).height,
				player.x, player.width, player.y, player.height))
		    {
			// brings player back to original position if collided with a solid
			if (player.gunDir == player.LEFT)
			    player.x = player.x + player.speed + player.moveSpeedBonus;
			else if (player.gunDir == player.DOWN)
			    player.y = player.y - player.speed - player.moveSpeedBonus;
			else if (player.gunDir == player.RIGHT)
			    player.x = player.x - player.speed - player.moveSpeedBonus;
			else if (player.gunDir == player.UP)
			    player.y = player.y + player.speed + player.moveSpeedBonus;
			break; // saves time (can only collide with one solid)
		    }
		}
		for (int j = 0 ; j < bonuses.size () ; j++)
		{
		    if (game.intersects (bonuses.get (j).x, Bonus.SIZE, bonuses.get (j).y, Bonus.SIZE,
				player.x, player.width, player.y, player.height))
		    {
			bonuses.get (j).activate (player, game.bonusManager);
			game.bonusManager.removeBonus (j);
		    }
		}

		// Update other things specific to type of player
		if (player.human)
		    updateHumanPlayer (player);
		else
		    updateComputerPlayer (player);

		// Check if player is offscreen
		player.x = Math.max (0, player.x);
		player.x = Math.min (game.SCREEN_WIDTH - player.width, player.x);
		player.y = Math.max (0, player.y);
		player.y = Math.min (game.SCREEN_HEIGHT - player.height, player.y);
	    }

	    // randomly add new enemy
	    //if (Math.random () > 0.99) // 1/101 chance
	    //    addPlayer (new Player (2, 50, game.PLAYER_IMG [4], false), false, 0);

	    try
	    {
		Thread.sleep (40);
	    }
	    catch (Exception e)
	    {
	    }
	}
    }


    public void keyPressed (KeyEvent e)
    {
	keysDown [e.getKeyCode ()] = true;
    }


    public void keyTyped (KeyEvent e)
    {
    }


    public void keyReleased (KeyEvent e)
    {
	keysDown [e.getKeyCode ()] = false;
    }
}


