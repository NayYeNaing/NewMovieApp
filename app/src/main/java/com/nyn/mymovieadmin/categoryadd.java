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

public class categoryadd extends DialogFragment {
    EditText categoryTitle;
    Button catOk,catCancel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cref;
    categoryModel cModel;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categoryadd,container,false);

        categoryTitle = view.findViewById(R.id.categoryName);
        catOk = view.findViewById(R.id.categoryOk);
        catCancel = view.findViewById(R.id.categoryCancel);

        if (cModel != null){
            categoryTitle.setText(cModel.categoryName);
        }

        catOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categoryTitle.getText().toString().equals("")){
                    Toasty.error(getContext(),"Input Something first",Toasty.LENGTH_SHORT,true).show();
                }
                else {
                    categoryModel categoryModel = new categoryModel();
                    categoryModel.categoryName = categoryTitle.getText().toString();
                    cref = db.collection("Category");
                    if (cModel != null){
                        cref.document(id).set(categoryModel);
                        Toasty.success(getContext(),"Updated",Toasty.LENGTH_SHORT,true).show();
                        cModel = null;
                        id = "";
                    }else {
                        cref.add(categoryModel);
                        Toasty.success(getContext(),"Saved",Toasty.LENGTH_SHORT,true).show();
                    }
                    categoryTitle.setText("");
                }
            }
        });

        catCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
