import java.util.*;;

public class SolidManager extends Thread
{
    private List <Solid>solids;
    Shooter game;

    public SolidManager (Shooter game)
    {
      solids = Collections.synchronizedList(new ArrayList<Solid>());
 this.game = game;
    }


    public List<Solid> getSolids ()
    {
 return solids;
    }


    public void run ()
    {
 List <Projectile> proj= game.projectileManager.getProjectiles ();;
 Solid s;

 while (true)
 {
     for (int i = 0 ; i < solids.size () ; i++)
     {
  s = solids.get (i);
  if (s.health <= 0)
  {
      if ((int) Math.random () * 4 == 0)
   game.bonusManager.addBonus (s.x + (int) (s.width / 2) - (int) (Bonus.SIZE / 2), s.y + (int) (s.y / 2) - (int) (Bonus.SIZE / 2));
      solids.remove (s);
  }
  else
  {
    // check collision with projectiles
      for (int j = 0 ; j < proj.size () ; j++)
      {
   if (game.intersects (proj.get(j).x, proj.get(j).width, proj.get(j).y, proj.get(j).height,
        s.x, s.width, s.y, s.height))
       s.health -= Math.max (0, proj.get(j).damage + proj.get(j).owner.bulletDamageBonus);
   game.projectileManager.removeProjectile (j);
      }
  }
     }
     try{Thread.sleep (20);}catch(Exception e){}
 }
    }
}


