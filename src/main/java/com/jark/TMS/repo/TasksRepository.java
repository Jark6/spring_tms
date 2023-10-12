package com.jark.TMS.repo;

import com.jark.TMS.models.Tasks;
import org.springframework.data.repository.CrudRepository;

public interface TasksRepository extends CrudRepository<Tasks, Long> {

}
