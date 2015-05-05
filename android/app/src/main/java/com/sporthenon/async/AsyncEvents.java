package com.sporthenon.async;

import android.os.AsyncTask;
import android.util.Log;

import com.sporthenon.activity.EventActivity;
import com.sporthenon.adapter.ListAdapter;
import com.sporthenon.data.DataItem;
import com.sporthenon.data.IDataItem;
import com.sporthenon.utils.AndroidUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class AsyncEvents extends AsyncTask<Object, Boolean, String> {

    EventActivity activity;
    private ArrayList<IDataItem> events;

    @Override
    protected String doInBackground(Object... params) {
        Integer spid = (Integer) params[0];
        activity = (EventActivity) params[1];
        Integer cpid = (Integer) params[2];
        Integer ev1id = (params.length > 3 ? (Integer) params[3] : 0);
        events = new ArrayList<IDataItem>();
        try {
            String url = "http://www.sporthenon.com/android/" + (ev1id > 0 ? "SE" : "EV") + "/" + spid + "-" + cpid + (ev1id > 0 ? "-" + ev1id : "") + "?lang=fr";
      Log.e("url", "url-" + url);
            HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(input);
            NodeList list = doc.getElementsByTagName("item");
            for (int i = 0 ; i < list.getLength() ; i++) {
                Node n = list.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;
                    Integer id = Integer.parseInt(e.getAttribute("value"));
                    String name = e.getAttribute("text");
                    String img = e.getAttribute("img");
                    events.add(new DataItem(id, name, AndroidUtils.getImage(activity, "EV", img, id)));
                }
            }
            connection.disconnect();
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            activity.getEvents().addAll(events);
            activity.getList().setAdapter(new ListAdapter(activity.getApplicationContext(), events));
        }
        catch(Exception e) {
            Log.e("Error", e.getMessage(), e);
        }
        super.onPostExecute(response);
    }

    @Override
    protected void onPreExecute() {
       super.onPreExecute();
    }

}