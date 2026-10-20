package li.com.bobsonclinic.manager;

/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import li.com.bobsonclinic.BOBApplication;
import li.com.bobsonclinic.data.BOBBabies;
import li.com.bobsonclinic.data.BOBBabyCamera;
import li.com.bobsonclinic.data.BOBBabyExam;
import li.com.bobsonclinic.data.BOBBabySwim;
import li.com.bobsonclinic.data.BOBConsumerList;
import li.com.bobsonclinic.data.BOBIntro;
import li.com.bobsonclinic.data.BOBMemberItem;
import li.com.bobsonclinic.data.BOBMomData;
import li.com.bobsonclinic.data.BOBMomExam;
import li.com.bobsonclinic.data.BOBMomTemPulPre;
import li.com.bobsonclinic.data.BOBMonthActivity;
import li.com.bobsonclinic.data.BOBNotifaction;
import li.com.bobsonclinic.data.BOBSubIntro;
import li.com.bobsonclinic.data.BOBTodaySchedule;
import li.com.bobsonclinic.data.BOBUIRightList;
import li.com.bobsonclinic.data.BOBVasList;
import li.com.bobsonclinic.request.base.BOBAPI;


public class BOBDataManager {
	private static BOBDataManager systemMgr = null;
	private Context mContext;
	public final static String INTRO_ABOUT_US = "AboutUS";
	public final static String INTRO_EDU = "Education";
	private Map<String, BOBUIRightList> mAboutUsData = new LinkedHashMap<String, BOBUIRightList>(); //關於我們
	private Map<String, BOBUIRightList> mEducationData = new LinkedHashMap<String, BOBUIRightList>(); //衛教資訊
	private List<BOBVasList> mVasList = new ArrayList<>(); //加值服務
	private List<BOBMonthActivity> mMonthList = new ArrayList<>(); //本月課程
	private Map<String, List<BOBMonthActivity>> mMonthMap = new LinkedHashMap<String, List<BOBMonthActivity>>(); //?月課程 key is month
	private List<BOBBabies> mBabyList = new ArrayList<>();
	private List<BOBConsumerList> mConsumerList = new ArrayList<>();
	private List<BOBNotifaction> mMessageList = new ArrayList<>();
	private List<BOBTodaySchedule> mTodayList = new ArrayList<>();
	private Map<Integer, BOBMomExam> mMomExamMap = new HashMap<Integer, BOBMomExam>();
	private Map<Integer, BOBBabyExam> mBabyExamMap = new HashMap<Integer, BOBBabyExam>();
	private LinkedList<BOBMomExam> mMomExamList = new LinkedList<>();
	private LinkedList<BOBBabyExam> mBabyExamList = new LinkedList<>();
	private List<BOBMomTemPulPre> mMomTemPulPreList = new ArrayList();
	private Map<String, String> mExplain = new HashMap<String, String>();
	private BOBBabyCamera mCamera = new BOBBabyCamera();
	private BOBMomData mMomeyData = new BOBMomData();
	private BOBBabySwim mBabySwim = new BOBBabySwim();
	private String selectBabyId, selectBabyName;

	private String recordRtspPath;

	private Map<String, String> babyVideoMap = new HashMap<String, String>();
	private List<BOBMemberItem> memberList = new ArrayList();
	private Map<String, String> myBabyMap = new HashMap<String, String>();
	private Map<String, String> momeyMap = new HashMap<String, String>();

	public static synchronized BOBDataManager getInstance(){
		if(systemMgr == null)
			systemMgr = new BOBDataManager();
		return systemMgr;
	}

	private BOBDataManager(){
		mContext = BOBApplication.getAppContext();
	}

	public Map<String, BOBUIRightList> getAboutUsData() {
		return mAboutUsData;
	}

	public Map<String, BOBUIRightList> getEducationData() {
		return mEducationData;
	}
	public Map<String, String> getBabyVideoMap() {
		return babyVideoMap;
	}

	public List<BOBMemberItem> getMemberList() {
		return this.memberList;
	}

	public Map<String, String> getMyBabyMap() {
		return this.myBabyMap;
	}

	public Map<String, String> getMomeyMap() {
		return this.momeyMap;
	}

	public Map<String, List<BOBMonthActivity>> getMonthClassMap() {
		return this.mMonthMap;
	}

	public void setMonthMap(Map<String, List<BOBMonthActivity>> mMonthMap) {
		this.mMonthMap = mMonthMap;
	}

