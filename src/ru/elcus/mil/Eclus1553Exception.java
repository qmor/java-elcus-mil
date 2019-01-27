package ru.elcus.mil;

public class Eclus1553Exception extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5958056693177730465L;

	public Eclus1553Exception(Elcus1553Device device, String message)
	{
		super(String.format("[%s] [%s] %s",device.getCardNumber(), device.getWorkMode(), message));
	}
}
