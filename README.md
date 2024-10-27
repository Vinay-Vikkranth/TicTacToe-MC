# TicTacToe-MC

This project is a Tic-Tac-Toe game developed for Android/iOS where a human player can compete against an AI opponent. The AI is built using the Minimax algorithm with alpha-beta pruning, providing a challenging game experience across different difficulty levels. The application is designed to work efficiently on mobile platforms and aims to demonstrate the use of AI in mobile gaming.

Game Overview
In this Tic-Tac-Toe game:

The user plays as "X" and the AI as "O".
The game consists of a 3x3 grid where players take turns marking spaces.
The first player to align three of their marks in a row, column, or diagonal wins.
If the grid is filled with no winner, the game is declared a draw.
Game Features
Difficulty Modes

Easy: AI chooses moves randomly.
Medium: AI picks optimal moves 50% of the time and random moves 50% of the time.
Hard: AI uses the Minimax algorithm with alpha-beta pruning to make optimal moves every time.
Game Persistence

The app stores the date, winner, and difficulty mode of past games, allowing players to view a history of their matches.
Game data is stored locally, ensuring it remains available after closing the app.
User Interface Screens

Game Screen: Displays the Tic-Tac-Toe board where the user can play against the AI.
Settings Screen: Allows users to adjust the difficulty level.
Past Games Screen: Displays a list of past games with details such as date, winner, and difficulty level.
Requirements
Android/iOS Device: The game is playable on both Android and iOS platforms.
AI Opponent: The AI opponent leverages the Minimax algorithm with alpha-beta pruning to ensure efficient and optimized gameplay.
Additional Features
Game Reset: Players can reset the game at any time.
Difficulty Adjustment: The user can change the difficulty mode at any point during gameplay.
