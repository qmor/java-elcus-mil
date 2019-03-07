package ru.elcus.mildecoders;

import javax.swing.JComponent;

import ru.elcus.mil.Mil1553Packet;

public interface IMil1553Decoder {
	int getDeviceRT();
	void initDecoder(String name, Integer rt);
	void processPacket(Mil1553Packet packet);
	JComponent getGui();
}
