package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import operations.*;
public class VICOperationsTest {
	@Test
	public void noCarryAdditionTest(){
		VICOperations vid = new VICOperations();
		assertEquals(15, vid.noCarryAddition(50, 65));
		assertEquals(0, vid.noCarryAddition(0, 0));
		assertEquals(534, vid.noCarryAddition(985, 659));
	}
	
	@Test
	public void chainAdditionTest(){
		VICOperations vic = new VICOperations();
		assertEquals(64, vic.chainAddition(640551213, 2));
		assertEquals(66, vic.chainAddition(6, 2));
		assertEquals(6628, vic.chainAddition(6, 4));
	}
	
	@Test
	public void digitPermutaionTest(){
		VICOperations vic = new VICOperations();
		assertTrue("2134697850".equals(vic.digitPermutation("Hello world")));
		assertTrue("4071826395".equals(vic.digitPermutation("BANANALAND")));
		assertTrue("1528069374".equals(vic.digitPermutation("2527058253")));
		
	}
	
	@Test
	public void stradlingCheckerboardTest(){
		VICOperations vic = new VICOperations();
		assertEquals(null, vic.straddlingCheckerboard("4071826395", "Hello there"));
	}
	
	@Test
	public void fullTest(){
		String agentID ="85721";
	    String date = "470918";
	    String phrase = "CHOOSETHER";
	    String phraseOriginal;
	    String anagram = "HEAT IS ON";
	    String message ="RUNAWAY";
	    String messageOriginal;
		 long noCarry = 0;
		 long chainAdd = 0;
		 String digiPerm = "";
		 ArrayList<String> stradCheck = null;
		 String encoded = "RUNAWAY";
		
		noCarry = VICOperations.noCarryAddition(Long.parseLong(date.substring(0, 5)), Long.parseLong(agentID));
		chainAdd = VICOperations.chainAddition(noCarry, 10);
		digiPerm = VICOperations.digitPermutation(phrase);
		noCarry = VICOperations.noCarryAddition(chainAdd, Long.parseLong(digiPerm));
		digiPerm = VICOperations.digitPermutation(noCarry+"");
		stradCheck = VICOperations.straddlingCheckerboard(digiPerm, anagram);
		char codeHelp = 'A';
		for(int n = 0; n < 26; n++){
				encoded = encoded.replaceAll(codeHelp + "", stradCheck.get(n));
				codeHelp++;
		}
		String message2 = "45385350074420063853973232044538535007442035839732027304453853500744283235423573240224020259535839732";
		int insertWhere = Integer.parseInt(date.substring(5, 6));
		if(encoded.length() <= insertWhere){
			encoded = encoded+agentID;
		}
		else{
			encoded = encoded.substring(0, insertWhere) + agentID + encoded.substring(insertWhere);
		}
		System.out.println(stradCheck.toString());
		System.out.println(encoded);
		/*
		 * 470918
choose THE representation THAT best SUPPORTS the OPERATIONS
hEaT iS oN
91592357857210178
		 */
		
		char firstIndex = digiPerm.charAt(anagram.indexOf(" "));
		char secondIndex = digiPerm.charAt(anagram.lastIndexOf(" "));
		System.out.println(firstIndex + " " + secondIndex);
		String capture = "";
		String printMe = "";
		char re = 'A';
		char other = '0';
		char [] alphabet = new char [26];
		for( int n = 0; n < 26; n++){
			alphabet[n] = re;
			System.out.print(alphabet[n] + ", ");
			re++;
		}
		for(int n = 0; n < message2.length(); n++){
			//Grabs the char and if it is one of the headers for the
			//two number strings, i.e. 01 vs 1, then it will grab the
			//next char and then add to the message to print
			if(message2.charAt(n) == firstIndex || message2.charAt(n) == secondIndex){
				capture = message2.charAt(n) + "" +message2.charAt(n+1);
				n++;
				printMe = printMe + alphabet[stradCheck.indexOf(capture)];
			}
			else{
				capture = message2.charAt(n) + "";
				printMe = printMe + alphabet[stradCheck.indexOf(capture)];
			}
		}
//		
		System.out.println(printMe);
		//Runtime.getRuntime().exec(DecryptVIC("decode.dec"));
		
	}
}
