package com.sultan.goldquiz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sultan.goldquiz.databinding.FragmentHomeBinding;
import com.sultan.goldquiz.databinding.FragmentWalletBinding;


public class WalletFragment extends Fragment {


    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentWalletBinding binding;
    FirebaseFirestore database;
    User user;
    String uid;
    String paypal;
    String acctit;
    String accnum;
    long cash = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWalletBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();

        database.collection("users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                binding.currentCoins.setText(String.valueOf(user.getCoins()));

            }
        });

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uid = FirebaseAuth.getInstance().getUid();
                paypal = binding.emailBox.getText().toString();
                acctit = binding.accountTitle.getText().toString();
                accnum = binding.accountNumber.getText().toString();


                if (user.getCoins() >= 40000){

                    WithdrawRequest withdrawRequest = new WithdrawRequest(uid,paypal,acctit,accnum,user.getName(),user.getCoins());

                    database.collection("withdraws")
                            .document(uid)
                            .set(withdrawRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Request send successfully", Toast.LENGTH_SHORT).show();
                            database.collection("users")
                                    .document(uid)
                                    .update("coins",FieldValue.delete()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    binding.currentCoins.setText(String.valueOf(cash));
                                    Toast.makeText(getContext(), "Coins deducted.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });

                }else {
                    Toast.makeText(getContext(), "you need more coins to withdraw...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }


}