package com.thinkify.show_booking_system.repository;

import com.thinkify.show_booking_system.model.Slot;
import com.thinkify.show_booking_system.model.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitListRepository extends JpaRepository<Waitlist,Long> {
    List<Waitlist> findBySlot(Slot slot);

}
