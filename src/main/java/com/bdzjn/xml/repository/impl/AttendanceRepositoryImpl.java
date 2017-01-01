package com.bdzjn.xml.repository.impl;

import com.bdzjn.xml.model.Attendance;
import com.bdzjn.xml.repository.AttendanceRepositoryCustom;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;


public class AttendanceRepositoryImpl extends QueryDslRepositorySupport implements AttendanceRepositoryCustom {

    public AttendanceRepositoryImpl() {
        super(Attendance.class);
    }

}
