package ru.elcus.mil.structs;

public enum EBus {
eBusA,
eBusB;

public Integer toInt()
{
	if (this.equals(eBusA) )
		return 0;
	return 1;
}

}
