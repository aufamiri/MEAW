package com.tekkom.meawapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailFragment extends Fragment {

    Book myBook;

    ImageView myCover;
    TextView myTitle, myDesc;

    View view;

    public BookDetailFragment() {
        // Required empty public constructor
    }

    public BookDetailFragment(Book book) {
        this.myBook = book;
    }

    public static BookDetailFragment newInstance(Book book) {
        return new BookDetailFragment(book);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        // Inflate the layout for this fragment

        myCover = view.findViewById(R.id.mybook_cover);
        myTitle = view.findViewById(R.id.mybook_title);
        myDesc = view.findViewById(R.id.mybook_desc);

        Glide.with(getContext()).load(myBook.getCoverURL()).into(myCover);
        myTitle.setText(myBook.getNamaMateri());
        myDesc.setText(myBook.getDeskripsi());

        return view;
    }

}
