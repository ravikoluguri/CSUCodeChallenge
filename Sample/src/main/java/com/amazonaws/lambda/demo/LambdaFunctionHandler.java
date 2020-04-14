package com.amazonaws.lambda.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class LambdaFunctionHandler implements RequestHandler<inputPOJO, String> {
	HashMap<String,String> keyhashMap;

	@Override
	public String handleRequest(inputPOJO input, Context context) {
		System.out.println(input.getlibrary_type());
		try {
			if(input.getlibrary_type().toString().equalsIgnoreCase("LearningAccess"))
			readFromS3("lal-tcal-rw", "security_key.txt");
			else if(input.getlibrary_type().toString().equalsIgnoreCase("TCAL"))
			{
			readFromS3("lal-tcal-rw", "TCAL_KeyReferences.txt");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String productID = input.getproductID();
		String getTelematicsVersion = input.gettelematics_Version();
		context.getLogger().log("Input: " + productID.concat("_").concat(getTelematicsVersion));
		String key = productID.concat("_").concat(getTelematicsVersion);
		if(this.keyhashMap.get(key)!=null)
			return this.keyhashMap.get(key);
		else
		{
			int telematicsVersion = Integer.parseInt(input.gettelematics_Version());
			if(getTelematicsVersion.equalsIgnoreCase("1001"))
			{
				context.getLogger().log("Output: "+ this.keyhashMap.get("1001"));
				return this.keyhashMap.get("1001");
			}
			else if(telematicsVersion>1001)
			{
				String productkey = productID.concat("_").concat("1001");
				if(this.keyhashMap.get(productkey)!=null)
				{
					context.getLogger().log("Output: "+ this.keyhashMap.get(productkey));
					return this.keyhashMap.get(productkey);
				}
				else
				{
					context.getLogger().log("Output: "+ this.keyhashMap.get("1001"));
					return this.keyhashMap.get("1001");
				}
			}
			else if(telematicsVersion<1000)
			{
				String productkey = productID.concat("_").concat("1000");
				if(this.keyhashMap.get(productkey)!=null)
				{
					context.getLogger().log("Output: "+ this.keyhashMap.get(productkey));
					return this.keyhashMap.get(productkey);
				}
				else
				{
					context.getLogger().log("Output: "+ this.keyhashMap.get("1000"));
					return this.keyhashMap.get("1000");
				}
			}
			else 
				return this.keyhashMap.get("1000");
		}
	}
	public void readFromS3(String bucketName, String key) throws IOException {
		this.keyhashMap = new HashMap<String,String>();
		String clientRegion = "us-west-2";
		AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
	    S3Object s3object = s3.getObject(new GetObjectRequest(
	            bucketName, key));
	    System.out.println(s3object.getObjectMetadata().getContentType());
	    System.out.println(s3object.getObjectMetadata().getContentLength());

	    BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
	    String line;
	    while((line = reader.readLine()) != null) {
	    	String[] splitString = line.split(",");
	    	this.keyhashMap.put(splitString[0], splitString[1]);
	      System.out.println(splitString[0]+splitString[1]);
	    }
	  }

}
