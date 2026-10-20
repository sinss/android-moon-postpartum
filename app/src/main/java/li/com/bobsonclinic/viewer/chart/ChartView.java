package li.com.bobsonclinic.viewer.chart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.CombinedXYChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.ScatterChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.data.BOBBabyBreath;
import li.com.bobsonclinic.data.BOBBabyJaundice;
import li.com.bobsonclinic.data.BOBBabyMilk;
import li.com.bobsonclinic.data.BOBBabyPulse;
import li.com.bobsonclinic.data.BOBBabyTemperature;
import li.com.bobsonclinic.data.BOBBabyWeight;
import li.com.bobsonclinic.data.BOBMomBreath;
import li.com.bobsonclinic.data.BOBMomPressure;
import li.com.bobsonclinic.data.BOBMomPulse;
import li.com.bobsonclinic.data.BOBMomTemperature;
import li.com.bobsonclinic.data.BOBMomWeight;
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

/**
 * Created by Zack on 2016/6/3.
 */
public class ChartView extends FrameLayout{

    protected FrameLayout mChartLayout;

    protected TextView chart1_txtV;
    protected TextView chart2_txtV;
    protected TextView chart3_txtV;
    protected TextView chart4_txtV;
    protected TextView chart5_txtV;

    protected View chart1_bg;
    protected View chart2_bg;
    protected View chart3_bg;
    protected View chart4_bg;
    protected View chart5_bg;

//    protected View progressBarV;
//    protected View nodataV;

    protected float density = 1;
    protected int screenWidth = 0;

