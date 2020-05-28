/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metamug.plugin.sftp;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.metamug.entity.Attachment;
import com.metamug.entity.Request;
import com.metamug.entity.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author pc
 */
@RunWith(MockitoJUnitRunner.class)
public class SFTPTest {

    @Mock
    private Request masonRequest;

    public SFTPTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of upload method, of class SFTP.
     */
    @Test
    public void downloadTest() {

        try (SFTP sftp = new SFTP("data-server.metamug.net",
                "root", "Android@123", "/root/projects/covid-counter")) {

            InputStream is = sftp.download("pom.xml");
            String s = slurp(is, 1024);
            assertTrue(s.length() > 0);
            
            //sftp.upload(new File("test.txt"));
        } catch (JSchException ex) {
            Logger.getLogger(SFTPTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SftpException ex) {
            Logger.getLogger(SFTPTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SFTPTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SFTPTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String slurp(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try (Reader in = new InputStreamReader(is, "UTF-8")) {
            for (;;) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0) {
                    break;
                }
                out.append(buffer, 0, rsz);
            }
        } catch (UnsupportedEncodingException ex) {
            /* ... */
        } catch (IOException ex) {
            /* ... */
        }
        return out.toString();
    }

    @Test
    public void pluginTest() throws Exception {
        when(masonRequest.getParameter("file")).thenReturn("pom.xml");
        FileDownload fileDownloadPlugin = new FileDownload();
        
        Map<String, Object> args = new HashMap<>();
        args.put("host", "data-server.metamug.net");
        args.put("user", "root");
        args.put("password", "Android@123");
        args.put("dir", "/root/projects/covid-counter");
        
        Response response = fileDownloadPlugin.process(masonRequest, null, args);
        Attachment attachment = (Attachment) response.getPayload();
        InputStream inputStream = attachment.getStream();
        
        String s = slurp(inputStream, 2048);
        assertTrue(s.length() > 0);
        
    }

}