	public List<BOBTodaySchedule> getTodayList() {
		return mTodayList;
	}

	public void setTodayList(List<BOBTodaySchedule> mTodayList) {
		this.mTodayList = mTodayList;
	}

	public List<BOBNotifaction> getMessageList() {
		return mMessageList;
	}

	public void setMessageList(List<BOBNotifaction> mMessageList) {
		this.mMessageList = mMessageList;
	}

	public BOBMomData getMomeyData() {
		return mMomeyData;
	}

	public List<BOBMonthActivity> getMonthList() {
		return mMonthList;
	}

	public void setMonthList(List<BOBMonthActivity> mMonthList) {
		this.mMonthList = mMonthList;
	}

	public void setMomeyData(BOBMomData momeyData) {
		this.mMomeyData = momeyData;
	}

	public void setData(){
//		setMemberList();
		setMyBabyMap();
		setBabyVideoMap();
		setMomeyMap();
		setExplainDefault();
	}

	public void cleanData(){
		if(this.mBabySwim != null) this.mBabySwim = new BOBBabySwim();
		if(this.mMomeyData != null) this.mMomeyData = new BOBMomData();
		if(this.mCamera != null) this.mCamera = new BOBBabyCamera();
		if(this.mBabyList != null) this.mBabyList.clear();
		if(this.mConsumerList != null) this.mConsumerList.clear();
		if(this.mMessageList != null) this.mMessageList.clear();
		if(this.memberList != null) this.memberList.clear();
		if(this.mMomExamMap != null) this.mMomExamMap.clear();
		if(this.mBabyExamMap != null) this.mBabyExamMap.clear();
		if(this.mMomExamList != null) this.mMomExamList.clear();
		if(this.mMomTemPulPreList != null) this.mMomTemPulPreList.clear();
	}

	public String getBabyId() {
		return selectBabyId;
	}

	public void setBabyId(String selectBabyId) {
		this.selectBabyId = selectBabyId;
	}

	public String getSelectBabyName() {
		return selectBabyName;
	}

	public void setSelectBabyName(String selectBabyName) {
		this.selectBabyName = selectBabyName;
	}

	public void setBaiesList(List<BOBBabies> list){
		this.mBabyList = list;
		if(list != null && list.size() > 0){
			setBabyId(list.get(0).getId());
		}
	}

	public List<BOBBabies> getBabyLst(){
		return this.mBabyList;
	}

	public List<BOBVasList> getVasList() {
		return mVasList;
	}

	public void setVasList(List<BOBVasList> mVasList) {
		this.mVasList = mVasList;
	}

	public List<BOBConsumerList> getConsumerList() {
		return mConsumerList;
	}

	public void setConsumerList(List<BOBConsumerList> mConsumerList) {
		this.mConsumerList = mConsumerList;
	}

	public void setCategoryIntro(String category, BOBIntro intro){
		if(category.equals(INTRO_ABOUT_US)){
			setAboutusData(intro);
		} else if(category.equals(INTRO_EDU)){
			setEducationData(intro);
		}
	}

	public void setCategorySubIntro(String category, String introId, BOBSubIntro intro){
		if(category.equals(INTRO_ABOUT_US)){
			setAboutusSubData(introId, intro);
		} else if(category.equals(INTRO_EDU)){
			setEducationData(introId, intro);
		}
	}

	public void setAboutusData(BOBIntro intro) {
		BOBUIRightList header;

		if(mAboutUsData.get(intro.getId()) == null){
			header = new BOBUIRightList();

		} else {
			header = mAboutUsData.get(intro.getId());
		}

		header.setContentUrl(BOBAPI.getAuthDomainURL() + intro.getEntry());
		header.setCss_path(intro.getCss_path());
		header.setTitle(intro.getTitle());
		mAboutUsData.put(intro.getId(), header);
	}

	public void setAboutusSubData(String introId, BOBSubIntro subIntro) {
		BOBUIRightList header;
		BOBUIRightList child;

		if(mAboutUsData.get(introId) == null){
			header = new BOBUIRightList();

		} else {
			header = mAboutUsData.get(introId);
		}

		if(mAboutUsData.get(introId).getSubData().get(subIntro.getId()) == null){
			child = new BOBUIRightList();

		} else {
			child = mAboutUsData.get(introId).getSubData().get(subIntro.getId());
		}

		child.setContentUrl(BOBAPI.getAuthDomainURL()+subIntro.getEntry());
		child.setCss_path(subIntro.getCss_path());
		child.setTitle(subIntro.getTitle());
		header.getSubData().put(subIntro.getId(), child);
		mAboutUsData.put(introId, header);
	}

