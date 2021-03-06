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

package org.icemobile.client.android.contacts;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.BaseColumns;
import org.icemobile.client.android.util.AttributeExtractor;
import org.icemobile.client.android.util.JavascriptInterface;
import org.icemobile.client.android.util.UtilInterface;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.util.Base64;
import android.util.Log;

/**
 *
 */
@TargetApi(8)
    public class ContactListInterface implements JavascriptInterface {

	private Context mContext; 
	private ContentResolver mResolver; 
	private Runnable mCompletionCallback; 

	private String[] mContactArray;	
	private String[] mIdArray;	

	private final String SELECT_TYPE_ARG = "select";
	private final String FIELDS_ARG = "fields"; 
	private final String NAME_FIELD = "name"; 
	private final String EMAIL_FIELD = "email"; 
	private final String PHONE_FIELD = "phone"; 
	private final String SORT_ORDER =  " asc"; 

	private UtilInterface mInterface; 
	private boolean mFetchContact; 
	private boolean mFetchEmail; 
	private boolean mFetchPhone;   
	private int selected;

	public ContactListInterface(UtilInterface util, Context context, ContentResolver cr) {
	    mContext = context; 
	    mResolver = cr; 
	    mInterface = util;
	}

    public void setCompletionCallback(Runnable callback)  {
        mCompletionCallback = callback;
    }

	public void fetchContact(String id, String attr) {

	    Log.d("ICEmobileContainer", "Value of attr string: " + attr);

	    selected = -1;
	    AttributeExtractor attributes = new AttributeExtractor(attr);
	    String selectedFields = attributes.getAttribute( FIELDS_ARG, NAME_FIELD);
	    processFields(selectedFields); 

	    String presetFilter = attributes.getAttribute("pattern", null);
	    Log.d("ICEmobileContainer", "Value of preset filter: " + presetFilter); 
	    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);        

	    Cursor cur = null;
	    String[] projection = { ContactsContract.Contacts._ID,
				    ContactsContract.Contacts.DISPLAY_NAME};

	    try { 
		if (presetFilter != null) {
		    presetFilter = "%"+ presetFilter + "%";
		    String selection = ContactsContract.Contacts.DISPLAY_NAME + " like ?";        	
		    cur = mResolver.query( ContactsContract.Contacts.CONTENT_URI, null, selection, 
					   new String[] {presetFilter}, 
					   ContactsContract.Contacts.DISPLAY_NAME + SORT_ORDER);

		} else { 
		    cur = mResolver.query( ContactsContract.Contacts.CONTENT_URI, projection, null, 
					   null, ContactsContract.Contacts.DISPLAY_NAME + SORT_ORDER);
		}

	    } catch (Exception e) { 
		Log.e("ICEmobileContainer", "Exception doing query: " + e); 
		return;
	    }

	    int rowCount = cur.getCount();
	    int rowIdx = 0; 

	    if ( rowCount > 0 ) { 

		mContactArray = new String[ rowCount ];
		mIdArray = new String[ rowCount ];
		cur.moveToFirst();
		while (rowIdx < rowCount) { 

		    // Get contact name;
		    mIdArray[rowIdx] = cur.getString(cur.getColumnIndex(BaseColumns._ID));
		    mContactArray[rowIdx] = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		    cur.moveToNext();
		    rowIdx++;
		}          	
		cur.close(); 

		builder.setTitle("Select from Contact List "); 
		builder.setCancelable(true);
		builder.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		    });

		builder.setSingleChoiceItems(mContactArray, -1 , new SingleSelectListener() );
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			    uploadContactInfo();
			}		
		    });

		builder.create().show(); 
			
	    } else { 
		builder.setMessage(R.string.no_contact_message).show(); 

	    }
	}

	private void processFields(String fields) { 

	    mFetchContact = false; 
	    mFetchEmail = false; 
	    mFetchPhone = false; 
	    if (fields == null) { 
		return; 
	    }
	    StringTokenizer st = new StringTokenizer(fields, ",");
	    String t;
	    while (st.hasMoreElements()) { 
		t = st.nextToken().trim(); 
		if (EMAIL_FIELD.equalsIgnoreCase(t)) {
		    mFetchEmail = true; 
		} else if (NAME_FIELD.equalsIgnoreCase(t)) { 
		    mFetchContact = true; 
		} else if (PHONE_FIELD.equalsIgnoreCase(t)) { 
		    mFetchPhone = true; 
		}
	    }    	
	}

	/**
	 * Upload the contact info to the hidden fields in the javascript layer via the UtilInterface.loadURL method. 
	 */
	private void uploadContactInfo() {
	    if (selected < 0) {
            return;
        }
		String encodedContactList = getEncodedContactList(); 
		try { 
		    Log.d("ICEcontacts", "Encoded Contact = " + encodedContactList);
		    mInterface.loadURL("javascript:ice.addHidden(ice.currentContactId, ice.currentContactId, '" +  encodedContactList + "'); ");
            if (null != mCompletionCallback)  {
                mCompletionCallback.run();
            }
		} catch (Exception e) { 
		    Log.e("ICEmobile", "Exception encoding contact information: " + e); 
		}
    }
    
	public String getEncodedContactList() {

	    if (selected < 0) {
            return null;
        }

		StringBuilder contactList = new StringBuilder(); 
		if (mFetchContact) { 
		    contactList.append( NAME_FIELD+"=" + mContactArray [selected] + "&");
		}

		String[] projection;
		Cursor dataCur;
		String phone;
		String email;
		if (mFetchPhone) { 
		    // Get phone;
		    projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };
		    dataCur = mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					      projection,ContactsContract.CommonDataKinds.Email.CONTACT_ID + 						     " = " + mIdArray[selected], null, null);
		    if (dataCur.moveToFirst()) {
			contactList.append("phone=" + dataCur.getString(dataCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + "&");
			dataCur.close();
		    }
		}
		if (mFetchEmail) { 
		    // Get email;
		    projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };  //should be ContactsContract.CommonDataKinds.Email.ADDRESS but not available until API 11
		    dataCur = mResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
					      projection,ContactsContract.CommonDataKinds.Email.CONTACT_ID + 						     " = " + mIdArray[selected], null, null);
		    if (dataCur.moveToFirst()) {
			contactList.append("email=" + dataCur.getString(dataCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + "&");
			dataCur.close();
		    }
		}

		String encodedContactList = null;
        try {
            encodedContactList = URLEncoder.encode(
                contactList.toString(), "utf-8");
        } catch (Exception e)  {
		    Log.e("ICEmobile", "Exception encoding contact information: " + e); 
        }

        return encodedContactList;

	}

	class SingleSelectListener implements DialogInterface.OnClickListener {
	    public void onClick(DialogInterface dialog, int which) {
		selected = which;
	    } 
	}

    }
