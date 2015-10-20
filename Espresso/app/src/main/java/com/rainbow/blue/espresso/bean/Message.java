package com.rainbow.blue.espresso.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blue on 2015/9/11.
 */
public class Message {

    private static final String image_url = "https://www.baidu.com/img/bdlogo.png";
    private String image;
    private String content;

    public static List<Message> createMessage(JSONArray jsonArray) {
        List<Message> list = new ArrayList<>();
        for (int i = 0; i < (jsonArray == null ? 0 : jsonArray.length()); i++) {
            Message message = new Message();
            try {
                JSONObject jo = jsonArray.getJSONObject(i);
                if (jo.has("image") && !jo.isNull("image")) {
                    message.setImage(jo.getString("image"));
                }
                if (jo.has("content") && !jo.isNull("content")) {
                    message.setContent(jo.getString("content"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.add(message);
        }
        return list;
    }

    public static Message getDefault() {
        Message item = new Message();
        item.setContent("item");
        item.setImage(image_url);
        return item;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
