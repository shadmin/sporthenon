package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.util.Log;

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

public class AsyncStatsLeaders extends AsyncTask<Object, Boolean, String> {

    private USLeaguesRequestActivity activity;
    private ArrayList<ResultItem> stats;

    @Override
    protected String doInBackground(Object... params) {
        activity = (USLeaguesRequestActivity) params[0];
        int lgid = (int) params[1];
        int evid = (int) params[2];
        stats = new ArrayList<>();
        try {
            String url = activity.getString(R.string.url) + "/android/US/stats-" + lgid + "-0-" + evid + "-1-1";
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
                    ResultItem ritem = new ResultItem(0, e.getAttribute("year"), null, null, e.getAttribute("rank1") + " - " + e.getAttribute("result1"));
                    ritem.setTxt2(e.getAttribute("rank2") + " - " + e.getAttribute("result2"));
                    ritem.setTxt3(e.getAttribute("rank3") + " - " + e.getAttribute("result3"));
                    stats.add(ritem);
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
            activity.getItemList().addAll(stats);
            activity.getList().setAdapter(new ResultListAdapter(activity.getApplicationContext(), stats));
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