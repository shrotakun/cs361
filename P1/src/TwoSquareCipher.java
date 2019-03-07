// Alex Leung, akl973
// Sai Shreyas Medapalli srm3683
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;

public class TwoSquareCipher {
    // matrices for keywords and ciphers
    private static String[][] square1;
    private static String[][] square2;
    private static final int DIMENSIONS = 5;

    public static String[][] makeSquare(String keyword) {

        // create matrix out of keyword
        String[][] temp = new String[DIMENSIONS][DIMENSIONS];
        String lettersSoFar = "";
        keyword = keyword.replaceAll("[^a-zA-Z]","");
        int wordLen = keyword.length();
        int rowIndex = 0;
        int colIndex = 0;
        int index = 0;

        // replace nulls in 2D array with letters of the keyword that have not been
        // used yet (tracked by lettersSoFar)
        while(index < wordLen) {
            String s = keyword.charAt(index) + "";
            if(!lettersSoFar.contains(s) && !s.equals("Q") && !s.equals(" ")) {
                temp[rowIndex][colIndex] = s;
                lettersSoFar += s;
                colIndex++;
                if(colIndex > DIMENSIONS-1) {
                    colIndex = 0;
                    rowIndex++;
                }
            }
            index++;
        }

        // add in the rest of the alphabet and omit letters already
        // in the matrix and the letter Q
        String alphabet = "ABCDEFGHIJKLMNOPRSTUVWXYZ";
        String lettersLeft = "";

        for(int i=0; i<DIMENSIONS*DIMENSIONS; i++) {
            String s = alphabet.charAt(i) + "";
            if(!lettersSoFar.contains(s) && !s.equals("Q")) {
                lettersLeft += s;
            }
        }

        index = 0;
        for(int i=rowIndex; i<temp.length; i++) {
            if(i != rowIndex) {
                colIndex=0;
            }
            for(int j=colIndex; j<temp[0].length; j++) {
                temp[i][j] = lettersLeft.charAt(index) + "";
                index++;
            }
        }
        return temp;
    }

    // replace brackets and unwanted characters such as Q or nonalphanumeric
    public static String modifyMessage(String word) {
        word = word.replaceAll("\\s+", "");
        word = word.replaceAll("[^a-zA-Z]","");
        word = word.replaceAll("Q", "");
        // If odd length, add the letter Z to even if out
        if(word.length() % 2 != 0) {
            word += "Z";
        }
        return word;
    }

    // Converts the message into a Digraph
    public static String[][] turnDigraph(String word) {
        int length = word.length();
        String[][] digraph = new String[length/2][2];
        int index = 0;

        for(int i=0; i<digraph.length; i++) {
            for(int j=0; j<digraph[0].length; j++) {
                String letter = word.charAt(index) + "";
                //Get rid of spaces
                if(!letter.equals(" ")) {
                    digraph[i][j] = letter;
                }
                index++;
            }
        }
        return digraph;
    }

    // Finds the index of a letter in whatever key matrix
    public static int[] findIndex(String s, Boolean squareNum) {
        int[] index = new int[2];
        String[][] square;
        if(squareNum == true) {
            square = square1;
        } else {
            square = square2;
        }
        for(int i=0; i<square.length; i++) {
            for(int j=0; j<square[0].length; j++) {
                String letter = square[i][j];
                if(letter.equals(s)) {
                    index[0] = i;
                    index[1] = j;
                    return index;
                }
            }
        }
        return index;
    }

    // Encrypts and decrypts the digraph passed
    public static String[][] encrypt(String[][] digraph) {
        String[][] encryptedDigraph = new String[digraph.length][digraph[0].length];
        for(int i=0; i<digraph.length; i++) {
            // first character index will be found in the top matrix
            String firstChar = digraph[i][0];
            int[] index1 = findIndex(firstChar, true);

            // second character index will be found in the bottom matrix
            String secondChar = digraph[i][1];
            int[] index2 = findIndex(secondChar, false);

            // Encrypted digraph is found by swapping the indicies
            // The first one takes the index of the first char and the second char
            // The second one takes the index of the second char and the first char
            encryptedDigraph[i][0] = square1[index1[0]][index2[1]];
            encryptedDigraph[i][1] = square2[index2[0]][index1[1]];
        }
        return encryptedDigraph;
    }

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Would you like to encrypt a message? If so, please enter Yes.");
        String answer = scan.nextLine().toUpperCase();

        while(answer.equals("YES")) {
            System.out.println("Please input the first keyword");
            String key1 = scan.nextLine().toUpperCase();
            square1 = makeSquare(key1);

            System.out.println("Please input the second keyword");
            String key2 = scan.nextLine().toUpperCase();
            square2 = makeSquare(key2);

            System.out.println("What's the message?");
            String message = scan.nextLine().toUpperCase();
            message = modifyMessage(message);

            String[][] digraph = turnDigraph(message);
            String[][] encryptedDigraph = encrypt(digraph);
            String[][] decryptedDigraph = encrypt(encryptedDigraph);

            String encrypted = Arrays.deepToString(encryptedDigraph);
            encrypted = encrypted.replaceAll("\\[", "").replaceAll("\\]","");
            encrypted = encrypted.replaceAll("\\s+", "");
            encrypted = encrypted.replaceAll(",", "");

            String decrypted = Arrays.deepToString(decryptedDigraph);
            decrypted = decrypted.replaceAll("\\[", "").replaceAll("\\]","");
            decrypted = decrypted.replaceAll("\\s+", "");
            decrypted = decrypted.replaceAll(",", "");


            System.out.println("Would you like to see the encrypted message? Enter yes, if so.");
            answer = scan.nextLine().toUpperCase();

            if(answer.equals("YES")) {
                System.out.println(encrypted);
            }

            System.out.println("Would you like to decrypt the message? Enter yes, if so.");
            answer  = scan.nextLine().toUpperCase();

            if(answer.equals("YES")) {
                System.out.println("Would you like to see the decrypted message? Enter yes, if so.");
                answer  = scan.nextLine().toUpperCase();

                if(answer.equals("YES")) {
                    System.out.println(decrypted);
                }
            }

            System.out.println("Would you like to encrypt another message? Enter yes, if so.");
            answer = scan.nextLine().toUpperCase();

        }

        System.out.println("See you around!");
    }

}
