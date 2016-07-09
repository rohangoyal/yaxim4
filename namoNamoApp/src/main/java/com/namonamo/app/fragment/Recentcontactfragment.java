package com.namonamo.app.fragment;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.namonamo.androidclient.XMPPRosterServiceAdapter;
import com.namonamo.androidclient.chat.ChatWindow;
import com.namonamo.androidclient.service.IXMPPRosterService;
import com.namonamo.androidclient.service.XMPPService;
import com.namonamo.app.R;
import com.namonamo.app.activity.AllContacts;
import com.namonamo.app.activity.EditStatus;
import com.namonamo.app.activity.HomeMain_activity;
import com.namonamo.app.adapter.InviteContactAdapter;
import com.namonamo.app.adapter.MergeAdapter;
import com.namonamo.app.adapter.NamoNamoContactAdapter;
import com.namonamo.app.apicall.DownloadRegisterUserApiCall;
import com.namonamo.app.apicall.SendInvitationApiCall;
import com.namonamo.app.common.ContactUtil;
import com.namonamo.app.common.Log;
import com.namonamo.app.constant.Hihelloconstant;
import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.content.InviteContact;
import com.namonamo.app.db.NamoNamoContact;
import com.namonamo.app.db.NamoNamoContactDBService;
import com.namonamo.app.service.Servicable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rohan on 3/11/2016.
 */
