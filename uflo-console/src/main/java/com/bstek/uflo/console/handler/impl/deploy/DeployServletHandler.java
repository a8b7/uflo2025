/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.uflo.console.handler.impl.deploy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.bstek.uflo.console.handler.ServletHandler;
import com.bstek.uflo.deploy.validate.ProcessValidateException;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.utils.EnvironmentUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jacky.gao
 * @since 2016年12月8日
 */
public class DeployServletHandler implements ServletHandler{
	private ProcessService processService;
	private boolean debug;
	@Override
	@Transactional
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginUser=EnvironmentUtils.getEnvironment().getLoginUser();
		if(loginUser==null && !debug){
			throw new IllegalArgumentException("Current run mode is not debug.");
		}
		InputStream inputStream=null;
		ServletContext servletContext = req.getSession().getServletContext();

		File repository = (File) servletContext.getAttribute("jakarta.servlet.context.tempdir");

		DiskFileItemFactory factory = DiskFileItemFactory.builder().setFile(repository)
				.get();
		JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload = new JakartaServletFileUpload<>(factory);

		try {
			for(DiskFileItem item:upload.parseRequest(req)){
				if(item.getFieldName().equals("processFile")){
					inputStream=item.getInputStream();
				}
			}
		} catch (FileUploadException e) {
			throw new ServletException(e);
		}
		if(inputStream!=null){
			Map<String,Object> result=new HashMap<String,Object>();
			try{
				processService.deployProcess(inputStream);
				result.put("result", "success");
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("result", "fail");
				Throwable exception= getCause(ex);
				if(exception instanceof ProcessValidateException){
					ProcessValidateException e=(ProcessValidateException)exception;
					result.put("error", e.getMessage());
				}else{
					result.put("error", ex.getMessage());					
				}
			}
			ObjectMapper mapper=new ObjectMapper();
			resp.setContentType("text/json");  
			resp.setCharacterEncoding("UTF-8");  
			OutputStream out=resp.getOutputStream();
			try{
				mapper.writeValue(out,result);
			}finally{
				out.flush();
				out.close();
			}
		}else{
			throw new IllegalArgumentException("Can not found uflo process definition!");				
		}
	}
	
	private Throwable getCause(Throwable ex){
		if(ex.getCause()==null){
			return ex;
		}else{
			return getCause(ex.getCause());
		}
	}
	
	public void setProcessService(ProcessService processService) {
		this.processService = processService;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	@Override
	public String url() {
		return "/deploy";
	}
}
