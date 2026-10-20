package li.com.bobsonclinic.viewer;

/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.manager.BOBSystemManager;


@SuppressLint({ "ViewConstructor", "NewApi" })
public class BOBQuickPopupView extends PopupWindow {

	public static enum QuickOrientation {HORIZONTAL, VERTICAL};
	public static enum QuickPosition {TOP, CENTER, BOTTOM};
	public static enum QuickPositionPriority {AUTO_UP, AUTO_DOWN, UP, DOWN};

	private Context context;
	private WindowManager mWindowManager;

	private ImageView arrowUp;
	private ImageView arrowDown;

	private View mRootView;
	private View mScrollView;

	private int[] mAarrowUpSize;
	private int[] mAarrowDownSize;
	private RelativeLayout mChildLayout;
	private QuickOrientation mOrientation;
	private QuickPositionPriority mPriority;
	private boolean isShowArrow = false;
	private ImageView mArrow;
	private Rect mLocation;
	private Rect viewLocation;
	private int default_rootWidth =-1;
	private int default_rootHeight =-1;

	private int rootWidth = -1;
	private int rootHeight = -1;
	private int screenWidth = -1;
	private int screenHeight = -1;
	private class MyListenerView extends RelativeLayout {

		public MyListenerView(Context context) {
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
	public BOBQuickPopupView(Context context, QuickOrientation vertical, QuickPositionPriority down, boolean isShowArrow, int width, int height, int marginTop){
		super(context);
		this.context = context;
		this.mOrientation = vertical;
		this.mPriority = down;
		this.isShowArrow = isShowArrow;
		LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRootView = inflator.inflate(R.layout.ug_view_quick_popup, null);
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		screenHeight = mWindowManager.getDefaultDisplay().getHeight();
		ColorDrawable dw = new ColorDrawable(0);//popup背景透明
		super.setContentView(mRootView);
		//super.setBackgroundDrawable(dw);
		super.setWidth(LayoutParams.WRAP_CONTENT);
		super.setHeight(LayoutParams.WRAP_CONTENT);
		super.setAnimationStyle(android.R.anim.fade_in);
		initView(width, height,marginTop);
	}
	
	private void initView(int width, int height, int marginTop){
		arrowUp = (ImageView)mRootView.findViewById(R.id.img_up);
		arrowDown = (ImageView)mRootView.findViewById(R.id.img_down);
		Drawable arrowUp_Src = context.getResources().getDrawable(R.drawable.edit_triangle_up);
		Drawable arrowDown_Src = context.getResources().getDrawable(R.drawable.edit_triangle_down);
		//設定箭頭的寬高
		mAarrowUpSize = new int[]{BOBSystemManager.getInstance().convertDpToPixel(16), BOBSystemManager.getInstance().convertDpToPixel(16)};
		mAarrowDownSize = new int[]{arrowDown_Src.getIntrinsicWidth()/2, arrowDown_Src.getIntrinsicHeight()/2};
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mAarrowUpSize[0], mAarrowUpSize[1]);
		arrowUp.setLayoutParams(params);
		arrowUp.setImageDrawable(arrowUp_Src);
		params = new RelativeLayout.LayoutParams(mAarrowDownSize[0], mAarrowDownSize[1]);
		//arrowDown.setLayoutParams(params);
		//arrowDown.setImageDrawable(arrowDown_Src);
		
		if(!isShowArrow){
			//arrowUp.setVisibility(View.GONE);
			//arrowDown.setVisibility(View.GONE);
		}
		
		int scrollWidthParam = FrameLayout.LayoutParams.WRAP_CONTENT;
		int scrollHeightParam = FrameLayout.LayoutParams.WRAP_CONTENT;
		if(width > 0){
			default_rootWidth = width;
			scrollWidthParam = FrameLayout.LayoutParams.MATCH_PARENT;
		}
			
		if(height > 0){
			default_rootHeight = height;
			scrollHeightParam = FrameLayout.LayoutParams.MATCH_PARENT;
		}
		
		RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(scrollWidthParam, scrollHeightParam);
		//scrollParams.weight = 1;
		scrollParams.setMargins(0, marginTop, 0, 0);
		ViewGroup vScrollView = (ViewGroup) mRootView.findViewById(R.id.VScrollView);
		ViewGroup hScrollView = (HorizontalScrollView)mRootView.findViewById(R.id.HScrollView);
		
		FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(width, height);
		
		mChildLayout = new MyListenerView(context);
		mChildLayout.setBackgroundColor(0);
		if(mOrientation == QuickOrientation.VERTICAL){
			hScrollView.setVisibility(View.GONE);
			vScrollView.setLayoutParams(scrollParams);
			vScrollView.addView(mChildLayout, fParams);
			//mChildLayout.setOrientation(LinearLayout.VERTICAL);
			mScrollView = vScrollView;
		}else if(mOrientation == QuickOrientation.HORIZONTAL){
			vScrollView.setVisibility(View.GONE);
			hScrollView.setLayoutParams(scrollParams);
			hScrollView.addView(mChildLayout, fParams);
			//mChildLayout.setOrientation(LinearLayout.HORIZONTAL);
			mScrollView = hScrollView;
		}
	}
	
