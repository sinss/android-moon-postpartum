package ui.view.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import li.com.bobsonclinic.R;


/**
 * @author Zack
 * @since 2013-04-25 13:00
 */
@SuppressLint({ "ViewConstructor", "NewApi" })
public class QuickPopupView extends PopupWindow {
	
	public static enum QuickOrientation {HORIZONTAL, VERTICAL};
	public static enum QuickPosition {TOP, CENTER, BOTTOM};
	public static enum QuickPositionPriority {AUTO_UP, AUTO_DOWN, UP, DOWN};


	private Context context;
	private WindowManager mWindowManager;

	public float SCREEN_DENSITY = 1.0f;
	public int SCREEN_WIDTH = -1;
	public int SCREEN_HEIGHT = -1;
	
	private ImageView arrowUp;
	private ImageView arrowDown;
	
	private ViewGroup mRootView;
	private ViewGroup mScrollView;
	private ViewGroup vScrollView ;
	private ViewGroup hScrollView;
	
	private int[] mAarrowUpSize;
	private int[] mAarrowDownSize;
	private LinearLayout mChildLayout;
	private QuickOrientation mOrientation;
	private QuickPositionPriority mPriority;
	private boolean isShowArrow;
	private ImageView mArrow;
	private Rect mLocation;
	private Rect viewLocation;
	private int default_rootWidth =-1; 
	private int default_rootHeight =-1;
	
	private int rootWidth = -1; 
	private int rootHeight = -1;

	private int leftAndRightPadding = 1;
	private int topAndBottomPadding = 1;

	private Handler mHandler = new Handler(Looper.getMainLooper());

	private class PopupListenerView extends LinearLayout{
		
		public PopupListenerView(Context context) {
			super(context);
		}
		
		@Override
	    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
	        super.onSizeChanged(xNew, yNew, xOld, yOld);
	        boolean isInit = false;
	        int newDx = (xNew - xOld);
	        if(rootWidth == -1)
	        	isInit = true;
			
	        if(rootHeight == -1)
	        	isInit = true;
	        
