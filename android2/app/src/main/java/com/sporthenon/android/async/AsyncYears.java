package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.YearActivity;
import com.sporthenon.android.adapter.ItemListAdapter;
import com.sporthenon.android.data.DataItem;

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

public class AsyncYears extends AsyncTask<Object, Boolean, String> {

    private YearActivity activity;
    private ArrayList<DataItem> years;

    @Override
     protected String doInBackground(Object... params) {
        activity = (YearActivity) params[0];
        Integer lgid = (Integer) params[1];
        years = new ArrayList<DataItem>();
        try {
            String url = (lgid != null && lgid > 0 ? activity.getString(R.string.url) + "/android/US/YR-" + lgid + "-hof" : activity.getString(R.string.url) + "/android/CL/YR");
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
                    years.add(new DataItem(id, name, null));
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
            activity.getItemList().addAll(years);
            activity.getList().setAdapter(new ItemListAdapter(activity.getApplicationContext(), years));
            activity.getPath().setVisibility(View.GONE);
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