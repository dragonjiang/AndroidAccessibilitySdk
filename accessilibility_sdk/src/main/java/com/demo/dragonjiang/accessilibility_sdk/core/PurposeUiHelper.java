package com.demo.dragonjiang.accessilibility_sdk.core;

import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.core.filter.ClassNameFilter;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.ContentFilter;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.Filter;

/**
 * @author DragonJiang
 * @Date 2016/8/20
 * @Time 12:44
 * @description
 */
public class PurposeUiHelper {


    public static boolean match(Filter[] filters, final AccessibilityNodeInfo node){
        if(node == null || filters == null || filters.length == 0){
            return false;
        }

        for (Filter filter : filters) {
            if(!filter.match(node)){
                return false;
            }
        }

        return true;
    }

    public static Filter[] buildContentFilters(@NonNull String ... args){
        if(args.length == 0){
            return null;
        }

        Filter[] filters = new Filter[args.length];

        for (int i = 0; i < args.length; i++) {
            filters[i] = new ContentFilter(args[i]);
        }

        return filters;
    }


    public static Filter[] buildClassNameFilters(@NonNull String ... args){
        if(args.length == 0){
            return null;
        }

        Filter[] filters = new Filter[args.length];

        for (int i = 0; i < args.length; i++) {
            filters[i] = new ClassNameFilter(args[i]);
        }

        return filters;
    }
}
