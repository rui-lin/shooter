import java.awt.Image;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.*;
import java.io.*;

public class Player
{
    final static int UP = 0;
    final static int DOWN = 1;
    final static int LEFT = 2;
    final static int RIGHT = 3;
    final static int SHOOT = 4;
    final static int PREVIOUS_WEAPON = 5;
    final static int NEXT_WEAPON = 6;

    static int[] [] DEFAULT_KEYS = {
	    {87, 83, 65, 68, 32, 81, 69},
	    {89, 72, 71, 74, 44, 84, 85},
	    {38, 40, 37, 39, 17, 47, 16},
	    {101, 102, 99, 105, 10, 98, 104}
	};

    final static ArrayList < Weapon > DEFAULT_WEAPONS;
    static
    {
	DEFAULT_WEAPONS = new ArrayList < Weapon > ();
	DEFAULT_WEAPONS.add (new Weapon ("Basic Cannon", true, "Bullets", -1, -1, 8, 8, 10, 2, 5, new NullBonus (), Color.BLACK));
	DEFAULT_WEAPONS.add (new Weapon ("Blob Gun", true, "Blobs", 20, 7, 12, 6, 20, 15, 15, new EffectBonus (EffectBonus.POISON, 200), new Color (0, 255, 0)));
	DEFAULT_WEAPONS.add (new Weapon ("Machine Gun", true, "Bullets", 30, 7, 1, 25, 5, 3, 5, new NullBonus (), Color.BLACK));
	DEFAULT_WEAPONS.add (new Weapon ("Rocket Launcher", true, "Rockets", 15, 3, 20, 15, 40, 8, 15, new NullBonus (), new Color (30, 0, 108)));
	DEFAULT_WEAPONS.add (new Weapon ("Sniper", false, "Snipes", 10, 2, 20, 50, 30, 1, 1000, new NullBonus (), Color.WHITE));
	DEFAULT_WEAPONS.add (new Weapon ("Mine", false, "Mine", 3, 1, 30, 0, 50, 30, 30, new NullBonus (), Color.RED));
	DEFAULT_WEAPONS.add (new Weapon ("Icicle Launcher", false, "Icicles", 5, 2, 30, 0, 0, 3, 500, new EffectBonus (EffectBonus.FREEZE, 50), new Color (190, 255, 255)));
	DEFAULT_WEAPONS.add (new Weapon ("Shovel", false, "Holes", 3, 2, 40, 0, 34, 20, 60, new EffectBonus (EffectBonus.POISON, 300), new Color (70, 60, 45)));
	DEFAULT_WEAPONS.add (new Weapon ("Waller", false, "Bricks", 20, 5, 15, 0, 0, 50, 12, new NullBonus ()  /*changesoon*/, new Color (231, 231, 231)));
    }


    int playerID;
    int teamID; // 0 is independant
    boolean human;
    int lives;
    int health;
    int maxhealth;
    int x, y;
    int width, height;
    int speed, timeInRecoil;
    boolean up, down, left, right;
    int moveSpeedBonus, recoilSpeedBonus;
    int bulletSpeedBonus, bulletDamageBonus, bulletHpDrainBonus;
    int frozenTime, poisonTime;
    boolean moving,attacking;
    int enragedtime;

    int keys[];
    Weapon weapon; // currently held weapon
    BufferedImage[] img;

    public int gunDir; // not effective

    List < Weapon > weapons; // all weapons player has.

    // default constructor
    public Player ()
    {
    }


    // creates a player that have certain lives and health
    public Player (int lives, int maxhealth, BufferedImage imgLeft, boolean human)
    {
	resetComplete ();

	this.lives = lives;
	this.maxhealth = maxhealth;
	this.human = human;
	this.teamID = 0;
	this.playerID = 0;

	img = new BufferedImage [4];
	img [LEFT] = imgLeft;
	img [UP] = rotate (img [LEFT], 90);
	img [RIGHT] = rotate (img [LEFT], 180);
	img [DOWN] = rotate (img [LEFT], 270);
    }


    // creates a human player with certain playerID and keys
    public Player (int playerID, int defaultKeys, BufferedImage imgLeft)
    {
	img = new BufferedImage [4];

	if (defaultKeys >= 0 && defaultKeys < DEFAULT_KEYS.length)
	    keys = DEFAULT_KEYS [defaultKeys];
	else
	{
	    keys = new int [7];
	    Arrays.fill (keys, 0);
	}

	img [LEFT] = imgLeft;
	img [UP] = rotate (img [LEFT], 90);
	img [RIGHT] = rotate (img [LEFT], 180);
	img [DOWN] = rotate (img [LEFT], 270);

	maxhealth = 100;
	this.playerID = playerID;
	teamID = 0;
	human = true;
	resetComplete ();
    }


    // Rotates an image by a certain amount of degrees
    public final static BufferedImage rotate (BufferedImage oldimg, int degrees)
    {
	AffineTransform transform = new AffineTransform ();
	transform.rotate (Math.toRadians (degrees), oldimg.getWidth () / 2, oldimg.getHeight () / 2);
	AffineTransformOp op = new AffineTransformOp (transform, AffineTransformOp.TYPE_BILINEAR);
	return op.filter (oldimg, null);
    }


    // images are not cloned. there is no need.
    public Player clone ()
    {
	Player playerClone = new Player ();
	playerClone.keys = (int[]) keys.clone ();
	playerClone.human = human;
	playerClone.img = img;

	playerClone.resetComplete ();

	return playerClone;
    }


    public void reset ()
    {
	lives--;
	health = maxhealth;
	x = 0;
	y = 0;
	timeInRecoil = 0;
	moveSpeedBonus = 0;
	bulletSpeedBonus = 0;
	bulletDamageBonus = 0;
	recoilSpeedBonus = 0;
	bulletHpDrainBonus = 0;
	frozenTime = 0;
	poisonTime = 0;
	attacking = false;
	moving=true;
	enragedtime=0;
    }


    public void resetComplete ()
    {
	x = 0;
	y = 0;
	lives = 3;
	health = maxhealth;
	width = 52 - 11;
	height = 49 - 11;
	up = false;
	down = false;
	left = false;
	right = false;
	speed = 6;
	timeInRecoil = 0;
	moveSpeedBonus = 0;
	bulletSpeedBonus = 0;
	bulletDamageBonus = 0;
	recoilSpeedBonus = 0;
	bulletHpDrainBonus = 0;
	frozenTime = 0;
	poisonTime = 0;
	attacking=false;
	moving=true;
	enragedtime=0;
	weapons = new ArrayList < Weapon > ();
	for (int i = 0 ; i < DEFAULT_WEAPONS.size () ; i++)
	    weapons.add (DEFAULT_WEAPONS.get (i).clone ());
	weapon = weapons.get (0);
    }
}


