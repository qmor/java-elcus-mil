package ru.elcus.mildecoders;

import javax.swing.JPanel;
import javax.swing.JTextArea;

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
		StringBuilder sb = new StringBuilder();
		int cmdword = packet.commandWord&2047;
		if (cmdword==0x440)
		{
			sb.append("<li>Пакет татной ТМИ</li><br/>");
			sb.append(String.format("<li>Номер массива %d</li><br/>",packet.dataWords[0]));
			sb.append(String.format("<li>Время привязки ТМИ %d</li><br/>",packet.dataWords[1]<<16|packet.dataWords[2]));
		}
		packet.decodeHTMLString = sb.toString();
		JPanel panel;
		JTextArea ta;

	}

}
