public class StatBonus extends Bonus
{
    public static final int PLAYER_SPEED_UP = 1;
    public static final int BULLET_SPEED_UP = 2;
    public static final int RECOIL_TIME_DOWN = 3;
    public static final int BULLET_DAMAGE_UP = 4;
    public static final int BULLET_DAMAGE_DOWN = 5;

    int effect;
    int magnitude;

    public StatBonus (int effect, int magnitude)
    {
 this.effect = effect;
 this.magnitude = magnitude;
    }


    public void activate (Player p, BonusManager lm)
    {
      switch (effect)
      {
        case PLAYER_SPEED_UP:
          p.moveSpeedBonus += magnitude;
          lm.addLabel ("Player Speed Increased", p.x, p.y +p.height+ 25, 20);
          break;
        case BULLET_SPEED_UP:
          p.bulletSpeedBonus += magnitude;
          lm.addLabel ("Bullet Speed Increased", p.x, p.y +p.height+ 25, 20);
          break;
        case BULLET_DAMAGE_UP:
          p.bulletDamageBonus += magnitude;
          lm.addLabel ("Bullet Damage Increased", p.x, p.y +p.height+ 25, 20);
          break;
        case BULLET_DAMAGE_DOWN:
          p.bulletSpeedBonus -= magnitude;
          lm.addLabel ("Bullet Damage Decreased", p.x, p.y +p.height+ 25, 20);
          break;
        case RECOIL_TIME_DOWN:
          p.recoilSpeedBonus += magnitude;
          lm.addLabel ("Recoil Time Decreased", p.x, p.y +p.height+ 25, 20);
          break;
      }
    }


    public static int randomStat ()
    {
      return (int) (Math.random () * 4 + 1);
    }


    public static int getDefaultMagnitude (int stat)
    {
      switch (stat)
      {
        case PLAYER_SPEED_UP:
          return 2;
        case BULLET_SPEED_UP:
          return 3;
        case RECOIL_TIME_DOWN:
          return 2;
        case BULLET_DAMAGE_UP:
          return 5;
        case BULLET_DAMAGE_DOWN:
          return 6;
        default:
          return 0;
      }
    }
}


