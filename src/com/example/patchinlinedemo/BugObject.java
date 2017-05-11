package com.example.patchinlinedemo;

public class BugObject {
	
	public static String fun() {
		String result = "a";
		//android.util.Log.d("PatchInlineDemo", "enter fun return=" + result);
		return result;
	}
}