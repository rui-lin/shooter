import java.applet.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.net.URL;

import javax.swing.*;
import javax.imageio.*;

public class Shooter extends JApplet // implements KeyListener
{
    // Add debugging

    static short SCREEN_WIDTH = 800;
    static short SCREEN_HEIGHT = 600;
    static String HOME_PATH;
    final static short INTRO = 0;
    final static short CONTROLS = 1;
    final static short INGAME = 2;

    static BufferedImage[] PLAYER_IMG;
    final static String CONFIG_FILE = "config.ini";
    final static String[] PLAYER_IMG_PATH = {
	"/images/player1.BMP",
	"/images/player2.BMP",
	"/images/player3.BMP",
	"/images/player4.BMP",
	"/images/player5.BMP"};

    final static Font titleFont = new Font ("Century Gothic", Font.BOLD, 24);

    short gameState = CONTROLS;

    int keyCodeDown;

    JPanel gameScreen;
    MainMenu mainMenu;
    ControlsMenu controlsMenu;

    //Player[] players;
    PlayerManager playerManager;
    ProjectileManager projectileManager;
    SolidManager solidManager;
    BonusManager bonusManager;

    public void init ()
    {
	try
	{
	    SwingUtilities.invokeAndWait (new Runnable ()
	    {
		public void run ()
		{
		    setup ();
		}
	    }
	    );
	}
	catch (Exception e)
	{
	}
    }


    public void start ()
    {
    }


    public void setup ()
    {
	// get size of screen
	SCREEN_WIDTH = (short) getWidth ();
	SCREEN_HEIGHT = (short) getHeight ();
	HOME_PATH = getCodeBase () + "Shooter.jar!";

	// sets up GUI
	createGUI ();

	// loads pictures
	PLAYER_IMG = new BufferedImage [PLAYER_IMG_PATH.length];
	for (int i = 0 ; i < PLAYER_IMG_PATH.length ; i++)
	{
	    try
	    {
		PLAYER_IMG [i] = ImageIO.read (new URL ("jar:" + HOME_PATH + PLAYER_IMG_PATH [i]));
	    }
	    catch (Exception e)
	    {
		System.out.println ("unable to load a picture");
	    }
	}

	// sets up players
	/*players = new Player [4];

	try
	{
	    for (int i = 0 ; i < 4 ; i++)
		players [i] = new Player (i, i, PLAYER_IMG [i]);
	}
	catch (Exception e)
	{
	}*/

	// loads controls
	//controlsMenu.loadControls ();

	// Show main menu
	showMainMenu ();
    }


    public void createGUI ()
    {
	// Sets look and feel
	/*try
	{
	    UIManager.setLookAndFeel ("javax.swing.plaf.metal.MetalLookAndFeel");
	}
	catch (Exception e)
	{
	    System.out.println ("unable to change look and feel:");
	    e.printStackTrace ();
	}*/

	// Create menus
	mainMenu = new MainMenu (this);
	controlsMenu = new ControlsMenu (this);
	gameScreen = new GameScreen (this);
    }


    public void showMainMenu ()
    {
	this.getContentPane ().removeAll ();
	mainMenu.show ();
	this.repaint ();
    }


    public void showControlsMenu ()
    {
	this.getContentPane ().removeAll ();
	controlsMenu.show ();
	this.repaint ();
    }


    public void showGameScreen ()
    {
	this.getContentPane ().removeAll ();
	this.getContentPane ().add (gameScreen);

	// Sets up the game
	solidManager = new SolidManager (this);
	projectileManager = new ProjectileManager (this);
	bonusManager = new BonusManager (this);
	playerManager = new PlayerManager (this);

	// hack
	Player godplayer = new Player (1, 0, PLAYER_IMG [0]);
	godplayer.lives = 3;
	// end hack

	playerManager.addPlayer (godplayer, true, 1);
	playerManager.addPlayer (new Player (2, 2, PLAYER_IMG [1]), true, 1);
	playerManager.addPlayer (new Player (3, 0, PLAYER_IMG [2]), false, 2);
	playerManager.addPlayer (new Player (4, 0, PLAYER_IMG [3]), false, 2);
	/*playerManager.addPlayer (players [2].clone (), false);
	playerManager.addPlayer (players [1].clone (), false);
	playerManager.addPlayer (players [3].clone (), false);
	playerManager.addPlayer (players [2].clone (), false);
	playerManager.addPlayer (players [1].clone (), false);
	playerManager.addPlayer (players [3].clone (), false);
	playerManager.addPlayer (players [2].clone (), false);*/


	// Starts the game;
	playerManager.start ();
	solidManager.start ();
	projectileManager.start ();
	bonusManager.start ();

	this.validate ();
	gameScreen.requestFocus ();
    }


    public static boolean intersects (int x1, int w1, int y1, int h1, int x2, int w2, int y2, int h2)
    {
	if (x1 > x2 + w2 || x2 > x1 + w1 || y1 > y2 + h2 || y2 > y1 + h1)
	    return false;

	return true;
    }
} // Shooter class


