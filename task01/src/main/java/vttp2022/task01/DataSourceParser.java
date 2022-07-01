package vttp2022.task01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSourceParser {
  private Reader readerCSV;
  private BufferedReader brCSV;

  private Reader readerTemplate;
  private BufferedReader brTemplate;

  private String templateFileFormat;

  private String[] variableNames;
  private Map<String, String> recordSet = new HashMap<>();
  private final String DOUBLEUNDERSCORE = "__";
  private final String DOUBLEUNDERSCORECOMMA = "__,";
  private final String DOUBLEUNDERSCOREDOT = "__.";
  private final String DOUBLEUNDERSCOREEXCLAMATION = "__!";
  private final String DOLLARDOUBLEUNDERSCORE = "$__";

  public DataSourceParser(String csvFileInput, String templateFile) throws FileNotFoundException{
    this.readerCSV = new FileReader(csvFileInput);
    this.brCSV = new BufferedReader(this.readerCSV);

   this.templateFileFormat = templateFile;
  }

  public void run (){
    try{
      // Start reading from the first row
      String data = this.brCSV.readLine();
      variableNames = data.split(",");

      //System.out.println("DataSourceParser - run: " + variableNames);

      while (data != null){
        data = this.brCSV.readLine();
        readRecord(data);
      }

    } catch(IOException ex){
      System.out.println(ex.getMessage());
    }
  }

  private void readRecord(String data) {
    if ((null == data) || (data.trim().length() <= 0))
        return;

    String[] fieldsValues = data.split(",");
    removeQuotes(fieldsValues);

    populateRecordValues(fieldsValues);
    System.out.println("DataSourceParser - readRecord: " + recordSet);

    readWriteToTemplateFile(recordSet);
  }

  private void readWriteToTemplateFile(Map<String, String> rs){
    try{
      this.readerTemplate = new FileReader(this.templateFileFormat);
      this.brTemplate = new BufferedReader(readerTemplate);

      // System.out.println("TemplateFileFormat: " +this.templateFileFormat);
      // int indexOfExtension = this.templateFileFormat.indexOf(".txt");
      // String newFile = this.templateFileFormat.substring(0, indexOfExtension) + "_filled.txt";
      // System.out.println("newFile: " + newFile);
      // File file = new File(newFile);

      //   if (!file.exists()) {
      //    file.createNewFile();
      // }

      // FileWriter fw = new FileWriter(file);
	    // BufferedWriter bw = new BufferedWriter(fw);

      String line;
      while ((line = this.brTemplate.readLine()) != null){
        // System.out.println("writeToTemplateFile: " + line);
        String[] words = line.split(" ");
        String[] postProcessedTemplate = prefixSuffixParser(words);
        // for (String s : postProcessedTemplate){
        //   System.out.println("postProcessedTemplate: " + s);
        // }
        printPostProcessedLine(postProcessedTemplate);
      }

    }catch(IOException ex){
      System.out.println(ex.getMessage());
    }
  }

  private void printPostProcessedLine(String[] postProcessedTerms){
    String postProcessedResult = String.join(" ", postProcessedTerms);
    System.out.println(postProcessedResult);
  }

  private String[] prefixSuffixParser(String[] words){
    String variableValue = "";
    for (int i = 0; i < words.length; i++){
      String word = words[i];
      // System.out.println("Word: " + word);
      if ( (word.startsWith(DOUBLEUNDERSCORE) && word.endsWith(DOUBLEUNDERSCORE))){
        // System.out.println("prefixSuffixParser Before 1st Condition: " + word);
        word = word.replaceAll(DOUBLEUNDERSCORE, "");
        // System.out.println("prefixSuffixParser After  1st Condition: " + word);
        // Find in dictionary, search by the key
        variableValue = recordSet.get(word);
        words[i] = variableValue;
        // System.out.println("[1] prefixSuffixParser - Key: " + word + " Value: " + variableValue);
      }
      else if ((word.startsWith(DOUBLEUNDERSCORE) && word.endsWith(DOUBLEUNDERSCORECOMMA))){
        // System.out.println("prefixSuffixParser Before  2nd Condition: " + word);
        word = word.replaceAll(DOUBLEUNDERSCORECOMMA, "");
        word = word.replaceAll(DOUBLEUNDERSCORE, "");
        // System.out.println("prefixSuffixParser After  2nd Condition: " + word);
        variableValue = recordSet.get(word);
        words[i] = variableValue.concat(",");
        // System.out.println("[2] prefixSuffixParser - Key: " + word + " Value: " + variableValue);
      }
      else if ((word.startsWith(DOUBLEUNDERSCORE) && word.endsWith(DOUBLEUNDERSCOREDOT))){
        // System.out.println("prefixSuffixParser Before  2nd Condition: " + word);
        word = word.substring(2);
        word = word.replaceAll("__|__.", "");
        word = word.substring(0, word.length() - 1);
        // System.out.println("prefixSuffixParser After 1st Condition: " + word);
        variableValue = recordSet.get(word);
        words[i] = variableValue;
        // System.out.println("[2] prefixSuffixParser - Key: " + word + " Value: " + variableValue);
      }
      else if ((word.startsWith(DOLLARDOUBLEUNDERSCORE) && word.endsWith(DOUBLEUNDERSCOREEXCLAMATION))){
        // System.out.println("prefixSuffixParser Before  2nd Condition: " + word);
        word = word.substring(3);
        // System.out.println("prefixSuffixParser After 1st Condition: " + word);
        word = word.replaceAll(DOUBLEUNDERSCOREEXCLAMATION, "");
        // System.out.println("prefixSuffixParser After 2nd Condition: " + word);
    
        variableValue = recordSet.get(word);
        words[i] = variableValue;
        // System.out.println("[2] prefixSuffixParser - Key: " + word + " Value: " + variableValue);
      }
    }
    return words;
  }

  // Helper method to remove quotes, as csv values are in Strings
  private void removeQuotes(String[] fieldValues) {
     for (int i = 0; i < fieldValues.length; i++){
      fieldValues[i] = fieldValues[i].replaceAll("\"", "");
     }
  }

  private void populateRecordValues(String[] values) {
    for (int i = 0; i < variableNames.length; i++){
      recordSet.put(variableNames[i], values[i]);
    }
  }
}
