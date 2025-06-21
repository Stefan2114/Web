export interface Flight{
  id:number,
  date: string,
  destinationCity: string,
  availableSeats: number
}


export interface Reservation{
  person: string,
  type: string
idReservedResource: number

}


export interface Hotel{
  id:number,
  name: string,
  date: string,
  city: string,
  availableRooms: number
}
