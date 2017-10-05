package com.xeleb.motionviews.viewmodel;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;
import com.xeleb.motionviews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel Victor Garcia on 9/28/17.
 */

public class MentionAdapter extends RecyclerViewPresenter<Mention> {

    protected Adapter adapter;
    private List<Mention> mentions;

    public MentionAdapter(Context context) {
        super(context);
    }

    public void setMentions(List<Mention> mentions) {
        this.mentions = mentions;
    }

    @Override
    protected PopupDimensions getPopupDimensions() {
        PopupDimensions dims = new PopupDimensions();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
//        List<Mention> Profiles = Mention.ProfileS;
        if (TextUtils.isEmpty(query)) {
            adapter.setData(mentions);
        } else {
            query = query.toString().toLowerCase();
            List<Mention> list = new ArrayList<>();
            for (Mention u : mentions) {
                if (u.getFullname() != null && !u.getFullname().equals("") && u.getFullname().toLowerCase().contains(query) ||
                        u.getNickname().toLowerCase().contains(query)) {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Log.e("ProfilePresenter", "found "+list.size()+" Profiles for query "+query);
        }
        adapter.notifyDataSetChanged();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<Mention> data;

        public class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView username;
            public Holder(View itemView) {
                super(itemView);
                root = itemView;
                username = ((TextView) itemView.findViewById(R.id.username));
            }
        }

        public void setData(List<Mention> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.item_mention, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (isEmpty()) {
                holder.username.setText("Sorry!");
                holder.root.setOnClickListener(null);
                return;
            }
            final Mention mention = data.get(position);
            holder.username.setText("@" + mention.getNickname());
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dispatchClick(mention);
                }
            });
        }
    }
}