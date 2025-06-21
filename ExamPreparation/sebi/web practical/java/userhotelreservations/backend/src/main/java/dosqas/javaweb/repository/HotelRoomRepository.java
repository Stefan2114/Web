package dosqas.javaweb.repository;

import dosqas.javaweb.model.HotelRoom;
import dosqas.javaweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRoomRepository extends JpaRepository<HotelRoom, Integer> {
}