package me.shinsunyoung.springbootdeveloper.repository;

import me.shinsunyoung.springbootdeveloper.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * email로 사용자정보 가져옴
     * 쿼리 예) FROM users
     *         WHERE email = #{email}
     */
    Optional<User> findByEmail(String email);

}
