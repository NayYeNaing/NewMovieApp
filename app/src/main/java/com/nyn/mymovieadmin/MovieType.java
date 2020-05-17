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


public class MovieType extends Fragment {
    ListView MTLV;
    ProgressBar MTprogressbar;
    FloatingActionButton floatingActionButton;
    ArrayList<movieTypeModel> movieTypeModelArrayList = new ArrayList<>();
    static ArrayList<String> docID = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference mtref;

    public MovieType() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_type,container,false);

        MTLV = view.findViewById(R.id.MTLV);
        MTprogressbar = view.findViewById(R.id.MTprogressbar);
        floatingActionButton = view.findViewById(R.id.addMTbtn);

        loadData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieTypeadd movieTypeadd = new movieTypeadd();
                movieTypeadd.show(getFragmentManager(),"movieTypeAdd");
            }
        });

        return view;
    }

    private void loadData() {
        mtref = db.collection("Movie Type");
        mtref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                docID.clear();
                movieTypeModelArrayList.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots){
                    docID.add(ds.getId());
                    movieTypeModelArrayList.add(ds.toObject(movieTypeModel.class));
                    movieTypeAdapter movieTypeAdapter = new movieTypeAdapter(movieTypeModelArrayList);
                    MTLV.setAdapter(movieTypeAdapter);
                    MTprogressbar.setVisibility(View.GONE);
                }

            }
        });
    }

    public class movieTypeAdapter extends BaseAdapter{
        ArrayList<movieTypeModel> arrayList = new ArrayList<>();

        public movieTypeAdapter(ArrayList<movieTypeModel> arrayList) {
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
            final TextView MTno,MTtext;
            LinearLayout linearLayout;
            LayoutInflater inflater = getLayoutInflater();
            View view1 = inflater.inflate(R.layout.listviewstyle,null);

            linearLayout = view1.findViewById(R.id.list_layout);
            MTno = view1.findViewById(R.id.list_No);
            MTtext = view1.findViewById(R.id.list_Text);

            MTno.setText(i+1+"");
            MTtext.setText(arrayList.get(i).movieType);
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),MTtext);
                    popupMenu.getMenuInflater().inflate(R.menu.edit_delete,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.edit){
                                movieTypeadd movieTypeadd = new movieTypeadd();
                                movieTypeadd.mTModel = arrayList.get(i);
                                movieTypeadd.id = docID.get(i);
                                movieTypeadd.show(getFragmentManager(),"Edited");
                            }
                            if (menuItem.getItemId() == R.id.delete){
                                mtref = db.collection("Movie Type");
                                mtref.document(docID.get(i)).delete();
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
