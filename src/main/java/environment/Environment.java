package environment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Environment {
    // Opens the csv file from sub_1.csv for reading.
    BufferedReader csvReader = new BufferedReader(new FileReader("src/main/dataset/lat_long_v2.csv"));

    public Environment() throws IOException {
        // This avoid the first line with the column's labels.
        this.nextLine();
    }

    // Returns next line in the csv file.
    public String[] nextLine() throws IOException {
        String row;
        if ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            return data;
        } else {
            csvReader.close();
            return null;
        }
    }

}
