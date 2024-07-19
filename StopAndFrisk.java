import java.util.ArrayList;

/**
 * The StopAndFrisk class represents stop-and-frisk data, provided by
 * the New York Police Department (NYPD), that is used to compare
 * during when the policy was put in place and after the policy ended.
 * 
 * @author Tanvi Yamarthy
 * @author Vidushi Jindal
 */
public class StopAndFrisk {

    /*
     * The ArrayList keeps track of years that are loaded from CSV data file.
     * Each SFYear corresponds to 1 year of SFRecords. 
     * Each SFRecord corresponds to one stop and frisk occurrence.
     */ 
    private ArrayList<SFYear> database; 

    /*
     * Constructor creates and initializes the @database array
     * 
     * DO NOT update nor remove this constructor
     */
    public StopAndFrisk () {
        database = new ArrayList<>();
    }

    /*
     * Getter method for the database.
     * *** DO NOT REMOVE nor update this method ****
     */
    public ArrayList<SFYear> getDatabase() {
        return database;
    }

    /**
     * This method reads the records information from an input csv file and populates 
     * the database.
     * 
     * Each stop and frisk record is a line in the input csv file.
     * 
     * 1. Open file utilizing StdIn.setFile(csvFile)
     * 2. While the input still contains lines:
     *    - Read a record line (see assignment description on how to do this)
     *    - Create an object of type SFRecord containing the record information
     *    - If the record's year has already is present in the database:
     *        - Add the SFRecord to the year's records
     *    - If the record's year is not present in the database:
     *        - Create a new SFYear 
     *        - Add the SFRecord to the new SFYear
     *        - Add the new SFYear to the database ArrayList
     * 
     * @param csvFile
     */
    public void readFile ( String csvFile ) {

        // DO NOT remove these two lines
        StdIn.setFile(csvFile); // Opens the file
        StdIn.readLine();       // Reads and discards the header line
        boolean flag = false;

        String nextLine = StdIn.readLine();

        // WRITE YOUR CODE HERE
        while(nextLine != null){

            String[] recordEntries = nextLine.split(",");
            int year = Integer.parseInt(recordEntries[0]);
            String description = recordEntries[2];
            String gender = recordEntries[52];
            String race = recordEntries[66];
            String location = recordEntries[71];
            Boolean arrested = recordEntries[13].equals("Y");
            Boolean frisked = recordEntries[16].equals("Y");
            SFRecord newSfRecord = new SFRecord(description, arrested, frisked, gender, race, location);

            for(int i = 0; i < database.size(); i++){
                if(database.get(i).getcurrentYear() == year){
                    database.get(i).addRecord(newSfRecord);
                    flag = true;
                    break;
                }
            }
            if(!flag || database.size() == 0){
                SFYear newSFYear = new SFYear(year);
                newSFYear.addRecord(newSfRecord);
                database.add(newSFYear);
            }
            nextLine = StdIn.readLine();
        }

    }

    /**
     * This method returns the stop and frisk records of a given year where 
     * the people that was stopped was of the specified race.
     * 
     * @param year we are only interested in the records of year.
     * @param race we are only interested in the records of stops of people of race. 
     * @return an ArrayList containing all stop and frisk records for people of the 
     * parameters race and year.
     */

