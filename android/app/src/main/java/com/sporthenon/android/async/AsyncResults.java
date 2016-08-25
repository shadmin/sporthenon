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
    private String winrec;
    private String path;

    public AsyncResults(String path) {
        this.path = path;
    }

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
            StringBuffer url = new StringBuffer(activity.getString(R.string.url) + "/android/RS/RS");
            url.append("-" + spid + "-" + cpid);
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
            winrec = doc.getDocumentElement().getAttribute("winrec-name") + " (" + doc.getDocumentElement().getAttribute("winrec-count") + ")";
            NodeList list = doc.getElementsByTagName("item");
            for (int i = 0 ; i < list.getLength() ; i++) {
                Node n = list.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;
                    Integer id = Integer.parseInt(e.getAttribute("id"));
                    String year = e.getAttribute("year");
                    String type = e.getAttribute("type");
                    String tie1 = e.getAttribute("tie1");
                    String tie2 = e.getAttribute("tie2");
                    String str1 = e.getAttribute("str1"); String str2 = e.getAttribute("str2"); String str3 = e.getAttribute("str3");
                    String str4 = e.getAttribute("str4"); String str5 = e.getAttribute("str5"); String str6 = e.getAttribute("str6");
                    String str7 = e.getAttribute("str7"); String str8 = e.getAttribute("str8"); String str9 = e.getAttribute("str9");
                    String[] tCode = e.getAttribute("code").split("\\|");
                    String[] tImg = e.getAttribute("img").split("\\|");
                    boolean isTie = (tie1.equals("1") || tie2.equals("1") || type.equals("4") || type.equals("5"));
                    ResultItem ri = new ResultItem(id, year, tImg[0], AndroidUtils.getImage(activity, tImg[0]), str2 + (str1 != null && !str1.matches("^[A-Z]{3}$") ? " " + str1 : "") + (tCode.length <= 0 && AndroidUtils.notEmpty(tCode[0]) ? " (" + tCode[0] + ")" : ""));
                    if (isTie) {
                        if (AndroidUtils.notEmpty(str5)) {
                            if (tImg != null && tImg.length > 1) {
                                ri.setImgURL2(tImg[1]);
                                ri.setImg2(AndroidUtils.getImage(activity, tImg[1]));
                            }
                            ri.setTxt2(str5 + (str4 != null && !str4.matches("^[A-Z]{3}$") ? " " + str4 : "") + (tCode.length > 1 && AndroidUtils.notEmpty(tCode[1]) ? " (" + tCode[1] + ")" : ""));
                        }
                        if (AndroidUtils.notEmpty(str8) && !type.equals("4")) {
                            if (tImg != null && tImg.length > 2) {
                                ri.setImgURL3(tImg[2]);
                                ri.setImg3(AndroidUtils.getImage(activity, tImg[2]));
                            }
                            ri.setTxt3(str8 + (str7 != null && !str7.matches("^[A-Z]{3}$") ? " " + str7 : "") + (tCode.length > 2 && AndroidUtils.notEmpty(tCode[2]) ? " (" + tCode[2] + ")" : ""));
                        }
                    }
                    results.add(ri);
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
            activity.getItemList().addAll(results);
            activity.getList().setAdapter(new ResultListAdapter(activity.getApplicationContext(), results));
            activity.setPath(path + "\r\n" + activity.getResources().getString(R.string.winrec) + " : " + winrec);
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