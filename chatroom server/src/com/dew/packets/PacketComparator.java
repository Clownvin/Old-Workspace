package com.dew.packets;

import java.util.Comparator;

import com.dew.packets.Packet;

public class PacketComparator implements Comparator<Packet> {

	@Override
	public int compare(Packet o1, Packet o2) {
		return o1.getProtocall().getPriority()
				- o2.getProtocall().getPriority();
	}

}
