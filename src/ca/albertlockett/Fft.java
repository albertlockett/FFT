package ca.albertlockett;

/**
 * FFT Class
 * 
 * @author albertlockett
 *
 */
public class Fft {

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
				combined[m] = Feven[m].add(omega(n, -m).times(Fodd[m]));
				combined[m + n/2] = Feven[m].subtract(
						omega(n,-m).times(Fodd[m]));
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
		
		return new Complex[3];
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
		
		return new Complex();
	}
	
}
