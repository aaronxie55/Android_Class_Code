package com.demo.irewards.adapter;

import android.annotation.SuppressLint;
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

import java.util.List;

public class ProfileAdapter extends BaseAdapter {

    private Context context;
    private List<Profile> profileList;

    public ProfileAdapter(Context context) {
        this.context = context;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return profileList == null ? 0 : profileList.size();
    }

    @Override
    public Object getItem(int position) {
        return profileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return profileList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProfileAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_profile, null);
            viewHolder = new ProfileAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bindData(profileList.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
        TextView tvPoints;
        TextView tvPosition;

        public ViewHolder(View view) {
            this.ivAvatar = view.findViewById(R.id.iv_avatar);
            this.tvName = view.findViewById(R.id.tv_name);
            this.tvPoints = view.findViewById(R.id.tv_points);
            this.tvPosition = view.findViewById(R.id.tv_position);
        }

        void bindData(Profile profile) {
            if (PublicUtil.getString("username").equals(profile.getUsername())) {
                tvName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                tvPosition.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                tvPoints.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
            ivAvatar.setImageBitmap(PublicUtil.base64ToBitmap(profile.getImageBytes()));
            tvName.setText(profile.getLastName() + ", " + profile.getFirstName());
            tvPosition.setText(profile.getPosition() + ", " + profile.getDepartment());
            tvPoints.setText(profile.awardedRewards() + "");
        }
    }
}
