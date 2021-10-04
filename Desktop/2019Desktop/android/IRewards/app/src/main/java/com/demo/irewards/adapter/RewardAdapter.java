package com.demo.irewards.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.irewards.R;
import com.demo.irewards.entity.Profile;
import com.demo.irewards.entity.Reward;
import com.demo.irewards.util.PublicUtil;

public class RewardAdapter extends BaseAdapter {

    private Context context;
    private Profile profile;
    public RewardAdapter(Context context, Profile profile) {
        this.context = context;
        this.profile = profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public int getCount() {
        return profile.getRewards() == null ? 1 : profile.getRewards().size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) return profile;
        return profile.getRewards().get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (position == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_header_profile, null);
            HeadViewHolder headViewHolder = new HeadViewHolder(convertView);
            headViewHolder.bindData(profile);
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_reward, null);
                viewHolder = new ViewHolder();
                viewHolder.findView(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.bindData((Reward) getItem(position));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvDate;
        TextView tvName;
        TextView tvValue;
        TextView tvNotes;

        void findView(View view) {
            tvDate = view.findViewById(R.id.tv_date);
            tvName = view.findViewById(R.id.tv_name);
            tvValue = view.findViewById(R.id.tv_value);
            tvNotes = view.findViewById(R.id.tv_notes);
        }
        void bindData(Reward reward) {
            tvDate.setText(reward.getDate());
            tvName.setText(reward.getName());
            tvValue.setText(reward.getValue() + "");
            tvNotes.setText(reward.getNotes());
        }
    }

    class HeadViewHolder {
        ImageView ivAvatar;
        TextView tvName;
        TextView tvUsername;
        TextView tvLocation;
        TextView tvAwarded;
        TextView tvDepartment;
        TextView tvPosition;
        TextView tvToAward;
        TextView tvStory;
        TextView tvAwardedCount;

        public HeadViewHolder(View view) {
            ivAvatar = view.findViewById(R.id.iv_avatar);
            tvName = view.findViewById(R.id.tv_name);
            tvUsername = view.findViewById(R.id.tv_username);
            tvLocation = view.findViewById(R.id.tv_location);
            tvAwarded = view.findViewById(R.id.tv_awarded);
            tvDepartment = view.findViewById(R.id.tv_department);
            tvPosition = view.findViewById(R.id.tv_position);
            tvToAward = view.findViewById(R.id.tv_to_award);
            tvStory = view.findViewById(R.id.tv_story);
            tvAwardedCount = view.findViewById(R.id.tv_awarded_count);
        }

        void bindData(Profile profile) {
            ivAvatar.setImageBitmap(PublicUtil.base64ToBitmap(profile.getImageBytes()));
            tvName.setText(profile.getLastName() + ", " + profile.getFirstName());
            tvUsername.setText("(" + profile.getUsername() + ")");
            tvLocation.setText(profile.getLocation());
            float awarded = 0;
            int count = 0;
            if (profile.getRewards() != null) {
                for (int i = 0; i < profile.getRewards().size(); i++) {
                    awarded += profile.getRewards().get(i).getValue();
                    count ++;
                }
            }
            tvAwarded.setText(awarded + "");
            tvDepartment.setText(profile.getDepartment());
            tvPosition.setText(profile.getPosition());
            tvToAward.setText(profile.getPointsToAward() + "");
            tvStory.setText(profile.getStory());
            tvAwardedCount.setText("Reward History(" + count + "):");
        }
    }
}
