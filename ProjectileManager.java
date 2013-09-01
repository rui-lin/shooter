import java.util.*;;

public class ProjectileManager extends Thread
{
  private Shooter game;
  private List <Projectile>projectiles;
  
  public ProjectileManager (Shooter game)
  {
    projectiles=Collections.synchronizedList(new ArrayList<Projectile>());
    this.game = game;
  }
  
  
  public void addProjectile (Projectile p){
    projectiles.add(p);
  }
  
  public List<Projectile> getProjectiles ()
  {
    return projectiles;
  }
  
  
  public void removeProjectile (int index)
  {
    projectiles.remove (index);
  }
  
  
  public void run ()
  {
    List <Player> players = game.playerManager.getPlayers(); 
    Projectile p;
    
    while (true)
    {
      for (int i = 0 ; i < projectiles.size () ; i++)
      {
        p = projectiles.get (i);
                
        // Moves projectile
        p.x += p.xMove;
        p.y -= p.yMove;
        
        // Checks if projectile is offscreen
        if (p.x > game.SCREEN_WIDTH || p.x + p.width < 0 ||
            p.y > game.SCREEN_HEIGHT || projectiles.get(i).y - p.height < 0)
          projectiles.remove (i);
        else
        {
          // Checks collision with each player
          for (int j = 0 ; j < players.size () ; j++)
          {
            if (p.owner != players.get(j)){
              if (game.intersects (players.get(j).x, players.get(j).width, players.get(j).y, players.get(j).height,
                                   p.x, p.width, p.y, p.height))
              {
                game.playerManager.damagePlayer(j, p.damage);
                p.effect.activate (players.get(j), game.bonusManager);
                projectiles.remove(p);
              }
            }
          }
        }
      }
      try{
        Thread.sleep (20);
      }catch(Exception e)
      {
      }
    }
  }
}


