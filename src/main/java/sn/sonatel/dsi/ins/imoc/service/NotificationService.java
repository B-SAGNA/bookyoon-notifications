package sn.sonatel.dsi.ins.imoc.service;

import static sn.sonatel.dsi.ins.imoc.security.SecurityUtils.getCurrentUserLogin;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.imoc.domain.Notification;
import sn.sonatel.dsi.ins.imoc.repository.NotificationRepository;
import sn.sonatel.dsi.ins.imoc.security.SecurityUtils;
import sn.sonatel.dsi.ins.imoc.service.dto.NotificationDTO;
import sn.sonatel.dsi.ins.imoc.service.mapper.NotificationMapper;

/**
 * Service Implementation for managing {@link sn.sonatel.dsi.ins.imoc.domain.Notification}.
 */
@Service
@Transactional
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Update a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO update(NotificationDTO notificationDTO) {
        log.debug("Request to update Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Partially update a notification.
     *
     * @param notificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificationDTO> partialUpdate(NotificationDTO notificationDTO) {
        log.debug("Request to partially update Notification : {}", notificationDTO);

        return notificationRepository
            .findById(notificationDTO.getId())
            .map(existingNotification -> {
                notificationMapper.partialUpdate(existingNotification, notificationDTO);

                return existingNotification;
            })
            .map(notificationRepository::save)
            .map(notificationMapper::toDto);
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id).map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }

    // RETOURNE L'ENSEMBLE DES NOTIFICATIONS DE L'UTILISATEUR CONNECTE
    public List<NotificationDTO> getNotificationsHistory() {
        return notificationMapper.toDto(notificationRepository.findAllByUserLoginIgnoreCase(getCurrentUserLogin().orElseThrow()));
    }

    // RETOURNE L'ENSEMBLE DES NOTIFICATIONS NON LUE DE L'UTILISATEUR CONNECTE
    public List<NotificationDTO> getNonLueNotificationsHistory() {
        return notificationMapper.toDto(
            notificationRepository.findAllByUserLoginIgnoreCaseAndDeletedIsFalseAndReadIsFalse(getCurrentUserLogin().orElseThrow())
        );
    }

    // MARQUE NOTIFICATIONS COMME LUE
    public void marquerLue(Long id) {
        String currentUserLogin = String.valueOf(getCurrentUserLogin());
        Notification notification = notificationRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notification not found with id " + id));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void marquerToutLue() {
        // Assurez-vous que getCurrentUserLogin() renvoie une valeur correcte
        String currentUserLogin = getCurrentUserLogin().orElse(null);

        // Si le login est null ou vide, loggez et arrêtez la méthode
        if (currentUserLogin == null || currentUserLogin.isEmpty()) {
            log.error("Aucun utilisateur connecté ou login non valide");
            return;
        }

        // Vérifiez que le login est bien récupéré et utilisez-le pour récupérer les notifications
        log.info("Tentative de marquer comme lues les notifications pour l'utilisateur: {}", currentUserLogin);

        // Récupérer les notifications non lues pour cet utilisateur
        List<Notification> notifications = notificationRepository.findByUserLoginAndReadIsFalse(currentUserLogin);

        if (notifications.isEmpty()) {
            log.info("Aucune notification non lue pour l'utilisateur: {}", currentUserLogin);
        } else {
            notifications.forEach(notification -> notification.setRead(true));
            notificationRepository.saveAll(notifications);
            log.info("{} notifications marquées comme lues pour l'utilisateur: {}", notifications.size(), currentUserLogin);
        }
    }

    // Suppression d'une notification
    public void deleteNotificationById(Long id) {
        notificationRepository
            .findById(id)
            .ifPresent(notification -> {
                notification.setDeleted(true);
                notificationRepository.save(notification);
            });
    }

    // Suppression de toutes les notifications d'un utilisate@Transactional
    public void deleteNotificationsByUser(String userLogin) {
        List<Notification> notifications = notificationRepository.findByUserLoginAndDeletedFalse(userLogin);
        for (Notification notification : notifications) {
            notification.setDeleted(true); // Marquer comme supprimé
            notificationRepository.save(notification);
        }
    }

    public void welcomeNotification(NotificationDTO notificationDTO) {
        // Création de l'entité Notification à partir du DTO
        Notification notification = new Notification();
        notification.setMessage(notificationDTO.getMessage());
        notification.setUserLogin(notificationDTO.getUserLogin());
        notification.setReservationId(notificationDTO.getReservationId());
        notification.setDeleted(notificationDTO.getDeleted());
        notification.setRead(notificationDTO.getRead());

        // Sauvegarde de la notification en base de données
        notificationRepository.save(notification);

        // Log pour vérifier la notification envoyée
        System.out.println("Notification de bienvenue : " + notificationDTO);
    }

    // Méthode pour compter les notifications non lues
    public Long countNotifications(String userLogin) {
        return notificationRepository.countByUserLoginAndReadFalseAndDeletedFalse(userLogin);
    }
}
