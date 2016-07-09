package com.hihello.androidclient.chat;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.hihello.androidclient.chat.content.ChatAudioWrapper;
import com.hihello.androidclient.chat.content.ChatContactWrapper;
import com.hihello.androidclient.chat.content.ChatImageWrapper;
import com.hihello.androidclient.chat.content.ChatLocationWrapper;
import com.hihello.androidclient.chat.content.ChatTextWrapper;
import com.hihello.androidclient.chat.content.ChatVideoWrapper;
import com.hihello.androidclient.chat.content.IWrapper;
import com.hihello.androidclient.data.ChatProvider;
import com.hihello.androidclient.data.RosterProvider;
import com.hihello.androidclient.service.IXMPPChatService;
import com.hihello.androidclient.service.XMPPService;
import com.hihello.app.R;
import com.hihello.app.activity.AudioRecorderActivity;
import com.hihello.app.activity.BaseActivity;
import com.hihello.app.activity.CircularImageView;
import com.hihello.app.activity.EditContact;
import com.hihello.app.activity.LocationShare;
import com.hihello.app.activity.SaveContact;
import com.hihello.app.activity.ScaleImageActivity;
import com.hihello.app.activity.SelectContactActivity;
import com.hihello.app.activity.UIApplication;
import com.hihello.app.activity.groupprofile;
import com.hihello.app.apicall.AddRewardApiCall;
import com.hihello.app.apicall.BlockUserApiCall;
import com.hihello.app.apicall.DeleteBlockUserApiCall;
import com.hihello.app.apicall.UploadImageHttp;
import com.hihello.app.common.BitmapUtil;
import com.hihello.app.common.ContactUtil;
import com.hihello.app.common.Log;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.HiHelloNewConstant;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.db.BlockUserDBService;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.RecentChat;
import com.hihello.app.db.RecentChatDBService;
import com.hihello.app.db.hiHelloContactDBService;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

/**
 * Created by delaine on 6/25/2016.
 */
