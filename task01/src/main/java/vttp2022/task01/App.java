package vttp2022.task01;

import java.io.FileNotFoundException;

/**
 * Task 01
 *
 */
public class App
{
    public static void main( String[] args ) throws FileNotFoundException
    {
        if (args.length > 1){
            String csvDataFile = args[0];
            String templateFile = args[1];

            DataSourceParser ds = new DataSourceParser(csvDataFile, templateFile);
            ds.run();
        } else{
            System.err.println("Error: Please input CSV File path and Template File path!");
        }
    }
}
