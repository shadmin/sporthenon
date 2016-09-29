package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.util.Log;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.OlympicsMedalsActivity;
import com.sporthenon.android.adapter.MedalListAdapter;
import com.sporthenon.android.data.MedalItem;

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

public class AsyncMedals extends AsyncTask<Object, Boolean, String> {

    private OlympicsMedalsActivity activity;
    private ArrayList<MedalItem> medals;
    private String path;

    public AsyncMedals(String path) {
        this.path = path;
    }

    @Override
     protected String doInBackground(Object... params) {
        activity = (OlympicsMedalsActivity) params[0];
        Integer olid = (Integer) params[1];
        medals = new ArrayList<MedalItem>();
        try {
            String url = activity.getString(R.string.url) + "/android/OL/OR-0-" + olid + "?lang=" + activity.getLang();
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
                    //Integer id = Integer.parseInt(e.getAttribute("value"));
                    String country = e.getAttribute("country");
                    String gold = e.getAttribute("gold");
                    String silver = e.getAttribute("silver");
                    String bronze = e.getAttribute("bronze");
                    medals.add(new MedalItem(country, Integer.parseInt(gold), Integer.parseInt(silver), Integer.parseInt(bronze)));
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
            activity.getItemList().addAll(medals);
            activity.getList().setAdapter(new MedalListAdapter(activity.getApplicationContext(), medals));
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