package model;

import com.google.gson.annotations.Expose;

public class TokenModel {
    @Expose
    private String token;

    public TokenModel() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
