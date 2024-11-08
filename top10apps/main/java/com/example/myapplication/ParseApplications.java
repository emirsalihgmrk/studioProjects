package com.example.myapplication;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;

    public ParseApplications() {
        this.applications = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplications(){
        return applications;
    }

    public boolean parse(String xmlData,int feedLimit){
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
//                        Log.d(TAG,"parse:Starting tag for: " + tagName);
                        if ("item".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentRecord = new FeedEntry();
                            feedLimit--;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
//                        Log.d(TAG,"parse:Ending tag for: " + tagName);
                        if (inEntry){
                            if ("item".equalsIgnoreCase(tagName)){
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if ("title".equalsIgnoreCase(tagName)) {
                                currentRecord.setTitle(textValue);
                            } else if ("link".equalsIgnoreCase(tagName)) {
                                currentRecord.setLink(textValue);
                            } else if ("creator".equalsIgnoreCase(tagName)) {
                                currentRecord.setDc_creator(textValue);
                            } else if ("pubDate".equalsIgnoreCase(tagName)) {
                                currentRecord.setPubDate(textValue);
                            } else if ("category".equalsIgnoreCase(tagName)){
                                currentRecord.addToCategories(textValue);
                            } else if ("guid".equalsIgnoreCase(tagName)) {
                                currentRecord.setGuid(textValue);
                            } else if ("description".equalsIgnoreCase(tagName)) {
                                currentRecord.setDescription(textValue);
                            }
                        }
                        if (feedLimit < 0){
                            return status;
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
//            for(FeedEntry app:applications){
//                Log.d(TAG, "*******************");
//                Log.d(TAG, app.toString());
//            }
        }catch (Exception e){
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
