package com.example.sketchxu.mall.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

//import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.MyApplication;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.bean.Address;
import com.example.sketchxu.mall.city.XmlParserHandler;
import com.example.sketchxu.mall.city.model.CityModel;
import com.example.sketchxu.mall.city.model.DistrictModel;
import com.example.sketchxu.mall.city.model.ProvinceModel;
import com.example.sketchxu.mall.http.OkHttpHelper;
import com.example.sketchxu.mall.http.SpotsCallback;
import com.example.sketchxu.mall.msg.BaseMessage;
import com.example.sketchxu.mall.widget.ClearEditText;
import com.example.sketchxu.mall.widget.CnToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Response;

import static com.example.sketchxu.mall.activity.AddressListActivity.ACTION_CREATE;
import static com.example.sketchxu.mall.activity.AddressListActivity.ACTION_EDIT;


public class AddressAddActivity extends BaseActivity {


    private OptionsPickerView mCityPikerView; //https://github.com/saiwu-bigkoo/Android-PickerView


    @ViewInject(R.id.txt_address)
    private TextView mTxtAddress;

    @ViewInject(R.id.edittxt_consignee)
    private ClearEditText mEditConsignee;

    @ViewInject(R.id.edittxt_phone)
    private ClearEditText mEditPhone;

    @ViewInject(R.id.edittxt_add)
    private ClearEditText mEditAddr;

    @ViewInject(R.id.toolbar)
    private CnToolbar mToolBar;


    private List<ProvinceModel> mProvinces;
    private ArrayList<ArrayList<String>> mCities = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<ArrayList<ArrayList<String>>>();


    private OkHttpHelper mHttpHelper=OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
        ViewUtils.inject(this);


        initToolbar();
        init();
    }

    private void initToolbar(){

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                createAddress();
            }
        });

    }

    private int action;
    private Address address;
    private int size;
    private void init() {

        initProvinceDatas();

        mCityPikerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View view) {

                String addresss = mProvinces.get(options1).getName() +"  "
                        + mCities.get(options1).get(option2)+"  "
                        + mDistricts.get(options1).get(option2).get(options3);
                mTxtAddress.setText(addresss);

            }
        }).setDividerColor(Color.BLACK)
              .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
              .setContentTextSize(16)
              .build();

        mCityPikerView.setPicker((ArrayList) mProvinces,mCities,mDistricts);
        mCityPikerView.setTitleText("选择城市");
        //mCityPikerView.setCyclic(false,false,false);
        //mCityPikerView.setOnoptionsSelectListener();

        Intent intent = getIntent();
        size = intent.getIntExtra("size", 0);
        action = intent.getIntExtra("action", ACTION_CREATE);
        if (action == ACTION_EDIT) {
            Serializable serializable = intent.getSerializableExtra("address");
            if (serializable != null && serializable instanceof Address) {
                address = (Address) serializable;
                mTxtAddress.setText(address.getAddr());
                mEditConsignee.setText(address.getConsignee());
                mEditPhone.setText(address.getPhone());
                mEditAddr.setText(address.getAddr());
            }
        }





    }



    protected void initProvinceDatas()
    {

        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            mProvinces = handler.getDataList();

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }

        if(mProvinces !=null){

            for (ProvinceModel p :mProvinces){

               List<CityModel> cities =  p.getCityList();
               ArrayList<String> cityStrs = new ArrayList<>(cities.size()); //城市List


               for (CityModel c :cities){

                   cityStrs.add(c.getName()); // 把城市名称放入 cityStrs


                   ArrayList<ArrayList<String>> dts = new ArrayList<>(); // 地区 List

                   List<DistrictModel> districts = c.getDistrictList();
                   ArrayList<String> districtStrs = new ArrayList<>(districts.size());

                   for (DistrictModel d : districts){
                       districtStrs.add(d.getName()); // 把城市名称放入 districtStrs
                   }
                   dts.add(districtStrs);


                  mDistricts.add(dts);
               }

               mCities.add(cityStrs); // 组装城市数据

            }
        }



    }



    @OnClick(R.id.ll_city_picker)
    public void showCityPickerView(View view){
        mCityPikerView.show();
    }


    public void createAddress(){


        String consignee = mEditConsignee.getText().toString();
        String phone = mEditPhone.getText().toString();
        String addr = mTxtAddress.getText().toString() + mEditAddr.getText().toString();


        Map<String,String> params = new HashMap<>(1);

        params.put("user_id", MyApplication.getInstance().getUser().getId() + "");
        params.put("consignee",consignee);
        params.put("phone",phone);
        params.put("addr",addr);
        params.put("zip_code","000000");


        String url = Contants.API.ADDRESS_CREATE;
        if (action == ACTION_EDIT && address != null) {
            url = Contants.API.ADDRESS_UPDATE;
            params.put("id", address.getId() + "");
            params.put("is_default",address.getIsDefault() ? "true" : "false");
        } else {
            params.put("is_default",size == 0 ? "true" : "false");
        }

        mHttpHelper.post(url, params, new SpotsCallback<BaseMessage>(this) {


            @Override
            public void onSuccess(Response response, BaseMessage baseRespMsg) {
                if(baseRespMsg.getStatus() == BaseMessage.STATUS_SUCCESS){
                    setResult(RESULT_OK);
                    finish();

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }





}
