/**
sammyer
*/
package com.sammyer.jsonreader.test;

import com.sammyer.json.JSONProperty;

public class PrimitiveModel {
	@JSONProperty
	boolean b;
	@JSONProperty
	byte by;
	@JSONProperty
	short s;
	@JSONProperty
	int i;
	@JSONProperty
	long l;
	@JSONProperty
	char c;
	@JSONProperty
	String str;
	@JSONProperty
	float f;
	@JSONProperty
	double d;
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (b ? 1231 : 1237);
		result = prime * result + by;
		result = prime * result + c;
		long temp;
		temp = Double.doubleToLongBits(d);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(f);
		result = prime * result + i;
		result = prime * result + (int) (l ^ (l >>> 32));
		result = prime * result + s;
		result = prime * result + ((str == null) ? 0 : str.hashCode());
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
		PrimitiveModel other = (PrimitiveModel) obj;
		if (b != other.b)
			return false;
		if (by != other.by)
			return false;
		if (c != other.c)
			return false;
		if (Double.doubleToLongBits(d) != Double.doubleToLongBits(other.d))
			return false;
		if (Float.floatToIntBits(f) != Float.floatToIntBits(other.f))
			return false;
		if (i != other.i)
			return false;
		if (l != other.l)
			return false;
		if (s != other.s)
			return false;
		if (str == null) {
			if (other.str != null)
				return false;
		} else if (!str.equals(other.str))
			return false;
		return true;
	}
	
	
}
