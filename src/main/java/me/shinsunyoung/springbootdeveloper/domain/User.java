package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails { //UserDetails를 상속받아 인증객체로 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Builder
    public User(String email, String password, String auth){
        this.email = email;
        this.password = password;
    }

    @Override //권한반환
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("user"));
    }

    //사용자의 id를 반환(고유한 값)
    @Override
    public String getUsername(){
        return email;
    }

    //사용자의 패스워드 반환
    @Override
    public String getPassword(){
        return password;
    }

    //계정만료 여부 반환
    @Override
    public boolean isAccountNonExpired(){
        return true; //true-> 만료되지 않았음
    }

    //계정잠금 여부 반환
    @Override
    public boolean isAccountNonLocked(){
        return true; //잠금되지 않았음
    }

    //패스워드 만료여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true; //만료되지 않았음
    }

    //계정사용 여부
    @Override
    public boolean isEnabled() {
        return true; //사용가능
    }






}
