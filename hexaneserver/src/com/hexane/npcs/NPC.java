package com.hexane.npcs;

public abstract class NPC {
	public final int npcID;
	public final long npcManagerID;

	public NPC(final int npcID, final long npcManagerID) {
		this.npcID = npcID;
		this.npcManagerID = npcManagerID;
	}
}