	public void setEducationData(BOBIntro intro) {
		BOBUIRightList header;

		if(mEducationData.get(intro.getId()) == null){
			header = new BOBUIRightList();

		} else {
			header = mEducationData.get(intro.getId());
		}

		header.setContentUrl(BOBAPI.getAuthDomainURL() + intro.getEntry());
		header.setCss_path(intro.getCss_path());
		header.setTitle(intro.getTitle());
		mEducationData.put(intro.getId(), header);
	}

	public void setEducationData(String introId, BOBSubIntro subIntro) {
		BOBUIRightList header;
		BOBUIRightList child;

		if(mEducationData.get(introId) == null){
			header = new BOBUIRightList();

		} else {
			header = mEducationData.get(introId);
		}

		if(mEducationData.get(introId).getSubData().get(subIntro.getId()) == null){
			child = new BOBUIRightList();

		} else {
			child = mEducationData.get(introId).getSubData().get(subIntro.getId());
		}

		child.setContentUrl(BOBAPI.getAuthDomainURL() + subIntro.getEntry());
		child.setCss_path(subIntro.getCss_path());
		child.setTitle(subIntro.getTitle());
		header.getSubData().put(subIntro.getId(), child);
		mEducationData.put(introId, header);
	}

	private void setMomeyMap(){
		momeyMap = new  LinkedHashMap<String, String>();
		momeyMap.put("1", "檢查總表");
		momeyMap.put("2", "每日體溫丶脈搏丶血壓");
		momeyMap.put("3", "體溫");
		momeyMap.put("4", "血壓");
		momeyMap.put("5", "呼吸");
		momeyMap.put("6", "脈搏");
	}

	private void setBabyVideoMap(){
		babyVideoMap =new LinkedHashMap<String, String>();
		babyVideoMap.put("1", "寶寶視訊");
//		babyVideoMap.put("2", "寶寶影片");
		babyVideoMap.put("3", "寶寶照片");
//		babyVideoMap.put("4", "寶寶游泳");
	}

	private void setMyBabyMap(){
		myBabyMap = new  LinkedHashMap<>();
		myBabyMap.put("1", "檢查總表");
		myBabyMap.put("2", "黃疸指數");
		myBabyMap.put("3", "體溫");
		myBabyMap.put("4", "體重");
		myBabyMap.put("5", "奶量");
		myBabyMap.put("6", "寶寶脈搏");
		myBabyMap.put("7", "寶寶呼吸");
	}


	private void setMemberList(){
		memberList = new ArrayList<BOBMemberItem>();
		List<String> history = new ArrayList<String>();
		BOBMemberItem item = new BOBMemberItem();
		item.setPosition("院長");
		item.setName("劉壽懷");
		item.setAvatar("https://dl.dropboxusercontent.com/u/18183877/baby1.jpg");
		history.add("台北醫學院醫師");
		history.add("台北國泰醫院婦產部總醫師、主治醫師");
		history.add("中華民國婦產科專科醫師");
		history.add("衛生署優生保健醫師");
		item.setHistory(history);
		memberList.add(item);


		item.setPosition("行政院長");
		item.setName("劉百峯");
		item.setAvatar("https://dl.dropboxusercontent.com/u/18183877/baby1.jpg");
		history = new ArrayList<String>();
		history.add("中國醫藥學院醫學士");
		history.add("長庚醫院婦產科專科醫師");
		history.add("中華民國婦科專科醫師");
		history.add("衛生署優生保健醫師師");
		item.setHistory(history);
		memberList.add(item);

		item.setPosition("主治醫師");
		item.setName("柯伯均");
		item.setAvatar("https://dl.dropboxusercontent.com/u/18183877/baby1.jpg");
		history = new ArrayList<String>();
		history.add("台北醫學大學醫學系");
		history.add("教育部部定講師");
		history.add("林口長庚醫院婦產部婦女泌尿主治醫師");
		history.add("中華民國婦產科專科醫師");
		history.add("中華民國周產期專科醫師");
		history.add("台灣婦女秘尿曁骨盆重建專科醫師");
		history.add("中華民國婦產科內視醫學會會員");
		item.setHistory(history);
		memberList.add(item);

		item.setPosition("主治醫師");
		item.setName("林靜怡");
		item.setAvatar("https://dl.dropboxusercontent.com/u/18183877/baby1.jpg");
		history = new ArrayList<String>();
		history.add("高雄醫學院醫學系");
		history.add("高醫附設院婦產部總醫師");
		history.add("高雄聖功醫院婦產科主治醫師");
		item.setHistory(history);
		memberList.add(item);

		item.setPosition("小兒科主治醫師");
		item.setName("林宜霈");
		item.setAvatar("https://dl.dropboxusercontent.com/u/18183877/baby1.jpg");
		history = new ArrayList<String>();
		history.add("中國醫藥大學醫學系");
		history.add("中華民國小兒科專科醫師");
		history.add("中華民國小兒科神經專科醫師");
		history.add("台大醫院小兒科住院醫師");
		history.add("台大醫院小兒科總醫師");
		history.add("台大醫院小兒科神經科研究醫師");
		history.add("雙和醫院小兒科主治醫師");
		item.setHistory(history);
		memberList.add(item);

		item.setPosition("小兒科主治醫師");
		item.setName("曾鈺茹");
		item.setAvatar("https://dl.dropboxusercontent.com/u/18183877/baby1.jpg");
		history = new ArrayList<String>();
		history.add("台灣大學醫學系");
		history.add("中華民國小兒科專科醫師");
		history.add("林口長庚醫院婦產部婦女泌尿主治醫師");
		history.add("台大醫院小兒科住院醫師");
		history.add("台大醫院小兒科總醫師");
		history.add("台大醫院小兒腸胃科研究醫師");
		item.setHistory(history);
		memberList.add(item);
	};

