package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

public class Jsonutil {

    public static String toJSon(User user) {
        try {

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("이름", user.get이름());
            jsonObj.put("ID", user.getID());
            jsonObj.put("PW", user.getPW());
            jsonObj.put("전화번호", user.get전화번호());
            jsonObj.put("이메일", user.get이메일());
            jsonObj.put("처음사귄날", user.get시작날짜());
            jsonObj.put("이미지배열", user.get이미지배열());

            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;

    }
}

