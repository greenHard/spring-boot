package com.example.advance.concurrent.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.concurrent.service.AsyncTaskService
 * @Description: 异步任务执行类
 * @create 2018/07/04 16:53
 */
@Service
public class AsyncTaskService {

    @Async
    public void executeAsyncTask(Integer i) {
        System.out.println("执行异步任务: " + i);
    }

    public void executeAsyncTaskPlus(Integer i) {
        System.out.println("执行异步任务+1： " + (i + 1));
    }
}
