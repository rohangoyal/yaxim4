package com.namonamo.app.common;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class ContactImageDownloader extends BaseImageDownloader {

	public ContactImageDownloader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected InputStream getStreamFromContent(String imageUri, Object extra)
			throws FileNotFoundException {
		ContentResolver res = context.getContentResolver();
		Uri uri = Uri.parse(imageUri);
		if (imageUri.startsWith("content://com.android.contacts/")) {
			return ContactsContract.Contacts.openContactPhotoInputStream(res,
					uri);
		} else {
			return res.openInputStream(uri);
		}
	}

}
