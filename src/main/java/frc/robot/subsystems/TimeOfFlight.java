package frc.robot.subsystems;

import au.grapplerobotics.LaserCan;

import com.ctre.phoenix6.hardware.core.CoreCANrange;
import com.ctre.phoenix6.configs.CANrangeConfiguration;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TimeOfFlight extends SubsystemBase {
  LaserCan laserCan = new LaserCan(4);
  CoreCANrange ctreCanRange = new CoreCANrange(0); // Replace deviceId with your sensor's ID

  public TimeOfFlight() {
    // Get the current configuration and modify the FOV parameters.
    var configs = new CANrangeConfiguration();
    ctreCanRange.getConfigurator().refresh(configs); // Refresh configs from the device

    System.out.println("Before FOV Range X: " + configs.FovParams.FOVRangeX);
    System.out.println("Before FOV Range Y: " + configs.FovParams.FOVRangeY);
    
    configs.FovParams.FOVRangeX = 7.0; // Set horizontal FOV to 20 degrees.
    configs.FovParams.FOVRangeY = 7.0; // Set vertical FOV to 20 degrees.

    configs.ToFParams.withUpdateFrequency(10);
    
    // Apply the new configuration.
    ctreCanRange.getConfigurator().apply(configs);

    System.out.println("After FOV Range X: " + configs.FovParams.FOVRangeX);
    System.out.println("After FOV Range Y: " + configs.FovParams.FOVRangeY);
  }

    @Override
  public void periodic() {
    if(DriverStation.isAutonomous()) {
        System.out.println("The target from the laserCan is " + GetLaserCanDistance() + "mm away");
        System.out.println("The target from the ctreCanRange is " + GetCtreCanRangeDistance() + "mm away");
    }
  }

  public int GetLaserCanDistance(){
    return laserCan.getMeasurement().distance_mm;
    //return 0;
  }

  public double GetCtreCanRangeDistance(){
    return ctreCanRange.getDistance().getValueAsDouble() * 1000;
    //return 0.0;
  }
}