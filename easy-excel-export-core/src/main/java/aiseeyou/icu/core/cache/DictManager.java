package aiseeyou.icu.core.cache;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * dict管理
 *
 * @author 柳成冬
 * @date 2023/05/04
 */
public class DictManager {

    private final String SEPARATOR = "@";

    private ConcurrentMap<String, Map<Object, Object>> dictCacheMap = new ConcurrentHashMap<>();

    public ConcurrentMap<String, Map<Object, Object>> getDictCacheMap() {
        return dictCacheMap;
    }

    public void setDictCacheMap(ConcurrentMap<String, Map<Object, Object>> dictCacheMap) {
        this.dictCacheMap = dictCacheMap;
    }

    public void addDict(String dictName, Map<Object, Object> dictMap){
        dictCacheMap.putIfAbsent(dictName, dictMap);
    }

    public void removeDict(String dictName){
        dictCacheMap.remove(dictName);
    }

    public Map<Object, Object> getDictByName(String dictName){
        return dictCacheMap.get(dictName);
    }
    public void clear(){
        dictCacheMap.clear();
    }

    /**
     * 得到dict类型名称
     *
     * @param field 场
     * @return {@link String }
     * @author 柳成冬
     */
    public String getDictName(Field field) {
        String simpleName = field.getDeclaringClass().getSimpleName();
        String fieldName = field.getName();
        return simpleName + SEPARATOR + fieldName;
    }
}
