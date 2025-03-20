package com.sultan.goldquiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sultan.goldquiz.databinding.FragmentProfileBinding;
import com.sultan.goldquiz.databinding.FragmentWalletBinding;

public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentProfileBinding binding;
    private FirebaseAuth auth;
    FirebaseFirestore database;
    User user;
    private InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);

        AdRequest adRequest = new AdRequest.Builder().build();
        database = FirebaseFirestore.getInstance();

        database.collection("users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                binding.emailBox.setText(String.valueOf(user.getEmail()));
                binding.userBox.setText(String.valueOf(user.getName()));
                binding.passBox.setText(String.valueOf(user.getPass()));

            }
        });

        binding.showPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v.getId()==R.id.show_pass_btn){
                    if(binding.passBox.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        ((ImageView)(v)).setImageResource(R.drawable.ic_baseline_visibility_off_24);
                        //Show Password
                        binding.passBox.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        ((ImageView)(v)).setImageResource(R.drawable.ic_baseline_visibility_24);
                        //Hide Password
                        binding.passBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            }
        });

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterstitialAd.load(getContext(),"ca-app-pub-3940256099942544/1033173712", adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                if (mInterstitialAd != null) {
                                    mInterstitialAd.show(getActivity());
                                } else {
                                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                }
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error

                                mInterstitialAd = null;
                            }
                        });

            }
        });



        // Inflate the layout for this fragment
        return binding.getRoot();
    }

}