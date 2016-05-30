package cn.edu.hit.itsmobile.ui.widget;

import android.app.Service;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.edu.hit.itsmobile.R;

public class BottomWindow extends PopupWindow{
	public ViewGroup mContainer;

	public BottomWindow(Context mContext){
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		mContainer = (ViewGroup) mLayoutInflater.inflate(R.layout.window_bottom, null);
		ColorDrawable dw = new ColorDrawable(0000000000);
		setContentView(mContainer);
		setFocusable(true);
		setOutsideTouchable(true);
        setBackgroundDrawable(dw);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.PopupAnimation);
		update();
	}

	public BottomWindow setText(String title) {
		TextView text = (TextView)mContainer.findViewById(R.id.text);
		text.setText(title);
		return this;
	}
	
	public BottomWindow setChevron(boolean visibility){
		ImageView chevron = (ImageView)mContainer.findViewById(R.id.chevron);
		chevron.setVisibility((visibility?View.VISIBLE:View.GONE));
		return this;
	}
	
	public BottomWindow setClose(boolean visibility){
		ImageView close = (ImageView)mContainer.findViewById(R.id.close);
		close.setVisibility((visibility?View.VISIBLE:View.GONE));
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		return this;
	}
	
	public void setOnClickListener(OnClickListener onClickListener) {
		mContainer.setOnClickListener(onClickListener);
	}
}
