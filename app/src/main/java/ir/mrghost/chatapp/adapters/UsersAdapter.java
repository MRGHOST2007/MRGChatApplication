package ir.mrghost.chatapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.mrghost.chatapp.databinding.ItemUserContainerBinding;
import ir.mrghost.chatapp.listeners.UserListener;
import ir.mrghost.chatapp.models.User;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> users;
    private final UserListener userListener;

    public UsersAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserContainerBinding itemUserContainerBinding = ItemUserContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent , false );

        return new UserViewHolder(itemUserContainerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder{

        ItemUserContainerBinding binding;

        UserViewHolder(ItemUserContainerBinding item){
            super(item.getRoot());
            binding = item;
        }

        void setUserData(User user){
            binding.nameText.setText(user.name);
            binding.emailText.setText(user.email);
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }
    }

}
