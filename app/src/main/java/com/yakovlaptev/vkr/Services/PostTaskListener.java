package com.yakovlaptev.vkr.Services;

public interface PostTaskListener<K> {
    void onPostTask(K result);
}
