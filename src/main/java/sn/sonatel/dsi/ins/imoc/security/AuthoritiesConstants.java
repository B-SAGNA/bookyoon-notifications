package sn.sonatel.dsi.ins.imoc.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String PASSAGER = "ROLE_PASSAGER";
    public static final String CONDUCTEUR = "ROLE_CONDUCTEUR";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
