package com.thirdparty.proxy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 加密解密工具包
 * @author Winter Lau
 * @date 2011-12-26
 */
public class CyptoUtils {

	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	
    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws InvalidAlgorithmParameterException 
     * @throws Exception 
     */
    public static String desEncode(String key,String data) {
    	if(data == null)
    		return null;
    	try{
	    	DESKeySpec dks = new DESKeySpec(key.getBytes());	    	
	    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        //key的长度不能够小于8位字节
	        Key secretKey = keyFactory.generateSecret(dks);
	        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
	        IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
	        AlgorithmParameterSpec paramSpec = iv;
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);           
	        byte[] bytes = cipher.doFinal(data.getBytes());            
	        return byte2hex(bytes);
    	}catch(Exception e){
    		e.printStackTrace();
    		return data;
    	}
    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    public static String desDecode(String key,String data) {
    	if(data == null)
    		return null;
        try {
	    	DESKeySpec dks = new DESKeySpec(key.getBytes());
	    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(hex2byte(data.getBytes())));
        } catch (Exception e){
    		e.printStackTrace();
    		return data;
        }
    }

	/**
	 * 二行制转字符串
	 * @param b
	 * @return
	 */
    private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b!=null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}

    
    private static byte[] hex2byte(byte[] b) {
        if((b.length%2)!=0)
            throw new IllegalArgumentException();
		byte[] b2 = new byte[b.length/2];
		for (int n = 0; n < b.length; n+=2) {
		    String item = new String(b,n,2);
		    b2[n/2] = (byte)Integer.parseInt(item,16);
		}
        return b2;
    }

	/**
	 * 获取字符串的 MD5
	 */
	public static String md5Encode(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes("UTF-8"));
			byte messageDigest[] = md5.digest();
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest) {
				hexString.append(String.format("%02X", b));
			}
			return hexString.toString().toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取文件的 MD5
	 */
	public static String md5Encode(File file) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			FileInputStream inputStream = new FileInputStream(file);
			DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
			//必须把文件读取完毕才能拿到md5
			byte[] buffer = new byte[4096];
			while (digestInputStream.read(buffer) > -1) {
			}
			MessageDigest digest = digestInputStream.getMessageDigest();
			digestInputStream.close();
			byte[] md5 = digest.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : md5) {
				sb.append(String.format("%02X", b));
			}
			return sb.toString().toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
}