	private void showAt(View v, Rect location){
		mLocation = location;
		screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		screenHeight = mWindowManager.getDefaultDisplay().getHeight();
		showAtLocation(v, Gravity.NO_GRAVITY, 0, 0);//執行後,MyCustomerView會收到Layout的Size改變的事件
		mRootView.setVisibility(View.INVISIBLE);
	}
	 
	private synchronized void showPopupPosition (int changeX, int changeY){
		mRootView.setVisibility(View.VISIBLE);
		rootWidth += changeX;
		rootHeight += changeY;
        
		if(default_rootWidth > 0)
			rootWidth = default_rootWidth;
    	if(default_rootHeight > 0)
    		rootHeight = default_rootHeight;
    	
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
			}
		}else if(mPriority == QuickPositionPriority.AUTO_DOWN){
			mArrow = arrowUp;
			popupTop = location.bottom;
			if((popupTop+rootHeight) > screenHeight){
				mArrow = arrowDown;
				popupTop = location.top - rootHeight;
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
		
		if((rootHeight) >= screenHeight){
			if(location.centerY() > screenHeight/2){
				//偏下
				mArrow = arrowDown;
				popupTop = 1;
				rootHeight = location.top-1;
			}else{
				//偏上
				mArrow = arrowUp;
				popupTop = location.bottom;
				rootHeight = (screenHeight - popupTop) -1;
			}
		}else if(popupTop < 1){
			popupTop = 1;
			rootHeight = location.top-1;
		}else if((popupTop+rootHeight) > screenHeight){
			popupTop = location.bottom;
			rootHeight = (screenHeight - popupTop) -1;
		}
		
		//Log.e("", "Top aft popupTop="+popupTop);
		//Log.e("", "Top aft rootHeight="+rootHeight);
		
		return popupTop;
	}
	
	private int countLeftPosition(Rect location){
		
		int[] arrowSize = null;
		if(mArrow == arrowUp)
			arrowSize = mAarrowUpSize;
		else
			arrowSize = mAarrowDownSize;
		
		int viewCenter = location.centerX();
		int popupLeft = (screenWidth - rootWidth);
		int arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		int newArrowCenter = arrowLeft + (arrowSize[0]/2);
		popupLeft += newArrowCenter - (rootWidth/2);
		
		//Log.e("", "left bef viewCenter="+viewCenter);
		//Log.e("", "left bef arrowLeft="+arrowLeft);
		//Log.e("", "left bef popupLeft="+popupLeft);
		//Log.e("", "left bef location.left="+location.left);
		
		if(popupLeft <= 0){
			popupLeft = 1;
			arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		}else if((popupLeft + rootWidth) >= screenWidth){
			popupLeft = (screenWidth - rootWidth)-1;
			if((popupLeft + rootWidth) >= screenWidth)
				popupLeft = popupLeft - ((popupLeft + rootWidth) - screenWidth) -1;
			arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		}else if((popupLeft + arrowLeft) < viewCenter){
			popupLeft += viewCenter - (popupLeft + arrowLeft);
			if((popupLeft + rootWidth) > screenWidth)
				popupLeft = popupLeft - ((popupLeft + rootWidth) - screenWidth) -1;
			arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		}
		
		if(popupLeft <= 0){
			popupLeft = 1;
			arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		}
		
		if(viewCenter + (rootWidth/2) < screenWidth && viewCenter - (rootWidth/2) > 0){
			popupLeft = viewCenter - (rootWidth/2);
			arrowLeft = viewCenter - popupLeft - (arrowSize[0]/2);
		}
		
		if(rootWidth >= screenWidth)
			rootWidth = (screenWidth-2);
				
		//Log.e("", "left aft viewCenter="+viewCenter);
		//Log.e("", "left aft arrowLeft="+arrowLeft);
		//Log.e("", "left aft popupLeft="+popupLeft);
		//Log.e("", "left aft location.left="+mLocation.left);
		//Log.e("", "left aft rootWidth="+rootWidth+", rootHeight="+rootHeight);
        
		/*
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
		*/
		if(viewCenter > screenWidth){
			//物件中心點往右移且超過視窗時
			arrowLeft -= (viewCenter - screenWidth);
		}else if(viewCenter < 0){
			//物件中心點往左移且超過視窗時
			arrowLeft -= viewCenter;
		}
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(arrowSize[0], arrowSize[1]);
		params.leftMargin = arrowLeft;
		mArrow.setLayoutParams(params);
		
		return popupLeft;
	}
	
	public static Rect locateView(View v){
	    int[] loc_int = new int[2];
	    try{
	        v.getLocationInWindow(loc_int);
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
	
	public void setContentView(View v, RelativeLayout.LayoutParams params){
		super.setContentView(mRootView);
		mChildLayout.removeAllViews();
		mChildLayout.addView(v, params);
	}

	
	public void setBackgroundResource(int resId){
		if(mScrollView != null)
			mScrollView.setBackgroundResource(resId);
	}
	
	public void setBackgroundDrawable(Drawable background){
		if(mScrollView != null)
			mScrollView.setBackground(background);
	}
	
	public void setBackgroundColor(int color){
		if(mScrollView != null)
			mScrollView.setBackgroundColor(color);
	}
	
	public void setArrowUpImageResource(int resId){
		Drawable drawable = context.getResources().getDrawable(resId);
		setArrowUpImageDrawable(drawable);
	}
	
	public void setArrowUpImageBitmap(Bitmap bmp, int width, int height){
		if(bmp == null)
			return;
		mAarrowUpSize = new int[]{bmp.getWidth()/2, bmp.getHeight()/2};
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mAarrowUpSize[0], mAarrowUpSize[1]);
		arrowUp.setLayoutParams(params);
		arrowUp.setImageBitmap(bmp);
	}
	
	public void setArrowUpImageDrawable(Drawable drawable){
		if(drawable == null)
			return;
		mAarrowUpSize = new int[]{drawable.getIntrinsicWidth()/2, drawable.getIntrinsicHeight()/2};
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mAarrowUpSize[0], mAarrowUpSize[1]);
		arrowUp.setLayoutParams(params);
		arrowUp.setImageDrawable(drawable);
	}
	
	public void setArrowDownImageResource(int resId){
		Drawable drawable = context.getResources().getDrawable(resId);
		setArrowDownImageDrawable(drawable);
	}
	
	public void setArrowDownImageBitmap(Bitmap bmp){
		if(bmp == null)
			return;
		mAarrowDownSize = new int[]{bmp.getWidth()/2, bmp.getHeight()/2};
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mAarrowDownSize[0], mAarrowDownSize[1]);
		arrowDown.setLayoutParams(params);
		arrowDown.setImageBitmap(bmp);
	}
	
