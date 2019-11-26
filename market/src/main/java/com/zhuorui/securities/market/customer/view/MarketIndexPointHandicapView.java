package com.zhuorui.securities.market.customer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.zhuorui.commonwidget.ZRStockTextView;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.commonwidget.model.Observer;
import com.zhuorui.commonwidget.model.Subject;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.manager.StockIndexHandicapDataManager;
import com.zhuorui.securities.market.model.PushIndexHandicapData;
import com.zhuorui.securities.market.socket.vo.IndexPonitHandicapData;
import com.zhuorui.securities.market.util.MathUtil;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/26
 * Desc:
 */
public class MarketIndexPointHandicapView extends FrameLayout implements Observer {
    private int type;
    private String code;
    private String ts;
    private String name;
    private TextView tv_point_one;
    private ZRStockTextView tv_one_ponit_num;
    private TextView tv_one_point_rate;
    private ZRThreePartLineLayout zr_line;
    private ZRThreePartLineLayout zr_line_text;
    private StockIndexHandicapDataManager manager;

    public MarketIndexPointHandicapView(Context context) {
        this(context,null);
    }

    public MarketIndexPointHandicapView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MarketIndexPointHandicapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MarketIndexPointHandicapView);
        type = a.getInt(R.styleable.MarketIndexPointHandicapView_zr_index_handicap_type,0);
        code = a.getString(R.styleable.MarketIndexPointHandicapView_zr_index_handicap_code);
        ts = a.getString(R.styleable.MarketIndexPointHandicapView_zr_index_handicap_ts);
        name = a.getString(R.styleable.MarketIndexPointHandicapView_zr_index_handicap_name);
        inflate(context, R.layout.item_index_point_handicap,this);
        initView();
        manager = StockIndexHandicapDataManager.Companion.getInstance(code,ts,type);
        // 添加股价订阅
        manager.registerObserver(this);
    }

    @SuppressLint("SetTextI18n")
   public void setInitData(IndexPonitHandicapData indexPonitHandicapData) {
        tv_point_one.setText(name);
        if(Float.valueOf(MathUtil.INSTANCE.subtract2(indexPonitHandicapData.getLast(), indexPonitHandicapData.getOpen()).toString())> 0f){
           tv_one_ponit_num.setText(indexPonitHandicapData.getLast().toString(),1);
           tv_one_point_rate.setText("+"+indexPonitHandicapData.getDiffPrice().toString()+"   +"+indexPonitHandicapData.getDiffRate()+"%");
           zr_line.setType(1);
            zr_line.setValues(indexPonitHandicapData.getRise(),indexPonitHandicapData.getFlatPlate(),indexPonitHandicapData.getFall());
            zr_line_text.setType(2);
            zr_line_text.setValues(indexPonitHandicapData.getRise(),indexPonitHandicapData.getFlatPlate(),indexPonitHandicapData.getFall());
        }else if(Float.valueOf(MathUtil.INSTANCE.subtract2(indexPonitHandicapData.getLast(), indexPonitHandicapData.getOpen()).toString())< 0f){
            tv_one_ponit_num.setText(indexPonitHandicapData.getLast().toString(),-1);
            tv_one_point_rate.setText(indexPonitHandicapData.getDiffPrice().toString()+"   "+indexPonitHandicapData.getDiffRate()+"%");
            zr_line.setType(1);
            zr_line.setValues(indexPonitHandicapData.getRise(),indexPonitHandicapData.getFlatPlate(),indexPonitHandicapData.getFall());
            zr_line_text.setType(2);
            zr_line_text.setValues(indexPonitHandicapData.getRise(),indexPonitHandicapData.getFlatPlate(),indexPonitHandicapData.getFall());
        }else{
            tv_one_ponit_num.setText(indexPonitHandicapData.getLast().toString(),0);
            tv_one_point_rate.setText(indexPonitHandicapData.getDiffPrice().toString()+indexPonitHandicapData.getDiffRate()+"%");
            zr_line.setType(1);
            zr_line.setValues(indexPonitHandicapData.getRise(),indexPonitHandicapData.getFlatPlate(),indexPonitHandicapData.getFall());
            zr_line_text.setType(2);
            zr_line_text.setValues(indexPonitHandicapData.getRise(),indexPonitHandicapData.getFlatPlate(),indexPonitHandicapData.getFall());
        }
    }

    private void initView() {
        tv_point_one =(TextView) findViewById(R.id.tv_point_one);
        tv_one_ponit_num = (ZRStockTextView) findViewById(R.id.tv_one_ponit_num);
        tv_one_point_rate = (TextView) findViewById(R.id.tv_one_point_rate);
        zr_line = (ZRThreePartLineLayout) findViewById(R.id.zr_line);
        zr_line_text = (ZRThreePartLineLayout) findViewById(R.id.zr_line_text);
    }

    @Override
    public void update(Subject subject) {
        if (subject instanceof StockIndexHandicapDataManager) {
            if(((StockIndexHandicapDataManager) subject).getIndexData()!=null&&((StockIndexHandicapDataManager) subject).getPushIndexData()==null){
                setInitData(((StockIndexHandicapDataManager) subject).getIndexData());
            }else if(((StockIndexHandicapDataManager) subject).getIndexData()!=null&&((StockIndexHandicapDataManager) subject).getPushIndexData()!=null){
                updatePrice(((StockIndexHandicapDataManager) subject).getPushIndexData());
            }
        }
    }

    private void updatePrice(PushIndexHandicapData pushIndexData) {
        tv_point_one.setText(name);
        if(Float.valueOf(MathUtil.INSTANCE.subtract2(pushIndexData.getLast(), pushIndexData.getOpen()).toString())> 0f){
            tv_one_ponit_num.setText(pushIndexData.getLast().toString(),1);
            tv_one_point_rate.setText("+"+pushIndexData.getDiffPrice().toString()+"   +"+pushIndexData.getDiffRate()+"%");
            tv_one_point_rate.setTextColor(LocalSettingsConfig.Companion.getInstance().getUpColor());
            zr_line.setType(1);
            zr_line.setValues(pushIndexData.getRise(),pushIndexData.getFlatPlate(),pushIndexData.getFall());
            zr_line_text.setType(2);
            zr_line_text.setValues(pushIndexData.getRise(),pushIndexData.getFlatPlate(),pushIndexData.getFall());
        }else if(Float.valueOf(MathUtil.INSTANCE.subtract2(pushIndexData.getLast(),pushIndexData.getOpen()).toString())< 0f){
            tv_one_ponit_num.setText(pushIndexData.getLast().toString(),-1);
            tv_one_point_rate.setText(pushIndexData.getDiffPrice().toString()+"   "+pushIndexData.getDiffRate()+"%");
            tv_one_point_rate.setTextColor(LocalSettingsConfig.Companion.getInstance().getDownColor());
            zr_line.setType(1);
            zr_line.setValues(pushIndexData.getRise(),pushIndexData.getFlatPlate(),pushIndexData.getFall());
            zr_line_text.setType(2);
            zr_line_text.setValues(pushIndexData.getRise(),pushIndexData.getFlatPlate(),pushIndexData.getFall());
        }else{
            tv_one_ponit_num.setText(pushIndexData.getLast().toString(),0);
            tv_one_point_rate.setText(pushIndexData.getDiffPrice().toString()+pushIndexData.getDiffRate()+"%");
            tv_one_point_rate.setTextColor(LocalSettingsConfig.Companion.getInstance().getDefaultColor());
            zr_line.setType(1);
            zr_line.setValues(pushIndexData.getRise(),pushIndexData.getFlatPlate(),pushIndexData.getFall());
            zr_line_text.setType(2);
            zr_line_text.setValues(pushIndexData.getRise(),pushIndexData.getFlatPlate(),pushIndexData.getFall());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 删除监听
        StockIndexHandicapDataManager.Companion.getInstance( code,ts, type).removeObserver(this);
    }
}
