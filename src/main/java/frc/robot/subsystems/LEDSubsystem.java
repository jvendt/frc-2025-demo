package frc.robot.subsystems;

import edu.wpi.first.units.measure.Dimensionless;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AddressableLEDBufferView;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.LEDPattern.GradientType;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.units.Units.Percent;
import static edu.wpi.first.units.Units.Seconds;

import java.util.Random;

public class LEDSubsystem extends SubsystemBase {
    private static final int kPort = 0;
    private static final int kLength = 3;

    private final AddressableLED m_led;
    private final AddressableLEDBuffer m_ledBuffer;
    private final AddressableLEDBufferView m_ledBottom;
    private final AddressableLEDBufferView m_ledMiddle;
    private final AddressableLEDBufferView m_ledTop;

    private static final Random m_random = new Random();

    private final Timer m_colorTimer = new Timer();
    private final Timer m_twinkleTimer = new Timer();

    public LEDSubsystem() {
        m_led = new AddressableLED(kPort);
        m_ledBuffer = new AddressableLEDBuffer(kLength);

        m_led.setLength(m_ledBuffer.getLength());

        m_ledBottom = m_ledBuffer.createView(0,0);
        m_ledMiddle = m_ledBuffer.createView(1,1);
        m_ledTop = m_ledBuffer.createView(2,2);

        m_led.start();

        m_twinkleTimer.start();
    }

    @Override
    public void periodic() {
        if(DriverStation.isAutonomous()) {
            LEDPattern.gradient(GradientType.kDiscontinuous, Color.kRed, Color.kBlue).breathe(Seconds.of(2)).applyTo(m_ledBuffer);
        }
        else if(DriverStation.isEnabled() && (m_colorTimer.hasElapsed(.75) || !m_colorTimer.isRunning()))
        {
            for (AddressableLEDBufferView bufferView : new AddressableLEDBufferView[]{ m_ledTop, m_ledMiddle, m_ledBottom }) {
                LEDPattern.solid(GetRandomColor()).atBrightness(getRandomBrightness()).applyTo(bufferView);                
            }

            if(!m_colorTimer.isRunning())
            {
                m_colorTimer.start();
            }

            m_colorTimer.reset();
        }
        else if(DriverStation.isEnabled() && m_twinkleTimer.hasElapsed(.1))
        {
            for (AddressableLEDBufferView bufferView : new AddressableLEDBufferView[]{ m_ledTop, m_ledMiddle, m_ledBottom }) {
                LEDPattern.solid(bufferView.getLED(0)).atBrightness(getRandomBrightness()).applyTo(bufferView);                
            }

            m_twinkleTimer.reset();
        }
        else if(DriverStation.isDisabled()) {
            LEDPattern.solid(Color.kRed).applyTo(m_ledTop);
            LEDPattern.solid(Color.kYellow).applyTo(m_ledMiddle);
            LEDPattern.solid(Color.kGreen).applyTo(m_ledBottom);
        }

        m_led.setData(m_ledBuffer);
    }

    private Dimensionless getRandomBrightness() {
        return Percent.of(m_random.nextDouble(75, 120));
    }
            
    private Color GetRandomColor() {
        return new Color(getRandomRGB(), getRandomRGB(), getRandomRGB());
    }

    private int getRandomRGB()
    {
        return m_random.nextInt(10, 245);
    }
}
