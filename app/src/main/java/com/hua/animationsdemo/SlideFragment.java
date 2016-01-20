package com.hua.animationsdemo;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SlideFragment extends Fragment {

    private static final String PAGE_ARG = "page";
    private int imgRes;

    public SlideFragment(){

    }

    public static SlideFragment create(int srcId){
        SlideFragment frag = new SlideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_ARG, srcId);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgRes = getArguments().getInt(PAGE_ARG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.fragment_slide_img);
        img.setImageResource(imgRes);
        return view;
    }
}
