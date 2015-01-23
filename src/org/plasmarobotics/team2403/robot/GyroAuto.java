package org.plasmarobotics.team2403.robot;

import edu.wpi.first.wpilibj.Gyro;

public class GyroAuto {
	
	Drive autoDrive;
	
	public final int GYRO_CHANNEL = 1;
	
	double target;
	double loc;
	
	Gyro gyro;
	
	public GyroAuto(Gyro gy, Drive dr){
		
		autoDrive = dr;
		gyro = gy;
		target = 0;
	}
	
	public void Reset(){
		gyro.reset();
	}
	
	public void SetAngle(double ang){
		
		if(ang < 360 && ang >=0)
			target = ang;
		
	}
	
	public void RunAuto(double speed){
		loc = gyro.getAngle();
		while(loc > 180){
			loc -= 360;
		}
		while(loc < -180){
			loc += 360;
		}
		
		if(loc - target <= 3 && loc - target >= -3){
			autoDrive.ManualTank(0,0);
		}
		else if(loc-target < 0){
			autoDrive.ManualTank(speed, -speed);
		}
		else{
			autoDrive.ManualTank(-speed, speed);
		}
				
		
	}
}
