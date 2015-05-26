package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.Result1Activity;
import com.sporthenon.android.activity.ResultActivity;
import com.sporthenon.android.data.Result1Item;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class AsyncResult1 extends AsyncTask<Object, Boolean, String> {

    private Result1Activity activity;
    private Result1Item result1;

    @Override
    protected String doInBackground(Object... params) {
        activity = (Result1Activity) params[0];
        Integer rsid = (Integer) params[1];
        String rsyr = (String) params[2];
        result1 = new Result1Item(rsid, rsyr);
        try {
            StringBuffer url = new StringBuffer(activity.getString(R.string.url) + "/android/R1");
            url.append("/" + rsid + "?lang=" + activity.getLang());
            HttpURLConnection connection = (HttpURLConnection)new URL(url.toString()).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(input);
            Node sport = doc.getElementsByTagName("sport").item(0);
            result1.setSport("<img src='http://img.sporthenon.com/0-1-L_2.png'/>" + sport.getTextContent());
            Node championship = doc.getElementsByTagName("championship").item(0);
            result1.setChampionship("<img src='http://img.sporthenon.com/0-1-L_2.png'/>" + championship.getTextContent());
           /* for (int i = 0 ; i < list.getLength() ; i++) {
                Node n = list.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;
                    Integer id = Integer.parseInt(e.getAttribute("id"));
                    String year = e.getAttribute("year");
                    //String rk1 = e.getAttribute("rk1");
                    String code = e.getAttribute("code");
                    String str1 = e.getAttribute("str1");
                    String str2 = e.getAttribute("str2");
                    results.add(new ResultItem(id, year, AndroidUtils.getImage(activity, "CN", e.getAttribute("img"), id), str2 + " " + str1 + " (" + code + ")"));
                }
            }*/
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
            activity.getSport().setText(Html.fromHtml(result1.getSport()));
            activity.getChampionship().setText(Html.fromHtml(result1.getChampionship()));
            activity.getYear().setText(result1.getYear());
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