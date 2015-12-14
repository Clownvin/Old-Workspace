package com.chatroom.packets;

import com.chatroom.main.ChatRoom;
import com.dew.lang.CString;
import com.dew.packets.AbstractPacketHandler;
import com.dew.packets.ListenerTimeoutException;
import com.dew.packets.Packet;
import com.dew.packets.PacketListener;
import com.dew.packets.Request;

public class ChatRoomPacketHandler extends AbstractPacketHandler {

    private static final ChatRoomPacketHandler SINGLETON = new ChatRoomPacketHandler();

    private ChatRoomPacketHandler() {
	// To prevent instantiation
    }

    public static ChatRoomPacketHandler getSingleton() {
	return SINGLETON;
    }

    @Override
    public boolean sendPacket(Packet p) {
	ChatRoom.getConnectionManager().sendPacket(p);
	return true;
    }

    @Override
    public void processPacket(Packet packet) {
	if (checkPacketListeners(packet))
	    return;
	switch (packet.getRequest()) {
	case MESSAGE:
	    ChatRoom.getGUI().getConsoleArea().append(((CString)packet.getData(0).getObject()).toString()+"\n");
	    break;
	default:
	    System.out.println("No case for request: " + packet.getRequest());
	    break;
	}

    }

}
