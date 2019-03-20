/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.cameraserver.CameraServer;

import odyssey.lib.EasyDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private static final int kFrontLeftChannel = 2;
  private static final int kRearLeftChannel = 3;
  private static final int kFrontRightChannel = 1;
  private static final int kRearRightChannel = 0;

  private static final int kJoystickChannel = 0;

  private EasyDrive m_robotDrive;
  private XboxController m_stick;
  //private AHRS ahrs = new AHRS(SPI.Port.kMXP);


  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    CameraServer.getInstance().startAutomaticCapture();

    SpeedControllerGroup leftDrive = new SpeedControllerGroup(new VictorSP(kFrontLeftChannel),  new VictorSP(kRearLeftChannel));
    SpeedControllerGroup rightDrive = new SpeedControllerGroup(new VictorSP(kFrontRightChannel), new VictorSP(kRearRightChannel));
    // Invert the left side motors.
    // You may need to change or remove this to match your robot.
    leftDrive.setInverted(true);

    m_robotDrive = new EasyDrive(leftDrive, rightDrive);
    m_stick = new XboxController(kJoystickChannel);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        CarDrive();
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
            // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
        CarDrive();
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    CarDrive();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  public void CarDrive()
  {
    // Car drive just means the robots turning speed is dependent on the speed of the movement
    // This would make more sense if you drove the robot

    double rightStick = m_stick.getTriggerAxis(GenericHID.Hand.kRight); //Accelerate
    double leftStick = m_stick.getTriggerAxis(GenericHID.Hand.kLeft); //"brake" slash reverse
    double turn = m_stick.getX(GenericHID.Hand.kLeft);
    double velocity = rightStick-leftStick;

    m_robotDrive.Update();
    m_robotDrive.SetMoveSpeed(Math.sqrt(velocity));
    m_robotDrive.SetTurnSpeed(turn*velocity); //Speed of turn moves with the speeed of the robot

  }

  public void RunDrive()
  {
    m_robotDrive.Update();
    m_robotDrive.SetMoveSpeed(-m_stick.getX(GenericHID.Hand.kRight));
    m_robotDrive.SetTurnSpeed(-m_stick.getY(GenericHID.Hand.kLeft)*0.75);
  }
}
