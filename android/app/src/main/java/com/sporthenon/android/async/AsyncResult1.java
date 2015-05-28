package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.Result1Activity;
import com.sporthenon.android.activity.ResultActivity;
import com.sporthenon.android.data.Result1Item;
import com.sporthenon.android.utils.AndroidUtils;

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
            Element sport = (Element) doc.getElementsByTagName("sport").item(0);
            result1.setSport(sport.getTextContent());
            result1.setSportImg(AndroidUtils.getImage(activity, sport.getAttribute("img")));
            Element championship = (Element) doc.getElementsByTagName("championship").item(0);
            result1.setChampionship(championship.getTextContent());
            result1.setChampionshipImg(AndroidUtils.getImage(activity, championship.getAttribute("img")));
            Element event = (Element) doc.getElementsByTagName("event").item(0);
            result1.setEvent(event.getTextContent());
            result1.setEventImg(AndroidUtils.getImage(activity, event.getAttribute("img")));
            Element subevent = (Element) doc.getElementsByTagName("subevent").item(0);
            if (subevent != null) {
                result1.setSubevent(subevent.getTextContent());
                result1.setSubeventImg(AndroidUtils.getImage(activity, subevent.getAttribute("img")));
            }
            Element subevent2 = (Element) doc.getElementsByTagName("subevent2").item(0);
            if (subevent2 != null) {
                result1.setSubevent2(subevent2.getTextContent());
                result1.setSubevent2Img(AndroidUtils.getImage(activity, subevent2.getAttribute("img")));
            }
            Element date = (Element) doc.getElementsByTagName("dates").item(0);
            result1.setDate((date.getAttribute("date1") != null && date.getAttribute("date1").length() > 0 ? date.getAttribute("date1") + " - " : "") + date.getAttribute("date2"));
            Element place1 = (Element) doc.getElementsByTagName("place1").item(0);
            if (place1 != null) {
                result1.setPlace1(place1.getTextContent());
                result1.setPlace1Img(AndroidUtils.getImage(activity, place1.getAttribute("img")));
            }
            Element place2 = (Element) doc.getElementsByTagName("place2").item(0);
            if (place2 != null) {
                result1.setPlace2(place2.getTextContent());
                result1.setPlace2Img(AndroidUtils.getImage(activity, place2.getAttribute("img")));
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
            activity.getYear().setText(result1.getYear());
            activity.getSport().setText(result1.getSport());
            activity.getSport().setCompoundDrawablesWithIntrinsicBounds(result1.getSportImg(), null, null, null);
            activity.getChampionship().setText(result1.getChampionship());
            activity.getChampionship().setCompoundDrawablesWithIntrinsicBounds(result1.getChampionshipImg(), null, null, null);
            activity.getEvent().setText(result1.getEvent());
            activity.getEvent().setCompoundDrawablesWithIntrinsicBounds(result1.getEventImg(), null, null, null);
            if (result1.getSubevent() != null) {
                activity.getSubevent().setText(result1.getSubevent());
                activity.getSubevent().setCompoundDrawablesWithIntrinsicBounds(result1.getSubeventImg(), null, null, null);
            }
            if (result1.getSubevent2() != null) {
                activity.getSubevent2().setText(result1.getSubevent2());
                activity.getSubevent2().setCompoundDrawablesWithIntrinsicBounds(result1.getSubevent2Img(), null, null, null);
            }
            activity.getDate().setText(result1.getDate());
            if (result1.getPlace1() != null) {
                activity.getPlace1().setText(result1.getPlace1());
                activity.getPlace1().setCompoundDrawablesWithIntrinsicBounds(result1.getPlace1Img(), null, null, null);
            }
            if (result1.getPlace2() != null) {
                activity.getPlace2().setText(result1.getPlace2());
                activity.getPlace2().setCompoundDrawablesWithIntrinsicBounds(result1.getPlace2Img(), null, null, null);
            }
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