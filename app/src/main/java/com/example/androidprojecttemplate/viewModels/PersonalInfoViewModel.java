package com.example.androidprojecttemplate.viewModels;

// android.text.TextUtils;
//import android.widget.Toast;
import com.example.androidprojecttemplate.models.firebaseAuthSingleton;
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
    private final PersonalInfo theData;
    public static int temp = 0;

    // For firebase authentication (to get user's email)
    FirebaseAuth theAuthenticationVariable;
    FirebaseUser user;

    // For real-time database
    DatabaseReference reference;
    DatabaseReference tempReference;

    String theUsersEmail;

    public PersonalInfoViewModel() {
        theData = new PersonalInfo();
    }

    public static synchronized PersonalInfoViewModel getInstance() {
        if (instance == null) {
            instance = new PersonalInfoViewModel();
        }

        return instance;
    }

    public void getCurrentUser() {
        // Get the current user's email, which will be used further down the code
        theAuthenticationVariable = firebaseAuthSingleton.getInstance()
                .getTheInstanceFromFirebase();
        user = firebaseAuthSingleton.getInstance().getUser();
        theUsersEmail = firebaseAuthSingleton.getInstance().getEmail();
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
                    if (theEmailFromFirebase.equals(theUsersEmail)) {
                        //Found the email, can now add the data for that specific user
                        //UserData theInfo = new personalInfo(height, weight, gender);
                        UserData data = new UserData();
                        data.setHeight(Integer.parseInt(height));
                        data.setWeight(Integer.parseInt(weight));
                        data.setGender(gender);
                        data.setAge(Integer.parseInt(age));

                        tempReference = reference.child(theSnapshot.child("name")
                                .getValue().toString());
                        tempReference.child("Personal Info").setValue(data);
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
