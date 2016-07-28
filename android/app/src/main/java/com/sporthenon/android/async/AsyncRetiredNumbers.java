package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.USLeaguesRequestActivity;
import com.sporthenon.android.adapter.ResultListAdapter;
import com.sporthenon.android.data.ResultItem;

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

public class AsyncRetiredNumbers extends AsyncTask<Object, Boolean, String> {

    private USLeaguesRequestActivity activity;
    private ArrayList<ResultItem> retnums;

    @Override
    protected String doInBackground(Object... params) {
        activity = (USLeaguesRequestActivity) params[0];
        int lgid = (int) params[1];
        int tmid = (int) params[2];
        retnums = new ArrayList<>();
        try {
            String url = activity.getString(R.string.url) + "/android/US/retnum-" + lgid + "-" + tmid;
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
                    String name = e.getAttribute("name");
                    String number = e.getAttribute("number");
                    retnums.add(new ResultItem(0, number, null, null, name));
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
            activity.getItemList().addAll(retnums);
            activity.getList().setAdapter(new ResultListAdapter(activity.getApplicationContext(), retnums));
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