package operations;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
/*=============================================================================
| Assignment: Program #1: Incomplete VIC (Encryption)
| Author: Michael Knatz michaelcknatz@email.arizona.edu
| Grader: [Your TA or Section Leader's name]
|
| Course: CSC345
| Instructor: L. McCann
| Due Date: Tuesday, September 17 @ 2:00pm
|
| Description: [Describe the program's goal, IN DETAIL.]
|
| Deficiencies: I know of no logical errors or unsatisfied requirements
*===========================================================================*/

class VICData {
    public String agentID;
    public String date;
    public String phrase;
    public String phraseOriginal;
    public String anagram;
    public String message;
    public String messageOriginal;
} // class VICData

public class EncryptVIC {

	//The global variables are just for highlighting purposes
	//to make the variables easier to find when reading
	private static long noCarry = 0;
	private static long chainAdd = 0;
	private static String digiPerm = "";
	private static ArrayList<String> stradCheck = null;
	private static String encoded;
	
	public static void main(String [] args){
		if(args.length == 0){
			System.out.println("Usage: ");
			return;
		}
		VICData information = readVICData(args[0]);
		//Steps for encryption
		encoded = information.message;
		noCarry = VICOperations.noCarryAddition(Long.parseLong(information.date.substring(0, 5)),
				Long.parseLong(information.agentID));
		chainAdd = VICOperations.chainAddition(noCarry, 10);
		digiPerm = VICOperations.digitPermutation(information.phrase);
		noCarry = VICOperations.noCarryAddition(chainAdd, Long.parseLong(digiPerm));
		digiPerm = VICOperations.digitPermutation(noCarry+"");
		stradCheck = VICOperations.straddlingCheckerboard(digiPerm, information.anagram);
		//using codeHelp, all the chars of a type can be replaces with the correct number
		//from straddlingCheckerboard
		char codeHelp = 'A';
		for(int n = 0; n < 26; n++){
				encoded = encoded.replaceAll(codeHelp + "", stradCheck.get(n));
				codeHelp++;
		}
		//now just insertID
		int insertWhere = Integer.parseInt(information.date.substring(5, 6));
		if(encoded.length() <= insertWhere){
			encoded = encoded+information.agentID;
		}
		else{
			encoded = encoded.substring(0, insertWhere) + information.agentID + encoded.substring(insertWhere);
		}
		
		System.out.println(encoded);
	}
	
	public static int     ID_LEN          = 5;   // # of chars in agent ID
    public static int     DATE_LEN        = 6;   // # of chars in date
    public static int     PHRASE_LEN      = 10;  // # letters of phrase to use
    public static int     ANAGRAM_LEN     = 10;  // # of chars in anagram
    public static int     ANAGRAM_LETTERS = 8;   // # of letters in anagram

    public static char    SPACE           = (char)32; // space is ASCII 32
    public static boolean DEBUG           = false;    // toggle debug prints
	


	       /*---------------------------------------------------------------------
	        |  Method readVICData (pathName)
	        |
	        |  Purpose:  VIC requires five pieces of information for an
	        |            encoding to be performed:  (1) agent ID (form: #####),
	        |            (2) date (form:YYMMDD), (3) a phrase containing at least
	        |            10 letters, (4) an anagram of 8 commonly-used letters
	        |            and 2 spaces, and (5) a message of at least one letter
	        |            to be encoded.  This method reads these pieces from the
	        |            given filename (or path + filename), one per line, and
	        |            all pieces are sanity-checked.  When reasonable,
	        |            illegal characters are dropped.  The program is halted
	        |            if an unrecoverable problem with the data is discovered.
	        |
	        |  Pre-condition:  None.  (We even check to see if the file exists.)
	        |
	        |  Post-condition:  The returned VICData object's fields are populated
	        |            with legal data.
	        |
	        |  Parameters:  pathName -- The filename or path/filename of the text
	        |            file containing the file pieces of data.  If only a
	        |            file name is provided, it is assumed that the file
	        |            is located in the same directory as the executable.
	        |
	        |  Returns:  A reference to an object of class VICData that contains
	        |            the five pieces of data.
	        *-------------------------------------------------------------------*/

