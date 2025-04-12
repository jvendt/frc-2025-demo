package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLEDBufferView;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;

public class LED {
    private final Timer m_colorTimer = new Timer();
    private double m_colorTimerDelay = 0.0;
    private final Timer m_twinkleTimer = new Timer();
    private double m_twinkleTimerDelay = 0.0;
    private final AddressableLEDBufferView m_bufferView;

    public LED(AddressableLEDBufferView bufferView) {
        m_bufferView = bufferView;
    }

    public AddressableLEDBufferView getBufferView() {
        return m_bufferView;
    }

    public Color getLedColor()
    {
        return m_bufferView.getLED(0);
    }

    public boolean colorTimerHasElapsed() {
        return timerHasElapsed(m_colorTimer, m_colorTimerDelay);
    }

    public boolean twinkleTimerHasElapsed() {
        return timerHasElapsed(m_twinkleTimer, m_twinkleTimerDelay);
    }

    private boolean timerHasElapsed(Timer timer, double seconds){
        if(!timer.isRunning())
        {
            timer.start();
            return true;
        }

        if(timer.hasElapsed(seconds)) {
            timer.reset();
            return true;
        }
        return false;
    }

    public void setColorTimerDelay(double secondsToDelay) {
        m_colorTimerDelay = secondsToDelay;
    }

    public void setTwinkleTimerDelay(double secondsToDelay) {
        m_twinkleTimerDelay = secondsToDelay;
    }
}
