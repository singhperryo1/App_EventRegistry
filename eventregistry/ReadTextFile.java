//to read textfile
package eventregistry;

import java.io.* ; 
import java.util.Scanner ; 

public class ReadTextFile {
    private final int MAX_LINES = 200 ; 
    private String[] fileContents = new String[MAX_LINES] ;  //to store file contents 
    private int count ; //to keep count of lines 
    boolean fileError = false ; 
    Scanner infile ; //reference for the Scanner object 
    
    //non-default constructor 
    ReadTextFile (String filename)
    {
        try 
        {
            infile = new Scanner (new File(filename)) ; //creating the stream to get text from file 
            //reading the entire file into the fileContents array 
            for (count = 0; infile.hasNextLine() && count < MAX_LINES; count ++)
            {
                fileContents[count] = infile.nextLine() ; 
            }
            infile.close(); 
        } //end of the block 
        catch (FileNotFoundException e)
        {
           fileError = true ; 
        } //end of the catch block 
    } //end  of the constructor
    
    //getters 
    
    public int getLineCount () { return count ; } 
    public String[] getFileContent () { return fileContents ; } 
    public boolean isFileError () { return fileError ; } 
    
}  // end  of the class
