package com.tugkgp.cn.view;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：PopoverView.java 
 * 描述：提示框
 * @author zhaoqp
 * @date：2013-11-18 下午5:02:16
 * @version v1.0
 */
public class PopOverView extends RelativeLayout implements OnTouchListener{
	
	/**
	 * AbPopoverView的监听器
	 */
	public static interface PopoverViewListener{
		/**
		 * Called when the popover is going to show
		 * @param view The whole popover view
		 */
		void popoverViewWillShow(PopOverView view);
		/**
		 * Called when the popover did show
		 * @param view The whole popover view
		 */
		void popoverViewDidShow(PopOverView view);
		/**
		 * Called when the popover is going to be dismissed
		 * @param view The whole popover view
		 */
		void popoverViewWillDismiss(PopOverView view);
		/**
		 * Called when the popover was dismissed
		 * @param view The whole popover view
		 */
		void popoverViewDidDismiss(PopOverView view);
	}
	
	
	/**
	 * Popover arrow points up. Integer to use with bit operators to tell the popover where the arrow should appear and from where the popover should appear
	 */
	public final static int PopoverArrowDirectionUp    = 0x00000001;
	/**
	 * Popover arrow points down. Integer to use with bit operators to tell the popover where the arrow should appear and from where the popover should appear
	 */
	public final static int PopoverArrowDirectionDown  = 0x00000002;
	/**
	 * Popover arrow points left. Integer to use with bit operators to tell the popover where the arrow should appear and from where the popover should appear
	 */
	public final static int PopoverArrowDirectionLeft  = 0x00000004;
	/**
	 * Popover arrow points right. Integer to use with bit operators to tell the popover where the arrow should appear and from where the popover should appear
	 */
	public final static int PopoverArrowDirectionRight = 0x00000008;
	/**
	 * Popover arrow points any direction. Integer to use with bit operators to tell the popover where the arrow should appear and from where the popover should appear
	 */
	public final static int PopoverArrowDirectionAny = PopoverArrowDirectionUp|PopoverArrowDirectionDown|PopoverArrowDirectionLeft|PopoverArrowDirectionRight;
	
	/**
	 * The delegate of the view
	 */
	private PopoverViewListener popoverViewListener;
	/**
	 * The main popover containing the view we want to show
	 */
	private RelativeLayout popoverView;
	/**
	 * The view group storing this popover. We need this so, when we dismiss the popover, we remove it from the view group
	 */
	private ViewGroup superview;
	/**
	 * The content size for the view in the popover
	 */
	private Point contentSizeForViewInPopover = new Point(0, 0);
	/**
	 * The real content size we will use (it considers the padding)
	 */
	private Point realContentSize = new Point(0, 0);
	/**
	 * A hash containing
	 */
	private Map<Integer, Rect> possibleRects;
	/**
	 * Whether the view is animating or not
	 */
	private boolean isAnimating = false;
	/**
	 * The fade animation time in milliseconds
	 */
	private int fadeAnimationTime = 300;
	/**
	 * The layout Rect, is the same as the superview rect
	 */
	private Rect popoverLayoutRect;
	/**
	 * The popover background drawable
	 */
	private Drawable backgroundDrawable;
	/**
	 * The popover arrow up drawable
	 */
	private Drawable arrowUpDrawable;
	/**
	 * The popover arrow down drawable
	 */
	private Drawable arrowDownDrawable;
	/**
	 * The popover arrow left drawable
	 */
	private Drawable arrowLeftDrawable;
	/**
	 * The popover arrow down drawable
	 */
	private Drawable arrowRightDrawable;
	
	/**
	 * 当前显示的箭头图标
	 */
	private ImageView arrowImageView = null;
	
	/**
	 * 当前显示的提示的View
	 */
	private View popoverContentView = null;
	
	
	/**
	 * Constructor to create a popover with a popover view
	 * @param context The context where we should create the popover view
	 */
	public PopOverView(Context context) {
		super(context);
		initPopoverView();
	}

