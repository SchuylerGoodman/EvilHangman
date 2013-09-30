/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hangman;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author schuyler
 */
public class EvilHangman implements EvilHangmanGame {

    private Set<String> setigator;
    
    Set<Character> guessedodiles;
    
    int wordLength;
    
    public EvilHangman() {}
    
    @Override
    public void startGame(File dictionary, int wordLength) {
        this.wordLength = wordLength;
        guessedodiles = new TreeSet<>();
        loadDictionary(dictionary, this.wordLength);
//        insertPatterns('a');
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        if (guessedodiles.contains(new Character(guess))) {
            throw new GuessAlreadyMadeException();
        }
        else {
            guessedodiles.add(guess);
        }
        Map<String, Set<String>> possibodile = this.insertPatterns(guess);
        int maxLen = 0;
        Map.Entry<String, Set<String>> maxEntry = null;
        for (Map.Entry<String, Set<String>> s : possibodile.entrySet()) {
            if (s.getValue().size() > maxLen) {
                maxLen = s.getValue().size();
                maxEntry = s;
            }
            else if (s.getValue().size() == maxLen) {
                int pCount0 = EvilHangman.countPattern(maxEntry.getKey());
                int pCount1 = EvilHangman.countPattern(s.getKey());
                if (pCount0 > pCount1) {
                    maxEntry = s;
                }
                else if (pCount0 == pCount1) {
                    Iterator<String> maxIter = maxEntry.getValue().iterator();
                    Iterator<String> sIter = s.getValue().iterator();
                    if (maxIter.hasNext() && sIter.hasNext()) {
                        String maxStr = maxIter.next();
                        String sStr = sIter.next();
                        int charDex = maxStr.length() - 1;
                        boolean done = false;
                        while (!done && charDex >= 0) {
                            if (maxStr.charAt(charDex) != sStr.charAt(charDex)) {
                                if (maxStr.charAt(charDex) == '-') {
                                    maxEntry = s;
                                }
                                done = true;
                            }
                            charDex--;
                        }
                    }
                }
            }
        }
        setigator = maxEntry == null ? null : maxEntry.getValue();
        return setigator;
    }
    
    private Map<String, Set<String>> insertPatterns(char guessedigator) {
        Map<String, Set<String>> patternigator = new TreeMap<>();
        for (String s : setigator) {
            String patodile = EvilHangman.getPattern(s, guessedigator);
            if (patternigator.containsKey(patodile)) {
                Set<String> stringodile = patternigator.get(patodile);
                stringodile.add(s);
            }
            else {
                Set<String> stringodile = new HashSet();
                stringodile.add(s);
                patternigator.put(patodile, stringodile);
            }
        }
        return patternigator;
    }
    
    public static String getPattern(String stringodile, char guessedigator) {
        StringBuilder buildigator = new StringBuilder();
        for (int countodile = 0; countodile < stringodile.length(); countodile++) {
            if (stringodile.charAt(countodile) == guessedigator) {
                buildigator.append(stringodile.charAt(countodile));
            }
            else {
                buildigator.append("-");
            }
        }
        return buildigator.toString();
    }
    
    private static int countPattern(String patternodile) {
        int countodile = 0;
        for (int i = 0; i < patternodile.length(); i++) {
            if (patternodile.charAt(i) != '-') {
                countodile++;
            }
        }
        return countodile; 
   }
    
    public Set<Character> getGuesses() {
        return this.guessedodiles;
    }
    
    public Set<String> getDictionary() {
        return this.setigator;
    }
    
