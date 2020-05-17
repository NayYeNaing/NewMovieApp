package com.nyn.mymovieadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class movieadd extends DialogFragment {
    EditText movieTitle,movieImage,moviVideo;
    Spinner movieCategory,movieTypeSpinner;
    Button movieOk,movieCancel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference mref,cref,mtref;
    movieModel mModel;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movieadd,container,false);

        final ArrayList<String> movieCategoryArrayList = new ArrayList<>();
        final ArrayList<String> movieTypeArrayList = new ArrayList<>();
        movieTitle = view.findViewById(R.id.movieTitle);
        movieImage = view.findViewById(R.id.movieImage);
        moviVideo = view.findViewById(R.id.movieVideo);
        movieCategory = view.findViewById(R.id.movieCategory);
        movieTypeSpinner = view.findViewById(R.id.movieTypeSpinner);
        movieOk = view.findViewById(R.id.movieOk);
        movieCancel = view.findViewById(R.id.movieCancel);

        if (mModel != null){
            movieTitle.setText(mModel.movieTitle);
            movieImage.setText(mModel.movieImage);
            moviVideo.setText(mModel.movieVideo);
        }

        cref = db.collection("Category");
        cref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                movieCategoryArrayList.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    movieCategoryArrayList.add(ds.toObject(categoryModel.class).categoryName);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,movieCategoryArrayList);
                movieCategory.setAdapter(arrayAdapter);
            }
        });

        mtref = db.collection("Movie Type");
        mtref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                movieTypeArrayList.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    movieTypeArrayList.add(ds.toObject(movieTypeModel.class).movieType);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,movieTypeArrayList);
                movieTypeSpinner.setAdapter(arrayAdapter);
            }
        });

        movieOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movieTitle.getText().toString().equals("")||movieImage.getText().toString().equals("")||moviVideo.getText().toString().equals("")){
                    Toasty.error(getContext(),"Input Something first",Toasty.LENGTH_SHORT,true).show();
                }
                else {
                    movieModel movieModel = new movieModel();
                    movieModel.movieTitle = movieTitle.getText().toString();
                    movieModel.movieImage = movieImage.getText().toString();
                    movieModel.movieVideo = moviVideo.getText().toString();
                    movieModel.movieCategory = movieCategoryArrayList.get(movieCategory.getSelectedItemPosition());
                    movieModel.movieType = movieTypeArrayList.get(movieTypeSpinner.getSelectedItemPosition());
                    mref = db.collection("Movie");
                    if (mModel != null){
                        mref.document(id).set(movieModel);
                        Toasty.success(getContext(),"Updated",Toasty.LENGTH_SHORT,true).show();
                        mModel = null;
                        id = null;
                    }else {
                        mref.add(movieModel);
                        Toasty.success(getContext(),"Saved",Toasty.LENGTH_SHORT,true).show();
                    }
                    movieTitle.setText("");
                    movieImage.setText("");
                    moviVideo.setText("");
                }
            }
        });

        movieCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
