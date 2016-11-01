package com.yulin.applib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class DragdropGridView extends GridView {  
    
    private int dragPosition;//开始拖拽的位置  
    private int dropPosition;// 结束拖拽的位置  
    private int dragPointX; //相对于item的x坐标  
    private int dragPointY; //相对于item的y坐标  
    private int dragOffsetX;  
    private int dragOffsetY;  
    private ImageView dragImageView; //拖动item的preview  
      
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;  
  
	public DragdropGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    public DragdropGridView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
      
    public DragdropGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {  
        
   	 
   	 //点击事件 触发
        if(ev.getAction() == MotionEvent.ACTION_DOWN){  
            
       	 
       	 //获取 点击的位置
            int x = (int)ev.getX();  
            int y = (int)ev.getY();  
            
            //根据点击位置 获取listview中的某个 position
            dragPosition = dropPosition = pointToPosition(x, y);  
            
            //判断这个view 在 adapter中是否是invalid_position 不是的话返回
            if(dragPosition == AdapterView.INVALID_POSITION){  
                return super.onInterceptTouchEvent(ev);  
            } 
            //获得 listview 中 position 为点击的view
            ViewGroup itemView = (ViewGroup)getChildAt(dragPosition - getFirstVisiblePosition());  
            //得到当前点在item内部的偏移量 即相对于item左上角的坐标  
            dragPointX = x - itemView.getLeft();  
            dragPointY = y - itemView.getTop();  
            //根据相对item的位置获得 相对屏幕的位置 x y
            dragOffsetX = (int)(ev.getRawX() - x);  
            dragOffsetY = (int)(ev.getRawY() - y);  
            //每次都销毁一次cache，重新生成一个bitmap  
            itemView.destroyDrawingCache();  
            itemView.setDrawingCacheEnabled(true);  
            Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());  
            //建立item的缩略图  
            startDrag(bm,x,y);  
              
            return false;  
        }  
          
        return super.onInterceptTouchEvent(ev);  
    }  
  
    
    /**
     * 开始拖拽
     * **/
    private void startDrag(Bitmap bm, int x, int y) {  
        stopDrag();  
        
        windowParams = new WindowManager.LayoutParams();  
        windowParams.gravity = Gravity.TOP|Gravity.LEFT;//这个必须加  
        //得到preview左上角相对于屏幕的坐标  
        windowParams.x = x - dragPointX + dragOffsetX;  
        windowParams.y = y - dragPointY + dragOffsetY;  
        //设置宽和高  
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  
                                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON  
                                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;  
        //设置屏幕透明
        windowParams.format = PixelFormat.TRANSLUCENT;  
        windowParams.windowAnimations = 0;  
          
        ImageView iv = new ImageView(getContext());  
        iv.setImageBitmap(bm);  
        
        //添加item缩略图到windowmanager中
        windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);//"window"  
        windowManager.addView(iv, windowParams);  
        dragImageView = iv;  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent ev) {  
        if(dragImageView!=null && dragPosition!=AdapterView.INVALID_POSITION){  
            int x = (int)ev.getX();  
            int y = (int)ev.getY();  
            switch(ev.getAction()){  
                case MotionEvent.ACTION_MOVE:  
                    onDrag(x,y);  
                    break;  
                case MotionEvent.ACTION_UP:  
                    stopDrag();  
                    onDrop(x,y);  
                    break;  
            }  
        }  
        return super.onTouchEvent(ev);  
    }  
  
  
  
    private void onDrag(int x, int y) {  
   	 
   	 //更新缩略图的移动位置
	    if(dragImageView!=null){  
	        windowParams.alpha = 0.6f;  
	        windowParams.x = x - dragPointX + dragOffsetX;  
	        windowParams.y = y - dragPointY + dragOffsetY;  
	        windowManager.updateViewLayout(dragImageView, windowParams);  
	        //缩略图所在的位置
	        int currPos = pointToPosition(x, y);
//	        if(lastUnderPosition != -1 && lastUnderPosition != currPos)
//	        {
//	        	ViewGroup itemView = (ViewGroup)getChildAt(lastUnderPosition - getFirstVisiblePosition()); 
//	        	startZoomOut(itemView);
//	        	
//	        	lastUnderPosition = -1;
//	        }
//	        if(currPos != dragPosition)
//	        {
//	        	if(currPos != lastUnderPosition)
//	        	{
//	        		ViewGroup itemView = (ViewGroup)getChildAt(currPos - getFirstVisiblePosition()); 
//	        		startZoomIn(itemView);
//	        		
//	        		lastUnderPosition = currPos;
//	        	}
//	        }
	    }  
    }  
  
    private void startZoomOut(final ViewGroup view)
    {
    	if(view == null)
    	{
    		return;
    	}
    	ScaleAnimation anim = new ScaleAnimation(0.8f, 1, 0.8f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    	anim.setDuration(300);
    	anim.setRepeatCount(0);
    	anim.setFillAfter(true);
    	anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				view.clearAnimation();
			}
		});
    	view.startAnimation(anim);
    }
    private void startZoomIn(final ViewGroup view)
    {
    	if(view == null)
    	{
    		return;
    	}
    	ScaleAnimation anim = new ScaleAnimation(1, 0.8f, 1, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    	anim.setDuration(300);
    	anim.setRepeatCount(0);
    	anim.setFillAfter(true);
    	anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				view.clearAnimation();
			}
		});
    	view.startAnimation(anim);
    }
    
    private void onDrop(int x,int y) {  
        int tempPosition = pointToPosition(x, y);  
        if(tempPosition != AdapterView.INVALID_POSITION){  
            dropPosition = tempPosition;  
        }  
        if(dropPosition != dragPosition){  
       	 	if(mDragdropListener != null)
       	 	{
       	 		mDragdropListener.onDragdrop(dragPosition, dropPosition);
       	 	}
        }  
    }  
    
    //在结束拖拽以后 remove window
    private void stopDrag() {  
        if(dragImageView!=null){  
            windowManager.removeView(dragImageView);  
            dragImageView = null;  
        }  
    }  
    
    private OnDragdropListener mDragdropListener = null;
    //抽象方法 用于 完成 拖拽以后要执行的 工作
    public static interface OnDragdropListener
    {
    	public void onDragdrop(int dragPosition,int dropPosition);
    }
  
    public void setOnDragdropListener(OnDragdropListener listener)
    {
    	mDragdropListener = listener;
    }
} 