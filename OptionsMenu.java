import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class OptionsMenu implements ActionListener
{
    JPanel panel;
    Shooter game;

    public OptionsMenu (Shooter game)
    {
	this.game = game;
	panel = new JPanel ();

	/* Creates top pane */
	JPanel addPlayersPane = new JPanel (new GridLayout (4, 1, 50, 50));
	addPlayersPane.setBorder (BorderFactory.createCompoundBorder (BorderFactory.createEtchedBorder (), BorderFactory.createEmptyBorder (50, 50, 50, 50)));

    }


    public void actionPerformed (ActionEvent e)
    {
    }
}
