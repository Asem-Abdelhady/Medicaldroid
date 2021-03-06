package com.therapdroid.ui.home.OnlineDoctor;

import android.arch.lifecycle.ViewModel;

import com.therapdroid.data.repository.ChatRepository;
import com.therapdroid.entity.local.Chat;
import com.therapdroid.entity.local.LocalQuestion;

import io.reactivex.Observable;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.rx.CollectionChange;

public class OnlineDoctorViewModel extends ViewModel {

    private final ChatRepository chatRepository = ChatRepository.getInstance();

    Observable<CollectionChange<RealmResults<Chat>>> getChats () {
        return chatRepository.getChats();
    }

    void addChat (Chat chat) {
        chat.setLocalQuestions(new RealmList<>());
        long time = System.currentTimeMillis();
        chat.getLocalQuestions().add(new LocalQuestion(
                time,
                LocalQuestion.Type.FREE_TEXT,
                "Hi! I am Doctor Droid"
        ));
        chat.getLocalQuestions().add(new LocalQuestion(
                time + 1,
                LocalQuestion.Type.FREE_TEXT,
                "Please describe your symptoms."
        ));
        chatRepository.addChat(chat);
    }

    void deleteChat (Chat chat) {
        chatRepository.deleteChat(chat.getId());
    }

}
