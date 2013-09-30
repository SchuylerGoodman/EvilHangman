/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hangman;

import java.io.File;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author schuyler
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            EvilHangmanGame game = new EvilHangman();
            game.startGame(new File(args[0]), 5);
            Set<String> s = game.makeGuess('a');
            System.out.println(s.toString());
            System.out.println(s.size());
        } catch (EvilHangmanGame.GuessAlreadyMadeException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
