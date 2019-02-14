package ru.elcus.mildecoders;

import ru.elcus.mil.Mil1553Packet;

public class MBOKZ2V implements IMil1553Decoder {
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
		// TODO Auto-generated method stub
	}

}
