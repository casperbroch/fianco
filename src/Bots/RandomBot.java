package Bots;
import java.util.Random;

public class RandomBot {
    
    public RandomBot() {

    }

    public String makeMove() {
        Random random = new Random();

        // Generate random letter between 'a' and letterBound
        char randomLetter = (char) ('a' + random.nextInt('i' - 'a' + 1));

        // Generate random number between 1 and numberBound
        int randomNumber = 1 + random.nextInt(9);

        // Return the combination of letter and number as a string
        return randomLetter + String.valueOf(randomNumber);
    }

}
