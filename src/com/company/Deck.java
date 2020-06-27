package com.company;

public class Deck {

    Card deck[] = new Card[52];

    //Creates a full deck, in order
    public Deck(){

        String suits[] = new String[4];
        suits[0] = "Spades";
        suits[1] = "Hearts";
        suits[2] = "Diamonds";
        suits[3] =  "Clubs";

        for(int i = 0; i < 4; i ++){
            for(int j = 0; j < 13; j ++){
                Card test = new Card(suits[i], j + 1);
                deck[(i*13 + j)] = test;
            }
        }
    }

    //Shuffles deck according to the Fisher-Yates shuffle
    public void shuffle(){

        Card temp = new Card();
        int j = 0;
        for(int i = 0; i < 52; i ++){
            j = randomWithRange(i, 51);
            temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
        }
    }

    //Prints deck in its entirety
    public void printDeck(){

        for(int i = 0; i < 52; i ++){
            System.out.println(deck[i].toString());
        }
    }

    public Card getCardAt(int index){
        return deck[index];
    }

    //Helper method
    private int randomWithRange(int min, int max){
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
}
