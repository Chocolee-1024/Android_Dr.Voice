package com.imac.dr.voice_app.core;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class FragmentLauncher {

    public static void change(Context context, int containerId, Bundle args, Class<? extends Fragment> fragmentClass) {
        change(context, containerId, args, fragmentClass.getName());
    }

    public static void change(Context context, int containerId, Bundle args, String fragmentClassName) {
        //呼叫dealWithFragment並存入"fragment"
        Fragment fragment = dealWithFragment(fragmentClassName);
        //判斷 fragment 有沒有東西
        if (fragment == null) {
            return;
        }
        //傳送Bundle(這裡為null)
        fragment.setArguments(args);
        //設定activity為context
        Activity activity = (Activity) context;
        //設定FragmentManager
        FragmentManager manager = activity.getFragmentManager();
        //呼叫FragmentManager的beginTransaction()方法
        FragmentTransaction transaction = manager.beginTransaction();
        //transaction設置為添加Fragment的動畫(關閉則是TRANSIT_FRAGMENT_CLOSE)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //使用replace方法(1)要使用的fragment容器的R.Id。(2)要放入的fragment。(3)fragment的Class名稱
        transaction.replace(containerId, fragment, fragment.getClass().getName());
        //加入
        transaction.commit();
    }
    public static void changeToBack(Context context, int containerId, Bundle args, String fragmentClassName) {
        Fragment fragment = dealWithFragment(fragmentClassName);
        if (fragment == null) {
            return;
        }
        fragment.setArguments(args);
        Activity activity = (Activity) context;

        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(containerId, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private static Fragment dealWithFragment(String fragmentClassName) {
        Fragment fragment = null;
        //使用try...catch判斷fragment = generatorFragment(fragmentClassName)是否有異常，如果有打印錯誤訊息
        try {
            //呼叫generatorFragment並存入"fragment"
            fragment = generatorFragment(fragmentClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    private static Fragment generatorFragment(String fragmentClassname)
            /**個個錯誤訊息的原因:
             * 1.ClassNotFoundException : 你所找尋的Class部存在。
             * 2.NoSuchMethodException :  (1)、缺少某些jar文件。(2)、某些jar文件有重復。
             * 3.IllegalAccessException :
             * 4.InvocationTargetException : 主要發生在使用反射代碼去調用方法，或者構造器本身出现異常時。
             * 5.InstantiationException : 由於在實體類中未添加無參構造函數引起的。
             */
        //做{}裡的是有可能會出"throws"裡的問題，並給呼叫這個方法的地方，{}所發生的錯誤訊息。
    throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        //找尋是否有"fragmentClassname"這一個class名稱
        Class<?> clazz = Class.forName(fragmentClassname);
        //抓取clazz所需要的建構值
        Constructor<?> constructor = clazz.getConstructor();
        //傳回Fragment的class(最後面的"()"是放入建構值，但這一個class沒有建構值，所以是空的)
        return (Fragment) constructor.newInstance(new Object[]{});
    }
}