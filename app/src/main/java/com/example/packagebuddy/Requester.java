package com.example.packagebuddy;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Requester {
    static Requester instance;
    RequestQueue queue;
    static String url = "http://10.0.2.2:5000/post";


    private Requester(Context c){
        queue = Volley.newRequestQueue(c);
        instance = this;
    }

    public static Requester getInstance(Context c){
        if(instance == null){
            instance = new Requester(c);
        }
        return instance;

    }

    public void makePOST(Map<String,Object> map){
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("trackingID",(String)map.get("trackingID"));
                params.put("last_updated",(String)map.get("last_updated"));
                params.put("lat",(String)map.get("lat"));
                params.put("lon",(String)map.get("lon"));
                params.put("max_temp",(String)map.get("max_temp"));
                params.put("max_gs",(String)map.get("max_gs"));
                params.put("current_temp",(String)map.get("current_temp"));
                return params;
            }

        };

        queue.add(request);

    }
}
