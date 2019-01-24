package ru.elcus.mil.structs;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class CLong2 extends Structure{
	public long[] _VTMK4Arg = new long[2];
	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList(new String[] {"_VTMK4Arg"});
	}
}
