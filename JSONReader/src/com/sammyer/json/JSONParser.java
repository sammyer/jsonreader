/**
sammyer

note : does not yet support wrapped classes
*/
package com.sammyer.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	private static final boolean DEBUG = true;
	private static final String TAG = "JSONParser";

	public static <T> T parse(JSONObject obj, Class<T> clazz) {
		return parseClass(obj, clazz);
	}

	private static <T> T parseClass(JSONObject json, Class<T> clazz) {
		if (DEBUG) Log.i(TAG, "parseClass "+clazz.getName()); 
		T obj;
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
			logException(e);
			return null;
		}
		for (Field field:clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(JSONProperty.class)) {
				JSONProperty annotation=field.getAnnotation(JSONProperty.class);
				String jsonKey=annotation.value();
				//default to variable name
				if (!isValidJsonKey(jsonKey)) jsonKey=field.getName();
				parseField(json, obj, field, jsonKey);
			}
		}
		return obj;
	}
	
	private static void parseField(JSONObject json, Object obj, Field field, String jsonKey) {
		ClassData fieldData=new ClassData(field);
		if (fieldData.clazz==null) return;
		try {
			if (fieldData.type==FieldType.PRIMITIVE) parsePrimitiveField(json, obj, field,jsonKey);
			else if (fieldData.type==FieldType.CLASS) parseClassField(json, obj, field,jsonKey);
			else if (fieldData.type==FieldType.LIST) parseListField(json, obj, field,jsonKey);
			else if (fieldData.type==FieldType.ARRAY) parseArrayField(json, obj, field,jsonKey);
			else if (fieldData.type==FieldType.JSON) parseJsonField(json, obj, field,jsonKey);
		} catch (JSONException e) {
			logException(e);
		} catch (Exception e) {
			logException(e);
		}
	}
	
	private static void logException(Exception e) {
		if (DEBUG) {
			Log.e(TAG,"jpt errx - "+e.toString());
			e.printStackTrace();
		}
	}
	private static void logMissingName(String name) {
		if (DEBUG) {
			Log.w(TAG,"field missing in json - "+name);
		}
	}
	
	private static void parsePrimitiveField(JSONObject json, Object obj, Field field, String name)
			throws IllegalArgumentException, IllegalAccessException, JSONException {
		if (!json.has(name)) {
			logMissingName(name);
			return;
		}
		Class<?> clazz=field.getType();
		if (DEBUG) Log.i(TAG, "parsePrimitiveField "+name);
		
		field.setAccessible(true);
		if (clazz.equals(int.class)) {
			field.set(obj, json.getInt(name));	
		} else if (clazz.equals(long.class)) {
			field.set(obj, json.getLong(name));	
		} else if (clazz.equals(float.class)) {
			field.set(obj, (float)json.getDouble(name));	
		} else if (clazz.equals(double.class)) {
			field.set(obj, json.getDouble(name));	
		} else if (clazz.equals(boolean.class)) {
			field.set(obj, json.getBoolean(name));	
		} else if (clazz.equals(short.class)) {
			field.set(obj, (short)json.getInt(name));	
		} else if (clazz.equals(char.class)) {
			String s=json.getString(name);
			if (s!=null&s.length()>0) field.set(obj, s.charAt(0));	
		} else if (clazz.equals(byte.class)) {
			field.set(obj, (byte)json.getInt(name));
		} else if (clazz.equals(String.class)) {
			field.set(obj, json.getString(name));
		}
		
		else {
			field.set(obj, parseBoxedPrimitive(json, name, clazz));
		} 
		
	}
	
	private static void parseClassField(JSONObject json, Object obj, Field field, String name) 
			throws JSONException, IllegalArgumentException, IllegalAccessException {
		if (DEBUG) Log.i(TAG, "parseClassField "+name);
		if (!json.has(name)) {
			logMissingName(name);
			return;
		}
		
		JSONObject childJson;
		childJson=json.getJSONObject(name);
		field.setAccessible(true);
		field.set(obj, parseClass(childJson,field.getType()));
	}

	private static void parseJsonField(JSONObject json, Object obj, Field field, String name) 
			throws JSONException, IllegalArgumentException, IllegalAccessException {
		if (DEBUG) Log.i(TAG, "parseJsonField "+name);
		if (!json.has(name)) {
			logMissingName(name);
			return;
		}
		
		JSONObject childJson;
		childJson=json.getJSONObject(name);
		field.setAccessible(true);
		field.set(obj, childJson);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void parseListField(JSONObject json, Object obj, Field field, String name)
		throws IllegalArgumentException, IllegalAccessException, JSONException {
		ClassData cdata=new ClassData(field).subclass;
		if (DEBUG) Log.i(TAG, "parseListField "+name+" / "+new ClassData(field));
		if (!json.has(name)) {
			logMissingName(name);
			return;
		}
		
		JSONArray jsonarr;
		jsonarr=json.getJSONArray(name);
		int arrlen=jsonarr.length();
		ArrayList list=new ArrayList();
		
		boolean isBoxedPrimitive=cdata.type==FieldType.PRIMITIVE;
		for (int i=0;i<arrlen;i++) {
			if (isBoxedPrimitive) {
				list.add(parseBoxedPrimitive(jsonarr, i, cdata.clazz));
			}
			else {
				JSONObject jsonobj;
				jsonobj = (JSONObject)jsonarr.get(i);
				if (jsonobj==null) continue;
				list.add(parseClass(jsonobj,cdata.clazz));
			}
		}
		field.setAccessible(true);
		field.set(obj, list);
	}
	
	
	private static Object parseBoxedPrimitive(JSONArray jsonarr, int i, Class<?> clazz) throws JSONException {
		if (clazz.equals(String.class)) {
			return jsonarr.getString(i);
		} if (clazz.equals(Integer.class)) {
			return Integer.valueOf(jsonarr.getInt(i));
		} else if (clazz.equals(Float.class)) {
			return Float.valueOf((float)jsonarr.getDouble(i));	
		} else if (clazz.equals(Double.class)) {
			return Double.valueOf(jsonarr.getDouble(i));
		} else if (clazz.equals(Long.class)) {
			return Long.valueOf(jsonarr.getLong(i));	
		} else if (clazz.equals(Short.class)) {
			return Short.valueOf((short)jsonarr.getInt(i));	
		} else if (clazz.equals(Byte.class)) {
			return Byte.valueOf((byte)jsonarr.getInt(i));	
		} else if (clazz.equals(Character.class)) {
			String s=jsonarr.getString(i);
			if (s!=null&s.length()>0) {
				return Character.valueOf(s.charAt(0));	
			}
		} else if (clazz.equals(Boolean.class)) {
			return Boolean.valueOf(jsonarr.getBoolean(i));	
		}		
		throw new JSONException("parseBoxedPrimitive :: type not handled");
	}
	
	private static Object parseBoxedPrimitive(JSONObject json, String fieldName, Class<?> clazz) throws JSONException {
		if (clazz.equals(String.class)) {
			return json.get(fieldName);
		} else if (clazz.equals(Integer.class)) {
			return Integer.valueOf(json.getInt(fieldName));	
		} else if (clazz.equals(Long.class)) {
			return Long.valueOf(json.getLong(fieldName));	
		} else if (clazz.equals(Float.class)) {
			return Float.valueOf((float)json.getDouble(fieldName));	
		} else if (clazz.equals(Double.class)) {
			return Double.valueOf(json.getDouble(fieldName));	
		} else if (clazz.equals(Boolean.class)) {
			return Boolean.valueOf(json.getBoolean(fieldName));	
		} else if (clazz.equals(Short.class)) {
			return Short.valueOf((short)json.getInt(fieldName));	
		} else if (clazz.equals(Character.class)) {
			String s=json.getString(fieldName);
			if (s!=null&s.length()>0) return Character.valueOf(s.charAt(0));	
		} else if (clazz.equals(Byte.class)) {
			return Byte.valueOf((byte)json.getInt(fieldName));
		}
		throw new JSONException("parseBoxedPrimitive :: type not handled");
	}

	
	private static void parseArrayField(JSONObject json, Object obj, Field field, String name) 
		throws IllegalArgumentException, IllegalAccessException, JSONException {
		ClassData cdata=new ClassData(field);
		if (DEBUG) Log.i(TAG, "parseArrayField "+name+" cl="+cdata);
		if (!json.has(name)) {
			logMissingName(name);
			return;
		}
		
		JSONArray jsonarr;
		jsonarr=json.getJSONArray(name);
		field.setAccessible(true);
		
		int arrlen=jsonarr.length();
		Object arr=Array.newInstance(cdata.subclass.clazz, arrlen);
		for (int i=0;i<arrlen;i++) {
			parseArrayFieldAux(arr, jsonarr, i, cdata.subclass);
		}
		
		
		field.set(obj, arr);
	}
	
	private static void parseArrayFieldAux(Object arr, JSONArray jsonarr, int i, ClassData cdata)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException, JSONException {
		
			Class<?> clazz=cdata.clazz;
			
			if (clazz.equals(int.class)) {
				Array.setInt(arr, i, jsonarr.getInt(i));
			} else if (clazz.equals(float.class)) {
				Array.setFloat(arr, i, (float)jsonarr.getDouble(i));	
			} else if (clazz.equals(double.class)) {
				Array.setDouble(arr, i, jsonarr.getDouble(i));	
			} else if (clazz.equals(long.class)) {
				Array.setLong(arr, i, jsonarr.getLong(i));	
			} else if (clazz.equals(short.class)) {
				Array.setShort(arr, i, (short)jsonarr.getInt(i));	
			} else if (clazz.equals(byte.class)) {
				Array.setByte(arr, i, (byte)jsonarr.getInt(i));	
			} else if (clazz.equals(char.class)) {
				String s=jsonarr.getString(i);
				if (s!=null&s.length()>0) Array.setChar(arr, i, s.charAt(0));	
			} else if (clazz.equals(boolean.class)) {
				Array.setBoolean(arr, i, jsonarr.getBoolean(i));	
			} else if (clazz.equals(String.class)) {
				Array.set(arr, i, jsonarr.getString(i));
			}
			
			else if (cdata.type==FieldType.ARRAY) {
				//multidimensional arrays
				JSONArray subJsonArr=jsonarr.getJSONArray(i);
				Object subarr=Array.newInstance(cdata.subclass.clazz, subJsonArr.length());
				for (int j=0;j<subJsonArr.length();j++) {
					parseArrayFieldAux(subarr, subJsonArr, j, cdata.subclass);
				}
				Array.set(arr, i, subarr);
			} else if (cdata.type==FieldType.PRIMITIVE) {
				Array.set(arr, i, parseBoxedPrimitive(jsonarr, i, clazz));
			} else {
				JSONObject obj=(JSONObject)jsonarr.get(i);
				if (obj!=null) Array.set(arr, i, parseClass(obj,clazz));
			}
	}
	
//--------------------------------------------------------------------------------------------
	
	public static JSONObject toJSON(Object obj) {
		return classToJSON(obj);
	}
	
	private static JSONObject classToJSON(Object obj) {
		JSONObject json=new JSONObject();
		Class<?> clazz=obj.getClass();
		for (Field field:clazz.getDeclaredFields()) {
			if (!field.isAnnotationPresent(JSONProperty.class)) continue;
			field.setAccessible(true);
			
			ClassData ctype=new ClassData(field);
			JSONProperty annotation=field.getAnnotation(JSONProperty.class);
			String name=annotation.value();
			if (!isValidJsonKey(name)) name=field.getName();
			
			if (ctype.clazz==null) continue;
			try {
				if (ctype.type!=FieldType.PRIMITIVE&&field.get(obj)==null) continue;
				
				if (ctype.type==FieldType.PRIMITIVE) {
					if (ctype.clazz.equals(int.class)) json.put(name, field.getInt(obj));
					else if (ctype.clazz.equals(short.class)) json.put(name, field.getShort(obj));
					else if (ctype.clazz.equals(byte.class)) json.put(name, field.getByte(obj));
					else if (ctype.clazz.equals(long.class)) json.put(name, field.getLong(obj));
					else if (ctype.clazz.equals(float.class)) json.put(name, field.getFloat(obj));
					else if (ctype.clazz.equals(double.class)) json.put(name, field.getDouble(obj));
					else if (ctype.clazz.equals(boolean.class)) json.put(name, field.getBoolean(obj));
					else if (ctype.clazz.equals(char.class)) json.put(name, Character.toString(field.getChar(obj)));
					else if (field.get(obj)!=null) json.put(name, field.get(obj));
				}
				else if (ctype.type==FieldType.CLASS) json.put(name, classToJSON(field.get(obj)));
				else if (ctype.type==FieldType.LIST) json.put(name, listToJSON((List<?>)field.get(obj),ctype.subclass));
				else if (ctype.type==FieldType.ARRAY) json.put(name, arrayToJSON(field.get(obj),ctype.subclass));
				else if (ctype.type==FieldType.JSON) json.put(name, field.get(obj));
			} catch (JSONException e) {
				logException(e);
			} catch (Exception e) {
				logException(e);
			}
		}
		return json;
	}
	
	private static <T> JSONArray listToJSON(List<T> list,ClassData ctype) throws JSONException {
		JSONArray arr=new JSONArray();
		for (T item:list) {
			if (ctype.type==FieldType.CLASS) arr.put(classToJSON(item));
			else if (ctype.type==FieldType.LIST) arr.put(listToJSON((List<?>)item,ctype.subclass));
			else arr.put(item);
		}
		return arr;
	}
	
	private static JSONArray arrayToJSON(Object itemArr,ClassData ctype) throws JSONException {
		JSONArray arr=new JSONArray();
		for (int i=0;i<Array.getLength(itemArr);i++) {
			if (ctype.clazz.equals(byte.class)) arr.put(Array.getByte(itemArr, i));
			else if (ctype.clazz.equals(short.class)) arr.put(Array.getShort(itemArr, i));
			else if (ctype.clazz.equals(int.class)) arr.put(Array.getInt(itemArr, i));
			else if (ctype.clazz.equals(long.class)) arr.put(Array.getLong(itemArr, i));
			else if (ctype.clazz.equals(float.class)) arr.put(Array.getFloat(itemArr, i));
			else if (ctype.clazz.equals(double.class)) arr.put(Array.getDouble(itemArr, i));
			else if (ctype.clazz.equals(char.class)) arr.put(Character.toString(Array.getChar(itemArr, i)));
			else if (ctype.clazz.equals(boolean.class)) arr.put(Array.getBoolean(itemArr, i));
			else if (ctype.type==FieldType.CLASS) arr.put(classToJSON(Array.get(itemArr, i)));
			else if (ctype.type==FieldType.LIST) arr.put(listToJSON((List<?>)Array.get(itemArr, i),ctype.subclass));
			else if (ctype.type==FieldType.ARRAY) arr.put(arrayToJSON(Array.get(itemArr, i),ctype.subclass));
			else arr.put(Array.get(itemArr, i));
		}
		return arr;
	}
	
//--------------------------------------------------------------------------------------------
	
	private static boolean isValidJsonKey(String key) {
		if (key==null||key=="") return false;
		return true;
	}
	
	private enum FieldType {
		PRIMITIVE,LIST,ARRAY,CLASS,JSON
	}
	
	private static class ClassData {
		public FieldType type;
		public Class<?> clazz;
		public ClassData subclass;
		
		private static final Class<?>[] primitiveClasses={
			Boolean.class,Byte.class,Short.class,Integer.class,Long.class,
			Float.class,Double.class,Character.class,String.class};
		
		public ClassData(Field field) {
			//Log.i(TAG, "ClassData field type="+field.getType()+" generic="+field.getGenericType());
			init(field.getType(),field.getGenericType());
		}
		
		public ClassData(Class<?> clazz) {
			init(clazz,clazz.getGenericSuperclass());
		}
		
		public ClassData(Type classType) {
			init((Class<?>)classType,classType);
		}
		
		private void init(Class<?> clazz, Type genericType) {
			this.clazz=clazz;
			if (isPrimitive(clazz)) {
				type=FieldType.PRIMITIVE;
			}
			else if (clazz.isArray()) {
				type=FieldType.ARRAY;
				subclass=new ClassData(clazz.getComponentType());
			} else if (clazz.isAssignableFrom(ArrayList.class)) {
				type=FieldType.LIST;
				try {
					ParameterizedType ptype=(ParameterizedType)genericType;
					subclass=new ClassData(ptype.getActualTypeArguments()[0]);
				} catch (Exception e) {
					Log.e(TAG, "Could not get list generic type");
					subclass=null;
				}
			}
			else if (clazz.isAssignableFrom(JSONObject.class)) {
				type=FieldType.JSON;
			}
			else type=FieldType.CLASS;
		}
		
		private static boolean isPrimitive(Class<?> clazz) {
			if (clazz.isPrimitive()) return true;
			for (Class<?> pclazz:primitiveClasses) {
				if (clazz==pclazz) return true;
			}
			return false;
		}
		
		public String toString() {
			return "[Class "+clazz.getName()+" ("+type.toString()+") "+(subclass==null?"":"sub="+subclass)+"]";
		}
	}


}
