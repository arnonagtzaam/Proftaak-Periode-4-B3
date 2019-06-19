package com.appsfromholland.mqtt_example.gameLogics;


import java.io.Serializable;
/*
Deze klasse houdt de status van het spelbord bij die te zien is op de app. Alle opdrachten die worden verstuurd via de server worden hier gecontroleerd.
Pas wanneer de opdracht goed is doorgekomen kan de volgende speler zijn beurt doen. -> playerCounter houdt bij wie er aan zet is.
 */
public class TicTacToeGame {

    private int playerCounter = 1;
    private PlayField playField;
    private boolean gameStatus;
    private int turnCounter;

    public TicTacToeGame() {
        playField = new PlayField();
        this.gameStatus = true;
        this.turnCounter = 0;
    }

    public int getPlayerTurn() {
        return playerCounter;
    }

    public int getPlayerCounter() {
        return playerCounter;
    }

    public PlayField getPlayField() {
        return this.playField;
    }

    public void setPlayerCounter() {
        if (gameStatus){
            if (playerCounter == 1) {
                playerCounter = 2;
            } else {
                playerCounter = 1;
            }
        }
    }

    public void checkGameStatus(int matrixNumber) {
        if (gameStatus) {
            for (Square square : playField.getSquares()) {
                if (getPlayerCounter() == 1) {
                    if (square.getPosition() == matrixNumber && !square.isClicked()) {
                        this.playField.playSquare(matrixNumber, Square.SquareProperty.RED);
                        square.setClicked();
                        System.out.println("Red added");
                        setPlayerCounter();
                        this.turnCounter++;
                    }
                } else if (getPlayerCounter() == 2) {
                    if (square.getPosition() == matrixNumber && !square.isClicked()) {
                        this.playField.playSquare(matrixNumber, Square.SquareProperty.BLUE);
                        square.setClicked();
                        System.out.println("Blue added");
                        setPlayerCounter();
                        this.turnCounter++;
                    }
                }
            }
        }
        if(turnCounter >= 9){
            this.gameStatus = false;
            this.playerCounter = 0; //gelijkspel
        }

        if (this.playField.checkPlayField() != null) {
            this.gameStatus = false;
        }
    }

    public boolean isGameStatus() {
        return gameStatus;
    }
}
