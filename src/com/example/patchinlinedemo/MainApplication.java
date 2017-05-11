package com.example.patchinlinedemo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

public class MainApplication extends Application {
	
	public static final String SHARED_PREFERENCE_USE_PATCH = "sp_use_patch";
	public static final String KEY_USE_PATCH = "key_use_patch";
	
	public static native int clearDexCache(long address, int numResolvedMethods, long resolvedTypes, int numResolvedTypes);

	@Override
    protected void attachBaseContext(Context base) {
    	super.attachBaseContext(base);
    	Log.d("PatchInlineDemo", "MainApplication attachBaseContext");
    	
    	String dexName = null;
    	boolean result = false;
    	
    	SharedPreferences sp = base.getSharedPreferences(SHARED_PREFERENCE_USE_PATCH, Context.MODE_PRIVATE);
    	boolean willUsePatch = sp.getBoolean(KEY_USE_PATCH, false);
    	if (willUsePatch) {
    		dexName = "patch.jar";
    		result = InjectUtil.inject(this, dexName, "", false);
    		Log.d("PatchInlineDemo", "MainApplication inject patch dex result=" + result);
    	} else {
    		Log.d("PatchInlineDemo", "MainApplication will not use patch");
    	}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("PatchInlineDemo", "MainApplication onCreate");
		if (android.os.Build.VERSION.SDK_INT >= 24) {
    		//clearAppDexCache();
    		//showClassLoaderInfo(true);
		}
	}

	public void showClassLoaderInfo(boolean clear) {
		Class<?> loaderClass = getClassLoader().getClass().getSuperclass().getSuperclass();
		Log.d("PatchInlineDemo", "classloader name=" + loaderClass.getName());
		try {
			Field classTableField = loaderClass.getDeclaredField("classTable");
			classTableField.setAccessible(true);
			long classTableValue = classTableField.getLong(getClassLoader());
			Field AllocatorField = loaderClass.getDeclaredField("allocator");
			AllocatorField.setAccessible(true);
			long allocatorValue = AllocatorField.getLong(getClassLoader());
			Log.d("PatchInlineDemo", "ClassLoader classTableValue=" + classTableValue + ", allocatorValue=" + allocatorValue);

			if (clear) {
				Log.d("PatchInlineDemo", "clear classTableField and AllocatorField");
				classTableField.set(getClassLoader(), Long.valueOf(0));
				AllocatorField.set(getClassLoader(), Long.valueOf(0));
			}
		} catch (NoSuchFieldException e) {
			Log.d("PatchInlineDemo", "clearClassLoaderInfo NoSuchFieldException=" + e);
		} catch (IllegalAccessException e) {
			Log.d("PatchInlineDemo", "clearClassLoaderInfo IllegalAccessException=" + e);
		}
	}

//	public void showDexCacheInfo() {
//		try {
//			Class<?> appClass = getClass();
//			Log.d("PatchInlineDemo", "showDexCacheInfo appClass=" + appClass);
//
//			Field dexCacheField = appClass.getClass().getDeclaredField(
//					"dexCache");
//			boolean access = dexCacheField.isAccessible();
//			if (!access)
//				dexCacheField.setAccessible(true);
//			Object dexCacheValue = dexCacheField.get(appClass);
//			if (!access)
//				dexCacheField.setAccessible(false);
//			Log.d("PatchInlineDemo", "showDexCacheInfo dexCacheValue="
//					+ dexCacheValue);
//
//			Class DexCacheClass = Class.forName("java.lang.DexCache");
//			int numFields = 0;
//			int numMethods = 0;
//			int numTypes = 0;
//			int numStrings = 0;
//			String dexLocation = null;
//			Field[] fields = DexCacheClass.getDeclaredFields();
//			for (Field field : fields) {
//				String fieldName = field.getName();
//				if ("location".equals(fieldName)
//						|| "numResolvedFields".equals(fieldName)
//						|| "numResolvedMethods".equals(fieldName)
//						|| "numResolvedTypes".equals(fieldName)
//						|| "numStrings".equals(fieldName)) {
//					access = field.isAccessible();
//					if (!access)
//						field.setAccessible(true);
//					Object value = field.get(dexCacheValue);
//					Log.d("PatchInlineDemo", "showDexCacheInfo dex "
//							+ fieldName + "=" + value);
//					if ("location".equals(fieldName)) {
//						dexLocation = (String) value;
//					} else if ("numResolvedFields".equals(fieldName)) {
//						numFields = ((Integer) value).intValue();
//					} else if ("numResolvedMethods".equals(fieldName)) {
//						numMethods = ((Integer) value).intValue();
//					} else if ("numResolvedTypes".equals(fieldName)) {
//						numTypes = ((Integer) value).intValue();
//					} else if ("numStrings".equals(fieldName)) {
//						numStrings = ((Integer) value).intValue();
//					}
//					if (!access)
//						field.setAccessible(false);
//				}
//			}
//
//			ArrayList<Integer> args = null;
//			Method getTypeMethod = DexCacheClass.getDeclaredMethod(
//					"getResolvedType", int.class);
//			access = getTypeMethod.isAccessible();
//			if (!access)
//				getTypeMethod.setAccessible(true);
//			for (int i = 0; i < numTypes; i++) {
//				Object result = getTypeMethod.invoke(dexCacheValue, (Object) i);
//				Log.d("PatchInlineDemo", "showDexCacheInfo invoke list result="
//						+ result + ", typeId=" + i);
//				if (result != null) {
//					String resultName = ((Class<?>) result).getName();
//					if (resultName != null
//							&& (resultName.contains("BugObject") || resultName
//									.contains("MainActivity"))) {
//						if (args == null) {
//							args = new ArrayList<Integer>();
//						}
//						args.add(i);
//						Log.d("PatchInlineDemo",
//								"showDexCacheInfo invoke match result="
//										+ result + ", typeId=" + i);
//					}
//				}
//			}
//			if (!access)
//				getTypeMethod.setAccessible(false);
//
//			if (args != null && args.size() > 0) {
//				Method setTypeMethod = DexCacheClass.getDeclaredMethod(
//						"setResolvedType", int.class, Class.class);
//				access = setTypeMethod.isAccessible();
//				if (!access)
//					setTypeMethod.setAccessible(true);
//				for (int j = 0; j < args.size(); j++) {
//					setTypeMethod.invoke(dexCacheValue, args.get(j), null);
//				}
//				if (!access)
//					setTypeMethod.setAccessible(false);
//
//				access = getTypeMethod.isAccessible();
//				if (!access)
//					getTypeMethod.setAccessible(true);
//				for (int i = 0; i < numTypes; i++) {
//					Object result = getTypeMethod.invoke(dexCacheValue,
//							(Object) i);
//					Log.d("PatchInlineDemo",
//							"showDexCacheInfo second time invoke list result="
//									+ result + ", typeId=" + i);
//					if (result != null
//							&& ((Class<?>) result).getName().contains(
//									"BugObject")) {
//						Log.d("PatchInlineDemo",
//								"showDexCacheInfo second time invoke match result="
//										+ result + ", typeId=" + i);
//						break;
//					}
//				}
//				if (!access)
//					getTypeMethod.setAccessible(false);
//			}
//
//			Log.d("PatchInlineDemo", "showDexCacheInfo invoke end");
//		} catch (Exception e) {
//			Log.d("PatchInlineDemo", "showDexCacheInfo Exception=" + e);
//		}
//	}
		
