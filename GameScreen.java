import java.awt.image.*;
import java.awt.Graphics;
import java.awt.Color;
import java.util.*;

import javax.swing.JPanel;

public class GameScreen extends JPanel
{
    Graphics bufferGraphics;
    BufferedImage buffer;

    Shooter game;

    public GameScreen (Shooter game)
    {
	buffer = new BufferedImage (game.SCREEN_WIDTH, game.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB); // createImage(game.SCREEN_WIDTH,game.SCREEN_HEIGHT);
	bufferGraphics = buffer.createGraphics ();
	this.game = game;
	this.setFocusable (true);
    }


    protected void paintComponent (Graphics g)
    {
	super.paintComponent (g);

	java.util.List < Label > labels = game.bonusManager.getLabels ();
	java.util.List < Bonus > bonuses = game.bonusManager.getBonuses ();
	java.util.List < Solid > solids = game.solidManager.getSolids ();
	java.util.List < Projectile > proj = game.projectileManager.getProjectiles ();
	java.util.List < Player > players = game.playerManager.getPlayers ();

	// Sets up for drawing to buffer
	bufferGraphics.setColor (Color.white);
	bufferGraphics.fillRect (0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
	bufferGraphics.setColor (Color.black);

	for (int i = 0 ; i < bonuses.size () ; i++)
	{
	    Bonus b = (Bonus) bonuses.get (i);
	    bufferGraphics.fillRoundRect (b.x, b.y, b.SIZE, b.SIZE, 10, 10);
	}
	for (int i = 0 ; i < labels.size () ; i++)
	{
	    Label l = (Label) labels.get (i);
	    bufferGraphics.drawString (l.text, l.x, l.y);
	}
	for (int i = 0 ; i < proj.size () ; i++)
	{
	    Projectile p = (Projectile) proj.get (i);
	    bufferGraphics.setColor (p.colour);
	    bufferGraphics.fillRect (p.x, p.y, p.width, p.height);
	}
	for (int i = 0 ; i < solids.size () ; i++)
	{
	    Solid s = (Solid) solids.get (i);
	    bufferGraphics.setColor (s.colour);
	    bufferGraphics.fillRect (s.x, s.y, s.width, s.height);
	}
	for (int i = 0 ; i < players.size () ; i++)
	{
	    Player p = players.get (i);
	    bufferGraphics.drawImage (p.img [p.gunDir], p.x, p.y, null);

	    /* player stats
	    bufferGraphics.drawString ("Speed " + p.speed, p.x, p.y + 80);
	    bufferGraphics.drawString ("Poison " + p.poisonTime,p.x,p.y+100);
	    bufferGraphics.drawString ("BonusSpeed " + p.moveSpeedBonus,p.x,p.y+120);*/

	    // draw player hp
	    bufferGraphics.setColor (Color.red);
	    bufferGraphics.fillRect (p.x, p.y + p.height + 20, 50, 14);
	    bufferGraphics.setColor (Color.green);
	    bufferGraphics.fillRect (p.x + 1, p.y + p.height + 21, p.health >> 1, 12); // >> 1 divides by 2 and truncates
	}

	// Draws panel describing various game info
	bufferGraphics.setColor (Color.black);
	int alignX = 5;
	int panelY = game.SCREEN_HEIGHT - 90;
	for (int i = 0 ; i < players.size () ; i++)
	{
	    //if (players.get (i).human)
	    //{
	    bufferGraphics.drawString ("Player " + players.get (i).playerID, alignX + 200 * i, panelY);
	    bufferGraphics.drawString ("Team: " + players.get (i).teamID, alignX + 200 * i, panelY + 20);
	    bufferGraphics.drawString ("Lives: " + players.get (i).lives, alignX + 200 * i, panelY + 50);
	    bufferGraphics.drawString ("Weapon: " + players.get (i).weapon.name, alignX + 200 * i, panelY + 70);
	    if (players.get (i).weapon.ammo == -1)
		bufferGraphics.drawString ("Ammo: N/A", alignX + 200 * i, panelY + 90);
	    else
		bufferGraphics.drawString ("Ammo: " + players.get (i).weapon.ammo, alignX + 200 * i, panelY + 90);
	    //}
	}

	// Draws buffer onto screen
	g.drawImage (buffer, 0, 0, null);


	try
	{
	    Thread.sleep (20);
	}
	catch (Exception e)
	{
	}

	repaint ();
    }
}
