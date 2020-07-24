package com.company;

import sun.awt.image.ImageWatched;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Game {

    LinkedList<Card>[] aceStack = new LinkedList[4];
    LinkedList<Card>[] nesc = new LinkedList[2];

    LinkedList<Card> playDeck = new LinkedList();
    LinkedList<Card> hand = new LinkedList();

    LinkedList<Card>[] playStack = new LinkedList[7];

    Deck deck;

    private boolean mIsRunning;
    private boolean mWon;
    private boolean mGameOver;

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String to;
    String from;

    final String availableChoices = "123456qwertyup";
    final String aceNums = "1234";

    public Game() {

        //Make sure the play stack is initialized
        for(int i = 0; i < 7; i ++){
            playStack[i] = new LinkedList<>();
        }

        //Make sure the ace stack is initialized
        for(int i = 0; i < 4; i++){
            aceStack[i] = new LinkedList<>();
        }

        for(int i = 0; i < 2; i++){
            nesc[i] = new LinkedList<>();
        }

    }

    public boolean initialize(){

        deck = new Deck();
        deck.shuffle();
        deck.shuffle();
        deck.shuffle();

        //For play area setup
        Card[][] temp = new Card[7][7];
        int count = 0;
        for(int j = 0; j < 7; j++){
            for(int k = j; k < 7; k++){

                temp[j][k] = deck.getCardAt(count);
                count++;
            }
        }

        for(int i = count; i < 52; i ++){
            playDeck.add(deck.getCardAt(i));
        }

        //Input into the play area
        Card card;
        for(int i = 0; i < 7; i ++){
            for(int j = 0; j < i+1; j++){
                card = temp[j][i];
                if(j==6){
                    card.flipCard();
                    card.reachCard();
                }
                else if(temp[j+1][i] == null) {
                    card.flipCard();
                    card.reachCard();
                }
                playStack[i].add(card);
            }
        }

        drawNewHand();
        mIsRunning = true;
        mGameOver = false;
        mWon = false;
        from = "";
        to = "";

        generateOutput();

        return true;
    }

    public void runLoop() throws IOException {
       while (mIsRunning)
        {
            processInput();
            updateGame();
            generateOutput();
        }
    }

    private void processInput() throws IOException {

        from = br.readLine();
        if(!from.equals("6") && !from.equals("p")){
            to = br.readLine();
        } else {
            to = "5";
        }

    }

    private void updateGame(){

        if(availableChoices.contains(from) && availableChoices.contains(to)){
            if(aceNums.contains(to)){
                moveAce(translateKey(from),translateKey(to));
            }
            else if(from.equals("6")){
                drawNewHand();
            }
            else if(from.equals("p")){
                mIsRunning = false;
            }
            else {
                smartStack(translateKey(from), translateKey(to));
            }
        }
        if(aceStack[0].size() == 13 && aceStack[1].size() == 13 && aceStack[2].size() == 13 && aceStack[3].size() == 13){
            mWon = true;
        }
    }

    //Displays the play table in the console
    private void generateOutput(){

        if(mWon){
            System.out.println("You Won!");
            mIsRunning = false;
        }
        else if(mGameOver){
            System.out.println("No moves left, Game Over");
            mIsRunning = false;
        }
        else {

            String emptySpace = "     ";
            String emptyAce = "[ A ]";

            //Display ace stacks
            for (int i = 0; i < 4; i++) {
                if (aceStack[i].isEmpty()) {
                    System.out.print(emptyAce + " ");
                } else {
                    System.out.print(aceStack[i].getLast().displayCard() + " ");
                }
            }

            //Display the hand
            for (int i = 0; i < 3; i++) {
                if (i >= hand.size()) {
                    System.out.print(emptySpace + " ");
                } else {
                    System.out.print(hand.get(i).displayCard() + " ");
                }
            }

            System.out.print("\n");

            System.out.print("( 1 ) ");
            System.out.print("( 2 ) ");
            System.out.print("( 3 ) ");
            System.out.print("( 4 ) ");
            System.out.print("      ");
            System.out.print("      ");
            System.out.print("( 5 ) ");

            System.out.print("\n");

            //Finds the longest individual stack to use as the for loop limit
            int val = 0;
            for (int i = 0; i < playStack.length; i++) {
                if (playStack[i].size() > val) {
                    val = playStack[i].size();
                }
            }

            //Display the play stack
            for (int j = 0; j < val; j++) {
                for (int i = 0; i < 7; i++) {
                    try {
                        System.out.print(playStack[i].get(j).displayCard() + " ");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.print(emptySpace + " ");
                    }
                }
                System.out.print("\n");
            }

            System.out.print("\n");

            System.out.print("( q ) ");
            System.out.print("( w ) ");
            System.out.print("( e ) ");
            System.out.print("( r ) ");
            System.out.print("( t ) ");
            System.out.print("( y ) ");
            System.out.print("( u ) ");
            System.out.print("\n");
        }
    }

    //Draws three new cards into the hand, puts the previous ones on the bottom of the deck
    private void drawNewHand(){
        int size;
        Card tempCard = new Card();

        //Flips the cards in the hand over and puts them at the "bottom" of the playing deck
        if(!hand.isEmpty()){
            size = hand.size();
            for(int i = 0; i < size; i++){
                tempCard = hand.removeFirst();
                tempCard.flipCard();
                if(tempCard.isReachable()){
                    tempCard.reachCard();
                }
                playDeck.add(tempCard);
            }
        }
        else {
            System.out.println("hand is empty");
        }

        //Draws the top three or however remaining cards, flips them over, lets the last one be reachable
        if(!playDeck.isEmpty()){

            size = playDeck.size();
            if(size >= 3) {
                size = 3;
            }
            for(int i = 0; i < size; i++){
                tempCard = playDeck.removeFirst();
                tempCard.flipCard();
                if(i == size - 1){
                    tempCard.reachCard();
                }
                hand.add(tempCard);
            }

        }
    }

    //Determines what cards to move from the moving stack to the destination
    private void smartStack(LinkedList<Card> move, LinkedList<Card> stay){
        Card start;
        Card finish;

        if(stay.isEmpty()){
            finish = new Card("Spades",14);
        }
        else{
            finish = stay.getLast();
        }

        if(!move.isEmpty()){
            for(int i = 0; i < move.size(); i ++){
                start = move.get(i);
                if(start.faceUp() && start.isReachable()){
                    if(checkCompatible(start, finish)){
                        int temp1 = move.size();
                        int temp2 = move.indexOf(start);
                        for(int j = 0; j < temp1 - temp2; j++){
                            stay.add(move.remove(temp2));
                        }
                        if(!move.isEmpty()){
                            if(!move.getLast().faceUp()){
                                move.getLast().flipCard();
                            }
                            if(!move.getLast().isReachable()){
                                move.getLast().reachCard();
                            }
                        }
                    }
                }
            }
        }

        nesc[0] = move;
        nesc[1] = stay;
    }

    private void moveAce(LinkedList<Card> move, LinkedList<Card> stay){
        if(!move.isEmpty()){
            Card start = move.getLast();
            if(!stay.isEmpty()){
                Card finish = stay.getLast();
                if (checkAceComp(start, finish)) {
                    stay.add(move.removeLast());
                    if(!move.isEmpty()){
                        if(!move.getLast().faceUp()){
                            move.getLast().flipCard();
                        }
                        if(!move.getLast().isReachable()){
                            move.getLast().reachCard();
                        }
                    }
                }
            } else if(move.getLast().getRank() == 1){
                stay.add(move.removeLast());
                if(!move.isEmpty()){
                    if(!move.getLast().faceUp()){
                        move.getLast().flipCard();
                    }
                    move.getLast().reachCard();
                }
            }
        }
    }

    private boolean checkCompatible(Card moving, Card inPlace){
        return (inPlace.getRank() == moving.getRank() + 1 && (inPlace.getColor() != moving.getColor() || inPlace.getRank() == 14));
    }
    //Specific case for moving to an ace stack
    private boolean checkAceComp(Card moving, Card inPlace){
        return (moving.getRank() == inPlace.getRank() + 1 && inPlace.getSuit() == moving.getSuit());
    }
    
    private LinkedList<Card> translateKey(String input){
        LinkedList<Card> value = new LinkedList<>();
        switch(input){
            case "1":
                value = aceStack[0];
                break;
            case "2":
                value = aceStack[1];
                break;
            case "3":
                value = aceStack[2];
                break;
            case "4":
                value = aceStack[3];
                break;
            case "5":
                value = hand;
                break;
            case "q":
                value = playStack[0];
                break;
            case "w":
                value = playStack[1];
                break;
            case "e":
                value = playStack[2];
                break;
            case "r":
                value = playStack[3];
                break;
            case "t":
                value = playStack[4];
                break;
            case "y":
                value = playStack[5];
                break;
            case "u":
                value = playStack[6];
                break;
        }
        return value;
    }

}
