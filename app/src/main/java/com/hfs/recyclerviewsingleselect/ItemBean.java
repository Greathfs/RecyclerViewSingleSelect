package com.hfs.recyclerviewsingleselect;

/**
 * @author HuangFusheng
 * @date 2019-11-11
 * description 列表的Item数据
 */
public class ItemBean {
    private String name;
    private boolean isSelected;

    public ItemBean(String name) {
        this.name = name;
    }

    public ItemBean(String name, boolean isSelected) {
        this.name = name;
        setSelected(isSelected);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
