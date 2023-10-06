package com.gfinance.application.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
// User object used to process Entity users retrieved from a database for settings view display
@Validated
public class WebSettingsUser {
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
