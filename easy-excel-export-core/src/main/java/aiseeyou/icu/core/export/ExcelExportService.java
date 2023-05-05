package aiseeyou.icu.core.export;

import aiseeyou.icu.core.annotation.ExcelChangeToForm;
import aiseeyou.icu.core.annotation.ExcelDict;
import aiseeyou.icu.core.cache.CacheStorage;
import aiseeyou.icu.core.common.ExcelDownloadRegisterTemplate;
import aiseeyou.icu.core.common.ExcelDownloadSubscriberTemplate;
import aiseeyou.icu.core.common.ExcelExportTemplate;
import aiseeyou.icu.core.common.FormItemToExcelVO;
import aiseeyou.icu.core.config.ExcelExportConfig;
import aiseeyou.icu.core.style.ExcelCellWidthStyleStrategy;
import aiseeyou.icu.core.subscriber.ExportSubscriber;
import aiseeyou.icu.core.util.SpringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * excel导出服务
 *
 * @author 柳成冬
 * @date 2023/05/04
 */
@Component
@ConditionalOnBean(CacheStorage.class)
public class ExcelExportService {


    @Resource
    private CacheStorage cacheStorage;

    @Resource
    private ExcelExportConfig excelExportConfig;

    private ThreadPoolExecutor threadPoolExecutor = null;

    public ExcelExportService() {
        if (excelExportConfig.isUseThreadPool()) {
            this.threadPoolExecutor = new ThreadPoolExecutor(
                    excelExportConfig.getThreadCount(),
                    excelExportConfig.getThreadCount(),
                    300,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue(512),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
    }

    /**
     * 导出excel
     *
     * @param response  响应
     * @param fn        文件名
     * @param condition 条件
     * @author 柳成冬
     */
    public void exportByExcel(HttpServletResponse response, String fn, Object condition) {
        // 开启计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(fn + "Excel表导出时间");
        // 处理相应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = null;
        try {
            fileName = URLEncoder.encode(fn, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        ExcelWriter excelWriter = null;
        try {
            ExcelCellWidthStyleStrategy widthStyleStrategy = new ExcelCellWidthStyleStrategy();
            excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(widthStyleStrategy).registerWriteHandler(getStyleStrategy()).build();
            // 获取列表
            List<ExcelExportTemplate> exportList = cacheStorage.getExportList();

            // 获取数据
            List<ExcelExportTemplate> resultList = new ArrayList<>();
            if (excelExportConfig.isUseThreadPool()) {
                resultList = this.getDataByThreadPool(exportList, condition);
            } else {
                resultList = this.getDataBySelf(exportList, condition);
            }
            // 处理数据
            if (resultList.size() > 0) {
                // 排序
                resultList.sort((x, y) -> y.getSheetNo().equals(x.getSheetNo()) ? y.getSheetName().compareToIgnoreCase(x.getSheetName()) : (int) (y.getSheetNo() - x.getSheetNo()) * 1000);
                // 处理注册列表
                ExcelWriter finalExcelWriter = excelWriter;
                resultList.forEach(item -> {
                    Class<?> returnType = item.getReturnType();
                    // 处理 字典
                    if (returnType.isAssignableFrom(ExcelDict.class)) {
                        List<Object> list = parseListByDict(item);
                        item.setResultData(list);
                    }
                    // 处理 form
                    if (returnType.isAssignableFrom(ExcelChangeToForm.class)) {
                        List<Object> list = parseEntryToForm(item);
                        item.setResultData(list);
                        item.setReturnType(FormItemToExcelVO.class);
                    }
                    WriteSheet writeSheet = EasyExcel.writerSheet(item.getSheetName()).head(item.getReturnType()).build();
                    finalExcelWriter.write(item.getResultData(), writeSheet);
                });
            }
        } catch (IOException | ExecutionException | InterruptedException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (excelWriter != null) {
                stopWatch.stop();
                excelWriter.finish();
            }
        }
    }


    /**
     * 获取数据自我
     *
     * @param exportList 出口清单
     * @return {@link List }<{@link ExcelExportTemplate }>
     * @author 柳成冬
     */
    private List<ExcelExportTemplate> getDataBySelf(List<ExcelExportTemplate> exportList, Object condition) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        List<ExcelExportTemplate> resultList = new ArrayList<>();
        for (ExcelExportTemplate excelExportTemplate : exportList) {
            ExcelExportTemplate result = getData(excelExportTemplate, condition);
            resultList.add(result);
        }
        return resultList;
    }

    /**
     * 获取数据
     *
     * @param exportList 出口清单
     * @return {@link List }<{@link ExcelExportTemplate }>
     * @author 柳成冬
     */
    private List<ExcelExportTemplate> getDataByThreadPool(List<ExcelExportTemplate> exportList, Object condition) throws ExecutionException, InterruptedException {
        Future[] listFuture = new Future[exportList.size()];
        List<ExcelExportTemplate> resultList = new ArrayList<>();
        if (threadPoolExecutor == null) {
            throw new RuntimeException("线程池为空");
        }
        for (int i = 0; i < exportList.size(); i++) {
            ExcelExportTemplate item = exportList.get(i);
            Callable callable = new Callable() {
                @Override
                public Object call() throws InterruptedException, InstantiationException, IllegalAccessException, InvocationTargetException {
                    return getData(item, condition);
                }
            };
            Future submit = threadPoolExecutor.submit(callable);
            listFuture[i] = submit;
        }
        for (Future future : listFuture) {
            resultList.add((ExcelExportTemplate) future.get());
        }
        return resultList;
    }

    /**
     * 获取数据
     *
     * @param item      项
     * @param condition 条件
     * @return {@link ExcelExportTemplate }
     * @author 柳成冬
     */
    private ExcelExportTemplate getData(ExcelExportTemplate item, Object condition) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (item instanceof ExcelDownloadSubscriberTemplate) {
            // 处理订阅者返回结果
            ExcelDownloadSubscriberTemplate subscriber = (ExcelDownloadSubscriberTemplate) item;
            ExportSubscriber exportSubscriber = subscriber.getExportSubscriber();
            ExcelDownloadSubscriberTemplate result = exportSubscriber.writeExcelSheet(subscriber, condition);
            if (result.getReturnType() == null || result.getResultData() == null) {
                // 结果校验
                return null;
            } else {
                return result;
            }
        } else if (item instanceof ExcelDownloadRegisterTemplate) {
            // 处理注册者返回结果 不建议使用此方法
            ExcelDownloadRegisterTemplate register = (ExcelDownloadRegisterTemplate) item;
            // 按方法名 查询方法
            Class<?> serviceClazz = register.getServiceClazz();
            Method searchMethod = null;
            Method[] declaredMethods = serviceClazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.getName().equals(register.getMethodName())) {
                    searchMethod = method;
                    break;
                }
            }
            if (searchMethod == null) {
                throw new RuntimeException("注册方法名错误");
            }
            // 获取入参 并实例化 注入属性
            // 要求方法 首个参数为 条件参数
            Class<?> parameterTypes = searchMethod.getParameterTypes()[0];
            Object instance = parameterTypes.newInstance();
            BeanUtils.copyProperties(instance, condition);
            // 按全类名获取 service
            Object service = SpringUtils.getBean(register.getServiceName());
            // 执行查询
            Object result = searchMethod.invoke(service, instance);
            // 获取结果参数类型
            Class<?> returnType = searchMethod.getReturnType();
            Type genericReturnType = searchMethod.getGenericReturnType();
            if (genericReturnType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                register.setReturnType((Class<?>) parameterizedType.getActualTypeArguments()[0]);
                register.setResultData((List<Object>) result);
            } else {
                List<Object> resultList = new ArrayList<>();
                resultList.add(result);
                register.setReturnType(returnType);
                register.setResultData(resultList);
            }

            return register;
        } else {
            return null;
        }
    }


