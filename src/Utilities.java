import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Joseph Edmunds
 * 11/18/2015.
 */
public class Utilities {
    /**
     * Generates a random number between 1 and 10,000 to be hashed into the table
     *
     * @return A random integer between 1 and 10,000
     */
    public static int randomGenerator() {
        LinkedList<Integer> keys = new LinkedList<>(); //a linked list to hold all keys used
        int num;
        Random rand = new Random();
        num = rand.nextInt(10000) + 1;
        while (keys.contains(num)) //generates a new key until it is unique
            num = rand.nextInt(10000) + 1;
        keys.add(num);
        return num;
    }

    /**
     * Prompts the user to input the necessary parameters: The load factor and type of probing
     *
     * @return An array that holds the input values
     */
    public static int[] acceptInput() {
        int[] inputs = new int[3];
        Scanner scanMan = new Scanner(System.in);
        System.out.println("How big is your hash table? ");
        inputs[0] = scanMan.nextInt();
        System.out.println("What load factor do you wish to use? (Enter as 0-100)");
        inputs[1] = scanMan.nextInt();
        System.out.println("Enter 0 for linear probing.\nEnter 1 for quadratic probing.");
        inputs[2] = scanMan.nextInt();
        return inputs;
    }
}
