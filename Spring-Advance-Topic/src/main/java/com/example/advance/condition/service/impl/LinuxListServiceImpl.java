package com.example.advance.condition.service.impl;

import com.example.advance.condition.service.ListService;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.condition.service.impl.LinuxListServiceImpl
 * @Description: Linux实现类
 * @create 2018/07/10 13:23
 */
public class LinuxListServiceImpl implements ListService {
    @Override
    public String showListCmd() {
        return "ls";
    }
}
