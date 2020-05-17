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

public class animeadd extends DialogFragment {
    EditText animeTitle,animeImage,animeVideo;
    Spinner animeSeriesTitle,animeTypeSpinner;
    Button animeOK,animeCancel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference aref,dtref,atref;
    animeModel aModel;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.animeadd,container,false);

        final ArrayList<String> animeSeriesArrayList = new ArrayList<>();
        final ArrayList<String> animeTypeArrayList = new ArrayList<>();
        animeTitle = view.findViewById(R.id.animeTitle);
        animeImage = view.findViewById(R.id.animeImage);
        animeVideo = view.findViewById(R.id.animeVideo);
        animeSeriesTitle = view.findViewById(R.id.animeSeriesTitle);
        animeTypeSpinner = view.findViewById(R.id.animeTypeSpinner);
        animeOK = view.findViewById(R.id.animeOk);
        animeCancel = view.findViewById(R.id.animeCancel);

        if (aModel != null){
            animeTitle.setText(aModel.animeTitle);
            animeImage.setText(aModel.animeImage);
            animeVideo.setText(aModel.animeVideo);
        }

        dtref = db.collection("Drama Title");
        dtref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                animeSeriesArrayList.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots){
                    animeSeriesArrayList.add(ds.toObject(dramaTitleModel.class).dramaTitle);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,animeSeriesArrayList);
                animeSeriesTitle.setAdapter(arrayAdapter);
            }
        });

        atref = db.collection("Anime Type");
        atref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                animeTypeArrayList.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots){
                    animeTypeArrayList.add(ds.toObject(animeTypeModel.class).animeType);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,animeTypeArrayList);
                animeTypeSpinner.setAdapter(arrayAdapter);
            }
        });

        animeOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(animeTitle.getText().toString().equals("")||animeImage.getText().toString().equals("")||animeVideo.getText().toString().equals("")){
                    Toasty.error(getContext(),"Input Something first",Toasty.LENGTH_SHORT,true).show();
                }
                else {
                    animeModel animeModel = new animeModel();
                    animeModel.animeTitle = animeTitle.getText().toString();
                    animeModel.animeImage = animeImage.getText().toString();
                    animeModel.animeVideo = animeVideo.getText().toString();
                    animeModel.animeSeries = animeSeriesArrayList.get(animeSeriesTitle.getSelectedItemPosition());
                    animeModel.animeType = animeTypeArrayList.get(animeTypeSpinner.getSelectedItemPosition());
                    aref = db.collection("Anime");
                    if (aModel != null){
                        aref.document(id).set(animeModel);
                        Toasty.success(getContext(),"Updated",Toasty.LENGTH_SHORT,true).show();
                        aModel = null;
                        id = "";
                    }else {
                        aref.add(animeModel);
                        Toasty.success(getContext(),"Saved",Toasty.LENGTH_SHORT,true).show();
                    }
                    animeTitle.setText("");
                    animeImage.setText("");
                    animeVideo.setText("");
                }
            }
        });

        animeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
