/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icemobile.client.android.sxcore;

import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.net.URL;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.ImageView;
import android.view.Window;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.net.Uri;
import android.provider.Browser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.icemobile.client.android.c2dm.C2dmHandler;
import org.icemobile.client.android.c2dm.C2dmRegistrationHandler;
import org.icemobile.client.android.qrcode.CaptureActivity;
import org.icemobile.client.android.qrcode.CaptureJSInterface;
import org.icemobile.client.android.qrcode.Intents;

import org.icemobile.client.android.arview.ARViewInterface;
import org.icemobile.client.android.arview.ARViewHandler;
import org.icemobile.client.android.audio.AudioInterface;
import org.icemobile.client.android.audio.AudioRecorder;
import org.icemobile.client.android.audio.AudioPlayer;
import org.icemobile.client.android.camera.CameraInterface;
import org.icemobile.client.android.camera.CameraHandler;
import org.icemobile.client.android.contacts.ContactListInterface;
import org.icemobile.client.android.video.VideoInterface;
import org.icemobile.client.android.video.VideoHandler;

import org.icemobile.client.android.util.UtilInterface;
import org.icemobile.client.android.util.SubmitProgressListener;

public class SxCore extends Activity 
    implements C2dmRegistrationHandler, SubmitProgressListener {

    private static final String LOG_TAG = "SxCore";

    private class SxProgress extends ProgressDialog {
	public SxProgress(Context context) {
	    super(context);
	}

	@Override
        protected void onStop() {
	    stopped = true;
	}
    }

    /* Container configuration constants */
    public static final String HOME_URL = "http://www.icesoft.org/java/demos/m/icemobile-demos.html";
    protected static final boolean INCLUDE_CAMERA = true;
    protected static final boolean INCLUDE_AUDIO = true;
    protected static final boolean INCLUDE_VIDEO = true;
    protected static final boolean INCLUDE_CONTACTS = true;
    protected static final boolean INCLUDE_LOGGING = true;
    protected static final boolean INCLUDE_QRCODE = true;
    protected static final boolean INCLUDE_ARVIEW = true;
    protected static final boolean INCLUDE_GCM = true;
    protected static final String GCM_SENDER = "1020381675267";

    /* Intent Return Codes */
    protected static final int TAKE_PHOTO_CODE = 1;
    protected static final int TAKE_VIDEO_CODE = 2;
    protected static final int HISTORY_CODE = 3;
    public static final int SCAN_CODE = 4;
    protected static final int RECORD_CODE = 5;
    protected static final int ARVIEW_CODE = 6;
    protected static final int ARMVIEW_CODE = 7;

    public static final String SCAN_ID = "org.icemobile.id";

    private Handler mHandler = new Handler();
    private UtilInterface utilInterface;
    private CameraHandler mCameraHandler;
    private ContactListInterface mContactListInterface;
    private CameraInterface mCameraInterface;
    private CaptureActivity mCaptureActivity;
    private CaptureJSInterface mCaptureInterface;
    private AudioInterface mAudioInterface;
    private AudioRecorder mAudioRecorder;
    private AudioPlayer mAudioPlayer;
    private C2dmHandler mC2dmHandler;
    private VideoHandler mVideoHandler;
    private VideoInterface mVideoInterface;
    private ARViewHandler mARViewHandler;
    private ARViewInterface mARViewInterface;
    private Activity self;
    private boolean pendingCloudPush;
    private Uri mReturnUri;
    private Uri mPOSTUri;
    private String mParameters;
    private String mCurrentId;
    private String mCurrentjsessionid;
    private String mCurrentMediaFile;
    private Runnable mBrowserReturn;
    private boolean doRegister = false;
    private SxProgress progressDialog;
    private boolean stopped;
    private ImageView myImage;
    private Hashtable<String,String> splashScreenMap;
    private Hashtable<String,Bitmap> splashScreens;

    public void returnToBrowser()  {
        if (null == mReturnUri)  {
            mReturnUri = Uri.parse(HOME_URL);
        }
        Intent browserIntent = new 
                Intent(Intent.ACTION_VIEW, 
                mReturnUri);
        browserIntent.putExtra(
                Browser.EXTRA_APPLICATION_ID, "_icemobilesx");
        startActivity(browserIntent);
    }

    public Map parseQuery(String uri)  {
        HashMap parts = new HashMap();
        String[] nvpairs = uri.split("&");
        for (String pair : nvpairs)  {
            int index = pair.indexOf("=");
            if (-1 == index)  {
                parts.put(pair, null);
            } else {
                String name = URLDecoder.decode(pair.substring(0, index));
                String value = URLDecoder.decode(pair.substring(index + 1));
                parts.put(name, value);
            }
        }

        return parts;
        
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
	pendingCloudPush = false;

        mBrowserReturn = new Runnable()  {
            public void run()  {
                returnToBrowser();
            }
        };

	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
	myImage = (ImageView) findViewById(R.id.background);
	myImage.setAlpha(127);
	myImage.setVisibility(View.INVISIBLE);
	splashScreenMap = new Hashtable();
	splashScreens = new Hashtable();
	//progressDialog = new SxProgress(this);
	//stopped = false;
	//progressDialog.show( self, getString(R.string.app_name), getString(R.string.spinner_message) ,false, true);

        includeUtil();
        if (INCLUDE_QRCODE) {
            includeQRCode();
        }
        if (INCLUDE_ARVIEW && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            includeARView();
        }
        if (INCLUDE_ARVIEW ) {
            includeARMarkers();
        }
        if (INCLUDE_CAMERA) {
            includeCamera();
        }
        if (INCLUDE_AUDIO) {
            includeAudio();
        }
        if (INCLUDE_VIDEO) {
            includeVideo();
        }
        if (INCLUDE_CONTACTS) {
            includeContacts();
        }
        if (INCLUDE_GCM) {
            includeC2dm();
        }

	handleIntent(getIntent());
    }

    public void onNewIntent(Intent intent) {
	handleIntent(intent);
    }

    private void handleIntent (Intent intent) {
	myImage.setVisibility(View.INVISIBLE);
        Uri uri = intent.getData();
	Log.d(LOG_TAG, "URL launched " + uri);
	Map commandParts = new HashMap();
	Map commandParams = new HashMap();
	String commandName = null;

	if (null != uri)  {
	    String uriString = uri.toString();
	    String fullCommand = uriString.substring("icemobile:".length());
        if (fullCommand.startsWith("//"))  {
            fullCommand = fullCommand.substring("//".length());
        }
	    commandParts = parseQuery(fullCommand);

	    Log.d(LOG_TAG, "processing commandParts " + commandParts);
	    String command = (String) commandParts.get("c");
	    Log.d(LOG_TAG, "processing command " + command);

	    int queryIndex = command.indexOf("?");
	    if (-1 == queryIndex)  {
		commandName = command;
	    } else {
		commandName = command.substring(0, queryIndex);
		String commandParamsString = command.substring(queryIndex + 1);
		commandParams = parseQuery(commandParamsString);
		mCurrentId = (String) commandParams.get("id");
	    }

	    mReturnUri = Uri.parse((String) commandParts.get("r"));
	    mPOSTUri = Uri.parse((String) commandParts.get("u"));
	    mParameters = (String) commandParts.get("p");
	    if (null != commandName)  {
		displaySplashScreen((String)commandParts.get("s"));
		dispatch(commandName, commandParts, commandParams);
	    }
	} else {
	    returnToBrowser();
	}
    }

    public void displaySplashScreen(String params) {
	Bitmap splash=null;
	if (params != null) {
	    // Splash screen specified;
	    String[] nvpairs = URLDecoder.decode(params).split("&");
	    for (String pair : nvpairs)  {
		int index = pair.indexOf("=");
		if (-1 != index)  {
		    String name = pair.substring(0, index);
		    String value = pair.substring(index + 1);
		    if (name.equals("i")) {
			if (splashScreens.containsKey(value)) {
			    // Get cached bitmap;
			    splash = splashScreens.get(value);
			} else {
			    // Download and cache bitmap;
			    try {
				splash = BitmapFactory.decodeStream(new URL(value).openStream());
				splashScreens.put(value, splash);
			    } catch (IOException e) {
			    }
			}
			// Cache splash screen uri for app uri
			splashScreenMap.put(mReturnUri.toString(), value);
		    }
		}
	    }
	} else {
	    // Get cached splash screen if there is one;
	    String splashUri = splashScreenMap.get(mReturnUri.toString());
	    if (splashUri != null) {
		splash = splashScreens.get(splashUri);
	    } else {
		// Default splashScreen;
		splash = BitmapFactory.decodeResource(self.getResources(), R.drawable.background);
	    }
	}

	// Set the imageView to splash screen;
	if (splash != null) {
	    myImage.setImageBitmap(splash);
	    myImage.setVisibility(View.VISIBLE);
	}
    }

    public void dispatch(String command, Map env, Map params)  {
	Log.d(LOG_TAG, "dispatch " + command);
        mCurrentjsessionid  = (String) env.get("JSESSIONID");
        String postUriString = mPOSTUri.toString();

        if (null != mCurrentjsessionid)  {
            utilInterface.setCookie(postUriString,
                    "JSESSIONID=" +
                    mCurrentjsessionid);
        }

        if ("camera".equals(command))  {
            String path = mCameraInterface
                .shootPhoto(mCurrentId, "");
            mCurrentMediaFile = path;
	    Log.d(LOG_TAG, "dispatched camera " + path);
        } else if ("camcorder".equals(command))  {
            String path = mVideoInterface
                .shootVideo(mCurrentId, "");
            mCurrentMediaFile = path;
	    Log.d(LOG_TAG, "dispatched camcorder " + path);
        } else if ("fetchContacts".equals(command))  {
            mContactListInterface
                .fetchContact(mCurrentId, packParams(params));
        } else if ("microphone".equals(command))  {
            String path = mAudioInterface
                .recordAudio(mCurrentId);
            mCurrentMediaFile = path;
	    Log.d(LOG_TAG, "dispatched microphone " + path);
        } else if ("aug".equals(command))  {
	    Log.d(LOG_TAG, "checking augmented reality view option " + params.get("v"));
            if ("vuforia".equals(params.get("v")))  {
		//will need to implement wrappers to support container invocation
		Log.d(LOG_TAG, "using Class.forName to load AR Marker view");
                try {
                    Intent arIntent = new Intent(getApplicationContext(),
                            Class.forName("com.qualcomm.QCARSamples.FrameMarkers.FrameMarkers"));
                    arIntent.putExtra(mCurrentId, ARMVIEW_CODE);
                    arIntent.putExtra("attributes", packParams(params));
                    startActivityForResult(arIntent, ARMVIEW_CODE);
                } catch (Exception e)  {
		    Log.e(LOG_TAG, "Augmented Reality marker view not available ", e);
                    returnToBrowser();
                }
            } else {
                mARViewInterface
                    .arView(mCurrentId, packParams(params));
	    Log.d(LOG_TAG, "dispatched augmented reality " + packParams(params));
            }

        } else if ("scan".equals(command))  {
            mCaptureInterface
                .scan(mCurrentId, packParams(params));
	    Log.d(LOG_TAG, "dispatched qr scan " + packParams(params));
        } else if ("register".equals(command))  {
	    Log.d(LOG_TAG, "dispatched register ");
	    if (INCLUDE_GCM) {
		//if GCM registration does not occur in 3 seconds,
		//proceed with registration anyway
		mHandler.postDelayed(new Runnable() {
			public void run()  {
			    if (doRegister)  {
				registerSX();
			    }
			}
		    }, 3000);
		doRegister = true;
	    } else {
		// No GCM so proceed with registration;
		registerSX();
	    }
        }
    }

    private void registerSX() {
	String postUriString = mPOSTUri.toString();
	doRegister = false;
	String encodedForm = "";
	String cloudNotificationId = getCloudNotificationId();
	if (null != cloudNotificationId)  {
	    encodedForm = "hidden-iceCloudPushId=" +
		URLEncoder.encode(cloudNotificationId);
	}
	Log.d(LOG_TAG, "POSTing to " + postUriString);
	Log.d(LOG_TAG, "POST to register will send " + encodedForm);
	utilInterface.setUrl(postUriString);
	utilInterface.submitForm("", encodedForm, mBrowserReturn);
	Log.d(LOG_TAG, "POST to register URL with jsessionid " + mCurrentjsessionid);
    }

    public String packParams(Map params)  {
        StringBuilder result = new StringBuilder();
        String separator = "";
        for (Object key : params.keySet())  {
            result.append(separator);
            result.append(String.valueOf(key));
            result.append("=");
            result.append(String.valueOf(params.get(key)));
            separator = "&";
        }
        return result.toString();
    }

    public String encodeMedia(String id, String path)  {
        return "file-" + id + "=" + URLEncoder.encode(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        //better to store return data in the intent than keep member
        //fields on this class

        String encodedForm = "";
        
        if (null != mParameters)  {
            encodedForm = mParameters + "&";
        }

	Log.d(LOG_TAG, "onActivityResult: request = " + requestCode + ", result = " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO_CODE:
		    Log.d(LOG_TAG, "onActivityResult will POST to " + mPOSTUri);
                    mCameraHandler.gotPhoto();
                    encodedForm += encodeMedia(mCurrentId,
                            mCurrentMediaFile);
                    utilInterface.setUrl(mPOSTUri.toString());
                    utilInterface.submitForm("", encodedForm, mBrowserReturn);
		    Log.d(LOG_TAG, "onActivityResult completed TAKE_PHOTO_CODE");
                    break;
                case TAKE_VIDEO_CODE:
                    mVideoHandler.gotVideo(data);
                    encodedForm += encodeMedia(mCurrentId,
                            mCurrentMediaFile);
                    utilInterface.setUrl(mPOSTUri.toString());
                    utilInterface.submitForm("", encodedForm, mBrowserReturn);
		    Log.d(LOG_TAG, "onActivityResult completed TAKE_VIDEO_CODE");
                    break;
                case SCAN_CODE:
                    String scanResult = data.getStringExtra(Intents.Scan.RESULT);

                    encodedForm += "hidden-" + mCurrentId + "=" +
                            URLEncoder.encode(scanResult);
                    utilInterface.setUrl(mPOSTUri.toString());
                    utilInterface.submitForm("", encodedForm, mBrowserReturn);
                    break;
                case RECORD_CODE:
                    mAudioRecorder.gotAudio(data);
                    encodedForm += encodeMedia(mCurrentId,
                            mCurrentMediaFile);
                    utilInterface.setUrl(mPOSTUri.toString());
                    utilInterface.submitForm("", encodedForm, mBrowserReturn);
		    Log.d(LOG_TAG, "onActivityResult completed RECORD_CODE");
                    break;
                case ARVIEW_CODE:
		    Log.d(LOG_TAG, "onActivityResult completed ARVIEW_CODE");
                    returnToBrowser();
                    break;
                case ARMVIEW_CODE:
//		mARViewHandler.arViewComplete(data);
		    Log.d(LOG_TAG, "completed AR Marker View");
                    returnToBrowser();
                    break;
            }
        } else {
	    returnToBrowser();
	}
    }

    @Override
	protected void onResume() {
        super.onResume();
	if (stopped) {
	    stopped = false;
	    progressDialog.show( self, getString(R.string.app_name), getString(R.string.spinner_message) ,false, true);
	}
	// Clear any existing C2DM notifications;
	if (pendingCloudPush) {
	    pendingCloudPush = false;
	    mC2dmHandler.clearPendingNotification();
	}
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAudioPlayer.release();
    }

    public void handleC2dmRegistration(String id) {
	Log.d(LOG_TAG, "handleC2dmRegistration " + id);
        if (doRegister)  {
	    registerSX();
        }
    }

    public void handleC2dmNotification() {
	Log.d(LOG_TAG, "handleC2dmNotification ");
        pendingCloudPush = true;
    }

    public void submitProgress(int progress) {
    }

    protected String getCloudNotificationId() {
        String id = null;
        if (mC2dmHandler != null) {
            id = mC2dmHandler.getRegistrationId();
        }
        return id;
    }

    private void includeUtil() {
        utilInterface = new UtilInterface(this, null, "");
    }

    private void includeCamera() {
        mCameraHandler = new CameraHandler(this, null, utilInterface, TAKE_PHOTO_CODE);
        mCameraInterface = new CameraInterface(mCameraHandler);
    }

    private void includeContacts() {
        mContactListInterface = new ContactListInterface(utilInterface, this, getContentResolver());
        mContactListInterface.setCompletionCallback(new Runnable() {
                public void run()  {
                    String encodedContact = 
                            mContactListInterface.getEncodedContactList();
                    String encodedForm = "hidden-" + mCurrentId + "=" + encodedContact;
                    if (null != mParameters)  {
                        encodedForm += "&" + mParameters;
                    }
                    utilInterface.setUrl(mPOSTUri.toString());
                    utilInterface.submitForm("", encodedForm);
		    Log.d(LOG_TAG, "completionCallback completed fetchContacts:" + encodedContact);
                    returnToBrowser();
                }
            });
    }

    private void includeARView() {
        mARViewHandler = new ARViewHandler(this, null, utilInterface, ARVIEW_CODE);
        mARViewInterface = new ARViewInterface(mARViewHandler);
    }

    private void includeARMarkers() {
//        mARMarkersHandler = new ARMarkersHandler(this, mWebView, utilInterface, ARVIEW_CODE);
//        mARMarkersInterface = new ARViewInterface(mARViewHandler);
    }

    private void includeQRCode() {
        mCaptureInterface = new CaptureJSInterface(this, SCAN_CODE, SCAN_ID);
    }

    private void includeVideo() {
        mVideoHandler = new VideoHandler(this, null, utilInterface, TAKE_VIDEO_CODE);
        mVideoInterface = new VideoInterface(mVideoHandler);
    }

    private void includeAudio() {
        mAudioRecorder = new AudioRecorder(this, utilInterface, RECORD_CODE);
        mAudioPlayer = new AudioPlayer();
        mAudioInterface = new AudioInterface(mAudioRecorder, mAudioPlayer);
    }
    
    private void includeC2dm() {
        if (mC2dmHandler == null) {
            mC2dmHandler = new C2dmHandler(this, R.drawable.c2dm_icon, "ICE", "ICEmobile", "GCM Notification", this);
        }
        mC2dmHandler.start(GCM_SENDER);
    }

}
