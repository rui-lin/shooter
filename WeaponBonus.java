public class WeaponBonus extends Bonus
{
    int weaponID;
    int ammo;
    String ammoName;


    public WeaponBonus (int weaponID, int ammo, String ammoName)
    {
 this.weaponID = weaponID;
 this.ammo = ammo;
 this.ammoName = ammoName;
    }


    public void activate (Player p, BonusManager lm)
    {
 Weapon w = p.weapons.get (weaponID);
 if (!w.available) // unlocks weapon if not unlocked
 {
     w.available = true;
     lm.addLabel (w.name + " Unlocked", p.x, p.y +p.height+ 25, 20);
 }
 else
 {
     if (w.name.equals("Waller") && Math.round(Math.random ()) == 0)
     {
  w.bulletWidth += 15;
  w.bulletHeight += 3;
  lm.addLabel ("Brick Size Enlarged", p.x, p.y + p.height+25, 20);
     }
     else
     {
  w.ammo += ammo;
  lm.addLabel (ammo + " " + ammoName + " [" + w.name + " ammo] Obtained.", p.x, p.y +p.height+ 25, 20);
     }
 }
    }
}
