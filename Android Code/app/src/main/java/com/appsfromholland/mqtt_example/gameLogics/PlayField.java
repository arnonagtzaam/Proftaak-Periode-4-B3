package com.appsfromholland.mqtt_example.gameLogics;

import java.io.Serializable;
import java.util.ArrayList;
/*

Deze klasse maakt een speelveld die de twee spelers gebruiken.
Daarnaast kijkt deze klasse of het speelveld een combinatie bevat waarop een speciale actie moet worden uitgevoerd. Bijvoorbeeld: iemand heeft gewonnnen.
 */
public class PlayField {

    private ArrayList<Square> squares;

    public PlayField(){
        squares = createPlayField();
    }

    private ArrayList<Square> createPlayField(){
        ArrayList<Square> squares = new ArrayList<>();

        for (int i = 0; i < 9; i++){
            squares.add(new Square(i,Square.SquareProperty.BLANK));
        }
        return squares;
    }

    /**
     * Sets the correct property for the correct square in the playing field
     * @param position
     * @param property
     */
    public void playSquare(int position, Square.SquareProperty property){
        squares.get(position).setProperty(property);
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    /**
     * @return null if Playfield is still playable
     */

    public Square[] checkPlayField(){

        //checks top horizontal row
        if (squares.get(0).getProperty() == squares.get(1).getProperty() && squares.get(1).getProperty() == squares.get(2).getProperty() && squares.get(0).getProperty() != Square.SquareProperty.BLANK){
            Square[] squareRow = new Square[3];
            squareRow[0] = this.squares.get(0);
            squareRow[1] = this.squares.get(1);
            squareRow[2] = this.squares.get(2);
            return squareRow;
        }
        //checks middle horizontal row
        else if (squares.get(3).getProperty() == squares.get(4).getProperty() && squares.get(4).getProperty() == squares.get(5).getProperty() && squares.get(3).getProperty() != Square.SquareProperty.BLANK){
            Square[] squareRow = new Square[3];
            squareRow[0] = this.squares.get(3);
            squareRow[1] = this.squares.get(4);
            squareRow[2] = this.squares.get(5);
            return squareRow;
        }
        //checks bottom horizontal row
        else if (squares.get(6).getProperty() == squares.get(7).getProperty() && squares.get(7).getProperty() == squares.get(8).getProperty() && squares.get(6).getProperty() != Square.SquareProperty.BLANK){
            Square[] squareRow = new Square[3];
            squareRow[0] = this.squares.get(6);
            squareRow[1] = this.squares.get(7);
            squareRow[2] = this.squares.get(8);
            return squareRow;
        }
        //checks left vertical column
        else if (squares.get(0).getProperty() == squares.get(3).getProperty() && squares.get(3).getProperty() == squares.get(6).getProperty() && squares.get(0).getProperty() != Square.SquareProperty.BLANK){
            Square[] squareRow = new Square[3];
            squareRow[0] = this.squares.get(0);
            squareRow[1] = this.squares.get(3);
            squareRow[2] = this.squares.get(6);
            return squareRow;
        }
        //checks middle vertical column
        else if (squares.get(1).getProperty() == squares.get(4).getProperty() && squares.get(4).getProperty() == squares.get(7).getProperty() && squares.get(1).getProperty() != Square.SquareProperty.BLANK){
            Square[] squareRow = new Square[3];
            squareRow[0] = this.squares.get(1);
            squareRow[1] = this.squares.get(4);
            squareRow[2] = this.squares.get(7);
            return squareRow;
        }
        //checks right vertical column
        else if (squares.get(2).getProperty() == squares.get(5).getProperty() && squares.get(5).getProperty() == squares.get(8).getProperty() && squares.get(2).getProperty() != Square.SquareProperty.BLANK){
            Square[] squareRow = new Square[3];
            squareRow[0] = this.squares.get(2);
            squareRow[1] = this.squares.get(5);
            squareRow[2] = this.squares.get(8);
            return squareRow;
        }
        //checks left top to right bottom slope
        else if (squares.get(0).getProperty() == squares.get(4).getProperty() && squares.get(4).getProperty() == squares.get(8).getProperty() && squares.get(0).getProperty() != Square.SquareProperty.BLANK){
            Square[] squareRow = new Square[3];
            squareRow[0] = this.squares.get(0);
            squareRow[1] = this.squares.get(4);
            squareRow[2] = this.squares.get(8);
            return squareRow;
        }
        //checks right top to left bottom slope
        else if (squares.get(2).getProperty() == squares.get(4).getProperty() && squares.get(4).getProperty() == squares.get(6).getProperty() && squares.get(2).getProperty() != Square.SquareProperty.BLANK){
            Square[] squareRow = new Square[3];
            squareRow[0] = this.squares.get(2);
            squareRow[1] = this.squares.get(4);
            squareRow[2] = this.squares.get(6);
            return squareRow;
        }else {
            return null;
        }



    }
}
