package com.sporthenon.async;

import android.os.AsyncTask;
import android.util.Log;

import com.sporthenon.activity.ResultActivity;
import com.sporthenon.adapter.ResultListAdapter;
import com.sporthenon.data.IResultItem;
import com.sporthenon.data.ResultItem;
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

public class AsyncResults extends AsyncTask<Object, Boolean, String> {

    ResultActivity activity;
    private ArrayList<IResultItem> results;

    @Override
    protected String doInBackground(Object... params) {
        Integer spid = (Integer) params[0];
        activity = (ResultActivity) params[1];
        Integer cpid = (Integer) params[2];
        Integer ev1id = (params.length > 3 ? (Integer) params[3] : 0);
        Integer ev2id = (params.length > 4 ? (Integer) params[4] : 0);
        results = new ArrayList<IResultItem>();
        try {
            String url = "http://www.sporthenon.com/android/RS/" + spid + "-" + cpid + (ev1id > 0 ? "-" + ev1id : "") + (ev2id > 0 ? "-" + ev2id : "") + "?lang=fr";
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
                    Integer id = Integer.parseInt(e.getAttribute("id"));
                    String year = e.getAttribute("year");
                    String img = e.getAttribute("img").replaceAll("\\'\\/\\>", "");
                    img = "http://www.sporthenon.com:81/" + img.substring(img.lastIndexOf("/") + 1);
                    //String rk1 = e.getAttribute("rk1");
                    String code = e.getAttribute("code");
                    String str1 = e.getAttribute("str1");
                    String str2 = e.getAttribute("str2");
                    results.add(new ResultItem(id, year, AndroidUtils.getImage(activity, "CN", img, id), str2 + " " + str1 + " (" + code + ")"));
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