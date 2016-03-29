package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.util.Log;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.CalendarActivity;
import com.sporthenon.android.adapter.CalendarListAdapter;
import com.sporthenon.android.data.CalendarItem;

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

public class AsyncCalendar extends AsyncTask<Object, Boolean, String> {

    private CalendarActivity activity;
    private ArrayList<CalendarItem> dates;
    private String path;

    public AsyncCalendar(String path) {
        this.path = path;
    }

    @Override
    protected String doInBackground(Object... params) {
        activity = (CalendarActivity) params[0];
        String dt1 = (String) params[1];
        String dt2 = (String) params[2];
        dates = new ArrayList<CalendarItem>();
        try {
            StringBuffer url = new StringBuffer(activity.getString(R.string.url) + "/android/CL/CL");
            url.append("-" + dt1 + "-" + dt2);
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
                    String sport = e.getAttribute("sport");
                    String event = e.getAttribute("event");
                    String dates_ = e.getAttribute("dates");
                    dates_ = dates_.replaceAll("\\-", " - ");
                    event = event.replaceAll("\\|", " - ");
                    dates.add(new CalendarItem(id, sport, event, dates_));
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
            activity.getItemList().addAll(dates);
            activity.getList().setAdapter(new CalendarListAdapter(activity.getApplicationContext(), dates));
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