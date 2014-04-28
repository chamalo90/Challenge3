package com.jcolmonts.challenge3;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Sha256HashTest{
  public static final Integer TO_ENCODE=0x12345678;
	
  private static byte[] sha256(byte[] input){
    try{
      MessageDigest digest=MessageDigest.getInstance("SHA-256");
      digest.update(input);
      return digest.digest();

    }
    catch(Exception e){
      e.printStackTrace();
      return null;
    }
  }
  
  private static String hexString(byte[] toConvert){
    return String.format("%x", new BigInteger(1, toConvert));
  }
  
	/**
	 * Reduce function for the rainbow table.
	 * 
	 * <p>It computes a password in the specified range (character set + password length) from a hash.
	 * The computed password depends on the supplied column index.</p> 
	 */
	public static String reduce(byte[] hash, int columnIndex, String characterSet, int passwordLength) {
/*		int setLength = characterSet.length();
		int hashLength = hash.length;
		char[] result = new char[passwordLength];
		for (int i = 0; i < passwordLength; i++) {
			int index = hash[(i + columnIndex) % hashLength];
			index += 128; // Add 128 to get a positive number because bytes range from -128 to 127.
			result[i] = characterSet.charAt(index % setLength);
		}
		return new String(result);*/
		
		int setLength = characterSet.length();
		int hashLength = hash.length;
		
		
		
		return hash+columnIndex; // lol
		
		
		
		
	}
	
	public static void test(Integer debut, int number, int ligne){
		List<Integer> all = new LinkedList<Integer>();
		List<Integer> fin = new LinkedList<Integer>();
		Integer previous=debut;
		int doublon=0;
		int doublonFin=0;
		for(int j=0; j<ligne;j++){
			all.add(previous);
			for(int i=0;i<number;i++){
				byte[] hash=sha256(sha256(sha256(ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(previous).array())));
				previous=Integer.parseInt(reduce(hash,i,"0123456789", 8));
			}
			if(fin.contains(previous)){
				doublonFin++;
			}	
			else{
				fin.add(previous);
			}	
			previous=new Random().nextInt();  
			while(all.contains(previous)){
				doublon++;
				previous=new Random().nextInt();  
			}
			if(j%1000==0){
				System.out.println(j);
				System.out.println(doublon + " sur "+ ligne + " soit "+ ((float)doublon/ligne*100));
				System.out.println(doublonFin + " sur "+ ligne + " soit "+ ((float)doublonFin/ligne*100));
			}
		}
		System.out.println(doublon + " sur "+ ligne + " soit "+ ((float)doublon/ligne*100));
		System.out.println(doublonFin + " sur "+ ligne + " soit "+ ((float)doublonFin/ligne*100));
	}
	
  public static void main(String[] args){
    try {      

      test(TO_ENCODE,10000,5000);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
}
