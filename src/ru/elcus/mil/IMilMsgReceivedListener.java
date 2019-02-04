package ru.elcus.mil;

@FunctionalInterface
public interface IMilMsgReceivedListener {
	void msgReceived(Mil1553Packet packet);
}
