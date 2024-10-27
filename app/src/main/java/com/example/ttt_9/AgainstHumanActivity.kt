package com.example.ttt_9


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AgainstHumanActivity : AppCompatActivity() {
    private lateinit var button10: Button
    private lateinit var button: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    private lateinit var button6: Button
    private lateinit var button7: Button
    private lateinit var button8: Button
    private lateinit var button9: Button
    private lateinit var textView: TextView
    private lateinit var textView2: TextView

    private var player1Count = 0
    private var player2Count = 0
    private var playerTurn = true // true for Player 1, false for Player 2

    private val player1 = ArrayList<Int>()
    private val player2 = ArrayList<Int>()
    private val emptyCells = ArrayList<Int>()
    private var activeUser = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_against_human)

        val historyButton = findViewById<Button>(R.id.historyButton)
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        button10 = findViewById(R.id.button10)
        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        button5 = findViewById(R.id.button5)
        button6 = findViewById(R.id.button6)
        button7 = findViewById(R.id.button7)
        button8 = findViewById(R.id.button8)
        button9 = findViewById(R.id.button9)
        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)

        button10.setOnClickListener {
            reset()
        }
    }

    fun clickfun(view: View) {
        if (playerTurn) {
            val but = view as Button
            val cellID = when (but.id) {
                R.id.button -> 1
                R.id.button2 -> 2
                R.id.button3 -> 3
                R.id.button4 -> 4
                R.id.button5 -> 5
                R.id.button6 -> 6
                R.id.button7 -> 7
                R.id.button8 -> 8
                R.id.button9 -> 9
                else -> 0
            }
            playerTurn = false
            Handler(Looper.getMainLooper()).postDelayed({ playerTurn = true }, 600)
            playnow(but, cellID)
        }
    }

    private fun playnow(buttonSelected: Button, currCell: Int) {
        val audio = MediaPlayer.create(this, R.raw.app_src_main_res_raw_poutch)
        if (activeUser == 1) {
            buttonSelected.text = "X"
            buttonSelected.setTextColor(Color.parseColor("#EC0C0C"))
            player1.add(currCell)
            emptyCells.add(currCell)
            audio.start()
            buttonSelected.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({ audio.release() }, 200)

            if (checkWinner() == 1) {
                Handler(Looper.getMainLooper()).postDelayed({ reset() }, 2000)
            } else {
                activeUser = 2
            }
        } else {
            buttonSelected.text = "O"
            buttonSelected.setTextColor(Color.parseColor("#D22BB804"))
            player2.add(currCell)
            emptyCells.add(currCell)
            audio.start()
            buttonSelected.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({ audio.release() }, 200)

            if (checkWinner() == 1) {
                Handler(Looper.getMainLooper()).postDelayed({ reset() }, 2000)
            }
            activeUser = 1
        }
    }

    private fun reset() {
        player1.clear()
        player2.clear()
        emptyCells.clear()
        activeUser = 1

        for (i in 1..9) {
            val buttonselected: Button? = when (i) {
                1 -> button
                2 -> button2
                3 -> button3
                4 -> button4
                5 -> button5
                6 -> button6
                7 -> button7
                8 -> button8
                9 -> button9
                else -> null
            }
            buttonselected?.isEnabled = true
            buttonselected?.text = ""
        }

        textView.text = "Player 1 (X) : $player1Count"
        textView2.text = "Player 2 (O) : $player2Count"
    }

    private fun checkWinner(): Int {
        val audio = MediaPlayer.create(this, R.raw.app_src_main_res_raw_success)

        // Winning conditions for Player 1
        if ((player1.contains(1) && player1.contains(2) && player1.contains(3)) ||
            (player1.contains(1) && player1.contains(4) && player1.contains(7)) ||
            (player1.contains(3) && player1.contains(6) && player1.contains(9)) ||
            (player1.contains(7) && player1.contains(8) && player1.contains(9)) ||
            (player1.contains(4) && player1.contains(5) && player1.contains(6)) ||
            (player1.contains(1) && player1.contains(5) && player1.contains(9)) ||
            (player1.contains(3) && player1.contains(5) && player1.contains(7)) ||
            (player1.contains(2) && player1.contains(5) && player1.contains(8))) {

            player1Count++
            buttonDisable()
            audio.start()
            disableReset()
            Handler(Looper.getMainLooper()).postDelayed({ audio.release() }, 4000)

            val build = AlertDialog.Builder(this)
            build.setTitle("Congratulations!")
            saveGameResult(this, "Player 1 ", "Human vs Human")
            build.setMessage("Player 1 Wins! 🎉\n\nDo you want to play again?")
            build.setPositiveButton("Play Again") { dialog, _ ->
                reset()
                audio.release()
            }
            build.setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
                audio.release()
            }
            Handler(Looper.getMainLooper()).postDelayed({ build.show() }, 2000)
            return 1
        }

        // Winning conditions for Player 2
        else if ((player2.contains(1) && player2.contains(2) && player2.contains(3)) ||
            (player2.contains(1) && player2.contains(4) && player2.contains(7)) ||
            (player2.contains(3) && player2.contains(6) && player2.contains(9)) ||
            (player2.contains(7) && player2.contains(8) && player2.contains(9)) ||
            (player2.contains(4) && player2.contains(5) && player2.contains(6)) ||
            (player2.contains(1) && player2.contains(5) && player2.contains(9)) ||
            (player2.contains(3) && player2.contains(5) && player2.contains(7)) ||
            (player2.contains(2) && player2.contains(5) && player2.contains(8))) {

            player2Count++
            buttonDisable()
            audio.start()
            disableReset()
            Handler(Looper.getMainLooper()).postDelayed({ audio.release() }, 4000)

            val build = AlertDialog.Builder(this)
            build.setTitle("Congratulations!")
            saveGameResult(this, "Player 2 ", "Human vs Human")
            build.setMessage("Player 2 Wins! 🎉\n\nDo you want to play again?")
            build.setPositiveButton("Play Again") { dialog, _ ->
                reset()
                audio.release()
            }
            build.setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
                audio.release()
            }
            Handler(Looper.getMainLooper()).postDelayed({ build.show() }, 2000)
            return 1
        }

        // Check for a draw
        else if (emptyCells.size == 9) {
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            saveGameResult(this, "Draw ", "Human vs Human")
            build.setMessage("It's a Draw! 🤝\n\nDo you want to play again?")
            build.setPositiveButton("Play Again") { dialog, _ ->
                reset()
                audio.release()
            }
            build.setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
                audio.release()
            }
            Handler(Looper.getMainLooper()).postDelayed({ build.show() }, 2000)
            return 1
        }

        return 0
    }

    private fun buttonDisable() {
        button.isEnabled = false
        button2.isEnabled = false
        button3.isEnabled = false
        button4.isEnabled = false
        button5.isEnabled = false
        button6.isEnabled = false
        button7.isEnabled = false
        button8.isEnabled = false
        button9.isEnabled = false
    }

    private fun disableReset() {
        button10.isEnabled = false
    }

    private fun saveGameResult(context: Context, winner: String, mode: String) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val sharedPref = context.getSharedPreferences("TicTacToeHistory", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("lastResult", "$currentDate - $mode: $winner")
        editor.apply()
    }
}
