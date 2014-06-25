package me.johnnywoof.check.moving;

import me.johnnywoof.Setting;
import me.johnnywoof.Variables;
import me.johnnywoof.check.Check;
import me.johnnywoof.check.CheckType;
import me.johnnywoof.check.DetectionType;
import me.johnnywoof.event.ViolationTriggeredEvent;
import me.johnnywoof.util.MoveData;
import me.johnnywoof.util.Utils;
import me.johnnywoof.util.XYZ;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class SurvivalFly extends Check{

	public SurvivalFly(Variables vars, CheckType ct) {
		super(vars, ct, DetectionType.MOVING);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int runMoveCheck(Player p, Location to, Location from, double yd, double xs, double zs, MoveData movedata, boolean up, boolean inwater, boolean onladder, XYZ lg){
		
			//Start survival fly checks
			if(p.isOnGround() || p.isInsideVehicle() || inwater || (p.isFlying()) || onladder){
				
				this.vars.lastGround.put(p.getName(), new XYZ(from));
				
			}else{
				
				if(!p.getAllowFlight() && !inwater && !onladder){//Ignore users that are allowed to fly. Doesn't count for the hack fly!
					
					if(up){
						
						double ydis = Math.abs(lg.y - to.getY());
						
						if(ydis > this.getMaxHight(p, movedata)){
							
							int id = this.vars.raiseViolationLevel(CheckType.FLY, p);
							
							ViolationTriggeredEvent vte = new ViolationTriggeredEvent(id, CheckType.FLY, p);
							
							Bukkit.getServer().getPluginManager().callEvent(vte);
							
							if(!vte.isCancelled()){
							
								if(id != 0){
									
									String message = Setting.flymes;
									
									message = message.replaceAll(".name.", ChatColor.YELLOW + "" + p.getName() + "" + ChatColor.GREEN);
									message = message.replaceAll(".vl.", id + "");

									Utils.messageAdmins(message);
									
								}
								return 1;
							
							}
							
						}
						
					}
					
				}
				
			}
			
			//End survival fly check
		
		return 0;
		
	}
	
	private double getMaxHight(Player p, MoveData md){
		
		double d = 0;
		
		if(p.hasPotionEffect(PotionEffectType.JUMP)){
			
			int level = Utils.getPotionEffectLevel(p, PotionEffectType.JUMP);
			
			if(level == 1){
				
				d = 1.9;
				
			}else if(level == 2){
				
				d = 2.7;
				
			}else if(level == 3){
				
				d = 3.36;
				
			}else if(level == 4){
				
				d = 4.22;
				
			}else if(level == 5){
				
				d = 5.16;
				
			}else if(level == 6){
				
				d = 6.19;
				
			}else if(level == 7){
				
				d = 7.31;
				
			}else if(level == 8){
				
				d = 8.5;
				
			}else if(level == 9){
				
				d = 9.76;
				
			}else if(level == 10){
				
				d = 11.1;
				
			}else{
				
				d = (level) + 1;
				
			}
			
			d = d + 1.35;
			
		}else{
			
			d = 1.35;
			
		}
		
		if(md.yda != 0 && (System.currentTimeMillis() < md.velexpirey)){
			
			d = d + md.yda;
			
		}
		
		return d;
		
	}

}
