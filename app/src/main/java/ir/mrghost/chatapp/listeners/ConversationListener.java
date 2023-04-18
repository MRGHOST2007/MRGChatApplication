package ir.mrghost.chatapp.listeners;

import ir.mrghost.chatapp.models.User;

public interface ConversationListener {
    void onConversationClicked(User user);
}
