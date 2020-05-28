/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metamug.plugin.sftp;

import com.metamug.entity.Request;
import com.metamug.entity.Response;
import com.metamug.event.UploadEvent;
import com.metamug.event.UploadListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author pc
 */
public class FileUpload implements UploadListener {

    @Override
    public Response uploadPerformed(UploadEvent event, DataSource dataSource) {
        File uploadedFile = event.getUploadedFile();
        Response response = new Response(uploadedFile);
        return response;
    }
}
