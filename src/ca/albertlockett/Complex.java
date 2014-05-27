package ca.albertlockett;

/**
 * Custom implementation of complex number class
 * @author albertlockett
 *
 */
public class Complex {
	
	private Double real;
	private Double imag;
	
	// Constructors
	public Complex(){
		this.real = 0.0;
		this.imag = 0.0;
	}
	public Complex(Double real, Double imag){
		this.real = real;
		this.imag = imag;
	}
	
	// Getters and setters
	public Double getReal(){
		return this.real;
	}
	public void setReadl(Double real){
		this.real = real;
	}
	
	public Double getImag(){
		return this.imag;
	}
	public void setImag(Double imag){
		this.imag = imag;
	}
	
	
	// Arithmetic Operations
	
	/**
	 * Arithmetic Multiply
	 * @param c2 whats multiplying this object
	 * @return this *  c2
	 */
	public Complex times(Complex c2){
		return new Complex();
	}
	
	/**
	 * Arithmentic Add
	 * @param c2 what is added to this object
	 * @return this + c2
	 */
	public Complex add(Complex c2){
		return new Complex();
	}
	
	/**
	 * Arithmetic Subtract
	 * @param c2 what is subtracted from this object
	 * @return this - c2
	 */
	public Complex subtract(Complex c2){
		return new Complex();
	}
	
	/**
	 * Arithmetic Divide
	 * @param c2 what is dividing this object
	 * @return this / c2
	 */
	public Complex divide(Complex c2){
		return new Complex();
	}
	
	// Other operations
	
	/**
	 * Switches from + to - sign
	 * @return - this
	 */
	public Complex switchSign(){
		return this;
	}
	
	/**
	 * Calculate reciprocal
	 * @reutrn 1/this
	 */
	public Complex invert(){
		return this;
	}
}