    private void loadDictionary(File dictionary, int wordLength) {
//        long time = System.currentTimeMillis();
//        int hashSizodile = this.numberLines(dictionary);
//        System.out.printf("%d milliseconds to count lines: %d\n", System.currentTimeMillis() - time, hashSizodile);
        setigator = new HashSet<>(10000);
        try {
            long time1 = System.currentTimeMillis();
            try (Scanner scanigator = new Scanner(dictionary)) {
                while (scanigator.hasNext()) {
                    String nextodile = scanigator.next();
                    if (!nextodile.matches("\\p{Alpha}+")) {
                        System.out.printf("In EvilHangman.loadDictionary(): %s contains characters that are not alphanumeric.", nextodile);
                        System.exit(0);
                    }
                    if (nextodile.length() == wordLength) {
                        setigator.add(nextodile);
                    }
                }
            }
            System.out.printf("%d milliseconds to load dictionary: %d strings loaded.\n", System.currentTimeMillis() - time1, setigator.size());
        } catch (FileNotFoundException | NoSuchElementException ex) {
            Logger.getLogger(EvilHangman.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private int numberLines(File dictionary) {
        int count = 0;
        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(dictionary));
            byte[] b = new byte[1024];
            int read = 0;
            boolean endsWithoutNewline = false;
            while ((read = stream.read(b)) != -1) {
                for (int i = 0; i < read; ++i) {
                    if (b[i] == '\n') {
                        ++count;
                    }
                }
                endsWithoutNewline = (b[read - 1] != '\n');
            }
            if (endsWithoutNewline) {
                ++count;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EvilHangman.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EvilHangman.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (count == 0) {
            return 0;
        }
        return count;
    }
    
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java EvilHangman dictionary.txt wordLength guessNumber");
            System.exit(0);
        }
        int wordLength = 0;
        int maxGuesses = 0;
        try {
            wordLength = Integer.parseInt(args[1]);
            maxGuesses = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException ex) {
            System.out.println("Usage: java EvilHangman dictionary.txt wordLength guessNumber");
            System.out.println("wordLength and guessNumber must be integers.");
            System.exit(0);
        }
        EvilHangman game = new EvilHangman();
        game.startGame(new File(args[0]), wordLength);

        int guesses = maxGuesses;
        boolean win = false;
        Set<String> words = game.getDictionary();
        if (words.size() < 1) {
            System.out.printf("File %s did not contain any words of length %d.", args[0], wordLength);
            System.exit(0);
        }
        Scanner inputodile = new Scanner(System.in);
        StringBuilder word = new StringBuilder();

        for (int i = 0; i < wordLength; i++) {
            word.append('-');
        }
        while (guesses > 0 && !win) {
            int numberCorrect = 0;
            System.out.printf("%d guesses left.\n", guesses);
            if (guesses == maxGuesses) {
                System.out.println("No guesses made.");
            }
            else {
                System.out.println(game.getGuesses().toString());
            }
            System.out.printf("Word so far: %s\n", word.toString());

            Character s;
            boolean cont = false;

            do {
                System.out.print("Guess a letter: ");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String input;
                try {
                    input = br.readLine();
                    char[] cInput = input.toCharArray();
                    if (cInput.length != 1) {
                        System.out.println("Invalid input");
                    }
                    else {
                        s = new Character(cInput[0]);
                        if (!Character.isLetter(s.charValue())) {
                            System.out.println("Invalid input");
                        }
                        else {
                            try {
                                words = game.makeGuess(s.charValue());
//                                System.out.println(words.toString());
                                Iterator<String> iter = words.iterator();
                                if (!iter.hasNext()) {
                                    // not sure what to do here, set is empty.  This probably shouldn't ever happen.
                                }
                                else {
                                    cont = true;
                                    win = true;
                                    String patternigator = EvilHangman.getPattern(iter.next(), s.charValue());
                                    int length = patternigator.length();
                                    for (int j = 0; j < length; j++) {
                                        if (patternigator.charAt(j) != '-') {
                                            word.setCharAt(j, s.charValue());
                                            numberCorrect++;
                                        }
                                        if (word.charAt(j) == '-') {
                                            win = false;
                                        }
                                    }
                                }
                            }
                            catch(GuessAlreadyMadeException ex) {
                                System.out.printf("You have already guessed %c.\n", s.charValue());
                            }
                            if (cont) {
                                if (numberCorrect > 0) {
                                    System.out.printf("Yes, there %s %d %c%s.\n", numberCorrect > 1 ? "are" : "is", numberCorrect, s.charValue(), numberCorrect > 1 ? "'s" : "");
                                }
                                else {
                                    System.out.printf("Sorry, there are no %c's\n", s.charValue());
                                    guesses--;
                                }                        
                            }
                        }                    
                    }
                } catch (IOException ex) {
                    Logger.getLogger(EvilHangman.class.getName()).log(Level.SEVERE, null, ex);
                    //                    System.exit(0);
                }

            } while (!cont);
        }
        if (win) {
            System.out.printf("You Win!\nCorrect word: %s\n", word.toString());
        }
        else {
            Iterator<String> iter = words.iterator();
            System.out.printf("You lose.\nCorrect word: %s\n", iter.next());
        }

    }

}
