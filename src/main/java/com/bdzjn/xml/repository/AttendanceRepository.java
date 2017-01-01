package com.bdzjn.xml.repository;


import com.bdzjn.xml.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, AttendanceRepositoryCustom {

    Optional<Attendance> findById(long id);

}
