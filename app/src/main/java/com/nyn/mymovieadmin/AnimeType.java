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

import es.dmoral.toasty.Toasty;

public class AnimeType extends Fragment {
    ListView ATLV;
    ProgressBar ATprogressbar;
    FloatingActionButton floatingActionButton;
    ArrayList<animeTypeModel> animeTypeModelArrayList = new ArrayList<>();
    static ArrayList<String> docID = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference atref;

    public AnimeType() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anime_type,container,false);

        ATLV = view.findViewById(R.id.ATLV);
        ATprogressbar = view.findViewById(R.id.ATprogressbar);
        floatingActionButton = view.findViewById(R.id.addATbtn);

        loadData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animeTypeadd animeTypeadd = new animeTypeadd();
                animeTypeadd.show(getFragmentManager(),"animeTypeAdd");
            }
        });

        return view;
    }

    private void loadData() {
        atref = db.collection("Anime Type");
        atref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                docID.clear();
                animeTypeModelArrayList.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots){
                    docID.add(ds.getId());
                    animeTypeModelArrayList.add(ds.toObject(animeTypeModel.class));
                }
                animeTypeAdapter animeTypeAdapter = new animeTypeAdapter(animeTypeModelArrayList);
                ATLV.setAdapter(animeTypeAdapter);
                ATprogressbar.setVisibility(View.GONE);
            }
        });
    }

    public class animeTypeAdapter extends BaseAdapter{
        ArrayList<animeTypeModel> arrayList = new ArrayList<>();

        public animeTypeAdapter(ArrayList<animeTypeModel> arrayList) {
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
            final TextView ATno,ATtext;
            LinearLayout linearLayout;
            LayoutInflater inflater = getLayoutInflater();
            View view1 = inflater.inflate(R.layout.listviewstyle,null);

            ATno = view1.findViewById(R.id.list_No);
            ATtext = view1.findViewById(R.id.list_Text);
            linearLayout = view1.findViewById(R.id.list_layout);

            ATno.setText(i+1+"");
            ATtext.setText(arrayList.get(i).animeType);
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),ATtext);
                    popupMenu.getMenuInflater().inflate(R.menu.edit_delete,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.edit){
                                animeTypeadd animeTypeadd = new animeTypeadd();
                                animeTypeadd.aTModel = animeTypeModelArrayList.get(i);
                                animeTypeadd.id = docID.get(i);
                                animeTypeadd.show(getFragmentManager(),"Edited");
                            }
                            if (menuItem.getItemId() == R.id.delete){
                                atref = db.collection("Anime Type");
                                atref.document(docID.get(i)).delete();
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
