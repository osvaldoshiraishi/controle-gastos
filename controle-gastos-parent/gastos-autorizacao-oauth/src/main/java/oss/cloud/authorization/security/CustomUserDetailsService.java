package oss.cloud.authorization.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService{

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {//NOSONAR

			GrantedAuthority role = new SimpleGrantedAuthority("ROLE_ASSOCIADO");
			List<GrantedAuthority> roles = new ArrayList<>();
			roles.add(role);

			if("usuarioTeste".equals(username)){

				return new User("usuarioTeste", "abc123", roles);

			}else{
				throw new InternalAuthenticationServiceException("!!!!!!!!!!!!!!!!!!!! NOME DE USUARIO INVALIDO "+username);
			}

		}

}