    public ChartView(Context context) {
        super(context);
        init(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        LayoutInflater li = LayoutInflater.from(context);
        li.inflate(R.layout.ui_view_chart_layout, this);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        density = dm.density;
        screenWidth = dm.widthPixels;

        mChartLayout = (FrameLayout)findViewById(R.id.ui_chart_layout);

        chart1_txtV = (TextView)findViewById(R.id.chart1_txtV);
        chart2_txtV = (TextView)findViewById(R.id.chart2_txtV);
        chart3_txtV = (TextView)findViewById(R.id.chart3_txtV);
        chart4_txtV = (TextView)findViewById(R.id.chart4_txtV);
        chart5_txtV = (TextView)findViewById(R.id.chart5_txtV);

        chart1_bg = findViewById(R.id.chart1_bg);
        chart2_bg = findViewById(R.id.chart2_bg);
        chart3_bg = findViewById(R.id.chart3_bg);
        chart4_bg = findViewById(R.id.chart4_bg);
        chart5_bg = findViewById(R.id.chart5_bg);

//        progressBarV = findViewById(R.id.progressBarV);
//        nodataV = findViewById(R.id.nodataV);

        chart1_bg.setVisibility(View.INVISIBLE);
        chart1_txtV.setVisibility(View.INVISIBLE);
        chart2_bg.setVisibility(View.INVISIBLE);
        chart2_txtV.setVisibility(View.INVISIBLE);
        chart3_bg.setVisibility(View.INVISIBLE);
        chart3_txtV.setVisibility(View.INVISIBLE);
        chart4_bg.setVisibility(View.INVISIBLE);
        chart4_txtV.setVisibility(View.INVISIBLE);
        chart5_bg.setVisibility(View.INVISIBLE);
        chart5_txtV.setVisibility(View.INVISIBLE);

//        progressBarV.setVisibility(VISIBLE);
//        nodataV.setVisibility(GONE);
    }

    public void createBabyMilkChart(BOBBabyMilkResponse data){
        if(data == null || data.getExams() == null || data.getExams().size() < 1){
//            nodataV.setVisibility(VISIBLE);
//            progressBarV.setVisibility(GONE);
            return;
        }
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Integer> y1Values = new ArrayList<>();
        ArrayList<Integer> y2Values = new ArrayList<>();
        ArrayList<Boolean> y3Values = new ArrayList<>();

        for(int i=0; i<data.getExams().size(); i++){
            BOBBabyMilk mExam = data.getExams().get(i);
            int bottle_milk = mExam.getMom_bottle_milk(); //母乳(瓶裝)
            int formula_milk = mExam.getFormula_milk() + bottle_milk; //配方奶 奶量
            boolean isMomBreast = mExam.getMom_breast_milk(); //母親(親餵)
            y1Values.add(formula_milk);
            y2Values.add(bottle_milk);
            y3Values.add(isMomBreast);
            xValues.add(mExam.getExam_datetime());
        }
        createChartView(data.getExams(), createBarAndScatterChart(xValues, y1Values, y2Values, y3Values, 50,
                 Color.parseColor("#F2F5A9"), Color.parseColor("#BCF5A9"), Color.parseColor("#FAAC58")));
        createLegendLabel(new String[]{"#BCF5A9", "#F2F5A9", "#FAAC58"}, new String[]{"母乳(瓶裝)", "配方奶 奶量", "母親(親餵)"});
//        progressBarV.setVisibility(GONE);
//        nodataV.setVisibility(GONE);
    }

    public void createBOBBabyBreath(BOBBabyBreathResponse data) {
        if (data == null || data.getExams() == null || data.getExams().size() < 1) {
//            nodataV.setVisibility(VISIBLE);
//            progressBarV.setVisibility(GONE);
            return;
        }

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();

        for(int i=0; i<data.getExams().size(); i++){
            BOBBabyBreath exam = data.getExams().get(i);
            xValues.add(exam.getExam_datetime());
            yValues.add((float)exam.getBreath());
        }
        createChartView(data.getExams(), createSingleLineChart(xValues, yValues, 10, Color.parseColor("#298A08")));
        createLegendLabel(new String[]{"#298A08"}, new String[]{"寶寶呼吸"});
//        progressBarV.setVisibility(GONE);
//        nodataV.setVisibility(GONE);
    }

    public void createBOBBabyPulse(BOBBabyPulseResponse data) {
        if (data == null || data.getExams() == null || data.getExams().size() < 1) {
//            nodataV.setVisibility(VISIBLE);
//            progressBarV.setVisibility(GONE);
            return;
        }

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();

        for(int i=0; i<data.getExams().size(); i++){
            BOBBabyPulse exam = data.getExams().get(i);
            xValues.add(exam.getExam_date());
            yValues.add((float)exam.getPulse());
        }
        createChartView(data.getExams(), createSingleLineChart(xValues, yValues, 10, Color.parseColor("#298A08")));
        createLegendLabel(new String[]{"#298A08"}, new String[]{"寶寶脈搏"});
//        progressBarV.setVisibility(GONE);
//        nodataV.setVisibility(GONE);
    }

    public void createBOBBabyJaundice(BOBBabyJaundiceResponse data) {
        if (data == null || data.getExams() == null || data.getExams().size() < 1) {
//            nodataV.setVisibility(VISIBLE);
//            progressBarV.setVisibility(GONE);
            return;
        }

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();

        for(int i=0; i<data.getExams().size(); i++){
            BOBBabyJaundice exam = data.getExams().get(i);
            xValues.add(exam.getExam_datetime());
            yValues.add(exam.getJaundice_index());
        }
        createChartView(data.getExams(), createSingleLineChart(xValues, yValues, 10, Color.parseColor("#298A08")));
        createLegendLabel(new String[]{"#298A08"}, new String[]{"黃疸指數"});
//        progressBarV.setVisibility(GONE);
//        nodataV.setVisibility(GONE);
    }

    public void createBOBBabyWeight(BOBBabyWeightResponse data) {
        if (data == null || data.getExams() == null || data.getExams().size() < 1) {
//            nodataV.setVisibility(VISIBLE);
//            progressBarV.setVisibility(GONE);
            return;
        }

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();

        for(int i=0; i<data.getExams().size(); i++){
            BOBBabyWeight exam = data.getExams().get(i);
            xValues.add(exam.getExam_datetime());
            yValues.add((float)exam.getWeight());
        }
        createChartView(data.getExams(), createSingleLineChart(xValues, yValues, 10, Color.parseColor("#298A08")));
        createLegendLabel(new String[]{"#298A08"}, new String[]{"寶寶體重"});
//        progressBarV.setVisibility(GONE);
//        nodataV.setVisibility(GONE);
    }

    public void createBOBBabyTemperature(BOBBabyTemperatureResponse data) {
        if (data == null || data.getExams() == null || data.getExams().size() < 1) {
//            nodataV.setVisibility(VISIBLE);
//            progressBarV.setVisibility(GONE);
            return;
        }

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();

        for(int i=0; i<data.getExams().size(); i++){
            BOBBabyTemperature exam = data.getExams().get(i);
            xValues.add(exam.getExam_datetime());
            yValues.add(exam.getTemperature());
        }
        createChartView(data.getExams(), createSingleLineChart(xValues, yValues, 10, Color.parseColor("#298A08")));
        createLegendLabel(new String[]{"#298A08"}, new String[]{"寶寶溫度"});
//        progressBarV.setVisibility(GONE);
//        nodataV.setVisibility(GONE);
    }


    public void createMomPressureChart(BOBMomPressureResponse data) {
        if (data == null || data.getExams() == null || data.getExams().size() < 1) {
//            nodataV.setVisibility(VISIBLE);
//            progressBarV.setVisibility(GONE);
            return;
        }

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Float> y1Values = new ArrayList<>();
        ArrayList<Float> y2Values = new ArrayList<>();

        for(int i=0; i<data.getExams().size(); i++){
            BOBMomPressure exam = data.getExams().get(i);
            xValues.add(exam.getExam_date());
            float mDiastolic = exam.getDiastolic_pressure(); //舒張壓
            float mSystolic = exam.getSystolic_pressure(); //收縮壓
            y1Values.add(mDiastolic);
            y2Values.add(mSystolic);
        }
        createChartView(data.getExams(), createDoubleLineChart(xValues, y1Values, y2Values, 10, Color.parseColor("#FFBF00"), Color.parseColor("#298A08")));
        createLegendLabel(new String[]{"#FFBF00", "#298A08"}, new String[]{"舒張壓", "收縮壓"});
//        progressBarV.setVisibility(GONE);
//        nodataV.setVisibility(GONE);
    }

    public void createMomPulseChart(BOBMomPulseResponse data) {
        if (data == null || data.getExams() == null || data.getExams().size() < 1) {
//            nodataV.setVisibility(VISIBLE);
//            progressBarV.setVisibility(GONE);
            return;
        }

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();

        for(int i=0; i<data.getExams().size(); i++){
            BOBMomPulse exam = data.getExams().get(i);
            xValues.add(exam.getExam_date());
            yValues.add(exam.getPulse());
        }
        createChartView(data.getExams(), createSingleLineChart(xValues, yValues, 10, Color.parseColor("#298A08")));
        createLegendLabel(new String[]{"#298A08"}, new String[]{"脈搏"});
//        progressBarV.setVisibility(GONE);
//        nodataV.setVisibility(GONE);
    }

    public void createMomTemperatureChart(BOBMomTemperatureResponse data) {
        if (data == null || data.getExams() == null || data.getExams().size() < 1) {
//            nodataV.setVisibility(VISIBLE);
//            progressBarV.setVisibility(GONE);
            return;
        }

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();

        for(int i=0; i<data.getExams().size(); i++){
            BOBMomTemperature exam = data.getExams().get(i);
            xValues.add(exam.getExam_date());
            yValues.add(exam.getTemperature());
        }

        createChartView(data.getExams(), createSingleLineChart(xValues, yValues, 10, Color.parseColor("#298A08")));
        createLegendLabel(new String[]{"#298A08"}, new String[]{"體溫"});
//        progressBarV.setVisibility(GONE);
//        nodataV.setVisibility(GONE);
    }


    public void createMomBreathChart(BOBMomBreathResponse data) {
        if (data == null || data.getExams() == null || data.getExams().size() < 1) {
//            nodataV.setVisibility(VISIBLE);
//            progressBarV.setVisibility(GONE);
            return;
        }

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();

        for(int i=0; i<data.getExams().size(); i++){
            BOBMomBreath exam = data.getExams().get(i);
            xValues.add(exam.getExam_date());
            yValues.add(exam.getBreath());
        }

        createChartView(data.getExams(), createSingleLineChart(xValues, yValues, 5, Color.parseColor("#298A08")));
        createLegendLabel(new String[]{"#298A08"}, new String[]{"呼吸"});
//        progressBarV.setVisibility(GONE);
//        nodataV.setVisibility(GONE);
    }

    public void createMomWeightChart(BOBMomWeightResponse data) {
        if (data == null || data.getExams() == null || data.getExams().size() < 1) {
//            nodataV.setVisibility(VISIBLE);
//            progressBarV.setVisibility(GONE);
            return;
        }

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();
        for(int i=0; i<data.getExams().size(); i++){
            BOBMomWeight exam = data.getExams().get(i);
            xValues.add(exam.getExam_date());
            yValues.add(exam.getWeight());
        }

        createChartView(data.getExams(), createSingleLineChart(xValues, yValues, 5, Color.parseColor("#298A08")));
        createLegendLabel(new String[]{"#298A08"}, new String[]{"體重"});
//        progressBarV.setVisibility(GONE);
//        nodataV.setVisibility(GONE);
    }

    private View createBarAndScatterChart(ArrayList<String> xList, ArrayList<Integer> y1List,
                                          ArrayList<Integer> y2List, ArrayList<Boolean> y3List,
                                          int yLabels, int color1, int color2, int color3){
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        XYSeries mSeries_1 = new XYSeries("");
        XYSeriesRenderer mRenderer_1 = new XYSeriesRenderer();
        mRenderer_1.setColor(color1);
        mRenderer_1.setDisplayChartValues(false);
        mRenderer_1.setLineWidth(1);

        XYSeries mSeries_2 = new XYSeries("");
        XYSeriesRenderer mRenderer_2 = new XYSeriesRenderer();
        mRenderer_2.setColor(color2);
        mRenderer_2.setDisplayChartValues(false); // 在柱子顶端显示数值
        mRenderer_2.setLineWidth(1);

        XYSeries mSeries_3 = new XYSeries("");
        XYSeriesRenderer mRenderer_3 = new XYSeriesRenderer();
        mRenderer_3.setColor(color3);
        mRenderer_3.setPointStyle(PointStyle.CIRCLE);
        mRenderer_3.setFillPoints(true);
        mRenderer_3.setLineWidth(6);

        int yMaxValue = 0;
        int yMinVale = Integer.MAX_VALUE;
        int xMaxValue = xList.size();
        int xMinVale = 0;

        for(int i=0; i < xMaxValue; i++){
            try {
                int index = xMaxValue - i -1;
                String xValue = xList.get(index);
                int y1 = y1List.get(index);
                int y2 = y2List.get(index);
                boolean y3 = y3List.get(index);

                if(y1 > yMaxValue){
                    yMaxValue = y1;
                }

                if(y2 > yMaxValue){
                    yMaxValue = y2;
                }

                if(y1 < yMinVale){
                    yMinVale = y1;
                }

                if(y2 < yMinVale){
                    yMinVale = y2;
                }

                if(i % 10 == 0){
                    mRenderer.addXTextLabel(i, xValue);
                }else{
                    mRenderer.addXTextLabel(i, "");
                }

                mSeries_1.add(i, y1);
                mSeries_2.add(i, y2);

                if(y3){
                    mSeries_3.add(i, 15);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mDataset.addSeries(0, mSeries_3);
        mDataset.addSeries(0, mSeries_2);
        mDataset.addSeries(0, mSeries_1);

        mRenderer.addSeriesRenderer(0, mRenderer_3);
        mRenderer.addSeriesRenderer(0, mRenderer_2);
        mRenderer.addSeriesRenderer(0, mRenderer_1);

        initRendererInfo(mRenderer, xMinVale, xMaxValue, yMinVale, yMaxValue);
        mRenderer.setPanLimits(new double[] { xMinVale -1 , (xMaxValue*2) - 1, 0, yMaxValue});//设置拉动的范围
        mRenderer.setMargins(new int[] {10, 70, 0, 0});
        mRenderer.setYLabels(yLabels/5);
        mRenderer.setXAxisMin(xMinVale-1);// 设置X轴起点
        mRenderer.setYAxisMin(0);
        mRenderer.setYLabelsPadding(10);
        mRenderer.setFitLegend(true);
        mRenderer.setBarSpacing(0.2f);
        mRenderer.setPointSize(12f);

        CombinedXYChart.XYCombinedChartDef[] types = new CombinedXYChart.XYCombinedChartDef[]{
                new CombinedXYChart.XYCombinedChartDef(BarChart.TYPE, 0), new CombinedXYChart.XYCombinedChartDef(BarChart.TYPE, 1),
                new CombinedXYChart.XYCombinedChartDef(ScatterChart.TYPE, 2)};


        View v = ChartFactory.getCombinedXYChartView(getContext(), mDataset, mRenderer, types);

        return v;
    }

    private View createDoubleLineChart(ArrayList<String> xList, ArrayList<Float> y1List, ArrayList<Float> y2List, int yLabels, int color1, int color2){
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        XYSeries mSeries_1 = new XYSeries("");
        XYSeriesRenderer mRenderer_1 = new XYSeriesRenderer();
        mRenderer_1.setColor(color1);
        mRenderer_1.setLineWidth(4f);

        XYSeries blackSeries_1 = new XYSeries("");
        XYSeriesRenderer blackRenderer_1 = new XYSeriesRenderer();
        blackRenderer_1.setColor(Color.parseColor("#000000"));
        blackRenderer_1.setPointStyle(PointStyle.CIRCLE);
        blackRenderer_1.setDisplayChartValues(true);
        blackRenderer_1.setDisplayChartValuesDistance(0);
        blackRenderer_1.setChartValuesSpacing(15);
        blackRenderer_1.setChartValuesTextSize(20);
        blackRenderer_1.setFillPoints(true);
        blackRenderer_1.setLineWidth(6);
        DecimalFormat sdf = new DecimalFormat(".0");
        blackRenderer_1.setChartValuesFormat(sdf);//設定折線點的值的格式,顯示小數點後2位


        XYSeries mSeries_2 = new XYSeries("");
        XYSeriesRenderer mRenderer_2 = new XYSeriesRenderer();
        mRenderer_2.setColor(color2);
        mRenderer_2.setLineWidth(4f);

        XYSeries blackSeries_2 = new XYSeries("");
        XYSeriesRenderer blackRenderer_2 = new XYSeriesRenderer();
        blackRenderer_2.setColor(Color.parseColor("#000000"));
        blackRenderer_2.setPointStyle(PointStyle.CIRCLE);
        blackRenderer_2.setDisplayChartValues(true);
        blackRenderer_2.setDisplayChartValuesDistance(0);
        blackRenderer_2.setChartValuesSpacing(15);
        blackRenderer_2.setChartValuesTextSize(20);
        blackRenderer_2.setFillPoints(true);
        blackRenderer_2.setLineWidth(6);
        DecimalFormat ddf = new DecimalFormat(".0");
        blackRenderer_2.setChartValuesFormat(ddf);//設定折線點的值的格式,顯示小數點後2位


        float yMaxValue = 0;
        float yMinVale = Integer.MAX_VALUE;

        int xMaxValue = xList.size();
        int xMinVale = 0;

        for(int i=0; i < xMaxValue; i++){
            int index = xMaxValue - i -1;
            try {
                String xValue = xList.get(index);
                float y1 = y1List.get(index);
                float y2 = y2List.get(index);

                if(y1 > yMaxValue){
                    yMaxValue = y1;
                }

                if(y2 > yMaxValue){
                    yMaxValue = y2;
                }

                if(y1 < yMinVale){
                    yMinVale = y1;
                }

                if(y2 < yMinVale){
                    yMinVale = y2;
                }

                if(i % 5 == 0){
                    mRenderer.addXTextLabel(i*2, xValue);
                }else{
                    mRenderer.addXTextLabel(i*2, "");
                }

                mSeries_1.add(i*2, y1);
                mSeries_2.add(i*2, y2);
                blackSeries_1.add(i*2, y1);
                blackSeries_2.add(i*2, y2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mDataset.addSeries(0, blackSeries_2);
        mDataset.addSeries(0, blackSeries_1);
        mDataset.addSeries(0, mSeries_2);
        mDataset.addSeries(0, mSeries_1);

        mRenderer.addSeriesRenderer(0, blackRenderer_2);
        mRenderer.addSeriesRenderer(0, blackRenderer_1);
        mRenderer.addSeriesRenderer(0, mRenderer_2);
        mRenderer.addSeriesRenderer(0, mRenderer_1);

        initRendererInfo(mRenderer, xMinVale, xMaxValue, yMinVale, (yMaxValue+10));
        mRenderer.setYLabels(yLabels);
        mRenderer.setPanLimits(new double[] { xMinVale - 1 , (xMaxValue * 2) + xMaxValue - 1, yMaxValue-5, yMaxValue+5});//设置拉动的范围 : 間距*2 所以需多加 xMaxValue

        CombinedXYChart.XYCombinedChartDef[] types = new CombinedXYChart.XYCombinedChartDef[]{
                new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 0),
                new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 1),
                new CombinedXYChart.XYCombinedChartDef(ScatterChart.TYPE, 2),
                new CombinedXYChart.XYCombinedChartDef(ScatterChart.TYPE, 3)};

        return ChartFactory.getCombinedXYChartView(getContext(), mDataset, mRenderer, types);
    }


    private View createSingleLineChart(ArrayList<String> xList, ArrayList<Float> yList, int yLabels, int color){
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        XYSeries ySeries = new XYSeries("");
        XYSeriesRenderer yRenderer = new XYSeriesRenderer();
        yRenderer.setColor(color);
        yRenderer.setLineWidth(4f);

        XYSeries yBlackSeries = new XYSeries("");
        XYSeriesRenderer yBlackRenderer = new XYSeriesRenderer();
        yBlackRenderer.setColor(Color.parseColor("#000000"));
        yBlackRenderer.setPointStyle(PointStyle.CIRCLE);
        yBlackRenderer.setDisplayChartValues(true);
        yBlackRenderer.setDisplayChartValuesDistance(0);
        yBlackRenderer.setChartValuesSpacing(15);
        yBlackRenderer.setChartValuesTextSize(20);
        yBlackRenderer.setFillPoints(true);
        yBlackRenderer.setLineWidth(6);
        DecimalFormat sdf = new DecimalFormat(".0");
        yBlackRenderer.setChartValuesFormat(sdf);//設定折線點的值的格式,顯示小數點後2位

        float yMaxValue = 0;
        float yMinVale = Integer.MAX_VALUE;

        int xMaxValue = xList.size();
        int xMinVale = 0;

        for(int i=0; i < xMaxValue; i++){
            int index = xMaxValue - i -1;
            String xValue = xList.get(index);
            float yValue = yList.get(index);
            try {
                if(yValue > yMaxValue){
                    yMaxValue = yValue;
                }

                if(yValue < yMinVale){
                    yMinVale = yValue;
                }

                if(i % 5 == 0){
                    mRenderer.addXTextLabel(i*2, xValue);
                }else{
                    mRenderer.addXTextLabel(i*2, "");
                }

                ySeries.add(i*2, yValue);
                yBlackSeries.add(i*2, yValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mDataset.addSeries(0, yBlackSeries);
        mDataset.addSeries(0, ySeries);

        mRenderer.addSeriesRenderer(0, yBlackRenderer);
        mRenderer.addSeriesRenderer(0, yRenderer);

        initRendererInfo(mRenderer, xMinVale, xMaxValue, yMinVale, yMaxValue);
        mRenderer.setYLabels(yLabels);
        mRenderer.setPanLimits(new double[] { xMinVale - 1 , (xMaxValue * 2) + xMaxValue - 1, yMaxValue-5, yMaxValue+5});//设置拉动的范围 : 間距*2 所以需多加 xMaxValue


        CombinedXYChart.XYCombinedChartDef[] types = new CombinedXYChart.XYCombinedChartDef[]{
                new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 0),
                new CombinedXYChart.XYCombinedChartDef(ScatterChart.TYPE, 1)};

        return ChartFactory.getCombinedXYChartView(getContext(), mDataset, mRenderer, types);
    }

    private void createChartView(List data, View chartV){
        //float ratio = (float) Math.ceil(getWidth() / data.size());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mChartLayout.getLayoutParams();
        int lineWidth = (int)(getWidth() / 20);
        params.width = (data.size() * lineWidth); //6*getWidth(); //(int)(data.size() * (20 * density)); //(int)((2*getWidth())*ratio);
        if( params.width < getWidth()){
            params.width = getWidth();
        }
        mChartLayout.removeAllViews();
        mChartLayout.addView(chartV, params);
    }

    private void createLegendLabel(String[] color, String[] name){

        if(color.length > 0){
            chart1_txtV.setText(name[0]);
            chart1_txtV.setTextColor(Color.parseColor(color[0]));
            chart1_bg.setBackgroundColor(Color.parseColor(color[0]));
            chart1_txtV.setVisibility(VISIBLE);
            chart1_bg.setVisibility(VISIBLE);
        }else{
            chart1_txtV.setVisibility(INVISIBLE);
            chart1_bg.setVisibility(INVISIBLE);
        }

        if(color.length > 1){
            chart2_txtV.setText(name[1]);
            chart2_txtV.setTextColor(Color.parseColor(color[1]));
            chart2_bg.setBackgroundColor(Color.parseColor(color[1]));
            chart2_txtV.setVisibility(VISIBLE);
            chart2_bg.setVisibility(VISIBLE);
        }else{
            chart2_txtV.setVisibility(INVISIBLE);
            chart2_bg.setVisibility(INVISIBLE);
        }

        if(color.length > 2){
            chart3_txtV.setText(name[2]);
            chart3_txtV.setTextColor(Color.parseColor(color[2]));
            chart3_bg.setBackgroundColor(Color.parseColor(color[2]));
            chart3_txtV.setVisibility(VISIBLE);
            chart3_bg.setVisibility(VISIBLE);
        }else{
            chart3_txtV.setVisibility(INVISIBLE);
            chart3_bg.setVisibility(INVISIBLE);
        }

        if(color.length > 3){
            chart4_txtV.setText(name[3]);
            chart4_txtV.setTextColor(Color.parseColor(color[3]));
            chart4_bg.setBackgroundColor(Color.parseColor(color[3]));
            chart4_txtV.setVisibility(VISIBLE);
            chart4_bg.setVisibility(VISIBLE);
        }else{
            chart4_txtV.setVisibility(INVISIBLE);
            chart4_bg.setVisibility(INVISIBLE);
        }

        if(color.length > 4){
            chart5_txtV.setText(name[4]);
            chart5_txtV.setTextColor(Color.parseColor(color[4]));
            chart5_bg.setBackgroundColor(Color.parseColor(color[4]));
            chart5_txtV.setVisibility(VISIBLE);
            chart5_bg.setVisibility(VISIBLE);
        }else{
            chart5_txtV.setVisibility(INVISIBLE);
            chart5_bg.setVisibility(INVISIBLE);
        }
    }

    private void initRendererInfo(XYMultipleSeriesRenderer mRenderer, float xMinVale, float xMaxValue, float yMinVale, float yMaxValue){
        mRenderer.setXLabels(0); //設定X軸不顯示數字, 改以程式設定文字
        mRenderer.setYLabels(0);
        mRenderer.setPanEnabled(true, true);//设置滑动,这边是横向可以滑动,竖向不可滑动
        mRenderer.setZoomEnabled(false, false);//X, Y軸不可放大縮小
        mRenderer.setShowGrid(true);// 显示网格
        mRenderer.setYAxisMax(yMaxValue + 5);//设置Y轴最大值
        mRenderer.setYAxisMin(yMinVale - 5);
        mRenderer.setXAxisMax(xMaxValue);
        mRenderer.setXAxisMin(xMinVale - 1);// 设置X轴起点
        mRenderer.setXLabelsPadding(10);//設定標籤的間距
        mRenderer.setYLabelsPadding(10);
        mRenderer.setLabelsTextSize(20);//設定Label字型大小
        mRenderer.setXLabelsAlign(Paint.Align.LEFT);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setPanLimits(new double[] { xMinVale - 1 , (xMaxValue * 2) - 1, yMaxValue-5, yMaxValue+5});//设置拉动的范围
        mRenderer.setGridColor(Color.GRAY); //設定網格的顏色
        mRenderer.setMarginsColor(Color.parseColor("#FFFFFF"));//背景色
        mRenderer.setMargins(new int[] {5, 70, 0, 0});
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setBarSpacing(10f);
        //mRenderer.setLegendHeight(90);//格線底部到螢幕底部的高度 是否需要全螢幕
        //mRenderer.setLegendTextSize(30);
        mRenderer.setShowLegend(false);
        mRenderer.setPointSize(5f);
    }
}