	public void setArrowDownImageDrawable(Drawable drawable){
		if(drawable == null)
			return;
		mAarrowDownSize = new int[]{drawable.getIntrinsicWidth()/2, drawable.getIntrinsicHeight()/2};
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mAarrowDownSize[0], mAarrowDownSize[1]);
		arrowDown.setLayoutParams(params);
		arrowDown.setImageDrawable(drawable);
	}
	
	public void removView(View child){
		mChildLayout.removeView(child);
	}
	
	public void addChildView(View child){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		mChildLayout.addView(child, params);
	}
	
	public void addChildView(View child, RelativeLayout.LayoutParams params){
		mChildLayout.addView(child, params);
	}
	
	public void addChildView(int layoutResID) {
		LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mChildLayout.addView(inflator.inflate(layoutResID, null));
	}
	
	public void showAt(View v){
		if (v == null) 
	    	return;
		viewLocation = locateView(v);
		if (viewLocation == null) 
	    	return;
		showAt(v, viewLocation);
	}
	
	public void showAt(View v, int paddingLeft, int paddingTop){
		if (v == null) 
	    	return;
		viewLocation = locateView(v);
		if (viewLocation == null) 
	    	return;
		viewLocation.left = viewLocation.left + paddingLeft;
		viewLocation.right = viewLocation.right + paddingLeft;
		viewLocation.top = viewLocation.top + paddingTop;
		viewLocation.bottom = viewLocation.bottom+ paddingTop;
		showAt(v, viewLocation);
	}
	
	public void showAt(View v, QuickPosition position){
		if (v == null) 
	    	return;
		viewLocation = locateView(v);
		if (viewLocation == null) 
	    	return;
		
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
	@Override
	public void setWidth(int width){
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT);
		mChildLayout.setLayoutParams(params);
	}
	
	@Override
	public void setHeight(int height){
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height);
		mChildLayout.setLayoutParams(params);
	}
	
	public void setHeight(int width, int height){
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
		mChildLayout.setLayoutParams(params);
	}
	
}
