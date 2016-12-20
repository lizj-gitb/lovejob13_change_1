package com.lovejob.ms;

import com.lovejob.R;
import com.lovejob.view._home.F_Home;
import com.lovejob.view._job.F_Job;
import com.lovejob.view._money.F_Money;
import com.lovejob.view._userinfo.F_UserInfo;

public enum MainTab {


	tab1(0,R.string.home, R.drawable.tab_icon_home,
			F_Home.class),

	tab2(1, R.string.job_hunt, R.drawable.tab_icon_job,
			F_Job.class),

	tab3(2, R.string.money, R.drawable.tab_icon_money, F_Money.class),

	tab4(3,R.string.me, R.drawable.tab_icon_user,
			F_UserInfo.class);

	// todo 	ME(4, R.string.main_tab_name_my, R.drawable.tab_icon_home,
	//	   F_UserInfo.class);


	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
		this.idx = idx;
		this.resName = resName;
		this.resIcon = resIcon;
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getResIcon() {
		return resIcon;
	}

	public void setResIcon(int resIcon) {
		this.resIcon = resIcon;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}
}
