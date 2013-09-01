public class HealthBonus extends Bonus
{
    int health;

    public HealthBonus (int health)
    {
	this.health = health;
    }


    public void activate (Player p, BonusManager lm)
    {
	p.health += health;
	if (p.health > p.maxhealth)
	    p.health = p.maxhealth;

	lm.addLabel ("Healed " + health + " HP.", p.x, p.y, 20);
    }
}
