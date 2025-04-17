package com.thinkify.show_booking_system.service;

import com.thinkify.show_booking_system.model.Slot;

import java.util.List;

public interface SlotRanking {
    List<Slot> rankSlots(List<Slot> slots);
}