	public void clearAppDexCache() {
		try {
			Class<?> appClass = getClass();
			Log.d("PatchInlineDemo", "clearDexCache appClass=" + appClass);

			Field dexCacheField = appClass.getClass().getDeclaredField(
					"dexCache");
			boolean access = dexCacheField.isAccessible();
			if (!access)
				dexCacheField.setAccessible(true);
			Object dexCacheAddress = dexCacheField.get(appClass);
			if (!access)
				dexCacheField.setAccessible(false);
			Log.d("PatchInlineDemo", "clearDexCache dexCacheValue="
					+ dexCacheAddress);

			Class<?> DexCacheClass = Class.forName("java.lang.DexCache");
			long addrMethods = 0;
			long addrTypes = 0;
			int numMethods = 0;
			int numTypes = 0;
			Field[] fields = DexCacheClass.getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				if ("resolvedMethods".equals(fieldName)
						|| "resolvedTypes".equals(fieldName)
						|| "numResolvedMethods".equals(fieldName)
						|| "numResolvedTypes".equals(fieldName)) {
					access = field.isAccessible();
					if (!access)
						field.setAccessible(true);
					Object value = field.get(dexCacheAddress);
					Log.d("PatchInlineDemo", "clearDexCache dex " + fieldName
							+ "=" + value);
					if ("resolvedMethods".equals(fieldName)) {
						addrMethods = ((Long) value).longValue();
					} else if ("resolvedTypes".equals(fieldName)) {
						addrTypes = ((Long) value).longValue();
					} else if ("numResolvedMethods".equals(fieldName)) {
						numMethods = ((Integer) value).intValue();
					} else if ("numResolvedTypes".equals(fieldName)) {
						numTypes = ((Integer) value).intValue();
					}
					if (!access)
						field.setAccessible(false);
				}
			}
			boolean isSoLoad = false;
			try {
				System.loadLibrary("qq_art_n");
				isSoLoad = true;
			} catch (Throwable e) {
				Log.d("PatchInlineDemo", "load throwable=" + e);
				isSoLoad = false;
			}
			Log.d("PatchInlineDemo", "clearDexCache loadLibrary result=" + isSoLoad);
			if (isSoLoad) {
				MainApplication.clearDexCache(addrMethods, numMethods,
						addrTypes, numTypes);
			}
		} catch (Exception e) {
			Log.d("PatchInlineDemo", "clearDexCache exception=" + e);
		}
	}

//	private void doDex2Oat() {
//        try {
//			Runtime.getRuntime().exec("dex2oat" +
//					" --dex-file=/data/data/com.example.patchinlinedemo/files/patch.jar" +
//					" --oat-file=/data/app/com.example.patchinlinedemo-1/oat/arm64/base.odex" +
//					" --instruction-set=arm64" +
//					" --instruction-set-variant=cortex-a53" +
//					" --instruction-set-features=default" +
//					" --runtime-arg -Xms64m" +
//					" --runtime-arg -Xmx512m " +
//					" --compiler-filter=speed " +
//					" --app-image-file=/data/app/com.example.patchinlinedemo-1/oat/arm64/patch.art " +
//					" --image-format=lz4");
//		} catch (Throwable e) {
//			Log.d("PatchInlineDemo", "doDex2Oat exception=" + e);
//		}
//	}
}