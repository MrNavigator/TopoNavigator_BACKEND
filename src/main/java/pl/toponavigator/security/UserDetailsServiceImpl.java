package pl.toponavigator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.toponavigator.model.User;
import pl.toponavigator.repository.UsersRepository;
import pl.toponavigator.utils.RoleEnum;

import java.util.HashSet;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = usersRepository.findOneByEmailOrUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        final HashSet<RoleEnum> roleEnums = new HashSet<>();
        for (final RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.getPermissionLevel() == user.getPermissionLevel()) {
                roleEnums.add(roleEnum);
                break;
            }
        }
        if (roleEnums.isEmpty()) {
            roleEnums.add(RoleEnum.USER);
        }
        user.setRoles(roleEnums);

        return new UserDetailsImpl(user);
    }

    @Autowired
    public void setUsersRepository(final UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
