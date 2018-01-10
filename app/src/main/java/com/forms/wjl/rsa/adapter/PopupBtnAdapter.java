package com.forms.wjl.rsa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.forms.wjl.rsa.R;

import java.util.List;

/**
 * 自定义的弹出框列表适配器,类似于大众点评或美团,如果想要此种效果可以直接使用
 */
public class PopupBtnAdapter extends ArrayAdapter<String> {

    private int resource;
    private int normalBg;
    private int pressBg;
    private int selection;

    public void setPressBg(int pressBg) {
        this.pressBg = pressBg;
    }

    public PopupBtnAdapter(Context context, String[] objects) {
        super(context, R.layout.item_popup_lv, objects);
        initParams();
    }


    public PopupBtnAdapter(Context context, List<String> objects) {
        super(context, R.layout.item_popup_lv, objects);
        initParams();
    }

    private void initParams() {
        this.resource = R.layout.item_popup_lv;
        this.normalBg = R.drawable.selector_lv_normal_bg;
        this.pressBg = R.color.colorChecked;
        this.selection = -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String s = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
            holder = new ViewHolder();
            holder.tv = convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(s);
        if (position == selection) {
            holder.tv.setTextColor(pressBg);
        } else {
            holder.tv.setBackgroundResource(normalBg);
        }
        return convertView;
    }

    public void setPressPosition(int position) {
        this.selection = position;
    }

    class ViewHolder {
        TextView tv;
    }
}
