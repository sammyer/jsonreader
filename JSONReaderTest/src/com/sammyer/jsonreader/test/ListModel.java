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
}
