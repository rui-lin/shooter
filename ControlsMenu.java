import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class ControlsMenu implements ActionListener, KeyListener
{
    JButton[] setControlKeys;
    JLabel[] viewControlKeys;
    JRadioButton radio1, radio2, radio3, radio4;
    JButton btnBack;
    JPanel panel;
    Shooter game;

    int keyCodeDown;
    int selectedPlayer;

    public ControlsMenu (Shooter game)
    {
	this.game = game;
	panel = new JPanel ();

	/* Create left pane */
	JPanel choosePlayerPane = new JPanel (new GridLayout (12, 1, 0, 10));
	choosePlayerPane.setPreferredSize (new Dimension (150, 450));
	choosePlayerPane.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 50));
	ButtonGroup group = new ButtonGroup ();

	// create radio buttons
	radio1 = new JRadioButton ("Player 1", new ImageIcon ("Images/player1.bmp"), true);
	radio2 = new JRadioButton ("Player 2", new ImageIcon ("Images/player2.bmp"), false);
	radio3 = new JRadioButton ("Player 3", new ImageIcon ("Images/player3.bmp"), false);
	radio4 = new JRadioButton ("Player 4", new ImageIcon ("Images/player4.bmp"), false);

	// add action listener
	radio1.addActionListener (this);
	radio2.addActionListener (this);
	radio3.addActionListener (this);
	radio4.addActionListener (this);

	// group buttons logically
	group.add (radio1);
	group.add (radio2);
	group.add (radio3);
	group.add (radio4);

	// add to pane
	choosePlayerPane.add (new JLabel ("Set controls for: ", JLabel.LEFT));
	choosePlayerPane.add (radio1);
	choosePlayerPane.add (radio2);
	choosePlayerPane.add (radio3);
	choosePlayerPane.add (radio4);
	/****/

	/* Create right pane */
	JPanel setControlsPane = new JPanel (new GridLayout (7, 2, 60, 30));
	setControlsPane.setBorder (BorderFactory.createCompoundBorder (BorderFactory.createEtchedBorder (), BorderFactory.createEmptyBorder (20, 20, 20, 20)));
	setControlsPane.setPreferredSize (new Dimension (450, 450));
	setControlKeys = new JButton [7];
	viewControlKeys = new JLabel [7];
	viewControlKeys [0] = new JLabel ("Move up");
	viewControlKeys [1] = new JLabel ("Move down");
	viewControlKeys [2] = new JLabel ("Move left");
	viewControlKeys [3] = new JLabel ("Move right");
	viewControlKeys [4] = new JLabel ("Shoot");
	viewControlKeys [5] = new JLabel ("Previous Weapon");
	viewControlKeys [6] = new JLabel ("Next Weapon");
	for (int i = 0 ; i < 7 ; i++)
	{
	    setControlKeys [i] = new JButton ();
	    setControlKeys [i].addKeyListener (this);
	    setControlKeys [i].addActionListener (this);
	    setControlKeys [i].setActionCommand (new Integer (i).toString ());
	    setControlsPane.add (viewControlKeys [i]);
	    setControlsPane.add (setControlKeys [i]);
	}
	/****/

	/* Create top pane */
	JPanel header = new JPanel (new GridLayout (1, 2, 400, 0));
	header.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 50));
	JLabel title = new JLabel ("Controls");
	title.setFont (game.titleFont);
	btnBack = new JButton ("Back");
	btnBack.addActionListener (this);
	header.add (title);
	header.add (btnBack);
	/****/

	panel.setLayout (new BorderLayout ());

	JPanel content = new JPanel ();
	content.setBorder (BorderFactory.createEmptyBorder (30, 0, 0, 0));
	content.add (choosePlayerPane);
	content.add (setControlsPane);
	panel.add (header, BorderLayout.NORTH);
	panel.add (content, BorderLayout.CENTER);
	panel.setBorder (BorderFactory.createEmptyBorder (50, 50, 10, 50));
    }


    public static String getKeyText (int keyCode)
    {
	String keyText = KeyEvent.getKeyText (keyCode).toUpperCase ();

	if ((keyCode >= 65 && keyCode <= 90) || (keyCode >= 48 && keyCode <= 57)) // alphanumeric character
	    return keyText;
	else
	    return "[" + keyText + "]";
    }


    public void show ()
    {
	loadControls ();
	game.getContentPane ().add (panel);
	game.getContentPane ().validate ();
	selectedPlayer = 0;
	updateControls ();
    }


    public void hide ()
    {
	saveControls ();
	game.getContentPane ().remove (panel);
	game.getContentPane ().validate ();
    }


    public void updateControls ()
    {
	for (int i = 0 ; i < 7 ; i++)
	{
	    setControlKeys [i].setText (getKeyText (Player.DEFAULT_KEYS [selectedPlayer] [i]));
	    setControlKeys [i].setActionCommand (new Integer (i).toString ());
	}
    }


    // Sets a key for a player if possible
    public void setKeys (int playerID, int keyID, int keyCode)
    {
	if (keyID < 0 || keyID > 7)
	    return;
	if (playerID < 0 || playerID > Player.DEFAULT_KEYS.length)
	    return;

	Player.DEFAULT_KEYS [playerID] [keyID] = keyCode;
    }


    public void loadControls ()
    {
	try
	{
	    File configFile = new File (game.CONFIG_FILE);

	    if (configFile.createNewFile ()) // file did not exist
	    {
		saveControls (); // save the default control keys to file
	    }
	    else // file exists
	    {
		BufferedReader in = new BufferedReader (new FileReader (configFile));
		String line = "";
		String[] line2 = {"", "", ""};
		int playerID;

		playerID = -1;
		if (in.readLine ().trim ().equals ("<controls>"))
		{
		    while (!line.equals ("</controls>"))
		    {
			line = in.readLine ().trim ();
			if (line.equals ("<player>"))
			{
			    while (!line2 [0].equals ("</player>"))
			    {
				line2 = in.readLine ().trim ().split (" ");
				if (line2 [0].equals ("<id>"))
				    playerID = Integer.parseInt (line2 [1]);
				else if (line2 [0].equals ("<up>"))
				    setKeys (playerID, Player.UP, Integer.parseInt (line2 [1]));
				else if (line2 [0].equals ("<down>"))
				    setKeys (playerID, Player.DOWN, Integer.parseInt (line2 [1]));
				else if (line2 [0].equals ("<left>"))
				    setKeys (playerID, Player.LEFT, Integer.parseInt (line2 [1]));
				else if (line2 [0].equals ("<right>"))
				    setKeys (playerID, Player.RIGHT, Integer.parseInt (line2 [1]));
				else if (line2 [0].equals ("<shoot>"))
				    setKeys (playerID, Player.SHOOT, Integer.parseInt (line2 [1]));
				else if (line2 [0].equals ("<prev>"))
				    setKeys (playerID, Player.PREVIOUS_WEAPON, Integer.parseInt (line2 [1]));
				else if (line2 [0].equals ("<next>"))
				    setKeys (playerID, Player.NEXT_WEAPON, Integer.parseInt (line2 [1]));
			    }
			}
		    }
		}

	    }
	}
	catch (SecurityException e)  // denied permission to file system
	{
	}
	catch (IOException e)  // problem writing to file system
	{
	}
	catch (Exception e)  // anything else
	{
	}
    }


    public void saveControls ()
    {
	try
	{
	    PrintWriter out = new PrintWriter (new FileWriter (game.CONFIG_FILE));
	    int[] playerKeys;

	    // writes controls of each player to file
	    out.println ("<controls>");
	    for (int i = 0 ; i < 4 ; i++) //note5
	    {
		playerKeys = Player.DEFAULT_KEYS [i];
		out.println ("  <player>");
		out.println ("    <id> " + i + " </id>");
		out.println ("    <up> " + playerKeys [Player.UP] + " </up>");
		out.println ("    <down> " + playerKeys [Player.DOWN] + " </down>");
		out.println ("    <left> " + playerKeys [Player.LEFT] + " </left>");
		out.println ("    <right> " + playerKeys [Player.RIGHT] + " </right>");
		out.println ("    <shoot> " + playerKeys [Player.SHOOT] + " </shoot>");
		out.println ("    <prev> " + playerKeys [Player.PREVIOUS_WEAPON] + " </prev>");
		out.println ("    <next> " + playerKeys [Player.NEXT_WEAPON] + " </next>");
		out.println ("  </player>");
	    }
	    out.println ("</controls>");
	    out.close ();
	}
	catch (SecurityException e)  // denied permission to file system
	{
	}
	catch (IOException e)  // problem writing to file system
	{
	}
	catch (Exception e)  // anything else
	{
	}
    }


    public void actionPerformed (ActionEvent e)
    {
	String actionCommand = e.getActionCommand ();

	// Back button
	if (actionCommand == btnBack.getActionCommand ())
	    game.showMainMenu ();

	// Other buttons
	if (actionCommand == radio1.getActionCommand ())
	    selectedPlayer = 0;
	else if (actionCommand == radio2.getActionCommand ())
	    selectedPlayer = 1;
	else if (actionCommand == radio3.getActionCommand ())
	    selectedPlayer = 2;
	else if (actionCommand == radio4.getActionCommand ())
	    selectedPlayer = 3;
	else
	    for (int i = 0 ; i < 7 ; i++)
		if (actionCommand == setControlKeys [i].getActionCommand ())
		    Player.DEFAULT_KEYS [selectedPlayer] [i] = keyCodeDown;
		    
	updateControls ();
    }


    public void keyTyped (KeyEvent e)
    {
    }


    public void keyPressed (KeyEvent e)
    {
	keyCodeDown = e.getKeyCode ();
	e.consume ();
    }


    public void keyReleased (KeyEvent e)
    {
    }
}


