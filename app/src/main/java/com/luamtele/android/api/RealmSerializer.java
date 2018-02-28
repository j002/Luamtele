package com.luamtele.android.api;

/**
 * Created by HP on 20/10/2015.
 */

import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import io.realm.RealmObject;

public class RealmSerializer implements JsonSerializer<RealmObject> {

 @Override
 public JsonElement serialize(RealmObject object, Type typeOfSrc, JsonSerializationContext context) {
  final JsonObject jsonObject = new JsonObject();
  Field[] attributes = object.getClass().getDeclaredFields();
  for (Field field : attributes) {
   field.setAccessible(true);
   try {
    jsonObject.addProperty( field.getName(), field.get(object).toString());
   } catch (IllegalAccessException e) {
    e.printStackTrace();
   }

  }
  return jsonObject;
 }
}
