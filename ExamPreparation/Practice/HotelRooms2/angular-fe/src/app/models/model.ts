export interface HotelRoom {
  id: number;
  roomNr: string;
  date: string;
  nrRooms: number;
}

export interface Reservation {
    person: string;
    type: string;
    typeId: number;
}


export interface Flight {
  id: number;
  flightNr: string;
  date: string;
  nrSeats: number;
}



