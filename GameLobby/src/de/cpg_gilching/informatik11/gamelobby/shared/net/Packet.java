package de.cpg_gilching.informatik11.gamelobby.shared.net;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketHallo;

public abstract class Packet {
	
	public static final long MAGIC_NUMBER = 0x9001FACEFAL;
	public static final Map<Integer, Class<? extends Packet>> byId = new HashMap<Integer, Class<? extends Packet>>();
	public static final Map<Class<? extends Packet>, Integer> byClass = new HashMap<Class<? extends Packet>, Integer>();
	
	static {
		addPacket(PacketHallo.class);
		addPacket(PacketDisconnect.class);
		
		//		addPacket(0x01, PacketHandshake.class);
		//		addPacket(0x03, PacketCommand.class);
		//		addPacket(0x04, PacketIntCommand.class);
		//		addPacket(0x05, PacketKeyState.class);
		//		addPacket(0x06, PacketMouseState.class);
		//		addPacket(0x07, PacketCharacterRotation.class);
		//		addPacket(0x0a, PacketConsoleLine.class);
		//		addPacket(0x0b, PacketBigMessage.class);
		//		addPacket(0x0c, PacketPlayerList.class);
		//		addPacket(0x10, PacketSpawnEntity.class);
		//		addPacket(0x11, PacketEntityMetadata.class);
		//		addPacket(0x12, PacketEntityMove.class);
		//		addPacket(0x13, PacketEntityVelocity.class);
		//		addPacket(0x30, PacketInventoryItem.class);
		//		addPacket(0x31, PacketChangeHeldItem.class);
		//		addPacket(0x32, PacketPlayerHealth.class);
		//		addPacket(0x50, PacketEditorEnterLeave.class);
		//		addPacket(0x52, PacketChangePlayerModel.class);
		//		addPacket(0xff, PacketDisconnect.class);
	}
	
	public static void addPacket(Class<? extends Packet> clazz) {
		int id = clazz.getCanonicalName().hashCode() & 0xFFFF;
		
		if (byId.containsKey(id) || byClass.containsKey(clazz))
			throw new Error("duplicate packet id: " + id + " for packet " + clazz.getCanonicalName());
		
		byId.put(id, clazz);
		byClass.put(clazz, id);
	}
	
	public final int id;
	
	protected Packet() {
		this.id = byClass.get(this.getClass());
	}
	
	public abstract void write(DataOutputStream out) throws IOException;
	
	public abstract void read(DataInputStream in) throws IOException;
	
	public boolean shouldLog() {
		return false;
	}
	
	public static void writeEnum(DataOutput out, Enum<?> enumValue) throws IOException {
		if (enumValue == null)
			out.writeUTF("");
		else
			out.writeUTF(enumValue.name());
	}
	
	public static <T extends Enum<T>> T readEnum(DataInput in, Class<T> enumType) throws IOException {
		String name = in.readUTF();
		if (name.isEmpty())
			return null;
		else
			return Enum.valueOf(enumType, name);
	}
	
}
