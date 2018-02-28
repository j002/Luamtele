package com.luamtele.android.utils;

import com.google.gson.JsonObject;

import java.lang.reflect.Field;

/**
 * Created by HP on 22/10/2015.
 */
public class Util {
    public static String toGSON(Object object){
        if(object==null) return "{object: null}";
        final JsonObject jsonObject = new JsonObject();
        Field[] attributes = object.getClass().getDeclaredFields();

        for (Field field : attributes) {
            if(field==null) return "{field: null}";
            field.setAccessible(true);

            try {

                if(field.get(object)==null) return "{object: null}";
                jsonObject.addProperty(field.getName() , field.get(object).toString());

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return jsonObject.toString();


    }

}
