// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class RomiDrivetrain {
  private static final double kCountsPerRevolution = 1440.0;
  private static final double kWheelDiameterInch = 2.75591; // 70 mm

  // The Romi has the left and right motors set to
  // PWM channels 0 and 1 respectively
  private final Spark m_leftMotor = new Spark(0);
  private final Spark m_rightMotor = new Spark(1);

  // The Romi has onboard encoders that are hardcoded
  // to use DIO pins 4/5 and 6/7 for the left and right
  private final Encoder m_leftEncoder = new Encoder(4, 5);
  private final Encoder m_rightEncoder = new Encoder(6, 7);

  /** Creates a new RomiDrivetrain. */
  public RomiDrivetrain() {
    // Use inches as unit for encoder distances
    m_leftEncoder.setDistancePerPulse(-(Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
    m_rightEncoder.setDistancePerPulse(-(Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
    resetEncoders();
  }

  public void tankDrive(double leftSpeed, double rightSpeed) {
    m_leftMotor.set(-leftSpeed);
    m_rightMotor.set(rightSpeed);
  }


  public void arcadeDrive(double speed, double rotation) {
    // Homework: Fill in this class to run the robot with arcade drive
    // Arcade Drive is: Left stick forward/back controls forward speed
    // right stick left/right controls rotation rate
    // Scenario: Turning full left, no forward
    /*
     * Inputs: speed = 0, rotation = -1 -> Rotate in place left
     * Outputs: leftSpeed = -1, rightSpeed = 1
     * 
     * Inputs: speed = 1, rotation = 0 -> Go forward
     * Outputs: leftSpeed = rightSpeed = 1
     * 
     * Inputs: speed = 1, rotation = 1 -> forward and to the right
     * Outputs: leftSpeed = 1, rightSpeed = 0
     *  
     * Inputs: speed = 1, rotation = -1 -> forward and left
     * Outputs: leftSpeed = 0, rightSpeed = 1
     * 
     * Inputs: speed = -1, rotation = 1 -> Backwards and to the right
     * Outputs: leftSpeed = -1, rightSpeed = 0
     * 
     * f(x, y) = (l, r)
     *   0  0     0  0    
     *   0  -1    -1  1
     *   1   0    1   1
     *   1   1    1   0
     *   1   -1   0   1
     *   -1   1   0   -1
     * 
     */
      m_leftMotor.set((speed + rotation));
      m_rightMotor.set(-(speed - rotation));
      

  }

  public void resetEncoders() {
    m_leftEncoder.reset();
    m_rightEncoder.reset();
  }

  public double getLeftDistanceInch() {
    return m_leftEncoder.getDistance();
  }

  public double getRightDistanceInch() {
    return m_rightEncoder.getDistance();
  }
}
