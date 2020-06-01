package com.cy.pj.common.util;

public class AssertUtil {

	public static void isArgsValid(boolean statement,String msg) {
		if(statement)
			throw new IllegalArgumentException(msg);
	}
}
