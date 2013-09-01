public class EffectBonus extends Bonus
{
    public static final int FREEZE = 1;
    public static final int POISON = 2;
    //public static final int HPDRAIN = 3;
    public static final int SLOW = 3;
    //public static final int WALL = 4;
    public static final int CHICKEN_SOUP = 4;
    public static final int ANTIDOTE = 5;


    int effect;
    int magnitude;

    public EffectBonus (int effect, int magnitude)
    {
	this.effect = effect;
	this.magnitude = magnitude;
    }


    public void activate (Player p, BonusManager lm)
    {
	switch (effect)
	{
	    case FREEZE:
		p.frozenTime += magnitude;
		if (p.frozenTime <= 0)
		    lm.addLabel ("Chicken Soup Thawed Player", p.x, p.y + p.height + 25, 20);
		else if (p.frozenTime < magnitude)
		    lm.addLabel ("Chicken Soup Partially Thawed Player", p.x, p.y + p.height + 25, 20);
		else if (magnitude > 50)
		    lm.addLabel ("Player Frozen Solid.", p.x, p.y + p.height + 25, 20);
		else
		    lm.addLabel ("Player Frozen.", p.x, p.y + p.height + 25, 20);
		break;
	    case POISON:
		p.poisonTime += magnitude;
		if (p.poisonTime <= 0)
		    lm.addLabel ("Player's Vaccine Prevented Poison", p.x, p.y + p.height + 25, 20);
		else if (p.poisonTime < magnitude)
		    lm.addLabel ("Player's Vaccine Weakened Poison's Effect", p.x, p.y + p.height + 25, 20);
		else if (magnitude > 200)
		    lm.addLabel ("Toxic Waste Trap!", p.x, p.y + p.height + 25, 20);
		else
		    lm.addLabel ("Player Poisoned.", p.x, p.y + p.height + 25, 20);
		break;
	    case SLOW:
		p.moveSpeedBonus -= magnitude;
		lm.addLabel ("Sludge Trap!", p.x, p.y + p.height + 25, 20);
		break;
	    case CHICKEN_SOUP:
		p.frozenTime -= magnitude;
		lm.addLabel ("Player Drank Chicken Soup", p.x, p.y + p.height + 25, 20);
		break;
	    case ANTIDOTE:
		p.poisonTime -= magnitude;
		if (p.poisonTime > 0)
		    lm.addLabel ("Antidote Weakened Poison's Effect", p.x, p.y + p.height + 25, 20);
		else if (p.poisonTime <= 0)
		    if (p.poisonTime + magnitude > 0)
			lm.addLabel ("Antidote Cured Poison", p.x, p.y + p.height + 25, 20);
		    else
			lm.addLabel ("Vaccine Taken.", p.x, p.y + p.height + 25, 20);
		break;
	}
    }


    public static int randomEffect ()
    {
	int effect = (int) (Math.random () * 4 + 1);
	if (effect == POISON || effect == FREEZE || effect == SLOW)
	    if (Math.random () >= 0.5)
		return randomEffect ();
	return effect;
    }


    public static int getDefaultMagnitude (int effect)
    {
	switch (effect)
	{
	    case FREEZE:
		return 100;
	    case POISON:
		return 600;
	    case SLOW:
		return 2;
	    case CHICKEN_SOUP:
		return 60;
	    case ANTIDOTE:
		return 400;
	    default:
		return 0;
	}
    }
}