	        if(isInit){
	        	rootWidth = mRootView.getMeasuredWidth();
	        	rootHeight = mRootView.getMeasuredHeight();
	        	showPopupPosition(0, 0);
	        	return;
	        }else{
        		showPopupPosition(newDx, 0);
	        	return;
	        }
	    }
	}
	
	public QuickPopupView(Context context, QuickOrientation orientation, QuickPositionPriority priority, boolean isShowArrow){
		this(context, orientation, priority, isShowArrow, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	public QuickPopupView(Context context, QuickOrientation orientation, QuickPositionPriority priority, boolean isShowArrow, int dp_width, int dp_height){
		super(context);

		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		SCREEN_HEIGHT = metrics.heightPixels;
		SCREEN_WIDTH = metrics.widthPixels;
		SCREEN_DENSITY = metrics.density;

		this.context = context;
		this.mOrientation = orientation;
		this.mPriority = priority;
		this.isShowArrow = isShowArrow;
		LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRootView = (ViewGroup) inflator.inflate(R.layout.ui_view_popup_layout, null);
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(dm);
		ColorDrawable dw = new ColorDrawable(0);//popup背景透明
		super.setContentView(mRootView);
		super.setBackgroundDrawable(dw);
		super.setWidth(LayoutParams.WRAP_CONTENT);
		super.setHeight(LayoutParams.WRAP_CONTENT);
		super.setAnimationStyle(R.style.Popup_Fade_In_Out_Animation);
		findViews();
		initView(dp_width, dp_height);
	}

	protected void findViews() {
		arrowUp = (ImageView)mRootView.findViewById(R.id.img_up);
		arrowDown = (ImageView)mRootView.findViewById(R.id.img_down);

		vScrollView = (ScrollView)mRootView.findViewById(R.id.VScrollView);
		hScrollView = (HorizontalScrollView)mRootView.findViewById(R.id.HScrollView);
	}

	protected void initView(int dp_width, int dp_height){
		int scrollWidthParam = FrameLayout.LayoutParams.WRAP_CONTENT;
		int scrollHeightParam = FrameLayout.LayoutParams.WRAP_CONTENT;
		if(dp_width > 0){
			int px_width = dpToPixel(dp_width);
			default_rootWidth = px_width;
			scrollWidthParam = FrameLayout.LayoutParams.MATCH_PARENT;

			dp_width = px_width;
		}

		if(dp_height > 0){
			int px_height = dpToPixel(dp_height);
			default_rootHeight = px_height;
			scrollHeightParam = FrameLayout.LayoutParams.MATCH_PARENT;

			dp_height = px_height;
		}

		LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(scrollWidthParam, scrollHeightParam);
		scrollParams.weight = 1;

		FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(dp_width, dp_height);
		mChildLayout = new PopupListenerView(context);
		if(mOrientation == QuickOrientation.VERTICAL){
			hScrollView.setVisibility(View.GONE);
			vScrollView.setLayoutParams(scrollParams);
			vScrollView.addView(mChildLayout, fParams);
			mChildLayout.setOrientation(LinearLayout.VERTICAL);
			mScrollView = vScrollView;
		}else if(mOrientation == QuickOrientation.HORIZONTAL){
			vScrollView.setVisibility(View.GONE);
			hScrollView.setLayoutParams(scrollParams);
			hScrollView.addView(mChildLayout, fParams);
			mChildLayout.setOrientation(LinearLayout.HORIZONTAL);
			mScrollView = hScrollView;
		}
	}

	protected int dpToPixel(int x) {
		return (int) (SCREEN_DENSITY * (float)x);
	}

	private void showAt(View v, Rect location){
		try{
			mLocation = location;
			DisplayMetrics dm = new DisplayMetrics();
			mWindowManager.getDefaultDisplay().getMetrics(dm);
			SCREEN_WIDTH = dm.widthPixels;
			SCREEN_HEIGHT = dm.heightPixels;
			showAtLocation(v, Gravity.NO_GRAVITY, 0, 0);//執行後, PopupListenerView會收到Layout的Size改變的事件
			mRootView.setVisibility(View.INVISIBLE);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	protected void setLeftAndRightPadding(int leftAndRightPadding) {
		this.leftAndRightPadding = leftAndRightPadding;
	}

	protected void setTopAndBottomPadding(int topAndBottomPadding) {
		this.topAndBottomPadding = topAndBottomPadding;
	}

	private synchronized void showPopupPosition (int changeX, int changeY){
		mRootView.setVisibility(View.VISIBLE);
		rootWidth += changeX;
		rootHeight += changeY;
        
		if(default_rootWidth > 0) {
			rootWidth = default_rootWidth;
		}

    	if(default_rootHeight > 0) {
			rootHeight = default_rootHeight;
		}
    	
    	////Log.e("", "showPopupPosition changeX="+changeX+", changeY="+changeY);
        ////Log.e("", "showPopupPosition rootWidth="+rootWidth+", rootHeight="+rootHeight);
        ////Log.e("", "showPopupPosition screenWidth="+screenWidth+", screenHeight="+screenHeight);

		int popupTop = countTopPosition(mLocation);
		int popupLeft = countLeftPosition(mLocation);

		update(popupLeft, popupTop, rootWidth, rootHeight);
	}
	
	private int countTopPosition(Rect location){
		int popupTop = 0;
		if(mPriority == QuickPositionPriority.AUTO_UP){
			mArrow = arrowDown;
			popupTop = (location.top - rootHeight);
			if(popupTop < 1){
				mArrow = arrowUp;
				popupTop = location.bottom;
				
				if((popupTop+rootHeight) > SCREEN_HEIGHT){
					//上下都超出範圍
					mArrow = arrowDown;
					popupTop = location.centerY();
				}
			}
		}else if(mPriority == QuickPositionPriority.AUTO_DOWN){
			mArrow = arrowUp;
			popupTop = location.bottom;
			if((popupTop+rootHeight) > SCREEN_HEIGHT){
				mArrow = arrowDown;
				popupTop = location.top - rootHeight;
				
				if(popupTop < 1){
					//上下都超出範圍
					mArrow = arrowUp;
					popupTop = location.centerY();
				}
			}
		}else if(mPriority == QuickPositionPriority.UP){
			mArrow = arrowDown;
			popupTop = (location.top - rootHeight);
		}else if(mPriority == QuickPositionPriority.DOWN){
			mArrow = arrowUp;
			popupTop = location.bottom;
		}
		//Log.e("", "Top bef location.top="+location.top);
		//Log.e("", "Top bef location.bottom="+location.bottom);
		//Log.e("", "Top bef popupTop="+popupTop);
		//Log.e("", "Top bef screenHeight="+screenHeight);
		//Log.e("", "Top bef rootHeight="+rootHeight);

		if((rootHeight) >= SCREEN_HEIGHT){
			if(location.centerY() > SCREEN_HEIGHT/2){
				//偏下
				mArrow = arrowDown;
				popupTop = topAndBottomPadding;
				rootHeight = location.top - topAndBottomPadding;
			}else{
				//偏上
				mArrow = arrowUp;
				popupTop = location.bottom;
				rootHeight = (SCREEN_HEIGHT - popupTop) - topAndBottomPadding;
			}
		}else if(popupTop < 1){
			if(rootHeight == -popupTop){
			}else{
				rootHeight = location.top - topAndBottomPadding;
			}
			popupTop = topAndBottomPadding;
		}else if((popupTop+rootHeight) > SCREEN_HEIGHT){
			popupTop = location.bottom;
			rootHeight = (SCREEN_HEIGHT - popupTop) - topAndBottomPadding;
		}
		
		//Log.e("", "Top aft popupTop="+popupTop);
		//Log.e("", "Top aft rootHeight="+rootHeight);

		if(topAndBottomPadding == 0 && location.bottom - popupTop == 0){
			popupTop = location.top;
		}
		
		return popupTop;
	}

	private int countLeftPosition(Rect location){
		
		int[] arrowSize = null;
		if(mArrow == arrowUp) {
			arrowSize = mAarrowUpSize;
		}else {
			arrowSize = mAarrowDownSize;
		}
		
		int viewCenter = location.centerX();
		int popupLeft = (SCREEN_WIDTH - rootWidth);
		int arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		int newArrowCenter = arrowLeft + (arrowSize[0]/2);
		popupLeft += newArrowCenter - (rootWidth/2);
		
		//Log.e("", "left bef viewCenter="+viewCenter);
		//Log.e("", "left bef arrowLeft="+arrowLeft);
		//Log.e("", "left bef popupLeft="+popupLeft);
		//Log.e("", "left bef location.left="+location.left);
		
		if(popupLeft <= 0){
			popupLeft = leftAndRightPadding;
			arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		}else if((popupLeft + rootWidth) >= SCREEN_WIDTH){
			popupLeft = (SCREEN_WIDTH - rootWidth)-1;
			if((popupLeft + rootWidth) >= SCREEN_WIDTH)
				popupLeft = popupLeft - ((popupLeft + rootWidth) - SCREEN_WIDTH) - leftAndRightPadding;
			arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		}else if((popupLeft + arrowLeft) < viewCenter){
			popupLeft += viewCenter - (popupLeft + arrowLeft);
			if((popupLeft + rootWidth) > SCREEN_WIDTH)
				popupLeft = popupLeft - ((popupLeft + rootWidth) - SCREEN_WIDTH) - leftAndRightPadding;
			arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		}
		
		if(popupLeft <= 0){
			popupLeft = leftAndRightPadding;
			arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		}
		
		if(viewCenter + (rootWidth/2) < SCREEN_WIDTH && viewCenter - (rootWidth/2) > 0){
			popupLeft = viewCenter - (rootWidth/2);
			arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		}
		
		if(rootWidth >= SCREEN_WIDTH) {
			rootWidth = (SCREEN_WIDTH - (leftAndRightPadding * 2));
		}
				
		//Log.e("", "left aft viewCenter="+viewCenter);
		//Log.e("", "left aft arrowLeft="+arrowLeft);
		//Log.e("", "left aft popupLeft="+popupLeft);
		//Log.e("", "left aft location.left="+mLocation.left);
		//Log.e("", "left aft rootWidth="+rootWidth+", rootHeight="+rootHeight);
        
		
		if(isShowArrow && default_rootHeight > 0){
			arrowUp.setVisibility(View.GONE);
			arrowDown.setVisibility(View.GONE);
		}else if(isShowArrow){
			arrowUp.setVisibility(View.INVISIBLE);
			arrowDown.setVisibility(View.INVISIBLE);
		}
		
		if(isShowArrow && mArrow != null){
			mArrow.setVisibility(View.VISIBLE);
		}
		
		if(viewCenter > SCREEN_WIDTH){
			//物件中心點往右移且超過視窗時
			arrowLeft -= (viewCenter - SCREEN_WIDTH);
		}else if(viewCenter < 0){
			//物件中心點往左移且超過視窗時
			arrowLeft -= viewCenter;
		}
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(arrowSize[0], arrowSize[1]);
		params.leftMargin = arrowLeft;
		mArrow.setLayoutParams(params);
		
		return popupLeft;
	}

	private Runnable toastRunnable;

	protected void startToastState(int duration){
		if(toastRunnable != null){
			mHandler.removeCallbacks(toastRunnable);
		}

		toastRunnable = new Runnable() {
			@Override
			public void run() {
				try{
					if(isShowing()){
						toastRunnable = null;
						dismiss();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		};
		mHandler.postDelayed(toastRunnable, duration);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		try{
			if(toastRunnable != null){
				mHandler.removeCallbacks(toastRunnable);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		toastRunnable = null;
	}

	protected static Rect locateView(View v){
	    int[] loc_int = new int[2];
			try{
				v.getLocationOnScreen(loc_int);
	    }catch (Exception npe){
	        return null;
	    }
	    Rect location = new Rect();
	    location.left = loc_int[0];
	    location.top = loc_int[1];
	    location.right = location.left + v.getWidth();
	    location.bottom = location.top + v.getHeight();
	    return location;
	}
	
	@Override
	public void setContentView(View v){
		super.setContentView(mRootView);
		mChildLayout.removeAllViews();
		mChildLayout.addView(v);
	}

	public Context getContext() {
		return context;
	}
	
	public void setContentView(View v, LinearLayout.LayoutParams params){
		super.setContentView(mRootView);
		mChildLayout.removeAllViews();
		mChildLayout.addView(v, params);
	}


	public void setRootView(View v, LinearLayout.LayoutParams mParam, int bgResId){
		mRootView.removeView(vScrollView);
		mRootView.removeView(hScrollView);
		mScrollView.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		params.weight = 1;
		if(mChildLayout.getParent() != null) {
			((ViewGroup) mChildLayout.getParent()).removeView(mChildLayout);
		}
		mRootView.addView(mChildLayout, 1, params);
		mChildLayout.addView(v, mParam);
		mChildLayout.setBackgroundResource(bgResId);
	}


	protected void setRootViewBackgroundResource(int resId){
		if(mChildLayout != null) {
			mChildLayout.setBackgroundResource(resId);
		}
	}

	protected void setRootViewBackgroundColor(int color){
		if(mChildLayout != null) {
			mChildLayout.setBackgroundColor(color);
		}
	}

	public void setBackgroundResource(int resId){
		if(mScrollView != null) {
			mScrollView.setBackgroundResource(resId);
		}
	}
	
	public void setBackgroundDrawable(Drawable background){
		if(mScrollView != null) {
			mScrollView.setBackground(background);
		}
	}
	
	public void setBackgroundColor(int color){
		if(mScrollView != null) {
			mScrollView.setBackgroundColor(color);
		}
	}

	public void addChildView(View child){
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		mChildLayout.addView(child, params);
	}
	
	public void addChildView(View child, LinearLayout.LayoutParams params){
		mChildLayout.addView(child, params);
	}
	
	public void addChildView(int layoutResID) {
		LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mChildLayout.addView(inflator.inflate(layoutResID, null));
	}
	
	public void removeAllViews(){
		if(mChildLayout != null) {
			mChildLayout.removeAllViews();
		}
	}

	public void showToastAt(View v, int duration){
		if(!isShowing()){
			showAt(v);
		}
		startToastState(duration);
	}

	public void showToastAt(View v, int duration, int dp_paddingLeft, int dp_paddingTop){
		if(!isShowing()){
			showAt(v, dp_paddingLeft, dp_paddingTop);
		}
		startToastState(duration);
	}

	public void showToastAt(View view, int duration, QuickPosition position){
		if(!isShowing()){
			showAt(view, position);
		}
		startToastState(duration);
	}

	public void showToastAt(int duration, Rect viewLocation){
		if(!isShowing()){
			showAt(viewLocation);
		}
		startToastState(duration);
	}

	public void showAt(View v){
		if (v == null) {
			return;
		}
		viewLocation = locateView(v);
		if (viewLocation == null) {
			return;
		}
		showAt(v, viewLocation);
	}

	public void showAt(Rect viewLocation){
		if (viewLocation == null) {
			return;
		}
		this.viewLocation = viewLocation;
		showAt(new View(getContext()), viewLocation);
	}
	
	public void showAt(View v, int dp_paddingLeft, int dp_paddingTop){
		if (v == null) {
			return;
		}
		viewLocation = locateView(v);
		if (viewLocation == null) {
			return;
		}
		int px_paddingLeft = dpToPixel(dp_paddingLeft);
		int px_paddingTop = dpToPixel(dp_paddingTop);

		viewLocation.left = viewLocation.left + px_paddingLeft;
		viewLocation.right = viewLocation.right + px_paddingLeft;
		viewLocation.top = viewLocation.top + px_paddingTop;
		viewLocation.bottom = viewLocation.bottom+ px_paddingTop;
		showAt(v, viewLocation);
	}

	public void showAt(View v, QuickPosition position){
		
		if (v == null) {
			return;
		}
		viewLocation = locateView(v);
		if (viewLocation == null) {
			return;
		}

		if(position == QuickPosition.TOP){
			Rect rec = new Rect(viewLocation.left, viewLocation.top, viewLocation.right, viewLocation.top+1);
			showAt(v, rec);
		}else if(position == QuickPosition.CENTER){
			Rect rec = new Rect(viewLocation.left, viewLocation.centerY(), viewLocation.right, viewLocation.centerY()+1);
			showAt(v, rec);
		}else if(position == QuickPosition.BOTTOM){
			Rect rec = new Rect(viewLocation.left, viewLocation.bottom, viewLocation.right, viewLocation.bottom+1);
			showAt(v, rec);
		}
	}
	
	public void setArrow(int upArrow, int downArrow, int dp_width, int dp_height){
		Drawable upDrawable = null;
		Drawable downDrawable = null;

		//設定箭頭的寬高
		if(upArrow > 0){
			try{
				upDrawable = context.getDrawable(upArrow);
			}catch (Exception e){
			}catch (OutOfMemoryError o){
			}
		}

		if(downArrow > 0){
			try{
				downDrawable = context.getDrawable(downArrow);
			}catch (Exception e){
			}catch (OutOfMemoryError o){
			}
		}

		setArrow(upDrawable, downDrawable, dp_width, dp_height);
	}

	public void setArrow(Drawable upDrawable, Drawable downDrawable, int dp_width, int dp_height){
		if(upDrawable != null){
			//mAarrowUpSize = new int[]{upDrawable.getIntrinsicWidth(), upDrawable.getIntrinsicHeight()};
			mAarrowUpSize = new int[]{dpToPixel(dp_width), dpToPixel(dp_height)};
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mAarrowUpSize[0], mAarrowUpSize[1]);
			arrowUp.setLayoutParams(params);
			arrowUp.setImageDrawable(upDrawable);
		}else{
			mAarrowUpSize = new int[]{1, 1};
		}

		if(downDrawable != null){
			//mAarrowDownSize = new int[]{downDrawable.getIntrinsicWidth(), downDrawable.getIntrinsicHeight()};
			mAarrowDownSize = new int[]{dpToPixel(dp_width), dpToPixel(dp_height)};
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mAarrowDownSize[0], mAarrowDownSize[1]);
			arrowDown.setLayoutParams(params);
			arrowDown.setImageDrawable(downDrawable);
		}else{
			mAarrowDownSize = new int[]{1, 1};
		}

		if(!isShowArrow){
			arrowUp.setVisibility(View.GONE);
			arrowDown.setVisibility(View.GONE);
		}
	}
	
}
