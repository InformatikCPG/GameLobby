package de.cpg_gilching.informatik11.gamelobby.shared.net;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet {
	
	public static final long MAGIC_NUMBER = 0x9001FACEFAL;
	
	protected Packet() {
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
