package com.example.inappbrowser;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecondFragment extends Fragment {

    EditText serch_et;

    ProgressBar progressBar;

    ArrayList<HashMap<String, String>> listRowData;

    public static String TAG_TITLE = "title";
    ListAdapter adapter;
    public static String TAG_LINK = "link";
    ImageView close_browse;
    WebView browser_web;
    public static final String PREFERENCES = "PREFERENCES_NAME";
    public static final String WEB_LINKS = "links";
    public static final String WEB_TITLE = "title";

    private Menu menu;

    EditText serch_et_browse;
    LinearLayout linearLayout;
    RelativeLayout llbrowse_search;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    String current_page_url;

    View view;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i("TAG", "onCreateView: CALLEDDD");
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        browser_web = (WebView)view.findViewById(R.id.browser_web);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        browser_web.loadUrl("https://www.google.com");
        Log.i("TAG", "onViewCreated: "+browser_web.getUrl());
        browser_web.setWebViewClient(new MyBrowser());
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.black);
        listView = (ListView)view.findViewById(R.id.listView);
        linearLayout = (LinearLayout)view.findViewById(R.id.emptyList);
        llbrowse_search = (RelativeLayout) view.findViewById(R.id.llbrowse_search);
        serch_et_browse = (EditText)view.findViewById(R.id.serch_et_browse);


    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("TAG", "onActivityCreated: ");

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    private void initWebView() {
        browser_web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                serch_et_browse.setText(url);
                progressBar.setVisibility(View.VISIBLE);
                current_page_url = url;
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                String links = sharedPreferences.getString(WEB_LINKS, null);

                if (links != null) {

                    Gson gson = new Gson();
                    ArrayList<String> linkList = gson.fromJson(links, new TypeToken<ArrayList<String>>() {
                    }.getType());

                    if (linkList.contains(current_page_url)) {
                        menu.getItem(2).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark_board_full));

//                        add_bookmark.setImageResource(R.drawable.bookmark_board_full);
                    } else {
                        menu.getItem(2).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark_board));

                    }
                } else {
                    menu.getItem(2).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark_board));

                }

//                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                browser_web.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    browser_web.loadUrl(request.getUrl().toString());
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.i("TAG", "onReceivedError: "+error);
                progressBar.setVisibility(View.GONE);
                getActivity().invalidateOptionsMenu();
            }
        });

        browser_web.getSettings().setDomStorageEnabled(true);
        browser_web.getSettings().setLoadsImagesAutomatically(true);
        browser_web.getSettings().setSupportZoom(true);
        browser_web.getSettings().setBuiltInZoomControls(true);
        browser_web.getSettings().setDisplayZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            browser_web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        browser_web.getSettings().setLoadWithOverviewMode(true);
        browser_web.getSettings().setUseWideViewPort(true);
        browser_web.clearCache(true);
        browser_web.clearHistory();
        browser_web.getSettings().setJavaScriptEnabled(true);
        browser_web.setHorizontalScrollBarEnabled(true);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String links = sharedPreferences.getString(WEB_LINKS, null);

        if (links != null) {

            Gson gson = new Gson();
            ArrayList<String> linkList = gson.fromJson(links, new TypeToken<ArrayList<String>>() {
            }.getType());

            if (linkList.contains(current_page_url)) {
                menu.getItem(2).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark_board));
            } else {
                menu.getItem(2).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark_board));
            }
        } else {
            menu.getItem(2).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark_board));
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object o = listView.getAdapter().getItem(position);
                if (o instanceof Map) {
                    Map map = (Map) o;
                    browser_web.setVisibility(View.VISIBLE);
                    llbrowse_search.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    browser_web.loadUrl(String.valueOf(map.get(TAG_LINK)));
                    serch_et_browse.setText(String.valueOf(map.get(TAG_LINK)));

                }


            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = listView.getAdapter().getItem(i);
                if (o instanceof Map) {
                    Map map = (Map) o;
                    deleteBookmark(String.valueOf(map.get(TAG_TITLE)), String.valueOf(map.get(TAG_LINK)));
                }

                return true;
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
        initWebView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_browse:


                if (browser_web.canGoBack()) {
                    browser_web.goBack();
                    if (mSwipeRefreshLayout.getVisibility() == View.VISIBLE)
                    {
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                        browser_web.setVisibility(View.VISIBLE);
                        serch_et_browse.setText(browser_web.getUrl());
                    }
                }else
                {
                    if (mSwipeRefreshLayout.getVisibility() == View.VISIBLE)
                    {
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                        browser_web.setVisibility(View.VISIBLE);
                        serch_et_browse.setText(browser_web.getUrl());

                    }

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                    String url = sharedPreferences.getString("homeurl","");
                    if (url != null && !url.isEmpty() && !url.equals("empty"))
                    {

                        browser_web.loadUrl(url);
                        serch_et_browse.setText(url);
                    }else
                    {
                        browser_web.loadUrl(current_page_url);
                        serch_et_browse.setText(current_page_url);
                    }
                }
                llbrowse_search.setVisibility(View.VISIBLE);

                return true;
            case R.id.new_browse:
                llbrowse_search.setVisibility(View.GONE);
                browser_web.setVisibility(View.VISIBLE);
                browser_web.loadUrl("https://www.google.com");
                initWebView();
                linearLayout.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                // start activity 2
                return true;
            case R.id.add_bookmark:

                String message;

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                String jsonLink = sharedPreferences.getString(WEB_LINKS, null);
                String jsonTitle = sharedPreferences.getString(WEB_TITLE, null);


                if (jsonLink != null && jsonTitle != null) {

                    Gson gson = new Gson();
                    ArrayList<String> linkList = gson.fromJson(jsonLink, new TypeToken<ArrayList<String>>() {
                    }.getType());

                    ArrayList<String> titleList = gson.fromJson(jsonTitle, new TypeToken<ArrayList<String>>() {
                    }.getType());

                    if (linkList.contains(current_page_url)) {
                        linkList.remove(current_page_url);
                        titleList.remove(browser_web.getTitle().trim());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(WEB_LINKS, new Gson().toJson(linkList));
                        editor.putString(WEB_TITLE, new Gson().toJson(titleList));
                        editor.apply();
                        menu.getItem(2).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark_board));

                        message = "Bookmark Removed";


                    } else {
                        linkList.add(current_page_url);
                        titleList.add(browser_web.getTitle().trim());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(WEB_LINKS, new Gson().toJson(linkList));
                        editor.putString(WEB_TITLE, new Gson().toJson(titleList));
                        editor.apply();
                        menu.getItem(2).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark_board_full));

                        message = "Bookmarked";

                    }
                } else {

                    ArrayList<String> linkList = new ArrayList<>();
                    ArrayList<String> titleList = new ArrayList<>();
                    linkList.add(current_page_url);
                    titleList.add(browser_web.getTitle());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(WEB_LINKS, new Gson().toJson(linkList));
                    editor.putString(WEB_TITLE, new Gson().toJson(titleList));
                    editor.apply();
                    menu.getItem(2).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark_board_full));

                    message = "Bookmarked";
                }

                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                return  true;
            case R.id.list_browse:
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                browser_web.setVisibility(View.GONE);
                llbrowse_search.setVisibility(View.GONE);
                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new LoadBookmarks().execute();

                    }
                });

                new LoadBookmarks().execute();
                return  true;
            default:
                //default intent
                return true;
        }

