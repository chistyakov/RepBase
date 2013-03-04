package com.example.repbase;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
// currently not used
public class PaintedSimpleAdapter extends SimpleAdapter {

	private int[] colors; //= new int[]{0x30FF0000, 0x300000FF};

	public PaintedSimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		Resources res = context.getResources();
		colors = new int[]{res.getColor(R.color.purple_item),
				res.getColor(R.color.blue_item)};
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		int colorPos = position % colors.length;
		view.setBackgroundColor(colors[colorPos]);
		view.setClickable(false);
		return view;
	}

}
