package bd.edu.seu.seulibrary.repository;

import bd.edu.seu.seulibrary.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByAdminIdAndPassword(int adminId, String password);

}
