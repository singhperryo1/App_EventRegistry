//event registry project 
package eventregistry;

import javafx.application.Application;
import javafx.scene.Node ; 
import javafx.scene.Scene ; 
import javafx.scene.control.CheckBox ; 
import javafx.scene.control.Button ; 
import javafx.scene.layout.BorderPane ; 
import javafx.scene.control.Label ; 
import javafx.scene.control.TextField ;
import javafx.scene.control.TextArea; 
import javafx.scene.control.Tooltip ; 
import javafx.scene.layout.GridPane; 
import javafx.scene.layout.VBox; 
import javafx.scene.layout.HBox; 
import javafx.scene.text.Font; 
import javafx.scene.text.FontWeight; 
import javafx.print.PrinterJob ;
import java.util.Date; 
import java.io.FileWriter ;
import java.io.BufferedWriter ; 
import java.text.DateFormat; 
import java.io.File ; 
import java.io.IOException ; 
import java.text.SimpleDateFormat;
import javafx.geometry.Pos; 
import javafx.geometry.Insets;
import javafx.stage.Stage;


public class EventRegistry extends Application {
    //global variables 
    private final String itemListFilename = "/Documents/textfiles/ItemList.txt"; 
    private final String Title = "Event Registry" ;    //default title if no file found 
    private Label lblTitle ;                          //title to be placed at the top of the scene 
    private TextField guest ;                        //to contain guest's name 
    private TextArea selectionList ;                 // user's selected items list on the right position of Border Pane 
    private  GridPane grid = null ;                  // to place/contain list of the items 
   
    @Override
    public void start(Stage primaryStage) {
     BorderPane stem = new BorderPane(); 
     stem.setTop(createTitle());
    stem.setBottom(createButtons());
      stem.setRight(createRightPosition());
      stem.setLeft(createLeftPosition());
      stem.setStyle("-fx-background-color: #F0E0C0");
      stem.setPadding(new Insets(5,10,0,10));    //spacing between the nodes 
      BorderPane.setMargin(selectionList,new Insets(5,10,0,5));
      
      Scene scene = new Scene(stem); 
      primaryStage.setTitle(Title);
      primaryStage.setScene(scene);
      primaryStage.setOnCloseRequest(e -> saveSelectionList());
      primaryStage.show(); 
    }
    //Error message when the program is not able to open the file (itemList.txt) 
    private static final String ERROR_MESSAGE1 =    //for the left postion of the BorderPane  
            "Unable to access the \"ItemList.txt\" file \n\n" +
            "Make sure ItemList.txt file is placed in a subfolder\n"+
            "named EventRegistry in the Documents folder \n\n" + 
            "The format of the ItemList.txt file is:\n" + 
            "Name of the Event Registry \n" + 
            "List of items, one line per item \n" + 
            "--Use a blank line to separate groups of items \n" + 
            "--Enter NEW COLUMN to start a new column \n" ;
    
  //Error message for the right position (TextArea) 
    private static final String ERROR_MESSAGE2 = 
            "Sample ItemList.txt file \n"+
            "Scroll to see full description \n" + 
            "------------------------------ \n"+ 
            "Singh's Wedding Registry Shop \n\n" + 
            "8 Plates\n" + 
            "8 Knives\n\n" + 
            "1 Spatula\n" + 
            "2 Serving Spoons\n" + 
            "1 Potato Peeler\n\n" + 
            "NEW COLUMN \n" + 
            "1 Dish Wash \n" + 
            "1 Box of Soap Bars \n" + 
            "1 Shampoo \n\n" + 
            "6 Light Bulbs \n" + 
            "2 Wash Cloths \n" ; 
         
    private HBox createTitle()  //HBox containing the title as Label  
    {
       HBox hbox = new HBox(); 
       hbox.setAlignment(Pos.CENTER);
       Font fontP = Font.font("Bradley Hand ITC",FontWeight.BOLD,36); 
       lblTitle = new Label(Title); 
         lblTitle.setFont(fontP);
        hbox.getChildren().add(lblTitle); 
        return hbox ; 
    }

