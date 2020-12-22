package com.shoppinglistapp;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<ListItem> searchArrayList;


    public ListAdapter(Context context, ArrayList<ListItem> initalList) {
        searchArrayList = initalList;
        mInflater = LayoutInflater.from(context);
    }

    public void updateList(ArrayList<ListItem> updatedList){
        searchArrayList = updatedList;
        //Triggers the list update
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return searchArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int index = position;
        //Init a new view that hasn't existed before
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_layout_tv1, parent, false);
        }

        //Find the Text view layout. Then set the text, color, and size.
        CheckBox checkBox = convertView.findViewById(R.id.checkBox1);
        checkBox.setChecked(searchArrayList.get(index).isChecked);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ListItem savedItem = searchArrayList.get(index);
                savedItem.isChecked = isChecked;

                if (isChecked) {
                    searchArrayList.remove(index);
                    searchArrayList.add(savedItem);

                }
                notifyDataSetChanged();

            }
        });

        EditText editText = convertView.findViewById(R.id.editItemText);

        editText.setText(searchArrayList.get(position).checkString);
        if (searchArrayList.get(index).isChecked) {
            editText.setTextColor(Color.GRAY);
        } else {
            editText.setTextColor(Color.BLACK);
        }

        editText.setTextSize(20);
        editText.requestFocus();
        editText.setSelection(editText.length());

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here

                // yourEditText...
                //updated item
                ListItem localListItem = searchArrayList.get(index);
                localListItem.checkString = s.toString();
                searchArrayList.set(index, localListItem);
                //notifyDataSetChanged();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        return convertView;
    }
}