//        return super.onOptionsItemSelected(item);
    }

    private class LoadBookmarks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    SharedPreferences sharedPreferences =   getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                    String jsonLink = sharedPreferences.getString(WEB_LINKS, null);
                    String jsonTitle = sharedPreferences.getString(WEB_TITLE, null);
                    listRowData = new ArrayList<>();

                    if (jsonLink != null && jsonTitle != null) {

                        Gson gson = new Gson();
                        ArrayList<String> linkArray = gson.fromJson(jsonLink, new TypeToken<ArrayList<String>>() {
                        }.getType());

                        ArrayList<String> titleArray = gson.fromJson(jsonTitle, new TypeToken<ArrayList<String>>() {
                        }.getType());


                        if (titleArray.size() > 0) {
                            for (int i = 0; i < linkArray.size(); i++) {
                                HashMap<String, String> map = new HashMap<>();
                                if (titleArray.get(i).length() == 0) {
//                                map.put(TAG_TITLE, "Bookmark " + (i + 1));
                                } else {
                                    map.put(TAG_TITLE, titleArray.get(i));
                                }

                                map.put(TAG_LINK, linkArray.get(i));
                                listRowData.add(map);
                            }
                        }

                        adapter = new SimpleAdapter(  getActivity(),
                                listRowData, R.layout.bookmark_list_row,
                                new String[]{TAG_TITLE, TAG_LINK},
                                new int[]{R.id.title, R.id.link});

                        listView.setAdapter(adapter);
                    }

                    linearLayout.setVisibility(View.VISIBLE);
                    listView.setEmptyView(linearLayout);


                }
            });
            return null;
        }

        protected void onPostExecute(String args) {
            //  mSwipeRefreshLayout.setRefreshing(false);
        }

    }




    private void deleteBookmark(final String title, final String link) {

        AlertDialog builder = new AlertDialog.Builder(getActivity(), R.style.dialogStyle)
                .setTitle("Remove Bookmark")
                .setMessage("Confirm that you want to delete this bookmark?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                        String jsonLink = sharedPreferences.getString(WEB_LINKS, null);
                        String jsonTitle = sharedPreferences.getString(WEB_TITLE, null);

                        if (jsonLink != null && jsonTitle != null) {

                            Gson gson = new Gson();
                            ArrayList<String> linkArray = gson.fromJson(jsonLink, new TypeToken<ArrayList<String>>() {
                            }.getType());

                            ArrayList<String> titleArray = gson.fromJson(jsonTitle, new TypeToken<ArrayList<String>>() {
                            }.getType());

                            linkArray.remove(link);
                            titleArray.remove(title);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(WEB_LINKS, new Gson().toJson(linkArray));
                            editor.putString(WEB_TITLE, new Gson().toJson(titleArray));
                            editor.apply();

                            new LoadBookmarks().execute();
                        }
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }



}