	public LinkedList<BOBMomExam> getMomExamList() {
		return mMomExamList;
	}

	public void setMomExamList(LinkedList<BOBMomExam> mMomExamList) {
		this.mMomExamList = mMomExamList;
		mMomExamMap = new HashMap<Integer, BOBMomExam>();
		for(int i=0;i<mMomExamList.size();i++){
			mMomExamMap.put(i, mMomExamList.get(i));
		}
	}

	public void setBabyExamList(LinkedList<BOBBabyExam> mBabyExamList) {
		this.mBabyExamList = mBabyExamList;
		mBabyExamMap = new HashMap<Integer, BOBBabyExam>();

		if(mBabyExamList == null)
			return;

		for(int i=0;i<mBabyExamList.size();i++){
			mBabyExamMap.put(i, mBabyExamList.get(i));
		}
	}

	public LinkedList<BOBBabyExam> getBabyExamList() {
		return mBabyExamList;
	}

	public Map<Integer, BOBMomExam> getMomExamMap() {
		return mMomExamMap;
	}

	public Map<Integer, BOBBabyExam> getBabyExamMap() {
		return mBabyExamMap;
	}

	public List<BOBMomTemPulPre> getMomTemPulPreList() {
		return mMomTemPulPreList;
	}

	public void setMomTemPulPreList(List<BOBMomTemPulPre> mMomTemPulPreList) {
		this.mMomTemPulPreList = mMomTemPulPreList;
	}

	public Map<String, String> getExplain() {
		return mExplain;
	}

	public void setExplain(Map<String, String> mExplain) {
		this.mExplain = mExplain;
	}

	private void setExplainDefault(){
		mExplain.put("1", "/exp/1");
		mExplain.put("2", "/exp/2");
		mExplain.put("3", "/exp/3");
		mExplain.put("4", "/exp/4");
		mExplain.put("5", "/exp/5");
		mExplain.put("6", "/exp/6");
		mExplain.put("7", "/exp/7");
		mExplain.put("8", "/exp/8");
		mExplain.put("9", "/exp/9");
		mExplain.put("10", "/exp/10");
		mExplain.put("11", "/exp/11");
		mExplain.put("12", "/exp/12");
		mExplain.put("13", "/exp/13");
		mExplain.put("14", "/exp/14");

	}

	public String getRecordRtspPath() {
		return recordRtspPath;
	}

	public void setRecordRtspPath(String recordRtspPath) {
		this.recordRtspPath = recordRtspPath;
	}

	public BOBBabyCamera getCamera() {
		return mCamera;
	}

	public void setCamera(BOBBabyCamera mCamera) {
		this.mCamera = mCamera;
	}

	public BOBBabySwim getBabySwim() {
		return mBabySwim;
	}

	public void setBabySwim(BOBBabySwim mBabySwim) {
		this.mBabySwim = mBabySwim;
	}
}
