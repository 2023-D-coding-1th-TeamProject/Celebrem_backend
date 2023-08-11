package Dcoding.Celebrem.domain;

public enum Authority {
    ROLE_USER("user"), ROLE_INFLUENCER("influencer"), ROLE_ADMIN("admin");

    private final String role;
    Authority(String role){
        this.role = role;
    }
}
