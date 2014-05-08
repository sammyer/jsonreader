/**
sammyer
*/
package com.sammyer.jsonreader.test;

import com.sammyer.json.JSONProperty;

public class ModelB {
	@JSONProperty
	String name;
	@JSONProperty
	int age;
	
	//NOTE : Models must have an empty constructor
	public ModelB() {}
	
	public ModelB(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String toString() {
		return "[ModelB name="+name+" age="+age+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ModelB other = (ModelB) obj;
		if (age != other.age)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
