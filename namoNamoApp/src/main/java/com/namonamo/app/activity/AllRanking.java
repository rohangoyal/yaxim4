package com.namonamo.app.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.namonamo.app.R;
import com.namonamo.app.adapter.AllRankingAdapter;
import com.namonamo.app.apicall.GetTopTenRewardApiCall;
import com.namonamo.app.content.RewardItem;
import com.namonamo.app.service.Servicable;
import com.namonamo.app.service.Servicable.ServiceListener;

public class AllRanking extends Fragment {
	// private String myfriends[];
	private View view;
	private ListView listView;
	private ServiceListener listener = new ServiceListener() {

		@Override
		public void onComplete(Servicable<?> service) {

			if (getActivity() == null)
				return;
			progressBar.setVisibility(View.GONE);
			ArrayList<RewardItem> rewardItems = ((GetTopTenRewardApiCall) service)
					.getAllRewardItems();
			if (rewardItems != null) {
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
		GetTopTenRewardApiCall apiCall = new GetTopTenRewardApiCall(getActivity());
		apiCall.runAsync(listener);
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
