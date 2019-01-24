package ru.elcus.mil.structs;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class CC extends Structure
{
	public short[] awVTMK4OutBuf = new short[6]; 
	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList(new String[] {"awVTMK4OutBuf"});
	}
}