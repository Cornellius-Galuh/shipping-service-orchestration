package com.shipping.notification_service.controller;

import com.shipping.notification_service.model.Notification;
import com.shipping.notification_service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    // CREATE (Kirim notifikasi)
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        Notification savedNotification = notificationRepository.save(notification);
        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
    }

    // READ ALL (Opsional untuk admin)
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return new ResponseEntity<>(notificationRepository.findAll(), HttpStatus.OK);
    }

    // READ BY RECIPIENT ID (Daftar notifikasi untuk customer)
    @GetMapping("/customer/{recipientId}")
    public ResponseEntity<List<Notification>> getNotificationsByRecipient(@PathVariable Integer recipientId) {
        List<Notification> notifications = notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable String id, @RequestBody Notification notification) {
        Optional<Notification> notificationData = notificationRepository.findById(id);

        if (notificationData.isPresent()) {
            Notification _notification = notificationData.get();
            _notification.setRecipientId(notification.getRecipientId());
            _notification.setType(notification.getType());
            _notification.setTitle(notification.getTitle());
            _notification.setMessage(notification.getMessage());
            
            return new ResponseEntity<>(notificationRepository.save(_notification), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteNotification(@PathVariable String id) {
        Optional<Notification> notificationData = notificationRepository.findById(id);

        if (notificationData.isPresent()) {
            notificationRepository.delete(notificationData.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
