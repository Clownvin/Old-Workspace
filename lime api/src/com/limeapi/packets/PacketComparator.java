package com.limeapi.packets;

import java.util.Comparator;

public class PacketComparator implements Comparator<Packet> {

    @Override
    public int compare(Packet o1, Packet o2) {
	return o1.getProtocall().priority - o2.getProtocall().priority;
    }

}
