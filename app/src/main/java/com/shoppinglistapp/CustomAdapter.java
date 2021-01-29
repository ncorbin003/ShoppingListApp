package com.shoppinglistapp;


import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<ListItem> searchArrayList;
    private ListItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private Activity mActivity;
    private ItemViewModel mItemViewModel;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param initialList Array list containing the data to populate views to be used
     *                    by RecyclerView.
     */
    public CustomAdapter(ArrayList<ListItem> initialList, Activity mActivity, ItemViewModel itemViewModel) {
        searchArrayList = initialList;
        this.mActivity = mActivity;
        mItemViewModel = itemViewModel;
    }


    public void deleteTask(int position) {
        mRecentlyDeletedItem = searchArrayList.get(position);
        mRecentlyDeletedItemPosition = position;
        searchArrayList.remove(position);

        mItemViewModel.addAll(searchArrayList);
        showUndoSnackbar();

    }

    private void showUndoSnackbar() {
        View view = mActivity.findViewById(R.id.constraint_layout);
        Snackbar snackbar = Snackbar.make(view, R.string.prompt,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo_text, v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        searchArrayList.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);

        mItemViewModel.addAll(searchArrayList);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     * <p>
     * Defines all the different pieces to be accessed by the adapter
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        //a checkbox and text is in each row
        private final CheckBox checkBox;
        private final EditText editText;

        //constructor
        public ViewHolder(final View view) {
            //calls the constructor in the parent in recycler view
            super(view);

            checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
            editText = (EditText) view.findViewById(R.id.editItemText);

            editText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // Perform action on key press
                        searchArrayList.add(new ListItem("", false));
                        editText.requestFocus();
                        //updates item on the screen
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                        System.out.println("enter");
                        return false;
                    }else {
                        ListItem localListItem = searchArrayList.get(getAdapterPosition());
                        localListItem.checkString = editText.getText().toString();
                        searchArrayList.set(getAdapterPosition(), localListItem);

                        mItemViewModel.addAll(searchArrayList);
                    }
                    return false;
                }
            }
            );
            //when you edit the text save it to data source

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ListItem savedItem = searchArrayList.get(getAdapterPosition());
                    savedItem.isChecked = isChecked;

                    //if the item is checked then it removes it from its current position and adds it
                    //to the end of the list. If it is not checked it sets the check to false.
                    if (isChecked) {
                        searchArrayList.remove(getAdapterPosition());
                        searchArrayList.add(savedItem);

                        mItemViewModel.addAll(searchArrayList);


                    } else {
                        searchArrayList.get(getAdapterPosition()).isChecked = false;
                        int lastUnchecked = 0;
                        searchArrayList.remove(getAdapterPosition());
                        //notifyItemRemoved(getAdapterPosition());
                        for (int i = 0; i < searchArrayList.size(); i++) {
                            if (!searchArrayList.get(i).isChecked) {
                                lastUnchecked = i;
                            }
                        }

                        searchArrayList.add(lastUnchecked, savedItem);

                        mItemViewModel.addAll(searchArrayList);



                    }
                    //forcing recycler view on UI thread to see the data set changed
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });


                }
            });

        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public EditText getEditText() {
            return editText;
        }
    }


    //when we press the + button
    public void updateList(ArrayList<ListItem> updatedList) {
        searchArrayList = updatedList;
//        mItemViewModel.deleteAll();
//        mItemViewModel.addAll(searchArrayList);
        //Triggers the list update
        //notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_layout_tv1, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    //takes the data from the data source and puts it onto the row
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getCheckBox().setChecked(searchArrayList.get(position).isChecked);

        //listener for when you click the checkbox

        //sets the text box to what the data source says
        viewHolder.getEditText().setText(searchArrayList.get(position).checkString);

        //change the color whether it is checked or not
        if (searchArrayList.get(position).isChecked) {
            viewHolder.getEditText().setTextColor(Color.GRAY);
        } else {
            viewHolder.getEditText().setTextColor(Color.BLACK);
        }

        int lastUncheckedItem = 0;
        for (int i = 0; i < searchArrayList.size(); i++) {
            if (!searchArrayList.get(i).isChecked) {
                lastUncheckedItem = i;
            }
        }

        if (position == lastUncheckedItem) {
        viewHolder.getEditText().setTextSize(20);
        viewHolder.getEditText().requestFocus();
        viewHolder.getEditText().setSelection(viewHolder.getEditText().length());}
        System.out.println("position: " + position + " length of text: " + viewHolder.getEditText().length());

    }


    // Return the size of your dataset (invoked by the layout manager)
    //used internally
    @Override
    public int getItemCount() {
        return searchArrayList.size();
    }


}

