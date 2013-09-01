import java.awt.*;

public class Weapon
{
    String name;
    boolean available;
    int ammo, recoil, speed, damage;
    int bulletWidth, bulletHeight;
    int ammoIncrease;
    String ammoName;

    Bonus effect;
    Color colour;


    public Weapon (String name, boolean avail, String ammoName, int ammo, int ammoIncrease, int recoil, int speed, int damage, int bWidth, int bHeight, Bonus effect, Color colour)
    {
	this.name = name;
	this.available = avail;
	this.ammoName = ammoName;
	this.ammo = ammo;
	this.ammoIncrease = ammoIncrease;
	this.recoil = recoil;
	this.speed = speed;
	this.damage = damage;
	this.bulletWidth = bWidth;
	this.bulletHeight = bHeight;
	this.effect = effect;
	this.colour = colour;
    }


    public Weapon clone ()
    {
	return new Weapon (name, available, ammoName, ammo, ammoIncrease, recoil, speed, damage, bulletWidth, bulletHeight, effect, colour);
    }
}
