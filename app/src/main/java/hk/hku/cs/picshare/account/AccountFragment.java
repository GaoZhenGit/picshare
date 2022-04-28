package hk.hku.cs.picshare.account;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import hk.hku.cs.picshare.LoginActivity;
import hk.hku.cs.picshare.MainActivity;
import hk.hku.cs.picshare.PicApplication;
import hk.hku.cs.picshare.R;
import hk.hku.cs.picshare.WebviewActivity;
import hk.hku.cs.picshare.base.BaseFragment;
import hk.hku.cs.picshare.lib.PicImageView;
import hk.hku.cs.picshare.list.PictureListAdapter;
import hk.hku.cs.picshare.post.PostActivity;

public class AccountFragment extends BaseFragment implements View.OnClickListener{
    TextView TextView_Userinfo, TextView_tag1,TextView_tag2, TextView_tag3,TextView_settings,TextView_Nickname,TextView_AccountID,TextView_Logout;
    PicImageView Profile_Photo;
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_account;
    }



    @Override
    public Drawable getOnSelectTabIcon() {
        return PicApplication.getInstance().getDrawable(R.drawable.tab_account_select);
    }

    @Override
    public Drawable getOnUnSelectTabIcon() {
        return PicApplication.getInstance().getDrawable(R.drawable.tab_account_unselect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return mRoot;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ac_textview_userinfo)
        {
            //TBD
        }
        else if(view.getId() == R.id.ac_textview_tag1)
        {
            //TBD
        }
        else if(view.getId() == R.id.ac_textview_tag2)
        {
            Intent intent = new Intent(getContext(), WebviewActivity.class);
            intent.putExtra(WebviewActivity.PARAM_URL, "https://store.steampowered.com/");
            startActivity(intent);
        }
        else if(view.getId() == R.id.ac_textview_tag3)
        {
            Intent intent = new Intent(getContext(), WebviewActivity.class);
            intent.putExtra(WebviewActivity.PARAM_URL, "https://www.hku.hk/");
            startActivity(intent);
        }
        else if(view.getId() == R.id.ac_textview_setting)
        {
            //TBD
        } else if (view.getId() == R.id.ac_textview_logout) {
            AccountManager.getInstance().clearUserInfo();
            startActivity(new Intent(getContext(), LoginActivity.class));
        }


    }

    private void initView() {
        TextView_Userinfo = mRoot.findViewById(R.id.ac_textview_userinfo);
        TextView_tag1= mRoot.findViewById(R.id.ac_textview_tag1);
        TextView_tag2= mRoot.findViewById(R.id.ac_textview_tag2);
        TextView_tag3= mRoot.findViewById(R.id.ac_textview_tag3);
        TextView_settings= mRoot.findViewById(R.id.ac_textview_setting);
        TextView_Nickname= mRoot.findViewById(R.id.textview_user_nickname);
        TextView_AccountID= mRoot.findViewById(R.id.textView_account_id);
        Profile_Photo=mRoot.findViewById(R.id.image_profile_photo);
        TextView_Logout = mRoot.findViewById(R.id.ac_textview_logout);

        TextView_Userinfo.setOnClickListener(this);
        TextView_tag1.setOnClickListener(this);
        TextView_tag2.setOnClickListener(this);
        TextView_tag3.setOnClickListener(this);
        TextView_settings.setOnClickListener(this);
        TextView_Logout.setOnClickListener(this);


        User currentUser = AccountManager.getInstance().getUser();
        TextView_Nickname.setText(currentUser.name);
        TextView_AccountID.setText(String.format("Email: %s", currentUser.email));
        Profile_Photo.loadRound(currentUser.avatar);
        //TBD: Add photo!
        //Profile_Photo
    }

    @Override
    public void onResume() {
        super.onResume();
        User currentUser = AccountManager.getInstance().getUser();
        if (TextView_Nickname != null && TextView_AccountID != null && Profile_Photo != null) {
            TextView_Nickname.setText(currentUser.name);
            TextView_AccountID.setText(String.format("Email: %s", currentUser.email));
            Profile_Photo.loadRound(currentUser.avatar);
        }
    }
}
