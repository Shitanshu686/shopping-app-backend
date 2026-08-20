package com.shitanshu.shopping.dto;

import jakarta.validation.constraints.NotNull;

public class DarkModeRequestDTO {

    @NotNull(message = "Dark mode preference is required")
    private Boolean darkMode;

    public DarkModeRequestDTO() {

    }

    public Boolean getDarkMode() {
        return darkMode;
    }

    public void setDarkMode(Boolean darkMode) {
        this.darkMode = darkMode;
    }
}