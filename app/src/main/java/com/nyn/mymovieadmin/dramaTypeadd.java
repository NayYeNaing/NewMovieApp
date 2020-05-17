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

public class dramaTypeadd extends DialogFragment {
    EditText dramaType;
    Button dramaTypeOK,dramaTypeCancel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dtref;
    dramaTypeModel dTypeModel;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dramatypeadd,container,false);

        dramaType = view.findViewById(R.id.dramaType);
        dramaTypeOK = view.findViewById(R.id.dramaTypeOk);
        dramaTypeCancel = view.findViewById(R.id.dramaTypeCancel);

        if (dTypeModel != null){
            dramaType.setText(dTypeModel.dramaType);
        }

        dramaTypeOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dramaType.getText().toString().equals("")){
                    Toasty.error(getContext(),"Input Something first",Toasty.LENGTH_SHORT,true).show();
                }
                else {
                    dramaTypeModel dramaTypeModel = new dramaTypeModel();
                    dramaTypeModel.dramaType = dramaType.getText().toString();
                    dtref = db.collection("Drama Type");
                    if (dTypeModel != null){
                        dtref.document(id).set(dramaTypeModel);
                        Toasty.success(getContext(),"Updated",Toasty.LENGTH_SHORT,true).show();
                        dTypeModel = null;
                        id = "";
                    }else {
                        dtref.add(dramaTypeModel);
                        Toasty.success(getContext(),"Saved",Toasty.LENGTH_SHORT,true).show();
                    }
                    dramaType.setText("");
                }
            }
        });

        dramaTypeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
