package com.example.mygame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.*
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlin.random.Random
import com.example.mygame.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var targetScore = 0
    private var sliderValue = 0
    private var totalScore = 0
    private var currentRound = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        startNewGame()

        binding.btnInfo?.setOnClickListener{
            navigateToAboutPage()
        }

        binding.btnPlay.setOnClickListener {
            totalScore += pointsCurrentRound()
            binding.txtScoreNumber?.text = totalScore.toString()
            showResult()
        }

        binding.btnRepeat.setOnClickListener{
            startNewGame()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                sliderValue = progress
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

    }

    private fun navigateToAboutPage() {
        var intent = Intent(this,AboutActivity::class.java)
        startActivity(intent)
    }

    private fun newTarget():Int = Random.nextInt(0,100)

    private fun pointsDifference() : Int = abs(targetScore - sliderValue)

    private fun pointsCurrentRound() : Int = when {pointsDifference() == 0 -> 200
        pointsDifference() == 1 -> 149
        else -> 100 - pointsDifference()}

    private fun startNewGame(){
        totalScore = 0
        currentRound = 1
        targetScore = newTarget()

        binding.seekBar.progress = 0
        binding.txtTarget.text = targetScore.toString()
        binding.txtRoundNumber?.text = currentRound.toString()
        binding.txtScoreNumber?.text = totalScore.toString()
    }

    private fun showResult() {

        val dialogTitle = modifiedTitle()
        val dialogMessage = getString(R.string.dialog_message,sliderValue,pointsCurrentRound())

        val builder = AlertDialog.Builder(this)

        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.setPositiveButton(R.string.dialog_next_button){dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()

        targetScore = newTarget()
        binding.txtTarget.text = targetScore.toString()

        currentRound += 1
        binding.txtRoundNumber?.text = currentRound.toString()
    }

    private fun modifiedTitle(): String {
        var dif = pointsDifference()
        return when {
            dif == 0 -> getString(R.string.dialog_title1)
            dif <= 5 -> getString(R.string.dialog_title2)
            dif < 10 -> getString(R.string.dialog_title3)
            else -> getString(R.string.dialog_title4)
        }
    }

}