/**
sammyer
*/
package com.sammyer.jsonreader.test;

import java.util.Arrays;

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
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(arr2d);
		result = prime * result + Arrays.hashCode(arr3d);
		result = prime * result + Arrays.hashCode(carr);
		result = prime * result + Arrays.hashCode(farr);
		result = prime * result + Arrays.hashCode(iarr);
		result = prime * result + Arrays.hashCode(objArr);
		result = prime * result + Arrays.hashCode(sarr);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArrayModel other = (ArrayModel) obj;
		if (!Arrays.deepEquals(arr2d, other.arr2d))
			return false;
		if (!Arrays.deepEquals(arr3d, other.arr3d))
			return false;
		if (!Arrays.equals(carr, other.carr))
			return false;
		if (!Arrays.equals(farr, other.farr))
			return false;
		if (!Arrays.equals(iarr, other.iarr))
			return false;
		if (!Arrays.equals(objArr, other.objArr))
			return false;
		if (!Arrays.equals(sarr, other.sarr))
			return false;
		return true;
	}
	
	
}
