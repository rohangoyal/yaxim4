package com.namonamo.app.fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.namonamo.androidclient.XMPPRosterServiceAdapter;
import com.namonamo.androidclient.chat.ChatWindow;
import com.namonamo.androidclient.data.ChatProvider;
import com.namonamo.androidclient.data.RosterProvider;
import com.namonamo.androidclient.service.IXMPPRosterService;
import com.namonamo.androidclient.service.XMPPService;
import com.namonamo.androidclient.util.PreferenceConstants;
import com.namonamo.androidclient.util.StatusMode;
import com.namonamo.app.R;
import com.namonamo.app.activity.AllContacts;
import com.namonamo.app.activity.DailyMessageActivity;
import com.namonamo.app.activity.EditStatus;
import com.namonamo.app.activity.HomeMain_activity;
import com.namonamo.app.activity.SettingPrefs;
import com.namonamo.app.activity.groupprofile;
import com.namonamo.app.adapter.RecentChatAdapter;
import com.namonamo.app.apicall.BlockUserApiCall;
import com.namonamo.app.apicall.DeleteBlockUserApiCall;
import com.namonamo.app.apicall.DownloadRegisterUserApiCall;
import com.namonamo.app.common.ContactUtil;
import com.namonamo.app.common.Log;
import com.namonamo.app.constant.Hihelloconstant;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.db.BlockUserDBService;
import com.namonamo.app.db.NamoNamoContact;
import com.namonamo.app.db.NamoNamoContactDBService;
import com.namonamo.app.db.RecentChat;
import com.namonamo.app.db.RecentChatDBService;
import com.namonamo.app.service.Servicable;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rohan on 3/11/2016.
 */
public class Recentchatfragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{

    View view;
    private static final String TAG = "Recent Chats";

//	private DrawerLayout mDrawerLayout;

    private ActionBar actionBar;
    private Intent xmppServiceIntent;
    ArrayAdapter<String> chatOptionMenu;
    private ServiceConnection xmppServiceConnection;
    private ContentObserver mContactObserver = new ContactObserver();
    private ArrayList<RecentChat> allRecentChats;
    private RecentChatAdapter recentChatAdapter;
    private ListView listView;
    private AdapterView.OnItemClickListener chatItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            if (arg2 == 0) {
                Intent intent = new Intent(getActivity(),
                        DailyMessageActivity.class);
                startActivity(intent);
            } else {
                RecentChat contact = showContact.get(arg2 - 1);
                if (contact.getDisplayName() == null) {
                    Hihelloconstant.username=contact.getDisplayName();
                    Hihelloconstant.picurl=contact.getProfilePic();
                    Hihelloconstant.status=contact.getStatus();
                    Hihelloconstant.mobnumber=contact.getMobileNo();
                    Log.e("status",Hihelloconstant.status);
                    Toast.makeText(getActivity(),"status="+Hihelloconstant.status,Toast.LENGTH_LONG).show();
                    contact.setDisplayName(contact.getMobileNo());
                }
                Hihelloconstant.username=contact.getDisplayName();
                Hihelloconstant.picurl=contact.getProfilePic();
                Hihelloconstant.status=contact.getStatus();
                Hihelloconstant.mobnumber=contact.getMobileNo();
                Log.e("status",Hihelloconstant.status);
                Toast.makeText(getActivity(),"status="+Hihelloconstant.status,Toast.LENGTH_LONG).show();
                startChatActivity(contact.getUserJID(),
                        contact.getDisplayName(), null);
            }
        }
    };

    private Servicable.ServiceListener userListener = new Servicable.ServiceListener() {
        @Override
        public void onComplete(Servicable<?> service) {
            showRecentChats();
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showRecentChats();
        }
    };

    protected XMPPRosterServiceAdapter serviceAdapter;

    public void setAndSaveStatus(StatusMode statusMode, String message) {
        SharedPreferences.Editor prefedit = PreferenceManager
                .getDefaultSharedPreferences(getActivity()).edit();
        // do not save "offline" to prefs, or else!
        if (statusMode != StatusMode.offline)
            prefedit.putString(PreferenceConstants.STATUS_MODE,
                    statusMode.name());
        prefedit.putString(PreferenceConstants.STATUS_MESSAGE, message);
        prefedit.commit();
        serviceAdapter.setStatusFromConfig();
    }

    private void registerXMPPService() {
        xmppServiceIntent = new Intent(getActivity(), XMPPService.class);
        xmppServiceIntent.setAction("org.yaxim.androidclient.XMPPSERVICE");

        xmppServiceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder service) {

                serviceAdapter = new XMPPRosterServiceAdapter(
                        IXMPPRosterService.Stub.asInterface(service));
                if (serviceAdapter != null && serviceAdapter.isAuthenticated())
                    setAndSaveStatus(StatusMode.chat, "online");
            }

            public void onServiceDisconnected(ComponentName name) {
                if (serviceAdapter != null && serviceAdapter.isAuthenticated())
                    setAndSaveStatus(StatusMode.away, "offline");

            }
        };

    }

    private void bindXMPPService() {
        getActivity().bindService(xmppServiceIntent, xmppServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindXMPPService() {
        try {
            getActivity().unbindService(xmppServiceConnection);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Service wasn't bound!");
        }
    }

    private EditText txtSearch;
    private ImageView imgClear;
    protected String searchText;
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            searchText = s.toString();
            showRecentChats();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private MenuItem.OnActionExpandListener listener = new MenuItem.OnActionExpandListener() {

        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            searchText = "";
            ((EditText) item.getActionView().findViewById(R.id.txt_search))
                    .setText("");
            showRecentChats();
            return true;
        }
    };

    private ArrayList<RecentChat> showContact;

    private ActionBarDrawerToggle mDrawerToggle;

    private long last_notify;

    private Handler mNotifyHandler = new Handler();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void startChatActivity(String user, String userName, String message) {
        Intent chatIntent = new Intent(getActivity(), ChatWindow.class);
        Uri userNameUri = Uri.parse(user);
        chatIntent.setData(userNameUri);
        chatIntent.putExtra(ChatWindow.INTENT_EXTRA_USERNAME, userName);
        if (message != null) {
            chatIntent.putExtra(ChatWindow.INTENT_EXTRA_MESSAGE, message);
        }
        startActivity(chatIntent);
    }

    void setUpActionView(View view) {
        txtSearch = (EditText) view.findViewById(R.id.txt_search);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtSearch, InputMethodManager.SHOW_IMPLICIT);
        imgClear = (ImageView) view.findViewById(R.id.img_search_clear);
        imgClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txtSearch.setText("");
            }
        });
        txtSearch.addTextChangedListener(watcher);
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.search:


                break;
            case R.id.newChat:
                Intent in=new Intent(getActivity(), AllContacts.class);
                startActivity(in);
                break;
            case  R.id.action_status:
                Intent in2 =new Intent(getActivity(),EditStatus.class);
                startActivity(in2);
                break;
            case  R.id.action_settings:
                Intent in3 =new Intent(getActivity(),SettingPrefs.class);
                startActivity(in3);
                break;
            case R.id.refersh:
                refershContacts(userListener);
                view.findViewById(R.id.layout_no_contact).setVisibility(View.GONE);
                view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                break;

        }
        return super.onOptionsItemSelected(menuItem);
    }

