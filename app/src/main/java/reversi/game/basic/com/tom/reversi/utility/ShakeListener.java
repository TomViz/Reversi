package reversi.game.basic.com.tom.reversi.utility;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * A listener that calls supplied "onShake" whenever a shake gesture is identified.
 * This shake can be further customized via the parameters "MIN_SHAKE_ACCELERATION", "MIN_MOVEMENTS" and "MAX_SHAKE_DURATION".
 * @see <a href="http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it">Stack Overflow - Android: I want to shake it</a>
 */
public class ShakeListener implements SensorEventListener
{
    /**
     * Interface for shake gesture. "onShake" is called when a shake gesture is detected.
     */
    public interface OnShakeListener
    {
        void onShake();
    }

    // Minimum acceleration needed to count as a shake movement
    private static final int MIN_SHAKE_ACCELERATION = 5;

    // Minimum number of movements to register a shake
    private static final int MIN_MOVEMENTS = 2;

    // Maximum time (in milliseconds) for the whole shake to occur
    private static final int MAX_SHAKE_DURATION = 500;

    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    private float[] mGravity = { 0.0f, 0.0f, 0.0f };
    private float[] mLinearAcceleration = { 0.0f, 0.0f, 0.0f };

    private long startTime = 0;
    private int moveCount = 0;

    private OnShakeListener shakeListener;

    public void setOnShakeListener(OnShakeListener shakeListener)
    {
        this.shakeListener = shakeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        setCurrentAcceleration(event);
        float maxLinearAcceleration = getMaxCurrentLinearAcceleration();

        if (maxLinearAcceleration > MIN_SHAKE_ACCELERATION)
        {
            long now = System.currentTimeMillis();
            if (startTime == 0)
            {
                startTime = now;
            }

            long elapsedTime = now - startTime;
            if (elapsedTime > MAX_SHAKE_DURATION)
            {
                resetShakeDetection();
            }
            else
            {
                moveCount++;
                if (moveCount > MIN_MOVEMENTS)
                {
                    shakeListener.onShake();
                    resetShakeDetection();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // Intentionally blank
    }

    private void setCurrentAcceleration(SensorEvent event)
    {
        final float alpha = 0.8f;

        mGravity[X] = alpha * mGravity[X] + (1 - alpha) * event.values[X];
        mGravity[Y] = alpha * mGravity[Y] + (1 - alpha) * event.values[Y];
        mGravity[Z] = alpha * mGravity[Z] + (1 - alpha) * event.values[Z];

        mLinearAcceleration[X] = event.values[X] - mGravity[X];
        mLinearAcceleration[Y] = event.values[Y] - mGravity[Y];
        mLinearAcceleration[Z] = event.values[Z] - mGravity[Z];
    }

    private float getMaxCurrentLinearAcceleration()
    {
        float maxLinearAcceleration = mLinearAcceleration[X];

        if (mLinearAcceleration[Y] > maxLinearAcceleration)
        {
            maxLinearAcceleration = mLinearAcceleration[Y];
        }

        if (mLinearAcceleration[Z] > maxLinearAcceleration)
        {
            maxLinearAcceleration = mLinearAcceleration[Z];
        }

        return maxLinearAcceleration;
    }

    private void resetShakeDetection()
    {
        startTime = 0;
        moveCount = 0;
    }
}
