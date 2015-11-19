/**
 * Created by Joseph Edmunds
 * 11/18/2015.
 */
public class Hash {
    private int[] table;
    private int probe;
    private double load;

    /**
     * Constructor for the Hash Table
     * @param size The size of the hash table
     * @param loadFactor The load factor of the hash table
     * @param probeType The probe type, either linear or quadratic for this project
     */
    public Hash(int size, int loadFactor, int probeType) {
        this.probe = probeType;
        this.table = new int[size];
        this.load = loadFactor;
        //Setting the value of all indices to -5. This will be used to test if the index has already been hashed
        for (int i = 0; i < table.length; i++) {
            table[i] = -5;
        }
        this.fillTable(probeType, (int) (size * (load/100)));
    }

    /**
     * Fills the Hash Table
     * @param probeType Whether to fill the table through linear probing or quadratic probing
     * @param fillSize The amount of the table that will be filled, based on the load factor
     */
    public void fillTable(int probeType, int fillSize) {
        int index;
        int currKey;

        for (int i = 0; i < fillSize; i++) {
            currKey = Utilities.randomGenerator();
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
                    index = quadraticProbe(index, table, currKey)[0];
                    table[index] = currKey;
                }
            }
        }
    }

    /**
     * Instructions for linear probing
     * @param currIndex The index currently being inspected
     * @return The next index to be inspected
     */
    public int linearProbe(int currIndex) {
        currIndex++;
        if (currIndex == table.length)
            currIndex = 0;
        return currIndex;
    }

    /**
     * Instructions for quadratic probing
     * @param startIndex The index the quadratic probe will start from
     * @param hashTable The hash table
     * @param value The value being searched for in the table
     * @return An array of the next index to test and the variance use to find that index
     */
    public int[] quadraticProbe(int startIndex, int[] hashTable, int value) {
        int[] info = new int[2];
        int variance = 1;
        int diff = 0;

        while (hashTable[startIndex] != -5) {
            if (hashTable[startIndex] == value){
                break; //Found
            }
            if (variance > 1) {
                diff = (int)Math.pow((double)variance, 2.0) % 1019;
                startIndex += diff;
                variance *= -1;
            }
            else {
                diff = (int)Math.pow((double)variance, 2.0) % 1019;
                startIndex -= diff;
                while (startIndex < 0)
                    startIndex += 1019;
                variance -= 1;
            }
        }
        info[0] = startIndex;
        info[1] = variance;
        return info;
    }

    /**
     * A method to search the hash table to find a value
     * @param hashTable The hash table being searched
     * @param value The value being looked for in the table
     * @param key The key where the value is being looked for
     * @param probeType The type of probing that will be used, if necessary, to search for the value
     * @param data An array that holds the number of probes used to find the value, and whether or not the search was successful
     * @return The data array
     */
    public int[] searchTable(int[] hashTable, int value, int key, int probeType, int[] data) {
        data[0]++; //Increment the number of probes every time this method is called. Will stop accumulating when the loops break.
        int successfulSearch;
        int type = probeType;

        //Base-case: Found on first probe
        if (hashTable[key] == value) {
            successfulSearch = 1;
            data[1] = successfulSearch;
        }
        else if (hashTable[key] == -5) {
            successfulSearch = 0;
            data[1] = successfulSearch;
        }
        else {
                if (type == 0) {
                    data = searchTable(hashTable, value, linearProbe(key), type, data);
                }
                else if (type == 1) {
                    data = searchTable(hashTable, value, quadraticProbe(key, hashTable, value)[0], type, data);
                }
        }

        return data;
    }

    /**
     * Finds the statistics of a linear probe and outputs them in an easy to read format
     * @param probeType The type of probing being used
     */
    public void generateStatsLinear(int probeType) {
        int[] data = new int[2];
        double[] successStats = new double[2];
        double[] failStats = new double[2];
        for (int i = 1; i <= 10000; i++) {
            data[0] = 0;
            data = searchTable(table, i, i % 1019, probeType, data);

            if (data[1] == 1) {
                successStats[0]++; //increment number of successful searches
                successStats[1] += data[0]; //add to the total number of probes used for successful searches
            }
            else {
                failStats[0]++; //increments the number of failed searches
                failStats[1] += data[0]; //add to the total number of probes used for failed searches
            }
        }

        double failAvg = failStats[1] / failStats[0];
        System.out.printf("Average probes per failure: %f\n", failAvg);

        double successAvg = (successStats[1]) / successStats[0];
        System.out.printf("Average probes per success: %f\n", successAvg);

    }

    /**
     * Finds the statistics of a quadratic probe and outputs them in an easy to read format
     * @param probeType The type of probing being used
     */
    public void generateStatsQuadratic(int probeType) {
        int[] data = new int[2];
        double[] successStats = new double[2];
        double[] failStats = new double[2];
        for (int i = 1; i <= 10000; i++) {
            data[0] = 0;
            data = searchTable(table, i, i % 1019, probeType, data);

            if (data[1] == 1) {
                successStats[0]++; //increment number of successful searches
                int hold = quadraticProbe(i % 1019, table, i)[1];
                if (hold <= 0)
                    successStats[1] += -hold*2 + 1;
                else
                    successStats[1] += hold*2;
            }
            else {
                failStats[0]++; //increments the number of failed searches
                int hold = quadraticProbe(i % 1019, table, i)[1];
                if (hold <= 0)
                    failStats[1] += -hold*2 + 1;
                else
                    failStats[1] += hold*2;
            }
        }

        double failAvg = failStats[1] / failStats[0];
        System.out.printf("Average probes per failure: %f\n", failAvg);

        double successAvg = (successStats[1]) / successStats[0];
        System.out.printf("Average probes per success: %f\n", successAvg);
    }
}
