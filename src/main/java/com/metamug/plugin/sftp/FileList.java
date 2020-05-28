/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metamug.plugin.sftp;

import com.metamug.entity.Request;
import com.metamug.entity.Response;
import com.metamug.exec.RequestProcessable;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author anishhirlekar
 */
public class FileList implements RequestProcessable {

    @Override
    public Response process(Request req, DataSource ds, Map<String, Object> args) throws Exception {
        try (SFTP sftp = new SFTP((String) args.get("host"),
                (String) args.get("user"), (String) args.get("password"), (String) args.get("dir"))) {
            Response response = new Response(sftp.getList((String)args.get("dir")));
            return response;
        }
    }
}