//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);


//		mDrawerToggle.syncState();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
         view = inflater.inflate(R.layout.chatrecentfragment, container, false);
        NamoNamoSharedPrefrence.setChatPageInit(getActivity(), true);
//        Toast.makeText(getActivity(),"onCreateView",Toast.LENGTH_SHORT).show();
//        Toolbar toolbar=(Toolbar)view.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
//        getSupportActionBar().setTitle("Select Contacts");
//		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
//		actionBar = getSupportActionBar();
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);

//		SlidingMenuSetUp slider_menu = new SlidingMenuSetUp();
//		mDrawerLayout = slider_menu.setUpDrawerMenu(this);
//		mDrawerToggle = slider_menu.getmDrawerToggle();
        // refershContacts(userListener);
        registerXMPPService();
        bindXMPPService();
        return  view;
    }

    @Override
    public void onResume() {
        try {

//            Toast.makeText(getActivity(),"onResume",Toast.LENGTH_SHORT).show();

            if (serviceAdapter != null && serviceAdapter.isAuthenticated())
                setAndSaveStatus(StatusMode.chat, "online");
//            Toast.makeText(getActivity(),"onResume if",Toast.LENGTH_SHORT).show();
            IntentFilter filter = new IntentFilter(
                    RecentChatDBService.RECENT_DB_CHANGE_ACTION);
            getActivity().registerReceiver(receiver, filter);
            showChatList();
            getActivity().getContentResolver().registerContentObserver(
                    RosterProvider.CONTENT_URI, true, mContactObserver);
        } catch (Exception x) {

        }
        super.onResume();
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
                    notifyStatusChange();
                    break;
                case RosterProvider.CONTACT_ID:
                    // String segment = uri.getPathSegments().get(1);
                    notifyStatusChange();
                    break;
            }
        }
    }

    private Runnable mNotifyChange = new Runnable() {

        @Override
        public void run() {
            updateContactStatus();
        }
    };

    private AdapterView.OnItemLongClickListener chatItemLongClick = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            RecentChat contact = showContact.get(arg2-1);
            if (contact.getDisplayName() == null) {
                Hihelloconstant.username=contact.getDisplayName();
                Hihelloconstant.picurl=contact.getProfilePic();
                Hihelloconstant.status=contact.getStatus();
                Hihelloconstant.mobnumber=contact.getMobileNo();
                Log.e("statusssssss",Hihelloconstant.picurl);
                contact.setDisplayName(contact.getMobileNo());
            }
            Hihelloconstant.username=contact.getDisplayName();
            Hihelloconstant.picurl=contact.getProfilePic();
            Hihelloconstant.status=contact.getStatus();
            Hihelloconstant.mobnumber=contact.getMobileNo();
            Log.e("status",Hihelloconstant.picurl);
