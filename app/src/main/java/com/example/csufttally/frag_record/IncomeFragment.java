package com.example.csufttally.frag_record;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.csufttally.R;
import com.example.csufttally.db.DBManager;
import com.example.csufttally.db.TypeBean;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGv() {
        super.loadDataToGv();
        //获取数据源
        List<TypeBean> inList = DBManager.getTypeList(1);
        typeList.addAll(inList);
        adapter.notifyDataSetChanged();
        typeTv.setText("其它");
        typeIv.setImageResource(R.mipmap.ic_in_else_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertItemToAccounttb(accountBean);
    }
}
