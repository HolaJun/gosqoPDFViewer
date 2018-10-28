package viewer_pro.viewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

/**
 * Created by gosqo on 2017-07-25.
 */

public class _PDF_View_Fragment extends Fragment {

    ImageButton pdfPreBtn, pdfNxtBtn, pdfPgBtn, pdfPntBtn, pdfErsBtn;
    PDFView pdfViewFrag;
    TextView pdfPageCount;
    Paint_Painter pdfPainter;
    SeekBar pdfSeekBar;
    String pdfNameForKey;
    File pdf;

    PDF_View_PageChangeListener pdfOnPageChangeListener;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        pdf = new File(getArguments().getString("pdfPath"));
        pdfNameForKey = pdf.getName();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

        SharedPreferences pdfInfo = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        //초기화--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        pdfViewFrag = rootView.findViewById(R.id.fragview_pdfview);
        pdfPageCount = rootView.findViewById(R.id.fragview_tv_pagecount);
        pdfPreBtn = rootView.findViewById(R.id.fragview_btn_prev);
        pdfNxtBtn = rootView.findViewById(R.id.fragview_btn_next);
        pdfPgBtn = rootView.findViewById(R.id.fragview_btn_pagingmode);
        pdfPntBtn = rootView.findViewById(R.id.fragview_btn_paintgmode);
        pdfErsBtn = rootView.findViewById(R.id.fragview_btn_eraser);
        pdfSeekBar = rootView.findViewById(R.id.fragview_seekbar);
        pdfPainter = rootView.findViewById(R.id.frg_paint_selection);

        pdfOnPageChangeListener = new PDF_View_PageChangeListener();


        //기본 설정----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        pdfSeekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        pdfOnPageChangeListener.getViews(pdfPageCount, pdfSeekBar);

        pdfViewFrag.fromFile(pdf).defaultPage(pdfInfo.getInt(pdfNameForKey, 0))
                .enableSwipe(_Stat_Storage.isPagingEnabled)
                .swipeHorizontal(true)
                .pageSnap(true)
                .pageFling(true)
                .autoSpacing(true)
                .enableAnnotationRendering(true)
                .onPageChange(pdfOnPageChangeListener)
                .load();

        pdfViewFrag.bringToFront();

        if (!_Stat_Storage.isPagingEnabled) {
            pdfPainter.bringToFront();
            pdfPgBtn.setImageResource(R.drawable.stop);
        }

        if (!_Stat_Storage.isPaintEnabled) {
            pdfViewFrag.bringToFront();
            pdfPntBtn.setImageResource(R.drawable.stop);
        }


        //이벤트처리 ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        pdfSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            //SsekBar Progress 값을 View Pager의 페이지 값으로 치환시킴
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.v("Seekbar_stat", "ProgressChanged");
                pdfViewFrag.jumpTo(progress);
            }//onProgressChanged

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.v("Seekbar_stat", "ProgressStartTouch");
            }//onStartTouch

            //SeekBar Progress 값의 최소치를 1로 고정
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.v("Seekbar_stat", "ProgressStopTouch");
            }//onStopTouch

        });
        pdfErsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfPainter.clearPaint();
            }
        });
        pdfPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfViewFrag.getCurrentPage() > 0) {
                    pdfViewFrag.jumpTo(pdfViewFrag.getCurrentPage() - 1);
                }
            }
        });
        pdfNxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfViewFrag.getCurrentPage() < pdfViewFrag.getPageCount())
                    pdfViewFrag.jumpTo(pdfViewFrag.getCurrentPage() + 1);
            }
        });
        pdfPgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfViewFrag.isSwipeEnabled()) {
                    _Stat_Storage.isPagingEnabled = false;
                    pdfViewFrag.setSwipeEnabled(_Stat_Storage.isPagingEnabled);
                    pdfPgBtn.setImageResource(R.drawable.stop);
                } else {
                    _Stat_Storage.isPagingEnabled = true;
                    _Stat_Storage.isPaintEnabled = false;
                    pdfViewFrag.setSwipeEnabled(_Stat_Storage.isPagingEnabled);
                    pdfPgBtn.setImageResource(0);
                    pdfPntBtn.setImageResource(R.drawable.stop);
                }
            }
        });
        pdfPntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_Stat_Storage.isPaintEnabled) { //그리는 중
                    _Stat_Storage.isPagingEnabled = true;
                    _Stat_Storage.isPaintEnabled = false;
                    pdfPgBtn.setImageResource(0);
                    pdfPntBtn.setImageResource(R.drawable.stop);
                    pdfViewFrag.setSwipeEnabled(_Stat_Storage.isPagingEnabled);
                    pdfViewFrag.bringToFront();
                } else { //그리지 않는 중
                    _Stat_Storage.isPagingEnabled = false;
                    _Stat_Storage.isPaintEnabled = true;
                    pdfPntBtn.setImageResource(0);
                    pdfViewFrag.setSwipeEnabled(_Stat_Storage.isPagingEnabled);
                    pdfPgBtn.setImageResource(R.drawable.stop);
                    pdfPainter.bringToFront();
                }
            }
        });
        return rootView;
    }//onCreate






    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences pdfInfo = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor pdfInfoEdit = pdfInfo.edit();
        pdfInfoEdit.putInt(pdfNameForKey, pdfViewFrag.getCurrentPage());
        pdfInfoEdit.apply();
        pdfInfoEdit.commit();
    }//onPause






    public static _PDF_View_Fragment newInstance(String path) {
        _PDF_View_Fragment fragment = new _PDF_View_Fragment();
        Bundle args = new Bundle();
        args.putString("pdfPath", path);
        fragment.setArguments(args);
        return fragment;
    }

}//class