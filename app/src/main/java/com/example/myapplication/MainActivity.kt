package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.example.myapplication.databinding.ActivityMain2Binding
import com.example.myapplication.databinding.ActivityMainBinding
import java.lang.Exception
import java.lang.NullPointerException
import java.sql.Array
import kotlin.random.Random


class MainActivity : AppCompatActivity(), SensorEventListener {
    //Rester af forsøget på en rotations-sensor, droppet da forsøgs-telefonen viste sig at være ude
    //af stand til at mærke rotation.
    private lateinit var sensorManager: SensorManager
    private var rotationSensor: Sensor? = null


    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

            // Initialize SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Get rotation vector sensor
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        val button = binding.Roller
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val diceNum = binding.Amount.text.toString()
                //diceVar bruges til at sætte terningetypen. 'when'-statementet sætter typen
                // baseret på den valgte svarmulighed.
                var diceVar = binding.TypeSelector.selectedItem.toString()
                var type = 0
                //Siden RNG'en bruger ranges, er tallene højere end terningens maksimale slag.
                //Else-værdien er sat til at være væsentligt højere end de andre, for at der er
                //tydelige udslag ved fejl.
                when (diceVar) {
                    "D4" -> type = 5
                    "D6" -> type = 7
                    "D8" -> type = 9
                    "D10" -> type = 11
                    "D12" -> type = 13
                    "D20" -> type = 21
                    else ->type = 42
                }
                //Terningerne rulles, og teksten på fronten sættes til resultatet.
                try {
                    val diceRolls = rollDice(diceNum.toInt(), type)
                    val output = diceRolls.joinToString(separator = ", ")
                    binding.MainText.setText(output)
                } catch (e: Exception) {

                }

            }
        })

    }

    override fun onResume() {
        super.onResume()
        // Register sensor listener
        rotationSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregister sensor listener to conserve battery
        sensorManager.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        // Handle sensor data change

        val tekst = binding.MainText


        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            // Access rotation sensor data here
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            // Use the rotation matrix as needed
            val orientationAngles = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            val rotValue = orientationAngles[1].toString()
            val test = "Override Succesful"


        //debugging in regards to making the text react to rotation.
            try {
                tekst.setText(test)
            } catch (e: NullPointerException) {
                print("Does not work")
            }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }


}

fun rollDice(amount: Int, DiceVariant: Int): ArrayList<Int> {
    var rolls = ArrayList<Int>()
    for (x in 1..amount) {
        val outCome = Random.nextInt(1, DiceVariant)
        rolls.add(outCome)
    }
    return (rolls)
}