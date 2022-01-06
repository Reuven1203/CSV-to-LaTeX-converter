// -----------------------------------------------------
// Assignment 3
// Written by: Reuven Ostrofsky - 40188881
// -----------------------------------------------------
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

import Exceptions.CSVDataMissing;
import Exceptions.CSVFileInvalidException;
/**
 * 
 * @author Reuven Ostrofsky
 * Driver class reads given csv files and converts them into existing/created latex files
 * User can choose for program to read created latex files and output its content in the console
 */
public class Driver {
	/**
	 * Latex attribute
	 */
    final static String beginTable = "\\begin{table}[]";
    /**
     * Latex attribute
     */
    final static String beginTabular = "\\begin{tabular}{";
    /**
     * Latex attribute
     */
    final static String endTabular = "\\end{tabular}";
    /**
     * Latex attribute
     */
    final static String endTable = "\\end{table}";
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        File[] files = {new File("src/Files/EmployerInterview-Missing Data-CSV.csv"),new File("src/Files/EmployerInterviewCVS.csv"),new File("src/Files/salaryStudy-MissingAttr-CSV.csv"),new File("src/Files/salaryStudy.csv")};
        Scanner[] scanners = new Scanner[4];
        String userFile;
        Scanner input = new Scanner(System.in);
        for(int i = 0 ; i < files.length;i++) {
            try{
                scanners[i] = new Scanner(new FileInputStream(files[i]));
            }catch(FileNotFoundException f) {
                System.out.println("Could not open input file " + files[i].getName() + " for reading");
                System.out.println("Please check if file exists! Program will terminate after closing all files.");
                for (int j=0; j<scanners.length;j++ ) {
                    scanners[j].close();
                    
                }
                System.exit(0);
            }
            
        }
        File[] texFiles = new File[4];
        String fileName;
        String Directory = "src/Files/";
        int errorCount = 0;
        PrintWriter p[] = new PrintWriter[4];
        for(int i = 0 ; i < files.length;i++) {
            try{
                fileName = files[i].getName();
                texFiles[i] = new File(Directory + fileName.substring(0,fileName.indexOf(".")) + ".tex");
                p[i] = new PrintWriter(new FileOutputStream(texFiles[i]));
               
            }catch(FileNotFoundException f){
              System.out.println("File " + texFiles[i].getName() + " cannot be opened/created...Deleting existing file and closing opened files now.");
              if(texFiles[i].delete()) {
                  System.out.println("File" + texFiles[i].getName() + " was succesfully deleted.");
              }else{
                  System.out.println("File" + texFiles[i].getName() + " was not able to be deleted.\nTerminating program IMMEDIATELY");
                  System.exit(0);
              }
              
              errorCount++;
              if(errorCount == texFiles.length) {
                System.out.println("No files were able to be created/opened/nClosing all streams and terminating program...");
               for(int j = 0; j < scanners.length; j++) {
                scanners[j].close();
               }
               System.exit(0);
            }

            }
        }
        int count = 0;
        processFilesForValidation(files,texFiles, scanners, p);
        BufferedReader b = null;
        while(count < 2) {
        System.out.println("\n\nWhich file would you like to read? ");
        userFile = input.nextLine() + ".tex";
        
        
        try {
            b = new BufferedReader(new FileReader(Directory+userFile));
            String line;
             while((line = b.readLine()) != null){
                System.out.println(line);
             }
             count = 2;

    
        }catch(FileNotFoundException f) {
            System.out.println("File has not been found");
            count++;
            if (count >=2) {
                System.out.println(":Program is terminating now");
                System.exit(0);
            }
        }catch(IOException i) {
            System.out.println("Something went wrong...terminating program");
            System.exit(0);
        }

       
    }
       input.close();

    }
    /**
     * This method processes csv files for validation to then convert them to latex
     * @param f Array of File type containing all csv files
     * @param texFiles latexFiles array created in driver
     * @param s Scanner input stream array created in driver
     * @param p PrintWriter output stream array created in driver
     */
    public static void processFilesForValidation(File[] f,File[] texFiles,Scanner[] s,PrintWriter[] p) {
        String title;
        String[] attributes;
        StringTokenizer att;
        String attributeLine = "";
        StringTokenizer data;
        String[] indData;
        String dataLine = "";
        PrintWriter log = null;
        try {
            log = new PrintWriter(new FileOutputStream("src/Files/logInfo.log"));
            
        }catch(FileNotFoundException fnf){
            System.out.println(fnf.getMessage());
        }
        int lineCount = 1;
        for(int i = 0; i < f.length; i++) {
           try{
               title = s[i].nextLine();
               attributeLine = s[i].nextLine();
               attributeLine = attributeLine.replaceAll(",,", ",***,");
               att = new StringTokenizer(attributeLine,",");
               lineCount++;
               attributes = new String[att.countTokens()];
               int columnNum = 0;
               for(int j = 0;j<attributes.length;j++){
                   attributes[j] = att.nextToken();
               }
               for(int j = 0;j<attributes.length;j++){
                if (attributes[j].equals("***")) {
                    throw new CSVFileInvalidException(f[i]);
                }

               }


               //Create latex 
               p[i].println(beginTable);
                p[i].print(beginTabular);
                for(int j  = 0; j<attributes.length;j++){
                    p[i].print("|1");
                }
                p[i].println("|}\n\\toprule");
                p[i].println(attributeLine.replaceAll(",", " & ") + " \\\\ \\midrule");
               while(s[i].hasNextLine()){
                   try {
                       dataLine = s[i].nextLine();
                       lineCount++;
                       dataLine = dataLine.replaceAll(",,", ",***,");
                       data = new StringTokenizer(dataLine,",");
                       
                    
                    indData = new String[data.countTokens()];
                for (int j = 0;j < indData.length;j++){
                    indData[j] = data.nextToken();
                }
                
                for (int j = 0;j < indData.length;j++){
                    if(indData[j].equals("***")){
                        columnNum = j;
                        throw new CSVDataMissing();
                    }
                }
                
                //Create row
                if (s[i].hasNextLine()) {
                    p[i].println(dataLine.replaceAll(","," & ") + " \\\\ \\midrule");
                }else {
                    p[i].println(dataLine.replaceAll(","," & ") + " \\\\ \\bottomrule");
                }




                
                
                

                   }catch(CSVDataMissing c){
                    System.out.println("In file " + f[i].getName() + " line " + lineCount + " not converted to latex");
                    log.println("File " + f[i].getName() + " line " + lineCount + ".");
                    log.println(dataLine);
                    log.println("Missing:" + attributes[columnNum] + "\n");
                   }
               
               }
               //End latex
               p[i].println(endTabular);
               p[i].println("\\caption{" + title.substring(0,title.indexOf(","))+"}");
               p[i].println("\\label{"+f[i].getName().substring(0,f[i].getName().indexOf(".")) + "}");
               p[i].print(endTable);
               

               
               
             

      }catch(CSVFileInvalidException e){
        System.out.println(attributeLine + "\nFile is not converted to LATEX");

           if(texFiles[i].delete())
           System.out.println("File " + texFiles[i].getName() + " has succesfully been deleted");
           log.println(e.getMessage(f[i]));
           log.println(attributeLine);
           log.println("File is not converted to LATEX");
          }finally{
              p[i].close();
              s[i].close();
              attributeLine = "";
          }
    }  
        log.close();
      
        }

       
       
}
