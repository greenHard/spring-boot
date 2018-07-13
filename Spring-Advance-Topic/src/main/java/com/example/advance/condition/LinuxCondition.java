package com.example.advance.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.condition.LinuxCondition
 * @Description: 判断条件 Linux
 * @create 2018/07/06 14:47
 */
public class LinuxCondition implements Condition{

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().getProperty("os.name").contains("Linux");
    }
}
