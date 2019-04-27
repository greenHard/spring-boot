package com.example.SpringBootMyBatis.entity;

/**
 * @author zhang yuyang
 * @ClassName: com.example.SpringBootMyBatis.entity.Description
 * @Description: 描述
 * @create 2018/04/12 16:58
 */
public class Description {
    private String province;

    private String city;

    @Override
    public String toString() {
        return "Description{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
