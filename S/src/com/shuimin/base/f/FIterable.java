package com.shuimin.base.f;

@SuppressWarnings("rawtypes")
public interface FIterable{

	/**
	 * <p>
	 * Call function on every element in the array return value is a new
	 * FIterable
	 * </p>
	 * 
	 * @param mapFunc
	 * @return
	 */
	public FIterable map(F mapFunc);

	/**
	 * <p>
	 * The each Function!!!
	 * </p>
	 * 
	 * @param eachFunc
	 */
	public void each(CB eachFunc);

	/**
	 * <p>
	 * Call the underlying filter function on every element then reduce it to a
	 * new FIterable object
	 * </p>
	 * 
	 * @param grepFunc
	 *            Function that returns a Boolean value
	 * @return
	 */
	public FIterable grep(F grepFunc);
	
	public Iterable val();
	
}