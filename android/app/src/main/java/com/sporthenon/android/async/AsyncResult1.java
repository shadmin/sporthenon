package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.Result1Activity;
import com.sporthenon.android.adapter.RankListAdapter;
import com.sporthenon.android.data.RankItem;
import com.sporthenon.android.data.Result1Item;
import com.sporthenon.android.utils.AndroidUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class AsyncResult1 extends AsyncTask<Object, Boolean, String> {

    private Result1Activity activity;
    private Result1Item result1;
    private ArrayList<RankItem> ranks;

    @Override
    protected String doInBackground(Object... params) {
        activity = (Result1Activity) params[0];
        Integer rsid = (Integer) params[1];
        String rsyr = (String) params[2];
        result1 = new Result1Item(rsid, rsyr);
        ranks = new ArrayList<RankItem>();
        try {
            StringBuffer url = new StringBuffer(activity.getString(R.string.url) + "/android/RS/R1-");
            url.append(rsid).append("?lang=").append(activity.getLang());
            HttpURLConnection connection = (HttpURLConnection)new URL(url.toString()).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(input);
            // Result Info
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
            if (date != null)
                result1.setDate((date.getAttribute("date1") != null && date.getAttribute("date1").length() > 0 ? date.getAttribute("date1") + Html.fromHtml("&nbsp;&ndash;&nbsp;") : "") + date.getAttribute("date2"));
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
            // Rankings
            RankItem item = null;
            Element rank1 = (Element) doc.getElementsByTagName("rank1").item(0);
            Element rank2 = (Element) doc.getElementsByTagName("rank2").item(0);
            Element rank3 = (Element) doc.getElementsByTagName("rank3").item(0);
            // TODO gerer isScore
            //boolean isScore = (rank1 != null && rank2 != null)
            if (rank1 != null) {
                String[] t1 = rank1.getTextContent().replaceAll("\\s\\(", "\\\r\\\n(").split("\\|");
                String[] t2 = rank1.getAttribute("img").split("\\|");
                for (int i = 0 ; i < t1.length ; i++) {
                    item = new RankItem("1.");
                    item.setText(t1[i]);
                    item.setImgURL(t2[i]);
                    item.setImg(AndroidUtils.getImage(activity, item.getImgURL()));
                    item.setResult(rank1.getAttribute("result"));
                    ranks.add(item);
                }
            }
            if (rank2 != null) {
                String[] t1 = rank2.getTextContent().replaceAll("\\s\\(", "\\\r\\\n(").split("\\|");
                String[] t2 = rank2.getAttribute("img").split("\\|");
                for (int i = 0 ; i < t1.length ; i++) {
                    item = new RankItem("2.");
                    item.setText(t1[i]);
                    item.setImgURL(t2[i]);
                    item.setImg(AndroidUtils.getImage(activity, item.getImgURL()));
                    item.setResult(rank2.getAttribute("result"));
                    ranks.add(item);
                }
            }
            if (rank3 != null) {
                String[] t1 = rank3.getTextContent().replaceAll("\\s\\(", "\\\r\\\n(").split("\\|");
                String[] t2 = rank3.getAttribute("img").split("\\|");
                for (int i = 0 ; i < t1.length ; i++) {
                    item = new RankItem("3.");
                    item.setText(t1[i]);
                    item.setImgURL(t2[i]);
                    item.setImg(AndroidUtils.getImage(activity, item.getImgURL()));
                    item.setResult(rank3.getAttribute("result"));
                    ranks.add(item);
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
            // Result Info
            activity.getYear().setText(result1.getYear());
            activity.getSport().setText(result1.getSport().toUpperCase());
            activity.getChampionship().setText(result1.getChampionship());
            activity.getEvent().setText(result1.getEvent());
            if (AndroidUtils.notEmpty(result1.getSubevent()))
                activity.getSubevent().setText(result1.getSubevent());
            else
                activity.getSubevent().setVisibility(View.GONE);
            if (AndroidUtils.notEmpty(result1.getSubevent2()))
                activity.getSubevent2().setText(result1.getSubevent2());
            else
                activity.getSubevent2().setVisibility(View.GONE);
            if (AndroidUtils.notEmpty(result1.getDate()))
                activity.getDate().setText(result1.getDate().replaceAll("\\&nbsp\\;", " "));
            else {
                activity.getLabelDate().setVisibility(View.GONE);
                activity.getDate().setVisibility(View.GONE);
            }
            if (AndroidUtils.notEmpty(result1.getPlace1()))
                activity.getPlace1().setText(result1.getPlace1());
            else
                activity.getPlace1().setVisibility(View.GONE);
            if (AndroidUtils.notEmpty(result1.getPlace2()))
                activity.getPlace2().setText(result1.getPlace2());
            else
                activity.getPlace2().setVisibility(View.GONE);
            if (!AndroidUtils.notEmpty(result1.getPlace1()) && !AndroidUtils.notEmpty(result1.getPlace2()))
                activity.getLabelPlace().setVisibility(View.GONE);
            // Result Rankings
            activity.getRankList().setAdapter(new RankListAdapter(activity.getApplicationContext(), ranks));
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