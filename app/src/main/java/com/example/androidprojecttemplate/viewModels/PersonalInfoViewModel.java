package com.example.androidprojecttemplate.viewModels;

// android.text.TextUtils;
//import android.widget.Toast;
import com.example.androidprojecttemplate.models.FirebaseDB;
import androidx.annotation.NonNull;

import com.example.androidprojecttemplate.models.UserData;
//import com.example.androidprojecttemplate.views.CreateAccountActivity;
import com.example.androidprojecttemplate.views.PersonalInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalInfoViewModel {
    private static PersonalInfoViewModel instance;
    private final PersonalInfo data;

    private static int temp = 0;

    // For firebase authentication (to get user's email)
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    // For real-time database
    private DatabaseReference reference;
    private DatabaseReference tempReference;

    private String email;

    public PersonalInfoViewModel() {
        data = new PersonalInfo();
    }

    public static synchronized PersonalInfoViewModel getInstance() {
        if (instance == null) {
            instance = new PersonalInfoViewModel();
        }

        return instance;
    }

    public void getCurrentUser() {
        // Get the current user's email, which will be used further down the code
        firebaseAuth = FirebaseDB.getInstance().getFirebaseAuth();
        user = FirebaseDB.getInstance().getUser();
        email = FirebaseDB.getInstance().getEmail();
    }


    public int putTheDataIntoFirebase(String height, String weight, String gender, String age) {
        // Can now add the data to the firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot theSnapshot: snapshot.getChildren()) {

                    String theEmailFromFirebase = theSnapshot.child("username")
                            .getValue().toString();
                    if (theEmailFromFirebase.equals(email)) {
                        //Found the email, can now add the data for that specific user
                        //UserData theInfo = new personalInfo(height, weight, gender);
                        UserData userData = new UserData();
                        userData.setHeight(Integer.parseInt(height));
                        userData.setWeight(Integer.parseInt(weight));
                        userData.setGender(gender);
                        userData.setAge(Integer.parseInt(age));

                        tempReference = reference.child(theSnapshot.child("name")
                                .getValue().toString());
                        tempReference.child("Personal Info").setValue(userData);
                        temp = 1;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                temp = 2;
            }
        });

        return temp;
    }
}