package org.safa.maintenanceservice.models.model;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.safa.maintenanceservice.models.dto.user.MaintenanceUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * This class here helps manage login and register.
 * It is crucial in a moments of dealing with Auth**/

public class MaintenanceUserDetails implements UserDetails {

    private final MaintenanceUser user;

    public MaintenanceUserDetails(MaintenanceUser user) {
        this.user = user;
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //turning every role into simple grand authority
        return user.roles().stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public @Nullable String getPassword() {
        return user.password();
    }

    @Override
    @NonNull
    public String getUsername() {
        return user.userName();
    }
}
