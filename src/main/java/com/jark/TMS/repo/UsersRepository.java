package com.jark.TMS.repo;

import com.jark.TMS.models.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersRepository extends CrudRepository<Users, Long> {
    Users findByLogin(String login);

    @Query("SELECT u FROM Users u WHERE u.user_id IN :ids")
    List<Users> findAllById(@Param("ids") List<Long> ids);
}
