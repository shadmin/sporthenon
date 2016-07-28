package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.util.Log;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.USLeaguesRequestActivity;
import com.sporthenon.android.adapter.ScoreListAdapter;
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

public class AsyncUSChampionships extends AsyncTask<Object, Boolean, String> {

    private USLeaguesRequestActivity activity;
    private ArrayList<ResultItem> results;

    @Override
    protected String doInBackground(Object... params) {
        activity = (USLeaguesRequestActivity) params[0];
        int lgid = (int) params[1];
        results = new ArrayList<ResultItem>();
        String url = activity.getString(R.string.url) + "/android/US/championships-" + lgid + "-0";
        try {
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
                    String rank1 = e.getAttribute("rank1");
                    String rank2 = e.getAttribute("rank2");
                    ResultItem rsitem = new ResultItem(0, e.getAttribute("year"), null, null, rank1);
                    rsitem.setTxt2(rank2);
                    rsitem.setResult1(e.getAttribute("result"));
                    results.add(rsitem);
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
            activity.getItemList().addAll(results);
            activity.getList().setAdapter(new ScoreListAdapter(activity.getApplicationContext(), results));
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