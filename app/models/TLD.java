package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


// data model to hold TLDs for validation.
@Entity
@Table(name = "tlds")
public class TLD {

    @Id
    @Column(name = "tld")
    private String domain;

    public void setDomain(String domain){
        this.domain = domain;
    }
    public String getDomain(){
        return domain;
    }

    public String toString() { // toString method because they are your friend.
        return "TLD: " + domain;
    }

    @Override
    public boolean equals(Object object){
        boolean isEqual = false;
        if(object == null){
            return false;
        }
        else if(object instanceof TLD){
            isEqual = (this.domain.equals(((TLD) object).domain));
        }else if (object instanceof String){
            isEqual = (this.domain.equals(object));
        }
        return isEqual;
    }

}