//			startChatActivity(contact.getUserJID(),
//					contact.getDisplayName(), null);
            if (arg2 > 0) {
                showChatOptionMenu(arg2 - 1);
            }
            return true;
        }
    };

    void notifyStatusChange() {
        long ts = System.currentTimeMillis();
        if (ts > last_notify + 500)
            mNotifyChange.run();
        else
            mNotifyHandler.postDelayed(mNotifyChange, 200);
        last_notify = ts;
    }

    void removeChatHistory(final String JID) {
        getActivity().getContentResolver().delete(ChatProvider.CONTENT_URI,
                ChatProvider.ChatConstants.JID + " = ?", new String[] { JID });
    }

    protected void showChatOptionMenu(final int index) {
        final RecentChat contact = showContact.get(index);
        final boolean isBlocked = BlockUserDBService.getBlockUser(
                getActivity(), contact.getUserJID()) != null;

        chatOptionMenu = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1);
        chatOptionMenu.add("View Contact");
        chatOptionMenu.add(isBlocked ? "Unblock contact" : "Block contact");

        chatOptionMenu.add("Archive conversation");
        chatOptionMenu.add("Delete conversation");
        chatOptionMenu.add("Email conversation");

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                getActivity());
        builderSingle.setAdapter(chatOptionMenu,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(getActivity(),
                                        groupprofile.class);
                                intent.putExtra("JID", contact.getUserJID());
                                startActivity(intent);
                                break;
                            case 1:
                                if (isBlocked)
                                    doUnBlockContact(showContact.get(index));
                                else
                                    doBlockContact(showContact.get(index));
                                break;
                            case 2:
                                // removeChatHistory(contact.getUserJID());
                                RecentChatDBService.removeRecentChat(
                                        getActivity(),
                                        contact.getUserJID());
                                showRecentChats();
                                break;
                            case 3:
                                removeChatHistory(contact.getUserJID());
                                RecentChatDBService.removeRecentChat(getActivity(),contact.getUserJID());
                                showRecentChats();
                                break;
                        }
                    }
                });
        builderSingle.show();

    }

    protected void doUnBlockContact(RecentChat recentChat) {
        DeleteBlockUserApiCall api = new DeleteBlockUserApiCall(
                getActivity(), recentChat.getUserJID());
        api.runAsync(null);
    }

    protected void doBlockContact(RecentChat recentChat) {
        BlockUserApiCall api = new BlockUserApiCall(getActivity(),
                recentChat.getUserJID());
        api.runAsync(null);
    }

    @Override
   public void onPause() {
        getActivity().unregisterReceiver(receiver);
        getActivity().getContentResolver().unregisterContentObserver(mContactObserver);
        super.onPause();
    }

    private static final String[] STATUS_QUERY = new String[] {
            RosterProvider.RosterConstants.ALIAS,
            RosterProvider.RosterConstants.STATUS_MODE,
            RosterProvider.RosterConstants.JID,
            RosterProvider.RosterConstants.STATUS_MESSAGE,

    };

    public void updateContactStatus() {
        setAllOffLine();
        if (serviceAdapter == null || !serviceAdapter.isAuthenticated())
            return;
        Cursor cursor = getActivity().getContentResolver().query(RosterProvider.CONTENT_URI,
                STATUS_QUERY, null, null, null);
        // int ALIAS_IDX = cursor
        // .getColumnIndex(RosterProvider.RosterConstants.ALIAS);
        // int MODE_IDX = cursor
        // .getColumnIndex(RosterProvider.RosterConstants.STATUS_MODE);
        int JID_IDX = cursor.getColumnIndex(RosterProvider.RosterConstants.JID);
        int MSG_IDX = cursor
                .getColumnIndex(RosterProvider.RosterConstants.STATUS_MESSAGE);

        if (cursor.moveToFirst()) {
            do {
                // int status_mode = cursor.getInt(MODE_IDX);
                String status_message = cursor.getString(MSG_IDX);
                String jid = cursor.getString(JID_IDX);

                if (status_message != null
                        && status_message.equalsIgnoreCase("online"))
                    for (RecentChat chat : allRecentChats)
                        if (chat.getUserJID().equalsIgnoreCase(jid)) {
                            chat.setStatus("online");
                            break;
                        }

            } while (cursor.moveToNext());
        }
        cursor.close();
        if (recentChatAdapter != null)
            recentChatAdapter.notifyDataSetChanged();
    }

    void setAllOffLine() {
        for (RecentChat chat : allRecentChats)
            chat.setStatus("");
    }

    @Override
    public void onDestroy() {
        if (serviceAdapter != null && serviceAdapter.isAuthenticated())
            setAndSaveStatus(StatusMode.available, new Date().getTime() + "");
        unbindXMPPService();
        super.onDestroy();
    }

    private void showChatList() {
        listView = (ListView)view.findViewById(R.id.chat_list);
        listView.setOnItemClickListener(chatItemClick);
        listView.setOnItemLongClickListener(chatItemLongClick);

        showRecentChats();
    }

    void setLastChatDetail(RecentChat chat) {
        String selection = ChatProvider.ChatConstants.JID + "='" + chat.getUserJID() + "'";
        Cursor _cur = getActivity().getContentResolver().query(ChatProvider.CONTENT_URI,
                null, selection, null, null);
        if (_cur.moveToLast()) {
            chat.setMessage(_cur.getString(_cur
                    .getColumnIndex(ChatProvider.ChatConstants.MESSAGE)));
            chat.setFromMe(_cur.getInt(_cur
                    .getColumnIndex(ChatProvider.ChatConstants.DIRECTION)) == ChatProvider.ChatConstants.OUTGOING);
            chat.setDeliveryStatus(_cur.getInt(_cur
                    .getColumnIndex(ChatProvider.ChatConstants.DELIVERY_STATUS)));

        } else {
            chat.setMessage("");
            chat.setFromMe(false);
            chat.setDeliveryStatus(-1);
        }
        _cur.close();
    }

    private void showRecentChats() {

        allRecentChats = RecentChatDBService
                .getAllRecentChat(getActivity());

        if (showContact != null)
            showContact.clear();
        else
            showContact = new ArrayList<RecentChat>();

        for (RecentChat chat : allRecentChats) {
            NamoNamoContact contact = NamoNamoContactDBService
                    .fetchContactByJID(getActivity(),
                            chat.getUserJID());
            if (contact != null) {

//                Toast.makeText(getActivity(),"pro_pic "+contact.getPic_url(),Toast.LENGTH_LONG).show();

                chat.setProfilePic(contact.getPic_url());
                chat.setDisplayName(contact.getDisplayName());
            }
            if (chat.getDisplayName() == null
                    || chat.getDisplayName().length() == 0) {
                String name = ContactUtil.getContactName(
                        getActivity(), chat.getMobileNo());
                if (name != null && name.length() > 0) {
                    chat.setDisplayName(name);
                }
            }
            setLastChatDetail(chat);
            if (searchText != null
                    && chat.getDisplayName() != null
                    && chat.getDisplayName().toUpperCase()
                    .contains(searchText.toUpperCase())) {
                showContact.add(chat);
            }
        }

        if (searchText == null || searchText.length() == 0) {
            showContact = allRecentChats;
        }
        updateContactStatus();

        if (recentChatAdapter == null) {
            recentChatAdapter = new RecentChatAdapter(getLayoutInflater(getArguments()),
                    showContact, getActivity(), true, true);
            listView.setAdapter(recentChatAdapter);
        } else {
            recentChatAdapter.setRecentChats(showContact);
            recentChatAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub

    }

    public void onBackPressed() {
        Intent i=new Intent(getActivity(),HomeMain_activity.class);
        startActivity(i);
    }
    public void refershContacts(final Servicable.ServiceListener listener) {
        new AsyncTask<Void, Void, Void>() {
            private ArrayList<String> mobile_numbers;

            @Override
            protected Void doInBackground(Void... params) {
                mobile_numbers = ContactUtil
                        .getPhoneContact(getActivity());
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                for (String mobiles : mobile_numbers) {
                    DownloadRegisterUserApiCall apiCall = new DownloadRegisterUserApiCall(
                            getActivity(), mobiles);
                    apiCall.runAsync(listener);
                }
            }
        }.execute((Void) null);
    }
}
