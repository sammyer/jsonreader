/**
sammyer
*/
package com.sammyer.jsonreader.test;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;
import android.util.Log;

import com.sammyer.json.JSONParser;

public class JSONTestCase extends AndroidTestCase {
	private ModelB testObjA=new ModelB("Abe",99);
	private ModelB testObjB=new ModelB("Bart",10);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	
	public void testReadPrimitives() {
		PrimitiveModel model;
		try {
			JSONObject obj = new JSONObject("{str:\"Hello\",b:true,s:123,c:\"Hello\",by:22,i:4.6786,l:0xFF,f:-29,d:234.96}");
			model=JSONParser.parse(obj, PrimitiveModel.class);
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
			return;
		}
		
		assertEquals(model.str, "Hello");
		assertEquals(model.b, true);
		assertEquals(model.s,(short)123);
		assertEquals(model.i,4);
		assertEquals(model.l,255L);
		assertEquals(model.f,(float)-29);
		assertEquals(model.d,(double)234.96);
		assertEquals(model.c,'H');
		assertEquals(model.by,(byte)22);
	}
	public void testReadObjects() {
		String jsonTestStr="{test:\"ok\"}";
		String s="{obj:{name:\"Abe\", age=99},json:"+jsonTestStr+"}";

		try {
			JSONObject obj = new JSONObject(s);
			JSONObject testJson=new JSONObject(jsonTestStr);
			ObjectModel model=JSONParser.parse(obj, ObjectModel.class);
			
			assertEquals(model.json.toString(),testJson.toString());
			assertEquals(model.obj,testObjA);
			
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	public void testReadArrays() {
		String s="{arr2d:[['x','Asd'],['Grob','Flog']],arr3d:[[[3,2,1],[4,5],[6]],[[1,6,4]]],"+
				"carr:['z','x'],iarr:[1,2,5],farr:[1,2,3.25],sarr:[\"Hello\",\"World\"]," +
				"objArr:[{name:\"Abe\", age=99},{name:\"Bart\", age=10}]}";

		try {
			JSONObject obj = new JSONObject(s);
			ArrayModel model=JSONParser.parse(obj, ArrayModel.class);
			
			assertEquals(model.arr2d[1][1],"Flog");
			assertEquals(model.arr3d[1][0][2],4);
			assertEquals(model.arr3d[0][2][0],6);
			assertEquals(model.iarr.length,3);
			assertEquals(model.iarr[2], 5);
			assertEquals(model.farr[2], 3.25f);
			assertEquals(model.carr[1],'x');
			assertEquals(model.sarr[1],"World");
			assertEquals(model.objArr.length, 2);
			assertEquals(model.objArr[0], testObjA);
			assertEquals(model.objArr[1], testObjB);
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	public void testReadLists() {
		String s="{clist:[\"z\",\"x\"],ilist:[1,2,5],flist:[1,2,3.25],slist:[\"Hello\",\"World\"],"+
				"objColl:[{name:\"Abe\", age=99},{name:\"Bart\", age=10}],"+
				"objListBasic:[{name:\"Abe\", age=99},{name:\"Bart\", age=10}],"+
				"objList:[{name:\"Abe\", age=99},{name:\"Bart\", age=10}]"+
				"}";
		try {
			JSONObject obj = new JSONObject(s);
			ListModel model=JSONParser.parse(obj, ListModel.class);
			
			assertEquals(model.ilist.size(), 3);
			assertEquals(model.ilist.get(2), Integer.valueOf(5));
			assertEquals(model.flist.get(1), Float.valueOf(2));
			assertEquals(model.clist.get(1),Character.valueOf('x'));
			assertEquals(model.slist.get(1),"World");
			assertEquals(model.objColl.size(), 2);
			assertEquals(model.objColl.iterator().next(), testObjA);
			assertEquals(model.objListBasic.size(), 2);
			assertEquals(model.objListBasic.get(1), testObjB);
			assertEquals(model.objList.size(), 2);
			assertEquals(model.objList.get(1), testObjB);
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	public void testWritePrimitives() {
		PrimitiveModel model=new PrimitiveModel();
		model.b=true;
		model.by=0x34;
		model.c='$';
		model.d=-1234.567;
		model.f=43.21f;
		model.i=-1;
		model.l=9999999999999L;
		model.s=-135;
		model.str="Hello";
		
		try {
			JSONObject json=JSONParser.toJSON(model);
			PrimitiveModel model2=JSONParser.parse(json, PrimitiveModel.class);
			assertEquals(model,model2);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	public void testWriteObjects() {
		ObjectModel model=new ObjectModel();
		model.json=new JSONObject();
		try {
			model.json.put("test", "Hello");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		model.obj=testObjA;
		
		JSONObject json=JSONParser.toJSON(model);
		try {
			assertEquals(json.getJSONObject("json"),model.json);
			assertEquals(json.getJSONObject("obj").getString("name"), testObjA.name);
			assertEquals(json.getJSONObject("obj").getInt("age"), testObjA.age);
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	char[] testCharArray={'z','x'};
	float[] testFloatArray={1,2,3.25f};
	int[] testIntArray={1,2,5};
	String[] testStrArray={"Hello","World"};
	ModelB[] testObjArray={testObjA,testObjB};
	
	public void testWriteArrays() {
		ArrayModel model=new ArrayModel();
		String[][] test2dArray={{"x","Asd"},{"Grob","Flog"}};
		int[][][] test3dArray={{{3,2,1},{4,5},{6}},{{1,6,4}}};
		
		model.arr2d=test2dArray;
		model.arr3d=test3dArray;
		model.carr=testCharArray;
		model.farr=testFloatArray;
		model.iarr=testIntArray;
		model.sarr=testStrArray;
		model.objArr=testObjArray;
		
		try {
			JSONObject json=JSONParser.toJSON(model);
			ArrayModel model2=JSONParser.parse(json, ArrayModel.class);
			assertEquals(model,model2);
			//Log.d("jsontest", "arraymodel : "+json.toString());
			/*
			assertTrue(Arrays.deepEquals(model.arr2d, model2.arr2d));
			assertEquals(model.arr3d, model2.arr3d);
			assertEquals(model.carr, model2.carr);
			assertEquals(model.farr, model2.farr);
			assertEquals(model.iarr, model2.iarr);
			assertEquals(model.sarr, model2.sarr);
			assertEquals(model.objArr, model2.objArr);
			*/
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	public void testWriteLists() {
		ListModel model=new ListModel();
		model.clist=new ArrayList<Character>();
		model.flist=new ArrayList<Float>();
		model.ilist=new ArrayList<Integer>();
		for (int i=0;i<testCharArray.length;i++) model.clist.add(Character.valueOf(testCharArray[i]));
		for (int i=0;i<testFloatArray.length;i++) model.flist.add(Float.valueOf(testFloatArray[i]));
		for (int i=0;i<testIntArray.length;i++) model.ilist.add(Integer.valueOf(testIntArray[i]));
		model.slist=new ArrayList<String>(Arrays.asList(testStrArray));
		model.objList=new ArrayList<ModelB>(Arrays.asList(testObjArray));
		model.objListBasic=Arrays.asList(testObjArray);
		model.objColl=Arrays.asList(testObjArray);
		
		try {
			JSONObject json=JSONParser.toJSON(model);
			ListModel model2=JSONParser.parse(json, ListModel.class);
			assertEquals(model,model2);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
