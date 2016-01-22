package com.home.chueh.homework2;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class Tab2 extends Fragment {
    OnTab2Activity mCallback;
    public interface OnTab2Activity {
        public void tab2Activity(String str);
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTab2Activity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTab2Activity");
        }
    }
    private View v;

    private ArrayList<String> Tab2List = new ArrayList<String>();
    private ArrayAdapter<String> Tab2Adapter;

    private Button clrButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.tab_2,container,false);

        Tab2Adapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                R.layout.tab_2_listlayout,   // custom layout for the list
                R.id.Tab2_text,                // ID of TextView item within the list layout
                Tab2List
        );
        ListView Tab2LV = (ListView)v.findViewById(R.id.Tab2_listView);
        Tab2LV.setAdapter(Tab2Adapter);

        clrButton = (Button)v.findViewById(R.id.clrButton);


        clrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tab2List.clear();
                notifyUpdate();
            }
        });

        Tab2LV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("INFO", String.format("%d, %d", position, id));
                showPopup(position);
                return false;
            }
        });



        return v;
    }
    public ArrayList<String> getItemList(){
        return Tab2List;
    }

    public void setItemList(String[] list){
        Tab2List.clear();
        Tab2List.addAll(Arrays.asList(list));
    }

    public void notifyUpdate(){
        Log.i("INFO", "tab2 notify");
        if (Tab2Adapter!=null) Tab2Adapter.notifyDataSetChanged();
    }
    public void addItem(String newStr){
        Log.i("INFO", "tab2 additem");
        if (!newStr.equals("") && Tab2List != null) Tab2List.add(newStr);
    }
    public void removeItem(String newStr){
        if (Tab2List != null) Tab2List.remove(newStr);
    }
    public void removeItem(int pos){
        if (Tab2List != null) Tab2List.remove(pos);
    }

    public void uncompleteItem(String newStr){
        if (Tab2List != null){
            mCallback.tab2Activity(newStr);
            Tab2List.remove(newStr);
        }
    }
    public void uncompleteItem(int pos){
        String str = Tab2List.get(pos);
        if (Tab2List != null){
            mCallback.tab2Activity(str);
            Tab2List.remove(str);
        }
    }
    private void showPopup(final int pos){

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog2_layout);
        dialog.show();
        ListView v = (ListView)dialog.findViewById(R.id.dialogList);
        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    //delete
                    removeItem(pos);
                    notifyUpdate();
                }
                else if (position == 1){
                    // mark uncomplete
                    uncompleteItem(pos);
                    notifyUpdate();
                }
                else if (position == 2){

                }
                dialog.hide();


            }
        });

    }
}