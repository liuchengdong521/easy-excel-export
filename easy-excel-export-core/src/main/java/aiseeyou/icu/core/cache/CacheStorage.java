package aiseeyou.icu.core.cache;

import aiseeyou.icu.core.common.ExcelDownloadRegisterTemplate;
import aiseeyou.icu.core.common.ExcelDownloadSubscriberTemplate;
import aiseeyou.icu.core.common.ExcelExportTemplate;
import aiseeyou.icu.core.subscriber.ExportSubscriber;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 缓存存储
 *
 * @author 柳成冬
 * @date 2023/05/04
 */
public class CacheStorage {

    private RegisterManager registerManager = new RegisterManager();

    private SubscriberManager subscriberManager = new SubscriberManager();

    private DictManager dictManager = new DictManager();

    public CacheStorage(HashSet<ExportSubscriber> subscriberList) {
        subscriberManager.setSubscriberList(subscriberList);
    }

    /**
     * 获得导出列表
     *
     * @return {@link List }<{@link ExcelExportTemplate }>
     * @author 柳成冬
     */
    public List<ExcelExportTemplate> getExportList() {
        List<ExcelExportTemplate> exportList = new ArrayList<>(16);
        if (registerManager.hasRegister()) {
            List<ExcelDownloadRegisterTemplate> registerList = new ArrayList<>(registerManager.getRegisterList());
            exportList.addAll(registerList);
        }
        if (subscriberManager.hasSubscriber()) {
            List<ExcelDownloadSubscriberTemplate> subscriberList = new ArrayList<>(subscriberManager.getSubscriberList());
            exportList.addAll(subscriberList);
        }
        return exportList;
    }

    /**
     * 得到东西
     *
     * @param dictName dict类型名称
     * @return {@link Map }<{@link Object }, {@link Object }>
     * @author 柳成冬
     */
    public Map<Object, Object> getDict(String dictName) {
        return dictManager.getDictByName(dictName);
    }

    public Map<Object, Object> getDictMap(String dictName) {
        return dictManager.getDictByName(dictName);
    }


    public void cacheDict(String dictName, Map<Object, Object> newDictMap) {
        dictManager.addDict(dictName, newDictMap);
    }
}
