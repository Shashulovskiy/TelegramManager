package org.telegram.telegrammanager.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.drinkless.td.libcore.telegram.apihelper.Chat;
import org.drinkless.td.libcore.telegram.apihelper.Handler;
import org.telegram.telegrammanager.Helpers.ChatListAdapter;
import org.telegram.telegrammanager.Models.ChatCard;
import org.telegram.telegrammanager.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.drinkless.td.libcore.telegram.apihelper.MessageSender.sendTextMessage;
import static org.telegram.telegrammanager.Helpers.TGClient.tClient;

public class ReceiversListFragment extends android.support.v4.app.Fragment {

    private Context context;
    public static String TAG = ReceiversListFragment.class.getSimpleName();
    public static String message;

    public ReceiversListFragment(){
    }

    public static ReceiversListFragment newInstance() {
        return new ReceiversListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.receivers_fragment,
                container, false);

        context = view.getContext();

        RecyclerView rv = view.findViewById(R.id.receivers_rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        ArrayList<ChatCard> groups = new ArrayList<ChatCard>();

        FloatingActionButton sendFab = view.findViewById(R.id.send_fab);

        AtomicBoolean done = new AtomicBoolean(false);
        //TODO убрать заглушки картинок
        int images[] = {R.drawable.logo3, R.drawable.logo1, R.drawable.logo2, R.drawable.logo3};

        ArrayList<Chat> receivers = new ArrayList<>();
        tClient.getChats((type, obj) -> {
            if (type == "chats") {
                ArrayList<Chat> chats = (ArrayList<Chat>)obj;
                ArrayList<Chat> myChannels = new ArrayList<>();
                int j = 0;
                for (Chat chat : chats){
                    if (chat.isChannel() && chat.isSuperGroupAdmin()) {
                        if (chat.getTitle() != null && chat.getLastMessage() != null && chat.getLastMessage().getMessageContent().isText())
                            groups.add(new ChatCard(chat, chat.getTitle(), chat.getLastMessage().getMessageContent().getText(), images[j]));
                        else
                            groups.add(new ChatCard(chat, chat.getTitle(), "Сообщение", images[j]));
                        myChannels.add(chat);
                        j++;

                    }
                }

                ChatListAdapter adapter = new ChatListAdapter(context, groups);
                adapter.setOnClick((i)->{
                    ChatCard chat = groups.get(i);

                    if (!receivers.contains(chat.chat)){
                        groups.set(i, new ChatCard(chat.chat, chat.name, chat.lastMes, R.mipmap.checked));
                        receivers.add(chat.chat);
                    } else {
                        groups.set(i, new ChatCard(chat.chat, chat.name, chat.lastMes, images[i]));
                        receivers.remove(chat.chat);
                    }
                    adapter.notifyItemChanged(i);
                });
                rv.setAdapter(adapter);
                done.set(true);
                
            } else if (type == "ERROR") {
                Log.e("Get Chat", type);
            }
        });

        sendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(receivers != null){
                    for(Chat chat : receivers){
                        sendTextMessage(tClient, chat, message, (Handler) (type, obj) -> {
                            if(type == "messageSent"){
                            }
                        });
                    }
                }

                Toast.makeText(getActivity(), "Сообщения отправлены",
                        Toast.LENGTH_LONG).show();

                ChatListFragment clf = new ChatListFragment();
                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_fragment_container, clf)
                        .commit();
                fragmentManager.popBackStack();
                fragmentManager.popBackStack();
            }
        });

        while (!done.get()) {}

        return view;
    }

    public void onResume() {
        super.onResume();

        TextView header_text = getActivity().findViewById(R.id.header_text);
        header_text.setText("Выберите получателей");
    }
}
