package com.thinkify.show_booking_system.service.Impl;

import com.thinkify.show_booking_system.model.Slot;
import com.thinkify.show_booking_system.service.SlotRanking;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlotRankingImpl implements SlotRanking {

    @Override
    public List<Slot> rankSlots(List<Slot> slots) {
        return slots.stream().sorted(Comparator.comparing(Slot::getStartTime))
                .collect(Collectors.toList());
    }
}
