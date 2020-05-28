/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metamug.plugin.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author pc
 */
public class SFTP implements AutoCloseable {
    JSch jsch = new JSch();
    ChannelSftp sftpChannel;
    Session session;

    public SFTP(String host, String user, String password) throws JSchException, SftpException {
        session = jsch.getSession(user, host);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();
        sftpChannel = (ChannelSftp) channel;
    }

    public SFTP(String host, String user, String password, String dir) throws JSchException, SftpException {
        this(host, user, password);
        sftpChannel.cd(dir);
    }

    public JSONObject getList(String dir) throws SftpException {
        Vector filelist = sftpChannel.ls(dir);
        JSONObject data = new JSONObject();
        data.put("directory", dir);
        
        JSONArray fileList = new JSONArray();
        for(int i=0; i<filelist.size();i++){
            LsEntry entry = (LsEntry) filelist.get(i);
            fileList.put(entry.getFilename());
        }
        data.put("fileList", fileList);
        return data;
    }
    
    public void upload(File file) throws FileNotFoundException, SftpException {
        sftpChannel.put(new FileInputStream(file), file.getName());
    }

    public void forceUpload(File file) throws FileNotFoundException, SftpException {
        sftpChannel.put(new FileInputStream(file), file.getName(), ChannelSftp.OVERWRITE);
    }

    public InputStream download(String file) throws SftpException {
        return sftpChannel.get(file);
    }

    public InputStream download(String file, String dir) throws SftpException {
        sftpChannel.cd(dir);
        return sftpChannel.get(file);
    }

    public void delete(String file) throws SftpException {
        sftpChannel.rm(file);
    }

    public void delete(String file, String dir) throws SftpException {
        sftpChannel.cd(dir);
        sftpChannel.rm(file);
    }

    @Override
    public void close() throws Exception {
        sftpChannel.exit();
        session.disconnect();
    }
}