public class ChatWindow2 extends BaseActivity implements TextWatcher,
        LoaderManager.LoaderCallbacks<Cursor> {
    Boolean isBlocked;

    public static final String INTENT_EXTRA_USERNAME = ChatWindow.class
            .getName() + ".username";

    public static final String INTENT_EXTRA_MESSAGE = ChatWindow.class
            .getName() + ".message";

    public static final String INTENT_EXTRA_PHONE_NO = ChatWindow.class
            .getName() + ".phone";

    private static final String TAG = "hihello ChatWindow";
    private static final String[] PROJECTION_FROM = new String[] {
            ChatProvider.ChatConstants._ID, ChatProvider.ChatConstants.DATE,
            ChatProvider.ChatConstants.DIRECTION,
            ChatProvider.ChatConstants.JID, ChatProvider.ChatConstants.MESSAGE,
            ChatProvider.ChatConstants.DELIVERY_STATUS };

    private static final int[] PROJECTION_TO = new int[] { R.id.chat_time,
            R.id.chat_from, R.id.chat_message };

    private static final int DELAY_NEWMSG = 400;
    private static final int CHAT_MSG_LOADER = 0;

    private static final int REQUEST_PICK_CONTACT = 100;
    private static final int REQUEST_AUDIO = 101;
    private static final int REQUEST_VIDEO = 102;
    private static final int REQUEST_LOCATION = 103;
    private static final int REQUEST_IMAGE = 104;
    private static final int REQUEST_CAMERA = 105;
    private static final int REQUEST_EDIT_CONTACT = 106;
    private static final int REQUEST_PICK_GALLERY = 107;
    private static final int REQUEST_AUDIO_RECORD = 108;

    private ContentObserver mContactObserver = new ContactObserver();
    private ImageView mSendButton = null;
    private ImageView mCaptureImage = null;

    private ProgressBar mLoadingProgress;
    private EditText mChatInput = null;
    private String mWithJabberID = null;
    private String mUserScreenName = null;
    private Intent mServiceIntent;
    private ServiceConnection mServiceConnection;
    private XMPPChatServiceAdapter mServiceAdapter;
    public int mChatFontSize;
    private ActionBar actionBar;
    private ListView mListView;
    private ChatWindowAdapter mChatAdapter;

    private boolean mShowOrHide = true;
    Map<Integer, Boolean> selectionTable = new Hashtable<Integer, Boolean>();
    private AdapterView.OnItemClickListener chatClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            final IWrapper wrapper = (IWrapper) arg1.getTag();
            if (actionMode != null) {
                makeListSelection(arg2, wrapper);
                return;
            }
            try {
                Bundle bnd = new Bundle();
                ChatItem chatItem = wrapper.getChatItem();
                if (chatItem.getChatType() == ChatItem.CHAT_TYPE_LOCATION) {
                    double dLat = chatItem.getLatitude();
                    double dLng = chatItem.getLongitude();

                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr="
                                    + dLat + "," + dLng));
                    startActivity(intent);
                } else if (chatItem.getChatType() == ChatItem.CHAT_TYPE_CONTACT) {
                    Intent intent = new Intent(ChatWindow2.this,
                            SaveContact.class);
                    bnd.putString("DATA", chatItem.getFullContact());
                    intent.putExtras(bnd);
                    startActivity(intent);
                } else if (chatItem.getChatType() == ChatItem.CHAT_TYPE_IMAGE) {

                    String image_uri = "";
                    if (chatItem.isFrom_me()) {
                        String[] splits = chatItem.getLocalUrl().split("/");
                        if (chatItem.getLocalUrl().contains("Upload")) {
                            image_uri = HiHelloConstant.UPLOAD_IMAGE_DIR
                                    + splits[splits.length - 1];

                        } else {
                            image_uri = HiHelloConstant.MEDIA_IMAGE_DIR
                                    + splits[splits.length - 1];
                        }
                    } else {
                        String localPath = HiHelloSharedPrefrence
                                .getLocalPath(ChatWindow2.this,
                                        chatItem.getUrl());
                        if (localPath != null && localPath.length() > 0) {
                            String[] splits = localPath.split("/");
                            image_uri = HiHelloConstant.MEDIA_IMAGE_DIR
                                    + splits[splits.length - 1];
                        }
                    }
                    if (image_uri != null && image_uri.length() > 0) {
                        Intent intent = new Intent(ChatWindow2.this,
                                ScaleImageActivity.class);
                        bnd.putString(ScaleImageActivity.IMAGE_URL, image_uri);
                        intent.putExtras(bnd);
                        startActivity(intent);
                    } else {
                        if (!chatItem.isFrom_me())
                            ((ChatImageWrapper) wrapper).startDownloadFile();
                    }
                } else if (chatItem.getChatType() == ChatItem.CHAT_TYPE_VIDEO) {
                    if (chatItem.isFrom_me()) {
                        Intent intentToPlayVideo = new Intent(
                                Intent.ACTION_VIEW);
                        intentToPlayVideo.setDataAndType(
                                Uri.parse(chatItem.getLocalUrl()), "video/*");
                        startActivity(intentToPlayVideo);
                    } else {
                        String localPath = HiHelloSharedPrefrence
                                .getLocalPath(getApplicationContext(),
                                        chatItem.getUrl());
                        if (localPath != null && localPath.length() > 0) {
                            Intent intentToPlayVideo = new Intent(
                                    Intent.ACTION_VIEW);
                            intentToPlayVideo.setDataAndType(
                                    Uri.parse(localPath), "video/*");
                            startActivity(intentToPlayVideo);
                        }
                    }
                } else if (chatItem.getChatType() == ChatItem.CHAT_TYPE_AUDIO) {
                    if (chatItem.isFrom_me()) {
                        Intent intentToPlayVideo = new Intent(
                                Intent.ACTION_VIEW);
                        File file = new File(chatItem.getLocalUrl());
                        intentToPlayVideo.setDataAndType(Uri.fromFile(file),
                                "audio/*");
                        startActivity(intentToPlayVideo);
                    } else {
                        String localPath = HiHelloSharedPrefrence
                                .getLocalPath(getApplicationContext(),
                                        chatItem.getUrl());
                        if (localPath != null && localPath.length() > 0) {
                            Intent intentToPlayVideo = new Intent(
                                    Intent.ACTION_VIEW);
                            File file = new File(localPath);
                            intentToPlayVideo.setDataAndType(
                                    Uri.fromFile(file), "audio/*");
                            startActivity(intentToPlayVideo);
                        } else {

                        }
                    }
                }
            } catch (Exception x) {

            }
        }
    };

    protected ActionMode actionMode;

    // protected TextView actionModeTitle;

    private AdapterView.OnItemLongClickListener chatLongClick = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            if (actionMode == null) {
                actionMode = startSupportActionMode(new ChatOptionCallback());
                View view = getLayoutInflater().inflate(R.layout.title_layout,
                        null);
                actionModeTitle = (TextView) view.findViewById(R.id.txt_title);
                actionModeTitle.setTextColor(0xffffffff);
                actionMode.setCustomView(view);
                IWrapper wrapper = (IWrapper) arg1.getTag();
                makeListSelection(arg2, wrapper);
            }
            return true;
        }
    };

    public TextView actionModeTitle;

    private TextView mSubTitle,username,lastseen;
    private Toolbar toolbar;
    private CircularImageView userpic;

    void makeListSelection(int index, IWrapper wrapper) {
        if (selectionTable.size() < 10) {
            boolean selection = true;
            if (selectionTable.containsKey(index)) {
                selection = !selectionTable.get(index);
            }
            if (!selection)
                selectionTable.remove(index);
            else
                selectionTable.put(index, true);
            wrapper.setSelection(selection);
            wrapper.populateFrom();
            if (selectionTable.size() == 0) {
                actionMode.finish();
                return;
            }
            actionModeTitle.setText(selectionTable.size() + "");
            setUpActionModeMenu();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Selection exceeds limit of 10", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpActionModeMenu() {
        boolean isShare = true;
        boolean isDelete = true;
        boolean isCopy = true;
        int medias = 0;
        for (int key : selectionTable.keySet()) {
            ChatItem chatItem = (ChatItem) mChatAdapter.getItem(key);
            if (chatItem.getChatType() == ChatItem.CHAT_TYPE_TEXT)
                isShare = false;
            if (chatItem.getChatType() == ChatItem.CHAT_TYPE_CONTACT
                    || chatItem.getChatType() == ChatItem.CHAT_TYPE_LOCATION) {
                isCopy = false;
                isShare = false;
            }
            if (chatItem.getChatType() == ChatItem.CHAT_TYPE_AUDIO
                    || chatItem.getChatType() == ChatItem.CHAT_TYPE_IMAGE
                    || chatItem.getChatType() == ChatItem.CHAT_TYPE_VIDEO) {
                medias++;
                isCopy = false;
                if (medias > 1)
                    isShare = false;
            }
        }
        if (!isShare)
            actionMode.getMenu().getItem(2).setVisible(false);
        else
            actionMode.getMenu().getItem(2).setVisible(true);
        if (!isCopy)
            actionMode.getMenu().getItem(1).setVisible(false);
        else
            actionMode.getMenu().getItem(1).setVisible(true);
    }

    class ChatOptionCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.chat_option_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_menu_copy:
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    StringBuffer buffer = new StringBuffer();
                    for (int key : selectionTable.keySet()) {
                        ChatItem chatItem = (ChatItem) mChatAdapter.getItem(key);
                        if (buffer.length() > 0)
                            buffer.append("\n");
                        buffer.append(chatItem.getChatText());

                    }
                    ClipData clip = ClipData.newPlainText("NamoNamo", buffer);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Message Copied.",
                            Toast.LENGTH_SHORT).show();
                    actionMode.finish();
                    break;
                case R.id.action_menu_delete:

                    AlertDialog alertDialog = new AlertDialog.Builder(
                            ChatWindow2.this).create(); // Read Update
                    alertDialog.setMessage("Are you sure?");

                    alertDialog.setButton("Delete",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    for (int key : selectionTable.keySet()) {
                                        ChatItem chatItem = (ChatItem) mChatAdapter
                                                .getItem(key);
                                        deleteChat(chatItem.getId());
                                    }
                                    actionMode.finish();

                                    // String selection = ChatConstants.JID + "='"
                                    // + mWithJabberID + "'";
                                    // Cursor _cur = getContentResolver().query(
                                    // ChatProvider.CONTENT_URI,
                                    // PROJECTION_FROM, selection, null, null);
                                    // String message = "";
                                    // if (_cur.moveToLast()) {
                                    // message = (_cur.getString(_cur
                                    // .getColumnIndex(ChatProvider.ChatConstants.MESSAGE)));
                                    // }
                                    // _cur.close();
                                    // updateRecentChatHistory(mWithJabberID,
                                    // message, false);
                                }
                            });
                    alertDialog.setButton2("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                }
                            });
                    alertDialog.show();
                    break;
                case R.id.action_menu_share:
                    for (int key : selectionTable.keySet()) {
                        try {
                            ChatItem chatItem = (ChatItem) mChatAdapter
                                    .getItem(key);
                            String localUrl = HiHelloSharedPrefrence.getLocalPath(
                                    getApplicationContext(), chatItem.getUrl());
                            if (localUrl.length() < 1)
                                localUrl = ContactUtil.uriToPath(
                                        Uri.parse(chatItem.getLocalUrl()),
                                        getApplicationContext());
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("*/*");
                            File file = new File(localUrl);

                            sharingIntent.putExtra(Intent.EXTRA_STREAM,
                                    Uri.fromFile(file));

                            startActivity(Intent.createChooser(sharingIntent, ""));
                        } catch (Exception x) {
                            x.getMessage();
                        }
                        break;
                    }
                    actionMode.finish();
                    break;
                case R.id.action_menu_forward:
                    JSONArray array = new JSONArray();
                    try {
                        for (int key : selectionTable.keySet()) {
                            ChatItem chatItem = (ChatItem) mChatAdapter
                                    .getItem(key);
                            array.put(new JSONObject(chatItem.getMessage()));
                        }
                    } catch (Exception x) {
                    }
                    if (array.length() < 1) {
                        return false;
                    }
                    Intent intent = new Intent(ChatWindow2.this,
                            SelectContactActivity.class);
                    intent.putExtra(SelectContactActivity.EXTRA_SHARE_DATA,
                            array.toString());
                    startActivity(intent);
                    break;

            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            // actionModeTitle = null;
            selectionTable.clear();
            mChatAdapter.notifyDataSetChanged();
        }

    }

    public void deleteChat(int id) {
        Uri rowuri = Uri.parse("content://" + ChatProvider.AUTHORITY + "/"
                + ChatProvider.TABLE_NAME + "/" + id);
        getContentResolver().delete(rowuri, null, null);
    }

    public void updateChat(int id, ContentValues values) {
        Uri rowuri = Uri.parse("content://" + ChatProvider.AUTHORITY + "/"
                + ChatProvider.TABLE_NAME + "/" + id);
        getContentResolver().update(rowuri, values, null, null);
    }
    AQuery aquery;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mChatFontSize = Integer
                .valueOf(UIApplication.getConfig(this).chatFontSize);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
		/*
		 * getSupportActionBar() .setBackgroundDrawable(
		 * getResources().getDrawable( R.drawable.header_bg));
		 */
        setContentView(R.layout.mainchat);
        getContentResolver().registerContentObserver(
                RosterProvider.CONTENT_URI, true, mContactObserver);




        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);

        userpic=(CircularImageView)toolbar.findViewById(R.id.conversation_contact_photo);
        username=(TextView)toolbar.findViewById(R.id.action_bar_title_1);
        lastseen=(TextView)toolbar.findViewById(R.id.action_bar_title_2);

        username.setText(HiHelloNewConstant.username);


        if(HiHelloNewConstant.picurl!=null) {
            if (HiHelloNewConstant.picurl.toString().length() > 1) {
                aquery = new AQuery(this, userpic);
                aquery.id(userpic).image(HiHelloNewConstant.picurl, true, true, 300, R.drawable.backheaderimage);
            }
        }


        if(getIntent().hasExtra("from"))
        {

            username.setText(getIntent().getStringExtra("name"));

            aquery = new AQuery(this, userpic);

            Log.e("pic",getIntent().getStringExtra("pic"));
            aquery.id(userpic).image(getIntent().getStringExtra("pic"), true, true, 300, R.drawable.backheaderimage);
        }
