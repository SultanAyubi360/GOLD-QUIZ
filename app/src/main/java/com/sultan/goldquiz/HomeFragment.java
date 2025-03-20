package com.sultan.goldquiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sultan.goldquiz.databinding.FragmentHomeBinding;

import java.util.ArrayList;


public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    FragmentHomeBinding binding;
    FirebaseFirestore database;
    RewardedAd mRewardedAd;
    long cash = 30;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(getContext(), "ca-app-pub-3940256099942544/5224354917", adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                mRewardedAd = rewardedAd;
            }
        });

        database = FirebaseFirestore.getInstance();

        ArrayList<CategoryModel> categories = new ArrayList<>();
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(),categories);

        database.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categories.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            CategoryModel model = snapshot.toObject(CategoryModel.class);
                            model.setCategoryId(snapshot.getId());
                            categories.add(model);
                        }
                        categoryAdapter.notifyDataSetChanged();
                    }
                });
        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));

        binding.categoryList.setAdapter(categoryAdapter);

        binding.spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedAd != null) {
                    mRewardedAd.show(getActivity(), new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                            startActivity(new Intent(getContext(),SpinnerActivity.class));
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Not available Spin it later Please!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedAd != null) {
                    mRewardedAd.show(getActivity(), new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                            FirebaseFirestore database = FirebaseFirestore.getInstance();

                            database.collection("users")
                                    .document(FirebaseAuth.getInstance().getUid())
                                    .update("coins", FieldValue.increment(cash)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Coins added in account.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Not available watch it later Please!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}