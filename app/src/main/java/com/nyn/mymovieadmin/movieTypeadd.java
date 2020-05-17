package com.nyn.mymovieadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class movieTypeadd extends DialogFragment {
    EditText movieType;
    Button movieTypeOk,movieTypeCancel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference mtref;
    movieTypeModel mTModel;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movietypeadd,container,false);

        movieType = view.findViewById(R.id.movieType);
        movieTypeOk = view.findViewById(R.id.movieTypeOk);
        movieTypeCancel = view.findViewById(R.id.movieTypeCancel);

        if (mTModel != null){
            movieType.setText(mTModel.movieType);
        }

        movieTypeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movieType.getText().toString().equals("")){
                    Toasty.error(getContext(),"Input Something first",Toasty.LENGTH_SHORT,true).show();
                }
                else {
                    movieTypeModel movieTypeModel = new movieTypeModel();
                    movieTypeModel.movieType = movieType.getText().toString();
                    mtref = db.collection("Movie Type");
                    if (mTModel != null){
                        mtref.document(id).set(movieTypeModel);
                        Toasty.success(getContext(),"Updated",Toasty.LENGTH_SHORT,true).show();
                        mTModel = null;
                        id = "";
                    }else {
                        mtref.add(movieTypeModel);
                        Toasty.success(getContext(),"Saved",Toasty.LENGTH_SHORT,true).show();
                    }
                    movieType.setText("");
                }
            }
        });

        movieTypeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
