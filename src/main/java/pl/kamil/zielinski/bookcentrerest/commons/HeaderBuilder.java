package pl.kamil.zielinski.bookcentrerest.commons;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

public class HeaderBuilder {
    private String ETag;
    private String Location;

    public HeaderBuilder setETag(String ETag){
        this.ETag = ETag;
        return this;
    }

    public HeaderBuilder setLocation(String Location){
        this.Location = Location;
        return this;
    }

    public MultiValueMap<String,String> build(){
        MultiValueMap<String,String> headers = new HttpHeaders();
        if(ETag!=null){
        headers.add("ETag", this.ETag);
        }
        if(Location!=null){
        headers.add("Location", this.Location);
        }
        return headers;
    }
}
