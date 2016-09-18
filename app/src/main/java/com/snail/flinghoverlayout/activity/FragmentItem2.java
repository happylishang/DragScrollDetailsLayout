package com.snail.flinghoverlayout.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.snail.flinghoverlayout.R;

/**
 * Created by netease on 16-7-25.
 */
public class FragmentItem2 extends Fragment {

    private ListView list;
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView ==null){
            rootView = inflater.inflate(R.layout.item_list,container,false);
            list = (ListView) rootView.findViewById(R.id.list);
            list.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return 60;
                }

                @Override
                public Object getItem(int i) {

                    return null;
                }

                @Override
                public long getItemId(int i) {
                    return i;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {
                    Button button=new Button(getActivity());
                    button.setText(""+i);
                    return button;
                }
            });
        }else {
            ViewGroup v=((ViewGroup)(rootView.getParent()));
            if(v!=null){
                v.removeView(rootView);
            }
        }
        return rootView ;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