	/**
	 * Constructor to create a popover with a popover view
	 * @param context The context where we should create the popover view
	 * @param attrs Attribute set to init the view
	 */
	public PopOverView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPopoverView();
	}
	
	/**
	 * Constructor to create a popover with a popover view
	 * @param context The context where we should create the popover view
	 * @param attrs Attribute set to init the view
	 * @param defStyle The default style for this view
	 */
	public PopOverView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPopoverView();
	}
	
	/**
	 * Init the popover view
	 * @param viewToEnclose The view we wan to insert inside the popover
	 */
	private void initPopoverView(){
		
		//Init the relative layout
		popoverView = new RelativeLayout(getContext());
		setBackgroundColor(Color.WHITE);
		setOnTouchListener(this);
	}
	
	/**
	 * Get the Rect frame for a view (relative to the Window of the application)
	 * @param v The view to get the rect from
	 * @return The rect of the view, relative to the application window
	 */
	public static Rect getFrameForView(View v){
		int location [] = new int [2];
		v.getLocationOnScreen(location);
		Rect viewRect = new Rect(location[0], location[1], location[0]+v.getWidth(), location[1]+v.getHeight());
		return viewRect;
	}
	
	
	/**
	 * Add the popover to the view with a defined rect inside the popover
	 * @param insertRect The rect we want to insert the view
	 */
	private void addPopoverInRect(Rect insertRect){
		//Set layout params
		LayoutParams insertParams = new LayoutParams(insertRect.width(), insertRect.height());
		insertParams.leftMargin = insertRect.left;
		insertParams.topMargin = insertRect.top;
		//Add the view
		addView(popoverView, insertParams);
		
	}
	
	
	private void initArrow(Rect originRect, Integer arrowDirection){
		
		//重新定位
		if(arrowImageView != null){
			removeView(arrowImageView);
		}
		
		//Add arrow drawable
		arrowImageView = new ImageView(getContext());
		Drawable arrowDrawable = null;
		int xPos = 0;
		int arrowWidth = 0;
		int yPos = 0;
		int arrowHeight = 0;
		//Get correct drawable, and get Width, Height, Xpos and yPos depending on the selected arrow direction
		if (arrowDirection == PopOverView.PopoverArrowDirectionUp){
			arrowDrawable = arrowUpDrawable;
			arrowWidth = arrowDrawable.getIntrinsicWidth();
			arrowHeight = arrowDrawable.getIntrinsicHeight();
			xPos = originRect.centerX() - (arrowWidth/2) - popoverLayoutRect.left;
			yPos = originRect.bottom - popoverLayoutRect.top;
		}
		else if (arrowDirection == PopOverView.PopoverArrowDirectionDown){
			arrowDrawable = arrowDownDrawable;
			arrowWidth = arrowDrawable.getIntrinsicWidth();
			arrowHeight = arrowDrawable.getIntrinsicHeight();
			xPos = originRect.centerX() - (arrowWidth/2) - popoverLayoutRect.left;
			yPos = originRect.top - arrowHeight - popoverLayoutRect.top;
		}
		else if (arrowDirection == PopOverView.PopoverArrowDirectionLeft){
			arrowDrawable = arrowLeftDrawable;
			arrowWidth = arrowDrawable.getIntrinsicWidth();
			arrowHeight = arrowDrawable.getIntrinsicHeight();
			xPos = originRect.right - popoverLayoutRect.left;
			yPos = originRect.centerY() - (arrowHeight/2) - popoverLayoutRect.top;
		}
		else if (arrowDirection == PopOverView.PopoverArrowDirectionRight){
			arrowDrawable = arrowRightDrawable;
			arrowWidth = arrowDrawable.getIntrinsicWidth();
			arrowHeight = arrowDrawable.getIntrinsicHeight();
			xPos = originRect.left - arrowWidth - popoverLayoutRect.left;
			yPos = originRect.centerY() - (arrowHeight/2) - popoverLayoutRect.top;
		}
		//Set drawable
		arrowImageView.setImageDrawable(arrowDrawable);
		//Init layout params
		LayoutParams arrowParams = new LayoutParams(arrowWidth, arrowHeight);
		arrowParams.leftMargin = xPos;
		arrowParams.topMargin = yPos;
		
		//add view
		addView(arrowImageView, arrowParams);
	}
	
	
	/**
	 * Calculates the rect for showing the view with Arrow Up
	 * @param originRect The origin rect
	 * @return The calculated rect to show the view
	 */
	private Rect getRectForArrowUp(Rect originRect){
		
		//Get available space		
		int xAvailable = popoverLayoutRect.width();
		if (xAvailable < 0)
			xAvailable = 0;
		int yAvailable = popoverLayoutRect.height() - (originRect.bottom - popoverLayoutRect.top);
		if (yAvailable < 0)
			yAvailable = 0;
		
		//Get final width and height
		int finalX = xAvailable;
		if ((realContentSize.x > 0) && (realContentSize.x < finalX))
			finalX = realContentSize.x;
		int finalY = yAvailable;
		if ((realContentSize.y > 0) && (realContentSize.y < finalY))
			finalY = realContentSize.y;
		
		//Get final origin X and Y
		int originX = (originRect.centerX()-popoverLayoutRect.left) - (finalX/2) ;
		if (originX < 0)
			originX = 0;
		else if (originX+finalX > popoverLayoutRect.width())
			originX = popoverLayoutRect.width() - finalX;
		int originY = (originRect.bottom - popoverLayoutRect.top);
		
		//Create rect
		Rect finalRect = new Rect(originX, originY, originX+finalX, originY+finalY);
		//And return
		return finalRect;
		
	}
	
	/**
	 * Calculates the rect for showing the view with Arrow Down
	 * @param originRect The origin rect
	 * @return The calculated rect to show the view
	 */
	private Rect getRectForArrowDown(Rect originRect){
		
		//Get available space		
		int xAvailable = popoverLayoutRect.width();
		if (xAvailable < 0)
			xAvailable = 0;
		int yAvailable = (originRect.top - popoverLayoutRect.top);
		if (yAvailable < 0)
			yAvailable = 0;
		
		//Get final width and height
		int finalX = xAvailable;
		if ((realContentSize.x > 0) && (realContentSize.x < finalX))
			finalX = realContentSize.x;
		int finalY = yAvailable;
		if ((realContentSize.y > 0) && (realContentSize.y < finalY))
			finalY = realContentSize.y;
		
		//Get final origin X and Y
		int originX = (originRect.centerX()-popoverLayoutRect.left) - (finalX/2) ;
		if (originX < 0)
			originX = 0;
		else if (originX+finalX > popoverLayoutRect.width())
			originX = popoverLayoutRect.width() - finalX;
		int originY = (originRect.top - popoverLayoutRect.top) - finalY;
		
		//Create rect
		Rect finalRect = new Rect(originX, originY, originX+finalX, originY+finalY);
		//And return
		return finalRect;
		
	}
	
	
	/**
	 * Calculates the rect for showing the view with Arrow Right
	 * @param originRect The origin rect
	 * @return The calculated rect to show the view
	 */
	private Rect getRectForArrowRight(Rect originRect){
		//Get available space		
		int xAvailable = (originRect.left - popoverLayoutRect.left);
		if (xAvailable < 0)
			xAvailable = 0;
		int yAvailable = popoverLayoutRect.height();
		if (yAvailable < 0)
			yAvailable = 0;
		
		//Get final width and height
		int finalX = xAvailable;
		if ((realContentSize.x > 0) && (realContentSize.x < finalX))
			finalX = realContentSize.x;
		int finalY = yAvailable;
		if ((realContentSize.y > 0) && (realContentSize.y < finalY))
			finalY = realContentSize.y;
		
		//Get final origin X and Y
		int originX = (originRect.left - popoverLayoutRect.left) - finalX;
		int originY = (originRect.centerY()-popoverLayoutRect.top) - (finalY/2) ;
		if (originY < 0)
			originY = 0;
		else if (originY+finalY > popoverLayoutRect.height())
			originY = popoverLayoutRect.height() - finalY;
		
		//Create rect
		Rect finalRect = new Rect(originX, originY, originX+finalX, originY+finalY);
		//And return
		return finalRect;
	}
	
	/**
	 * Calculates the rect for showing the view with Arrow Left
	 * @param originRect The origin rect
	 * @return The calculated rect to show the view
	 */
	private Rect getRectForArrowLeft(Rect originRect){
		//Get available space		
		int xAvailable = popoverLayoutRect.width() - (originRect.right - popoverLayoutRect.left);
		if (xAvailable < 0)
			xAvailable = 0;
		int yAvailable = popoverLayoutRect.height();
		if (yAvailable < 0)
			yAvailable = 0;
		
		//Get final width and height
		int finalX = xAvailable;
		if ((realContentSize.x > 0) && (realContentSize.x < finalX))
			finalX = realContentSize.x;
		int finalY = yAvailable;
		if ((realContentSize.y > 0) && (realContentSize.y < finalY))
			finalY = realContentSize.y;
		
		//Get final origin X and Y
		int originX = (originRect.right - popoverLayoutRect.left);
		int originY = (originRect.centerY()-popoverLayoutRect.top) - (finalY/2) ;
		if (originY < 0)
			originY = 0;
		else if (originY+finalY > popoverLayoutRect.height())
			originY = popoverLayoutRect.height() - finalY;
		
		//Create rect
		Rect finalRect = new Rect(originX, originY, originX+finalX, originY+finalY);
		//And return
		return finalRect;
	}
	
	
	/**
	 * Add available rects for each selected arrow direction
	 * @param originRect The rect where the popover will appear from
	 * @param arrowDirections The bit mask for the possible arrow directions
	 */
	private void addAvailableRects(Rect originRect, int arrowDirections){
		//Get popover rects for the available directions
		possibleRects = new HashMap<Integer, Rect>();
		if ((arrowDirections & PopOverView.PopoverArrowDirectionUp) != 0){
			possibleRects.put(PopOverView.PopoverArrowDirectionUp, getRectForArrowUp(originRect));
		}
		if ((arrowDirections & PopOverView.PopoverArrowDirectionDown) != 0){
			possibleRects.put(PopOverView.PopoverArrowDirectionDown, getRectForArrowDown(originRect));
		}
		if ((arrowDirections & PopOverView.PopoverArrowDirectionRight) != 0){
			possibleRects.put(PopOverView.PopoverArrowDirectionRight, getRectForArrowRight(originRect));
		}
		if ((arrowDirections & PopOverView.PopoverArrowDirectionLeft) != 0){
			possibleRects.put(PopOverView.PopoverArrowDirectionLeft, getRectForArrowLeft(originRect));
		}
		
	}
	
	/**
	 * Get the best available rect (bigger area)
	 * @return The Integer key to get the Rect from posibleRects (PopoverArrowDirectionUp,PopoverArrowDirectionDown,PopoverArrowDirectionRight or PopoverArrowDirectionLeft)
	 */
	private Integer getBestRect(){
		//Get the best one (bigger area)
		Integer best = null;
		for (Integer arrowDir : possibleRects.keySet()) {
			if (best == null){
				best = arrowDir;	
			}
			else{
				Rect bestRect = possibleRects.get(best);
				Rect checkRect = possibleRects.get(arrowDir);
				if ((bestRect.width()*bestRect.height()) < (checkRect.width()*checkRect.height()))
					best = arrowDir;
			}
		}
		return best;
	}
	
	
	/**
	 * Gets the current fade animation time
	 * @return The fade animation time, in milliseconds
	 */
	public int getFadeAnimationTime() {
		return fadeAnimationTime;
	}

	/**
	 * Sets the fade animation time
	 * @param fadeAnimationTime The time in milliseconds
	 */
	public void setFadeAnimationTime(int fadeAnimationTime) {
		this.fadeAnimationTime = fadeAnimationTime;
	}
	
	/**
	 * Get the content size for view in popover
	 * @return The point with the content size
	 */
	public Point getContentSizeForViewInPopover() {
		return contentSizeForViewInPopover;
	}

	/**
	 * Sets the content size for the view in a popover, if point is (0,0) the popover will full the screen
	 * @param contentSizeForViewInPopover
	 */
	public void setContentSizeForViewInPopover(Point contentSizeForViewInPopover) {
		this.contentSizeForViewInPopover = contentSizeForViewInPopover;
		//Save the real content size
		realContentSize = new Point(contentSizeForViewInPopover);
		realContentSize.x += popoverView.getPaddingLeft()+popoverView.getPaddingRight();
		realContentSize.y += popoverView.getPaddingTop()+popoverView.getPaddingBottom();
		
	}


	public PopoverViewListener getPopoverViewListener() {
		return popoverViewListener;
	}

	public void setPopoverViewListener(PopoverViewListener popoverViewListener) {
		this.popoverViewListener = popoverViewListener;
	}

	/**
	 * This method shows a popover in a ViewGroup, from an origin rect (relative to the Application Window)
	 * @param group The group we want to insert the popup. Normally a Relative Layout so it can stand on top of everything
	 * @param originRect The rect we want the popup to appear from (relative to the Application Window!)
	 * @param arrowDirections The mask of bits to tell in which directions we want the popover to be shown
	 * @param animated Whether is animated, or not
	 */
	public void showPopoverFromRectInViewGroup(ViewGroup group, Rect originRect, int arrowDirections, boolean animated){
		
		//First, tell delegate we will show
		if (popoverViewListener != null)
			popoverViewListener.popoverViewWillShow(this);
		
		//Save superview
		superview = group;
		
		//First, add the view to the view group. The popover will cover the whole area
		android.view.ViewGroup.LayoutParams insertParams =  new  android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		group.addView(this, insertParams);
		
		//Now, save rect for the layout (is the same as the superview)
		popoverLayoutRect = PopOverView.getFrameForView(superview);
		
		//Add available rects
		addAvailableRects(originRect, arrowDirections);
		//Get best rect
		Integer best = getBestRect();
		
		//Add popover
		Rect bestRect = possibleRects.get(best);
		addPopoverInRect(bestRect);
		
		//箭头图标
		initArrow(originRect, best);
		
		
		//If we don't want animation, just tell the delegate
		if (!animated){
			//Tell delegate we did show
			if (popoverViewListener != null)
				popoverViewListener.popoverViewDidShow(this);
		}
		//If we want animation, animate it!
		else{
			//Continue only if we are not animating
			if (!isAnimating){
				
				//Create alpha animation, with its listener
				AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
				animation.setDuration(fadeAnimationTime);
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						//Nothing to do here
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						//Nothing to do here
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						//End animation
						isAnimating = false;
						//Tell delegate we did show
						if (popoverViewListener != null)
							popoverViewListener.popoverViewDidShow(PopOverView.this);
					}
				});
				
				//Start animation
				isAnimating = true;
				startAnimation(animation);
				
			}
		}
		
	}
	
	/**
	 * Dismiss the current shown popover
	 * @param animated Whether it should be dismissed animated or not
	 */
	public void dissmissPopover(boolean animated){
		
		//Tell delegate we will dismiss
		if (popoverViewListener != null)
			popoverViewListener.popoverViewWillDismiss(PopOverView.this);
		
		//If we don't want animation
		if (!animated){
			//Just remove views
			popoverView.removeAllViews();
			removeAllViews();
			superview.removeView(this);
			//Tell delegate we did dismiss
			if (popoverViewListener != null)
				popoverViewListener.popoverViewDidDismiss(PopOverView.this);
		}
		else{
			//Continue only if there is not an animation in progress
			if (!isAnimating){
				//Create alpha animation, with its listener
				AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
				animation.setDuration(fadeAnimationTime);
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						//Nothing to do here
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						//Nothing to do here
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						//Remove the view
						popoverView.removeAllViews();
						removeAllViews();
						PopOverView.this.superview.removeView(PopOverView.this);
						//End animation
						isAnimating = false;
						//Tell delegate we did dismiss
						if (popoverViewListener != null)
							popoverViewListener.popoverViewDidDismiss(PopOverView.this);
					}
				});
				
				//Start animation
				isAnimating = true;
				startAnimation(animation);
			}
			
		}
		
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//If we touched over the background popover view (this)
		if ((!isAnimating) && (v  == this)){
			dissmissPopover(true);
		}
		return true;
	}

	public Drawable getBackgroundDrawable() {
		return backgroundDrawable;
	}

	public void setBackgroundDrawable(Drawable backgroundDrawable) {
		this.backgroundDrawable = backgroundDrawable;
		popoverView.setBackgroundDrawable(backgroundDrawable);
	}

	public Drawable getArrowUpDrawable() {
		return arrowUpDrawable;
	}

	public void setArrowUpDrawable(Drawable arrowUpDrawable) {
		this.arrowUpDrawable = arrowUpDrawable;
	}

	public Drawable getArrowDownDrawable() {
		return arrowDownDrawable;
	}

	public void setArrowDownDrawable(Drawable arrowDownDrawable) {
		this.arrowDownDrawable = arrowDownDrawable;
	}

	public Drawable getArrowLeftDrawable() {
		return arrowLeftDrawable;
	}

	public void setArrowLeftDrawable(Drawable arrowLeftDrawable) {
		this.arrowLeftDrawable = arrowLeftDrawable;
	}

	public Drawable getArrowRightDrawable() {
		return arrowRightDrawable;
	}

	public void setArrowRightDrawable(Drawable arrowRightDrawable) {
		this.arrowRightDrawable = arrowRightDrawable;
	}

	public View getPopoverContentView() {
		return popoverContentView;
	}

	/**
	 * 
	 * 描述：设置显示的View
	 * @param popoverContentView
	 * @throws 
	 */
	public void setPopoverContentView(View popoverContentView) {
		this.popoverContentView = popoverContentView;
		popoverView.removeAllViews();
		popoverView.addView(popoverContentView,LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}
	
	
	
}