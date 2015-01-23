package org.plasmarobotics.team2403.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Pneumatics {
	
	Compressor compressor;
	DoubleSolenoid solen1;
	
	public Pneumatics(){
		compressor = new Compressor();
		solen1 = new DoubleSolenoid(0,1);
		
		compressor.start();
	}
	
	public void up(){
		solen1.set(DoubleSolenoid.Value.kForward);
	}
	
	public void down(){
		solen1.set(DoubleSolenoid.Value.kReverse);
	}
}
