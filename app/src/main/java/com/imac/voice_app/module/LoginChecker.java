package com.imac.voice_app.module;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.imac.voice_app.util.login.LoginActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginChecker {
    private final static String TAG = LoginChecker.class.getClass().getName();
    private final static String ACCOUNT_NAME = "voice.dr.wang@gmail.com";
    private GoogleAccountCredential credential;
    private Drive drive;
    private Context context;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private eventCallBack event;

    public interface eventCallBack {
        public void onSuccessful();

        public void onFail();

        public void showProgress();

        public void hideProgress();
    }

    public LoginChecker(Context context, eventCallBack event) {
        this.context = context;
        this.event = event;
        credential = GoogleAccountCredential.usingOAuth2(
                context,
                DriveScopes.all()
        );

        credential.setSelectedAccountName(ACCOUNT_NAME);

        drive = new Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential)
                .build();

        if (credential.getSelectedAccountName() == null)
            ((LoginActivity) context).startActivityForResult(credential.newChooseAccountIntent(), 123);

    }

    public void checkFile(final String account) {
        event.showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileList result = null;
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
                    boolean isSuccess = false;
                    while ((line = reader.readLine()) != null) {
                        Log.e(TAG, String.valueOf(line));
                        if (line.equals(account)) {
                            isSuccess = true;
                            break;
                        }
                    }
                    if (isSuccess) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                event.hideProgress();
                                event.onSuccessful();
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                event.hideProgress();
                                event.hideProgress();
                                event.onFail();
                            }
                        });
                    }

                } catch (UserRecoverableAuthIOException e) {
                    //跳出權限dialog
                    ((LoginActivity) context).startActivityForResult(e.getIntent(), 321);
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}