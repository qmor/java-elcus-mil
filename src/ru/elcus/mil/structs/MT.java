package ru.elcus.mil.structs;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class MT extends Structure{
	public  short wBase;
	public  short wResultX;
	
  	@Override
  	protected List<String> getFieldOrder() {
  		return Arrays.asList(new String[] { "wBase","wResultX"});
  	}
}
