package com.nyn.mymovieadmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class drama extends Fragment {
    RecyclerView drama_RV;
    EditText serachDrama;
    ProgressBar dramaProgressBar;
    FloatingActionButton floatingActionButton;
    public static ArrayList<String> NumberArrayList = new ArrayList<>();
    final ArrayList<dramaModel> dramaModelArrayList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static CollectionReference dref;

    public drama() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drama,container,false);

        drama_RV = view.findViewById(R.id.drama_RV);
        serachDrama = view.findViewById(R.id.searchdrama_et);
        dramaProgressBar = view.findViewById(R.id.dramaprogressbar);
        floatingActionButton = view.findViewById(R.id.adddramabtn);

        loadData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dramaadd dramaadd = new dramaadd();
                dramaadd.show(getFragmentManager(),"dramaadd");
            }
        });

        serachDrama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = serachDrama.getText().toString();
                dref = db.collection("Drama");
                dref.orderBy("dramaTitle").startAt(name).endAt(name+'\uf8ff').addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        ArrayList<dramaModel> arrayList = new ArrayList<>();
                        arrayList.clear();
                        for (DocumentSnapshot ds : queryDocumentSnapshots){
                            arrayList.add(ds.toObject(dramaModel.class));
                        }
                        dramaAdapter dramaAdapter = new dramaAdapter(arrayList);
                        LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                        drama_RV.setAdapter(dramaAdapter);
                        drama_RV.setLayoutManager(lm);
                        dramaProgressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    public void loadData() {
        dref = db.collection("Drama");
        dref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                NumberArrayList.clear();
                dramaModelArrayList.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    NumberArrayList.add(ds.getId());
                    dramaModelArrayList.add(ds.toObject(dramaModel.class));
                }
                dramaAdapter dramaAdapter = new dramaAdapter(dramaModelArrayList);
                LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                drama_RV.setAdapter(dramaAdapter);
                drama_RV.setLayoutManager(lm);
                dramaProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public class dramaAdapter extends RecyclerView.Adapter<dramaAdapter.dramaHolder>{
        ArrayList<dramaModel> arrayList = new ArrayList<>();

        public dramaAdapter(ArrayList<dramaModel> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public dramaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.rvstyle,parent,false);
            dramaHolder dramaHolder = new dramaHolder(view);
            return dramaHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final dramaHolder holder, final int position) {
            holder.number.setText(position+1+"");
            holder.title.setText(arrayList.get(position).dramaTitle);
            Glide.with(getContext()).load(arrayList.get(position).dramaImage).into(holder.image);
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),holder.title);
                    popupMenu.getMenuInflater().inflate(R.menu.edit_delete,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(menuItem.getItemId() == R.id.edit){
                                dramaadd dramaadd = new dramaadd();
                                dramaadd.dModel = arrayList.get(position);
                                dramaadd.id = NumberArrayList.get(position);
                                dramaadd.show(getFragmentManager(),"Edit");
                            }
                            if(menuItem.getItemId() == R.id.delete){
                                dref = db.collection("Drama");
                                dref.document(drama.NumberArrayList.get(position)).delete();
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

        public class dramaHolder extends RecyclerView.ViewHolder{
            TextView number,title;
            ImageView image;
            LinearLayout linearLayout;
            public dramaHolder(@NonNull View itemView) {
                super(itemView);
                number = itemView.findViewById(R.id.no);
                title = itemView.findViewById(R.id.titleRV);
                image = itemView.findViewById(R.id.imageRV);
                linearLayout = itemView.findViewById(R.id.rv_layout);
            }
        }
    }
}
