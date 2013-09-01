import java.awt.*;

public class Projectile
{
    int x, y;
    int width, height;
    int xMove, yMove;
    int damage;

    Color colour;
    Bonus effect;
    Player owner;

    public Projectile (Player player, Weapon weapon)
    {
	this.owner = player;
	this.damage = weapon.damage + player.bulletDamageBonus;
	this.effect = weapon.effect;
	this.colour = weapon.colour;

	switch (player.gunDir)
	{
	    case Player.UP:
		width = weapon.bulletWidth;
		height = weapon.bulletHeight;
		xMove = 0;
		yMove = (weapon.speed + player.bulletSpeedBonus);
		x = player.x + (int) (player.width / 2) - (int) (width / 2);
		y = player.y - height;
		break;
	    case Player.DOWN:
		width = weapon.bulletWidth;
		height = weapon.bulletHeight;
		xMove = 0;
		yMove = -(weapon.speed + player.bulletSpeedBonus);
		x = player.x + (int) (player.width / 2) - (int) (width / 2);
		y = player.y + player.height;
		break;
	    case Player.LEFT:
		width = weapon.bulletHeight;
		height = weapon.bulletWidth;
		xMove = -(weapon.speed + player.bulletSpeedBonus);
		yMove = 0;
		x = player.x - width;
		y = player.y + (int) (player.height / 2) - (int) (height / 2);
		break;
	    case Player.RIGHT:
		width = weapon.bulletHeight;
		height = weapon.bulletWidth;
		xMove = (weapon.speed + player.bulletSpeedBonus);
		yMove = 0;
		x = player.x + player.width;
		y = player.y + (int) (player.height / 2) - (int) (height / 2);
		break;
	}
	if (weapon.speed == 0) // if weapon speed 0, speed bonus cannot increase that
	{
	    xMove = 0;
	    yMove = 0;
	}
    }
}
