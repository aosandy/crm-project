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
    public UserDetails loadUserByUsername(String number) throws UsernameNotFoundException {
        UserDetails userDetails = null;
        try {
            userDetails = clientService.loadUserByUsername(number);
        } catch (UsernameNotFoundException ignored) {
        }
        try {
            userDetails = managerService.loadUserByUsername(number);
        } catch (UsernameNotFoundException ignored) {
        }
        return userDetails;
    }
}
