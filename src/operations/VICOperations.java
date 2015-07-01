package operations;

import java.util.ArrayList;

/*+----------------------------------------------------------------------
||
|| Class VICOperations
||
|| Author: Michael Knatz
||
|| Purpose: Incomplete VIC requires several operations for both encryption
||			and decryption. Since both of them require the same operations,
||			this allows access to the necessary operations that are needed
||			without having to create them one in each program.  Also by
||			having them in a separate class, it becomes easier to test the
||			methods.
||
|| Inherits From: None
||
|| Interfaces: None
||
|+-----------------------------------------------------------------------
||
|| Constants: None
||
|+-----------------------------------------------------------------------
||
|| Constructors: None
||
|| Class Methods: None
||
|| Inst. Methods: 
||	noCarryAddition, (long first, long second), long
||	chainAddition, (long chain, int length), long
||  digitPermutation, String perm, String
||	straddlingCheckerboard, (String digiPerm, String anagram), ArrayList<String>
||
++-----------------------------------------------------------------------*/

public class VICOperations {
	/*---------------------------------------------------------------------
	| Method noCarryAddition (first, second)
	|
	| Purpose: Incomplete VIC encryption and decryption requires the ability
	| 		   to have large numbers added together without any carry factors
	|		   in order to maintain a specific number of numbers within the
	|		   long
	|
	| Pre-condition: None
	|
	| Post-condition: The returned long that is equal to an addition
	|			operation without carrying. i.e. 17+23 = 30 instead of 40
	|
	| Parameters:
	| 		first -- the first long or int to be added
	|		second -- the second long or int to be added
	|
	| Returns: A long that is first and second added together without
	|		   any carrys during the addition.
	*-------------------------------------------------------------------*/
	
	public static long noCarryAddition(long first, long second){
		long add1 = 0;
		long add2 = 0;
		long sum = 0;
		long recur1 = first;
		long recur2 = second;
		String numberChain = "";
		while(recur1 + recur2 != 0){
			add1 = recur1 % 10;
			add2 = recur2 % 10;
			if(add1 + add2 > 9){
				sum = (add1 + add2) % 10;
				numberChain = sum + numberChain;
			}
			else{
				sum = add1 + add2;
				numberChain = sum + numberChain;
			}
			recur1 = recur1 / 10;
			recur2 = recur2 / 10;
		}
		if(numberChain.equals("")){
			numberChain = "0";
		}
		return Long.parseLong(numberChain);
	}
	
	/*---------------------------------------------------------------------
	| Method: chainAddition (chain, length)
	|
	| Purpose: In order to make sure that a digit permutation required
	|		   for VIC has the correct number of of digits later (including
	|		   a no carry addition) this is required.
	|
	| Pre-condition: chain != 0
	|
	| Post-condition: A chain of "length" that is equivalent to the
	|				  addition (without carrying) of n + n+1 indices
	|				  of the first x numbers i.e. (2463, 6) would 
	|				  return 246360, or if "chain" is bigger than "length"
	|				  a chain that is shrunk to length is returned
	|
	| Parameters:
	| chain -- A non-negative non-zero long that is used to create a larger
	|          or smaller chain depending on the length the user wants.
	| length -- This is how many digits of a chain will be returned
	|
	| Returns: a long with "length" number of digits that is the result of
	|		   either shrinking the initial chain or no carry addition lengthening
	| 		   of the chain.
	*-------------------------------------------------------------------*/
	
	public static long chainAddition (long chain, int length){
		String chained = chain + "";
		if(chained.length() == 1){
			chained += chained;
		}
		int start = 0;
		long sum = 0;
		if(length < chained.length()){
			chained = chained.substring(0, length);
		}
		while(chained.length() < length){
			sum = noCarryAddition(Integer.parseInt(chained.substring(start, start+1)),
					Integer.parseInt(chained.substring(start+1, start+2)));
			chained = chained + sum;
			start += 1;
		}
		return Long.parseLong(chained);
	}
	
