package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.util.Log;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.USLeaguesRequestActivity;
import com.sporthenon.android.adapter.RecordListAdapter;
import com.sporthenon.android.data.RecordItem;

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

public class AsyncUSRecords extends AsyncTask<Object, Boolean, String> {

    private USLeaguesRequestActivity activity;
    private ArrayList<RecordItem> records;
    private String path;

    public AsyncUSRecords(String path) {
        this.path = path;
    }

    @Override
    protected String doInBackground(Object... params) {
        activity = (USLeaguesRequestActivity) params[0];
        int lgid = (int) params[1];
        int evid = (int) params[2];
        records = new ArrayList<>();
        try {
            String url = activity.getString(R.string.url) + "/android/US/records-" + lgid + "-0-" + evid + "-it-a";
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
                    RecordItem ritem = new RecordItem(e.getAttribute("label"), e.getAttribute("type1"), e.getAttribute("type2"), e.getAttribute("record"));
                    ritem.setRank1(e.getAttribute("rank1"));
                    ritem.setRank2(e.getAttribute("rank2"));
                    ritem.setRank3(e.getAttribute("rank3"));
                    ritem.setRank4(e.getAttribute("rank4"));
                    ritem.setRank5(e.getAttribute("rank5"));
                    ritem.setDate1(e.getAttribute("date1"));
                    ritem.setDate2(e.getAttribute("date2"));
                    ritem.setDate3(e.getAttribute("date3"));
                    ritem.setDate4(e.getAttribute("date4"));
                    ritem.setDate5(e.getAttribute("date5"));
                    records.add(ritem);
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
            activity.getItemList().addAll(records);
            activity.getList().setAdapter(new RecordListAdapter(activity.getApplicationContext(), records));
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