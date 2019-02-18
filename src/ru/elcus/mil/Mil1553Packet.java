package ru.elcus.mil;

import ru.elcus.mil.structs.EBus;
import ru.elcus.mil.structs.EMilFormat;

import java.time.LocalDateTime;


public class Mil1553Packet {
	public LocalDateTime date;
	
	public short commandWord;
	public short answerWord;
	public short[] dataWords = new short[32];
	public EBus bus;
	public EMilFormat format;
	public EMilPacketStatus status;
	public EMilErrorCode errorCode  = EMilErrorCode.SX_NOERR;
	public String decodeHTMLString;
	public String shortDescr;
	public Mil1553Packet() {}
	public Mil1553Packet(Mil1553RawPacketMT rawPacket)
	{
		date =  LocalDateTime.now();
		bus = ((rawPacket.sw&0xffff)>>15)==1?EBus.eBusB:EBus.eBusA;
		commandWord = rawPacket.basedata[0];
		
		status = EMilPacketStatus.eRECEIVED;

		if (!errorCode.equals(EMilErrorCode.SX_NOERR))
		{
			status = EMilPacketStatus.eFAILED;
		}
		
		
		format = calcFormat(commandWord);
		switch (format) {
		case CC_FMT_1:
			System.arraycopy(rawPacket.basedata,1,dataWords,0,getWordsCount(commandWord));
			int i = getWordsCount(commandWord);
			if (i==0) 
				i = 32;
			answerWord = rawPacket.basedata[i+1];
			break;
		case CC_FMT_2:
			answerWord = rawPacket
			.basedata[1];
			System.arraycopy(rawPacket.basedata,2,dataWords,0,getWordsCount(commandWord));
			break;
		case CC_FMT_4:
			answerWord = rawPacket.basedata[1];
			break;
		case CC_FMT_5:
			answerWord = rawPacket.basedata[1];
			dataWords[0] = rawPacket.basedata[2];
			break;
		case CC_FMT_6:
			answerWord = rawPacket.basedata[2];
			dataWords[0] = rawPacket.basedata[1];
			break;
		default:
			break;
		}

	}
	public static Integer getWordsCount(short cmdWord)
	{
		return cmdWord&0x1f;
	}
	public static Integer getRTRBit(short cmdWord)
	{
		return (cmdWord>>10)&0x1;
	}
	public static Integer getSubAddress(short cmdWord)
	{
		return (cmdWord>>5)&0x1f;
	}
	public static Integer getRtAddress(short cmdWord)
	{
		return (cmdWord&0xffff)>>11;
	}
	public static EMilFormat calcFormat(short cmdWord)
	{
		Integer rtrbit = getRTRBit(cmdWord);
		Integer subaddress = getSubAddress(cmdWord);
		boolean isItMode = (subaddress==0||subaddress==0x1f)?true:false;

		if (rtrbit==0 && !isItMode)
		{
			return EMilFormat.CC_FMT_1;
		}
		else if (rtrbit==1 && !isItMode)
		{
			return EMilFormat.CC_FMT_2;
		}
		else if (rtrbit==1 && isItMode && (subaddress>=0 && subaddress<=15))
		{
			return EMilFormat.CC_FMT_4;
		}
		else if (rtrbit==1 && isItMode && (subaddress==16 || subaddress==18 || subaddress==19))
		{
			return EMilFormat.CC_FMT_5;
		}
		else if (rtrbit==0 && isItMode && (subaddress==17 || subaddress==20 || subaddress==21))
		{
			return EMilFormat.CC_FMT_6;
		}

		return EMilFormat.CC_FMT_UNKNWN;
		
	}
	
	@Override
	public String toString() {
		return String.format("CW %04x AW %04x %s %s %s %s [%s]", commandWord, answerWord, format, bus, TimeManipulation.ToLongTimeStringMillis(date),shortDescr,errorCode);
	}
}
