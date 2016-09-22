package com.imac.voice_app.module;

import android.app.Activity;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.model.FileList;
import com.imac.voice_app.module.base.BaseGoogleDrive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginChecker extends BaseGoogleDrive {
    private final static String TAG = LoginChecker.class.getClass().getName();
    public final static String ACCOUNT_NAME = "voice.dr.wang@gmail.com";
    private Activity activity;
    private eventCallBack event;
    private String account;

    @Override
    protected void beforeAccess() {
        event.showProgress();
    }

    @Override
    protected boolean onAccess() {

        FileList result = null;
        boolean isSuccess = false;
        try {
            result = drive.files().list()
                    .setQ("name='使用者帳號管理'")
                    .execute();

            Log.e(TAG, result.getFiles().get(0).getId());
            InputStream inputStream1 = drive.files()
                    .export(result.getFiles().get(0).getId(), "text/csv")
                    .executeMediaAsInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream1));
            String line = "";
            while ((line = reader.readLine()) != null) {
                Log.e(TAG, String.valueOf(line));
                if (line.equals(account)) {
                    isSuccess = true;
                    break;
                }
            }
        } catch (UserRecoverableAuthIOException e) {
            //跳出權限dialog
            activity.startActivityForResult(e.getIntent(), 321);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return isSuccess;
    }

    @Override
    protected void successAccess() {
        event.hideProgress();
        event.onSuccessful(account);
    }

    @Override
    protected void failAccess() {
        event.hideProgress();
        event.onFail();
    }

    @Override
    protected String setAccount() {
        return ACCOUNT_NAME;
    }

    public LoginChecker(Activity activity, eventCallBack event) {
        super(activity);
        this.activity = activity;
        this.event = event;
    }

    public void checkFile(final String account) {
        this.account = account;
        execute();
    }
    public interface eventCallBack {
        public void onSuccessful(String account);

        public void onFail();

        public void showProgress();

        public void hideProgress();
    }
}