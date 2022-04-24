package vkb.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vkb.entity.UserAccount;

import java.util.List;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String>{


    Page<UserAccount> findAllByFirstNameLike(String lastName, Pageable pageable);

    List<UserAccount>  findAllByEmail(String email);

    List<UserAccount>  findAllByPhone(String phone);

}