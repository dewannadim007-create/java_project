package bd.edu.seu.seulibrary.services;

import bd.edu.seu.seulibrary.model.Admin;
import bd.edu.seu.seulibrary.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin login(int adminId, String password) {
        return adminRepository.findByAdminIdAndPassword(adminId, password);
    }
}