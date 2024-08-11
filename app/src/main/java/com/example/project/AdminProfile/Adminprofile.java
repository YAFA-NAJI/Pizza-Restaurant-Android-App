package com.example.project.AdminProfile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

public class Adminprofile extends Fragment {

    private static final int REQUEST_CODE_UPDATE_PERSONAL_INFO = 1;
    private static final int  REQUEST_CODE_View_PERSONAL_INFO=1;
    private SharedPreferences sharedPreferences;


    // الطريقة التي تُستدعى عند إنشاء عرض الفريقنت
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // تضمين الخطوط الأساسية لعرض الفراغنت
        View rootView = inflater.inflate(R.layout.fragment_admin_profile, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        // استدعاء PersonalInfoActivity عند النقر على زر التحديث
        Button btnViewPersonalInfo = rootView.findViewById(R.id.btnViewPersonalInfo);
        Button btnUpdatePersonalInfo = rootView.findViewById(R.id.btnUpdateInfo);
        btnUpdatePersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateInfoActivity.class);
                startActivityForResult(intent, REQUEST_CODE_UPDATE_PERSONAL_INFO);
            }
        });

        btnViewPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewPersonalInfoActivity.class);
                String savedEmail = sharedPreferences.getString("email", "");

                intent.putExtra("email",savedEmail);
                startActivityForResult(intent, REQUEST_CODE_View_PERSONAL_INFO);
            }
        });

        return rootView;
    }

    // الطريقة التي تُستدعى عند عودة النتيجة من PersonalInfoActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE_PERSONAL_INFO && resultCode == getActivity().RESULT_OK) {
            // قم بتحديث معلومات المسؤول هنا باستخدام البيانات المحدثة المُرجعة من PersonalInfoActivity
            Toast.makeText(getActivity(), "Personal information updated successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
