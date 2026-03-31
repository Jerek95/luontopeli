import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class StepCounterManager(context: Context) {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val stepSensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    private val gyroSensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private var stepListener: SensorEventListener? = null
    private var gyroListener: SensorEventListener? = null

    fun startStepCounting(onStep: () -> Unit) {
        stepListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                    onStep()
                }
            }
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Ei tarvita askelmittarissa
            }
        }

        stepSensor?.let {
            val registered = sensorManager.registerListener(
                stepListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
            if (!registered) {
                // Laite ei tue askelmittaria
            }
        }
    }

    fun stopStepCounting() {
        stepListener?.let { sensorManager.unregisterListener(it) }
        stepListener = null
    }

    fun startGyroscope(onRotation: (Float, Float, Float) -> Unit) {
        gyroListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                    // values[0] = pyöriminen x-akselin ympäri (pitch)
                    // values[1] = pyöriminen y-akselin ympäri (roll)
                    // values[2] = pyöriminen z-akselin ympäri (yaw)
                    onRotation(event.values[0], event.values[1], event.values[2])
                }
            }
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        gyroSensor?.let {
            sensorManager.registerListener(
                gyroListener,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    fun stopGyroscope() {
        gyroListener?.let { sensorManager.unregisterListener(it) }
        gyroListener = null
    }

    fun stopAll() {
        stopStepCounting()
        stopGyroscope()
    }

    fun isStepSensorAvailable(): Boolean = stepSensor != null

    val STEP_LENGTH_METERS = 0.74f
}

