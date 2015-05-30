package com.example.administrador.myapplication.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.administrador.myapplication.models.persistence.ServiceOrdersRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ServiceOrder implements Parcelable {

    private Integer mId;
    private String mClient;
    private String mPhone;
    private String mAddress;
    private Date mDate;
    private double mValue;
    private boolean mPaid;
    private String mDescription;
    private boolean mActive;

    public ServiceOrder() {
        super();
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public String getClient() {
        return mClient;
    }

    public void setClient(String client) {
        this.mClient = client;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        this.mValue = value;
    }

    public boolean isPaid() {
        return mPaid;
    }

    public void setPaid(boolean paid) {
        this.mPaid = paid;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public boolean isActive() { return mActive; }

    public void setActive(boolean mActive) { this.mActive = mActive; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceOrder that = (ServiceOrder) o;

        if (Double.compare(that.mValue, mValue) != 0) return false;
        if (mPaid != that.mPaid) return false;
        if (mActive != that.mActive) return false;
        if (!mId.equals(that.mId)) return false;
        if (!mClient.equals(that.mClient)) return false;
        if (!mPhone.equals(that.mPhone)) return false;
        if (!mAddress.equals(that.mAddress)) return false;
        if (!mDate.equals(that.mDate)) return false;
        return mDescription.equals(that.mDescription);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mId.hashCode();
        result = 31 * result + mClient.hashCode();
        result = 31 * result + mPhone.hashCode();
        result = 31 * result + mAddress.hashCode();
        result = 31 * result + mDate.hashCode();
        temp = Double.doubleToLongBits(mValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (mPaid ? 1 : 0);
        result = 31 * result + mDescription.hashCode();
        result = 31 * result + (mActive ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + mId +
                ", \"client\": \"" + mClient + '\"' +
                ", \"phone\": \"" + mPhone + '\"' +
                ", \"address\": \"" + mAddress + '\"' +
                ", \"date\":" + (mDate == null ? 0 : mDate.getTime()) +
                ", \"value\":" + mValue +
                ", \"paid\":" + mPaid +
                ", \"description\": \"" + mDescription + '\"' +
                "}";
    }

    public static List<ServiceOrder> getAll() {
        return ServiceOrdersRepository.getInstance().getAll();
    }

    public static List<ServiceOrder> filterBy(Map<String, String[]> filters) {
        return ServiceOrdersRepository.getInstance().filterBy(filters);
    }

    public void save() {
        ServiceOrdersRepository.getInstance().save(this);
    }

    public void delete() {
        ServiceOrdersRepository.getInstance().delete(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mId);
        dest.writeString(this.mClient);
        dest.writeString(this.mPhone);
        dest.writeString(this.mAddress);
        dest.writeLong(mDate != null ? mDate.getTime() : -1);
        dest.writeDouble(this.mValue);
        dest.writeByte(mPaid ? (byte) 1 : (byte) 0);
        dest.writeString(this.mDescription);
        dest.writeByte(mActive ? (byte) 1 : (byte) 0);
    }

    private ServiceOrder(Parcel in) {
        this.mId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mClient = in.readString();
        this.mPhone = in.readString();
        this.mAddress = in.readString();
        long tmpMDate = in.readLong();
        this.mDate = tmpMDate == -1 ? null : new Date(tmpMDate);
        this.mValue = in.readDouble();
        this.mPaid = in.readByte() != 0;
        this.mDescription = in.readString();
        this.mActive = in.readByte() != 0;
    }

    public static final Creator<ServiceOrder> CREATOR = new Creator<ServiceOrder>() {
        public ServiceOrder createFromParcel(Parcel source) {
            return new ServiceOrder(source);
        }

        public ServiceOrder[] newArray(int size) {
            return new ServiceOrder[size];
        }
    };
}
