package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.util.Log;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.ResultActivity;
import com.sporthenon.android.adapter.ResultListAdapter;
import com.sporthenon.android.data.ResultItem;
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

public class AsyncResults extends AsyncTask<Object, Boolean, String> {

    private ResultActivity activity;
    private ArrayList<ResultItem> results;

    @Override
    protected String doInBackground(Object... params) {
        activity = (ResultActivity) params[0];
        Integer spid = (Integer) params[1];
        Integer cpid = (Integer) params[2];
        Integer ev1id = (params.length > 3 ? (Integer) params[3] : 0);
        Integer ev2id = (params.length > 4 ? (Integer) params[4] : 0);
        Integer ev3id = (params.length > 5 ? (Integer) params[5] : 0);
        results = new ArrayList<ResultItem>();
        try {
            StringBuffer url = new StringBuffer(activity.getString(R.string.url) + "/android/RS");
            url.append("/" + spid + "-" + cpid);
            url.append(ev1id > 0 ? "-" + ev1id : "");
            url.append(ev2id > 0 ? "-" + ev2id : "");
            url.append(ev3id > 0 ? "-" + ev3id : "");
            url.append("?lang=" + activity.getLang());
            HttpURLConnection connection = (HttpURLConnection)new URL(url.toString()).openConnection();
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
                    Integer id = Integer.parseInt(e.getAttribute("id"));
                    String year = e.getAttribute("year");
                    String code = e.getAttribute("code");
                    String str1 = e.getAttribute("str1");
                    String str2 = e.getAttribute("str2");
                    results.add(new ResultItem(id, year, AndroidUtils.getImage(activity, "CN", e.getAttribute("img"), id), str2 + " " + str1 + " (" + code + ")"));
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
            activity.getResults().addAll(results);
            activity.getList().setAdapter(new ResultListAdapter(activity.getApplicationContext(), results));
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