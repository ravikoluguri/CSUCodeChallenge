package com.amazonaws.lambda.demo;

public class inputPOJO {
	String productID;
    String telematics_Version;
    String library_type;
    
    public String getproductID() {
        return productID;
    }

    public void setproductID(String productID) {
        this.productID = productID;
    }

    public String gettelematics_Version() {
        return telematics_Version;
    }

    public void settelematics_Version(String telematics_Version) {
        this.telematics_Version = telematics_Version;
    }
    public String getlibrary_type() {
        return library_type;
    }
    public void setlibrary_type(String type) {
        this.library_type = type;
    }

    public inputPOJO(String productID, String telematics_Version, String type) {
        this.productID = productID;
        this.telematics_Version = telematics_Version;
        this.library_type=type;
    }

    public inputPOJO() {
    }
}
