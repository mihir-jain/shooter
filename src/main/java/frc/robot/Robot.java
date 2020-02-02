/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
  private SpeedController m_kicker;
  private CANSparkMax LeftShooter;
  private CANSparkMax RightShooter;
  private CANEncoder LeftEncoder;
  private CANEncoder RightEncoder;
  private boolean isUptoSpeed;
  private XboxController controller;
  private SpeedControllerGroup shooter;
  private int RPMForLoop;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    m_kicker = new PWMVictorSPX(0);
    LeftShooter = new CANSparkMax(16, MotorType.kBrushless);
    LeftShooter.setInverted(true);
    RightShooter = new CANSparkMax(17, MotorType.kBrushless);
    LeftEncoder = LeftShooter.getEncoder();
    RightEncoder = RightShooter.getEncoder();
    isUptoSpeed = false;
    controller = new XboxController(0);
    shooter = new SpeedControllerGroup(LeftShooter, RightShooter);
    RPMForLoop = 0;
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
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
      break;
    case kDefaultAuto:
    default:
      // Put default auto code here
      break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    if (controller.getAButton()){
      RPMForLoop = 5400;
    }
    if (controller.getYButton()){
      RPMForLoop = 5200;
    }
    if (controller.getAButton() || controller.getYButton()){
      if (RightEncoder.getVelocity() > RPMForLoop){
        isUptoSpeed = true;
      } else{
        isUptoSpeed = false;
      }

      if (isUptoSpeed){
        if (controller.getBButton()){
          m_kicker.set(1);
        }
        shooter.set(0);
        
      }
      else{
        m_kicker.set(0);
        shooter.set(1);
        
      }
    
    } else if(controller.getXButton()){
      shooter.set(0.45);
      
      m_kicker.set(1);
    } else {
      if (RightEncoder.getVelocity() > 1000){
        shooter.set(-0.1);
      } else{
        shooter.set(0);
      }
      m_kicker.set(0);
    }
  
    
    
    SmartDashboard.putNumber("left Shooter RPM", LeftEncoder.getVelocity());
    SmartDashboard.putNumber("Right Shooter RPM", RightEncoder.getVelocity());
    SmartDashboard.putNumber("left Temp", LeftShooter.getMotorTemperature());
    SmartDashboard.putNumber("right Temp", RightShooter.getMotorTemperature());


  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
