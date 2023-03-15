package com.zhangll.core.config;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.zhangll.core.config.model.PerformantModel;
import com.zhangll.core.config.user.RequestThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author : Liangliang.Zhang4
 * @version : 1.0
 * @date : 2023/3/7
 */
@Aspect
@Slf4j
@Component
public class PerformanceAspect {
    ThreadLocal<Map<Integer, List<PerformantModel>>> metaDataMap = new ThreadLocal<>();
    ThreadLocal<Stack<Integer>> historyStack = new ThreadLocal<>();
    ThreadLocal<Integer> stepCount = new ThreadLocal<>();
    ThreadLocal<Integer> idIndex = new ThreadLocal<>();

    /**
     * Pointcut注解声明切点
     * 配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
     * <p>
     * within 对类起作用，@annotation 对方法起作用
     */
    @Pointcut("@annotation(com.zhangll.core.config.model.MethodPerformance)")
    public void performancePointCut() {
    }

    /**
     * 配置前置通知,使用在方法aspect()上注册的切入点
     * 同时接受JoinPoint切入点对象,可以没有该参数
     *
     * @param proceedingJoinPoint 拦截点
     * @throws ClassNotFoundException 无法找到类
     */
    @Around("performancePointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 加1
        addOne();
        indexAdd();
        Object result = null;
        // 开始时间
        long startTime = System.currentTimeMillis();
        Integer id = idIndex.get();
        addStack(id);

        try {
            result = proceedingJoinPoint.proceed();
            // 计算执行时间
            long endTime = System.currentTimeMillis();
            buildCostTime(proceedingJoinPoint, startTime, endTime, "", id);
        } catch (Throwable e) {
            long endTime = System.currentTimeMillis();
            buildCostTime(proceedingJoinPoint, startTime, endTime, e.getMessage(), id);
            throw e;
        } finally {
            reduceOne();
            popOneOnStack();
            clearMetaData();
        }
        return handleObject(result);
    }

    private void popOneOnStack() {
        Stack<Integer> stack = historyStack.get();
        if (stack == null || stack.isEmpty()) {
            return;
        }
        stack.pop();
    }

    private void addStack(Integer id) {
        Stack<Integer> modelStack = Optional.ofNullable(historyStack.get()).orElse(new Stack<>());
        modelStack.push(id);
        historyStack.set(modelStack);
    }

    private void indexAdd() {
        Integer step = idIndex.get();
        if (step == null) {
            idIndex.set(1);
        } else {
            idIndex.set(step + 1);
        }
    }

    private void buildCostTime(ProceedingJoinPoint proceedingJoinPoint,
                               long startTime,
                               long endTime,
                               String errorMessage,
                               Integer id) {
        // 获取类的字节码对象，通过字节码对象获取方法信息
        Class<?> targetCls = proceedingJoinPoint.getTarget().getClass();
        // 获取方法签名(通过此签名获取目标方法信息)
        MethodSignature ms = (MethodSignature) proceedingJoinPoint.getSignature();

        Object[] args = proceedingJoinPoint.getArgs();
        String method = targetCls.getName() + "." + ms.getName() + "()";
        PerformantModel model = new PerformantModel();
        model.setId(id);
        model.setError(errorMessage);
        model.setCostTime(endTime - startTime);
        model.setMethodName(method);
        model.setStartTime(startTime);
        model.setParameters(args);
        // update stack
        Stack<Integer> modelStack = Optional.ofNullable(historyStack.get()).orElse(new Stack<>());
        if (modelStack.isEmpty()) {
            model.setParentId(0);
        } else {
            Integer pop = modelStack.pop();
            if(modelStack.isEmpty()) {
                model.setParentId(0);
            } else {
                model.setParentId(modelStack.peek());
            }
            modelStack.push(pop);
        }


        // update metaDataMap
        Integer step = Optional.ofNullable(stepCount.get()).orElse(1);
        Map<Integer, List<PerformantModel>> metaMap = Optional.ofNullable(metaDataMap.get()).orElse(new HashMap<>());
        List<PerformantModel> performantModels = metaMap.get(step);
        if (CollectionUtils.isEmpty(performantModels)) {
            performantModels = Lists.newArrayList(model);
        } else {
            performantModels.add(model);
        }
        metaMap.put(step, performantModels);
        metaDataMap.set(metaMap);

    }

    private void clearMetaData() {
        Integer step = stepCount.get();
        if (step == null || step == 0) {
            log.info("cost info reqId:[{}], data: [{}]", RequestThreadLocal.safeGet().getRequestId(), JSONUtil.toJsonStr(metaDataMap.get()));
            metaDataMap.remove();
            stepCount.remove();
            historyStack.remove();
            idIndex.remove();
        }
    }

    private void reduceOne() {
        Integer step = stepCount.get();
        if (step == null) {
            stepCount.set(0);
        } else {
            stepCount.set(step - 1);
        }
    }

    private void addOne() {
        Integer step = stepCount.get();
        if (step == null) {
            stepCount.set(1);
        } else {
            stepCount.set(step + 1);
        }
    }

    private Object handleObject(Object result) {
        return result;
    }

}
