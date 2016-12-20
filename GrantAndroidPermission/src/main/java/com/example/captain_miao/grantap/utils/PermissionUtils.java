package com.example.captain_miao.grantap.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.webkit.PermissionRequest;

import com.example.captain_miao.grantap.annotation.PermissionCheck;
import com.example.captain_miao.grantap.annotation.PermissionDenied;
import com.example.captain_miao.grantap.annotation.PermissionGranted;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Copyright 2015 Seunghwan Kim
 * <p/>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * <p/>
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * <p/>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * modify from https://github.com/lovedise/PermissionGen
 */


final public class PermissionUtils {
    private PermissionUtils() {
    }

    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Returns true if <code>Activity</code> or <code>Fragment</code> has access to all given permissions.
     *
     * @param context     context
     * @param permissions permissions
     * @return returns true if <code>Activity</code> or <code>Fragment</code> has access to all given permissions.
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static boolean hasSelfPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (isOverMarshmallow() && permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                if (!Settings.canDrawOverlays(context)) {
                    return false;
                }
            } else if (isOverMarshmallow() && permission.equals(Manifest.permission.WRITE_SETTINGS)) {
                if (!Settings.System.canWrite(context)) {
                    return false;
                }
            } else if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }



    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissions(Activity activity, String... permission) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission) {
            // CommonsWare's blog post:https://commonsware.com/blog/2015/08/17/random-musings-android-6p0-sdk.html
            //support SYSTEM_ALERT_WINDOW,WRITE_SETTINGS
            if (isOverMarshmallow() && value.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)){
                if(!Settings.canDrawOverlays(activity)){
                    denyPermissions.add(value);
                }
            } else if(isOverMarshmallow() && value.equals(Manifest.permission.WRITE_SETTINGS)){
                if(!Settings.System.canWrite(activity)){
                    denyPermissions.add(value);
                }
            }
            //else if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
            //    denyPermissions.add(value);
            //}
            else if(PermissionChecker.checkSelfPermission(activity, value) != PackageManager.PERMISSION_GRANTED){
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    public static List<Method> findAnnotationMethods(Class clazz, Class<? extends Annotation> clazz1) {
        List<Method> methods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(clazz1)) {
                methods.add(method);
            }
        }
        return methods;
    }

    public static <A extends Annotation> Method findMethodPermissionDeniedWithRequestCode(Class clazz,
                                                                                          Class<A> permissionFailClass, int requestCode) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(permissionFailClass)) {
                if (requestCode == method.getAnnotation(PermissionDenied.class).requestCode()) {
                    return method;
                }
            }
        }
        return null;
    }

    public static boolean isEqualRequestCodeFromAnnotation(Method m, Class clazz, int requestCode) {
        if (clazz.equals(PermissionDenied.class)) {
            return requestCode == m.getAnnotation(PermissionDenied.class).requestCode();
        } else if (clazz.equals(PermissionGranted.class)) {
            return requestCode == m.getAnnotation(PermissionGranted.class).requestCode();
        }  else if (clazz.equals(PermissionRequest.class)) {
            return requestCode == m.getAnnotation(PermissionGranted.class).requestCode();
        } else {
            return false;
        }
    }
    public static boolean isEqualRequestCodeFromAnnotation(Field m, Class clazz, int requestCode) {
        if (clazz.equals(PermissionDenied.class)) {
            return requestCode == m.getAnnotation(PermissionDenied.class).requestCode();
        } else if (clazz.equals(PermissionGranted.class)) {
            return requestCode == m.getAnnotation(PermissionGranted.class).requestCode();
        }  else if (clazz.equals(PermissionCheck.class)) {
            return requestCode == m.getAnnotation(PermissionCheck.class).requestCode();
        } else {
            return false;
        }
    }

    public static <A extends Annotation> Method findMethodWithRequestCode(Class clazz,
                                                                          Class<A> annotation, int requestCode) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                if (isEqualRequestCodeFromAnnotation(method, annotation, requestCode)) {
                    return method;
                }
            }
        }
        return null;
    }

    public static  <A extends Annotation> String[] findPermissionsWithRequestCode(Object object, Class clazz, Class<A> annotation, int requestCode) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field :fields) {
            if (field.isAnnotationPresent(annotation)) {
                if (isEqualRequestCodeFromAnnotation(field, annotation, requestCode)) {
                    field.setAccessible(true);
                    try {
                        return (String[]) field.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static <A extends Annotation> Method findMethodPermissionGrantedWithRequestCode(Class clazz,
                                                                                           Class<A> permissionFailClass, int requestCode) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(permissionFailClass)) {
                if (requestCode == method.getAnnotation(PermissionGranted.class).requestCode()) {
                    return method;
                }
            }
        }
        return null;
    }

    public static Activity getActivity(Object object) {
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            return (Activity) object;
        }
        return null;
    }
}
