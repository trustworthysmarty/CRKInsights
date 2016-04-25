package com.relsellglobal.crk.app.pojo;

/**
 * Created by anilkukreti on 02/03/16.
 */
public class ContactUsListItem implements IListItem{
    String id;
    String description;
    String category;
    String categoryId;
    String isHeader;


    public String isHeader() {
        return isHeader;
    }

    public void setHeader(String header) {
        isHeader = header;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public String toString() {
        return "ServicesListItem{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", isHeader='" + isHeader + '\'' +
                '}';
    }
}
