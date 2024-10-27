package com.example.ttt_9

import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlin.random.Random
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess

class GameActivity : AppCompatActivity() {
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
    private var playerTurn = true
    private var singleUser = true

    private val player1 = ArrayList<Int>()
    private val player2 = ArrayList<Int>()
    private val emptyCells = ArrayList<Int>()
    private var activeUser = 1

    private lateinit var difficultySpinner: Spinner
    private var difficultyLevel = "Easy"  // Default difficulty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)

        val historyButton = findViewById<Button>(R.id.historyButton)
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        // Initialize spinner
        difficultySpinner = findViewById(R.id.difficultySpinner)
        val adapter = ArrayAdapter.createFromResource(
            this, R.array.difficulty_levels, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        difficultySpinner.adapter = adapter

        // Set difficulty based on spinner selection
        difficultySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                difficultyLevel = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                difficultyLevel = "Easy"
            }
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
            } else if (singleUser) {
                Handler(Looper.getMainLooper()).postDelayed({ robot() }, 500)
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
                Handler(Looper.getMainLooper()).postDelayed({ reset() }, 4000)
            }
            activeUser = 1
        }
    }

    private fun robot() {
        when (difficultyLevel) {
            "Easy" -> makeRandomMove()
            "Medium" -> {
                if (Random.nextBoolean()) makeRandomMove() else makeOptimalMove()
            }
            "Hard" -> makeOptimalMove()
        }
    }

    private fun makeRandomMove() {
        val rnd = (1..9).random()
        if (emptyCells.contains(rnd)) {
            robot()  // Retry if cell already taken
        } else {
            val buttonselected = getButtonById(rnd)
            if (buttonselected != null) {
                emptyCells.add(rnd)
                buttonselected.text = "O"
                buttonselected.setTextColor(Color.parseColor("#D22BB804"))
                player2.add(rnd)
                buttonselected.isEnabled = false
                checkWinner()
            }
        }
    }

    private fun makeOptimalMove() {
        var bestScore = Int.MIN_VALUE
        var bestMove = -1

        for (cell in 1..9) {
            // Check if the cell is empty
            if (!player1.contains(cell) && !player2.contains(cell)) {
                // Simulate the move
                val newCurrPlayer = player2 + cell
                val score = minimax(newCurrPlayer, player1, 0, false) // AI is maximizing
                if (score > bestScore) {
                    bestScore = score
                    bestMove = cell
                }
            }
        }

        if (bestMove != -1) {
            val buttonselected = getButtonById(bestMove)
            if (buttonselected != null) {
                emptyCells.add(bestMove)
                buttonselected.text = "O"
                buttonselected.setTextColor(Color.parseColor("#D22BB804"))
                player2.add(bestMove)
                buttonselected.isEnabled = false
                checkWinner()
            }
        }
    }

    private fun getButtonById(cellId: Int): Button? {
        return when (cellId) {
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
    }

    private fun minimax(currPlayer: List<Int>, opponent: List<Int>, depth: Int, isMaximizing: Boolean): Int {
        val score = evaluateBoard(currPlayer, opponent)

        // Check if the current state is a terminal state (win, lose, or draw)
        if (score == 10) return score - depth  // AI wins
        if (score == -10) return score + depth // Opponent wins
        if (currPlayer.size + opponent.size == 9) return 0 // Draw

        // Maximizing player's turn (AI)
        return if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (cell in 1..9) {
                if (!currPlayer.contains(cell) && !opponent.contains(cell)) {
                    // Make the move
                    val newCurrPlayer = currPlayer + cell
                    val score = minimax(newCurrPlayer, opponent, depth + 1, false)
                    bestScore = maxOf(bestScore, score)
                }
            }
            bestScore
        } else { // Minimizing player's turn (opponent)
            var bestScore = Int.MAX_VALUE
            for (cell in 1..9) {
                if (!currPlayer.contains(cell) && !opponent.contains(cell)) {
                    // Make the move
                    val newOpponent = opponent + cell
                    val score = minimax(currPlayer, newOpponent, depth + 1, true)
                    bestScore = minOf(bestScore, score)
                }
            }
            bestScore
        }
    }

    private fun evaluateBoard(currPlayer: List<Int>, opponent: List<Int>): Int {
        // Winning conditions
        val winningCombinations = listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
            listOf(7, 8, 9),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(3, 6, 9),
            listOf(1, 5, 9),
            listOf(3, 5, 7)
        )

        // Check if current player has won
        for (combination in winningCombinations) {
            if (combination.all { currPlayer.contains(it) }) {
                return 10 // AI wins
            }
            if (combination.all { opponent.contains(it) }) {
                return -10 // Opponent wins
            }
        }
        return 0 // No winner yet
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

        textView.text = "Human (X) : $player1Count"
        textView2.text = "AI (O) : $player2Count"
    }

    private fun disableReset() {
        button10.isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({ button10.isEnabled = true }, 2200)
    }

    private fun checkWinner(): Int {
        val audio = MediaPlayer.create(this, R.raw.app_src_main_res_raw_success)

        // Winning conditions for Player 1
        if ((player1.contains(1) && player1.contains(2) && player1.contains(3)) ||
            (player1.contains(1) && player1.contains(4) && player1.contains(7)) ||
            (player1.contains(3) && player1.contains(6) && player1.contains(9)) ||
            (player1.contains(7) && player1.contains(8) && player1.contains(9)) || // Fixed duplicate cell ID from (8, 8, 8)
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
            saveGameResult(this, "Human       ", difficultyLevel)
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
            (player2.contains(7) && player2.contains(8) && player2.contains(9)) || // Fixed duplicate cell ID from (8, 8, 8)
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
            saveGameResult(this, "AI                 ", difficultyLevel)
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
        else if (emptyCells.size == 9) {  // Only true if all cells are filled
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            saveGameResult(this, "Draw           ", difficultyLevel)
            build.setMessage("It's a Draw! \n\nDo you want to play again?")
            build.setPositiveButton("Play Again") { dialog, _ ->
                reset()
            }
            build.setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            build.show()
            return 1
        }

        return 0 // No winner yet
    }

    private fun buttonDisable() {
        // Disable all buttons when the game is over
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
            buttonselected?.isEnabled = false
        }
    }

    fun saveGameResult(context: Context, winner: String, difficulty: String) {
        val sharedPref = context.getSharedPreferences("GameHistory", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dateTime = dateFormat.format(Date())

        val result = "$dateTime : $winner : $difficulty"
        editor.putString(dateTime, result)
        editor.apply()
    }
}
