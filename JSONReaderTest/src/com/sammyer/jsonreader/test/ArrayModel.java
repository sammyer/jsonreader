/**
sammyer
*/
package com.sammyer.jsonreader.test;

import com.sammyer.json.JSONProperty;

public class ArrayModel {
	@JSONProperty
	float[] farr;
	@JSONProperty
	int[] iarr;
	@JSONProperty
	String[] sarr;
	@JSONProperty
	char[] carr;
	@JSONProperty
	public String[][] arr2d;
	@JSONProperty
	public int[][][] arr3d;
	@JSONProperty
	ModelB[] objArr;
}