    //method creating TextArea in right position of BorderPane  
    private TextArea createRightPosition () 
    {
     selectionList = new TextArea ();          //reference created at the top 
     Font  myFont = Font.font("Courier New",FontWeight.BOLD,14); 
     selectionList.setFont(myFont);
     selectionList.setPrefWidth(300.0);
     selectionList.setWrapText(true);
     return selectionList ; 
    }
    
    //method creating VBox in the left postion of BorderPane  
    private VBox createLeftPosition ()
    {
        VBox vbox = new VBox(); 
        // a string that contains fully qualified filename 
        // /User/username/Documents/GIftRegistry/GiftItems.txt
        // where: username is the ID of the person who logged in to the system
        
        String homePath = System.getenv("HOMEPATH");  //system environment varialbe 
        
        if (homePath==null)                             //may be it is Mac or Linux system 
            homePath = System.getenv("HOME"); 
        String filename = homePath + itemListFilename ;  // address stored into variable filename 
 
        //reading the list from the disk file 
        
        ReadTextFile selectionOptions = new ReadTextFile(filename); 
        //display error messages if unable to read the file 
        if (selectionOptions.isFileError())
        {
            Label errorMessage = new Label(ERROR_MESSAGE1); 
        vbox.getChildren().add(errorMessage); 
        selectionList.setText(ERROR_MESSAGE2);    //error message in the TextArea in right position of BorderPane 
        return vbox ;                        //makes createLeftPostion() method to end, but th program does not terminate
    }                                       //end of if statement
    
     //else getting item list from the file and placing in the array 
     String [] itemList = selectionOptions.getFileContent(); 
     
     int lineCount = selectionOptions.getLineCount(); 
     int line = 0 ;       //line of the String array 
     int column = 0 ;     //X position in grid  
     int row = 0 ;       //Y postion in grid 
     
     //first thing in .txt file  should be the name  
     do {
         lblTitle.setText(itemList[line]);       //setting the title of the application from the file 
     } while (itemList[line++].trim().equals("")); 
     
     //skip empty line until the first option in the list is reached 
     while (itemList[line++].trim().equals(""));  //skipping blank lines using empty while loop statements 
 
        //TextFiled for guest's name 
        Label lblGuestName = new Label ("Guest Name"); 
        guest = new TextField();                  //reference created at the top 
        Label lblBlankLine = new Label("") ;      // leave a blank space between the TextField and List of options avail 
        
        //now, adding the items to the BorderPane left position using a grid 
        grid = new GridPane();                   //referecne at the top 
        grid.setStyle("-fx-background-color: #C0E0FF");
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));         //spacing around the grid 

        while (line<lineCount)
        {
            if(itemList[line].trim().toUpperCase().equals("NEW COLUMN"))
            {
                row = 0 ; 
            column++ ; 
        }
            else if (itemList[line].trim().equals(""))   //if a blank line appears
            {
                grid.add(new Label(""), column, row++);
            }
            else 
            {
                CheckBox chkOption = new CheckBox(itemList[line]); 
                grid.add(chkOption,column,row++);     //anything other than NEW COLUMN and blank line
            }
            line ++ ; 
            
        }        // end of while loop 
     vbox.getChildren().addAll(lblGuestName,guest,lblBlankLine,grid); 
        return vbox ; 
    }
    private HBox createButtons ()        //HBox containing Buttons 
    {
        HBox hbox = new HBox(); 
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(20.0);          //spacing between the buttons 
        hbox.setPrefHeight(50);         // spacing around the top and bottom 
       
        // creating the buttons now
        Button btnSubmit = new Button("Submit"); 
        btnSubmit.setPrefSize(110,20);
        btnSubmit.setOnAction(e -> submitToSelectionList());
        Tooltip tt = new Tooltip(); 
        tt.setText("Click to Enter a Guest Name");
        btnSubmit.setTooltip(tt);
        
        //Button to print the TextArea node 
        Button printTextButton = new Button("Print List"); 
        printTextButton.setPrefSize(110,20); 
        printTextButton.setOnAction( e -> printSelections());
        Tooltip printTip = new Tooltip (); 
        printTip.setText("Click to Print a List");
        printTextButton.setTooltip(printTip);
        
        //Button to save/clear 
        Button btnSC = new Button("Save/Clear"); 
        btnSC.setPrefSize(110,20);
        btnSC.setOnAction(e -> saveAndClear());
        Tooltip ScTip = new Tooltip(); 
        ScTip.setText("Click to Save a List and clear the CheckBoxes");
        btnSC.setTooltip(ScTip);
        
        //Button to exit/end the program 
        Button btnExit = new Button("Exit"); 
        btnExit.setPrefSize(110, 20);
        btnExit.setOnAction(e-> System.exit(0));
        
        hbox.getChildren().addAll(btnSubmit, printTextButton, btnSC,btnExit); 
        return hbox ; 
    }
    
    //action handler method for submit button 
   private void submitToSelectionList()
   {
      String currentSelections ;                    //receipt for a single item, append to full receipt
      currentSelections = guest.getText() + "\n" ;  //adding guest's name at the top 
      
      //adding checked item to the currentSelections 
      
      if (grid!=null)                               //to make sure the grid is not empty 
          for (Node node : grid.getChildren())
          {
              if (node instanceof CheckBox)
              {
                  if (((CheckBox)node).isSelected()          //if the item is selected 
                          && !((CheckBox)node).isDisable())  //not disabled
                  {
                      //append the tiems to currentSelections string 
                      currentSelections += ((CheckBox)node).getText() + "\n"; 
                  
                 
              }  // end of if(isSelected()) test 
                  
          } //end of instanceof ChechBox
   } //end of for loop
    
      //adding the currentSelection to the top of the selectionList (TextArea) in BorderPane right position 
      selectionList.setText(currentSelections + "\n" + selectionList.getText());
      
      //for next guest, disabling checkboxes (current selections) 
      
      if (grid!=null) 
           for (Node node: grid.getChildren())
           {
               if (node instanceof CheckBox )             //if it is an checkbox
               {
                   if(((CheckBox) node).isSelected())     //if checkbox is selected
                   {
                       ((CheckBox)node).setDisable(true);  //disabling checkbox
                   }
                   }  //end of if instanceof 
               }    //end of for loop
               
           }   //end of the submitToSelectionList() method 
   //action handler method for print button 
   private void printSelections () 
   {
       final int LINES_ON_PAGE = 40 ; 
       int page = 1 ; 
       
       //a single lable to collect all the lines for a page to be printed 
       Label linesToPrint = new Label(); 
       Font fontCourierNew = Font.font("Courier New",FontWeight.NORMAL,11); 
       linesToPrint.setFont(fontCourierNew);
       
       //separating the TextArea selectionList into array of Strings 
       String[] listOfItems = selectionList.getText().split("\n") ; 
       
       int totalLines = listOfItems.length ; 
       
       //addinf titile and date at the top of the page to be printed 
       DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
       Date date = new Date(); 
       
       int line = 0 ;              //index of listOfItems[]
       
       do
       {
           // creating the header at top of each page 
           linesToPrint.setText(                       //header at top of page
                   lblTitle.getText() + "   " +        //event title 
                           dateFormat.format(date) +   //date  
           "    Page " +                               //page 
           Integer.toString(page++) + "\n\n") ;        // page #
           
           //creating the printable list of items 
           do 
           {
               linesToPrint.setText(
                       linesToPrint.getText() +         //build the print string 
                       listOfItems [line] + "\n\n");    //add an item to the String 
               line ++ ; 
           } while (line%LINES_ON_PAGE!=0 &&line<totalLines) ; 
           print(linesToPrint);                         //sending page to printer    
           } while (line<totalLines);                  //keep going until the entire TextArray is printed 
   }   //end of the method
           
   // creating method for print()
   private void print(Label text)
   {
       PrinterJob job = PrinterJob.createPrinterJob(); 
       
       if(job!=null) {            // then the PrinterJob was created successfully 
           boolean printed = job.printPage(text);
           if (printed) 
           {
               job.endJob() ;    //End the printer Job 
           } 
       else 
       {
           System.out.println("Printing Failed!");
       }
       }
       else 
       {
         System.out.println("Could not create a printer job.");
       }
   }
     //creating method to define saveAndClear() method for action handler of Save/Clear
   private void saveAndClear() 
   {
       //calling routine for saving the selectionList to disk 
       saveSelectionList() ; 
       
       //clearing check marks on the CheckBoxes  
       if (grid!=null)                           //making sure the grid has been build 
       {
           for (Node node : grid.getChildren())  //get an entry in the grid 
           {
               if (node instanceof CheckBox)      //to check if it is checkbox or not 
               {
                  ((CheckBox) node).setSelected(false) ; 
                     ((CheckBox) node).setDisable(false); 
               }
           }
       }
           guest.setText("");         //clear the guest's name 
           selectionList.clear();     // clear the TextArea on the right side         
       }   //end of the method 
  
       //creating method to define  saveSelectionList() 
   private void saveSelectionList () 
   {
  final  String logFileName = "/Documents/textfiles/EventRegistry.log";  //for the file to  save the items 
     
       try 
   {
       boolean toWrite = false ; 
       if (!guest.getText().equals(""))     //if the guest name has been entered 
       {
           toWrite = true ; 
       }
       if (grid !=null)                                   //make sure grid has been build 
           for (Node node : grid.getChildren()) 
           {
               if (node instanceof CheckBox)                   //if it is check box 
                   if(((CheckBox) node).isSelected())           // if the checkbox is marked
                       toWrite = true ; 
           } // end of for loop 
       if (!toWrite)
           return ;                                              //if nothing to write, skip the rest of the method 
                String homePath = System.getenv("HOMEPATH");    //system environment varialbe 
        
        if (homePath==null)                                     //may be it is Mac or Linux system 
            homePath = System.getenv("HOME"); 
        String Fn = homePath + logFileName ;  
                   File Fname = new File(Fn);
                   if (!Fname.exists())
                       Fname.createNewFile() ; 
                       FileWriter fileWriter = new FileWriter(Fname,true);   // set to true to open the file in append mode 
                       //BufferedWriter gives better performance 
                       BufferedWriter writer = new BufferedWriter(fileWriter) ; 
                       //seperator for each time the file is updated 
                       writer.write("=================================================================================");
                       writer.newLine();
                       writer.newLine();
                       //writing the  title and date  at the top
                          DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
       Date date = new Date(); 
                      writer.write(lblTitle.getText() + "           "+ dateFormat.format(date));
                      writer.newLine();
                      //array of String to collect info from selectionList (TextArea) right positon
                      
                     String [] listOfItems = selectionList.getText().split("\n") ;
                     int TotalLines = listOfItems.length ; 
                     //adding the list of items from the left positon to disk file 
                     for (int line= 0; line<TotalLines ; line ++ ) 
                     {
                         writer.write(listOfItems[line]);        //adding an item to the file 
                         writer.newLine();                       // moving to new line 
                     }
                     //closing the BufferedWriter 
                     writer.close(); 
                     
           }
       catch (IOException ioe )
       {
      System.out.println("Exception Occured") ; 
      ioe.printStackTrace();
       } //end of try/ctach block 
   }    //end of the method 
   
    public static void main(String[] args) {
        launch(args);
    }
    
}







