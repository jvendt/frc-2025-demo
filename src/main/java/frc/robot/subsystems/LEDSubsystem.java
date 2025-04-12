package frc.robot.subsystems;

import edu.wpi.first.units.measure.Dimensionless;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.LEDPattern;
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

    private static final Random m_random = new Random();

    private final LED[] leds = new LED[3];

    public LEDSubsystem() {
        m_led = new AddressableLED(kPort);
        m_ledBuffer = new AddressableLEDBuffer(kLength);

        m_led.setLength(m_ledBuffer.getLength());

        for (var x = 0; x <= 2; x++)
        {
            leds[x] = new LED(m_ledBuffer.createView(x, x));
        }

        m_led.start();
    }

    @Override
    public void periodic() {
        if(DriverStation.isAutonomous()) {
            LEDPattern.gradient(GradientType.kDiscontinuous, Color.kRed, Color.kBlue).breathe(Seconds.of(2)).applyTo(m_ledBuffer);
        }
        else if(DriverStation.isEnabled())
        {
            for (LED led : leds) {
                if(led.colorTimerHasElapsed()){
                    led.setColorTimerDelay(m_random.nextDouble(.5, 3));
                    LEDPattern.solid(GetRandomColor()).atBrightness(getRandomBrightness()).applyTo(led.getBufferView());
                }

                if(led.twinkleTimerHasElapsed()) {
                    led.setTwinkleTimerDelay(m_random.nextDouble(.25, .75));
                    LEDPattern.solid(led.getLedColor()).atBrightness(getRandomBrightness()).applyTo(led.getBufferView());
                }
            }
        }
        else if(DriverStation.isDisabled()) {
            LEDPattern.solid(Color.kRed).applyTo(leds[2].getBufferView());
            LEDPattern.solid(Color.kYellow).applyTo(leds[1].getBufferView());
            LEDPattern.solid(Color.kGreen).applyTo(leds[0].getBufferView());
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
        return m_random.nextInt(0, 255);
    }
}
