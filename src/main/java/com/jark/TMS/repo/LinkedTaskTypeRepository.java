package com.jark.TMS.repo;

import com.jark.TMS.models.Tasks;
import org.springframework.data.repository.CrudRepository;

public interface LinkedTaskTypeRepository extends CrudRepository<Tasks, Long> {

}
