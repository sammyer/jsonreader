/**
sammyer
*/
package com.sammyer.jsonreader.test;

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
}
