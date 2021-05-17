package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.util.Log;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.OlympicsPodiumsActivity;
import com.sporthenon.android.adapter.PodiumListAdapter;
import com.sporthenon.android.data.PodiumItem;
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

public class AsyncPodiums extends AsyncTask<Object, Boolean, String> {

    private OlympicsPodiumsActivity activity;
    private ArrayList<PodiumItem> podiums;
    private String path;

    public AsyncPodiums(String path) {
        this.path = path;
    }

    @Override
    protected String doInBackground(Object... params) {
        activity = (OlympicsPodiumsActivity) params[0];
        Integer olid = (Integer) params[1];
        Integer spid = (Integer) params[2];
        podiums = new ArrayList<PodiumItem>();
        try {
            String url = activity.getString(R.string.url) + "/android/OL/RS-0-" + olid + "-" + spid + "?lang=" + activity.getLang();
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
                    String event1 = e.getAttribute("event1"); String event2 = e.getAttribute("event2"); String event3 = e.getAttribute("event3");
                    String rank1 = e.getAttribute("rank1"); String rank2 = e.getAttribute("rank2"); String rank3 = e.getAttribute("rank3");
                    String img1 = e.getAttribute("img1"); String img2 = e.getAttribute("img2"); String img3 = e.getAttribute("img3");
                    String cn1 = e.getAttribute("prcn1"); String cn2 = e.getAttribute("prcn2"); String cn3 = e.getAttribute("prcn3");
                    if (AndroidUtils.notEmpty(cn1))
                        rank1 += " (" + cn1 + ")";
                    if (AndroidUtils.notEmpty(cn2))
                        rank2 += " (" + cn2 + ")";
                    if (AndroidUtils.notEmpty(cn3))
                        rank3 += " (" + cn3 + ")";
                    String venue = e.getAttribute("venue");
                    PodiumItem pi = new PodiumItem(id, event1 + (AndroidUtils.notEmpty(event2) ? ", " + event2 : "") + (AndroidUtils.notEmpty(event3) ? ", " + event3 : ""), img1, AndroidUtils.getImage(activity, img1), rank1);
                    if (AndroidUtils.notEmpty(rank2)) {
                        pi.setTxt2(rank2);
                        pi.setImg2(AndroidUtils.getImage(activity, img2));
                        pi.setImgURL2(img2);
                    }
                    if (AndroidUtils.notEmpty(rank3)) {
                        pi.setTxt3(rank3);
                        pi.setImg3(AndroidUtils.getImage(activity, img3));
                        pi.setImgURL3(img3);
                    }
                    pi.setVenue(venue);
                    podiums.add(pi);
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
            activity.getItemList().addAll(podiums);
            activity.getList().setAdapter(new PodiumListAdapter(activity.getApplicationContext(), podiums));
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