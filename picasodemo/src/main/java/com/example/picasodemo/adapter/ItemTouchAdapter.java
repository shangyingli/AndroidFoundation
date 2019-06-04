package com.example.picasodemo.adapter;

public interface ItemTouchAdapter {

    void onItemDragged(int from, int to);

    void  onItemSwiped(int position);
}