    /**
     * 得到dict类型名称
     *
     * @param field 场
     * @return {@link String }
     * @author 柳成冬
     */
    private String getDictName(Field field) {
        String simpleName = field.getDeclaringClass().getSimpleName();
        String fieldName = field.getName();
        return simpleName + excelExportConfig.getSEPARATOR() + fieldName;
    }

    /**
     * 由dict解析列表
     *
     * @param item 项
     * @return {@link List }<{@link Object }>
     * @author 柳成冬
     */
    private List<Object> parseListByDict(ExcelExportTemplate item) {
        List<Object> resultList = item.getResultData();
        Class<?> resultType = item.getReturnType();
        Field[] declaredFields = resultType.getDeclaredFields();
        List<Field> fieldCollect = Arrays.stream(declaredFields).filter(one -> one.getAnnotation(ExcelDict.class) != null).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(fieldCollect)) {
            return resultList;
        }
        fieldCollect.forEach(field -> {
            ExcelDict excelDict = field.getAnnotation(ExcelDict.class);
            String dictName = excelDict.dictName();
            String SEPARATOR = excelExportConfig.getSEPARATOR();
            if (SEPARATOR.equals(dictName)) {
                dictName = getDictName(field);
            }
            Map<Object, Object> dictMap = cacheStorage.getDict(dictName);
            if (dictMap == null || dictMap.isEmpty()) {
                // 反射获取字典
                String[] value = excelDict.value();
                Map<Object, Object> newDictMap = new HashMap<>(8);
                Arrays.stream(value).forEach(two -> {
                    String[] dict = two.split(SEPARATOR);
                    newDictMap.put(dict[0], dict[1]);
                });
                // 缓存
                cacheStorage.cacheDict(dictName, newDictMap);
            }

            Map<Object, Object> dictMapResult = cacheStorage.getDict(dictName);
            if (dictMapResult == null || dictMapResult.isEmpty()) {
                // 啥也不干
            } else {
                // 进行字典翻译
                resultList.forEach(one -> {
                    try {
                        field.setAccessible(true);
                        Object key = field.get(one);
                        Object value = dictMapResult.get(key);
                        if (value != null) {
                            field.set(one, dictMapResult.get(key));
                        } else {
                            field.set(one, key);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
        return resultList;
    }

    /**
     * 解析条目形成
     *
     * @param item 项
     * @return {@link List }<{@link Object }>
     * @author 柳成冬
     */
    private List<Object> parseEntryToForm(ExcelExportTemplate item) {
        Object result = item.getResultData().get(0);
        Class<?> resultType = item.getReturnType();
        List<Object> formList = new ArrayList<>();
        Field[] declaredFields = resultType.getDeclaredFields();
        Arrays.stream(declaredFields).forEach(field -> {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(ExcelProperty.class)) {
                    String key = field.getAnnotation(ExcelProperty.class).value()[0];
                    Object value = "";
                    if (result != null) {
                        value = field.get(result);
                    }

                    FormItemToExcelVO formItemToExcelVO = new FormItemToExcelVO(key, value.toString());
                    formList.add(formItemToExcelVO);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return formList;
    }

    /**
     * 得到风格策略
     * 内容居中
     *
     * @return {@link HorizontalCellStyleStrategy }
     * @author 柳成冬
     */
    public HorizontalCellStyleStrategy getStyleStrategy() {
        // 创建一个写出的单元格样式对象
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setWrapped(true);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        return horizontalCellStyleStrategy;
    }
}
