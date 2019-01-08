package com.example.administrator.dk;

import android.content.Context;


import java.util.List;

/**
 * Created by zzx on 2017/12/20 0020.
 */

public class ConsumeTypeAdapter extends CommonAdapter<String> {
    private List<String> consumeList;

    public ConsumeTypeAdapter(Context context, List<String> datas) {
        super(context, datas, R.layout.adapter_comsume_type);
        consumeList = datas;
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.tvConsumeType, s);
    }
}
