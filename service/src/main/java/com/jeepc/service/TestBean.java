package com.jeepc.service;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jeepc on 2018/1/3.
 */

@Entity
public class TestBean implements Parcelable {

    @Id(autoincrement = true)
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected TestBean(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
    }

    @Generated(hash = 1979658847)
    public TestBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 2087637710)
    public TestBean() {
    }

    
    public static final Creator<TestBean> CREATOR = new Creator<TestBean>() {
        @Override
        public TestBean createFromParcel(Parcel in) {
            return new TestBean(in);
        }

        @Override
        public TestBean[] newArray(int size) {
            return new TestBean[size];
        }
    };



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
    }
}
