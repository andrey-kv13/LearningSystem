package students.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import students.dev.entity.Schedule;

public interface ScheduleRepository  extends JpaRepository<Schedule,Long> {
    
}
