package sn.sonatel.dsi.ins.imoc.repository;

import feign.Param;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.ins.imoc.domain.Notification;

/**
 * Spring Data JPA repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
    List<Notification> findAllByUserLoginIgnoreCase(String login);
    List<Notification> findAllByUserLoginIgnoreCaseAndDeletedIsFalseAndReadIsFalse(String userLogin);
    List<Notification> findByUserLoginAndReadIsFalse(String userLogin);
    List<Notification> findByUserLoginAndDeletedFalse(String userLogin);
    Optional<Notification> findByIdAndDeletedFalse(Long id);
    Long countByUserLoginAndReadFalseAndDeletedFalse(String userLogin);
}
