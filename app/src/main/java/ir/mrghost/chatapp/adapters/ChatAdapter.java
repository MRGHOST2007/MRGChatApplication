package ir.mrghost.chatapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.mrghost.chatapp.databinding.ItemReceivedMessageContainerBinding;
import ir.mrghost.chatapp.databinding.ItemSentMessgaeContainerBinding;
import ir.mrghost.chatapp.models.ChatMessage;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final String senderID;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> chatMessages, String senderID) {
        this.chatMessages = chatMessages;
        this.senderID = senderID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT){
            return new SentMessageViewHolder(ItemSentMessgaeContainerBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent , false
            ));
        } else {
            return new ReceivedMessageViewHolder(ItemReceivedMessageContainerBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent , false
            ));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == VIEW_TYPE_SENT)
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        else ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position));

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderID.equals(senderID))
            return VIEW_TYPE_SENT;
        else return VIEW_TYPE_RECEIVED;
    }

    /////////////////////////////////////////View Holder Sent Message

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemSentMessgaeContainerBinding binding;

        SentMessageViewHolder(ItemSentMessgaeContainerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(ChatMessage message) {
            binding.messageText.setText(message.message);
            binding.timeText.setText(message.dateTime);
        }
    }

    /////////////////////////////////////////View Holder Received Message

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemReceivedMessageContainerBinding binding;

        ReceivedMessageViewHolder(ItemReceivedMessageContainerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(ChatMessage message) {
            binding.messageText.setText(message.message);
            binding.timeText.setText(message.dateTime);
        }

    }

}
