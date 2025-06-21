export interface HotelRoom {
  id: number;
  roomNumber: string;
  capacity: number;
  basePrice: number;
  checkInDate?: string;
  checkOutDate?: string;
}


export interface ReserveDTO{
  hotelRoomId: number;
  start: string;
  end: string;
}

export interface Reservation {
  id: number;
  username: string;
  hotelRoomId: number,
  start: string;
  end: string;
  hotelRoom?: HotelRoom;
}

export interface LoginRequest {
  username: string;
  password: string;
}
