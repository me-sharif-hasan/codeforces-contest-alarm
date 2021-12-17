package com.iishanto.cpalarm.Codeforces;

import android.util.Log;

import com.iishanto.cpalarm.EventRecycler.Adapter;
import com.iishanto.cpalarm.ToolsSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CfApi {
    Adapter cfAdapter;
    public CfApi(Adapter adapter,LoadingAnimation lda){
        setLoader(lda);
        cfAdapter=adapter;
        System.out.println("adapter called");
        refresh();
        System.out.println("loadded 1");
    }
    public void refresh(){
        refreshing=true;
        loadingAnimation.setStatus(10,true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("ii: thread running..");
                String json="";
                boolean netStat=false;
                try {
                    loadingAnimation.setStatus(25,true);
                    URL url = new URL("https://codeforces.com/api/contest.list?gym=false");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    loadingAnimation.setStatus(50,true);

                    int bufferSize = 1;
                    char[] buffer = new char[bufferSize];
                    StringBuilder out = new StringBuilder();
                    InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
                    for (int numRead; (numRead = inputStreamReader.read(buffer, 0, buffer.length)) > 0; ) {
                        out.append(buffer);
                    }
                    json=out.toString();
                    if(!json.equals("")) netStat=true;
                }catch (Exception e){
                    Log.d("ii::","Failed");
                    netStat=false;
                }


                json=saveAndGet(json);

                loadingAnimation.setStatus(75,netStat);

                filter(json,netStat);

            }
        }).start();
    }
    boolean refreshing=false;
    public boolean isRefreshing(){
        return refreshing;
    }
    private String saveAndGet(String json) {
        Log.d("ii::","ii: processing");
        File appDir=new File(ToolsSingleton.getInstance().getMainContext().getFilesDir(),"cfapi.save");
        try {
            System.out.println("ii: "+json);
            if(json==null||json.equals("")){
                System.out.println("ii: retriving files");
                FileReader fr=new FileReader(appDir);
                BufferedReader bf=new BufferedReader(fr);
                String line;
                StringBuilder sb=new StringBuilder();
                while ((line=bf.readLine())!=null){
                    sb.append(line);
                }
                bf.close();
                System.out.println("ii: "+sb.toString());
                return sb.toString();
            }else {
                FileWriter fw=new FileWriter(appDir,false);
                fw.write(json);
                fw.close();
                System.out.println("ii: File write success");
                return json;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void filter(String json,boolean netStat){
        try {
            JSONObject jsonObject=new JSONObject(json);
            if(jsonObject.getString("status").equals("OK")){
                System.out.println(json);
                JSONArray result=jsonObject.getJSONArray("result");


                ArrayList <JSONObject> ar=new ArrayList<>();

                for(int i=0;i<result.length();i++){
                    JSONObject contest=(JSONObject) result.get(i);
                    if(contest.getString("phase").equals("BEFORE")){
                        ar.add(contest);
                    }
                }

                float increase= (float) (24.0/ar.size());
                float k=75;
                for(JSONObject contest:ar){
                    cfAdapter.addItem(contest);
                    k+=increase;
                    loadingAnimation.setStatus(k,netStat);
                }
            }
            loadingAnimation.setStatus(100,netStat);
        } catch (JSONException e) {
            loadingAnimation.setStatus(100,netStat);
        }
        refreshing=false;
    }

    private LoadingAnimation loadingAnimation;
    public void setLoader(LoadingAnimation lda){
        loadingAnimation=lda;
    }

    public interface LoadingAnimation{
        void setStatus(float percent, boolean status);
    }
}
