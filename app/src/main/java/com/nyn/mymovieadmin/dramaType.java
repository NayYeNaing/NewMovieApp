package com.nyn.mymovieadmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;

public class dramaType extends Fragment {
    ListView DTlistView;
    ProgressBar DTprogressbar;
    FloatingActionButton floatingActionButton;
    ArrayList<dramaTypeModel> dramaTypeModelArrayList = new ArrayList<>();
    static ArrayList<String> docID = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dtref;

    public dramaType() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drama_type,container,false);

        DTlistView = view.findViewById(R.id.DTLV);
        DTprogressbar = view.findViewById(R.id.DTprogressbar);
        floatingActionButton = view.findViewById(R.id.addDTbtn);

        loadData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dramaTypeadd dramaTypeadd = new dramaTypeadd();
                dramaTypeadd.show(getFragmentManager(),"dramaTypeadd");
            }
        });

        return view;
    }

    private void loadData() {
        dtref = db.collection("Drama Type");
        dtref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                docID.clear();
                dramaTypeModelArrayList.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots){
                    docID.add(ds.getId());
                    dramaTypeModelArrayList.add(ds.toObject(dramaTypeModel.class));
                }
                DTAdapter dtAdapter = new DTAdapter(dramaTypeModelArrayList);
                DTlistView.setAdapter(dtAdapter);
                DTprogressbar.setVisibility(View.GONE);
            }
        });
    }
    public  class  DTAdapter extends BaseAdapter{
        ArrayList<dramaTypeModel> arrayList = new ArrayList<>();

        public DTAdapter(ArrayList<dramaTypeModel> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final TextView dtNo,dtText;
            LinearLayout linearLayout;
            LayoutInflater inflater = getLayoutInflater();
            View view1 = inflater.inflate(R.layout.listviewstyle,null);

            dtNo = view1.findViewById(R.id.list_No);
            dtText = view1.findViewById(R.id.list_Text);
            linearLayout = view1.findViewById(R.id.list_layout);

            dtNo.setText(i+1+"");
            dtText.setText(arrayList.get(i).dramaType);
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),dtText);
                    popupMenu.getMenuInflater().inflate(R.menu.edit_delete,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.edit){
                                dramaTypeadd dramaTypeadd = new dramaTypeadd();
                                dramaTypeadd.dTypeModel = arrayList.get(i);
                                dramaTypeadd.id = docID.get(i);
                                dramaTypeadd.show(getFragmentManager(),"edited");
                            }
                            if (menuItem.getItemId() == R.id.delete){
                                dtref = db.collection("Drama Type");
                                dtref.document(docID.get(i)).delete();
                                loadData();
                            }
                            return true;
                        }
                    });
                    return true;
                }
            });

            return view1;
        }
    }
}
