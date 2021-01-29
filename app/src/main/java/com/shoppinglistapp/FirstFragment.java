package com.shoppinglistapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FirstFragment extends Fragment {
    //holds all list items
    ArrayList trackedList = new ArrayList<ListItem>();
    ItemViewModel mItemViewModel;


    //creates and returns the view hierarchy associated with the fragment.
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment - ?create on the phone?
        View view = inflater.inflate(R.layout.fragment_first, container, false);


        //Inflate the Recycler view - create the list
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mItemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        //get items from database and if there are no item then add an empty one
        //trackedList = new ArrayList(mItemViewModel.getList());
        //initial data added
        if (trackedList.size() == 0) {
            trackedList.add(new ListItem("", false));
        }

        //creating the custom adapter with the one item
        final CustomAdapter customAdapter = new CustomAdapter(trackedList, getActivity(),
                mItemViewModel);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                customAdapter.updateList(new ArrayList(mItemViewModel.getList()));
            }
        });


        //set this adapter to the recycler view
        recyclerView.setAdapter(customAdapter);

        //define what kind of layout manager you want
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(customAdapter, recyclerView.getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //The plus button, searching for it and adding the click listener
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setFocusable(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackedList.add(new ListItem("", false));
                customAdapter.updateList(trackedList);
                //updates item on the screen
                customAdapter.notifyItemChanged(trackedList.size()-1);
                System.out.println("text");
            }
        });

        //pass it back to android to handle
        return view;
    }

    /*Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
    but before any saved state has been restored in to the view. This gives subclasses a chance to
    initialize themselves once they know their view hierarchy has been completely created.
    The fragment's view hierarchy is not however attached to its parent at this point*/

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}