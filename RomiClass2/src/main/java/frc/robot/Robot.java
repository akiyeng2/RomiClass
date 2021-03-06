// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final RomiDrivetrain m_drivetrain = new RomiDrivetrain();
  private final Joystick m_controller = new Joystick(0);

  private enum Stt{
    DRIVE_FORWARD,
    TURN,
    IDLE;
}

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Left Distance", m_drivetrain.getLeftDistanceInch());
    SmartDashboard.putNumber("Right Distance", m_drivetrain.getRightDistanceInch());
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_drivetrain.resetEncoders();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    // We want our robot to move forward 12 inches
    double leftDistance = m_drivetrain.getLeftDistanceInch();
    double rightDistance = m_drivetrain.getRightDistanceInch();
    // Homework: accuracy is your only challenge
    // Must happen within 10 seconds of autonomousInit
    double distance = (leftDistance + rightDistance) / 2.0;
    if(distance < 12) {
      m_drivetrain.arcadeDrive(-0.75, 0);
    } else {
      m_drivetrain.arcadeDrive(0, 0);
    }

  }

  /** This function is called once when teleop is enabled. */
  Stt current_Stt=Stt.IDLE;
  int drive_counter= 0;
  int turn_counter=0;
  Stt next_Stt=Stt.IDLE;

  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    boolean isYButtonPressed = m_controller.getRawButton(8);
    SmartDashboard.putBoolean("Button Pressed", isYButtonPressed);
    // When ZR is pressed, tank drive, when released arcade drive
    // Homework: When one button is pressed, change to tank drive
    // When anothe is pressed, change to arcade drive
    if (isYButtonPressed) {
      double leftSpeed = m_controller.getRawAxis(1);
      double rightSpeed = m_controller.getRawAxis(3);
      m_drivetrain.tankDrive(leftSpeed, rightSpeed);
    } else {
      double speed = m_controller.getRawAxis(1);
      double rotation = m_controller.getRawAxis(2);
      m_drivetrain.arcadeDrive(speed, rotation);
    }

    if (isYButtonPressed){
      for (int i = 1; i < 3; i++){
        m_drivetrain.arcadeDrive(12, 0);
      }

    }
    
    boolean isBButtonPressed= m_controller.getRawButton(2);
    if (current_Stt==Stt.IDLE){
      if (isBButtonPressed){
        next_Stt=Stt.DRIVE_FORWARD;
        m_drivetrain.resetEncoders();
      }
    }

    if(current_Stt == Stt.DRIVE_FORWARD && drive_counter<3){
      if(Math.abs(m_drivetrain.getLeftDistanceInch()) < 6) {
        m_drivetrain.arcadeDrive(0.3, 0);
        next_Stt = Stt.DRIVE_FORWARD;
      } else {
        m_drivetrain.arcadeDrive(0, 0);
        drive_counter += 1;
        next_Stt = Stt.TURN;
        m_drivetrain.resetEncoders();
      }
    }

       
    if(current_Stt==Stt.TURN && turn_counter<3){
      if(Math.abs(m_drivetrain.getLeftDistanceInch()) < 6.8) {
        m_drivetrain.arcadeDrive(0, 0.3);
      } else {
        m_drivetrain.arcadeDrive(0, 0);
        turn_counter+=1;
        next_Stt= Stt.DRIVE_FORWARD; 
        m_drivetrain.resetEncoders();
      }
    }

    else if (current_Stt==Stt.DRIVE_FORWARD && drive_counter==3){
      next_Stt=Stt.IDLE;
      drive_counter = 0;
      turn_counter = 0;
    }

    else if (current_Stt==Stt.TURN && turn_counter==3){
      next_Stt=Stt.IDLE;
      drive_counter = 0;
      turn_counter = 0;
    }

    current_Stt = next_Stt;    
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
