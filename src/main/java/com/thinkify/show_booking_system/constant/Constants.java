package com.thinkify.show_booking_system.constant;

public class Constants {
        public static final String STATUS_BOOKED = "BOOKED";
        public static final String STATUS_CANCELLED = "CANCELLED";
        public static final String STATUS_WAITLISTED = "WAITLISTED";

        public static final String USER_NOT_FOUND = "User with ID not found.";
        public static final String SHOW_NOT_FOUND = "Show with ID not found";
        public static final String BOOKING_NOT_FOUND = "Booking with ID not found";
        public static final String NOT_YOUR_BOOKING = "You can only view or cancel your own booking! ";
        public static final String ALREADY_CANCELLED = "Booking is already cancelled!";
        public static final String INVALID_SLOT = "Invalid slot duration ";
        public static final String SLOT_OVERLAP = "Slot overlap with an existing slot";

        public static final String INVALID_EMAIL = "Invalid email address";
        public static final String INVALID_PASSWORD = "Invalid password";

        public static final String INVALID_USER = "Only organizers can create shows!";
}
