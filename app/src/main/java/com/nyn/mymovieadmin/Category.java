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


public class Category extends Fragment {
    ListView catListView;
    ProgressBar catProgressbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cref;
    FloatingActionButton floatingActionButton;
    ArrayList<categoryModel> categoryModelArrayList = new ArrayList<>();
    public static ArrayList<String> docID = new ArrayList<>();

    public Category() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category,container,false);

        catListView = view.findViewById(R.id.categoryLV);
        catProgressbar = view.findViewById(R.id.catProgressbar);
        floatingActionButton = view.findViewById(R.id.addcatbtn);

        loadData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryadd categoryadd = new categoryadd();
                categoryadd.show(getFragmentManager(),"catadd");
            }
        });

        return view;
    }

    private void loadData() {
        cref = db.collection("Category");
        cref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                docID.clear();
                categoryModelArrayList.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    docID.add(ds.getId());
                    categoryModelArrayList.add(ds.toObject(categoryModel.class));
                }
                catAdapter catAdapter = new catAdapter(categoryModelArrayList);
                catListView.setAdapter(catAdapter);
                catProgressbar.setVisibility(View.GONE);
            }
        });
    }

    public class catAdapter extends BaseAdapter{
        ArrayList<categoryModel> arrayList = new ArrayList<>();

        public catAdapter(ArrayList<categoryModel> arrayList) {
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
            LayoutInflater inflater = getLayoutInflater();
            View view1 = inflater.inflate(R.layout.listviewstyle,null);

            final TextView catNo,catTitle;
            LinearLayout linearLayout;

            catNo = view1.findViewById(R.id.list_No);
            catTitle = view1.findViewById(R.id.list_Text);
            linearLayout = view1.findViewById(R.id.list_layout);

            catNo.setText(i+1+"");
            catTitle.setText(arrayList.get(i).categoryName);
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),catTitle);
                    popupMenu.getMenuInflater().inflate(R.menu.edit_delete,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.edit){
                                categoryadd categoryadd = new categoryadd();
                                categoryadd.cModel = arrayList.get(i);
                                categoryadd.id = docID.get(i);
                                categoryadd.show(getFragmentManager(),"edited");
                            }
                            if (menuItem.getItemId() == R.id.delete){
                                cref = db.collection("Category");
                                cref.document(docID.get(i)).delete();
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
