package com.sporthenon.async;

import android.os.AsyncTask;
import android.util.Log;

import com.sporthenon.activity.SportActivity;
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

public class AsyncSports extends AsyncTask<Object, Boolean, String> {

    SportActivity activity;
    private ArrayList<IDataItem> sports;

    @Override
    protected String doInBackground(Object... params) {
        activity = (SportActivity) params[0];
        sports = new ArrayList<IDataItem>();
        try {
            String url = "http://test.sporthenon.com/android/SP/0?lang=fr";
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
                    sports.add(new DataItem(id, name.toUpperCase(), AndroidUtils.getImage(activity, "SP", img, id)));
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
            activity.getSports().addAll(sports);
            activity.getList().setAdapter(new ItemListAdapter(activity.getApplicationContext(), sports));
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