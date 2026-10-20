package li.com.bobsonclinic.fragment;

/**
 * Agricola
 * <p>
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.data.BOBVas;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.request.base.BOBAPI;
import li.com.bobsonclinic.util.Picasso;


public class BOBAddServiceDetailFragment extends BaseFragment implements View.OnClickListener {

    private ViewGroup rootView;
    private TextView title, date;
    private TextView fee;
    private ImageView cover;
    private Button check;
    private String mVasId;


    public static BOBAddServiceDetailFragment createFragment(String id) {
        BOBAddServiceDetailFragment fragment = new BOBAddServiceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("vasid", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_service_detail, null);
        rootView.findViewById(R.id.left_btn).setOnClickListener(this);
        //rootView.findViewById(R.id.is_check).setOnClickListener(this);
        title = (TextView) rootView.findViewById(R.id.class_name);
        fee = (TextView) rootView.findViewById(R.id.class_fee);
        cover = (ImageView) rootView.findViewById(R.id.cover);
        check = rootView.findViewById(R.id.is_check);
        date = rootView.findViewById(R.id.date);
        mVasId = getArguments().getString("vasid");
        ((MainActivity) getActivity()).postVas(mVasId);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }

    public void setData(BOBVas vas) {
        if (vas == null)
            return;
        if (!isAdded())
            return;

        if (!mVasId.equals(vas.getId()))
            return;

        title.setText(vas.getTitle());

        //String html = String.format("<html lang=\'en\'><head><meta charset=\'utf-8\'><meta http-equiv=\'X-UA-Compatible\' content=\'IE=edge\'><meta name=\'viewport\' content=\'width=device-width, initial-scale=1\'><title>BOBSON產後護理照顧系統</title><link href=\'https://bobson.biz/css/font-awesome.min.css\' rel=\'stylesheet\' type=\'text/css\'><link href=\'https://bobson.biz/css/bootstrap.min.css\' rel=\'stylesheet\'><link href=\'https://bobson.biz/css/custom.css\' rel=\'stylesheet\' type=\'text/css\' ><style>.fa-btn {margin-right: 6px;} }</style></head><body id=\'app-layout\'><div class=\'container\'><div class=\'\'><font color=\'#C4A5AF'>%s</font></div></div><script src=\'https://bobson.biz/js/jquery.min.js\'></script><script src=\'https://bobson.biz/js/bootstrap.min.js\'></script></body></html>", vas.getEntry());

        WebView webview = (WebView) getView().findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setDefaultFontSize(30);
        webview.setBackgroundColor(0);
        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webview.loadDataWithBaseURL("file:///android_asset/", getHtml(vas.getEntry()), "text/html", "UTF-8", "");

        date.setText(vas.getUpdated_at());

//		URLImageParser p = new URLImageParser(detail, getContext());
//		Spanned htmlSpan = Html.fromHtml(vas.getEntry(), p, null);
//		detail.setText(htmlSpan);
//		detail.setText(Html.fromHtml(vas.getEntry()));

        if (vas.getFee().equals("0")) {
            fee.setText(getString(R.string.free));
        } else {
            fee.setText(vas.getFee() + getString(R.string.co));
        }
        Picasso.getInstance().load(BOBAPI.getAuthDomainURL() + vas.getImg()).fit().centerCrop().placeholder(R.drawable.placeholder_c).into(cover);
        Log.d("Milla", "getJoin_status: [" + vas.getJoin_status() + "]");
        if (vas.getJoin_status().equals("checked")) {
            check.setSelected(true);
            check.setText(R.string.is_class);
        } else {
            check.setSelected(false);
            check.setText(R.string.un_class);
        }
    }

    public void setCheck(boolean ischeck) {

        if (ischeck) {
            //check.setSelected(true);
            check.setText(R.string.is_class);
        } else {
            //check.setSelected(false);
            check.setText(R.string.un_class);
        }

    }


    private String getHtml(String str) {
        String html = "";

        @SuppressLint("ResourceType")
        String mBackColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.backgroundColor) & 0x00ffffff);//"#F6F3F1";//getActivity().getResources().getString(R.color.backgroundColor);

        html += "<html lang=\'en\'>" +
                "<head><meta charset=\'utf-8\'><meta http-equiv=\'X-UA-Compatible\' content=\'IE=edge\'>" +
                "<meta name=\'viewport\' content=\'width=device-width, initial-scale=1\'>" +
                "<title>BOBSON產後護理照顧系統</title>" +
                "<link href=\'https://bobson.biz/css/font-awesome.min.css\' rel=\'stylesheet\' type=\'text/css\'>" +
                "<link href=\'https://bobson.biz/css/bootstrap.min.css\' rel=\'stylesheet\'>" +
                "<link href=\'https://bobson.biz/css/custom.css\' rel=\'stylesheet\' type=\'text/css\' >";


        html += "<style>" +
                ".fa-btn " +
                "{margin-right: 6px;} "+
                "body {" +
                "background: " + mBackColor + " !important;" +
                "font-family:sans-serif;" +
                "margin:16px;" +
                "align-content:center;" +
                "font-weight:normal;" +
                "word-break:break-all;" +
                "}"+
                "</style>";

        html += "</head><body id=\'app-layout\'>"+
                "<div class=\'container\'><div class=\'\'><font color=\'#C4A5AF'>"+str+"</font>" +
                "</div></div><script src=\'https://bobson.biz/js/jquery.min.js\'>" +
                "</script><script src=\'https://bobson.biz/js/bootstrap.min.js\'>" +
                "</script></body></html>";
        return html;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.left_btn:
                getActivity().onBackPressed();
                if (!(((MainActivity) getActivity()).getCurrentFragment() instanceof BOBAddServiceFragment)) {
                    ((MainActivity) getActivity()).goAddService();
                }
                break;

        }
    }


}
