package jubaopen.jinjiuyun.com.jubaopen.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import butterknife.BindView;
import butterknife.Unbinder;
import jubaopen.jinjiuyun.com.jubaopen.R;
import jubaopen.jinjiuyun.com.jubaopen.adapter.NewsFlashItemAdapter;
import jubaopen.jinjiuyun.com.jubaopen.base.BaseFragment;

/**
 *
 */
public class NewsFlashItemFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rcv)
    RecyclerView mRcv;
    Unbinder unbinder;
    @BindView(R.id.springview)
    SpringView mSpringview;
    Unbinder unbinder1;
    
    private String mParam1;
    private String mParam2;
    
    
    public NewsFlashItemFragment() {
        // Required empty public constructor
    }
    
    public static NewsFlashItemFragment newInstance(String param1, String param2) {
        NewsFlashItemFragment fragment = new NewsFlashItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    
    @Override
    public int getRootViewId() {
        return R.layout.fragment_news_flash_item;
    }
    
    @Override
    public void initUI() {
        mSpringview.setHeader(new DefaultHeader(mContext));//设置默认的
        mSpringview.setFooter(new DefaultFooter(mContext));
        
        mRcv.setLayoutManager(new LinearLayoutManager(mContext));
        NewsFlashItemAdapter adapter = new NewsFlashItemAdapter(mContext);
        mRcv.setAdapter(adapter);
        
        mSpringview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mSpringview.onFinishFreshAndLoadDelay();
            }
            @Override
            public void onLoadmore() {
                mSpringview.onFinishFreshAndLoadDelay();
            }
        });
        
       mSpringview.callFreshDelay();//手动刷新
    }
    
    @Override
    public void initData() {
    
    }
}
