package com.soho.spring.aspect;

import com.alibaba.fastjson.JSON;
import com.soho.spring.utils.MD5Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class DefaultAspect {

    protected String getReqID(ProceedingJoinPoint joinPoint, String prefix) {
        // 获取请求参数唯一标识,hash(类名+方法名+参数)
        String className = joinPoint.getThis().toString().split("@")[0];
        String methodName = joinPoint.getSignature().getName();
        String classAndMethod = className + "." + methodName;
        StringBuffer buffer = new StringBuffer()
                .append(classAndMethod).append("; ")
                .append(joinPoint.getArgs() == null ? "" : JSON.toJSONString(joinPoint.getArgs())).append("; ");
        String reqId = (prefix == null ? "" : prefix) + MD5Utils.encrypt(buffer.toString(), null);
        return reqId;
    }

    protected Method getMethod(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = null;
        if (!(signature instanceof MethodSignature)) {
            return null;
        }
        methodSignature = (MethodSignature) signature;
        Object target = joinPoint.getTarget();
        try {
            return target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String getSpelValue(String key, ProceedingJoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        if (method == null) {
            return "";
        }
        Object[] args = joinPoint.getArgs();
        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer discoverer =
                new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = discoverer.getParameterNames(method);
        //使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        //SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        String value = parser.parseExpression(key).getValue(context, String.class);
        return value == null ? "" : value;
    }

}