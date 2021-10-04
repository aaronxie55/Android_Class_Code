package com.assignment.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.assignment.newsgateway.adapter.StringAdapter;
import com.assignment.newsgateway.adapter.VPAdapter;
import com.assignment.newsgateway.api.Fetcher;
import com.assignment.newsgateway.bean.Article;
import com.assignment.newsgateway.bean.Source;
import com.assignment.newsgateway.constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public Context ctx;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ImageView ivOption;
    private ListPopupWindow listPopupWindow;

    private RecyclerView recyclerView;
    private StringAdapter rvAdapter;
    private List<String> sourceNameList = new ArrayList<>();
    private List<String> categoryNameList = new ArrayList<>();
    private Map<String, Object> sourceMap = new HashMap<>();

    private ViewPager viewPager;
    private VPAdapter vpAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentManager fm;
    private FragmentTransaction ft;

    private TextView tvPage;
    private ImageView ivBackground;

    private BroadcastReceiver newsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        if (AppConstant.ACTION_NEWS_STORY.equals(intent.getAction())) {
            System.out.println("MainActivity receive from newsService.");
            List<Article> articleList = (List<Article>) intent.getSerializableExtra("articleList");
            reDoFragments(articleList);
        }
        }
    };

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstant.ACTION_NEWS_STORY);
        registerReceiver(newsReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;
        startService(new Intent(ctx, NewsService.class));

        initView();
        initData();
    }

    // view
    private void initView() {
        initDrawer();
        initRecyclerView();
        initViewPager();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        rvAdapter = new StringAdapter(new StringAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                viewPager.setBackground(null);
                ivBackground.setVisibility(View.VISIBLE);
                Source source = (Source) sourceMap.get(sourceNameList.get(position));
                setTitle(source.getName());
                Intent intent = new Intent();
                intent.setAction(AppConstant.ACTION_MSG_TO_SERVICE);
                intent.putExtra("sourceId", source.getId());
                sendBroadcast(intent);
                System.out.println("MainActivity send broadcast to newsService.");
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        recyclerView.setAdapter(rvAdapter);
    }

    private void initDrawer() {
        toolbar = findViewById(R.id.toolbar);
        ivOption = findViewById(R.id.iv_option);
        ivOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow();
            }
        });
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.view_pager);
        fm = getSupportFragmentManager();
        vpAdapter = new VPAdapter(fm, fragmentList);
        viewPager.setAdapter(vpAdapter);

        tvPage = findViewById(R.id.tv_page);

        ivBackground = findViewById(R.id.iv_background);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPage.setText(position + " of " + vpAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // data
    private void initData() {
        new NewsSourceDownloader().execute("all");
    }

    // logic
    private void reDoFragments(List<Article> articleList) {
        ivBackground.setVisibility(View.INVISIBLE);

        for (int i = 0; i < vpAdapter.getCount(); i++) {
            ft = fm.beginTransaction();
            for (Fragment fragment: fragmentList) {
                ft.remove(fragment);
            }
            ft.commit();
            fm.executePendingTransactions();
        }

        fragmentList.clear();

        for (int i = 0; i < articleList.size(); i++) {
            ArticleFragment fragment = new ArticleFragment(articleList.get(i));
            fragmentList.add(fragment);
        }
        vpAdapter.notifyDataSetChanged();

        viewPager.setCurrentItem(0);
        tvPage.setText(0 + " of " + vpAdapter.getCount());
    }

    private void showPopupWindow() {
        listPopupWindow = new ListPopupWindow(ctx);
        listPopupWindow.setAdapter(new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, android.R.id.text1, categoryNameList));
        listPopupWindow.setWidth(dip2px(180));
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setVerticalOffset(-dip2px(40));
        listPopupWindow.setAnchorView(ivOption);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listPopupWindow.dismiss();
                new NewsSourceDownloader().execute(categoryNameList.get(i));
            }
        });
        listPopupWindow.show();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(newsReceiver);
        super.onStop();
    }

    // inner class
    class NewsSourceDownloader extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String category = strings[0];
            if (category.equals("all")) category = "";
            Map<String, Object> params = new HashMap<>();
            params.put("language", "en");
            params.put("country", "us");
            params.put("category", category);
            params.put("apiKey", AppConstant.API_KEY);
            return Fetcher.getInstance().get("sources", params);
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            try {
                JSONObject respJsonObject = new JSONObject(string);
                String status = respJsonObject.getString("status");
                if ("ok".equals(status)) {
                    JSONArray sourceJsonArray = respJsonObject.getJSONArray("sources");
                    sourceNameList.clear();
                    sourceMap.clear();
                    categoryNameList.clear();
                    Set<String> categorySet = new HashSet<>();
                    for (int i = 0; i < sourceJsonArray.length(); i++) {
                        JSONObject object = sourceJsonArray.getJSONObject(i);
                        Source source = new Source(object);
                        sourceNameList.add(source.getName());
                        sourceMap.put(source.getName(), source);
                        categorySet.add(source.getCategory());
                    }
                    categoryNameList.add("all");
                    categoryNameList.addAll(categorySet);
                    rvAdapter.setList(sourceNameList);
                } else {
                    Toast.makeText(ctx, "Network error!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public int dip2px(float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
