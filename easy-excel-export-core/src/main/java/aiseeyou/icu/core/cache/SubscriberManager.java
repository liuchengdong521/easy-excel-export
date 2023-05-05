package aiseeyou.icu.core.cache;

import aiseeyou.icu.core.common.ExcelDownloadSubscriberTemplate;
import aiseeyou.icu.core.subscriber.ExportSubscriber;

import java.util.HashSet;
import java.util.Set;

/**
 * 注册管理
 *
 * @author 柳成冬
 * @date 2023/05/04
 */
public class SubscriberManager {

    private HashSet<ExcelDownloadSubscriberTemplate> subscriberList = new HashSet<>();

    public Set<ExcelDownloadSubscriberTemplate> getSubscriberList() {
        return subscriberList;
    }

    public void setSubscriberList(Set<ExportSubscriber> subscribers) {
        for (ExportSubscriber item : subscribers) {
            subscriberList.add(new ExcelDownloadSubscriberTemplate(item));

        }
    }

    public void addSubscriber(ExcelDownloadSubscriberTemplate subscriber) {
        subscriberList.add(subscriber);
    }

    public void removeSubscriber(ExcelDownloadSubscriberTemplate subscriber) {
        subscriberList.remove(subscriber);
    }

    public void clear() {
        subscriberList.clear();
    }

    public boolean hasSubscriber() {
        return subscriberList.size() != 0;
    }
}
