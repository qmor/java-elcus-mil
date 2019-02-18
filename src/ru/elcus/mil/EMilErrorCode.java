package ru.elcus.mil;

import ru.elcus.mil.structs.EMilFormat;

public enum EMilErrorCode {
	SX_NOERR,// 0
	SX_MEO,// 1
	SX_TOA,// 2
	SX_TOD,// 3
	SX_ELN,// 4
	SX_ERAO,// 5
	SX_ESYN,// 6
	SX_EBC; //7


	public static EMilErrorCode fromInteger(Integer errCode)
	{
		switch (errCode)
		{
		case 0x00:
			return SX_NOERR;
		case 0x01:
			return SX_MEO;
		case 0x02:
			return SX_TOA;
		case 0x03:
			return SX_TOD;
		case 0x04:
			return SX_ELN;
		case 0x05:
			return SX_ERAO;
		case 0x06:
			return SX_ESYN;
		case 0x07:
			return SX_EBC;

		default:
			return SX_NOERR;

		}
	}

}
