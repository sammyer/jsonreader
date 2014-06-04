/**
sammyer
*/
package com.sammyer.jsonreader.test;

import com.sammyer.json.JSONProperty;

public class KeyNameModel {
	@JSONProperty
	public String withoutKey;
	@JSONProperty("hasKey")
	public String withKey;
}
