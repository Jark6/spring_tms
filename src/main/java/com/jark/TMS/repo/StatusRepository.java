package com.jark.TMS.repo;

import com.jark.TMS.models.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatusRepository extends CrudRepository<Status, Long> {
}
