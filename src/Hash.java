/**
 * Created by Joseph Edmunds
 * 11/18/2015.
 */
public class Hash {
    private int[] table;
    private int probe;
    private double loadFactor;

    /**
     * Hash Constructor
     * @param size
     * @param loadFactor
     * @param probeType
     */
    public Hash(int size, int loadFactor, int probeType) {
        this.probe = probeType;
        this.loadFactor = loadFactor / 100;
        this.table = new int[size*loadFactor];
        //Setting the value of all indices to -5. This will be used to test if the index has already been hashed
        for (int i = 0; i < table.length; i++) {
            table[i] = -5;
        }
        this.fillTable(probeType);
    }

    /**
     * Fills the Hash Table
     * @param probeType Whether to fill the table through linear probing or quadratic probing
     */
    public void fillTable(int probeType) {
        int index;
        int currKey = Utilities.randomGenerator();
        for (int i = 0; i < table.length; i++) {
            index = currKey % 1019;
            if (table[index] == -5) {
                table[index] = currKey;
            }
            else { //I'm using the assumption that they only input a correct probe type
                if (probeType == 0) {
                    while (table[index] != -5) {
                        index = linearProbe(index);
                    }
                    table[index] = currKey;
                } else if (probeType == 1) {
                    while (table[index] != -5) {
                        index = quadraticProbe(index);
                    }
                    table[index] = currKey;
                }
            }
            currKey = Utilities.randomGenerator();
        }
    }

    /**
     * Instructions for linear probing
     * @param currIndex
     * @return
     */
    public int linearProbe(int currIndex) {
        currIndex++;
        if (currIndex == table.length)
            currIndex = 0;
        return currIndex;
    }

    /**
     * Instructions for quadratic probing
     * @param currIndex
     * @return
     */
    public int quadraticProbe(int currIndex) {
        return currIndex;
    }

    /**
     * Searching the table
     * @param hashTable
     * @param value
     * @param key
     * @param probeType
     * @param numProbes
     * @return
     */
    public int[] searchTable(int[] hashTable, int value, int key, int probeType, int numProbes) {
        int data[] = new int[2];
        int successfulSearch = -1;

        //Base-case: Found on first probe
        if (hashTable[key] == value) {
            numProbes = 1;
            successfulSearch = 1;
        }
        else {

                if (probeType == 0) {
                    searchTable(hashTable, value, linearProbe(key), probeType, numProbes++);
                }
                else if (probeType == 1) {
                    searchTable(hashTable, value, quadraticProbe(key), probeType, numProbes++);
                }
                else if (hashTable[key] == -5)
                    successfulSearch = 0;

        }
        data[0] = numProbes;
        data[1] = successfulSearch;
        return data;
    }

    /**
     * Finds the statistics and outputs them in an easy to read format
     * @param probeType
     */
    public void generateStats(int probeType) {
        int[] data;
        int[] successStats = new int[2];
        int[] failStats = new int[2];
        for (int i = 0; i < 10000; i++) {
            data = searchTable(table, i, i % 1019, probeType, 0);

            if (data[1] == 1) {
                successStats[0]++; //increment number of successful searches
                successStats[1] += data[0]; //add to the total number of probes used for successful searches
            }
            else {
                failStats[0]++; //increments the number of failed searches
                failStats[0] += data[0]; //add to the total number of probes used for failed searches
            }
        }

        double successAvg = successStats[1] / successStats[0];
        double failAvg = failStats[1] / failStats[0];

        System.out.printf("Total successes: %d\t\tAverage probes per: %f\n", successStats[0], successAvg);
        System.out.printf("Total failures: %d\t\tAverage probes per: %f\n", failStats[0], failAvg);
    }
}
