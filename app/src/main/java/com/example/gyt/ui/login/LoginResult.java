package com.example.gyt.ui.login;

import androidx.annotation.Nullable;

import com.example.gyt.data.model.User;

class LoginResult {
    @Nullable
    private User success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable User success) {
        this.success = success;
    }

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    @Nullable
    User getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
