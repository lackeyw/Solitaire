package com.company;

public class Card {

    private String suit;
    private int rank;
    private boolean isReachable;
    private boolean faceUp;


    public Card() {
        this.suit = "Spades";
        this.rank = 1;
    }

    public Card(String suit, int rank) {
        this.suit = suit;
        this.rank = rank;
        this.faceUp = false;
        this.isReachable = false;
    }

    //Returns true if the card is facing up
    public boolean faceUp() {
        return faceUp;
    }

    //Returns true if the card is reachable
    public boolean isReachable() {
        return isReachable;
    }

    public String getSuit(){
        return suit;
    }

    public int getRank() {
        return rank;
    }

    public void flipCard() {
        faceUp = !faceUp;
    }

    public void reachCard() {
        isReachable = !isReachable;
    }

    //Converts card to plain text format
    public String toString() {
        if(rank < 2){
            return "Ace of " + suit;
        }
        else if(rank > 10){
            if(rank == 11){
                return "Jack of " + suit;
            }
            else if(rank == 12) {
                return "Queen of " + suit;
            }
            else {
                return "King of " + suit;
            }
        }
        else {
            return rank + " of " + suit;
        }
    }

    //Returns the color of the card, red = true and black = false
    public boolean getColor(){
        return suit.equals("Hearts") || suit.equals("Diamonds");
    }

    //Converts card to how it is displayed in-game
    public String displayCard(){

        String displayCard;

        char[] cat = new char[5];
        if(isReachable){
            cat[0] = '|';
            cat[4] = '|';
        }
        else {
            cat[0] = '{';
            cat[4] = '}';
        }

        if(faceUp) {
            switch (suit) {
                case "Diamonds":
                    cat[1] = 'D';
                    break;
                case "Hearts":
                    cat[1] = 'H';
                    break;
                case "Spades":
                    cat[1] = 'S';
                    break;
                default:
                    cat[1] = 'C';
                    break;
            }

            if (rank == 1) {
                cat[2] = ' ';
                cat[3] = 'A';
            } else if (rank > 1 && rank < 10) {
                cat[2] = ' ';
                cat[3] = (char) (rank + '0');
            } else if (rank == 10) {
                cat[2] = '1';
                cat[3] = '0';
            } else if (rank == 11) {
                cat[2] = ' ';
                cat[3] = 'J';
            } else if (rank == 12) {
                cat[2] = ' ';
                cat[3] = 'Q';
            } else {
                cat[2] = ' ';
                cat[3] = 'K';
            }
        }
        else {
            cat[0] = '[';
            cat[1] = ' ';
            cat[2] = ' ';
            cat[3] = ' ';
            cat[4] = ']';
        }

        displayCard = String.valueOf(cat);
        return displayCard;

    }
}
