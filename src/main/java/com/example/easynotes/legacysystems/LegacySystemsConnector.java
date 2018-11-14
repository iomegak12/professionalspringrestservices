package com.example.easynotes.legacysystems;

public class LegacySystemsConnector {
	public static int connectAndTrigger(int referenceKey, String title) {
		System.out.println("Connecting to Legacy Systems ... with ... " +
				referenceKey + ", " + title);
	
		return (int) (referenceKey / 2);
	}
}
