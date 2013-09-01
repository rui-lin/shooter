import java.util.*;

public class BonusManager extends Thread
{
    private List < Bonus > bonuses;
    private List < Label > labels;
    private Shooter game;

    public BonusManager (Shooter game)
    {
	bonuses = Collections.synchronizedList (new ArrayList < Bonus > ());
	labels = Collections.synchronizedList (new ArrayList < Label > ());
	this.game = game;
    }


    public void addRandomBonus ()
    {
	addBonus ((int) (Math.random () * (game.SCREEN_WIDTH - Bonus.SIZE)),
		(int) (Math.random () * (game.SCREEN_HEIGHT - Bonus.SIZE)));
    }


    public void addBonus (int x, int y)
    {
	Bonus newBonus;

	switch ((int) Math.round (Math.random () * 3))
	{
	    case 0:
		int wIndex = (int) (Math.random () * (Player.DEFAULT_WEAPONS.size () - 1) + 1);
		newBonus = new WeaponBonus (wIndex,
			Player.DEFAULT_WEAPONS.get (wIndex).ammoIncrease,
			Player.DEFAULT_WEAPONS.get (wIndex).ammoName);
		break;
	    case 1:
		newBonus = new HealthBonus ((int) (Math.random () * 79 + 20));
		break;
	    case 2:
		int effect = EffectBonus.randomEffect ();
		newBonus = new EffectBonus (effect, EffectBonus.getDefaultMagnitude (effect));
		break;
	    case 3:
		int stat = StatBonus.randomStat ();
		newBonus = new StatBonus (stat, StatBonus.getDefaultMagnitude (stat));
		break;
	    default:
		newBonus = new HealthBonus (0);
		break;
	}

	newBonus.x = x;
	newBonus.y = y;
	bonuses.add (newBonus);
    }


    public void addLabel (String text, int x, int y, int duration)
    {
	labels.add (new Label (text, x, y, duration));
    }


    public List < Bonus > getBonuses ()
    {
	return bonuses;
    }


    public List < Label > getLabels ()
    {
	return labels;
    }


    public void removeBonus (int i)
    {
	bonuses.remove (i);
    }


    public void removeLabel (int i)
    {
	labels.remove (i);
    }


    public void run ()
    {
	List < Player > players = game.playerManager.getPlayers ();
	while (true)
	{
	    // Updates Bonuses
	    int lowerBound, upperBound;
	    lowerBound = 300 + game.playerManager.getMaxPlayers () * 35;
	    upperBound = 700 - lowerBound;
	    if ((int) (Math.random () * upperBound + lowerBound) == lowerBound)
		addRandomBonus ();

	    // Updates Labels
	    for (int i = 0 ; i < labels.size () ; i++)
	    {
		if (labels.get (i).duration <= 0)
		{
		    labels.remove (i);
		    continue;
		}
		if (labels.get (i).text.startsWith (" ")) // supposed to follow player
		{
		    labels.get (i).x = players.get (Integer.parseInt (labels.get (i).text)).x;
		    labels.get (i).y = players.get (Integer.parseInt (labels.get (i).text)).y + 20;
		}
		labels.get (i).duration--;
	    }
	    try
	    {
		Thread.sleep (20);
	    }
	    catch (Exception e)
	    {
	    }
	}
    }
}