public class Recentcontactfragment extends Fragment {
    static final int REQUEST_CONTACT = 106;
    View view;
    private ProgressDialog progress;
    protected static final String TAG = "ALL CONTACT";
    private Intent xmppServiceIntent;
    private ServiceConnection xmppServiceConnection;
    private ActionBar actionBar;
    protected ArrayList<NamoNamoContact> allContact;
    private Servicable.ServiceListener userListener = new Servicable.ServiceListener() {

        @Override
        public void onComplete(Servicable<?> service) {

            dismissProgress();
            if (service instanceof DownloadRegisterUserApiCall
                    && serviceAdapter != null
                    && serviceAdapter.isAuthenticated()) {

                final ArrayList<NamoNamoContact> newContacts = ((DownloadRegisterUserApiCall) service)
                        .getAllContact();
                if (newContacts != null && newContacts.size() > 0) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            for (NamoNamoContact contact : newContacts) {
                                serviceAdapter.sendPresenceRequest(
                                        contact.getJid(), "subscribed");

                                if (!serviceAdapter.hasRosterItem(contact
                                        .getJid()))

                                    serviceAdapter.addRosterItem(
                                            contact.getJid(),
                                            contact.getDisplayName(),
                                            NamoNamoConstant.GENRAL_GROUP);
                            }
                            return null;
                        }

                        protected void onPostExecute(Void result) {

                            allContact = NamoNamoContactDBService
                                    .getAllContact(getActivity());
                            showContactList();
                            checkForContact();
                        }

                        ;
                    }.execute((Void) null);

                }
            }
        }
    };
    private ImageView imgClear;
    private EditText txtSearch;
    protected String searchText;
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            searchText = s.toString();
            showContactList();

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };
    private MenuItem.OnActionExpandListener listener = new MenuItem.OnActionExpandListener() {

        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            searchText = "";
            ((EditText) item.getActionView().findViewById(R.id.txt_search))
                    .setText("");
            showContactList();
            return true;
        }
    };

    void setUpActionView(View view) {
        txtSearch = (EditText) view.findViewById(R.id.txt_search);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtSearch, 0);
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
    public void onResume() {
        bindXMPPService();
        super.onResume();
    }

    private void bindXMPPService() {
        getActivity().bindService(xmppServiceIntent, xmppServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        unbindXMPPService();
        super.onPause();
    }

    private void unbindXMPPService() {
        try {
            getActivity().unbindService(xmppServiceConnection);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Service wasn't bound!");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
menu.clear();
        inflater.inflate(R.menu.selectcontact_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private AdapterView.OnItemClickListener chatItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            if (arg2 < showContact.size()) {
                NamoNamoContact contact = showContact.get(arg2);
                String userName = contact.getDisplayName();
                if (userName == null || userName.length() < 1)
                    userName = contact.getMobile_number();
                Hihelloconstant.username = contact.getDisplayName();
                Hihelloconstant.picurl = contact.getPic_url();
                Hihelloconstant.status = contact.getStatus();


                Hihelloconstant.mobnumber = contact.getMobile_number();
                startChatActivity(contact.getJid(), userName, null);
                getActivity().finish();
            } else {
                int index = arg2 - showContact.size();
                SendInvitationApiCall api = new SendInvitationApiCall(
                        NamoNamoSharedPrefrence
                                .getUserId(getActivity()),
                        inviteContacts.get(index).getPhoneNo().replace("+", ""));
                api.runAsync(invitationListener);
            }
        }
    };
    private NamoNamoContactAdapter contactAdapter;
    private ArrayList<NamoNamoContact> showContact;
    protected XMPPRosterServiceAdapter serviceAdapter;
    private View.OnClickListener refershClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            refershContacts(userListener);
            view.findViewById(R.id.layout_no_contact).setVisibility(View.GONE);
            view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }
    };
    private View.OnClickListener inviteFriend = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, REQUEST_CONTACT);
        }
    };
    private Servicable.ServiceListener invitationListener = new Servicable.ServiceListener() {

        @Override
        public void onComplete(Servicable<?> service) {
            if (((SendInvitationApiCall) service).isSuccess()) {
                Toast.makeText(getActivity(),
                        "Invitation sending successfull.", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };
    private MergeAdapter mergeAdapter;
    private ListView listView;
    protected InviteContactAdapter inviteAdapter;
    protected ArrayList<InviteContact> inviteContacts;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONTACT:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri contactUri = data.getData();
                    if (null == contactUri)
                        return;
                    Cursor cursor = getActivity().getContentResolver()
                            .query(contactUri,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                    null, null, null);
                    if (null == cursor)
                        return;
                    try {
                        while (cursor.moveToNext()) {
                            String number = cursor.getString(0);
                            number = ContactUtil.prepareContactNumber(number);
                            SendInvitationApiCall api = new SendInvitationApiCall(
                                    NamoNamoSharedPrefrence
                                            .getUserId(getActivity()),
                                    number.replace("+", ""));
                            api.runAsync(invitationListener);
                        }
                    } finally {
                        cursor.close();
                    }
                }
                break;
        }
    }

    ;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.recentcontactfragment, container, false);
        allContact = NamoNamoContactDBService
                .getAllContact(getActivity());

        listView = (ListView) view.findViewById(R.id.contactList);
		listView.addFooterView(getInviteView());
        listView.setOnItemClickListener(chatItemClick);
        inviteAdapter = new InviteContactAdapter(getLayoutInflater(getArguments()), null,
                getActivity());
        showContactList();
		fetchInviteContactList();

        registerXMPPService();
        checkForContact();
        return  view;
    }


    private void fetchInviteContactList() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                inviteContacts = ContactUtil
                        .getAllInviteContact(getActivity());
				Collections.sort(inviteContacts);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (inviteAdapter == null)
                    inviteAdapter = new InviteContactAdapter(
                            getLayoutInflater(getArguments()), inviteContacts,
                            getActivity());
                else
                    inviteAdapter.setContacts(inviteContacts);
                checkForContact();
                notifyAdapter();
            }
        }.execute((Void) null);
    }

    private void checkForContact() {
        if ((allContact == null || allContact.size() == 0) && (inviteContacts==null || inviteContacts.size()==0)) {
            if (DownloadRegisterUserApiCall.isBusy) {
                view.findViewById(R.id.layout_no_contact).setVisibility(View.GONE);
                view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.progressBar).setVisibility(View.GONE);


            }
        } else {
            view.findViewById(R.id.progressBar).setVisibility(View.GONE);

        }
    }

    private void registerXMPPService() {
        xmppServiceIntent = new Intent(getActivity(), XMPPService.class);
        xmppServiceIntent.setAction("org.yaxim.androidclient.XMPPSERVICE");

        xmppServiceConnection = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceAdapter = new XMPPRosterServiceAdapter(
                        IXMPPRosterService.Stub.asInterface(service));

            }

            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "called onServiceDisconnected()");
            }
        };

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.refresh:
                refershContacts(userListener);

                view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                break;

            case R.id.contact:
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                int PICK_CONTACT = 100;
                startActivityForResult(intent, PICK_CONTACT);
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private void showContactList() {
        if (searchText == null || searchText.length() == 0) {
            showContact = allContact;
        } else {
            showContact = new ArrayList<NamoNamoContact>();
            for (NamoNamoContact contact : allContact) {
                if (contact.getDisplayName().toUpperCase()
                        .contains(searchText.toUpperCase()))
                    showContact.add(contact);
            }
        }
        if (contactAdapter == null)
            contactAdapter = new NamoNamoContactAdapter(getLayoutInflater(getArguments()),
                    showContact, getActivity());
        else
            contactAdapter.setContacts(showContact);

        notifyAdapter();

//		getActionBar().setSubtitle(allContact.size() + " Contacts");
    }

    void notifyAdapter() {
        if (mergeAdapter == null) {
            mergeAdapter = new MergeAdapter();
            mergeAdapter.addAdapter(contactAdapter);
            mergeAdapter.addAdapter(inviteAdapter);
            listView.setAdapter(mergeAdapter);
        }
        mergeAdapter.notifyDataSetChanged();
    }

    private View getInviteView() {
        View view = getLayoutInflater(getArguments()).inflate(R.layout.cell_invite_friend,
                null);
        view.setOnClickListener(inviteFriend);
        return view;
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
    public void dismissProgress() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }
}
