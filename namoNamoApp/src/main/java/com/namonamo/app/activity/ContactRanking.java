package com.namonamo.app.activity;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.namonamo.app.R;
import com.namonamo.app.adapter.AllRankingAdapter;
import com.namonamo.app.apicall.GetTopTenContactRewardApiCall;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.content.RewardItem;
import com.namonamo.app.db.NamoNamoContactDBService;
import com.namonamo.app.service.Servicable;
import com.namonamo.app.service.Servicable.ServiceListener;

public class ContactRanking extends Fragment {
	// private String myfriends[];
	private View view;
	private ListView listView;
	private ServiceListener listener = new ServiceListener() {

		@Override
		public void onComplete(Servicable<?> service) {

			if (getActivity() == null)
				return;
			progressBar.setVisibility(View.GONE);

			ArrayList<RewardItem> rewardItems = ((GetTopTenContactRewardApiCall) service)
					.getAllRewardItems();
			if (rewardItems == null) {
				if (getActivity() != null)
					Toast.makeText(getActivity(), "Something Wrong",
							Toast.LENGTH_SHORT).show();
			} else {
				AllRankingAdapter adapter = new AllRankingAdapter(
						getActivity(), rewardItems);
				listView.setAdapter(adapter);
			}

		}
	};
	private ProgressBar progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.list_fragment, container, false);

		// ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_list_item_1, myfriends);
		listView = (ListView) view.findViewById(R.id.listView);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		// listView.setAdapter(listAdapter);
		startFetchingAllData();

		return view;

	}

	private void startFetchingAllData() {
		new AsyncTask<Void, Void, Void>() {
			private String contact_user_id = "";

			@Override
			protected Void doInBackground(Void... params) {
				if (getActivity() != null) {
					ArrayList<String> items = NamoNamoContactDBService
							.getAllUsersId(getActivity());
					for (String item : items) {
						contact_user_id = contact_user_id + item + ",";
					}
					if (contact_user_id.length() > 0) {
						contact_user_id = contact_user_id.substring(0,
								contact_user_id.length() - 1);
					}
				} else {
					cancel(false);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				GetTopTenContactRewardApiCall apiCall = new GetTopTenContactRewardApiCall(
						getActivity(), contact_user_id,
						NamoNamoSharedPrefrence.getUserId(getActivity()));
				apiCall.runAsync(listener);
			}
		}.execute((Void) null);
	}

	// @Override
	// public void onListItemClick(ListView list, View v, int position, long id)
	// {
	//
	// Toast.makeText(getActivity(),
	// getListView().getItemAtPosition(position).toString(),
	// Toast.LENGTH_LONG).show();
	// }
}
