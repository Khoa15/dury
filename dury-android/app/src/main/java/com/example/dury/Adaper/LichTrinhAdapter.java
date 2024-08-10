package com.example.dury.Adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dury.R;

import java.util.List;

public class LichTrinhAdapter extends BaseAdapter {
    private Context mContext;
    private List<Object> mDataList; // Đổi YourDataModel thành kiểu dữ liệu mà bạn muốn hiển thị

    // Constructor
    public LichTrinhAdapter(Context context, List<Object> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewItem = convertView;
        if (gridViewItem == null) {
            // Nếu convertView không được sử dụng, inflate layout cho mỗi item
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridViewItem = inflater.inflate(R.layout.grid_item_layout, null);
        }

//        TextView textView1 = gridViewItem.findViewById(R.id.textView4);
//        TextView textView2 = gridViewItem.findViewById(R.id.textView5);
//        TextView textView3 = gridViewItem.findViewById(R.id.textView6);
//
//        // Đặt dữ liệu vào các thành phần
//        FolderModel_ver2 dataModel = mDataList.get(position);
//
//        textView1.setText(dataModel.getId());
//        textView2.setText(dataModel.getFolderName());
//        textView3.setText(dataModel.getYear());

        return gridViewItem;
    }
}
