package ru.elcus.mil.structs;

public enum EMilFormat {


	//MIL_DATA_BC_RT =0x00,
	//MIL_DATA_RT_BC =0x01,
	//MIL_DATA_RT_RT =0x02,
	//MIL_CTRL_C_A = 0x03,
	//MIL_CTRL_CD_A = 0x04,
	//MIL_CTRL_C_AD = 0x05,
	//MIL_DATA_BC_RT_BRCST = 0x08,
	//MIL_DATA_RT_RT_BRCST =0x0A,
	//MIL_CTRL_C_BRCST = 0x0B,
	//MIL_CTRL_CD_BRCST = 0x0C,
	CC_FMT_1,
	CC_FMT_2,
	CC_FMT_3,
	CC_FMT_4,
	CC_FMT_5,
	CC_FMT_6,
	CC_FMT_7,
	CC_FMT_8,
	CC_FMT_9,
	CC_FMT_10,
	CC_FMT_UNKNWN;
/*
 * CC_FMT_1 = 0x00,
CC_FMT_2 = 0x01,
CC_FMT_3 = 0x02,
CC_FMT_4 = 0x03,
CC_FMT_5 = 0x05,
CC_FMT_6 = 0x04,
CC_FMT_7 = 0x08,
CC_FMT_8 = 0x0A,
CC_FMT_9 = 0x0B,
CC_FMT_10 = 0x0C 
 */
	public Integer asInteger()
	{
		switch (this) {
		case CC_FMT_1:
			return 0;
		case CC_FMT_2:
			return 0x01;
		case CC_FMT_3:
			return 0x02;
		case CC_FMT_4:
			return 0x03;
		case CC_FMT_5:
			return 0x05;
		case CC_FMT_6:
			return 0x04;
		case CC_FMT_7:
			return 0x08;
		case CC_FMT_8:
			return 0x0a;
		case CC_FMT_9:
			return 0x0b;
		case CC_FMT_10:
			return 0x0c;
		default:
			return -1;
		}
	}
	public EMilFormat fromInteger(Integer fmtcode)
	{
		switch (fmtcode)
		{
		case 0x00:
			return CC_FMT_1;
		case 0x01:
			return CC_FMT_2;
		case 0x02:
			return CC_FMT_3;
		case 0x03:
			return CC_FMT_4;
		case 0x05:
			return CC_FMT_5;
		case 0x04:
			return CC_FMT_6;
		case 0x08:
			return CC_FMT_7;
		case 0x0a:
			return CC_FMT_8;
		case 0x0b:
			return CC_FMT_9;
		case 0x0c:
			return CC_FMT_10;
		default:
			return EMilFormat.CC_FMT_UNKNWN;

		}
	}

}
