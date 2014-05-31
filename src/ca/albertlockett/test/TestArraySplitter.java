package ca.albertlockett.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.albertlockett.ArraySplitter;
import ca.albertlockett.Complex;


/**
 * Test class for ArraySplitter
 * 
 * @author albertlockett
 *
 */
public class TestArraySplitter {

	@Test
	public void smokeTest(){
		
		// Smoke test 1:
		Complex[] smokeTest1Signal = {
				new Complex(1),
				new Complex(0),
				new Complex(2),
				new Complex(0)
		};
		
		Complex[] fEven = ArraySplitter.getArrayPart(
				smokeTest1Signal,
				ArraySplitter.FFT.EVEN);
		
		Complex[] smokeTest1Exp =  {smokeTest1Signal[0], smokeTest1Signal[2]};
		for(int i = 0; i < fEven.length; i++){
			assertEquals(smokeTest1Exp[i], fEven[i]);
		}
		
		
	}
	
	
}
