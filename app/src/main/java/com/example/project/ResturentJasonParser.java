// StudentJasonParser.java
package com.example.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResturentJasonParser {
    public static List<String> getObjectFromJson(String json) {
        List<String> types = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("types");
            for (int i = 0; i < jsonArray.length(); i++) {
                types.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return types;
    }
}

