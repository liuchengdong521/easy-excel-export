package aiseeyou.icu.core.style;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel单元格宽度风格策略
 * 抄来的
 * @author 柳成冬
 * @date 2023/04/11
 */
public class ExcelCellWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {
    private static final int MAX_COLUMN_WIDTH = 50;
    private static final int MIN_COLUMN_WIDTH = 10;
    private  Map<Integer, Map<Integer, Integer>> CACHE = new HashMap(8);


    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        boolean needSetWidth = isHead || !CollectionUtils.isEmpty(cellDataList);
        if (needSetWidth) {
            Map<Integer, Integer> maxColumnWidthMap = (Map)CACHE.get(writeSheetHolder.getSheetNo());
            if (maxColumnWidthMap == null) {
                maxColumnWidthMap = new HashMap(16);
                CACHE.put(writeSheetHolder.getSheetNo(), maxColumnWidthMap);
            }

            Integer columnWidth = this.dataLength(cellDataList, cell, isHead);
            if (columnWidth >= 0) {
                if (columnWidth > MAX_COLUMN_WIDTH) {
                    columnWidth = MAX_COLUMN_WIDTH;
                }

                Integer maxColumnWidth = (Integer)((Map)maxColumnWidthMap).get(cell.getColumnIndex());
                if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
                    if(columnWidth < MIN_COLUMN_WIDTH){
                        columnWidth = MIN_COLUMN_WIDTH;
                    }
                    ((Map)maxColumnWidthMap).put(cell.getColumnIndex(), columnWidth);
                    writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 350);
                }

            }
        }
    }

    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (isHead) {
            return cell.getStringCellValue().getBytes().length;
        } else {
            CellData cellData = (CellData)cellDataList.get(0);
            CellDataTypeEnum type = cellData.getType();
            if (type == null) {
                return -1;
            } else {
                switch(type) {
                    case STRING:
                        return cellData.getStringValue().getBytes().length;
                    case BOOLEAN:
                        return cellData.getBooleanValue().toString().getBytes().length;
                    case NUMBER:
                        return cellData.getNumberValue().toString().getBytes().length;
                    default:
                        return -1;
                }
            }
        }
    }

}