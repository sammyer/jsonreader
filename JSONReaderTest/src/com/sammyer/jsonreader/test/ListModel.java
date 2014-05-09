/**
sammyer
*/
package com.sammyer.jsonreader.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sammyer.json.JSONProperty;

public class ListModel {
	@JSONProperty
	List<ModelB> objListBasic;
	@JSONProperty
	Collection<ModelB> objColl;
	@JSONProperty
	ArrayList<ModelB> objList;
	@JSONProperty
	ArrayList<Integer> ilist;
	@JSONProperty
	ArrayList<Float> flist;
	@JSONProperty
	ArrayList<Character> clist;
	@JSONProperty
	ArrayList<String> slist;
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clist == null) ? 0 : clist.hashCode());
		result = prime * result + ((flist == null) ? 0 : flist.hashCode());
		result = prime * result + ((ilist == null) ? 0 : ilist.hashCode());
		result = prime * result + ((objColl == null) ? 0 : objColl.hashCode());
		result = prime * result + ((objList == null) ? 0 : objList.hashCode());
		result = prime * result
				+ ((objListBasic == null) ? 0 : objListBasic.hashCode());
		result = prime * result + ((slist == null) ? 0 : slist.hashCode());
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
		ListModel other = (ListModel) obj;
		if (clist == null) {
			if (other.clist != null)
				return false;
		} else if (!clist.equals(other.clist))
			return false;
		if (flist == null) {
			if (other.flist != null)
				return false;
		} else if (!flist.equals(other.flist))
			return false;
		if (ilist == null) {
			if (other.ilist != null)
				return false;
		} else if (!ilist.equals(other.ilist))
			return false;
		if (objColl == null) {
			if (other.objColl != null)
				return false;
		} else if (!objColl.equals(other.objColl))
			return false;
		if (objList == null) {
			if (other.objList != null)
				return false;
		} else if (!objList.equals(other.objList))
			return false;
		if (objListBasic == null) {
			if (other.objListBasic != null)
				return false;
		} else if (!objListBasic.equals(other.objListBasic))
			return false;
		if (slist == null) {
			if (other.slist != null)
				return false;
		} else if (!slist.equals(other.slist))
			return false;
		return true;
	}
	
}
