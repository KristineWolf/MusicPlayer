package com.mastercode.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // variables
    var startTime = 0.0
    var finalTime = 0.0
    var forwardTime = 10000
    var backwardTime = 10000
    var oneTimeOnly = 0

    // Handler
    var handler: Handler = Handler()

    lateinit var mediaPlayer: MediaPlayer
    lateinit var seekBar: SeekBar
    lateinit var playButton: Button
    lateinit var pauseButton: Button
    lateinit var backButton: Button
    lateinit var forwardButton: Button
    lateinit var finalTimeTextView: TextView
    lateinit var songTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initMediaPlayer()
        initSeekbarStatus()
        addListener()
    }

    private fun initViews() {
        playButton = findViewById(R.id.play_button)
        pauseButton = findViewById(R.id.pause_button)
        backButton = findViewById(R.id.back_button)
        forwardButton = findViewById(R.id.forward_button)
        seekBar = findViewById(R.id.seek_bar)
        finalTimeTextView = findViewById(R.id.time_left_text)
        songTitle = findViewById(R.id.song_title)
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.astronaut
        )
        songTitle.setText(resources.getResourceEntryName(R.raw.astronaut).toString())
    }

    private fun initSeekbarStatus() {
        seekBar.isClickable = false
    }

    private fun addListener() {
        playButton.setOnClickListener() {
            mediaPlayer.start()

            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()

            if (oneTimeOnly == 0) {
                seekBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            finalTimeTextView.text = startTime.toString()
            seekBar.setProgress(startTime.toInt())

            handler.postDelayed(UpdateSongTime, 100)

        }

        pauseButton.setOnClickListener() {
            mediaPlayer.pause()
        }

        forwardButton.setOnClickListener() {
            var temp = startTime
            if ((temp + forwardTime) <= finalTime) {
                startTime = startTime + forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            } else {
                Toast.makeText(this, "Can not jump forward", Toast.LENGTH_LONG).show()
            }
        }

        backButton.setOnClickListener() {
            var temp = startTime.toInt()

            if ((temp - backwardTime) > 0) {
                startTime = startTime - backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            } else {
                Toast.makeText(this, "Can not jump backward", Toast.LENGTH_LONG).show()
            }
        }

    }

    val UpdateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            finalTimeTextView.text = "" +
                    String.format(
                        "%d min , %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(
                            startTime.toLong()
                                    - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    startTime.toLong()
                                )
                            ))
                    )

            seekBar.progress = startTime.toInt()
            handler.postDelayed(this, 100)
        }
    }
}