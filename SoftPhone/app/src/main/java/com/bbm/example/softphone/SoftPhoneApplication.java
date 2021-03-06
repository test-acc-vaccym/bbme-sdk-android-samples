/*
 * Copyright (c) 2017 BlackBerry.  All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbm.example.softphone;

import android.app.Application;

import com.bbm.sdk.BBMEnterprise;
import com.bbm.sdk.support.identity.auth.google.auth.GoogleAuthHelper;
import com.bbm.sdk.support.identity.user.firebase.FirebaseUserDbSync;
import com.bbm.sdk.support.util.FirebaseHelper;


public class SoftPhoneApplication extends Application {

    private static SoftPhoneApplication mApp;

    public static SoftPhoneApplication getInstance() {
        return mApp;
    }

    private IncomingCallObserver mCallObserver;

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;

        //sync our local user with firebase, retrieve all remote users, and start protected lite
        FirebaseHelper.initUserDbSyncAndProtected(true);

        // Initialize BBMEnterprise SDK then start it
        BBMEnterprise.getInstance().initialize(this);
        BBMEnterprise.getInstance().start();

        //Add incoming call observer
        mCallObserver = new IncomingCallObserver(SoftPhoneApplication.this);
        BBMEnterprise.getInstance().getMediaManager().addIncomingCallObserver(mCallObserver);

        //sign in with their Google account, and pass that data to our user manager when ready
        //Note that we must call GoogleAuthHelper.setActivity() from the main activity in case google needs to prompt the user to sign in
        GoogleAuthHelper.initGoogleSignIn(getApplicationContext(), FirebaseUserDbSync.getInstance(), getString(R.string.default_web_client_id));
    }

    public final IncomingCallObserver getCallObserver() {
        return mCallObserver;
    }

}
