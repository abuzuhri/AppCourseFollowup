package ae.ac.adec.coursefollowup.services;

import ae.ac.adec.coursefollowup.Application.myApplication;

/**
 * Created by Tareq on 03/12/2015.
 */
public class BusinessRoleExcption extends Exception {

    public BusinessRoleExcption(String Message){
        super(Message);
    }
    public BusinessRoleExcption(int ResourceID){
        super(getName(ResourceID));
    }
    private static String getName(int ResourceID) {
        String name = myApplication.getContext().getResources().getString(ResourceID);
        return name;
    }
}
