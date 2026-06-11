package org.safa.maintenanceservice.service;

import org.jspecify.annotations.NullMarked;
import org.safa.maintenanceservice.models.dto.user.MaintenanceUser;
import org.safa.maintenanceservice.models.model.MaintenanceUserDetails;
import org.safa.maintenanceservice.models.model.UserRole;
import org.safa.maintenanceservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    /**
     * Matches username via the database of maintenance which we created
     @return UserDetails
     **/

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(item->new MaintenanceUserDetails(new MaintenanceUser(item.getUsername(), UserRole.valueOf(item.getRole()), item.getPassword())))
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
    }
}