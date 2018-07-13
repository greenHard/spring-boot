package com.example.advance.aware;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.aware.AwareService
 * @Description: spring aware服务类, 实现bean
 * {@link BeanNameAware}
 * {@link ResourceLoaderAware}
 * @create 2018/07/04 14:18
 */
@Component
public class AwareService implements BeanNameAware,ResourceLoaderAware {

    private String beanName;

    private ResourceLoader loader;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.loader = resourceLoader;
    }

    public void outPutResult(){
        // 输出类的名称
        System.out.println("Bean 的名称为: "+beanName);

        // 加载文件
        Resource resource = loader.getResource("classpath:test.txt");

        // 输出文件内容
        try {
            System.out.println(IOUtils.toString(resource.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
