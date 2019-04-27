package com.example.advance.condition.service.impl;

import com.example.advance.condition.service.ListService;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.condition.service.impl.WindowListServiceImpl
 * @Description: windows 实现类
 * @create 2018/07/10 13:21
 */
public class WindowListServiceImpl implements ListService {
    @Override
    public String showListCmd() {
        return "dir";
    }
}
