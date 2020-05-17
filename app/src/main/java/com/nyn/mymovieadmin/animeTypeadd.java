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

public class animeTypeadd extends DialogFragment {
    EditText animeType;
    Button animeTypeOK,animeTypeCancel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference atref;
    animeTypeModel aTModel;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.animetypeadd,container,false);

        animeType = view.findViewById(R.id.animeType);
        animeTypeOK = view.findViewById(R.id.animeTypeOk);
        animeTypeCancel = view.findViewById(R.id.animeTypeCancel);

        if (aTModel != null){
            animeType.setText(aTModel.animeType);
        }

        animeTypeOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(animeType.getText().toString().equals("")){
                    Toasty.error(getContext(),"Input Something first",Toasty.LENGTH_SHORT,true).show();
                }
                else {
                    animeTypeModel animeTypeModel = new animeTypeModel();
                    animeTypeModel.animeType = animeType.getText().toString();
                    atref = db.collection("Anime Type");
                    if (aTModel != null){
                        atref.document(id).set(animeTypeModel);
                        Toasty.success(getContext(),"Updated",Toasty.LENGTH_SHORT,true).show();
                        aTModel = null;
                        id = "";
                    }else {
                        atref.add(animeTypeModel);
                        Toasty.success(getContext(),"Saved",Toasty.LENGTH_SHORT,true).show();
                    }
                    animeType.setText("");
                }
            }
        });

        animeTypeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
