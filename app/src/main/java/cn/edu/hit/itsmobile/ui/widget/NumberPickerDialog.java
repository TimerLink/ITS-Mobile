package cn.edu.hit.itsmobile.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.edu.hit.itsmobile.R;

public class NumberPickerDialog extends AlertDialog {
	private Context mContext;
	private TextView tvTitle;
	private TextView tvValue;
	private TextView tvUnit;
	private Button btnOK;
	private Button btnCancel;
	private SeekBar seekBar;
	private onButtonsClickListener listener;
	
	private int currentValue = 0;
	private int minValue = 0;
	//private int maxValue = 0;

	public NumberPickerDialog(Context context) {
		super(context);
		this.mContext = context;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View mView = inflater.inflate(R.layout.dialog_number_picker, null);
		tvTitle = (TextView)mView.findViewById(R.id.title);
		tvValue = (TextView)mView.findViewById(R.id.value);
		tvUnit = (TextView)mView.findViewById(R.id.unit);
		btnOK = (Button)mView.findViewById(R.id.ok);
		btnCancel = (Button)mView.findViewById(R.id.cancel);
		seekBar = (SeekBar)mView.findViewById(R.id.seekBar);
		setView(mView);
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				currentValue = progress + minValue;
				tvValue.setText(String.valueOf(currentValue));
			}
		});
	}
	
	public NumberPickerDialog setTitle(String title){
		tvTitle.setText(title);
		return this;
	}
	
	public NumberPickerDialog setUnit(String unit){
		tvUnit.setText(unit);
		return this;
	}
	
	public NumberPickerDialog setValue(int current, int min, int max){
		this.minValue = min;
		//this.maxValue = max;
		this.currentValue = current;
		if(max < min){
			int temp = min;
			min = max;
			max = temp;
		}
		seekBar.setMax(max - min);
		seekBar.setProgress(current + min);
		tvValue.setText(String.valueOf(current));
		return this;
	}

	public NumberPickerDialog setOnButtonsClickListener(onButtonsClickListener listener_){
		if(listener_ != null){
			this.listener = listener_;
			btnOK.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onOKClicked(NumberPickerDialog.this, currentValue);
				}
			});
			btnCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.onCancelClicked(NumberPickerDialog.this);
				}
			});
		}
		return this;
	}
	
	public interface onButtonsClickListener {
		public void onOKClicked(NumberPickerDialog dialog, int currentValue);
		public void onCancelClicked(NumberPickerDialog dialog);
	}
}
