package com.nyn.mymovieadmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class DramaTitle extends Fragment {
    RecyclerView DTRV;
    ProgressBar DTprogressbar;
    FloatingActionButton floatingActionButton;
    ArrayList<dramaTitleModel> dramaTitleModelArrayList = new ArrayList<>();
    static ArrayList<String> docID = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dtitleref;

    public DramaTitle() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drama_title,container,false);

        DTRV = view.findViewById(R.id.DTitleRV);
        DTprogressbar = view.findViewById(R.id.DTitleprogressbar);
        floatingActionButton = view.findViewById(R.id.addDTbtn);

        loadData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dramaTitleadd dramaTitleadd = new dramaTitleadd();
                dramaTitleadd.show(getFragmentManager(),"dramTitleAdd");
            }
        });

        return view;
    }

    private void loadData() {
        dtitleref = db.collection("Drama Title");
        dtitleref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                docID.clear();
                dramaTitleModelArrayList.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots){
                    docID.add(ds.getId());
                    dramaTitleModelArrayList.add(ds.toObject(dramaTitleModel.class));
                }
                dramaTitleAdapter adapter = new dramaTitleAdapter(dramaTitleModelArrayList);
                LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                DTRV.setAdapter(adapter);
                DTRV.setLayoutManager(lm);
                DTprogressbar.setVisibility(View.GONE);
            }
        });
    }

    public class dramaTitleAdapter extends RecyclerView.Adapter<dramaTitleAdapter.dramaTitleHolder>{
        ArrayList<dramaTitleModel> arrayList = new ArrayList<>();

        public dramaTitleAdapter(ArrayList<dramaTitleModel> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public dramaTitleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.dramatitlervstyle,parent,false);
            dramaTitleHolder dramaTitleHolder = new dramaTitleHolder(view);
            return dramaTitleHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final dramaTitleHolder holder, final int position) {
            holder.DTno.setText(position+1+"");
            holder.DTtext.setText(arrayList.get(position).dramaTitle);
            Glide.with(getContext()).load(arrayList.get(position).dramaImage).into(holder.DTimage);
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),holder.DTtext);
                    popupMenu.getMenuInflater().inflate(R.menu.edit_delete,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(menuItem.getItemId() == R.id.edit){
                                dramaTitleadd dramaTitleadd = new dramaTitleadd();
                                dramaTitleadd.dTModel = arrayList.get(position);
                                dramaTitleadd.id = docID.get(position);
                                dramaTitleadd.show(getFragmentManager(),"edited");
                            }
                            if(menuItem.getItemId() == R.id.delete){
                                dtitleref = db.collection("Drama Title");
                                dtitleref.document(DramaTitle.docID.get(position)).delete();
                                loadData();
                            }
                            return true;
                        }
                    });
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class dramaTitleHolder extends RecyclerView.ViewHolder{
            TextView DTno,DTtext;
            ImageView DTimage;
            LinearLayout linearLayout;
            public dramaTitleHolder(@NonNull View itemView) {
                super(itemView);
                DTno = itemView.findViewById(R.id.dramaTitlesrno);
                DTtext = itemView.findViewById(R.id.dramaTitletitleTV);
                DTimage = itemView.findViewById(R.id.dramaTitleimageIV);
                linearLayout = itemView.findViewById(R.id.DTrv_layout);
            }
        }
    }
}