	    public static VICData readVICData (String pathName)
	    {
	        VICData vic = new VICData(); // Object to hold the VIC input data
	        Scanner inFile = null;       // Scanner file object reference

	        try {
	              inFile = new Scanner(new File(pathName));
	        } catch (Exception e) {
	              System.out.println("File does not exist: " + pathName + "!\n");
	              System.exit(1);
	        }

	                // Read and sanity-check agent ID.  Needs to be ID_LEN long
	                // and numeric.

	        if (inFile.hasNext()) {
	              vic.agentID = inFile.nextLine();
	        } else {
	              System.out.println("ERROR:  Agent ID not found!\n");
	              System.exit(1);
	        }

	        if (vic.agentID.length() != ID_LEN) {
	              System.out.printf("ERROR:  Agent ID length is %d, must be %d!\n",
	                                vic.agentID.length(),ID_LEN);
	              System.exit(1);
	        }

	        try {
	            long idValue = Long.decode(vic.agentID);
	        } catch (NumberFormatException e) {
	              System.out.println("Agent ID `" + vic.agentID 
	                               + "contains non-numeric characters!\n");
	              System.exit(1);
	        }

	                // Read and sanity-check date.  Needs to be DATE_LEN long
	                // and numeric.

	        if (inFile.hasNext()) {
	              vic.date = inFile.nextLine();
	        } else {
	              System.out.println("ERROR:  Date not found!\n");
	              System.exit(1);
	        }

	        if (vic.date.length() != DATE_LEN) {
	              System.out.printf("ERROR:  Date length is %d, must be %d!\n",
	                                vic.date.length(),DATE_LEN);
	              System.exit(1);
	        }

	        try {
	            long dateValue = Long.decode(vic.date);
	        } catch (NumberFormatException e) {
	              System.out.println("Date `" + vic.date 
	                               + "contains non-numeric characters!\n");
	              System.exit(1);
	        }

	                // Read and sanity-check phrase.  After removing non-letters,
	                // at least PHRASE_LEN letters must remain.

	        if (inFile.hasNext()) {
	              vic.phraseOriginal = inFile.nextLine();
	              StringBuffer sb = new StringBuffer(vic.phraseOriginal);
	              for (int i = 0; i < sb.length(); i++) {
	                  if (!Character.isLetter(sb.charAt(i))) {
	                      sb.deleteCharAt(i);
	                      i--;  // Don't advance to next index o.w. we miss a char
	                  }
	              }
	              vic.phrase = sb.toString().toUpperCase();
	              if (vic.phrase.length() >= PHRASE_LEN) {
	                  vic.phrase = vic.phrase.substring(0,PHRASE_LEN);
	              }
	        } else {
	              System.out.println("ERROR:  Phrase not found!\n");
	              System.exit(1);
	        }

	        if (vic.phrase.length() != PHRASE_LEN) {
	              System.out.printf("ERROR:  Phrase contains %d letter(s), "
	                              + "must have at least %d!\n",
	                                vic.phrase.length(),PHRASE_LEN);
	              System.exit(1);
	        }

	                // Read and sanity-check anagram.  Must be ANAGRAM_LEN long,
	                // and contain ANAGRAM_LETTERS letters and the rest spaces.

	        if (inFile.hasNext()) {
	              vic.anagram = inFile.nextLine().toUpperCase();
	        } else {
	              System.out.println("ERROR:  Anagram not found!\n");
	              System.exit(1);
	        }

	        if (vic.anagram.length() != ANAGRAM_LEN) {
	            System.out.printf("ERROR:  Anagram length is %d, must be %d!\n",
	                              vic.anagram.length(),ANAGRAM_LEN);
	            System.exit(1);
	        }

	        for (int i = 0; i < vic.anagram.length(); i++) {
	            if (    !Character.isLetter(vic.anagram.charAt(i))
	                 && vic.anagram.charAt(i) != SPACE             ) {
	                System.out.printf("ERROR:  Anagram contains character `%c'.\n",
	                                  vic.anagram.charAt(i) );
	                System.exit(1);
	            }
	        }

	        int numLetters = 0;
	        for (int i = 0; i < vic.anagram.length(); i++) {
	            if (Character.isLetter(vic.anagram.charAt(i))) {
	                numLetters++;
	            }
	        }
	        if (numLetters != ANAGRAM_LETTERS) {
	            System.out.printf("ERROR:  Anagram contains %d letters, "
	                            + "should have %d plus %d spaces.\n",
	                              numLetters, ANAGRAM_LETTERS,
	                              ANAGRAM_LEN - ANAGRAM_LETTERS);
	            System.exit(1);
	        }

	                // Read and sanity-check message.  After removing non-letters
	                // and capitalizing, at least one letter must remain.

	        if (inFile.hasNext()) {
	              vic.messageOriginal = inFile.nextLine();
	              StringBuffer sb = new StringBuffer(vic.messageOriginal);
	              for (int i = 0; i < sb.length(); i++) {
	                  if (!Character.isLetter(sb.charAt(i))) {
	                      sb.deleteCharAt(i);
	                      i--;  // Don't advance to next index o.w. we miss a char
	                  }
	              }
	              vic.message = sb.toString().toUpperCase();
	        } else {
	              System.out.println("ERROR:  Message not found!\n");
	              System.exit(1);
	        }

	        if (vic.message.length() == 0) {
	              System.out.printf("ERROR:  Message contains no letters!\n");
	              System.exit(1);
	        }


	        if (DEBUG) {
	            System.out.printf("vic.agentID = %s\n",vic.agentID);
	            System.out.printf("vic.date = %s\n",vic.date);
	            System.out.printf("vic.phrase = %s\n",vic.phrase);
	            System.out.printf("vic.anagram = %s\n",vic.anagram);
	            System.out.printf("vic.message = %s\n",vic.message);
	        }

	        return vic;
	    }
	   
}
