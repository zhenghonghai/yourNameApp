package com.example.gyt.ui.login;

import androidx.annotation.Nullable;

class LoginFormState {
    @Nullable
    private Integer userNameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    LoginFormState(@Nullable Integer userNameError, @Nullable Integer passwordError) {
        this.userNameError = userNameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.userNameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUserNameError() {
        return userNameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
