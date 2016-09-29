package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.util.Log;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.OlympicsActivity;
import com.sporthenon.android.adapter.ItemListAdapter;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.utils.AndroidUtils;

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

public class AsyncOlympics extends AsyncTask<Object, Boolean, String> {

    private OlympicsActivity activity;
    private ArrayList<DataItem> olympics;
    private String path;

    public AsyncOlympics(String path) {
        this.path = path;
    }

    @Override
     protected String doInBackground(Object... params) {
        activity = (OlympicsActivity) params[0];
        Integer oltype = (Integer) params[1];
        olympics = new ArrayList<DataItem>();
        try {
            String url = activity.getString(R.string.url) + "/android/OL/OL-" + oltype + "?lang=" + activity.getLang();
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
                    olympics.add(new DataItem(id, name, AndroidUtils.getImage(activity, img)));
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
            activity.getItemList().clear();
            activity.getItemList().addAll(olympics);
            activity.getList().setAdapter(new ItemListAdapter(activity.getApplicationContext(), olympics));
            activity.setPath(path);
        }
        catch(Exception e) {
            Log.e("Error", e.getMessage(), e);
        }
        finally {
            activity.hideProgress();
        }
        super.onPostExecute(response);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}