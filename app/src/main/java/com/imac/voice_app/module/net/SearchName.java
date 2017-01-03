package com.imac.voice_app.module.net;

import android.app.Activity;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.model.FileList;
import com.imac.voice_app.module.net.base.BaseGoogleDrive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by isa on 2016/9/21.
 */
public class SearchName extends BaseGoogleDrive {
    private Activity activity;
    private onCallBackEvent event;
    private String fileName;
    public final static String ACCOUNT_NAME = "femh.voice@gmail.com";
    private ArrayList<String> resultList;

    public SearchName(Activity activity, String fileName, onCallBackEvent event) {
        super(activity);
        this.activity = activity;
        this.event = event;
        this.fileName = fileName;
    }


    @Override
    protected void beforeAccess() {

    }

    @Override
    protected boolean onAccess() {
        FileList result = null;
        String line = "";
        resultList = new ArrayList<>();
        try {
            result = drive.files().list()
                    .setQ("name='" + fileName + "'")
                    .execute();
            if (result.getFiles().size() != 0) {
                InputStream inputStream1 = drive.files()
                        .export(result.getFiles().get(0).getId(), "text/csv")
                        .executeMediaAsInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream1));
                while ((line = reader.readLine()) != null) {
                    resultList.add(line);
                }
                return true;
            }
        } catch (UserRecoverableAuthIOException e) {
            //跳出權限dialog
            activity.startActivityForResult(e.getIntent(), 321);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void successAccess() {
        event.onSearchResult(resultList);
    }

    @Override
    protected void failAccess() {
        event.onSearchFail();
    }

    @Override
    protected String setAccount() {
        return ACCOUNT_NAME;
    }

    public interface onCallBackEvent {

        public void onSearchResult(ArrayList<String> search);

        public void onSearchFail();
    }
}
