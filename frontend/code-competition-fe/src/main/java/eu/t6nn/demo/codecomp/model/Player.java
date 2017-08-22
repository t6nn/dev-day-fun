package eu.t6nn.demo.codecomp.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Player {

    @NotNull
    @Size(min=2, max=100)
    private String name;

    private String email;

    @NotNull
    private Language lang;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }
}
