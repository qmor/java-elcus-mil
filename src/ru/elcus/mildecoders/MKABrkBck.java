package ru.elcus.mildecoders;

import ru.elcus.mil.Mil1553Packet;

public class MKABrkBck implements IMil1553Decoder {
	Integer rt;
	String name;
	@Override
	public int getDeviceRT() {
		return rt;
	}

	@Override
	public void initDecoder(String name, Integer rt) {
		this.name = name;
		this.rt = rt;
		
	}

	@Override
	public void processPacket(Mil1553Packet packet) {
		int sa = Mil1553Packet.getSubAddress(packet.commandWord);
		int rtr =Mil1553Packet.getRTRBit(packet.commandWord); 
		if (rtr==0 && sa>=1&& sa<=8)
		{
			packet.decodeHTMLString = String.format("КПИ %d",sa );
			packet.shortDescr = String.format("КПИ %d",sa);
		}
		else if (rtr == 1 && sa == 10)
		{
			packet.decodeHTMLString = String.format("ПК");
			packet.shortDescr =  String.format("ПК");
		}
		else if (rtr == 0 && sa == 11)
		{
			packet.decodeHTMLString = String.format("КвтПК");
			packet.shortDescr =  String.format("КвтПК");
		}		
		
		else if (rtr==0 && sa>=29&& sa<=30)
		{
			packet.decodeHTMLString = String.format("ТМИ КИС %d",sa-28 );
			packet.shortDescr =  String.format("ТМИ КИС %d",sa-28 );
		}
		else if (rtr==1 && sa>=12&& sa<=28)
		{
			packet.decodeHTMLString = String.format("ТМИ БЦК %d",sa-11 );
			packet.shortDescr =  String.format("ТМИ БЦК %d",sa-11 );
		}
	}

}
