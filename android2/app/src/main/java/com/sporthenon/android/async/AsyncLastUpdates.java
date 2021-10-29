package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.LastUpdatesActivity;
import com.sporthenon.android.activity.SportActivity;
import com.sporthenon.android.adapter.ItemListAdapter;
import com.sporthenon.android.adapter.LastUpdateListAdapter;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.data.LastUpdateItem;
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

public class AsyncLastUpdates extends AsyncTask<Object, Boolean, String> {

    private LastUpdatesActivity activity;
    private ArrayList<LastUpdateItem> updates;
    private String path;

    public AsyncLastUpdates(String path) {
        this.path = path;
    }

    @Override
     protected String doInBackground(Object... params) {
        activity = (LastUpdatesActivity) params[0];
        updates = new ArrayList<>();
        try {
            String url = activity.getString(R.string.url) + "/android/LU/100?lang=" + activity.getLang();
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
                    Integer idResult = Integer.parseInt(e.getAttribute("rs_id"));
                    String year = e.getAttribute("year");
                    String sport = e.getAttribute("sport");
                    String event = e.getAttribute("event");
                    LastUpdateItem item = new LastUpdateItem(idResult, year, sport, event);
                    item.setImgURLSport(e.getAttribute("sport-img"));
                    item.setImgSport(AndroidUtils.getImage(activity, item.getImgURLSport()));
                    if (AndroidUtils.notEmpty(e.getAttribute("pos1_id"))) {
                        item.setPos1(e.getAttribute("pos1"));
                        item.setIdPos1(Integer.parseInt(e.getAttribute("pos1_id")));
                        item.setImgURLPos1(e.getAttribute("pos1_img"));
                        item.setImgPos1(AndroidUtils.getImage(activity, item.getImgURLPos1()));
                    }
                    if (AndroidUtils.notEmpty(e.getAttribute("pos2_id"))) {
                        item.setPos2(e.getAttribute("pos2"));
                        item.setIdPos2(Integer.parseInt(e.getAttribute("pos2_id")));
                        item.setImgURLPos2(e.getAttribute("pos2_img"));
                        item.setImgPos2(AndroidUtils.getImage(activity, item.getImgURLPos2()));
                    }
                    if (AndroidUtils.notEmpty(e.getAttribute("pos3_id"))) {
                        item.setPos3(e.getAttribute("pos3"));
                        item.setIdPos3(Integer.parseInt(e.getAttribute("pos3_id")));
                        item.setImgURLPos3(e.getAttribute("pos3_img"));
                        item.setImgPos3(AndroidUtils.getImage(activity, item.getImgURLPos3()));
                    }
                    if (AndroidUtils.notEmpty(e.getAttribute("pos4_id"))) {
                        item.setPos4(e.getAttribute("pos4"));
                        item.setIdPos4(Integer.parseInt(e.getAttribute("pos4_id")));
                        item.setImgURLPos4(e.getAttribute("pos4_img"));
                        item.setImgPos4(AndroidUtils.getImage(activity, item.getImgURLPos4()));
                    }
                    item.setScore(e.getAttribute("score"));
                    item.setDate(e.getAttribute("date"));
                    updates.add(item);
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
            activity.getItemList().addAll(updates);
            activity.getList().setAdapter(new LastUpdateListAdapter(activity.getApplicationContext(), updates));
            activity.getPath().setVisibility(View.GONE);
            if (path != null) {
                activity.setPath(path);
            }
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