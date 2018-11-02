package com.example.deneme.xapiyoutubevideo;

import android.os.AsyncTask;
import android.util.Log;

import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class VeriGonder extends AsyncTask <Void, Void, Void> {

    public double oran;  //Vdeonun ne kadarını izlediğini döndürür


    public void setOran (double orn){
        this.oran = orn;
       // Log.d("-----------------------", "Gerçek Oran2: " + oran);
       // Log.d("-----------------------", "Gerçek Orn2: " + orn);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        StatementClient client = null;
        try {
            client = new StatementClient("https://cloud.scorm.com/tc/APYWA5C6MI/sandbox/", "a8NKmHQVLyE8RZC54fg","2b2HoNCgOR-eZCn_39I");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Statement statement = new Statement();
        Agent agent = new Agent();
        Verb verb = Verbs.completed();
        agent.setMbox("mailto:ogrenci@example.com");
        agent.setName("OgrenciBir");
        statement.setActor(agent);
        statement.setId(UUID.randomUUID().toString());
        statement.setVerb(verb);
        Activity a = new Activity();
        a.setId("http://example.adlnet.gov/xapi/example/youtubevideo");
        statement.setObject(a);
        ActivityDefinition ad = new ActivityDefinition();
        ad.setChoices(new ArrayList<InteractionComponent>());
        InteractionComponent ic = new InteractionComponent();
        ic.setId("http://example.com");
        ic.setDescription(new HashMap<String, String>());
        ic.getDescription().put("en-US", "test");
        ad.getChoices().add(ic);
        ad.setInteractionType("choice");
        ad.setMoreInfo("http://example.com");
        a.setDefinition(ad);

        Result r = new Result();

        if(oran <= 1.2 && oran >= 0.8){
            r.setCompletion(true);
            r.setSuccess(true);
        }
        else{
            r.setCompletion(false);
            r.setSuccess(false);
        }

        Score ssss = new Score();

        ssss.setScaled((float) oran);

        r.setScore(ssss);

        statement.setResult(r);

        try {
            String publishedId = client.postStatement(statement);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("-----------------------", "Oran: "+oran);



        return null;
    }

}
