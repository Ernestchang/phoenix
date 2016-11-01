package com.yulin.applib.page.i;

/**
 * Page动画接口
 */
public interface PageAnimationI {

	/**
	 * 新页面入栈时，新页面进入动画
	 *
	 * @return 动画的资源id，为0表示无动画
	 * */
	int enterAnimation();

	/**
	 * 新页面入栈时，原页面退出动画
	 *
	 * @return 动画的资源id，为0表示无动画
	 * */
	int exitAnimation();

	/**
	 * 当前页面出栈时，栈中当前页面下方页面复出时的动画
	 *
	 * @return 动画的资源id，为0表示无动画
	 * * */
	int popEnterAnimation();

	/**
	 * 当前页面出栈时，当前页面的弹出动画
	 *
	 * @return 动画的资源id，为0表示无动画
	 * */
	int popExitAnimation();

}
