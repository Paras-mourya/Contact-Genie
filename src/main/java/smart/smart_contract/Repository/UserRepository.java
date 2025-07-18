package smart.smart_contract.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import smart.smart_contract.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    // ✅ Spring JPA खुद ही query बना लेगा (by method name)
    User findByEmail(String email);

   }
