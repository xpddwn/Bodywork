package com.example.appmarket.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
/**
 * 
 * fragment tab的适配器，当fragment切换的时候，可以保存fragment的状态
 * @author tuomao
 *
 */
public class FragementTabAdapter {
	private List<Fragment> fragments; // 一个tab页面对应一个Fragment
	private FragmentActivity fragmentActivity; // Fragment所属的Activity
	private int containerId; // Activity中所要被替换的区域的id
	private int currentTab = 0; // 当前Tab页面索引
	

	public FragementTabAdapter(List<Fragment> fragments,
			FragmentActivity fragmentActivity, int containerId, int currentTab) {
		this.fragments = fragments;
		this.fragmentActivity = fragmentActivity;
		this.containerId = containerId;
		this.currentTab = currentTab;
		// 默认显示第一页
		FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
				.beginTransaction();
		ft.add(containerId, fragments.get(currentTab));
		ft.commit();
	}

	public void showTab(int position) {
		if (position != currentTab) {
			Fragment fragment = fragments.get(position);
			Fragment curFragment = getCurrentFragment();
			FragmentTransaction ft = obtainFragmentTransaction(position);
			curFragment.onPause(); // 暂停当前tab
			// getCurrentFragment().onStop(); // 暂停当前tab
			if (fragment.isAdded()) {
				// fragment.onStart(); // 启动目标tab的onStart()
				fragment.onResume(); // 启动目标tab的onResume()
			} else {
				ft.add(containerId, fragment);
			}
			ft.hide(curFragment);
			ft.show(fragment);
			ft.commit();
			currentTab = position;
		}
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public Fragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

	/**
	 * 获取一个带动画的FragmentTransaction
	 * 
	 * @param index
	 * @return
	 */
	private FragmentTransaction obtainFragmentTransaction(int index) {
		FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
				.beginTransaction();
		// 设置切换动画
		/*
		 * if(index > currentTab){ ft.setCustomAnimations(R.anim.slide_left_in,
		 * R.anim.slide_left_out); }else{
		 * ft.setCustomAnimations(R.anim.slide_right_in,
		 * R.anim.slide_right_out); }
		 */
		return ft;
	}
	public interface updateFragmentCallBack{
		public void onUpdate(int preFragment,int curFragment);
	}
}
