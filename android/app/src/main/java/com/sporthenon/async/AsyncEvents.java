package com.sporthenon.async;

import android.os.AsyncTask;
import android.util.Log;

import com.sporthenon.activity.EventActivity;
import com.sporthenon.adapter.ItemListAdapter;
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
        activity = (EventActivity) params[0];
        Integer spid = (Integer) params[1];
        Integer cpid = (Integer) params[2];
        Integer ev1id = (params.length > 3 ? (Integer) params[3] : 0);
        Integer ev2id = (params.length > 4 ? (Integer) params[4] : 0);
        events = new ArrayList<IDataItem>();
        try {
            StringBuffer url = new StringBuffer("http://test.sporthenon.com/android/");
            url.append(ev2id > 0 ? "SE2" : (ev1id > 0 ? "SE" : "EV"));
            url.append("/" + spid + "-" + cpid);
            url.append(ev1id > 0 ? "-" + ev1id : "");
            url.append(ev2id > 0 ? "-" + ev2id : "");
            url.append("?lang=fr");
            HttpURLConnection connection = (HttpURLConnection)new URL(url.toString()).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(input);
            NodeList list = doc.getElementsByTagName("item");
            if (list == null || list.getLength() == 0) {
                activity.loadResults(spid, cpid, ev1id, ev2id, null);
                return null;
            }
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
            activity.getList().setAdapter(new ItemListAdapter(activity.getApplicationContext(), events));
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