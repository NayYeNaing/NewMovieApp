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

public class dramaTitleadd extends DialogFragment {
    EditText dramaTitleTitle,dramaTitleImage;
    Spinner dramaTitleCategory;
    Button dramaTitleOk,dramaTitleCancel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dTref,cref;
    dramaTitleModel dTModel;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dramatitleadd,container,false);

        final ArrayList<String> dramaTitleCategoryAL = new ArrayList<>();
        dramaTitleTitle = view.findViewById(R.id.dramaTitleTitle);
        dramaTitleImage = view.findViewById(R.id.dramaTitleImage);
        dramaTitleCategory = view.findViewById(R.id.dramaTitleCategory);
        dramaTitleOk = view.findViewById(R.id.dramaTitleOk);
        dramaTitleCancel = view.findViewById(R.id.dramaTitleCancel);

        if(dTModel != null){
            dramaTitleTitle.setText(dTModel.dramaTitle);
            dramaTitleImage.setText(dTModel.dramaImage);
        }

        cref = db.collection("Category");
        cref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dramaTitleCategoryAL.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    dramaTitleCategoryAL.add(ds.toObject(categoryModel.class).categoryName);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,dramaTitleCategoryAL);
                dramaTitleCategory.setAdapter(arrayAdapter);
            }
        });

        dramaTitleOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dramaTitleTitle.getText().toString().equals("")||dramaTitleImage.getText().toString().equals("")){
                    Toasty.error(getContext(),"Input Something first",Toasty.LENGTH_SHORT,true).show();
                }
                else {
                    dramaTitleModel dramaTitleModel = new dramaTitleModel();
                    dramaTitleModel.dramaTitle = dramaTitleTitle.getText().toString();
                    dramaTitleModel.dramaImage = dramaTitleImage.getText().toString();
                    dramaTitleModel.dramaCategory = dramaTitleCategoryAL.get(dramaTitleCategory.getSelectedItemPosition());
                    dTref = db.collection("Drama Title");
                    if (dTModel != null){
                        dTref.document(id).set(dramaTitleModel);
                        Toasty.success(getContext(),"Updated",Toasty.LENGTH_SHORT,true).show();
                        dTModel = null;
                        id = "";
                    }else {
                        dTref.add(dramaTitleModel);
                        Toasty.success(getContext(),"Saved",Toasty.LENGTH_SHORT,true).show();
                    }
                    dramaTitleTitle.setText("");
                    dramaTitleImage.setText("");
                }
            }
        });

        dramaTitleCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
