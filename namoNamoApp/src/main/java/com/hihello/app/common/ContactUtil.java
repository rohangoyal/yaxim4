package com.hihello.app.common;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Contacts.Phones;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hihello.app.R;
import com.hihello.app.activity.UIApplication;
import com.hihello.app.adapter.ContentAdapter;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.content.InviteContact;

public class ContactUtil {
	public static String getContactName(Context context, String phoneNumber) {
		String contactName = null;
		Cursor cursor = null;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(phoneNumber));
			cursor = cr.query(uri, new String[] { PhoneLookup.DISPLAY_NAME },
					null, null, null);
			if (cursor == null) {
				return null;
			}
			if (cursor.moveToFirst()) {
				contactName = cursor.getString(cursor
						.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			}

		} catch (Exception x) {

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return contactName;
	}

	public static ArrayList<String> getAllContactNumbers(Context ctx) {
		ArrayList<String> allContacts = new ArrayList<String>();
		ContentResolver cr = ctx.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					allContacts.addAll(getAllPhoneNumber(id, cr));
				}
			}
			cur.close();

		}
		return allContacts;
	}

	public static ArrayList<InviteContact> getAllInviteContact(Context ctx) {
		ArrayList<InviteContact> allContacts = new ArrayList<InviteContact>();

		if(ctx!=null) {
			ContentResolver cr = ctx.getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
					null, null, ContactsContract.Contacts.DISPLAY_NAME);
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					String id = cur.getString(cur
							.getColumnIndex(ContactsContract.Contacts._ID));
					if (Integer
							.parseInt(cur.getString(cur
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						allContacts.addAll(getAllContacts(id, cr));
					}
				}
				cur.close();

			}
		}
		return allContacts;
	}

	public static ArrayList<String> getAllPhoneNumber(String id,
			ContentResolver cr) {
		ArrayList<String> phones = new ArrayList<String>();

		Cursor pCur = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
				new String[] { id }, null);
		while (pCur != null && pCur.moveToNext()) {
			String phoneNo = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			String contactName = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			if (contactName != null && contactName.length() > 1 && phoneNo.length()>=10)
				phones.add(phoneNo);
		}
		pCur.close();

		return phones;
	}

	public static ArrayList<InviteContact> getAllContacts(String id,
			ContentResolver cr) {
		ArrayList<InviteContact> phones = new ArrayList<InviteContact>();

		Cursor pCur = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
				new String[]{id}, null);
		while (pCur != null && pCur.moveToNext()) {
			String phoneNo = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			String contactName = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String image_uri = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
			phoneNo = prepareContactNumber(phoneNo);
			if (contactName != null && contactName.length() > 1
					&& phoneNo.length() > 10)
				phones.add(new InviteContact(contactName, phoneNo, image_uri));
		}
		pCur.close();

		return phones;
	}

	public static ArrayList<String> getAllEmail(String id, ContentResolver cr) {
		ArrayList<String> emails = new ArrayList<String>();

		Cursor emailCur = cr.query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
				new String[] { id }, null);
		while (emailCur.moveToNext()) {
			String email = emailCur
					.getString(emailCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			// String email_type = emailCur
			// .getString(emailCur
			// .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
			emails.add(email);
		}
		emailCur.close();
		return emails;
	}

	public static String getNumberType(int type) {
		switch (type) {
		case Phones.TYPE_CUSTOM:
			return "CUSTOM";
		case Phones.TYPE_FAX_HOME:
			return "FAX_HOME";
		case Phones.TYPE_FAX_WORK:
			return "FAX_WORK";
		case Phones.TYPE_HOME:
			return "HOME";
		case Phones.TYPE_MOBILE:
			return "MOBILE";
		case Phones.TYPE_OTHER:
			return "OTHER";
		case Phones.TYPE_PAGER:
			return "PAGER";
		case Phones.TYPE_WORK:
			return "WORK";
		default:
			return "MOBILE";
		}
	}

	public static String getEmailType(int type) {
		switch (type) {
		case Email.TYPE_CUSTOM:
			return "CUSTOM";
		case Email.TYPE_HOME:
			return "HOME";
		case Email.TYPE_MOBILE:
			return "MOBILE";
		case Email.TYPE_OTHER:
			return "OTHER";
		case Email.TYPE_WORK:
			return "WORK";
		default:
			return "MOBILE";
		}
	}

	public static JSONObject getContactDetail(Uri contactUri, Context ctx)
			throws Exception {
		JSONObject json = new JSONObject();
		ContentResolver cr = ctx.getContentResolver();
		Cursor cur = cr.query(contactUri, null, null, null, null);
		if (cur.getCount() > 0) {
			if (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String image_uri = cur
						.getString(cur
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
				json.put("Name", name);
				json.put("Image", image_uri);

				JSONArray arrayPhone = new JSONArray();
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						JSONObject phoneJson = new JSONObject();
						String phoneNo = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						String phoneType = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
						phoneJson.put("Number", phoneNo);
						phoneJson.put("Type", phoneType);

						arrayPhone.put(phoneJson);
					}
					pCur.close();
				}
				json.put("Phone", arrayPhone);

				JSONArray arrayEmail = new JSONArray();
				Cursor emailCur = ctx.getContentResolver().query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Email.CONTACT_ID
								+ " = ?", new String[] { id }, null);
				while (emailCur.moveToNext()) {
					JSONObject emailJson = new JSONObject();
					String email = emailCur
							.getString(emailCur
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					String email_type = emailCur
							.getString(emailCur
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
					emailJson.put("Email", email);
					emailJson.put("Type", email_type);
					arrayEmail.put(emailJson);
				}
				emailCur.close();
				json.put("Email", arrayEmail);

			}
			cur.close();

		}
		return json;
	}

	public static ArrayList<String> getPhoneContact(Context ctx) {
		String myMobileNo = HiHelloSharedPrefrence.getMobileNo(ctx);
		ArrayList<String> allContacts = ContactUtil.getAllContactNumbers(ctx);
		ArrayList<String> mobileColls = new ArrayList<String>();
		String mobile_numbers = "";
		for (String real_number : allContacts) {
			if(real_number.contains("99111")) {
				String x= "123456";
				x = x;
			}
			String number = prepareContactNumber(real_number);
			mobile_numbers = mobile_numbers + number + ",";
			if (mobile_numbers.length() > 1000) {
				if (mobile_numbers.endsWith(","))
					mobile_numbers = mobile_numbers.substring(0,
							mobile_numbers.length() - 1);
				mobileColls.add(mobile_numbers);
				mobile_numbers = "";
			}
		}
		mobile_numbers.replace(myMobileNo, "");
		mobile_numbers.replace(",,", ",");

		if (mobile_numbers.endsWith(","))
			mobile_numbers = mobile_numbers.substring(0,
					mobile_numbers.length() - 1);
		if (mobile_numbers.length() > 0) {
			mobileColls.add(mobile_numbers);
			mobile_numbers = "";
		}
		return mobileColls;
	}

	public static Bitmap loadContactPhoto(ContentResolver cr, Uri uri) {
		InputStream input = null;
		try {
			input = cr.openInputStream(uri);
		} catch (FileNotFoundException e) {
			input = ContactsContract.Contacts.openContactPhotoInputStream(cr,
					uri);
			e.printStackTrace();
		}
		if (input == null) {
			return null;
		}
		return BitmapFactory.decodeStream(input);
	}

	public static String uriToPath(Uri uri, Context ctx) {

		Cursor cursor = ctx.getContentResolver().query(uri, null, null, null,
				null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String file_path = cursor.getString(column_index);
		cursor.close();
		return file_path;
	}

	public static AlertDialog getContactDialog(Activity activity,
			JSONObject json) {
		Builder builder = new AlertDialog.Builder(activity).setCancelable(true);
		try {
			View view = activity.getLayoutInflater().inflate(
					R.layout.edit_contact, null);
			((TextView) view.findViewById(R.id.txt_name)).setText(json
					.optString("Name"));
			ImageView image_contact = (ImageView) view
					.findViewById(R.id.image_contact);
			String imageUri = json.optString("Image");
			image_contact.setImageBitmap(loadContactPhoto(
					activity.getContentResolver(), Uri.parse(imageUri)));
			ListView list_content = (ListView) view
					.findViewById(R.id.list_content);
			list_content.setAdapter(new ContentAdapter(activity
					.getLayoutInflater(), json, activity));

			builder.setView(view);
		} catch (Exception x) {
			x.getMessage();
		}

		return builder.create();
	}

	public static void saveContact2(Activity activity, JSONObject json) {
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
		intent.putExtra(ContactsContract.Intents.Insert.NAME,
				json.optString("Name"));
		ArrayList<ContentValues> data = new ArrayList<ContentValues>();

		if (json.has("Phone")) {
			JSONArray phoneArray = json.optJSONArray("Phone");
			int size = phoneArray.length();
			for (int index = 0; index < size; index++) {
				JSONObject phoneJson = phoneArray.optJSONObject(index);
				ContentValues row = new ContentValues();
				row.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				row.put(Phone.NUMBER, phoneJson.optString("Number"));
				row.put(Phone.TYPE, phoneJson.optInt("Type"));
				data.add(row);
			}

		}
		if (json.has("Email")) {
			JSONArray emailArray = json.optJSONArray("Email");
			int size = emailArray.length();
			for (int index = 0; index < size; index++) {
				JSONObject emailJson = emailArray.optJSONObject(index);
				ContentValues row = new ContentValues();
				row.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
				row.put(Email.ADDRESS, emailJson.optString("Email"));
				row.put(Email.TYPE, emailJson.optInt("Type"));
				data.add(row);
			}
		}
		intent.putParcelableArrayListExtra(
				ContactsContract.Intents.Insert.DATA, data);

		activity.startActivity(intent);
	}

	public static void saveContact(Context ctx, JSONObject json)
			throws JSONException {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		ContentProviderOperation.Builder op = ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				.withValue(
						ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
						json.optString("Name"));
		ops.add(op.build());

		if (json.has("Phone")) {
			JSONArray phoneArray = json.getJSONArray("Phone");
			int size = phoneArray.length();
			for (int index = 0; index < size; index++) {
				JSONObject phoneJson = phoneArray.getJSONObject(index);
				String phone = phoneJson.getString("Number");
				String phoneType = phoneJson.getString("Type");
				op = ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(
								ContactsContract.Data.RAW_CONTACT_ID, 0)
						.withValue(
								ContactsContract.Data.MIMETYPE,
								ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(
								ContactsContract.CommonDataKinds.Phone.NUMBER,
								phone)
						.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
								phoneType);
				ops.add(op.build());
			}
		}
		if (json.has("Email")) {
			JSONArray emailArray = json.getJSONArray("Email");
			int size = emailArray.length();
			for (int index = 0; index < size; index++) {
				JSONObject emailJson = emailArray.getJSONObject(index);
				String email = emailJson.getString("Email");
				String emailType = emailJson.getString("Type");
				op = ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(
								ContactsContract.Data.RAW_CONTACT_ID, 0)
						.withValue(
								ContactsContract.Data.MIMETYPE,
								ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
						.withValue(
								ContactsContract.CommonDataKinds.Email.ADDRESS,
								email)
						.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
								emailType);
				ops.add(op.build());
			}
		}
		try {

			ContentProviderResult[] result = ctx.getContentResolver()
					.applyBatch(ContactsContract.AUTHORITY, ops);
			int size = result.length;
		} catch (Exception e) {
//			int duration = Toast.LENGTH_SHORT;
//			Toast toast = Toast.makeText(ctx, e.getMessage(), duration);
//			toast.show();
		}

	}

	@TargetApi(19)
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	public static String prepareContactNumber(String number) {
		if(number.contains("99111")) {
			int x = 1;
			x++;
		}
//		number = number.trim().replace(" ", "");
//		number = number.replaceAll("[\\-\\.\\^:,]", "");
//		number = number.replaceAll("[^\\w\\s]", "");
		number = number.trim().replace(" ", "").replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "");
		if (number.startsWith("00")) {
		} else if (number.startsWith("+")) {
		} else if (number.startsWith("0")) {
			number = HiHelloSharedPrefrence
					.getCountryCode(UIApplication.context)
					+ number.substring(1);
		} else if (number.startsWith("91")) {
			number = "+" + number;
		} else {
			number = HiHelloSharedPrefrence
					.getCountryCode(UIApplication.context) + number;

		}
		return number;
	}

}