    public ArrayList<SFRecord> populationStopped ( int year, String race ) {

        // WRITE YOUR CODE HERE
        ArrayList<SFRecord> newArr = new ArrayList<>();

        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){
                    if(database.get(i).getRecordsForYear().get(j).getRace().equals(race)){
                        newArr.add(database.get(i).getRecordsForYear().get(j));
                    }
                }
                break;
            }
        }

        return newArr;
    }

    /**
     * This method computes the percentage of records where the person was frisked and the
     * percentage of records where the person was arrested.
     * 
     * @param year we are only interested in the records of year.
     * @return the percent of the population that were frisked and the percent that
     *         were arrested.
     */
    public double[] friskedVSArrested ( int year ) {
        
        // WRITE YOUR CODE HERE
        double friskCount = 0;
        double arrestCount = 0;
        int index = 0;
        double[] output = new double[2];

        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                index = i;
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){
                    if(database.get(i).getRecordsForYear().get(j).getFrisked()){
                        friskCount += 1;
                    }
                    if(database.get(i).getRecordsForYear().get(j).getArrested()){
                        arrestCount += 1;
                    }
                }
                break;
            }
        }

        double numOfRecords = database.get(index).getRecordsForYear().size();
        output[0] = (friskCount / numOfRecords) * 100;
        output[1] = (arrestCount / numOfRecords) * 100;

        return output; // update the return value
    }

    /**
     * This method keeps track of the fraction of Black females, Black males,
     * White females and White males that were stopped for any reason.
     * Drawing out the exact table helps visualize the gender bias.
     * 
     * @param year we are only interested in the records of year.
     * @return a 2D array of percent of number of White and Black females
     *         versus the number of White and Black males.
     */
    public double[][] genderBias ( int year ) {

        // WRITE YOUR CODE HERE
        double blackPeople = 0;
        double whitePeople = 0;
        double blackMen = 0;
        double blackWomen = 0;
        double whiteMen = 0;
        double whiteWomen = 0;

        double[][] output = new double[2][3];

        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){
                    if(database.get(i).getRecordsForYear().get(j).getRace().equals("B")){
                        blackPeople += 1;
                        if(database.get(i).getRecordsForYear().get(j).getGender().equals("M")){
                            blackMen += 1;
                        }else if(database.get(i).getRecordsForYear().get(j).getGender().equals("F")){
                            blackWomen += 1;
                        }
                    }else if(database.get(i).getRecordsForYear().get(j).getRace().equals("W")){
                        whitePeople += 1;
                        if(database.get(i).getRecordsForYear().get(j).getGender().equals("M")){
                            whiteMen += 1;
                        }else if(database.get(i).getRecordsForYear().get(j).getGender().equals("F")){
                            whiteWomen += 1;
                        }
                    }
                }
                break;
            }
        }

        output[0][0] = (blackWomen / blackPeople) * 0.5 * 100;
        output[0][1] = (whiteWomen / whitePeople) * 0.5 * 100;
        output[0][2] = output[0][0] + output[0][1]; //total female

        output[1][0] = (blackMen / blackPeople) * 0.5 * 100;
        output[1][1] = (whiteMen / whitePeople) * 0.5 * 100;
        output[1][2] = output[1][0] + output[1][1]; //total male

        return output; // update the return value
    }

    /**
     * This method checks to see if there has been increase or decrease 
     * in a certain crime from year 1 to year 2.
     * 
     * Expect year1 to preceed year2 or be equal.
     * 
     * @param crimeDescription
     * @param year1 first year to compare.
     * @param year2 second year to compare.
     * @return 
     */

    public double crimeIncrease ( String crimeDescription, int year1, int year2 ) {
        
        // WRITE YOUR CODE HERE
        double year1Count = 0;
        double year2Count = 0;
        double year1Size = 0;
        double year2Size = 0;
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year1){
                year1Size = database.get(i).getRecordsForYear().size();
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){
                    if(database.get(i).getRecordsForYear().get(j).getDescription().indexOf(crimeDescription) != -1){
                        year1Count += 1;
                    }
                }
            }
            if(database.get(i).getcurrentYear() == year2){
                year2Size = database.get(i).getRecordsForYear().size();
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){
                    if(database.get(i).getRecordsForYear().get(j).getDescription().indexOf(crimeDescription) != -1){
                        year2Count += 1;
                    }
                }
            }
        }

        double year1Percentage = (year1Count / year1Size) * 100;
        double year2Percentage = (year2Count / year2Size) * 100;
        double output = Math.abs(year1Percentage - year2Percentage);
        if(year1Percentage > year2Percentage){
            output *= -1;
        }

	return output; // update the return value
    }

    /**
     * This method outputs the NYC borough where the most amount of stops 
     * occurred in a given year. This method will mainly analyze the five 
     * following boroughs in New York City: Brooklyn, Manhattan, Bronx, 
     * Queens, and Staten Island.
     * 
     * @param year we are only interested in the records of year.
     * @return the borough with the greatest number of stops
     */
    public String mostCommonBorough ( int year ) {

        // WRITE YOUR CODE HERE
        String[] locationNames = {"Brooklyn", "Manhattan", "Bronx", "Queens", "Staten Island"};
        int[] counts = new int[5];

        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++){
                    if(database.get(i).getRecordsForYear().get(j).getLocation().equalsIgnoreCase("Brooklyn")){
                        counts[0] += 1;
                    }else if(database.get(i).getRecordsForYear().get(j).getLocation().equalsIgnoreCase("Manhattan")){
                        counts[1] += 1;
                    }else if(database.get(i).getRecordsForYear().get(j).getLocation().equalsIgnoreCase("Bronx")){
                        counts[2] += 1;
                    }else if(database.get(i).getRecordsForYear().get(j).getLocation().equalsIgnoreCase("Queens")){
                        counts[3] += 1;
                    }else if(database.get(i).getRecordsForYear().get(j).getLocation().equalsIgnoreCase("Staten Island")){
                        counts[4] += 1;
                    }
                }
                break;
            }
        }

        int max = counts[0];
        int maxIndex = 0;
        for(int k = 1; k < counts.length; k++){
            if(counts[k] > max){
                max = counts[k];
                maxIndex = k;
            }
        }

        return locationNames[maxIndex]; // update the return value
    }

}
