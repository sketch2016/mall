package com.example.sketchxu.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.activity.AddressAddActivity;
import com.example.sketchxu.mall.bean.Address;

import java.util.List;

public class AddressAdapter extends BaseAdapter<Address> {

    private  AddressLisneter lisneter;

    public AddressAdapter(Context context, List<Address> datas, AddressLisneter lisneter) {
        super(context, R.layout.template_address,datas);

        this.lisneter = lisneter;

    }


    @Override
    public void bindData(BaseViewHolder viewHoder, final Address item) {

        viewHoder.getTextView(R.id.txt_name).setText(item.getConsignee());
        viewHoder.getTextView(R.id.txt_phone).setText(replacePhoneNum(item.getPhone()));
        viewHoder.getTextView(R.id.txt_address).setText(item.getAddr());
        viewHoder.getTextView(R.id.txt_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lisneter != null) {
                    lisneter.onEdit(item);
                }
            }
        });
        viewHoder.getTextView(R.id.txt_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lisneter != null) {
                    lisneter.onDel(item);
                }
            }
        });

        final CheckBox checkBox = viewHoder.getCheckBox(R.id.cb_is_defualt);

        final boolean isDefault = item.getIsDefault();
        checkBox.setChecked(isDefault);


        if(isDefault){
            checkBox.setText("默认地址");
            checkBox.setChecked(true);
            checkBox.setClickable(false);
        }
        else{
            checkBox.setChecked(false);
            checkBox.setClickable(true);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked && lisneter !=null){

                        item.setIsDefault(true);
                        lisneter.setDefault(item);
                    }
                }
            });


        }


    }


    public String replacePhoneNum(String phone){

        return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
    }


   public interface AddressLisneter{


        public void setDefault(Address address);

        public void onEdit(Address address);

        public void onDel(Address address);

    }



}
