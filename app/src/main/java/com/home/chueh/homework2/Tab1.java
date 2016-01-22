package com.home.chueh.homework2;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class Tab1 extends Fragment{
    OnTab1Activity mCallback;
    public interface OnTab1Activity {
        public void tab1Activity(String str);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTab1Activity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTab1Activity");
        }
    }

    private ArrayList<String> Tab1List = new ArrayList<String>();

    private ArrayAdapter<String> Tab1Adapter;
    private Button addButton;

    private View v;
    private EditText editBox;

    public ArrayList<String> getItemList(){
        return Tab1List;
    }

    public void setItemList(String[] list){
        Tab1List.clear();
        Tab1List.addAll(Arrays.asList(list));
        notifyUpdate();
    }
    public void clrItemList(){
        Tab1List.clear();
        notifyUpdate();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.tab_1,container,false);

        Tab1Adapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                R.layout.tab_1_listlayout,   // custom layout for the list
                R.id.Tab1_text,                // ID of TextView item within the list layout
                Tab1List
        );

        ListView Tab1LV = (ListView)v.findViewById(R.id.Tab1_listView);
        Tab1LV.setAdapter(Tab1Adapter);
        Tab1LV.setEnabled(true);
        addButton = (Button)v.findViewById(R.id.addButton);
        editBox = (EditText)v.findViewById(R.id.editBox);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addItem(editBox.getText().toString());
                editBox.setText("");
                notifyUpdate();
            }
        });
        /*
        addButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clrItemList();
                return false;
            }
        });

        */

        Tab1LV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("INFO",String.format("%d, %d",position,id));
                showPopup(position);
                return false;
            }
        });



        return v;
    }

    public void notifyUpdate(){
        Log.i("INFO", "tab1Notify");
        if (Tab1Adapter!=null) Tab1Adapter.notifyDataSetChanged();
    }
    public void addItem(String newStr){
        Log.i("INFO", "tab1 additem");
        if (!newStr.equals("") && Tab1List != null) Tab1List.add(newStr);
    }
    public void removeItem(String newStr){
        if (Tab1List != null) Tab1List.remove(newStr);
    }
    public void removeItem(int pos){
        if (Tab1List != null) Tab1List.remove(pos);
    }

    public void completeItem(String newStr){
        if (Tab1List != null){
            mCallback.tab1Activity(newStr);
            Tab1List.remove(newStr);
        }
    }
    public void completeItem(int pos){
        String str = Tab1List.get(pos);
        if (Tab1List != null){
            mCallback.tab1Activity(str);
            Tab1List.remove(str);
        }
    }

    private void confirmDelPopup(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.confirmdel_layout);
        dialog.show();
        Button yesB = (Button)dialog.findViewById(R.id.yesButton);
        Button noB = (Button)dialog.findViewById(R.id.noButton);

        yesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clrItemList();
                dialog.hide();
            }
        });

        noB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });


    }
    private void showPopup(final int pos){

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog1_layout);
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
                                    // mark complete
                    completeItem(pos);
                    notifyUpdate();
                }
                else if (position == 2){
                    //clear all items
                    dialog.hide();
                    confirmDelPopup();
                }
                else if (position == 3){
                                    // back
                }
                dialog.hide();


            }
        });

    }


}