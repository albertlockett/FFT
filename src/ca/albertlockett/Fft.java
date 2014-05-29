package ca.albertlockett;

import java.util.ArrayList;
import java.util.List;

/**
 * FFT Class
 * 
 * @author albertlockett
 *
 */
public class Fft {

	private final ComplexOperator co = new ComplexOperator();
	
	/**
	 * Recursively calculate FFT 
	 * 
	 * Based on description available here: 
	 * http://jeremykun.com/2012/07/18/the-fast-fourier-transform/
	 * @param signal
	 * @return FFT of signal
	 */
	public Complex[] fft(Complex[] signal){
		
		int n = signal.length;
		
		if(n == 1){
		
			return signal;
		
		} else {
			
			Complex[] Feven = getArrayPart(signal, FFT.EVEN);
			Complex[] Fodd  = getArrayPart(signal, FFT.ODD);
			
			Complex[] combined = new Complex[n];
			
			for(int m = 0; m < n/2; m++){
				
				combined[m] = co.add(Feven[m], co.times(omega(n,-m), Feven[m]));
				combined[m + n/2] = co.subtract(Feven[m],
						co.times(omega(n,-m), Fodd[m]));
			}
			
			return combined;
		}
		
		
	}
	
	/**
	 * Used to choose the whether the partial array should return even or odd
	 * values of the signal
	 * @author albertlockett
	 *
	 */
	private enum FFT{
		EVEN, ODD;
	}
	
	
	/**
	 * Splits signal array into even or odd elements based on part enum
	 * @param signal signal to be split
	 * @param part FFT either even or odd
	 * @return either all even values or all odd values of signal
	 */
	private Complex[] getArrayPart(Complex[] signal, FFT part){
		
		List<Complex> outputSignalContainer = new ArrayList<Complex>();
		
		int x = 1;
		if (FFT.EVEN == part){
			x = 0;
		}
		
		for(int i = 0; i < signal.length; i++){
			if((i + x) % 2 == 0){
				outputSignalContainer.add(signal[i]);
			}
		}
		
		Complex[] outputSignal = new Complex[outputSignalContainer.size()];
		for(int i = 0; i < outputSignal.length; i++){
			outputSignal[i] = outputSignalContainer.get(i);
		}
		
		
		return outputSignal;
	}
	
	
	/**
	 * Shorthand for complex exponential
	 * 
	 * w(n,m) = e^(2*pi*j*m/n)
	 * 
	 * @param n demoninator
	 * @param m numerator
	 * @return complex value of e^(2*pi*j*m/n)
	 */
	private Complex omega(int n, int m) {
		
		double x = 2.0 * 3.145 * (double) m / (double) n;
		
		return new Complex( Math.cos(x), Math.sin(x));
	}
	
}
