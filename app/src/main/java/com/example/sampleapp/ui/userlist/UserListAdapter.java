package com.example.sampleapp.ui.userlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sampleapp.R;
import com.example.sampleapp.data.db.model.User;
import com.example.sampleapp.di.PerActivity;
import com.example.sampleapp.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@PerActivity
public class UserListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_EMPTY = 0;
    private static final int VIEW_NORMAL = 1;

    private Callback callback;
    private List<User> list;

    public UserListAdapter(List<User> list) {
        this.list = list;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void removeCallback() {
        this.callback = null;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_NORMAL:
                return new NormalViewHolder(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_user, parent, false));
            case VIEW_EMPTY:
            default:
                return new EmptyViewHolder(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_empty, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (list.size() > 0)
            return list.size();
        else return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (list != null && list.size() > 0)
            return VIEW_NORMAL;
        else return VIEW_EMPTY;
    }


    /*
    *   This method is for updating the list on every App launch.
    * */
    public void updateListItems(List<User> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /*
     *   This method is for adding to the existing list as and when the new paging results come in.
     * */
    public void addListItems(List<User> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public interface Callback {
        void retryInternet();
    }

    public class NormalViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_first_name)
        TextView tvFirstName;

        @BindView(R.id.tv_last_name)
        TextView tvLastName;

        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;

        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            tvFirstName.setText("");
            tvLastName.setText("");
            ivAvatar.setImageDrawable(null);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            User user = list.get(position);

            if (user.getFirstName() != null)
                tvFirstName.setText(user.getFirstName());

            if (user.getLastName() != null)
                tvLastName.setText(user.getLastName());

            if (user.getAvatarUrl() != null)
                Glide.with(itemView.getContext())
                        .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.vd_placeholder))
                        .load(user.getAvatarUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivAvatar);
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            itemView.setOnClickListener(v -> {
                if (callback != null)
                    callback.retryInternet();
            });
        }

        @Override
        protected void clear() {

        }
    }
}
