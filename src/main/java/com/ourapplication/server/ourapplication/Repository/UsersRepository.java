package com.ourapplication.server.ourapplication.Repository;

import com.ourapplication.server.ourapplication.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByActiveKey(String activeKey);

}
