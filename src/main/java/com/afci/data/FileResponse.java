package com.afci.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "file_responses")
public class FileResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;

    @NotNull
    @Column(name = "file_download_uri")
    private String fileDownloadUri;

    @NotNull
    @Size(max = 100)
    @Column(name = "file_type")
    private String fileType;

    @NotNull
    @Column(name = "size")
    private long size;

    @Column(name = "upload_status")
    private String uploadStatus;

    @NotNull
    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_date")
    private Date uploadDate;

    @Size(max = 500)
    @Column(name = "description")
    private String description;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "download_count")
    private int downloadCount;

    @Size(max = 255)
    @Column(name = "checksum")
    private String checksum;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "thumbnail_path")
    private String thumbnailPath;

    // Constructeur utilisé pour la réponse API basique
    public FileResponse(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.uploadDate = new Date();
        this.uploadStatus = "SUCCESS";
    }

    // Méthodes utilitaires
    public String getFormattedSize() {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
        }
    }

    public void incrementDownloadCount() {
        this.downloadCount++;
    }

    public boolean isImage() {
        return fileType != null && fileType.startsWith("image/");
    }

    public boolean isPDF() {
        return fileType != null && fileType.equals("application/pdf");
    }

    public String getFileExtension() {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }

    // Méthode pour vérifier si le fichier est expiré (si nécessaire)
    public boolean isExpired() {
        if (uploadDate == null) {
            return false;
        }
        // Par exemple, expire après 30 jours
        long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;
        return System.currentTimeMillis() - uploadDate.getTime() > thirtyDaysInMillis;
    }
}