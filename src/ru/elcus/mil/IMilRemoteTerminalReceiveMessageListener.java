package ru.elcus.mil;

public interface IMilRemoteTerminalReceiveMessageListener {
	void msgReceived(Mil1553Packet packet);
}