	/*---------------------------------------------------------------------
	| Method digitPermutation (perm)
	|
	| Purpose: In order to make a VIC encryption key, a 10 digit string of
	|		   unique numbers 0-9 is needed. This method creates the 10 digit
	|		   String needed.
	|
	| Pre-condition: None.
	|
	| Post-condition: The returned String is 0-9 and will allow for various
	|				  other operations to get a key for encrpytion/decryption
	|
	| Parameters:
	| perm -- A string of 10+ characters without spaces.
	|
	| Returns: A 10 length string of unique numbers 0-9 or if invalid
	|          data is passed, it returns null 
	*-------------------------------------------------------------------*/
	
	public static String digitPermutation (String perm){
		String tempPerm = perm.replaceAll(" ", "");
		if(tempPerm.length() < 10){
			return null;
		}
		StringBuilder temp = new StringBuilder("0000000000");
		int [] checker = new int [10];
		for(int n=0; n < checker.length; n++){
			checker[n]= tempPerm.toUpperCase().charAt(n);
		}
		int placeHolder = 0;
		int highest;
		for(int n = 0; n < 10; n++){
			highest = -1;
			for(int i = 0; i < 10; i++){
				if(checker[i] >= highest){
					placeHolder = i;
					highest = checker[i];
				}
			}
			checker[placeHolder] = -1;//because no ascii are negative
			temp.replace(placeHolder, placeHolder+1, 9-n + "");
		}
		return temp.toString();
	}
	
	/*---------------------------------------------------------------------
	| Method straddlingCheckerboard (digiPerm, anagram)
	|
	| Purpose: VIC requires a key for encryption and decryption which is created
	|		   by running this method.
	|
	| Pre-condition: the string representing 10 digit permutation be
	|				 the numbers 0-9 only and unique and that the
	|				 anagram be 8 unique letters with two spaces.	
	|
	| Post-condition: the returned ArrayList<String> has numbers representing the
	|				  26 letters of the alphabet.
	|
	| Parameters:
	| digiPerm -- A string consisting of 0-9 that is used to
	|			  create the number strings that will represent
	|			  the letters of the alphabet.
	| anagram -- A string of 8 unique letters and 2 black spaces
	|			 where the blank spaces will correspond to the first
	|			 numbers of the 2 number strings 
	|
	| Returns: An ArrayList<String> that represents the letters of the
	|		   Alphatbet (same A-Z order) as numbers in string format
	*-------------------------------------------------------------------*/
	
	public static ArrayList<String> straddlingCheckerboard (String digiPerm, String anagram){
		ArrayList<String> returns = new ArrayList<String>();
		String upper = anagram.toUpperCase();
		int firstSpace = upper.indexOf(" ");
		int secondSpace = upper.lastIndexOf(" ");
		if(firstSpace == secondSpace || anagram.length() < 10){
			return null;
		}
		char firstRow = digiPerm.charAt(firstSpace);
		char secondRow = digiPerm.charAt(secondSpace);
		int nonAnagramLetters = 0; //Keeps track of charcter combinations in the arraylist 
		char runThrough = 'A'; //helps check the anagram for which letters have single number assignments vs double numbers
		//We know there are only so many letters
		for(int n = 0; n < 26; n++){
			if(upper.indexOf(runThrough) >= 0){
				returns.add(digiPerm.charAt(upper.indexOf(runThrough)) + "");
			}
			else if(nonAnagramLetters < 10){
				returns.add(firstRow +""+ digiPerm.charAt(nonAnagramLetters) + "");
				nonAnagramLetters++;
			}
			else{
				returns.add(secondRow +""+ digiPerm.charAt(nonAnagramLetters-10) + "");
				nonAnagramLetters++;
			}
			runThrough++;
		}
		
		return returns;
		
	}
}
