package environment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Environment {
    // Verified
    BufferedReader csvReader = new BufferedReader(new FileReader("src/main/dataset/almoco.csv"));
    // Verified
//    BufferedReader csvReader = new BufferedReader(new FileReader("src/main/dataset/palestra.csv"));
    //
//    BufferedReader csvReader = new BufferedReader(new FileReader("src/main/dataset/museu.csv"));

    public Environment() throws IOException {
        // Avoids the first line with the column's labels.
        this.step();
    }

    // Returns next line in the csv file.
    public String[] step() throws IOException {
        String row;
        if ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            return data;
        } else {
            csvReader.close();
            return null;
        }
    }

}
