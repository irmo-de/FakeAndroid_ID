package de.irmo.fakeandroid_id;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.BaseDexClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        // Only modify apps containing "voi" in their package name

        XposedBridge.log("Hooking into package: :)))" + lpparam.packageName);
        XposedBridge.log("Hooking into package: :)))" + lpparam.packageName);

        try {
            XposedHelpers.findAndHookMethod("android.provider.Settings$Secure", lpparam.classLoader, "getString",
                    ContentResolver.class, String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            String key = (String) param.args[1];
                            XposedBridge.log("Intercepted Settings.System.getString call for key: " + key);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Object originalResult = param.getResult();
                            XposedBridge.log("Original result: " + originalResult);

                            if ("specific_key".equals(param.args[1])) {
                                param.setResult("Modified Value");
                            }
                        }
                    });
        } catch (Exception e) {
            XposedBridge.log("Error hooking android.provider.Settings$System: " + e.getMessage());
        }
    }

}