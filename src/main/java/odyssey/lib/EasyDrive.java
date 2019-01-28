package odyssey.lib;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class EasyDrive {
	String driveMode;
	MecanumDrive mecDrive;
	DifferentialDrive diffDrive;

	static final String d_mec = "mec";
	static final String d_diff = "diff";

	double xSpeed; // Speed going forwards and backwards
	double ySpeed; // Speed going side to side (strafe)
	double zRotation; // Speed at which robot is being rotated or turned
	
	//Constructor for tank drive
	public EasyDrive(SpeedController leftMotor, SpeedController rightMotor)
	{
		diffDrive = new DifferentialDrive(leftMotor, rightMotor);
		driveMode = d_diff;
	}

	//Constructor for mecanum drive
	public EasyDrive(SpeedController frontLeftMotor, SpeedController rearLeftMotor, SpeedController frontRightMotor, SpeedController rearRightMotor)
	{
		mecDrive = new MecanumDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
		driveMode = d_mec;
	}

	public void Update()
	{
		if(driveMode.equals(d_mec))
		{
			mecDrive.driveCartesian(ySpeed, xSpeed, zRotation);
		}

		if(driveMode.equals(d_diff))
		{
			diffDrive.arcadeDrive(xSpeed, zRotation);
		}
	}

	public void SetTurnSpeed(double turnSpeed)
	{
		zRotation = turnSpeed;
	}

	public void SetMoveSpeed(double moveSpeed)
	{
		xSpeed = moveSpeed;
	}

	public void SetStrafeSpeed(double strafeSpeed)
	{
		ySpeed = strafeSpeed;
	}

	public void Stop()
	{
		zRotation = 0;
		xSpeed = 0;
		ySpeed = 0;
	}
}