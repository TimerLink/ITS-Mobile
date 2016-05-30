package cn.edu.hit.itsmobile.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.model.RuntimeParams;
import cn.edu.hit.itsmobile.ui.widget.NumberPickerDialog;
import cn.edu.hit.itsmobile.ui.widget.NumberPickerDialog.onButtonsClickListener;

public class SettingsFragment extends Fragment implements OnItemClickListener{
	private ListView listSettings;
	private String[] mSettingsItems;
	
	private RuntimeParams mRuntimeParams;

	@SuppressLint("InflateParams")
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_settings, null);
		listSettings = (ListView) mView.findViewById(R.id.list_settings);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mRuntimeParams = RuntimeParams.newInstance();
		
		mSettingsItems = getResources().getStringArray(R.array.setting_items);
		listSettings.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item_settings, mSettingsItems));
		listSettings.setOnItemClickListener(this);
	}
	
	public void setBusStationLookupRadius() {
		NumberPickerDialog dialog = new NumberPickerDialog(getActivity());
		dialog.setTitle(mSettingsItems[0]).setUnit(getString(R.string.meter));
		dialog.setValue(mRuntimeParams.nearbySearchRadius(), 1, 2000);
		dialog.setOnButtonsClickListener(new onButtonsClickListener() {

			@Override
			public void onOKClicked(NumberPickerDialog dialog, int currentValue) {
			    mRuntimeParams.setNearbySearchRadius(currentValue);
				dialog.dismiss();
			}

			@Override
			public void onCancelClicked(NumberPickerDialog dialog) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			setBusStationLookupRadius();
			break;

		default:
			break;
		}
	}

}
