package org.plasmarobotics.team2403.robot;

//import java.util.ArrayList;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DriverStation;


import edu.wpi.first.wpilibj.interfaces.Accelerometer;

import org.plasmarobotics.team2403.robot.Drive;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	Drive drive;
	public final int GYRO_CHANNEL = 1;
	
	Gyro gyro;
	GyroAuto gyroAuto;
	DigitalInput limitSwitch;
	Accelerometer accel;
	
	double lastTimeRan;
	double xVelocity;
	double yVelocity;
	double timeDiff;
	
	boolean isFieldOriented = false;
	
	//auto stuff
	int state = 1;
	int step = 1;
	double time;
	
	
    public void robotInit() {
    	 gyro = new Gyro(GYRO_CHANNEL);
    	 drive = new Drive();
    	 gyroAuto = new GyroAuto(gyro, drive);
    	 gyroAuto.Reset();
    	 limitSwitch = new DigitalInput(0);
    	 accel = new BuiltInAccelerometer();
    	 
    	 lastTimeRan = 0;
    	 xVelocity = 0;
    	 yVelocity = 0;
    	 
    	 
    }
    
    public void autonomousInit(){
    	state = 2;
    	step = 1;
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	switch(state){
    	case 1:
    		switch(step){
    		case 1:
    			time = System.currentTimeMillis();
    			step++;
    			break;
    		case 2:
    			if(System.currentTimeMillis() - time < 5000){
    				
    				drive.FieldOrientedDrive(0, .4, 0, gyro.getAngle());
    				
    			}
    			else{
    				drive.FieldOrientedDrive(0, 0, 0, gyro.getAngle());
    			}
    			break;
    		}
    		break;
    	
    	case 2:
    		switch(step){
    		case 1:
    			time = System.currentTimeMillis();
    			step++;
    			break;
    		case 2:
    			if(System.currentTimeMillis() - time < 5000){
    				if(gyro.getAngle() > 93){
    					drive.FieldOrientedDrive(.4, 0, -.1, gyro.getAngle());
    				}
    				else if(gyro.getAngle() < 87){
    					drive.FieldOrientedDrive(.4, 0, .1, gyro.getAngle());
    				}
    				else{
    					drive.FieldOrientedDrive(.4, 0, 0, gyro.getAngle());
    				}
    			}
    			else{
    				step++;
    			}
    			break;
    		case 3:
    			if(gyro.getAngle() > 3){
    				drive.FieldOrientedDrive(0, 0, -.1, gyro.getAngle());
    			}
    			else if(gyro.getAngle() < -3){
    				drive.FieldOrientedDrive(0, 0, .1, gyro.getAngle());
    			}
    			else{
    				step++;
    			}
    			break;
    		case 4:
    			time = System.currentTimeMillis();
    			step++;
    			break;
    		case 5:
    			if(System.currentTimeMillis() - time < 5000){
    				if(gyro.getAngle() > 3){
    					drive.FieldOrientedDrive(0, .4, -.1, gyro.getAngle());
    				}
    				else if(gyro.getAngle() < -3){
    					drive.FieldOrientedDrive(0, .4, .1, gyro.getAngle());
    				}
    				else{
    					drive.FieldOrientedDrive(0, .4, 0, gyro.getAngle());
    				}
    			}
    			else{
    				drive.FieldOrientedDrive(0, 0, 0, gyro.getAngle());
    			}
    			break;
    			
    		}
    		break;
    	}
    	DriverStation.reportError(gyro.getAngle() + "\n", false);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	 
    	while (true && isOperatorControl() && isEnabled()) {
    		if(isFieldOriented == true){
    			drive.FieldOrientedDrive(drive.gamepad.getLeftJoystickXAxis(), -drive.gamepad.getLeftJoystickYAxis(), drive.gamepad.getRightJoystickXAxis(),gyro.getAngle());
    		}
    		else{
    			drive.update();
    		}
    		
    		if(lastTimeRan == 0){
    			lastTimeRan = System.currentTimeMillis();
    		}
    		else{
    			timeDiff = (System.currentTimeMillis() - lastTimeRan)/1000;
    			lastTimeRan = System.currentTimeMillis();
    			if(Math.abs(accel.getY()) > .1){
    				yVelocity += accel.getY() * timeDiff;
    			}
    			DriverStation.reportError(accel.getY() + "\n", false);
    		}
    		
    		if(drive.gamepad.getAButton().isPressed()){
    			
    			gyroAuto.Reset();
    			
    		}
    		if(drive.gamepad.getTriggerAxis() > 0){
    			gyroAuto.RunAuto(.4 * drive.gamepad.getTriggerAxis());
    		}
    		
    		if(drive.gamepad.getLeftBumper().isPressed()){
    			isFieldOriented = false;
    		}
    		if(drive.gamepad.getRightBumper().isPressed()){
    			isFieldOriented = true;
    		}
    		/*if(limitSwitch.get()){
    			DriverStation.reportError("A limit switch is being pressed.\n", false);
    		}
    		else{
    			DriverStation.reportError("A limit switch is not being pressed.\n", false);
    		}
    		*/
    		
    		
    		Timer.delay(0.01);
    		
    	}
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
    
}
