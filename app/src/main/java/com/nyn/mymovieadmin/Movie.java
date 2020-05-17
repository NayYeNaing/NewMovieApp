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


public class Movie extends Fragment {
    RecyclerView movie_RV;
    ProgressBar movieProgressBar;
    EditText searchMovie;
    FloatingActionButton floatingActionButton;
    public static ArrayList<String> numberArrayList = new ArrayList<>();
    final ArrayList<movieModel> movieModelArrayList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference mref;

    public Movie() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie,container,false);

        movie_RV = view.findViewById(R.id.movie_RV);
        searchMovie = view.findViewById(R.id.searchmovies_et);
        movieProgressBar = view.findViewById(R.id.moviesprogressbar);
        floatingActionButton = view.findViewById(R.id.addmoviesbtn);

        searchMovie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = searchMovie.getText().toString();
                mref = db.collection("Movie");
                mref.orderBy("movieTitle").startAt(name).endAt(name+'\uf8ff').addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        ArrayList<movieModel> arrayList = new ArrayList<>();
                        arrayList.clear();
                        numberArrayList.clear();
                        for (DocumentSnapshot ds : queryDocumentSnapshots){
                            numberArrayList.add(ds.getId());
                            arrayList.add(ds.toObject(movieModel.class));
                        }
                        movieAdapter movieAdapter = new movieAdapter(arrayList,getContext());
                        LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                        movie_RV.setAdapter(movieAdapter);
                        movie_RV.setLayoutManager(lm);
                        movieProgressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loadData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieadd movieadd = new movieadd();
                movieadd.show(getFragmentManager(),"movieadd");
            }
        });

        return view;
    }

    public void loadData() {
        mref = db.collection("Movie");
        mref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                numberArrayList.clear();
                movieModelArrayList.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    numberArrayList.add(ds.getId());
                    movieModelArrayList.add(ds.toObject(movieModel.class));
                }
                movieAdapter movieAdapter = new movieAdapter(movieModelArrayList,getContext());
                LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                movie_RV.setAdapter(movieAdapter);
                movie_RV.setLayoutManager(lm);
                movieProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public class movieAdapter extends RecyclerView.Adapter<movieAdapter.movieHolder>{
        ArrayList<movieModel> arrayList = new ArrayList<>();
        Context context;

        public movieAdapter(ArrayList<movieModel> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public movieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.moviervstyle,parent,false);
            return new movieHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final movieHolder holder, final int position) {
            holder.moviesrno.setText(position+1+"");
            holder.movieTitle.setText(arrayList.get(position).movieTitle);
            Glide.with(getContext()).load(arrayList.get(position).movieImage).into(holder.movieImage);
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),holder.movieTitle);
                    popupMenu.getMenuInflater().inflate(R.menu.edit_delete,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.edit){
                                movieadd movieadd = new movieadd();
                                movieadd.mModel = arrayList.get(position);
                                movieadd.id = numberArrayList.get(position);
                                movieadd.show(getFragmentManager(),"edited");
                            }
                            if (menuItem.getItemId() == R.id.delete){
                                mref = db.collection("Movie");
                                mref.document(numberArrayList.get(position)).delete();
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

        public class movieHolder extends RecyclerView.ViewHolder{
            TextView moviesrno,movieTitle;
            ImageView movieImage;
            LinearLayout linearLayout;
            public movieHolder(@NonNull View itemView) {
                super(itemView);
                moviesrno = itemView.findViewById(R.id.moviesrno);
                movieTitle = itemView.findViewById(R.id.movietitleTV);
                movieImage = itemView.findViewById(R.id.movieimageIV);
                linearLayout = itemView.findViewById(R.id.movierv_layout);
            }
        }
    }
}
