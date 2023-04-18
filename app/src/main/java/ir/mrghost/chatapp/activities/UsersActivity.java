package ir.mrghost.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import ir.mrghost.chatapp.adapters.UsersAdapter;
import ir.mrghost.chatapp.databinding.ActivityUsersBinding;
import ir.mrghost.chatapp.listeners.UserListener;
import ir.mrghost.chatapp.models.User;
import ir.mrghost.chatapp.utils.Constants;
import ir.mrghost.chatapp.utils.PreferenceManager;

public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        setListeners();
        getUsers();

    }

    private void setListeners(){
        binding.backBtn.setOnClickListener(v -> onBackPressed());
    }

    private void getUsers(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUID = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null){
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if (currentUID.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        if (users.size() > 0){
                            UsersAdapter adapter = new UsersAdapter(users, this);
                            binding.usersRecycler.setAdapter(adapter);
                            binding.usersRecycler.setVisibility(View.VISIBLE);
                        } else showErrorMessage();
                    } else showErrorMessage();
                });
    }

    private void showErrorMessage(){
        binding.errorMessageText.setText(String.format("%s" , "No users found !"));
        binding.errorMessageText.setVisibility(View.VISIBLE);
    }

    private void loading(boolean isLoading){
        if (isLoading)
            binding.prograssBar.setVisibility(View.VISIBLE);
        else binding.prograssBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext() , ChatActivity.class);
        intent.putExtra(Constants.KEY_USER , user);
        startActivity(intent);
        finish();
    }

}