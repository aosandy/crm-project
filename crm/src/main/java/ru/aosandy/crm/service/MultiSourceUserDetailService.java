package ru.aosandy.crm.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Данный класс нужен чтобы объединить UserDetailsService клиента и менеджера
 */

@Service
@AllArgsConstructor
public class MultiSourceUserDetailService implements UserDetailsService {

    private ClientService clientService;
    private ManagerService managerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = null;
        try {
            userDetails = clientService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ignored) {}
        try {
            userDetails = managerService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ignored) {}
        if (userDetails == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        return userDetails;
    }
}
