package com.dew.packets;

import com.dew.packets.Packet;
import com.dew.lang.Conditional;

public class PacketListener {
	public static final long DEFAULT_TIMEOUT = 15000; // 15 seconds
	protected Protocall protocall = Protocall.NONE;
	protected Request request = Request.NULL;
	protected Packet packet = null;
	protected final boolean singleUse;
	protected long timeout = DEFAULT_TIMEOUT;

	protected static final Conditional<Packet, PacketListener> CONDITION = new Conditional<Packet, PacketListener>() {

		@Override
		public boolean evaluate(Packet t, PacketListener p) {
			if (p.getProtocall() != Protocall.NONE
					&& p.getProtocall() != t.getProtocall()) {
				return false;
			}
			if (p.getRequest() != Request.NULL
					&& p.getRequest() != t.getRequest()) {
				return false;
			}
			return true;
		}

	};

	public PacketListener() {
		this.protocall = Protocall.NONE;
		this.request = Request.NULL;
		this.singleUse = true;
	}

	public PacketListener(boolean uses) {
		this.protocall = Protocall.NONE;
		this.request = Request.NULL;
		this.singleUse = uses;
	}

	public PacketListener(Protocall p) {
		this.protocall = p;
		this.singleUse = true;
	}

	public PacketListener(Protocall p, boolean uses) {
		this.protocall = p;
		this.singleUse = uses;
	}

	public PacketListener(Protocall p, Request req) {
		this.protocall = p;
		this.request = req;
		this.singleUse = true;
	}

	public PacketListener(Protocall p, Request req, boolean uses) {
		this.protocall = p;
		this.request = req;
		this.singleUse = uses;
	}

	public PacketListener(Request req) {
		this.request = req;
		this.singleUse = true;
	}

	public PacketListener(Request req, boolean uses) {
		this.request = req;
		this.singleUse = uses;
	}

	public boolean checkConditional(Packet p) {
		return CONDITION.evaluate(p, this);
	}

	public void clear() {
		packet = null;
	}

	public synchronized Packet getPacket() throws ListenerTimeoutException { // Will return the packet, or will pause thread until it receives the packet or timeout.
		if (packet == null) {
			long elapsedTime = 0L;
			long start;
			while (packet == null && timeout - elapsedTime > 0L) {
				start = System.currentTimeMillis();
				try {
					this.wait(timeout - elapsedTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				elapsedTime += (System.currentTimeMillis() - start);
			}
		} else {
			return packet; // Skip the next "if" statement. We already know it's not null.
		}
		if (packet == null) {
			throw new ListenerTimeoutException(
					"Listener timed out while waiting for packet.");
		}
		return packet;
	}

	public Protocall getProtocall() {
		return protocall;
	}

	public Request getRequest() {
		return request;
	}

	public long getTimeout() {
		return timeout;
	}

	public boolean isSingleUse() {
		return singleUse;
	}

	public synchronized void setPacket(Packet p) {
		this.packet = p;
		this.notifyAll();
	}

	public PacketListener setProtocall(Protocall protocall) {
		this.protocall = protocall;
		return this;
	}

	public PacketListener setRequest(Request request) {
		this.request = request;
		return this;
	}

	public PacketListener setTimeout(long time) {
		if (time < 0) {
			this.timeout = DEFAULT_TIMEOUT;
			return this;
		}
		this.timeout = time;
		return this;
	}
}
