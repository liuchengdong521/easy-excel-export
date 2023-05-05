package aiseeyou.icu.core.cache;

import aiseeyou.icu.core.common.ExcelDownloadRegisterTemplate;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 注册管理
 *
 * @author 柳成冬
 * @date 2023/05/04
 */
public class RegisterManager {

    private HashSet<ExcelDownloadRegisterTemplate> registerList = new HashSet<>();

    public Set<ExcelDownloadRegisterTemplate> getRegisterList() {
        return registerList;
    }

    public void setRegisterList(Set<ExcelDownloadRegisterTemplate> registerList) {
        this.registerList = (HashSet<ExcelDownloadRegisterTemplate>) registerList;
    }

    public void addRegister(ExcelDownloadRegisterTemplate  register) {
        registerList.add(register);
    }

    public void removeRegister(ExcelDownloadRegisterTemplate  register) {
        registerList.remove(register);
    }

    public void clear() {
        registerList.clear();
    }

    public boolean hasRegister() {
        return registerList.size() != 0;
    }
}
