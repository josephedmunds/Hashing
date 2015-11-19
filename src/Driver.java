/**
 * Created by Joseph Edmunds
 * 11/18/2015.
 */
public class Driver {
    public static void main(String[] args) {
        //Array of size 2. Argument 1: Load factor. Argument 2: Probe Type
        //int[] arguments = Utilities.acceptInput();

        //Hash hash = new Hash(arguments[0], arguments[1], arguments[2]);

        //FOR TEST PURPOSES
        Hash hash = new Hash(1019, 80, 1);

        hash.generateStats(0);

    }
}
