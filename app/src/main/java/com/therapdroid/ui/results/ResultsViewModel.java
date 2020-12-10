package com.therapdroid.ui.results;

import android.arch.lifecycle.ViewModel;

import com.therapdroid.data.repository.ChatRepository;
import com.therapdroid.entity.local.Chat;

public class ResultsViewModel extends ViewModel {

    private final ChatRepository chatRepository = ChatRepository.getInstance();

    public Chat getChat (String chatId) {
        return chatRepository.findChatById(chatId);
    }

}
