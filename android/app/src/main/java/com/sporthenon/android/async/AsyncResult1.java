package com.sporthenon.android.async;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
            StringBuffer url = new StringBuffer(activity.getString(R.string.url) + "/android/R1/");
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
                result1.setDate((date.getAttribute("date1") != null && date.getAttribute("date1").length() > 0 ? date.getAttribute("date1") + Html.fromHtml(" &ndash; ") : "") + date.getAttribute("date2"));
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
            String rs1 = null;
            String rs2 = null;
            String rs3 = null;
            Element rank1 = (Element) doc.getElementsByTagName("rank1").item(0);
            if (rank1 != null) {
                result1.setRank1(rank1.getTextContent().replaceAll("\\s\\(", "\\\r\\\n("));
                result1.setRank1ImgURL(rank1.getAttribute("img"));
                result1.setRank1Img(AndroidUtils.getImage(activity, result1.getRank1ImgURL()));
                rs1 = rank1.getAttribute("result");
            }
            Element rank2 = (Element) doc.getElementsByTagName("rank2").item(0);
            if (rank2 != null) {
                result1.setRank2(rank2.getTextContent().replaceAll("\\s\\(", "\\\r\\\n("));
                result1.setRank2ImgURL(rank2.getAttribute("img"));
                result1.setRank2Img(AndroidUtils.getImage(activity, result1.getRank2ImgURL()));
                rs2 = rank2.getAttribute("result");
            }
            Element rank3 = (Element) doc.getElementsByTagName("rank3").item(0);
            if (rank3 != null) {
                result1.setRank3(rank3.getTextContent().replaceAll("\\s\\(", "\\\r\\\n("));
                result1.setRank3ImgURL(rank3.getAttribute("img"));
                result1.setRank3Img(AndroidUtils.getImage(activity, result1.getRank3ImgURL()));
                rs3 = rank3.getAttribute("result");
            }
            if (AndroidUtils.notEmpty(result1.getRank1()) && AndroidUtils.notEmpty(result1.getRank2()) && AndroidUtils.notEmpty(rs1) && !AndroidUtils.notEmpty(rs2) && !AndroidUtils.notEmpty(rs3))
                result1.setScore(rs1);
            else {
                if (AndroidUtils.notEmpty(rs1))
                    result1.setRank1(result1.getRank1() + "\r\n" + rs1);
                if (AndroidUtils.notEmpty(rs2))
                    result1.setRank2(result1.getRank2() + "\r\n" + rs2);
                if (AndroidUtils.notEmpty(rs3))
                    result1.setRank3(result1.getRank3() + "\r\n" + rs3);
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
            else
                activity.getDate().setVisibility(View.GONE);
            if (AndroidUtils.notEmpty(result1.getPlace1()))
                activity.getPlace1().setText(result1.getPlace1());
            else
                activity.getPlace1().setVisibility(View.GONE);
            if (AndroidUtils.notEmpty(result1.getPlace2()))
                activity.getPlace2().setText(result1.getPlace2());
            else
                activity.getPlace2().setVisibility(View.GONE);
            // Rankings
            if (AndroidUtils.notEmpty(result1.getRank1())) {
                activity.getRank1Text().setText(result1.getRank1().replaceAll("\\|", " / "));
                if (result1.getRank1Img() != null) {
                    activity.getRank1Img().setImageDrawable(result1.getRank1Img());
                    activity.getRank1Img().setLayoutParams(AndroidUtils.getImageSize(activity.getApplicationContext(), result1.getRank1ImgURL()));
                }
            }
            if (AndroidUtils.notEmpty(result1.getRank2())) {
                activity.getRank2Text().setText(result1.getRank2().replaceAll("\\|", " / "));
                if (result1.getRank2Img() != null) {
                    activity.getRank2Img().setImageDrawable(result1.getRank2Img());
                    activity.getRank2Img().setLayoutParams(AndroidUtils.getImageSize(activity.getApplicationContext(), result1.getRank2ImgURL()));
                }
            }
            if (AndroidUtils.notEmpty(result1.getRank3())) {
                activity.getRank3Text().setText(result1.getRank3().replaceAll("\\|", " / "));
                if (result1.getRank3Img() != null) {
                    activity.getRank3Img().setImageDrawable(result1.getRank3Img());
                    activity.getRank3Img().setLayoutParams(AndroidUtils.getImageSize(activity.getApplicationContext(), result1.getRank3ImgURL()));
                }
            }
            if (AndroidUtils.notEmpty(result1.getScore()))
                activity.getScore().setText(result1.getScore());
            else
                activity.getScore().setVisibility(View.GONE);
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