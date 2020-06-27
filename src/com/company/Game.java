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
    String scramble;

    final String avaibleChoices = "1234qwertyu";

    //TODO need to finish
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
        scramble = "b";

        generateOutput();

        return true;
    }

    //TODO need to remember to remove test cases
    public void runLoop() throws IOException {
       while (mIsRunning)
        {
            processInput();
            updateGame();
            generateOutput();
        }
        //testSmartMove();
    }

    private void testSmartMove(){
        LinkedList<Card> move = new LinkedList<>();
        LinkedList<Card> stay = new LinkedList<>();

        Card c1 = new Card("Hearts", 4);
        Card c2 = new Card("Spades", 3);
        Card c3 = new Card("Diamonds", 2);
        Card c4 = new Card("Spades", 1);

        Card c5 = new Card("Hearts", 13);
        Card c6 = new Card("Spades", 12);

        c1.flipCard();
        c1.reachCard();
        c2.flipCard();
        c2.reachCard();
        c3.flipCard();
        c3.reachCard();
        c4.flipCard();
        c4.reachCard();
        c5.flipCard();
        c5.reachCard();
        c6.flipCard();
        c6.reachCard();

        move.add(c1);
        move.add(c2);
        move.add(c3);
        move.add(c4);

        stay.add(c5);
        stay.add(c6);

        for(int i = 0; i < move.size(); i ++){
            System.out.print(move.get(i).displayCard() + " ");
        }
        System.out.println("");

        for(int j = 0; j < stay.size(); j ++){
            System.out.print(stay.get(j).displayCard() + " ");
        }
        System.out.println("");

        smartStack(move, stay);

        move = nesc[0];
        stay = nesc[1];

        for(int i = 0; i < move.size(); i ++){
            System.out.print(nesc[0].get(i).displayCard() + " ");
        }
        System.out.println("");

        for(int j = 0; j < stay.size(); j ++){
            System.out.print(nesc[1].get(j).displayCard() + " ");
        }
        System.out.println("");
    }

    //TODO need to add anything here?
    private void processInput() throws IOException {

        from = br.readLine();
        if(!from.equals("6") && !from.equals("p")){
            to = br.readLine();
        }

        if(avaibleChoices.indexOf(to)<0){
            System.out.println("Scrambled");
            from = scramble;
        }

    }

    //TODO all of this, make sure to include a win state, and a game over state;
    //Tested 6, p, default
    private void updateGame(){

        switch(from){
            case "6":
                drawNewHand();
                break;
            case "p":
                mIsRunning = false;
                break;
            case "1":
                moveAce(aceStack[0], translateKey(to));
                break;
            case "2":
                moveAce(aceStack[1], translateKey(to));
                break;
            case "3":
                moveAce(aceStack[2], translateKey(to));
                break;
            case "4":
                moveAce(aceStack[3], translateKey(to));
                break;
            case "5":
                smartStack(hand, translateKey(to));
            case "q":
                smartStack(playStack[0], translateKey(to));
                break;
            case "w":
                smartStack(playStack[1], translateKey(to));
                break;
            case "e":
                smartStack(playStack[2], translateKey(to));
                break;
            case "r":
                smartStack(playStack[3], translateKey(to));
                break;
            case "t":
                smartStack(playStack[4], translateKey(to));
                break;
            case "y":
                smartStack(playStack[5], translateKey(to));
                break;
            case "u":
                smartStack(playStack[6], translateKey(to));
                break;
            default:
                break;
        }

    }

    //TODO test when the game has been running for a bit
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
    //TODO I mean it works but i need to use pointers :(, also need to flip over the remaining card if its flipped
    //make a case for Ace?
    private void smartStack(LinkedList<Card> move, LinkedList<Card> stay){

        Card finish = stay.getLast();
        Card start;

        for(int i = 0; i < move.size(); i ++){
            start = move.get(i);
            if(start.faceUp()){
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
                        move.getLast().reachCard();
                    }
                }
            }
        }

        nesc[0] = move;
        nesc[1] = stay;
    }

    private void moveAce(LinkedList<Card> move, LinkedList<Card> stay){
        Card finish = stay.getLast();
        if(move.isEmpty()){
            move.add(stay.getLast());
        }
        else{
            Card start = move.getLast();

            if (checkCompatible(finish, start)) {
                stay.add(move.removeLast());
            }
        }

    }

    //If you're moving it to the ace stack, need to swap moving and inPlace !!!!!!!
    private boolean checkCompatible(Card moving, Card inPlace){
        return (inPlace.getRank() == moving.getRank() + 1 && inPlace.getColor() != moving.getColor());
    }

    //TODO Test(?)
    //Do I even need this?
    //If I decide to go back to the original way of doing it, like if its too cluttered, then ill need it
    private int numFlipped(LinkedList<Card> list){
        int flipped = 0;

        for (Card card : list) {
            if (!card.faceUp()) {
                flipped++;
            }
        }
        return flipped;
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
