package sn.sonatel.dsi.ins.imoc.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.sonatel.dsi.ins.imoc.domain.Notification;
import sn.sonatel.dsi.ins.imoc.repository.NotificationRepository;
import sn.sonatel.dsi.ins.imoc.service.NotificationQueryService;
import sn.sonatel.dsi.ins.imoc.service.NotificationService;
import sn.sonatel.dsi.ins.imoc.service.criteria.NotificationCriteria;
import sn.sonatel.dsi.ins.imoc.service.dto.NotificationDTO;
import sn.sonatel.dsi.ins.imoc.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.sonatel.dsi.ins.imoc.domain.Notification}.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private static final String ENTITY_NAME = "bookyoonnotificationserviceNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationService notificationService;

    private final NotificationRepository notificationRepository;

    private final NotificationQueryService notificationQueryService;

    public NotificationResource(
        NotificationService notificationService,
        NotificationRepository notificationRepository,
        NotificationQueryService notificationQueryService
    ) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.notificationQueryService = notificationQueryService;
    }

    /**
     * {@code POST  /notifications} : Create a new notification.
     *
     * @param notificationDTO the notificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationDTO, or with status {@code 400 (Bad Request)} if the notification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NotificationDTO> createNotification(@Valid @RequestBody NotificationDTO notificationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Notification : {}", notificationDTO);
        if (notificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new notification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        notificationDTO = notificationService.save(notificationDTO);
        return ResponseEntity.created(new URI("/api/notifications/" + notificationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, notificationDTO.getId().toString()))
            .body(notificationDTO);
    }

    /**
     * {@code PUT  /notifications/:id} : Updates an existing notification.
     *
     * @param id the id of the notificationDTO to save.
     * @param notificationDTO the notificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationDTO,
     * or with status {@code 400 (Bad Request)} if the notificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationDTO> updateNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NotificationDTO notificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Notification : {}, {}", id, notificationDTO);
        if (notificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        notificationDTO = notificationService.update(notificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationDTO.getId().toString()))
            .body(notificationDTO);
    }

    /**
     * {@code PATCH  /notifications/:id} : Partial updates given fields of an existing notification, field will ignore if it is null
     *
     * @param id the id of the notificationDTO to save.
     * @param notificationDTO the notificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationDTO,
     * or with status {@code 400 (Bad Request)} if the notificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificationDTO> partialUpdateNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificationDTO notificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Notification partially : {}, {}", id, notificationDTO);
        if (notificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificationDTO> result = notificationService.partialUpdate(notificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notifications} : get all the notifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(
        NotificationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Notifications by criteria: {}", criteria);

        Page<NotificationDTO> page = notificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notifications/count} : count all the notifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNotifications(NotificationCriteria criteria) {
        log.debug("REST request to count Notifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(notificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notifications/:id} : get the "id" notification.
     *
     * @param id the id of the notificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotification(@PathVariable("id") Long id) {
        log.debug("REST request to get Notification : {}", id);
        Optional<NotificationDTO> notificationDTO = notificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationDTO);
    }

    /**
     * {@code DELETE  /notifications/:id} : delete the "id" notification.
     *
     * @param id the id of the notificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable("id") Long id) {
        log.debug("REST request to delete Notification : {}", id);
        notificationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/welcome")
    public ResponseEntity<Void> welcomeNotification(@RequestBody NotificationDTO notificationDTO) {
        notificationService.welcomeNotification(notificationDTO);
        log.debug("Received NotificationDTO: {}", notificationDTO);
        return ResponseEntity.ok().build();
    }

    // Récupére l'historique des Notifications
    @GetMapping("/history")
    public ResponseEntity<List<NotificationDTO>> getNotificatonsHistory() {
        List<NotificationDTO> notifications = notificationService.getNotificationsHistory();
        return ResponseEntity.ok(notifications);
    }

    // Récupére l'historique des Notifications
    @GetMapping("/history/non-lue")
    public ResponseEntity<List<NotificationDTO>> getNonLueNotificationsHistory() {
        List<NotificationDTO> nonlueNotifications = notificationService.getNonLueNotificationsHistory();
        return ResponseEntity.ok(nonlueNotifications);
    }

    //Marque notification comme lue
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> marqueNotificationLue(@PathVariable Long id) {
        notificationService.marquerLue(id);
        return ResponseEntity.noContent().build();
    }

    //Marque tous les notifications comme lue
    @PatchMapping("/read-all")
    public ResponseEntity<Void> maqueToutNotificationsLue() {
        notificationService.marquerToutLue();
        return ResponseEntity.noContent().build();
    }

    // Suppression d'une notification
    //    @DeleteMapping("/{id}")
    //    public ResponseEntity<Void> deleteNotificationById(@PathVariable Long id) {
    //        notificationService.deleteNotificationById(id);
    //        return ResponseEntity.noContent().build();
    //    }

    // Suppression de toutes les notifications d'un utilisateur
    //    @DeleteMapping("/delete-all")
    //    public ResponseEntity<Void> deleteNotificationsByUserLogin(@RequestParam String userLogin) {
    //        notificationService.deleteNotificationsByUserLogin(userLogin);
    //        return ResponseEntity.noContent().build();
    //    }
    @DeleteMapping("/user/{userLogin}")
    public ResponseEntity<Void> deleteNotificationsByUser(@PathVariable String userLogin) {
        notificationService.deleteNotificationsByUser(userLogin);
        return ResponseEntity.noContent().build();
    }

    //Notifications non lue
    @GetMapping("/non-lue")
    public ResponseEntity<Long> getNotificationsCountNonLue(@RequestParam String userLogin) {
        Long count = notificationService.countNotifications(userLogin);
        return ResponseEntity.ok(count);
    }
}
