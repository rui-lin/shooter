import java.awt.*;

public class Solid
{
    int health;
    int width, height;
    int x, y;

    Color colour;

    public Solid (int health, int width, int height, int x, int y, Color colour)
    {
	this.health = health;
	this.width = width;
	this.height = height;
	this.x = x;
	this.y = y;
	this.colour = colour;
    }
}
