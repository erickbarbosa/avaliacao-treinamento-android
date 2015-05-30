package com.example.administrador.myapplication.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.administrador.myapplication.models.persistence.UserRepository;

/**
 * Created by erick.barbosa on 28/05/2015.
 */
public class User implements Parcelable {

    private Long id;
    private String login;
    private String password;
    private String name;
    private Boolean stored;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isStored() { return stored; }

    public void setStored(Boolean stored) {
        this.stored = stored;
    }

    public Boolean isAuthenticated() {
        return UserRepository.getInstance().authenticate(this.getLogin(), this.getPassword());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!login.equals(user.login)) return false;
        if (!password.equals(user.password)) return false;
        if (!name.equals(user.name)) return false;
        return stored.equals(user.stored);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + stored.hashCode();
        return result;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.login);
        dest.writeString(this.password);
        dest.writeString(this.name);
        dest.writeValue(this.stored);
    }

    private User(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.login = in.readString();
        this.password = in.readString();
        this.name = in.readString();
        this.stored = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
