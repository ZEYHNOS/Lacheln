package aba3.lucid.schedule.Business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.schedule.Service.ReservationService;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class ReservationBusiness {

    private final ReservationService reservationService;



}
