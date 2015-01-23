package org.plasmarobotics.team2403.robot;

import edu.wpi.first.wpilibj.Talon;

import org.plasmarobotics.team2403.robot.PlasmaGamepad;

public class Drive {
	
	public final double MAX_SPEED = 0.8;
	public final int GAMEPAD_PORT =0;
	public final int MULTIPLIER = -1;
	public final double COMP = 0.1;
	
	public final int TALON_R1 =5; //CHANNELS for talons
	public final int TALON_R2 =3;
	public final int TALON_L1 =4;
	public final int TALON_L2 =2;
	public final int TALON_M1 =0;
	public final int TALON_M2 =1;
	
	Talon talon_r1;
	Talon talon_r2;
	Talon talon_l1;
	Talon talon_l2;
	Talon talon_m1;
	Talon talon_m2;
	
	
	PlasmaGamepad gamepad = new PlasmaGamepad(1);
	
	/*
	 * binds talons
	 * creates new gamepad
	 * 
	 * @author cathy
	 */
	
	public Drive (){
		talon_r1 = new Talon(TALON_R1);
		talon_r2 = new Talon(TALON_R2);
		talon_l1 = new Talon(TALON_L1);
		talon_l2 = new Talon(TALON_L2);
		talon_m1 = new Talon(TALON_M1);
		talon_m2 = new Talon(TALON_M2);
		
		gamepad = new PlasmaGamepad(GAMEPAD_PORT);
		
	}
	
	/*
	 * updates input from gamepad during teleop
	 * compensates for turns W/O GYRO
	 * 
	 * mostly finished/compensated
	 * 
	 * @author cathy
	 */
	public void update(){
		double x = gamepad.getLeftJoystickXAxis()*MAX_SPEED;
		double y = gamepad.getLeftJoystickYAxis()*MAX_SPEED;
		double z = gamepad.getRightJoystickXAxis()*MAX_SPEED;
		
		if(Math.abs(x) > COMP){
			talon_m1.set(x*MULTIPLIER);
			talon_m2.set(x*MULTIPLIER);
		}
		else{
			talon_m1.set(0);
			talon_m2.set(0);
		}
		
		if (y<-COMP){ //it's moving forward/backward already
			if (z <-COMP){ //turn left
				
				talon_r1.set(y*MULTIPLIER);
				talon_r2.set(y*MULTIPLIER);
				talon_l1.set(y-Math.abs(z));
				talon_l2.set(y-Math.abs(z));
			
			} else if (z>COMP){ //turn right
				talon_l1.set(y);
				talon_l2.set(y);
				talon_r1.set((y-Math.abs(z))*MULTIPLIER);
				talon_r2.set((y-Math.abs(z))*MULTIPLIER);
			} else { //no turn
				talon_r1.set(y*MULTIPLIER);
				talon_r2.set(y*MULTIPLIER);
				talon_l1.set(y);
				talon_l2.set(y);
			}
		} else if (y>COMP){
			if (z >COMP){ //turn left
				
				talon_r1.set(y*MULTIPLIER);
				talon_r2.set(y*MULTIPLIER);
				talon_l1.set(y-Math.abs(z));
				talon_l2.set(y-Math.abs(z));
			
			} else if (z<-COMP){ //turn right
				talon_l1.set(y);
				talon_l2.set(y);
				talon_r1.set((y-Math.abs(z))*MULTIPLIER);
				talon_r2.set((y-Math.abs(z))*MULTIPLIER);
			} else { //no turn
				talon_r1.set(y*MULTIPLIER);
				talon_r2.set(y*MULTIPLIER);
				talon_l1.set(y);
				talon_l2.set(y);
			}
			
		} else { //idle turn
			talon_l1.set(z);
			talon_l2.set(z);
			talon_r1.set(z);
			talon_r2.set(z);
		}
		
		
		
	}
	/*
	 * tank drive that is implemented by GyroAuto
	 * 
	 * @param right
	 * @param left
	 * @author nic
	 */
	public void ManualTank(double left, double right){
		talon_r1.set(right * MULTIPLIER);
		talon_r2.set(right * MULTIPLIER);
		talon_l1.set(left);
		talon_l2.set(left);
	}
	
	public void FieldOrientedDrive(double x, double y, double z, double gyroAng){
		
		double contAng;
		double ang;
		double forward = 0;
		double sideways = 0;
		double magnitude;
		if(Math.abs(y) > COMP || Math.abs(x) > COMP){
			//radians
			contAng = Math.atan(x/y);
			if(y < 0){
				contAng += Math.PI;
			}
			ang = contAng - Math.toRadians(gyroAng);
			while(ang > Math.PI){
				ang -= Math.PI*2;
			}
			while(ang < -Math.PI){
				ang += Math.PI*2;
			}
			magnitude = Math.sqrt(y*y + x*x);
			forward = MAX_SPEED * magnitude * Math.cos(ang);
			sideways = MAX_SPEED * magnitude * Math.sin(ang);
			
			
			talon_r1.set(forward + (.5 * z));
			talon_r2.set(forward + (.5 * z));
			talon_l1.set(forward * MULTIPLIER + (.5 * z));
			talon_l2.set(forward * MULTIPLIER + (.5 * z));
			
			
			
			talon_m1.set(sideways * MULTIPLIER);
			talon_m2.set(sideways * MULTIPLIER);
		}
		else{
			talon_r1.set(z);
			talon_r2.set(z);
			talon_l1.set(z);
			talon_l2.set(z);
			talon_m1.set(0);
			talon_m2.set(0);
		}
		
	}
	
	

}
