package com.jark.TMS.repo;

import com.jark.TMS.models.Comments;
import com.jark.TMS.models.Tasks;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentsRepository extends CrudRepository<Comments, Long> {
    List<Comments> findByTask(Tasks task);
}
