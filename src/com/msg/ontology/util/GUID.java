package com.msg.ontology.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class GUID implements java.io.Serializable, Comparable<GUID>
{

	private static final long serialVersionUID = -4856846361193249489L;

	private final long mostSigBits;

	private final long leastSigBits;

	private static class Holder
	{
		static final SecureRandom numberGenerator = new SecureRandom();
	}

	private GUID(byte[] data)
	{
		long msb = 0;
		long lsb = 0;
		assert data.length == 16 : "data must be 16 bytes in length";
		for (int i = 0; i < 8; i++)
			msb = (msb << 8) | (data[i] & 0xff);
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (data[i] & 0xff);
		this.mostSigBits = msb;
		this.leastSigBits = lsb;
	}

	public GUID(long mostSigBits, long leastSigBits)
	{
		this.mostSigBits = mostSigBits;
		this.leastSigBits = leastSigBits;
	}

	public static GUID randomUUID()
	{
		SecureRandom ng = GUID.Holder.numberGenerator;

		byte[] randomBytes = new byte[16];
		ng.nextBytes(randomBytes);
		randomBytes[6] &= 0x0f;
		randomBytes[6] |= 0x40;
		randomBytes[8] &= 0x3f;
		randomBytes[8] |= 0x80;
		return new GUID(randomBytes);
	}

	public static GUID nameUUIDFromBytes(byte[] name)
	{
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsae) {
		}
		byte[] md5Bytes = md.digest(name);
		md5Bytes[6] &= 0x0f;
		md5Bytes[6] |= 0x30;
		md5Bytes[8] &= 0x3f;
		md5Bytes[8] |= 0x80;
		return new GUID(md5Bytes);
	}

	public static GUID fromString(String name)
	{
		String[] components = name.split("-");
		if (components.length != 5)
			throw new IllegalArgumentException("Invalid UUID string: " + name);
		for (int i = 0; i < 5; i++)
			components[i] = "0x" + components[i];

		long mostSigBits = Long.decode(components[0]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[1]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[2]).longValue();

		long leastSigBits = Long.decode(components[3]).longValue();
		leastSigBits <<= 48;
		leastSigBits |= Long.decode(components[4]).longValue();

		return new GUID(mostSigBits, leastSigBits);
	}

	public long getLeastSignificantBits()
	{
		return leastSigBits;
	}

	public long getMostSignificantBits()
	{
		return mostSigBits;
	}

	public int version()
	{
		return (int) ((mostSigBits >> 12) & 0x0f);
	}

	public int variant()
	{
		return (int) ((leastSigBits >>> (64 - (leastSigBits >>> 62))) & (leastSigBits >> 63));
	}

	public long timestamp()
	{
		if (version() != 1) {
			throw new UnsupportedOperationException("Not a time-based UUID");
		}

		return (mostSigBits & 0x0FFFL) << 48 | ((mostSigBits >> 16) & 0x0FFFFL) << 32 | mostSigBits >>> 32;
	}

	public int clockSequence()
	{
		if (version() != 1) {
			throw new UnsupportedOperationException("Not a time-based UUID");
		}

		return (int) ((leastSigBits & 0x3FFF000000000000L) >>> 48);
	}

	public long node()
	{
		if (version() != 1) {
			throw new UnsupportedOperationException("Not a time-based UUID");
		}

		return leastSigBits & 0x0000FFFFFFFFFFFFL;
	}

	public String toString()
	{
		return (digits(mostSigBits >> 32, 8) + digits(mostSigBits >> 16, 4) + digits(mostSigBits, 4)
				+ digits(leastSigBits >> 48, 4) + digits(leastSigBits, 12));
	}

	private static String digits(long val, int digits)
	{
		long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}

	public int hashCode()
	{
		long hilo = mostSigBits ^ leastSigBits;
		return ((int) (hilo >> 32)) ^ (int) hilo;
	}

	public boolean equals(Object obj)
	{
		if ((null == obj) || (obj.getClass() != GUID.class))
			return false;
		GUID id = (GUID) obj;
		return (mostSigBits == id.mostSigBits && leastSigBits == id.leastSigBits);
	}

	public int compareTo(GUID val)
	{
		return (this.mostSigBits < val.mostSigBits ? -1
				: (this.mostSigBits > val.mostSigBits ? 1
						: (this.leastSigBits < val.leastSigBits ? -1
								: (this.leastSigBits > val.leastSigBits ? 1 : 0))));
	}
}
