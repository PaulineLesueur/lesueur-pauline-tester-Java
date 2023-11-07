package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket, Boolean discount){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        double duration = (outHour - inHour) / (1000 * 60 * 60);

        duration = freeParking(duration);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                if(discount) {
                    ticket.setPrice(ticket.getPrice() * Fare.DISCOUNT);
                }
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                if(discount) {
                    ticket.setPrice(ticket.getPrice() * Fare.DISCOUNT);
                }
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    public void calculateFare(Ticket ticket) {
        calculateFare(ticket, false);
    }

    private double freeParking(double duration) {
        if (duration < Fare.FREE_PARKING) {
            duration = 0;
        }
        return duration;
    }
}