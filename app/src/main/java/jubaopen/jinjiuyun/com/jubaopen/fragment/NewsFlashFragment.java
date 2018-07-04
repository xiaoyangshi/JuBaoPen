package jubaopen.jinjiuyun.com.jubaopen.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jubaopen.jinjiuyun.com.jubaopen.R;
import jubaopen.jinjiuyun.com.jubaopen.adapter.FragmentAdapter;
import jubaopen.jinjiuyun.com.jubaopen.base.BaseFragment;

/**
 * 快讯
 * Created by shxioyang on 2018/7/2.
 */

public class NewsFlashFragment extends BaseFragment {
    
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    Unbinder unbinder;
    
    @Override
    public int getRootViewId() {
        return R.layout.fragment_newsflash;
    }
    
    @Override
    public void initUI() {
        List<Fragment> fragmetns = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        
        titles.add("推荐");
        titles.add("推荐1");
        titles.add("推荐2");
        titles.add("推荐3");
        titles.add("推荐4");
        fragmetns.add(new NewsFlashItemFragment());
        fragmetns.add(new NewsFlashItemFragment());
        fragmetns.add(new NewsFlashItemFragment());
        fragmetns.add(new NewsFlashItemFragment());
        fragmetns.add(new NewsFlashItemFragment());
        initViewpager(fragmetns, titles);
    }
    
    private void initViewpager(List<Fragment> fragmetns, List<String> titles) {
        FragmentPagerAdapter adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragmetns, titles);
        mViewpager.setAdapter(adapter);
        mTablayout.setupWithViewPager(mViewpager);
    }
    
    @Override
    public void initData() {
    
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
