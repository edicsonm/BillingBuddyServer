package au.com.billingbuddy.common.objects;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import au.com.billingbuddy.exceptions.objects.SecurityMethodsException;

public class SecurityMethods {

	
	public static String GenerateSALT(String str) throws SecurityMethodsException {
		 return GenereateMD5(str+Long.toString(System.currentTimeMillis()));
	}
	
	public static String GenereateMD5(String str) throws SecurityMethodsException {
		MessageDigest nessageDigest = null;
		try {
			nessageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityMethodsException(e);
		}
		nessageDigest.update(str.getBytes(), 0, str.length());
		return new BigInteger(1, nessageDigest.digest()).toString(16);
	}
	
	public static String sha1Calculator(String value) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(value.getBytes());
			return byteToHexa(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String byteToHexa(byte[] bytes){
		return Hex.encodeHexString(bytes);
	}
	
	public static byte[] hexaToBytes(String hexa){
		byte[] bytes = null;
		try {
			bytes = Hex.decodeHex(hexa.toCharArray());
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
}
