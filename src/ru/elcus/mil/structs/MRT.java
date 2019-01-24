package ru.elcus.mil.structs;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class MRT extends Structure{
	public  short wStatus;
	
  	@Override
  	protected List<String> getFieldOrder() {
  		return Arrays.asList(new String[] { "wStatus"});
  	}
}
