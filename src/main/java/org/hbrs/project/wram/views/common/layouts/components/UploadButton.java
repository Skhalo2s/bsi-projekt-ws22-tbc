package org.hbrs.project.wram.views.common.layouts.components;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.rowset.serial.SerialBlob;

import org.hbrs.project.wram.control.entwickler.EntwicklerService;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
/**
 * @apiNote this class is responsible for uploading images
 * @author @tkeppe2s (Tom Keppeler)
 * @vision 1.0
 */
public class UploadButton extends Upload {
    @Autowired
    private EntwicklerService entwicklerService;
    private final UUID userId;
    private MemoryBuffer memoryBuffer = new MemoryBuffer();
    private int maxFileSizeInBytes = 10 * 1024 * 1024; // 10MB;
    private String description = "Upload profile image";

    public UploadButton(UUID userId) {
        super();
        memoryBuffer = new MemoryBuffer();
        this.userId = userId;
        init();
    }
    /**
     * @apiNote this method initializes the upload button and sets the listener. In addition, only a jpg or png image is accepted
     * @param userId
     * @param listener
     */
    private void init() {
        this.setReceiver(memoryBuffer);
        this.setAcceptedFileTypes("application/jpg", "application/png", ".jpg", ".png");
        this.setMaxFileSize(this.maxFileSizeInBytes);
        this.setUploadButton(new Button(this.description));
        addSucceededListener(clickListener());
    }
        
        /** 
         * @apiNote gets the image from the memory buffer and calls the methode to saves it in the database
         * @return ComponentEventListener<SucceededEvent>
         */
        private ComponentEventListener<SucceededEvent> clickListener() {
        return event -> {
            // Get information about the uploaded file
            InputStream fileData = memoryBuffer.getInputStream();
            String fileName = event.getFileName();
            long contentLength = event.getContentLength();
            String mimeType = event.getMIMEType();
            processFile(fileData, fileName, contentLength, mimeType);
    };
    }
        
        /** 
         * @apiNote saves the image in the database
         * @param fileData
         * @param fileName
         * @param contentLength
         * @param mimeType
         */
        private void processFile(InputStream fileData, String fileName, long contentLength, String mimeType) {
            //save file to database
            SerialBlob blob = null;
            try {
                blob = new SerialBlob(fileData.readAllBytes());
            } catch (SQLException | IOException e) {
                System.err.println(e.getMessage());
                Notification.show("error while saving the image");
            }
            if(blob != null) {
                entwicklerService.saveImage(blob, userId);
            }
        }
        public void setMaxFileSizeInBytes(int maxFileSizeInBytes) {
            this.maxFileSizeInBytes = maxFileSizeInBytes;
        }

        public void setDescription(String description) {
            this.description = description;
        }
}