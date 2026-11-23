package ro.utcn.scd.wardrobeai.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // cautare dupa email (query creat de Spring din numele metodei)
    Optional<User> findByEmail(String email);
}