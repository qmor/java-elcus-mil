package ru.elcus.mil.structs;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class TMK extends Structure{
	public short wRequest;
  	@Override
  	protected List<String> getFieldOrder() {
  		return Arrays.asList(new String[] { "wRequest"});
  	}
}