//		else
//		{
//			String secondLetter = String.valueOf(HiHelloNewConstant.username.charAt(0));
//			ColorGenerator generator1 = ColorGenerator.MATERIAL; // or use DEFAULT
//			int color1 = generator1.getRandomColor();
//			TextDrawable drawable1 = TextDrawable.builder()
//					.buildRound(secondLetter, color1);
//			userpic.setImageResource(R.drawable.defaultuser);
//
//		}
        getContentResolver().registerContentObserver(
                RosterProvider.CONTENT_URI, true, mContactObserver);

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ChatWindow2.this, groupprofile.class);
                in.putExtra("jid",mWithJabberID);
                in.putExtra("last",lastseen.getText().toString());
                startActivity(in);
            }
        });

        userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ChatWindow2.this, groupprofile.class);
                in.putExtra("jid", mWithJabberID);
                in.putExtra("last", lastseen.getText().toString());
                startActivity(in);
            }
        });


        setContactFromUri();

        // Setup the actual chat view
        mListView = (ListView) findViewById(android.R.id.list);
        mChatAdapter = new ChatWindowAdapter(null, PROJECTION_FROM,
                PROJECTION_TO, mWithJabberID, mUserScreenName);
        mListView.setAdapter(mChatAdapter);
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        mListView.setDividerHeight((int) (metrics.density * 10));
        mListView.setDivider(null);
        mListView.setOnItemClickListener(chatClick);
        mListView.setOnItemLongClickListener(chatLongClick);
        // mListView.set
        Log.d(TAG, "registrs for contextmenu...");
        // registerForContextMenu(mListView);

        registerXMPPService();
        setSendButton();
        setUserInput();


        View view = LayoutInflater.from(this).inflate(R.layout.title_layout,
                null);
        ((TextView) view.findViewById(R.id.txt_title)).setText(mUserScreenName);
        mSubTitle = (TextView) view.findViewById(R.id.txt_sub_title);


        setUpEmojiKeyboard();
        // Setup the loader

        getLoaderManager().initLoader(CHAT_MSG_LOADER, null,
                (LoaderManager.LoaderCallbacks<Cursor>) this);

        // Loading progress
        mLoadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        mLoadingProgress.setVisibility(View.VISIBLE);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(PHOTO_PATH)) {
            mPhotoPath = Uri.parse(savedInstanceState.getString(PHOTO_PATH));
        }
        if (savedInstanceState != null
                && savedInstanceState.containsKey(VIDEO_PATH)) {
            mVideoPath = Uri.parse(savedInstanceState.getString(VIDEO_PATH));
        }

    }

    private View.OnClickListener emojiClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!popup.isShowing()) {

                // If keyboard is visible, simply show the emoji popup
                if (popup.isKeyBoardOpen()) {
                    popup.showAtBottom();
                    img_emoji.setImageResource(R.drawable.ic_action_keyboard);
                }

                // else, open the text keyboard first and immediately after that
                // show the emoji popup
                else {
                    mChatInput.setFocusableInTouchMode(true);
                    // mChatInput.requestFocus();
                    popup.showAtBottomPending();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mChatInput,
                            InputMethodManager.SHOW_IMPLICIT);
                    img_emoji.setImageResource(R.drawable.ic_action_keyboard);
                }
            }

            // If popup is showing, simply dismiss it to show the undelying text
            // keyboard
            else {
                popup.dismiss();
            }
        }
    };
    private ImageView img_emoji;
    private EmojiconsPopup popup;

    private void setUpEmojiKeyboard() {
        popup = new EmojiconsPopup(findViewById(R.id.root_view), this);
        popup.setSizeForSoftKeyboard();
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                mChatInput.append(emojicon.getEmoji());
            }
        });

        // Set on backspace click listener
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0,
                        0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                mChatInput.dispatchKeyEvent(event);
            }
        });
        // If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                img_emoji.setImageResource(R.drawable.smiley);
            }
        });

        // If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();
            }
        });

        img_emoji = (ImageView) findViewById(R.id.img_emoji);
        img_emoji.setOnClickListener(emojiClick);

    }

    // private void setCustomTitle(String title) {
    // LayoutInflater inflater = (LayoutInflater)
    // getSystemService(LAYOUT_INFLATER_SERVICE);
    // View layout = inflater.inflate(R.layout.chat_action_title, null);
    // mStatusMode = (ImageView) layout.findViewById(R.id.action_bar_status);
    // mTitle = (TextView) layout.findViewById(R.id.action_bar_title);
    // mSubTitle = (TextView) layout.findViewById(R.id.action_bar_subtitle);
    // mTitle.setText(title);
    //
    // setTitle(null);
    // // getSupportActionBar().setCustomView(layout);
    // // getSupportActionBar().setDisplayShowCustomEnabled(true);
    // }

    protected boolean needs_to_bind_unbind = false;
    private HiHelloContact hiHelloContact;

    private ProgressDialog progress;

    protected boolean dataToShareComplete;

    private Uri mPhotoPath;
    private Uri mVideoPath;

    private View.OnClickListener onCapture = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imageCapture();
        }
    };

    private ImageView mRecordAudio;

    private View.OnClickListener onRecorder = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ChatWindow2.this,
                    AudioRecorderActivity.class);
            startActivityForResult(intent, REQUEST_AUDIO_RECORD);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        updateContactStatus();
        needs_to_bind_unbind = true;
        if (mServiceAdapter != null)
            mServiceAdapter.setCurrentJID(mWithJabberID);
        RecentChatDBService.setReadMessage(getApplicationContext(),
                mWithJabberID);

    }

    @Override
    protected void onPause() {
        super.onPause();
        needs_to_bind_unbind = true;
        if (mServiceAdapter != null)
            mServiceAdapter.setCurrentJID("");
        RecentChatDBService.setReadMessage(getApplicationContext(),
                mWithJabberID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);

        isBlocked = BlockUserDBService.getBlockUser(
                ChatWindow2.this, mWithJabberID) != null;

        MenuItem bedMenuItem = menu.findItem(R.id.block);
        if (isBlocked) {
            bedMenuItem.setTitle("Unblock");
        } else {
            bedMenuItem.setTitle("Block");
        }
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!needs_to_bind_unbind)
            return;
        if (hasFocus)
            bindXMPPService();
        else
            unbindXMPPService();
        needs_to_bind_unbind = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (hasWindowFocus())
            unbindXMPPService();
        getContentResolver().unregisterContentObserver(mContactObserver);
    }

    private void registerXMPPService() {
        Log.i(TAG, "called startXMPPService()");
        mServiceIntent = new Intent(this, XMPPService.class);
        Uri chatURI = Uri.parse(mWithJabberID);
        mServiceIntent.setData(chatURI);
        mServiceIntent
                .setAction("com.namonamo.androidclient.service.XMPPSERVICE");

        mServiceConnection = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "called onServiceConnected()");
                mServiceAdapter = new XMPPChatServiceAdapter(
                        IXMPPChatService.Stub.asInterface(service),
                        mWithJabberID);

                mServiceAdapter.clearNotifications(mWithJabberID);
                try {
                    sendSeenMessage();
                    Bundle bnd = getIntent().getExtras();
                    if (bnd != null && !dataToShareComplete) {
                        String dataToShare = bnd
                                .getString(INTENT_EXTRA_SHARE_MESSAGE);
                        if (dataToShare != null && dataToShare.length() > 0) {
                            JSONArray array = new JSONArray(dataToShare);
                            int size = array.length();
                            for (int index = 0; index < size; index++) {
                                sendMessage(array.getJSONObject(index)
                                        .toString());
                            }
                        }
                        String streamToShare = bnd
                                .getString(INTENT_EXTRA_SHARE_STREAM);
                        String stream = streamToShare;
                        if (streamToShare != null) {
                            if (streamToShare.startsWith("content")) {
                                handleAllMediaUriSend(Uri.parse(streamToShare));
                            } else if (streamToShare.startsWith("file")) {

                                String streamMimeType = getMimeType(streamToShare);
                                String prefix = "file://"
                                        + Environment
                                        .getExternalStorageDirectory();
                                streamToShare = streamToShare.replace(prefix,
                                        "/sdcard");
                                streamToShare = URLDecoder.decode(
                                        streamToShare, "UTF-8");
                                stream = URLDecoder.decode(stream, "UTF-8");
                                if (streamMimeType.toLowerCase().startsWith(
                                        "image")) {
                                    Bitmap bitmap = BitmapUtil
                                            .getBitmapFromPath(streamToShare,
                                                    30, true);
                                    String bitmapEncodeString = BitmapUtil
                                            .BitMapToString(bitmap);
                                    bitmap.recycle();
                                    doUploadImage(streamToShare, true,
                                            bitmapEncodeString);
                                } else if (streamMimeType.toLowerCase()
                                        .startsWith("audio")) {
                                    startUploadingAudio(streamMimeType, "");
                                } else if (streamMimeType.toLowerCase()
                                        .startsWith("video")) {
                                    if (stream.contains("file://"))
                                        stream = stream.replace("file://", "");
                                    Bitmap bitmap = BitmapUtil
                                            .getVideoThumbnail(
                                                    getApplicationContext(),
                                                    getContentResolver(),
                                                    stream);
                                    String bitmapEncodeString = BitmapUtil
                                            .BitMapToString(bitmap);
                                    doUploadVideoFile(bitmapEncodeString, "0",
                                            streamToShare);
                                }
                            }
                        }
                        dataToShareComplete = true;
                    }
                } catch (Exception x) {
                    x.getMessage();
                }
                updateContactStatus();
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "called onServiceDisconnected()");
            }

        };
    }

    protected void sendSeenMessage() {
        // mServiceAdapter
        mServiceAdapter.setCurrentJID(mWithJabberID);
    }

    private void unbindXMPPService() {
        try {
            unbindService(mServiceConnection);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Service wasn't bound!");
        }
    }

    private void bindXMPPService() {
        bindService(mServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void setSendButton() {
        mSendButton = (ImageView) findViewById(R.id.img_send_chat);
        mCaptureImage = (ImageView) findViewById(R.id.img_capture_image);
        mRecordAudio = (ImageView) findViewById(R.id.img_record_audio);

        View.OnClickListener onSend = getOnSetListener();
        mSendButton.setOnClickListener(onSend);
        mCaptureImage.setOnClickListener(onCapture);
        mRecordAudio.setOnClickListener(onRecorder);
        mSendButton.setEnabled(false);

    }

    private void setUserInput() {
        Intent i = getIntent();
        mChatInput = (EditText) findViewById(R.id.Chat_UserInput);
        mChatInput.addTextChangedListener(this);
        if (i.hasExtra(INTENT_EXTRA_MESSAGE)) {
            mChatInput.setText(i.getExtras().getString(INTENT_EXTRA_MESSAGE));
        }
    }

    private void setContactFromUri() {
        Intent i = getIntent();
//        mWithJabberID = i.getDataString().toLowerCase();
        hiHelloContact = hiHelloContactDBService.fetchContactByJID(
                getApplicationContext(), mWithJabberID);
        RecentChatDBService.setReadMessage(getApplicationContext(),
                mWithJabberID);
        if (i.hasExtra(INTENT_EXTRA_USERNAME)) {
            mUserScreenName = i.getExtras().getString(INTENT_EXTRA_USERNAME)
                    .trim();
        } else {
            mUserScreenName = "Unknown";
        }

        ImageLoadingListener listener = new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {

            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                BitmapDrawable icon = new BitmapDrawable(arg2);
                getSupportActionBar().setIcon(icon);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {

            }
        };
        if (hiHelloContact != null && hiHelloContact.getPic_url() != null
                && hiHelloContact.getPic_url().length() > 0) {
//			ImageLoader.getInstance().loadImage(hiHelloContact.getPic_url(),
//					UIApplication.diOptions, listener);
        } else {

        }

    }

	/*
	 * @Override public void onCreateContextMenu(ContextMenu menu, View v,
	 * ContextMenu.ContextMenuInfo menuInfo) { super.onCreateContextMenu(menu,
	 * v, menuInfo);
	 *
	 * View target = ((AdapterContextMenuInfo) menuInfo).targetView; TextView
	 * from = (TextView) target.findViewById(R.id.chat_from);
	 * getMenuInflater().inflate(R.menu.chat_contextmenu, menu); if
	 * (!from.getText().equals(getString(R.string.chat_from_me))) {
	 * menu.findItem(R.id.chat_contextmenu_resend).setEnabled(false); } }
	 */

    // private CharSequence getMessageFromContextMenu(MenuItem item) {
    // View target = ((AdapterContextMenuInfo) item.getMenuInfo()).targetView;
    // TextView message = (TextView) target.findViewById(R.id.chat_message);
    // return message.getText();
    // }

    private View.OnClickListener getOnSetListener() {
        return new View.OnClickListener() {

            public void onClick(View v) {
                sendMessageIfNotNull();
            }
        };
    }

    private void sendMessageIfNotNull() {
        String message = mChatInput.getText().toString().trim();
        if (message.length() >= 1) {
            JSONObject json = new JSONObject();
            try {
                json.put("message", message);
                json.put("type", "TEXT");
                json.put("date", new Date().getTime());
                json.put("user_id", HiHelloSharedPrefrence
                        .getUserId(getApplicationContext()));

                json.put("mobileNo", HiHelloSharedPrefrence
                        .getMobileNo(getApplicationContext()));

            } catch (Exception x) {
            }
            sendMessage(json.toString());
        }
    }

    private void sendMessage(String message) {
        mChatInput.setText(null);
        mSendButton.setEnabled(false);
        mServiceAdapter.sendMessage(mWithJabberID, message);
//		if (!mServiceAdapter.isServiceAuthenticated())
//			showToastNotification(R.string.toast_stored_offline);
        updateRecentChatHistory(mWithJabberID, message);
        postReward(message);
        getListView().setSelection(getListView().getAdapter().getCount() - 1);
    }

    private void updateRecentChatHistory(String mWithJabberID2, String message) {
        RecentChat chat = RecentChatDBService.fetchRecentChat(
                getApplicationContext(), mWithJabberID2);
        if (chat == null) {
            chat = new RecentChat();
        }
        chat.setDate(new Date());
        chat.setFromMe(true);
        chat.setMessage(message);
        chat.setUnReadCount(0);
        if (hiHelloContact != null) {
            chat.setMobileNo(hiHelloContact.getMobile_number());
            chat.setProfilePic(hiHelloContact.getPic_url());
        }
        chat.setUserJID(mWithJabberID2);
        RecentChatDBService.saveRecentChat(getApplicationContext(), chat);
    }

    private void postReward(String message) {
        try {
            JSONObject json = new JSONObject(message);
            String rewardType = json.getString("type");
            int reward = 0;
            if (rewardType.equalsIgnoreCase("IMAGE")) {
                HiHelloSharedPrefrence.addRewardPoint(getApplicationContext(),
                        1, rewardType);
                reward = HiHelloSharedPrefrence.getRewardPoint(
                        getApplicationContext(), rewardType) / 2;

            } else if (rewardType.equalsIgnoreCase("AUDIO")
                    || rewardType.equalsIgnoreCase("VIDEO")
                    || rewardType.equalsIgnoreCase("CONTACT")
                    || rewardType.equalsIgnoreCase("LOCATION")) {

                reward = 1;
                HiHelloSharedPrefrence.addRewardPoint(getApplicationContext(),
                        1, rewardType);

            } else if (rewardType.equalsIgnoreCase("TEXT")) {
                HiHelloSharedPrefrence.addRewardPoint(getApplicationContext(),
                        1, rewardType);
                reward = HiHelloSharedPrefrence.getRewardPoint(
                        getApplicationContext(), rewardType) / 100;
            }
            if (reward > 0) {
                AddRewardApiCall api = new AddRewardApiCall(
                        getApplicationContext(),
                        HiHelloSharedPrefrence
                                .getUserId(getApplicationContext()),
                        HiHelloSharedPrefrence
                                .getMobileNo(getApplicationContext()), reward,
                        rewardType);
                api.runAsync(null);
            }
        } catch (Exception x) {
        }
    }

    private void markAsReadDelayed(final int id, final int delay) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (Exception e) {
                }
                markAsRead(id);
            }
        }.start();
    }

    private void markAsRead(int id) {
        Uri rowuri = Uri.parse("content://" + ChatProvider.AUTHORITY + "/"
                + ChatProvider.TABLE_NAME + "/" + id);
        Log.d(TAG, "markAsRead: " + rowuri);
        ContentValues values = new ContentValues();
        values.put(ChatProvider.ChatConstants.DELIVERY_STATUS, ChatProvider.ChatConstants.DS_SENT);
        getContentResolver().update(rowuri, values, null, null);
    }

    class ChatWindowAdapter extends SimpleCursorAdapter {
        String mScreenName, mJID;
        int prevDay = 0;

        ChatWindowAdapter(Cursor cursor, String[] from, int[] to, String JID,
                          String screenName) {
            super(ChatWindow2.this, android.R.layout.simple_list_item_1, cursor,
                    from, to);
            mScreenName = screenName;
            mJID = JID;
        }

        @Override
        public Object getItem(int position) {
            Cursor cursor = this.getCursor();
            int count = cursor.getCount();
            cursor.moveToPosition(count - 1 - position);
            return new ChatItem(cursor);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            boolean showDate = false;
            View row = convertView;
            ChatItem chatItem = (ChatItem) getItem(position);
            if (position == 0)
                showDate = true;
            else {
                ChatItem pChatItem = (ChatItem) getItem(position - 1);

                Date pDate = new Date(pChatItem.getDateMilliseconds());
                Date nDate = new Date(chatItem.getDateMilliseconds());
                int pDay = pDate.getDate();
                int nDay = nDate.getDate();
                if (pDay != nDay)
                    showDate = true;
            }
            // String from = chatItem.getJid();
            // if (chatItem.getJid().equals(mJID))
            // from = mScreenName;

            if (!chatItem.isFrom_me()
                    && chatItem.getDelivery_status() == ChatProvider.ChatConstants.DS_NEW) {
                markAsReadDelayed(chatItem.getId(), DELAY_NEWMSG);
            }

            try {
                boolean selection = false;
                if (selectionTable.containsKey(position)) {
                    selection = selectionTable.get(position);
                }

                if (chatItem.getChatType() == ChatItem.CHAT_TYPE_LOCATION) {

                    ChatLocationWrapper wrapper = new ChatLocationWrapper(
                            chatItem, ChatWindow2.this, selection, showDate);
                    row = wrapper.getRow();

                } else if (chatItem.getChatType() == ChatItem.CHAT_TYPE_AUDIO) {

                    ChatAudioWrapper wrapper = new ChatAudioWrapper(chatItem,
                            ChatWindow2.this, selection, showDate);
                    row = wrapper.getRow();

                } else if (chatItem.getChatType() == ChatItem.CHAT_TYPE_CONTACT) {

                    ChatContactWrapper wrapper = new ChatContactWrapper(
                            chatItem, ChatWindow2.this, selection, showDate);
                    row = wrapper.getRow();

                } else if (chatItem.getChatType() == ChatItem.CHAT_TYPE_IMAGE) {
                    ChatImageWrapper wrapper = new ChatImageWrapper(chatItem,
                            ChatWindow2.this, selection, showDate);
                    row = wrapper.getRow();

                } else if (chatItem.getChatType() == ChatItem.CHAT_TYPE_VIDEO) {
                    ChatVideoWrapper wrapper = new ChatVideoWrapper(chatItem,
                            ChatWindow2.this, selection, showDate);
                    row = wrapper.getRow();

                } else if (chatItem.getChatType() == ChatItem.CHAT_TYPE_TEXT) {
                    ChatTextWrapper wrapper = new ChatTextWrapper(chatItem,
                            ChatWindow2.this, selection, showDate);
                    row = wrapper.getRow();

                }
            } catch (Exception x) {

            }

            return row;
        }
    }

	/*
	 * public boolean onKey(View v, int keyCode, KeyEvent event) { if
	 * (event.getAction() == KeyEvent.ACTION_DOWN && keyCode ==
	 * KeyEvent.KEYCODE_ENTER) { sendMessageIfNotNull(); return true; } return
	 * false;
	 *
	 * }
	 */

    public void afterTextChanged(Editable s) {
        mSendButton.setVisibility(View.GONE);
        mCaptureImage.setVisibility(View.GONE);
        mRecordAudio.setVisibility(View.GONE);
        if (mChatInput.getText().length() >= 1) {
            mSendButton.setEnabled(true);
            mSendButton.setVisibility(View.VISIBLE);
        } else {
            mCaptureImage.setVisibility(View.VISIBLE);
            mRecordAudio.setVisibility(View.VISIBLE);
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private void showToastNotification(int message) {
        Toast toastNotification = Toast.makeText(this, message,
                Toast.LENGTH_SHORT);
        toastNotification.show();
    }

    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraVideo");
        mediaStorageDir.mkdirs();
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date
                .getTime());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.send_media:
                break;

            case R.id.send_contact:
                intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK_CONTACT);
                break;
            case R.id.send_picture:
                intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*, video/*");
                intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_PICK_GALLERY);
                break;
            case R.id.send_photo:
                imageCapture();
                break;
            case R.id.send_audio:
                intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("audio/*");
                intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_AUDIO);
                break;
            case R.id.send_location:
                intent = new Intent(ChatWindow2.this, LocationShare.class);
                startActivityForResult(intent, REQUEST_LOCATION);
                break;
            case R.id.send_video:
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                mVideoPath = getOutputMediaFileUri();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoPath);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                startActivityForResult(intent, REQUEST_VIDEO);
                break;

            case R.id.contact:

                Intent intent1 = new Intent(
                        ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
                        Uri.parse("tel:" + HiHelloNewConstant.mobnumber));
                intent1.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
                startActivity(intent1);
                break;
            case R.id.call_contact: {
                String phone;
                if (hiHelloContact != null) {
                    phone = hiHelloContact.getMobile_number();
                } else {
                    phone = mUserScreenName;
                }
                String uri = "tel:" + phone;
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
            break;

            case R.id.block:

                if (isBlocked)
                    doUnBlockContact();
                else
                    doBlockContact();

                finish();

                break;

            case R.id.archive:

                RecentChatDBService.removeRecentChat(
                        ChatWindow2.this,
                        mWithJabberID);
                finish();

                break;

            case R.id.email:
                break;

            case R.id.clear:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatWindow2.this);

//				// Setting Dialog Title
//				alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to clear message in this chat?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.delete);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("CLEAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event
//						Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        removeChatHistory(mWithJabberID);
                        RecentChatDBService.removeRecentChat(ChatWindow2.this,mWithJabberID);

                        mChatAdapter = new ChatWindowAdapter(null, PROJECTION_FROM,
                                PROJECTION_TO, mWithJabberID, mUserScreenName);
                        mListView.setAdapter(mChatAdapter);
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
//						Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

                break;

            case android.R.id.home:
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    protected void doUnBlockContact() {
        DeleteBlockUserApiCall api = new DeleteBlockUserApiCall(
                ChatWindow2.this,mWithJabberID);
        api.runAsync(null);
    }

    protected void doBlockContact() {
        BlockUserApiCall api = new BlockUserApiCall(ChatWindow2.this,
                mWithJabberID);
        api.runAsync(null);
    }

    private void imageCapture() {
        mPhotoPath = captureImage(REQUEST_CAMERA);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPhotoPath != null && outState != null)
            outState.putString(PHOTO_PATH, mPhotoPath.toString());
        if (mVideoPath != null && outState != null)
            outState.putString(VIDEO_PATH, mVideoPath.toString());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PICK_CONTACT:
                if (resultCode == RESULT_OK) {
                    Uri contactData = data.getData();
                    try {
                        JSONObject json = ContactUtil.getContactDetail(contactData,
                                getApplicationContext());
                        Intent intent = new Intent(this, EditContact.class);
                        Bundle bnd = new Bundle();
                        bnd.putString("DATA", json.toString());
                        intent.putExtras(bnd);
                        startActivityForResult(intent, REQUEST_EDIT_CONTACT);
                    } catch (Exception x) {
                        x.getMessage();
                    }
                }
                break;
            case REQUEST_PICK_GALLERY:
                if (resultCode == RESULT_OK) {
                    AlertDialog alert = new AlertDialog.Builder(this)
                            .setMessage("send to " + mUserScreenName)
                            .setCancelable(true).create();
                    alert.setButton2("Cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    alert.setButton("Send", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri mUploadMediaPath = data.getData();
                            if (mUploadMediaPath != null) {
                                handleAllMediaUriSend(mUploadMediaPath);
                            } else {
                                // showAlert("Please select profile Image");
                            }
                        }
                    });
                    alert.show();

                }

                break;
            case REQUEST_EDIT_CONTACT:
                if (resultCode == RESULT_OK) {
                    String message = data.getExtras().getString("DATA");
                    JSONObject json = new JSONObject();
                    try {
                        json.put("message", new JSONObject(message));
                        json.put("type", "CONTACT");
                        json.put("date", new Date().getTime());
                        json.put("user_id", HiHelloSharedPrefrence
                                .getUserId(getApplicationContext()));
                        json.put("mobileNo", HiHelloSharedPrefrence
                                .getMobileNo(getApplicationContext()));
                    } catch (Exception x) {
                    }
                    sendMessage(json.toString());
                }
                break;
            case REQUEST_AUDIO:
                if (resultCode == RESULT_OK) {
                    Uri audioPath = data.getData();
                    if (resultCode == RESULT_OK) {
                        String filePath = audioPath.toString();
                        filePath = ContactUtil.getPath(getApplicationContext(),
                                Uri.parse(filePath));
                        startUploadingAudio(filePath, "");
                    }
                }
                break;
            case REQUEST_AUDIO_RECORD:
                if (resultCode == RESULT_OK) {
                    Bundle bnd = data.getExtras();
                    String filePath = bnd
                            .getString(AudioRecorderActivity.AUDIO_OUTPUT);
                    String duration = bnd
                            .getString(AudioRecorderActivity.AUDIO_DURATION);

                    startUploadingAudio(filePath, duration);
                }
                break;
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    startUploadingImageFromUri(mPhotoPath, true);
                }
                break;
            case REQUEST_VIDEO:
                if (resultCode == RESULT_OK) {
                    startUploadingVideo(mVideoPath, false);
                }
                break;

            case REQUEST_LOCATION:
                if (resultCode == RESULT_OK) {
                    String message = data.getExtras().getString("DATA");
                    JSONObject json = new JSONObject();
                    try {
                        json.put("message", new JSONObject(message));
                        json.put("type", "LOCATION");
                        json.put("date", new Date().getTime());
                        json.put("user_id", HiHelloSharedPrefrence
                                .getUserId(getApplicationContext()));

                        json.put("mobileNo", HiHelloSharedPrefrence
                                .getMobileNo(getApplicationContext()));
                    } catch (Exception x) {
                    }
                    sendMessage(json.toString());
                }
                break;
        }
    }

    private void handleAllMediaUriSend(Uri mUploadMediaPath) {
        if (mUploadMediaPath
                .toString()
                .toLowerCase()
                .contains(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()
                                .toLowerCase())) {
            startUploadingImageFromUri(mUploadMediaPath, true);
        } else if (mUploadMediaPath
                .toString()
                .toLowerCase()
                .contains(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString()
                                .toLowerCase())) {
            startUploadingVideo(mUploadMediaPath, true);
        } else if (mUploadMediaPath
                .toString()
                .toLowerCase()
                .contains(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString()
                                .toLowerCase())) {
            String filePath = ContactUtil.getPath(getApplicationContext(),
                    mUploadMediaPath);
            startUploadingAudio(filePath, "");
        }
    }

    private void startUploadingAudio(String filePath, String duration) {
        try {
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("FileName", "RECORDING");
            jsonMessage.put("Duration", duration);
            jsonMessage.put("LocalPath", filePath);
            JSONObject json = new JSONObject();
            json.put("message", jsonMessage);
            json.put("type", "AUDIO");
            json.put("date", new Date().getTime());
            json.put("mobileNo", HiHelloSharedPrefrence
                    .getMobileNo(getApplicationContext()));
            String message = json.toString();
            getListView().setSelection(
                    getListView().getAdapter().getCount() - 1);
            mServiceAdapter.saveMessage(mWithJabberID, message);
            postReward(message);

        } catch (Exception x) {
        }
    }

    private void startUploadingVideo(Uri mProfilePath, boolean fromMediaStore) {
        String bitmapEncodeString = "";
        String duration = "";

        if (fromMediaStore) {
            int videoId = Integer.parseInt(mProfilePath.getLastPathSegment());
            bitmapEncodeString = BitmapUtil.getVideoThumbnailEncodeString(
                    videoId, getContentResolver());
            duration = BitmapUtil.getVideoDuration(mProfilePath,
                    getContentResolver());

        } else {
            Uri uri = Uri.parse("content://media/external/video/media");
            Cursor cursor = getContentResolver().query(uri, null, null, null,
                    null);
            if (cursor.moveToLast()) {
                int videoId = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));
                duration = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

                bitmapEncodeString = BitmapUtil.getVideoThumbnailEncodeString(
                        videoId, getContentResolver());
            }
            cursor.close();
        }

        String filePath = ContactUtil.getPath(getApplicationContext(),
                mProfilePath);
        doUploadVideoFile(bitmapEncodeString, duration, filePath);
    }

    void removeChatHistory(final String JID) {
        getContentResolver().delete(ChatProvider.CONTENT_URI,
                ChatProvider.ChatConstants.JID + " = ?", new String[]{JID});
    }



    private void doUploadVideoFile(String bitmapEncodeString, String duration,
                                   String filePath) {
        try {
            filePath = BitmapUtil.saveUploadVideo(getApplicationContext(),
                    filePath);
        } catch (Exception x) {
            return;
        }
        File file = new File(filePath);
        long length = file.length();

        String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
        JSONObject jsonUrl = new JSONObject();
        try {
            jsonUrl.put("FileName", filename);
            jsonUrl.put("PhotoData", bitmapEncodeString);
            jsonUrl.put("FileSize", length);
            jsonUrl.put("Duration", duration);

            jsonUrl.put("LocalPath", filePath);
            JSONObject json = new JSONObject();
            json.put("message", jsonUrl);
            json.put("type", "VIDEO");
            json.put("date", new Date().getTime());
            json.put("mobileNo", HiHelloSharedPrefrence
                    .getMobileNo(getApplicationContext()));
            String message = json.toString();
            getListView().setSelection(
                    getListView().getAdapter().getCount() - 1);
            mServiceAdapter.saveMessage(mWithJabberID, message);
            // updateRecentChatHistory(mWithJabberID, message);
            postReward(message);

        } catch (Exception x) {
        }
    }

    private void startUploadingImageFromUri(final Uri mProfilePath,
                                            final boolean fromCamera) {
        String bitmapEncodeString;
        int imageId = Integer.parseInt(mProfilePath.getLastPathSegment());
        bitmapEncodeString = BitmapUtil.getImageThumbnailEncodeString(imageId,
                getContentResolver());
        String filePath = ContactUtil.getPath(getApplicationContext(),
                mProfilePath);
        doUploadImage(filePath, fromCamera, bitmapEncodeString);
    }

    private void doUploadImage(String filePath, final boolean fromCamera,
                               String bitmapEncodeString) {
        filePath = BitmapUtil.saveUploadImage(getApplicationContext(),
                filePath, fromCamera);
        File file = new File(filePath);
        long length = file.length();

        String filename = filePath.substring(filePath.lastIndexOf("/") + 1);

        JSONObject jsonUrl = new JSONObject();
        try {
            jsonUrl.put("FileName", filename);
            jsonUrl.put("PhotoData", bitmapEncodeString);
            jsonUrl.put("FileSize", length);
            jsonUrl.put("LocalPath", filePath);
            JSONObject json = new JSONObject();
            json.put("message", jsonUrl);
            json.put("type", "IMAGE");
            json.put("date", new Date().getTime());
            json.put("mobileNo", HiHelloSharedPrefrence
                    .getMobileNo(getApplicationContext()));
            String message = json.toString();
            getListView().setSelection(
                    getListView().getAdapter().getCount() - 1);
            mServiceAdapter.saveMessage(mWithJabberID, message);
            postReward(message);

        } catch (Exception x) {
        }
    }

    class asyncUploadImage extends AsyncTask<Void, Void, Void> {
        protected String data;
        String filePath;
        String fileUri;
        private String jsonString = null;
        String fileType = "";
        public boolean fromCamera;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                fileUri = filePath;

                jsonString = UploadImageHttp.uploadMedia(filePath, fileType,
                        HiHelloSharedPrefrence
                                .getUserId(getApplicationContext()));
            } catch (Exception x) {
                x.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (jsonString == null)
                return;
            try {

                JSONArray jsonOutput = new JSONArray(jsonString);
                JSONObject jsonUrl = jsonOutput.getJSONObject(0);
                String filename = filePath
                        .substring(filePath.lastIndexOf("/") + 1);
                jsonUrl.put("FileName", filename);
                jsonUrl.put("PhotoData", data);
                jsonUrl.put("LocalPath", fileUri);
                JSONObject json = new JSONObject();
                json.put("message", jsonUrl);
                json.put("type", fileType);
                json.put("date", new Date().getTime());
                json.put("user_id", HiHelloSharedPrefrence
                        .getUserId(getApplicationContext()));

                json.put("mobileNo", HiHelloSharedPrefrence
                        .getMobileNo(getApplicationContext()));
                sendMessage(json.toString());

            } catch (Exception x) {

            }

        }
    }

    class asyncUploadAudio extends AsyncTask<Void, Void, Void> {
        String filePath;
        String fileUri;
        private String jsonString = null;
        String fileType = "";

        @Override
        protected Void doInBackground(Void... params) {
            try {
                fileUri = filePath;
                filePath = ContactUtil.getPath(getApplicationContext(),
                        Uri.parse(filePath));
                jsonString = UploadImageHttp.uploadMedia(filePath, fileType,
                        "2");
            } catch (Exception x) {
                x.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dismissProgress();
            if (jsonString == null)
                return;
            try {
                JSONArray jsonOutput = new JSONArray(jsonString);
                JSONObject jsonUrl = jsonOutput.getJSONObject(0);
                String filename = filePath
                        .substring(filePath.lastIndexOf("/") + 1);
                jsonUrl.put("FileName", filename);
                jsonUrl.put("LocalPath", fileUri);
                JSONObject json = new JSONObject();
                json.put("message", jsonUrl);
                json.put("type", fileType);
                json.put("date", new Date().getTime());
                json.put("user_id", HiHelloSharedPrefrence
                        .getUserId(getApplicationContext()));

                json.put("mobileNo", HiHelloSharedPrefrence
                        .getMobileNo(getApplicationContext()));
                sendMessage(json.toString());

            } catch (Exception x) {

            }

        }
    }

    class asyncUploadVideo extends AsyncTask<Void, Void, Void> {
        String filePath;
        String fileUri;
        private String jsonString = null;
        String fileType = "";

        @Override
        protected Void doInBackground(Void... params) {
            try {
                fileUri = filePath;
                filePath = ContactUtil.getPath(getApplicationContext(),
                        Uri.parse(filePath));
                jsonString = UploadImageHttp.uploadMedia(filePath, fileType,
                        "2");
            } catch (Exception x) {
                x.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dismissProgress();
            if (jsonString == null)
                return;
            try {
                JSONArray jsonOutput = new JSONArray(jsonString);
                JSONObject jsonUrl = jsonOutput.getJSONObject(0);
                String filename = filePath
                        .substring(filePath.lastIndexOf("/") + 1);
                jsonUrl.put("FileName", filename);
                jsonUrl.put("LocalPath", fileUri);
                JSONObject json = new JSONObject();
                json.put("message", jsonUrl);
                json.put("type", fileType);
                json.put("date", new Date().getTime());
                json.put("user_id", HiHelloSharedPrefrence
                        .getUserId(getApplicationContext()));

                json.put("mobileNo", HiHelloSharedPrefrence
                        .getMobileNo(getApplicationContext()));
                sendMessage(json.toString());

            } catch (Exception x) {

            }

        }
    }

    private static final String[] STATUS_QUERY = new String[] {
            RosterProvider.RosterConstants.ALIAS,
            RosterProvider.RosterConstants.STATUS_MODE,
            RosterProvider.RosterConstants.STATUS_MESSAGE, };

    public static final String INTENT_EXTRA_SHARE_MESSAGE = "INTENT_EXTRA_SHARE_MESSAGE";

    public static final String INTENT_EXTRA_SHARE_STREAM = "INTENT_EXTRA_SHARE_STREAM";

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private void updateContactStatus() {

        Cursor cursor = getContentResolver().query(RosterProvider.CONTENT_URI,
                STATUS_QUERY, RosterProvider.RosterConstants.JID + " = ?",
                new String[] { mWithJabberID }, null);
        int MSG_IDX = cursor
                .getColumnIndex(RosterProvider.RosterConstants.STATUS_MESSAGE);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            String status_message = cursor.getString(MSG_IDX);
            if (status_message == null)
                return;
            try {
                status_message = getCompleteTimeString(status_message);
            } catch (Exception x) {
            }
//			mSubTitle.setVisibility((status_message != null && status_message
//					.length() != 0) ? View.VISIBLE : View.GONE);

            lastseen.setVisibility((status_message !=null && status_message.length() !=0) ? View.VISIBLE : View.GONE);

            lastseen.setText(status_message);

            lastseen.setSelected(true);
            if (!status_message.equalsIgnoreCase("online")
                    && (hiHelloContact == null || hiHelloContact
                    .getShow_last_seen() == false)) {
                lastseen.setVisibility(View.GONE);
            }
        }
        cursor.close();
    }
    private String getCompleteTimeString(String status_message) {
        long time = Long.parseLong(status_message);
        Date lastDate = new Date(time);
        Date nowDate = new Date();
        if (lastDate.getMonth() == nowDate.getMonth()
                && lastDate.getYear() == nowDate.getYear()) {
            if (lastDate.getDate() == nowDate.getDate()) {
                status_message = "Last Seen at "
                        + getTimeString(time, "hh:mm aa");
            } else {
                Date lastSDate = new Date(lastDate.getYear(),
                        lastDate.getMonth(), lastDate.getDate());
                Date nowSDate = new Date(nowDate.getYear(), nowDate.getMonth(),
                        nowDate.getDate());

                long delay = (nowSDate.getTime() - lastSDate.getTime());

                if (delay == 24 * 60 * 60 * 1000) {
                    status_message = "Last Seen Yesterday at "
                            + getTimeString(time, "hh:mm aa ");
                } else {
                    status_message = "Last Seen at "
                            + getTimeString(time, "hh:mm aa dd-MMM");
                }
            }
        } else {
            status_message = "Last Seen at "
                    + getTimeString(time, "hh:mm aa dd-MMM");
        }
        return status_message;
    }

    private String getTimeString(long milliSeconds, String format) {
        SimpleDateFormat dateFormater = new SimpleDateFormat(format);
        Date date = new Date(milliSeconds);
        return dateFormater.format(date);
    }

    public ListView getListView() {
        return mListView;
    }

    public void showProgress(String message) {
        dismissProgress();
        progress = ProgressDialog.show(this, "", message, true);
    }

    public void dismissProgress() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    private void showKeyboard() {
        mChatInput.requestFocus();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mChatInput,
                        InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    private class ContactObserver extends ContentObserver {
        public ContactObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            int match = RosterProvider.URI_MATCHER.match(uri);
            switch (match) {
                case RosterProvider.CONTACTS:
                    updateContactStatus();
                    break;
                case RosterProvider.CONTACT_ID:
                    String segment = uri.getPathSegments().get(1);
                    updateContactStatus();
                    break;
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i == CHAT_MSG_LOADER) {
            String selection = ChatProvider.ChatConstants.JID + "='" + mWithJabberID + "'";
            return new CursorLoader(this, ChatProvider.CONTENT_URI,
                    PROJECTION_FROM, selection, null,
                    ChatProvider.ChatConstants._ID + " desc");

        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mLoadingProgress.setVisibility(View.GONE);
        mChatAdapter.changeCursor(cursor);

        // Only do this the first time (show or hide the keyboard)
        if (mShowOrHide) {
            if (cursor.getCount() == 0) {
                showKeyboard();
            }
            mShowOrHide = false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // Make sure we don't leak the (memory of the) cursor
        mChatAdapter.changeCursor(null);
    }
}