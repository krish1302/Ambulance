package com.example.ambulance_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.ambulance_demo.databinding.ActivityHospitalBinding;

public class HospitalActivity extends AppCompatActivity {

    private ActivityHospitalBinding binding;

    String tutorials[]
            = { "Algorithms", "Data Structures",
            "Languages", "Interview Corner",
            "GATE", "ISRO CS",
            "UGC NET CS", "CS Subjects",
            "Web Technologies" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(HospitalActivity.this, R.layout.activity_hospital);

        ArrayAdapter<String> arr;
        arr
                = new ArrayAdapter<String>(HospitalActivity.this, R.layout.hospital_list_item, tutorials);

        this.binding.listView.setAdapter(arr);

    }
}