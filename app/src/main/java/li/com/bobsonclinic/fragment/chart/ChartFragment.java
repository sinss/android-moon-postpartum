package li.com.bobsonclinic.fragment.chart;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.data.server.BOBBabyBreathResponse;
import li.com.bobsonclinic.data.server.BOBBabyJaundiceResponse;
import li.com.bobsonclinic.data.server.BOBBabyMilkResponse;
import li.com.bobsonclinic.data.server.BOBBabyPulseResponse;
import li.com.bobsonclinic.data.server.BOBBabyTemperatureResponse;
import li.com.bobsonclinic.data.server.BOBBabyWeightResponse;
import li.com.bobsonclinic.data.server.BOBMomBreathResponse;
import li.com.bobsonclinic.data.server.BOBMomPressureResponse;
import li.com.bobsonclinic.data.server.BOBMomPulseResponse;
import li.com.bobsonclinic.data.server.BOBMomTemperatureResponse;
import li.com.bobsonclinic.data.server.BOBMomWeightResponse;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.viewer.chart.ChartView;

/**
 * Created by Zack on 6/5/16.
 */
public class ChartFragment extends BaseFragment {

    private ViewGroup rootView;
    private ChartView mChartView;
    private Object obj;
    private String title;

    public void setTitle(String t) {
            this.title= t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chart, null);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findview();
        init();
    }

    private void findview() {

    }

    private void init() {
        mChartView = new ChartView(getContext());
        rootView.removeAllViews();
        rootView.addView(mChartView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        setChartData(obj);
    }

    public void setChartData(Object obj){
        if(!isAdded()){
            this.obj = obj;
            return;
        }

        if(obj instanceof BOBMomPressureResponse){
            BOBMomPressureResponse data = (BOBMomPressureResponse)obj;
            mChartView.createMomPressureChart(data);
        }else if(obj instanceof BOBMomTemperatureResponse){
            BOBMomTemperatureResponse data = (BOBMomTemperatureResponse)obj;
            mChartView.createMomTemperatureChart(data);
        }else if(obj instanceof BOBMomPulseResponse){
            BOBMomPulseResponse data = (BOBMomPulseResponse)obj;
            mChartView.createMomPulseChart(data);
        }else if(obj instanceof BOBMomBreathResponse){
            BOBMomBreathResponse data = (BOBMomBreathResponse)obj;
            mChartView.createMomBreathChart(data);
        }else if(obj instanceof BOBMomWeightResponse){
            BOBMomWeightResponse data = (BOBMomWeightResponse)obj;
            mChartView.createMomWeightChart(data);
        }else if(obj instanceof BOBBabyMilkResponse){
            BOBBabyMilkResponse data = (BOBBabyMilkResponse)obj;
            mChartView.createBabyMilkChart(data);
        }else if(obj instanceof BOBBabyBreathResponse){
            BOBBabyBreathResponse data = (BOBBabyBreathResponse)obj;
            mChartView.createBOBBabyBreath(data);
        }else if(obj instanceof BOBBabyPulseResponse){
            BOBBabyPulseResponse data = (BOBBabyPulseResponse)obj;
            mChartView.createBOBBabyPulse(data);
        }else if(obj instanceof BOBBabyJaundiceResponse){
            BOBBabyJaundiceResponse data = (BOBBabyJaundiceResponse)obj;
            mChartView.createBOBBabyJaundice(data);
        }else if(obj instanceof BOBBabyWeightResponse){
            BOBBabyWeightResponse data = (BOBBabyWeightResponse)obj;
            mChartView.createBOBBabyWeight(data);
        }else if(obj instanceof BOBBabyTemperatureResponse){
            BOBBabyTemperatureResponse data = (BOBBabyTemperatureResponse)obj;
            mChartView.createBOBBabyTemperature(data);
        }
    }

}
