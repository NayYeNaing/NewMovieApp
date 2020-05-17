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

import static com.nyn.mymovieadmin.Movie.numberArrayList;

public class Anime extends Fragment {
    RecyclerView animeRV;
    ProgressBar animeProgressbar;
    FloatingActionButton floatingActionButton;
    public  static ArrayList<String> docID = new ArrayList<>();
    final ArrayList<animeModel> animeModelArrayList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference aref;

    public Anime() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anime,container,false);

        animeRV = view.findViewById(R.id.anime_RV);
        animeProgressbar = view.findViewById(R.id.animeProgressbar);
        floatingActionButton = view.findViewById(R.id.addanimebtn);

        loadData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animeadd animeadd = new animeadd();
                animeadd.show(getFragmentManager(),"animeadd");
            }
        });

        return view;
    }

    public void loadData() {
        aref = db.collection("Anime");
        aref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                docID.clear();
                animeModelArrayList.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots){
                    docID.add(ds.getId());
                    animeModelArrayList.add(ds.toObject(animeModel.class));
                }
                animeAdapter animeAdapter = new animeAdapter(animeModelArrayList);
                LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                animeRV.setAdapter(animeAdapter);
                animeRV.setLayoutManager(lm);
                animeProgressbar.setVisibility(View.GONE);
            }
        });
    }

    public  class animeAdapter extends RecyclerView.Adapter<animeAdapter.animeHolder>{
        ArrayList<animeModel> arrayList = new ArrayList<>();

        public animeAdapter(ArrayList<animeModel> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public animeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.animervstyle,parent,false);
            return new animeHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final animeHolder holder, final int position) {
            holder.animeSrno.setText(position+1+"");
            holder.animeTitle.setText(arrayList.get(position).animeTitle);
            Glide.with(getContext()).load(arrayList.get(position).animeImage).into(holder.animeImage);
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),holder.animeTitle);
                    popupMenu.getMenuInflater().inflate(R.menu.edit_delete,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.edit){
                                animeadd animeadd = new animeadd();
                                animeadd.aModel = arrayList.get(position);
                                animeadd.id = numberArrayList.get(position);
                                animeadd.show(getFragmentManager(),"edited");
                            }
                            if (menuItem.getItemId() == R.id.delete){
                                aref = db.collection("Anime");
                                aref.document(docID.get(position)).delete();
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

        public class animeHolder extends RecyclerView.ViewHolder{
            TextView animeSrno,animeTitle;
            ImageView animeImage;
            LinearLayout linearLayout;
            public animeHolder(@NonNull View itemView) {
                super(itemView);
                animeSrno = itemView.findViewById(R.id.animeSrno);
                animeTitle = itemView.findViewById(R.id.animetitleTV);
                animeImage = itemView.findViewById(R.id.animeimageIV);
                linearLayout= itemView.findViewById(R.id.animerv_layout);
            }
        }
    }
}
