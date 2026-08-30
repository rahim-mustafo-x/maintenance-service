package org.safa.maintenanceservice.user.service;

import org.jspecify.annotations.NullMarked;
import org.safa.maintenanceservice.user.model.dto.MaintenanceUser;
import org.safa.maintenanceservice.user.model.MaintenanceUserDetails;
import org.safa.maintenanceservice.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(item->new MaintenanceUserDetails(new MaintenanceUser(item.getUsername(), item.getRoles().stream().map(roles->roles.getRole().name()).collect(Collectors.toSet()), item.getPassword())))
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
    }
}