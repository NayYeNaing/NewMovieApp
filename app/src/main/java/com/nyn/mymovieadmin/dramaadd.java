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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class dramaadd extends DialogFragment {
    EditText dramaTitle,dramaImage,dramaVideo;
    Spinner dramaSeriesTitle,dramaTypeSpinner;
    Button dramaOK,dramaCancel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dref,dtref,dtyperef;
    dramaModel dModel;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dramaadd,container,false);

        final ArrayList<String> dramaSeriesArrayList = new ArrayList<>();
        final ArrayList<String> dramaTypeArrayList = new ArrayList<>();
        dramaTitle = view.findViewById(R.id.dramaTitle);
        dramaImage = view.findViewById(R.id.dramaImage);
        dramaVideo = view.findViewById(R.id.dramaVideo);
        dramaSeriesTitle = view.findViewById(R.id.dramaSeriesTitle);
        dramaTypeSpinner = view.findViewById(R.id.dramaTypeSpinner);
        dramaOK = view.findViewById(R.id.dramaOk);
        dramaCancel = view.findViewById(R.id.dramaCancel);

        if (dModel != null) {
            dramaTitle.setText(dModel.dramaTitle);
            dramaImage.setText(dModel.dramaImage);
            dramaVideo.setText(dModel.dramaVideo);
        }

        dtref = db.collection("Drama Title");
        dtref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dramaSeriesArrayList.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    dramaSeriesArrayList.add(ds.toObject(dramaTitleModel.class).dramaTitle);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,dramaSeriesArrayList);
                dramaSeriesTitle.setAdapter(arrayAdapter);
            }
        });

        dtyperef = db.collection("Drama Type");
        dtyperef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dramaTypeArrayList.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    dramaTypeArrayList.add(ds.toObject(dramaTypeModel.class).dramaType);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,dramaTypeArrayList);
                dramaTypeSpinner.setAdapter(arrayAdapter);
            }
        });

        dramaOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dramaTitle.getText().toString().equals("") && dramaImage.getText().toString().equals("") && dramaVideo.getText().toString().equals("")) {
                    Toasty.error(getContext(), "Input Something first", Toasty.LENGTH_SHORT, true).show();
                } else {
                    dramaModel dramaModel = new dramaModel();
                    dramaModel.dramaTitle = dramaTitle.getText().toString();
                    dramaModel.dramaImage = dramaImage.getText().toString();
                    dramaModel.dramaVideo = dramaVideo.getText().toString();
                    dramaModel.dramaSeriesTitle = dramaSeriesArrayList.get(dramaSeriesTitle.getSelectedItemPosition());
                    dramaModel.dramaType = dramaTypeArrayList.get(dramaTypeSpinner.getSelectedItemPosition());
                    dramaModel.favCount = 0;
                    dramaModel.viewCount = 0;
                    dref = db.collection("Drama");
                    if (dModel != null) {
                        dref.document(id).set(dramaModel);
                        Toasty.success(getContext(), "Updated", Toasty.LENGTH_SHORT, true).show();
                        dModel = null;
                        id = "";
                    } else {
                        dref.add(dramaModel);
                        Toasty.success(getContext(), "Saved", Toasty.LENGTH_SHORT, true).show();
                    }
                    dramaTitle.setText("");
                    dramaImage.setText("");
                    dramaVideo.setText("");
                }
            }
        });

        dramaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
