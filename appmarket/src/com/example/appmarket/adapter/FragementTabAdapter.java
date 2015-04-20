package com.example.appmarket.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * 
 * fragment tab������������fragment�л���ʱ�򣬿��Ա���fragment��״̬
 * 
 * @author tuomao
 * 
 */
public class FragementTabAdapter {
	private List<Fragment> fragments; // һ��tabҳ���Ӧһ��Fragment
	private FragmentActivity fragmentActivity; // Fragment������Activity
	private int containerId; // Activity����Ҫ���滻�������id
	private int currentTab = 0; // ��ǰTabҳ������

	public FragementTabAdapter(List<Fragment> fragments,
			FragmentActivity fragmentActivity, int containerId, int currentTab) {
		this.fragments = fragments;
		this.fragmentActivity = fragmentActivity;
		this.containerId = containerId;
		this.currentTab = currentTab;
		// Ĭ����ʾ��һҳ
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
			curFragment.onPause(); // ��ͣ��ǰtab
			// getCurrentFragment().onStop(); // ��ͣ��ǰtab
			if (fragment.isAdded()) {
				// fragment.onStart(); // ����Ŀ��tab��onStart()
				fragment.onResume(); // ����Ŀ��tab��onResume()
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
	 * ��ȡһ������FragmentTransaction
	 * 
	 * @param index
	 * @return
	 */
	private FragmentTransaction obtainFragmentTransaction(int index) {
		FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
				.beginTransaction();
		// �����л�����
		/*
		 * if(index > currentTab){ ft.setCustomAnimations(R.anim.slide_left_in,
		 * R.anim.slide_left_out); }else{
		 * ft.setCustomAnimations(R.anim.slide_right_in,
		 * R.anim.slide_right_out); }
		 */
		return ft;
	}

	public interface updateFragmentCallBack {
		public void onUpdate(int preFragment, int curFragment);
	}
}
