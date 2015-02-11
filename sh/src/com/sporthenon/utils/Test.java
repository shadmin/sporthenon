package com.sporthenon.utils;

import org.postgresql.util.Base64;

public class Test {
	
	public static void main(String[] args) throws Exception {
		System.out.println(Base64.encodeBytes("10-200-200-300-151,152-_fr".getBytes()));;
		System.out.println(new String(Base64.decode("MTAtMjAwLTIwMC0zMDAtMTUxLDE1Mi1fZnI=")));
	}
	
}