package com.therapdroid.data.repository;

import android.util.Log;

import com.therapdroid.data.remote.Api;
import com.therapdroid.data.remote.RemoteDataSource;
import com.therapdroid.entity.local.Answer;
import com.therapdroid.entity.local.Chat;
import com.therapdroid.entity.local.LocalQuestion;
import com.therapdroid.entity.remote.request.DiagnosisRequest;
import com.therapdroid.entity.remote.request.ParseRequest;
import com.therapdroid.entity.remote.response.DiagnosisResponse;
import com.therapdroid.entity.remote.response.ParseResponse;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MessageRepository {

    private static final MessageRepository ourInstance = new MessageRepository();

    private Api api = RemoteDataSource.getInstance().getApi();
    private Realm realm;

    public static MessageRepository getInstance() {
        return ourInstance;
    }

    private MessageRepository() {
        realm = Realm.getDefaultInstance();
    }

    private Chat findChatById (String chatId) {
        return realm.where(Chat.class).equalTo("id", chatId).findFirst();
    }

    public void addAnswer (String chatId, Answer answer) {
        Chat chat = findChatById(chatId);
        realm.executeTransaction(realm -> chat.getAnswers().add(answer));
    }

    public void addQuestion (String chatId, LocalQuestion localQuestion) {
        Chat chat = findChatById(chatId);
        realm.executeTransaction(realm -> chat.getLocalQuestions().add(localQuestion));
        Log.i(TAG, "addQuestion: Here !");
    }

    public Single<Response<ParseResponse>> parse (ParseRequest request) {
        return api
                .parseFreeText(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Response<DiagnosisResponse>> diagnose (DiagnosisRequest request) {
        return api
                .diagnose(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
