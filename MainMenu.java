import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu implements ActionListener
{
    JPanel panel;
    JButton btnStart, btnControls, btnQuit;

    Shooter game;


    public MainMenu (Shooter game)
    {
	this.game = game;
	panel = new JPanel (new GridLayout (5, 5, 0, 30));

	// Create buttons
	btnStart = new JButton ("Start");
	btnControls = new JButton ("Controls");
	btnQuit = new JButton ("Quit");

	// Requests that this class listens to button clicks
	btnStart.addActionListener (this);
	btnControls.addActionListener (this);
	btnQuit.addActionListener (this);

	// Sets up the main menu
	panel.add (btnStart);
	panel.add (btnControls);
	panel.add (btnQuit);
	panel.setPreferredSize (new Dimension (400, 600));
	panel.setOpaque (false);
	panel.setBorder (BorderFactory.createEmptyBorder (100, (int) (game.SCREEN_WIDTH / 2.8), 100, (int) (game.SCREEN_WIDTH / 2.8)));

    }


    public void show ()
    {
	game.getContentPane ().add (panel);
	game.getContentPane ().validate ();
    }


    public void hide ()
    {
	game.getContentPane ().remove (panel);
	game.getContentPane ().validate ();
    }


    public void actionPerformed (ActionEvent e)
    {
	if (e.getActionCommand () == btnStart.getActionCommand ())
	{
	    game.showGameScreen ();
	}
	else if (e.getActionCommand () == btnControls.getActionCommand ())
	{
	    game.showControlsMenu ();
	}
	else if (e.getActionCommand () == btnQuit.getActionCommand ())
	{
	}
    }
}
